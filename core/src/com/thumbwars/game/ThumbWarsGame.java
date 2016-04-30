package com.thumbwars.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.thumbwars.game.com.thumbwars.game.stages.GameStage;
import com.thumbwars.game.com.thumbwars.game.utils.WorldUtils;


public class ThumbWarsGame extends ApplicationAdapter{
    private static final String TAG = "ThumbWarsGame";

    public static final int TIMEOUT = 5000;
    public static final int TCP_PORT = 54555;
    public static final int UDP_PORT = 54777;
    public static final float NETWORK_TIME = 0.2f;

    public static final String IP = "IP";
    public static final String ISSERVER = "ISSERVER";
    public static final String PLAYERNAME = "PLAYERNAME";
    public static final String ONEPLAYER = "ONEPLAYER";
    public static final String TEAM = "TEAM";
    public static final String NUMBERPLAYER = "NUMBER";

    private Server server;
    private Client client;

    //private OrthographicCamera camera;
    //private Stage stage;
    private GameStage gameStage;
    private SpriteBatch batch;
    private Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;
    //private Texture personajeTextureJ2;
    //private Sprite personajeSpriteJ2;
    //private float personajeSpeed;
    public boolean isserver;
    public boolean oneplayer;
    public GameState gameState;
    //public Connection connection;
    public String playername;
    public int numberplayer;
    public int team;

    public Player player;

    public InetAddress ip;
    private float time;

    public ThumbWarsGame(){
        super();
    }

    public void start() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        setupGameState();
        if(oneplayer){
            return;
        }
        if(this.isserver){
            this.setServer(new Server());
            Kryo kryo = getServer().getKryo();
            registerClasses(kryo);
            try {
                this.getServer().addListener(new Listener() {
                    public void connected(Connection connection) {
                        Gdx.app.debug(TAG, "[SERVER] Connected: " + connection.toString());
                    }

                    public void received(Connection connection, Object object) {
                        Gdx.app.debug(TAG, "[SERVER] Received: " + connection.toString() + " " + object);
                        if (object instanceof String) {
                            Gdx.app.debug(TAG, "[SERVER] Received String");
                            String received = (String) object;
                            if (received.equals(Messages.JOIN_REQUEST)) {
                                Results currentState = new Results.INITIAL(ThumbWarsGame.this.gameState);
                                connection.sendTCP(currentState);
                            }
                        }
                        if (object instanceof Player){
                            Player objectPlayer = (Player)object;
                            if(encontrarPlayerId(objectPlayer.id) == null){
                                gameStage.addPlayerToWorld(objectPlayer);
                                gameState.players.add(objectPlayer);
                                ThumbWarsGame.this.getServer().sendToAllTCP(new Results.ADDPLAYER(ThumbWarsGame.this.gameState, objectPlayer));
                                Gdx.app.debug(TAG, "[SERVER] Received PlayerAdd " + objectPlayer);
                            }
                        }
                        if (object instanceof ActionMove) {
                            Gdx.app.debug(TAG, "[SERVER] Received ActionMove");
                            Results result = processMove((ActionMove) object);
                            //connection.sendTCP(result);
                        }
                        if (object instanceof ActionShoot) {
                            Gdx.app.debug(TAG, "[SERVER] Received ActionShoot");
                            Results result = processShoot((ActionShoot) object);
                            ThumbWarsGame.this.getServer().sendToAllTCP((ActionShoot)object);
                        }
                    }
                });
                this.getServer().bind(TCP_PORT, UDP_PORT);
                this.getServer().start();
            } catch (IOException e) {
                e.printStackTrace();
                Gdx.app.error(TAG, "[SERVER] Problemas al hacer server setup", e);
            }
        }else {
            this.setClient(new Client());
            Kryo kryo = this.getClient().getKryo();
            registerClasses(kryo);
            getClient().start();
            this.getClient().addListener(new Listener() {
                public void received(Connection connection, Object object) {
                    Gdx.app.debug(TAG, "Client Received: " + connection + " " + object);
                    if (object instanceof String && object.equals(Messages.JOIN_OK)) {
                        Gdx.app.debug(TAG, "Received String");
                    }
                    //Juego
                    if (object instanceof Results.INITIAL) {
                        Gdx.app.debug(TAG, "Received Results INITIAL");
                        processResultInitial((Results) object);
                    }
                    if (object instanceof Results.ADDPLAYER) {
                        Gdx.app.debug(TAG, "Received Add Player");
                        Results.ADDPLAYER ra = (Results.ADDPLAYER)object;
                        if (ra.player.id != numberplayer && !gameState.players.contains(ra.player)){
                            gameStage.addPlayerToWorld(ra.player);
                            gameState.players.add(ra.player);
                        }
                    }
                    if (object instanceof Results) {
                        Gdx.app.debug(TAG, "[CLIENT]Received Results");
                        processResult((Results) object);
                    }
                    if (object instanceof ActionShoot) {
                        Gdx.app.debug(TAG, "[CLIENT] Received ActionShoot");
                        processShoot((ActionShoot) object);
                    }
                }
            });
            try {
                InetAddress address = null;
                //address = this.getClient().discoverHost(TCP_PORT, 10000);
                Gdx.app.debug(TAG, "[CLIENT] Conectando al servidor : " + address + "|||" + ip);
                this.getClient().connect(TIMEOUT, address == null ? ip : address, TCP_PORT, UDP_PORT);
            } catch (IOException e) {
                e.printStackTrace();
                Gdx.app.error(TAG, "[CLIENT] Problemas al hacer client setup", e);
            }
            this.getClient().sendTCP(Messages.JOIN_REQUEST);
            this.getClient().sendTCP(player);

        }
    }

    public ThumbWarsGame(InetAddress ip, boolean isserver, boolean oneplayer, String playername, int numberplayer, int team) {
        this();
        this.ip = ip;
        this.isserver = isserver;
        this.oneplayer = oneplayer;
        this.playername = playername;
        this.numberplayer = numberplayer;
        this.team = team;
    }

    private void setupGameState() {
        Gdx.app.debug(TAG, "ID Numero de jugador:  " + numberplayer);
        this.gameState = new GameState();
        player = new Player(this.numberplayer,this.team,this.playername,4,2);
        gameState.players.add(player);

    }

    private void registerClasses(Kryo kryo) {
        kryo.register(String.class);
        kryo.register(Results.class);
        kryo.register(Actions.class);
        kryo.register(ActionMove.class);
        kryo.register(Results.FAIL.class);
        kryo.register(Results.OK.class);
        kryo.register(GameState.class);
        kryo.register(com.thumbwars.game.Results.INITIAL.class);
        kryo.register(com.thumbwars.game.Results.ADDPLAYER.class);
        kryo.register(com.thumbwars.game.Results.OK.class);
        kryo.register(com.thumbwars.game.Player.class);
        kryo.register(com.thumbwars.game.Objeto.class);
        kryo.register(java.util.List.class);
        kryo.register(java.util.ArrayList.class);
        kryo.register(ActionShoot.class);
    }

    void processResult(Results result) {
        this.gameState = result.gameState;
        int i = 0;
        for (Player a : gameState.players){
            Gdx.app.debug(TAG, "Received Results : " + i++ + " Player : " + a.toString());
        }
        Player p = encontrarPlayer(gameState);
        //eliminamos al mismo jugador para transformar a todos menos a si mismo
        gameState.players.remove(p);
        //this.gameState.players.add(player);
    }

    void processResultInitial(Results result) {
        //First process
        processResult(result);
    }

    public Player encontrarPlayer(GameState gs){
        Object[] ps = gs.players.toArray();
        for (int i = 0 ; i < ps.length; i++){
            Player p = (Player) ps[i];
            if(p.id == player.id){
                return p;
            }
        }
        return null;
    }

    public Player encontrarPlayerId(int id){
        Object[] ps = gameState.players.toArray();
        Gdx.app.debug(TAG, "Buscando " + id + " en : ");
        for (int i = 0 ; i < ps.length; i++){
            Player p = (Player) ps[i];
            Gdx.app.debug(TAG, "" + p.toString());
            if(p.id == id){
                return p;
            }
        }
        return null;
    }

    Results processMove(ActionMove move) {
        Player p = encontrarPlayerId(move.id);
        if (p != null){
            p.bodyX = move.bodyX;
            p.bodyY = move.bodyY;
            p.bodyAngle = move.bodyAngle;
        }
        Results ok = new Results.OK(this.gameState);
        Gdx.app.debug(TAG, "[SERVER] Received MOVE from :" + move.id + "|" + move.bodyX + "|" +move.bodyY + "|" + move.bodyAngle);
        return ok;
    }

    Results processShoot(ActionShoot shoot) {
        if (shoot.id != numberplayer) {
            gameStage.createMissile(shoot.x, shoot.y, shoot.x2, shoot.y2, shoot.vel);
            Gdx.app.debug(TAG, "[CLIENT] Missile created");
        }
        Results ok = new Results.OK(this.gameState);
        return ok;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        //Create camera
        //float aspectRatio = (float) Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
        //camera = new OrthographicCamera();
        //camera.setToOrtho(false, 10f * aspectRatio, 10f);
        //Viewport viewport = new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        //Create a Stage and add TouchPad
        //stage = new Stage(viewport, batch);
        gameStage = new GameStage(numberplayer,isserver,oneplayer,this);
        //Create a touchpad skin
        touchpadSkin = new Skin();
        //Set background image
        touchpadSkin.add("touchBackground", new Texture("data/touchBackground.png"));
        //Set knob image
        touchpadSkin.add("touchKnob", new Texture("data/touchKnob.png"));
        //Create TouchPad Style
        touchpadStyle = new Touchpad.TouchpadStyle();
        //Create Drawable's from TouchPad skin
        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");
        //Apply the Drawables to the TouchPad Style
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;

        touchpad = new Touchpad(5, touchpadStyle);
        //setBounds(x,y,width,height)
        touchpad.setBounds(15, 15, 100, 100);
        gameStage.addActor(touchpad);

        Gdx.input.setInputProcessor(gameStage);

        Object[] gs = gameState.players.toArray();
        for (Object obj :gs) {
            Player p = (Player)obj;
            Gdx.app.debug(TAG, "[CLIENT] Contando players : " + gs.length + " | " + p );
            if(p.id != numberplayer){
                gameStage.addPlayerToWorld(p);
            }
        }
    }

    public void transformAllPlayers(){
        Object[] ps = gameState.players.toArray(new Player[0]);
        for (int i = 0; i < ps.length; i++){
            Player pTemp = (Player) ps[i];
            if (pTemp.id == numberplayer){
            }else{
                int index =  gameStage.encontrarPlayer(pTemp.id);
                com.thumbwars.game.com.thumbwars.game.actors.Player p = gameStage.players.get(index);
                if(p != null){
                    p.body.setTransform(pTemp.bodyX,pTemp.bodyY,pTemp.bodyAngle);
                }
            }
        }

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.294f, 0.294f, 0.294f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //camera.update();
        if (this.isserver){
            //Gdx.app.debug(TAG, "[SERVER] Y Percent : " + touchpadJ1.getKnobPercentY());
            Vector2 PLAYERS_MOVING_LINEAR_IMPULSE = new Vector2(touchpad.getKnobPercentX(), 0);
            gameStage.player.body.applyLinearImpulse(PLAYERS_MOVING_LINEAR_IMPULSE, gameStage.player.body.getWorldCenter(), true);
            if (touchpad.getKnobPercentY() > 0.5f){
                gameStage.player.jump();
                //Gdx.app.debug(TAG, "[SERVER] Salto ejecutado..");
            }
            transformAllPlayers();
            if (!oneplayer){
            if (this.time > NETWORK_TIME) {
                this.time = 0;
                Gdx.app.debug(TAG, "[SERVER] Es tiempo de mandar nuevo game state");
                player.bodyX = gameStage.player.body.getPosition().x;
                player.bodyY = gameStage.player.body.getPosition().y;
                player.bodyAngle = gameStage.player.body.getPosition().angle();
                sendClientsGameState();
            } else {
                this.time += Gdx.graphics.getDeltaTime();
            }
            }
        }else{
            Vector2 PLAYERS_MOVING_LINEAR_IMPULSE = new Vector2(touchpad.getKnobPercentX(), 0);
            gameStage.player.body.applyLinearImpulse(PLAYERS_MOVING_LINEAR_IMPULSE, gameStage.player.body.getWorldCenter(), true);
            if (touchpad.getKnobPercentY() > 0.5f){
                gameStage.player.jump();
                //Gdx.app.debug(TAG, "[SERVER] Salto ejecutado..");
            }
            transformAllPlayers();
            if (this.time > NETWORK_TIME) {
                //Gdx.app.debug(TAG, "[CLIENT] Es tiempo de mandar nuevo game state");
                this.time = 0;
                //int id,float bodyX, float bodyY, float bodyAngle
                //if(Gdx.input.justTouched()){
                    ActionMove action = new ActionMove(numberplayer,this.gameStage.player.body.getPosition().x, this.gameStage.player.body.getPosition().y,this.gameStage.player.body.getPosition().angle());
                    if(!this.getClient().isConnected()){
                        Gdx.app.debug(TAG, "[CLIENT] Se perdio la conexion");
                        try {
                            Gdx.app.debug(TAG, "[CLIENT] reconectando");
                            this.getClient().reconnect();
                        } catch (IOException e) {
                            Gdx.app.error(TAG, "[CLIENT] problema reconectando", e);
                        }
                    }else{
                        sendMessage(action);
                    }
                //}
            } else {
                this.time += Gdx.graphics.getDeltaTime();
            }
        }
        gameStage.draw();
        gameStage.act(Gdx.graphics.getDeltaTime());
    }


    public synchronized Server getServer() {
        return server;
    }

    public synchronized  void setServer(Server server) {
        this.server = server;
    }

    public synchronized Client getClient() {
        return client;
    }

    public synchronized  void setClient(Client client) {
        this.client = client;
    }

    public void sendClientsActionShoot(float x, float y, float x2, float y2, float vel){
        ActionShoot action = new ActionShoot(numberplayer,x, y, x2, y2,vel);
        this.getServer().sendToAllTCP(action);
    }

    public void sendServerActionShoot(float x, float y, float x2, float y2 , float vel) {
        ActionShoot action = new ActionShoot(numberplayer,x, y, x2, y2,vel);
        sendMessage(action);
    }

    private void sendMessage(Actions action) {
        this.getClient().sendTCP(action);
    }

    private void sendClientsGameState() {
        Results.OK ok = new Results.OK(this.gameState);
        this.getServer().sendToAllTCP(ok);
    }
}

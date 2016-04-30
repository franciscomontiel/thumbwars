package com.thumbwars.game.com.thumbwars.game.stages;

/**
 * Created by Paco on 03/11/2015.
 */
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.thumbwars.game.ThumbWarsGame;
import com.thumbwars.game.com.thumbwars.game.actors.Background;
import com.thumbwars.game.com.thumbwars.game.actors.Ground;
import com.thumbwars.game.com.thumbwars.game.actors.Missile;
import com.thumbwars.game.com.thumbwars.game.actors.Player;
import com.thumbwars.game.com.thumbwars.game.actors.Wall;
import com.thumbwars.game.com.thumbwars.game.box2d.MissileUserData;
import com.thumbwars.game.com.thumbwars.game.box2d.PlayerUserData;
import com.thumbwars.game.com.thumbwars.game.utils.BodyUtils;
import com.thumbwars.game.com.thumbwars.game.utils.Constants;
import com.thumbwars.game.com.thumbwars.game.utils.WorldUtils;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class GameStage extends Stage implements ContactListener {

    // This will be our viewport measurements while working with the debug renderer
    private static final int VIEWPORT_WIDTH = Constants.APP_WIDTH;
    private static final int VIEWPORT_HEIGHT = Constants.APP_HEIGHT;

    public boolean isserver;
    public boolean oneplayer;
    public ThumbWarsGame thumbWarsGame;

    public World world;
    public Ground ground;
    public Wall groundLeft;
    public Wall groundRight;
    private Missile missile;
    public List<Missile> missiles = new ArrayList<Missile>();
    Rectangle joystickJ1Area =new Rectangle( 15, 15, 100, 100);
    //Botones
    TextButton button;
    TextButton.TextButtonStyle textButtonStyle;
    BitmapFont font;
    Skin skinGravedad;

    public int id;
    public Player player;
    public List<Player> players = new ArrayList<Player>();

    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;

    private OrthographicCamera camera;
    private Box2DDebugRenderer renderer;

    private Vector3 touchPoint;

    private static final String TAG = "ThumbWarsGame";

    public GameStage(int id, boolean isserver, boolean oneplayer, ThumbWarsGame thumbWarsGame) {
        super(new ScalingViewport(Scaling.stretch, VIEWPORT_WIDTH, VIEWPORT_HEIGHT,
                new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT)));
        setUpWorld();
        setupCamera();
        setupTouchControlAreas();
        this.isserver = isserver;
        this.oneplayer = oneplayer;
        this.thumbWarsGame = thumbWarsGame;
        this.id = id;
        //renderer = new Box2DDebugRenderer();
    }

    private void setUpWorld() {
        world = WorldUtils.createWorld();
        world.setContactListener(this);
        setUpBackground();
        setUpGround();
        setUpPlayer1();
    }
    private void setUpGround() {
        ground = new Ground(WorldUtils.createGround(world));
        groundLeft = new Wall(WorldUtils.createWallLeft(world));
        groundRight = new Wall(WorldUtils.createWallRight(world));
        addActor(ground);
        addActor(groundLeft);
        addActor(groundRight);
    }

    private void setUpPlayer1() {
        player = new Player(id,WorldUtils.createPlayer1(world));
        addActor(player);
        players.add(player);
    }

    public void addPlayerToWorld(com.thumbwars.game.Player jugador){
        Player p = new Player(jugador.id, WorldUtils.createPlayer(world,jugador.bodyX,jugador.bodyY));
        addActor(p);
        players.add(p);
    }

    public int encontrarPlayer(int id){
        Object[] p = this.players.toArray();
        for (int i = 0; i < p.length ; i++){
            Player player = (Player)p[i];
            if(player.id == id){
                return i;
            }
        }
        return -1;
    }

    private void setupCamera() {
        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
        camera.update();
    }

    private void setupTouchControlAreas() {
        touchPoint = new Vector3();
        font = new BitmapFont();
        textButtonStyle = new TextButton.TextButtonStyle();
        skinGravedad = new Skin();
        skinGravedad.add("fondo", new Texture("buttons/gravedad.png"));
        skinGravedad.add("fondoDown", new Texture("buttons/gravedadDown.png"));
        textButtonStyle.up = skinGravedad.getDrawable("fondo");
        textButtonStyle.down = skinGravedad.getDrawable("fondoDown");
        textButtonStyle.font = font;
        button = new TextButton("Gravedad", textButtonStyle);
        button.setBounds(20, 400, 60, 60); //hardcode por pruebas
        button.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //Gdx.app.debug(TAG, "[SERVER] Gravedad Activado!");
                Object[] arr = missiles.toArray();
                for (Object m : arr) {
                    Missile mTemp = (Missile) m;
                    mTemp.body.setGravityScale(Constants.PLAYERS_GRAVITY_SCALE + 2);//Fuerza alterada de la gravedad
                }
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //Gdx.app.debug(TAG, "[SERVER] Gravedad desactivado!");
            }
        });
        addActor(button);
    }

    private void setUpBackground() {
        addActor(new Background());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        Array<Body> bodies = new Array<Body>(world.getBodyCount());
        world.getBodies(bodies);

        for (Body body : bodies) {
            update(body);
        }
        // Fixed timestep
        accumulator += delta;

        while (accumulator >= delta) {
            world.step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }

        //TODO: Implement interpolation
    }

    private void update(Body body) {
        if (BodyUtils.bodyToRemove(body)) {
            world.destroyBody(body);
        }
    }

    //Eliminar debuggerRenderer
    @Override
    public void draw() {
        super.draw();
        //renderer.render(world, camera.combined);
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        // Need to get the actual coordinates
        translateScreenToWorldCoordinates(x, y);
        if (joystickJ1Area.contains(touchPoint.x,touchPoint.y)){
            return super.touchDown(x, y, pointer, button);
        }
        if (isserver){
            if (!oneplayer){
                thumbWarsGame.sendClientsActionShoot(touchPoint.x, touchPoint.y, player.body.getPosition().x, player.body.getPosition().y + 2, 15f);
            }
            createMissile(touchPoint.x, touchPoint.y, player.body.getPosition().x, player.body.getPosition().y + 2 , 15f);
        }else{
            thumbWarsGame.sendServerActionShoot(touchPoint.x, touchPoint.y, player.body.getPosition().x, player.body.getPosition().y + 2 , 15f);
            createMissile(touchPoint.x, touchPoint.y, player.body.getPosition().x, player.body.getPosition().y + 2 , 15f);
        }

        return super.touchDown(x, y, pointer, button);
    }

    public void createMissile(float xPoint, float yPoint, float xOrigin, float yOrigin, float vel){
        missile = new Missile(WorldUtils.createMissile(world,xPoint/32,yPoint/32,xOrigin, yOrigin , vel));
        missiles.add(missile);
        addActor(missile);
    }

    @Override
    public void beginContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();
        if ((BodyUtils.bodyIsPlayer(a) && BodyUtils.bodyIsGround(b)) ||
                (BodyUtils.bodyIsGround(a) && BodyUtils.bodyIsPlayer(b))) {
            if(BodyUtils.bodyIsPlayer(a)){
                int i = encontrarPlayer(((PlayerUserData) a.getUserData()).getId());
                Player p = players.get(i);
                p.landed();
            }else{
                int i = encontrarPlayer(((PlayerUserData) b.getUserData()).getId());
                Player p = players.get(i);
                p.landed();
            }

        }
        if ((BodyUtils.bodyIsMissile(a) && BodyUtils.bodyIsPlayer(b)) ||
                (BodyUtils.bodyIsPlayer(a) && BodyUtils.bodyIsMissile(b))) {
            if(BodyUtils.bodyIsMissile(a)){
                ((MissileUserData) a.getUserData()).remove =true;
            }else{
                ((MissileUserData) b.getUserData()).remove =true;
            }
        }
        if ((BodyUtils.bodyIsMissile(a) && BodyUtils.bodyIsMissile(b))) {
            ((MissileUserData) a.getUserData()).remove =true;
            ((MissileUserData) b.getUserData()).remove =true;
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    /**
     * Helper function to get the actual coordinates in my world
     * @param x
     * @param y
     */
    private void translateScreenToWorldCoordinates(int x, int y) {
        getCamera().unproject(touchPoint.set(x, y, 0));
        //Vector3 v = getCamera().unproject(touchPoint.set(x, y, 0), x, y, Constants.APP_WIDTH,Constants.APP_HEIGHT);
        //Gdx.app.debug(TAG, "[SERVER] Punto presionado CONVERTIDO: " + touchPoint.x + "|||" + touchPoint.y);
        //return v;
    }
}
package com.thumbwars.game.com.thumbwars.game.utils;

/**
 * Created by Paco on 03/11/2015.
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.thumbwars.game.com.thumbwars.game.box2d.GroundUserData;
import com.thumbwars.game.com.thumbwars.game.box2d.MissileUserData;
import com.thumbwars.game.com.thumbwars.game.box2d.PlayerOneUserData;
import com.thumbwars.game.com.thumbwars.game.box2d.PlayerTwoUserData;
import com.thumbwars.game.com.thumbwars.game.box2d.PlayerUserData;
import com.thumbwars.game.com.thumbwars.game.box2d.WallUserData;

public class WorldUtils {

    public static World createWorld() {
        return new World(Constants.WORLD_GRAVITY, true);
    }

    public static Body createGround(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(new Vector2(Constants.GROUND_X, Constants.GROUND_Y));
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.GROUND_WIDTH / 2, Constants.GROUND_HEIGHT / 2);
        body.createFixture(shape, Constants.GROUND_DENSITY);
        body.setUserData(new GroundUserData(Constants.GROUND_WIDTH,Constants.GROUND_HEIGHT));
        shape.dispose();
        //Pared Izq
        return body;
    }
    public static Body createWallLeft(World world){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(new Vector2(Constants.WALL_LEFT_X, Constants.WALL_LEFT_Y));
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1 / 2, 40 / 2);
        body.createFixture(shape, Constants.GROUND_DENSITY);
        body.setUserData(new WallUserData());
        shape.dispose();
        return body;
    }

    public static Body createWallRight(World world){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(new Vector2(Constants.WALL_RIGHT_X, Constants.WALL_RIGHT_Y));
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.WALL_WIDTH / 2, Constants.WALL_HEIGHT / 2);
        body.createFixture(shape, Constants.GROUND_DENSITY);
        body.setUserData(new WallUserData());
        shape.dispose();
        return body;
    }

    public static Body createPlayer1(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(Constants.PLAYER1_X, Constants.PLAYER1_Y));
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.PLAYERS_WIDTH / 2, Constants.PLAYERS_HEIGHT / 2);
        Body body = world.createBody(bodyDef);
        body.setGravityScale(Constants.PLAYERS_GRAVITY_SCALE); //Caida del pj
        body.createFixture(shape, Constants.PLAYERS_DENSITY);
        body.resetMassData();
        body.setUserData(new PlayerUserData(Constants.PLAYERS_WIDTH, Constants.PLAYERS_HEIGHT));
        shape.dispose();
        return body;
    }

    public static Body createPlayer(World world,float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(x, y));
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.PLAYERS_WIDTH / 2, Constants.PLAYERS_HEIGHT / 2);
        Body body = world.createBody(bodyDef);
        body.setGravityScale(Constants.PLAYERS_GRAVITY_SCALE); //Caida del pj
        body.createFixture(shape, Constants.PLAYERS_DENSITY);
        body.resetMassData();
        body.setUserData(new PlayerUserData(Constants.PLAYERS_WIDTH, Constants.PLAYERS_HEIGHT));
        shape.dispose();
        return body;
    }

    public static Body createMissile(World world,float x, float y,float origenX,float origenY , float vel) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        //Gdx.app.debug("Thumbwars", "[SERVER] P1 Pos: " + origenX + "|||" + origenY);
        bodyDef.position.set(new Vector2(origenX , origenY ));
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.MISSILE_WIDTH / 2, Constants.MISSILE_HEIGHT / 2);
        Body body = world.createBody(bodyDef);
        body.createFixture(shape, Constants.PLAYERS_DENSITY);
        body.resetMassData();
        body.setUserData(new MissileUserData(Constants.MISSILE_WIDTH, Constants.MISSILE_HEIGHT));

        Vector2 vec = new Vector2(x - bodyDef.position.x, y - bodyDef.position.y);
        body.setLinearVelocity(vec.nor().scl(vel));

        //body.applyLinearImpulse(((MissileUserData)body.getUserData()).getShootLinearImpulse(), body.getWorldCenter(), true);
        shape.dispose();
        return body;
    }
}

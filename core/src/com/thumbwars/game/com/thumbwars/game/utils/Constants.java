package com.thumbwars.game.com.thumbwars.game.utils;

/**
 * Created by Paco on 03/11/2015.
 */
import com.badlogic.gdx.math.Vector2;

public class Constants {

    public static final int APP_WIDTH = 800;
    public static final int APP_HEIGHT = 480;

    public static final float WORLD_TO_SCREEN = 32;

    public static final Vector2 WORLD_GRAVITY = new Vector2(0, -10);

    public static final float GROUND_X = 0;
    public static final float GROUND_Y = 0;
    public static final float GROUND_WIDTH = 50f;
    public static final float GROUND_HEIGHT = 4f;
    public static final float GROUND_DENSITY = 0f;

    public static final float WALL_LEFT_X = 0f;
    public static final float WALL_LEFT_Y = 4f;

    public static final float WALL_RIGHT_X = 26f;
    public static final float WALL_RIGHT_Y= 4f;

    public static final float WALL_WIDTH = 2f;
    public static final float WALL_HEIGHT = 40f;

    public static final float PLAYER1_X = 2;
    public static final float PLAYER1_Y = GROUND_Y + GROUND_HEIGHT;

    public static final float PLAYER2_X = 10;
    public static final float PLAYER2_Y = GROUND_Y + GROUND_HEIGHT;

    public static final float PLAYERS_WIDTH = 2f;
    public static final float PLAYERS_HEIGHT = 2f;

    public static final float MISSILE_WIDTH = 1f;
    public static final float MISSILE_HEIGHT = 1f;


    public static float PLAYERS_DENSITY = 0.5f;
    public static final float PLAYERS_GRAVITY_SCALE = 3f;
    public static final Vector2 PLAYERS_JUMPING_LINEAR_IMPULSE = new Vector2(0, 35f);

    public static final Vector2 MISSILE_LINEAR_IMPULSE = new Vector2(5f, 5f);

    public static final String GROUND_IMAGE_PATH = "stages/ground.png";

}
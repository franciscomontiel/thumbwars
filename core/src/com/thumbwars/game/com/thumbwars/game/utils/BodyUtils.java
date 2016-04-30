package com.thumbwars.game.com.thumbwars.game.utils;

import com.badlogic.gdx.physics.box2d.Body;
import com.thumbwars.game.com.thumbwars.game.box2d.UserData;
import com.thumbwars.game.com.thumbwars.game.enums.UserDataType;

/**
 * Created by Paco on 03/11/2015.
 */
public class BodyUtils {

    public static boolean bodyIsPlayerOne(Body body) {
        UserData userData = (UserData) body.getUserData();
        return userData != null && userData.getUserDataType() == UserDataType.PLAYER1;
    }

    public static boolean bodyIsPlayer(Body body) {
        UserData userData = (UserData) body.getUserData();
        return userData != null && userData.getUserDataType() == UserDataType.PLAYER;
    }

    public static boolean bodyIsPlayerTwo(Body body) {
        UserData userData = (UserData) body.getUserData();
        return userData != null && userData.getUserDataType() == UserDataType.PLAYER2;
    }

    public static boolean bodyIsGround(Body body) {
        UserData userData = (UserData) body.getUserData();
        return userData != null && userData.getUserDataType() == UserDataType.GROUND;
    }

    public static boolean bodyIsWall(Body body) {
        UserData userData = (UserData) body.getUserData();
        return userData != null && userData.getUserDataType() == UserDataType.WALL;
    }

    public static boolean bodyIsMissile(Body body) {
        UserData userData = (UserData) body.getUserData();
        return userData != null && userData.getUserDataType() == UserDataType.MISSILE;
    }

    //Error aqui VVV
    public static boolean bodyToRemove(Body body) {
        UserData userData = (UserData) body.getUserData();
        if (userData == null || userData.getUserDataType() == null){
            return false;
        }
        switch (userData.getUserDataType()) {
            case PLAYER1:
                return userData.remove;
            case PLAYER2:
                return userData.remove;
            case MISSILE:
                return userData.remove;
            case GROUND:
                return userData.remove;
            case WALL:
                return userData.remove;
        }
        return false;
    }
}
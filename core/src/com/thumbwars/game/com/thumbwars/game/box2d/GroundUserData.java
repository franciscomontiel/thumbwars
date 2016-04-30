package com.thumbwars.game.com.thumbwars.game.box2d;

import com.thumbwars.game.com.thumbwars.game.enums.UserDataType;

/**
 * Created by Paco on 03/11/2015.
 */
public class GroundUserData extends UserData {

    public GroundUserData(float width, float height) {
        super(width, height);
        userDataType = UserDataType.GROUND;
    }

}
package com.thumbwars.game.com.thumbwars.game.box2d;

import com.thumbwars.game.com.thumbwars.game.enums.UserDataType;

/**
 * Created by Paco on 04/11/2015.
 */
public class WallUserData extends UserData {

    public WallUserData() {
        super();
        userDataType = UserDataType.WALL;
    }

}
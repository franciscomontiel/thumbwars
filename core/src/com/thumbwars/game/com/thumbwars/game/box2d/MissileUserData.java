package com.thumbwars.game.com.thumbwars.game.box2d;

import com.badlogic.gdx.math.Vector2;
import com.thumbwars.game.com.thumbwars.game.enums.UserDataType;
import com.thumbwars.game.com.thumbwars.game.utils.Constants;

/**
 * Created by Paco on 04/11/2015.
 */
public class MissileUserData extends UserData {

    private Vector2 shootLinearImpulse;

    public MissileUserData(float width, float height) {
        super(width, height);
        shootLinearImpulse = Constants.MISSILE_LINEAR_IMPULSE;
        userDataType = UserDataType.MISSILE;
    }

    public Vector2 getShootLinearImpulse() {
        return shootLinearImpulse;
    }

    public void setShootLinearImpulse(Vector2 jumpingLinearImpulse) {
        this.shootLinearImpulse = jumpingLinearImpulse;
    }

}
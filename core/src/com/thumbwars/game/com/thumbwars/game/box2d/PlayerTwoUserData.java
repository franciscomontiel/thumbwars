package com.thumbwars.game.com.thumbwars.game.box2d;

import com.badlogic.gdx.math.Vector2;
import com.thumbwars.game.com.thumbwars.game.enums.UserDataType;
import com.thumbwars.game.com.thumbwars.game.utils.Constants;

/**
 * Created by Paco on 03/11/2015.
 */
public class PlayerTwoUserData extends UserData {

    private Vector2 jumpingLinearImpulse;

    public PlayerTwoUserData(float width, float height) {
        super(width, height);
        jumpingLinearImpulse = Constants.PLAYERS_JUMPING_LINEAR_IMPULSE;
        userDataType = UserDataType.PLAYER2;
    }

    public Vector2 getJumpingLinearImpulse() {
        return jumpingLinearImpulse;
    }

    public void setJumpingLinearImpulse(Vector2 jumpingLinearImpulse) {
        this.jumpingLinearImpulse = jumpingLinearImpulse;
    }

}

package com.thumbwars.game.com.thumbwars.game.box2d;

import com.badlogic.gdx.math.Vector2;
import com.thumbwars.game.com.thumbwars.game.enums.UserDataType;
import com.thumbwars.game.com.thumbwars.game.utils.Constants;

/**
 * Created by Paco on 11/11/2015.
 */
public class PlayerUserData  extends UserData {

    private Vector2 jumpingLinearImpulse;
    private int id;

    public PlayerUserData(float width, float height) {
        super(width, height);
        jumpingLinearImpulse = Constants.PLAYERS_JUMPING_LINEAR_IMPULSE;
        userDataType = UserDataType.PLAYER;
    }

    public Vector2 getJumpingLinearImpulse() {
        return jumpingLinearImpulse;
    }

    public void setJumpingLinearImpulse(Vector2 jumpingLinearImpulse) {
        this.jumpingLinearImpulse = jumpingLinearImpulse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
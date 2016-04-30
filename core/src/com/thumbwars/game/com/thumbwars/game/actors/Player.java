package com.thumbwars.game.com.thumbwars.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.thumbwars.game.com.thumbwars.game.box2d.PlayerOneUserData;
import com.thumbwars.game.com.thumbwars.game.box2d.PlayerUserData;

/**
 * Created by Paco on 11/11/2015.
 */
public class Player extends GameActor {

    private final TextureRegion textureRegion;
    private boolean jumping;
    public int id;

    public Player(int id, Body body) {
        super(body);
        this.id = id;
        textureRegion = new TextureRegion(new Texture(Gdx.files.internal("data/apacheIzquierda.PNG")));
    }

    @Override
    public PlayerUserData getUserData() {
        return (PlayerUserData) userData;
    }

    public void jump() {

        if (!jumping) {
            body.applyLinearImpulse(getUserData().getJumpingLinearImpulse(), body.getWorldCenter(), true);
            jumping = true;
        }

    }

    public void landed() {
        jumping = false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(textureRegion, screenRectangle.x, screenRectangle.y, screenRectangle.getWidth(),
                screenRectangle.getHeight());
    }
}
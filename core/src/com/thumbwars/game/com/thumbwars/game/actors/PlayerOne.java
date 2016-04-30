package com.thumbwars.game.com.thumbwars.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.thumbwars.game.com.thumbwars.game.box2d.PlayerOneUserData;
import com.thumbwars.game.com.thumbwars.game.utils.Constants;

/**
 * Created by Paco on 03/11/2015.
 */
public class PlayerOne  extends GameActor {

    private final TextureRegion textureRegion;
    private boolean jumping;

    public PlayerOne(Body body) {
        super(body);
        textureRegion = new TextureRegion(new Texture(Gdx.files.internal("data/apacheIzquierda.PNG")));
    }

    @Override
    public PlayerOneUserData getUserData() {
        return (PlayerOneUserData) userData;
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
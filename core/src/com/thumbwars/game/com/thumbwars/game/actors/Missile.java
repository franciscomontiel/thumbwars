package com.thumbwars.game.com.thumbwars.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.thumbwars.game.com.thumbwars.game.box2d.MissileUserData;

/**
 * Created by Paco on 04/11/2015.
 */
public class Missile extends GameActor {

    private final TextureRegion textureRegion;

    public Missile(Body body) {
        super(body);
        textureRegion = new TextureRegion(new Texture(Gdx.files.internal("data/shuriken.png")));
    }

    @Override
    public MissileUserData getUserData() {
        return (MissileUserData) userData;
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(textureRegion, screenRectangle.x, screenRectangle.y, screenRectangle.getWidth(),
                screenRectangle.getHeight());

    }
}
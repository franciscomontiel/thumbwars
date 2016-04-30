package com.thumbwars.game.com.thumbwars.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.thumbwars.game.com.thumbwars.game.box2d.GroundUserData;
import com.thumbwars.game.com.thumbwars.game.utils.Constants;

/**
 * Created by Paco on 03/11/2015.
 */
public class Ground extends GameActor {

    private final TextureRegion textureRegion;
    private Rectangle textureRegionBounds;

    public Ground(Body body) {
        super(body);
        textureRegion = new TextureRegion(new Texture(Gdx.files.internal(Constants.GROUND_IMAGE_PATH)));
        textureRegionBounds = new Rectangle(0, 0, getUserData().getWidth(),
                getUserData().getHeight());
    }

    @Override
    public GroundUserData getUserData() {
        return (GroundUserData) userData;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(textureRegion, textureRegionBounds.x, screenRectangle.y, screenRectangle.getWidth(),
                screenRectangle.getHeight());

    }

}
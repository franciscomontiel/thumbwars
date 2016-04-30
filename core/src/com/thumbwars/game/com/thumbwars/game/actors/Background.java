package com.thumbwars.game.com.thumbwars.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.thumbwars.game.com.thumbwars.game.utils.Constants;

/**
 * Created by Paco on 08/11/2015.
 */
public class Background extends Actor {

    private final TextureRegion textureRegion;
    private Rectangle textureRegionBounds;

    public Background() {
        textureRegion = new TextureRegion(new Texture(Gdx.files.internal("stages/escenarioB.png")));
        //textureRegionBounds = new Rectangle(0 - Constants.APP_WIDTH / 2, 0, Constants.APP_WIDTH, Constants.APP_HEIGHT);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(textureRegion, 0, 0, Constants.APP_WIDTH, Constants.APP_HEIGHT);
    }

}

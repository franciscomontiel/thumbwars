package com.thumbwars.game.com.thumbwars.game.actors;

import com.badlogic.gdx.physics.box2d.Body;
import com.thumbwars.game.com.thumbwars.game.box2d.GroundUserData;

/**
 * Created by Paco on 04/11/2015.
 */
public class Wall extends GameActor {

    public Wall(Body body) {
        super(body);
    }

    @Override
    public GroundUserData getUserData() {
        return (GroundUserData) userData;
    }

}
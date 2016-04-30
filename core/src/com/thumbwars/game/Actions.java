package com.thumbwars.game;

/**
 * Created by Paco on 22/10/2015.
 */
public class Actions {
    public int id;

    public Actions() {
    }
}

class ActionMove extends Actions {

    public float bodyX;
    public float bodyY;
    public float bodyAngle;

    public ActionMove() {
        super();
    }

    public ActionMove(int id,float bodyX, float bodyY, float bodyAngle) {
        this.id = id;
        this.bodyX = bodyX;
        this.bodyY = bodyY;
        this.bodyAngle = bodyAngle;
    }
}

class ActionShoot extends Actions {

    float x,y;
    float x2,y2;
    float vel;

    public  ActionShoot(){
        super();
    }

    public ActionShoot(int id, float x,float y, float x2, float y2, float vel){
        this.id = id;
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
        this.vel = vel;
    }
}
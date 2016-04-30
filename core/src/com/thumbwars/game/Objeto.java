package com.thumbwars.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;

/**
 * Created by Paco on 29/10/2015.
 */
public class Objeto {
    public Sprite sprite;//contiene x,y,alto,ancho...
    public boolean isDerechaDir;
    public boolean isP1;
    public int accel;
    public Rectangle rec;

    public Objeto(Sprite sprite, boolean isDerechaDir, int accel, boolean isP1) {
        this.sprite = sprite;
        this.isDerechaDir = isDerechaDir;
        this.accel = accel;
        this.rec = new Rectangle(sprite.getX(),sprite.getY(),sprite.getWidth(),sprite.getHeight());
    }
    public void setX(float x){
        this.sprite.setX(x);
        this.rec.setX(x);
    }

    public boolean checkCollision(List<Objeto> objetosActivos) {
        for (Objeto obj: objetosActivos){
            if (this != obj && !this.equals(obj)){
                obj.rec.overlaps(this.rec);
                return true;
            }
        }
        return false;
    }
    //public abstract void pintarse();
}

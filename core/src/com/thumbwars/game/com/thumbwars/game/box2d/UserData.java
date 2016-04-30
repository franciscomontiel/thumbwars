package com.thumbwars.game.com.thumbwars.game.box2d;

import com.thumbwars.game.com.thumbwars.game.enums.UserDataType;

/**
 * Created by Paco on 03/11/2015.
 */
public abstract class UserData {

    protected UserDataType userDataType;
    protected float width;
    protected float height;
    public boolean remove = false;

    public UserData() {

    }

    public UserData(float width, float height) {
        this.width = width;
        this.height = height;
    }
    public UserDataType getUserDataType() {
        return userDataType;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
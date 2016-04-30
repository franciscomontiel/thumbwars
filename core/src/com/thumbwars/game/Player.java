package com.thumbwars.game;

/**
 * Created by luis on 10/28/15.
 */
public class Player {

    public int id;
    public int team;
    public String name;
    public int hp = 5;
    public int energia = 10;

    public float bodyX;
    public float bodyY;
    public float bodyAngle;

    public Player(){

    }

    public Player(int id, int team, String name, float bodyX, float bodyY) {
        this.id = id;
        this.team = team;
        this.name = name;
        this.bodyX = bodyX;
        this.bodyY = bodyY;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", team=" + team +
                ", name='" + name + '\'' +
                ", hp=" + hp +
                ", energia=" + energia +
                ", bodyX=" + bodyX +
                ", bodyY=" + bodyY +
                ", bodyAngle=" + bodyAngle +
                '}';
    }
}

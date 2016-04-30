package com.thumbwars.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paco on 22/10/2015.
 */
public class GameState {

    public List<Player> players;

    public GameState(){
        players = new ArrayList<Player>();
    }
    public GameState(List<Player> players) {
        this.players = players;
    }

}

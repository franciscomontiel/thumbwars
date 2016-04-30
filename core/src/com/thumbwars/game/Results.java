package com.thumbwars.game;

/**
 * Created by Paco on 22/10/2015.
 */
public class Results {
    GameState gameState;
    public Results(){

    }

    public Results(GameState gameState) {
        this.gameState = gameState;
    }

    public static class OK extends Results {
        public OK(GameState gameState){
            super(gameState);
        }
        public OK(){

        }
    }

    public static class FAIL extends Results {
        public FAIL(GameState gameState){
            super(gameState);
        }
        public FAIL(){

        }
    }

    public static class INITIAL extends Results {
        public  INITIAL(){
        }

        public INITIAL(GameState gameState){
            super(gameState);
        }
    }

    public static class ADDPLAYER extends Results {

        public Player player;

        public  ADDPLAYER(){
        }

        public ADDPLAYER(GameState gameState,Player player){
            super(gameState);
            this.player = player;
        }
    }
}


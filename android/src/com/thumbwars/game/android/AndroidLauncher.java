package com.thumbwars.game.android;

import android.os.Bundle;


import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.thumbwars.game.ThumbWarsGame;

import java.net.InetAddress;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = this.getIntent().getExtras();
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		InetAddress ip = (InetAddress)bundle.get(ThumbWarsGame.IP);
		boolean isserver = bundle.getBoolean(ThumbWarsGame.ISSERVER);
		boolean oneplayer = bundle.getBoolean(ThumbWarsGame.ONEPLAYER);
		int numberplayer = bundle.getInt(ThumbWarsGame.NUMBERPLAYER);
		String playername = bundle.getString(ThumbWarsGame.PLAYERNAME);
		int team = bundle.getInt(ThumbWarsGame.TEAM);
		//config.useGLSurfaceView20API18 = false;
		ThumbWarsGame game = new ThumbWarsGame(ip, isserver,oneplayer,playername,numberplayer,team);
		initialize(game, config);
		game.start();

	}
}


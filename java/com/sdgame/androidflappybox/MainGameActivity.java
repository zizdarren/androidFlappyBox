package com.sdgame.androidflappybox;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainGameActivity extends Activity {

    GameView gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set Fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Set No title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        gv = new GameView(this);

        setContentView(gv);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gv.pause();
    }

    //running the game when activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        gv.resume();
    }
}

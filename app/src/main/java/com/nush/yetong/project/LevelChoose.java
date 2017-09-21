package com.nush.yetong.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;


import java.util.ArrayList;

/**
 * Created by Yetong on 2017/2/19.
 */

public class LevelChoose extends Activity {

    ImageButton ib1,ib2,ib3,ib4;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_page);
        context = getApplicationContext();
        DrawingView.stopTheThread();

        ib1 = (ImageButton) findViewById(R.id.animeGame);
        ib2 = (ImageButton) findViewById(R.id.Animal);
        ib3 = (ImageButton) findViewById(R.id.Null);
        ib4 = (ImageButton) findViewById(R.id.nill);

        checkClear();
    }

    public void checkClear() {
        SharedPreferences sp = getSharedPreferences("clear", 0);

        if(sp.getString("Act1", null) != null){
            ib1.setImageResource(R.drawable.back1clear);
        }
        if(sp.getString("Act2", null) != null){
            ib2.setImageResource(R.drawable.back2clear);
        }
        if(sp.getString("Act3", null) != null){
            ib3.setImageResource(R.drawable.back3clear);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.animeGame:
                //cache(context);
                checkClear();
                DrawingView.initSpecialVar(150, 4000, 1);
                Intent myIntent = new Intent(LevelChoose.this, MainActivity.class);
                startActivity(myIntent);
                break;
            case R.id.Animal:
                checkClear();
                DrawingView.initSpecialVar(150, 3000, 2);
                myIntent = new Intent(LevelChoose.this, MainActivity.class);
                startActivity(myIntent);
                break;
            case R.id.Null:
                checkClear();
                DrawingView.initSpecialVar(150, 5000, 3);
                myIntent = new Intent(LevelChoose.this, MainActivity.class);
                startActivity(myIntent);
                break;
            case R.id.nill:
                checkClear();
                DrawingView.initSpecialVar(0, 10000000, 4);
                myIntent = new Intent(LevelChoose.this, MainActivity.class);
                startActivity(myIntent);
                break;
            case R.id.bckButton:
                myIntent = new Intent(LevelChoose.this, StartPage.class);
                startActivity(myIntent);
                break;
            default:
                break;
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onBackPressed(){
    }
}

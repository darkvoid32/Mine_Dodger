package com.nush.yetong.project;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.nush.yetong.project.R;

public class MainActivity extends AppCompatActivity {
    private static Context context;
    private static DrawingView dv;
    private static MainActivity MA = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        dv= new DrawingView(this);
        ((Activity) dv.getContext()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((Activity) dv.getContext()).requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);


        ll.addView(dv);
        //ActionBar actionBar = getActionBar();
        //actionBar.hide();
    }
    public static Context getAppContext() {
        return MainActivity.context;
    }


    public static DrawingView getDv(){
        return MainActivity.dv;
    }

    public static void win() {
        Intent myIntent = new Intent(getAppContext(), LevelChoose.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       getAppContext().startActivity(myIntent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    public static Handler UIHandler;

    static
    {
        UIHandler = new Handler(Looper.getMainLooper());
    }
    public static void runOnUI(Runnable runnable) {
        UIHandler.post(runnable);
    }

    @Override
    public void onBackPressed() {
        if (dv.interceptBackPressed()) {
            Intent myIntent = new Intent(MainActivity.this, LevelChoose.class);
            startActivity(myIntent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        super.onBackPressed();//finish your Activity
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        dv.t.interrupt();
        dv.t2.interrupt();
        dv.t3.interrupt();
        finish();
    }

}
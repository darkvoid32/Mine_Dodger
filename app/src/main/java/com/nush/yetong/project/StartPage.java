package com.nush.yetong.project;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
/*import android.support.v4.util.LruCache;*/
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by Yetong on 2017/2/19.
 */

public class StartPage extends Activity implements View.OnClickListener {
    Button b1, b2, b3;
    /*private LruCache<String, Bitmap> memCache;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_page);


        /*cache();*/

        b1 = (Button) findViewById(R.id.startBtn);
        b2 = (Button) findViewById(R.id.optionBtn);
        b3 = (Button) findViewById(R.id.quitButton);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);


    }

    /*private void cache() {

        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        memCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap image) {
                return image.getByteCount() / 1024;
            }
        };



    }*/

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startBtn:
                Intent intent = new Intent(StartPage.this, LevelChoose.class);
                startActivity(intent);
                break;
            case R.id.optionBtn:
                Intent intent2 = new Intent(StartPage.this, OptionChoose.class);
                startActivity(intent2);
                break;
            case R.id.quitButton:
                finish();
                break;
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}

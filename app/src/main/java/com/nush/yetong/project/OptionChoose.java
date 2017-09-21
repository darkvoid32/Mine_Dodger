package com.nush.yetong.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

/**
 * Created by Yetong on 2017/2/19.
 */
public class OptionChoose extends Activity {
    private Switch mySwitch;
    private Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_page);

        mySwitch = (Switch) findViewById(R.id.mySwitch);
        //set the switch to ON
        mySwitch.setChecked(true);
        //attach a listener to check for changes in state
        if (DrawingView.getMusic()==false){
            mySwitch.setChecked(false);
        }
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    DrawingView.music(true);
                }else{
                    DrawingView.music(false);
                }

            }
        });

    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.backButton:
                Intent myIntent = new Intent(this, StartPage.class);
                startActivity(myIntent);
                break;
            case R.id.creditButton:
                Intent myIntent2 = new Intent(this, CreditsPage.class);
                startActivity(myIntent2);
                break;

        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}

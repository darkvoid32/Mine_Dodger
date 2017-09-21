package com.nush.yetong.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Yetong on 2017/4/16.
 */

public class HighScore extends Activity {
    SharedPreferences sp;
    SharedPreferences.Editor Ed;

    TextView name1, name2, name3, name4, name5, score1, score2, score3, score4, score5;
    String name = "";
    Context context;
    Activity currentAct;
    LinearLayout ll1;
    View v;
    int time = 0;
    private String m_Text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscore_page);
        currentAct = this;
        v = findViewById(android.R.id.content);

        sp = getSharedPreferences("Score", 0);
        Ed = sp.edit();
        time = sp.getInt("NewScore", 0);

        name1 = (TextView) findViewById(R.id.Name1);
        name2 = (TextView) findViewById(R.id.Name2);
        name3 = (TextView) findViewById(R.id.Name3);
        name4 = (TextView) findViewById(R.id.Name4);
        name5 = (TextView) findViewById(R.id.Name5);
        score1 = (TextView) findViewById(R.id.Score1);
        score2 = (TextView) findViewById(R.id.Score2);
        score3 = (TextView) findViewById(R.id.Score3);
        score4 = (TextView) findViewById(R.id.Score4);
        score5 = (TextView) findViewById(R.id.Score5);

        context = getApplicationContext();
        name = askName();

    }

    private void checkScore() {
        init();
        if (name.length() > 8) {
            name = name.substring(0, 12);
            name += "...";
        }
        if(score1.getText().toString().equals("")){
            score1.setText("0");
        }
        if(score2.getText().toString().equals("")){
            score1.setText("0");
        }
        if(score3.getText().toString().equals("")){
            score1.setText("0");
        }
        if(score4.getText().toString().equals("")){
            score1.setText("0");
        }
        if(score5.getText().toString().equals("")){
            score1.setText("0");
        }
        if (Integer.parseInt(score1.getText().toString()) < time) {
            shiftDown(1, time, name);
        } else if (Integer.parseInt(score2.getText().toString()) < time) {
            shiftDown(2, time, name);
        } else if (Integer.parseInt(score3.getText().toString()) < time) {
            shiftDown(3, time, name);
        } else if (Integer.parseInt(score4.getText().toString()) < time) {
            shiftDown(4, time, name);
        } else if (Integer.parseInt(score5.getText().toString()) < time) {
            shiftDown(5, time, name);
        }
    }

    private void init() {
        String highS1, highS2, highS3, highS4, highS5;
        String nameS1, nameS2, nameS3, nameS4, nameS5;

        highS1 = sp.getString("Place1", null);
        highS2 = sp.getString("Place2", null);
        highS3 = sp.getString("Place3", null);
        highS4 = sp.getString("Place4", null);
        highS5 = sp.getString("Place5", null);
        nameS1 = sp.getString("Name1", null);
        nameS2 = sp.getString("Name2", null);
        nameS3 = sp.getString("Name3", null);
        nameS4 = sp.getString("Name4", null);
        nameS5 = sp.getString("Name5", null);

        name1.setText(nameS1);
        name2.setText(nameS2);
        name3.setText(nameS3);
        name4.setText(nameS4);
        name5.setText(nameS5);
        score1.setText(highS1);
        score2.setText(highS2);
        score3.setText(highS3);
        score4.setText(highS4);
        score5.setText(highS5);
    }

    private void shiftDown(int i, int time, String name) {
        String highS1, highS2, highS3, highS4;
        String nameS1, nameS2, nameS3, nameS4;
        switch (i) {
            case 1:
                highS1 = sp.getString("Place1", null);
                highS2 = sp.getString("Place2", null);
                highS3 = sp.getString("Place3", null);
                highS4 = sp.getString("Place4", null);
                nameS1 = sp.getString("Name1", null);
                nameS2 = sp.getString("Name2", null);
                nameS3 = sp.getString("Name3", null);
                nameS4 = sp.getString("Name4", null);

                Ed.putString("Place1", Integer.toString(time));
                Ed.putString("Place2", highS1);
                Ed.putString("Place3", highS2);
                Ed.putString("Place4", highS3);
                Ed.putString("Place5", highS4);
                Ed.putString("Name1", name);
                Ed.putString("Name2", nameS1);
                Ed.putString("Name3", nameS2);
                Ed.putString("Name4", nameS3);
                Ed.putString("Name5", nameS4);
                break;
            case 2:
                highS1 = sp.getString("Place2", null);
                highS2 = sp.getString("Place3", null);
                highS3 = sp.getString("Place4", null);
                nameS1 = sp.getString("Name2", null);
                nameS2 = sp.getString("Name3", null);
                nameS3 = sp.getString("Name4", null);

                Ed.putString("Place2", Integer.toString(time));
                Ed.putString("Place3", highS1);
                Ed.putString("Place4", highS2);
                Ed.putString("Place5", highS3);
                Ed.putString("Name2", name);
                Ed.putString("Name3", nameS1);
                Ed.putString("Name4", nameS2);
                Ed.putString("Name5", nameS3);
                break;
            case 3:
                highS1 = sp.getString("Place3", null);
                highS2 = sp.getString("Place4", null);
                nameS1 = sp.getString("Name3", null);
                nameS2 = sp.getString("Name4", null);

                Ed.putString("Place3", Integer.toString(time));
                Ed.putString("Place4", highS1);
                Ed.putString("Place5", highS2);
                Ed.putString("Name3", name);
                Ed.putString("Name4", nameS1);
                Ed.putString("Name5", nameS2);
                break;
            case 4:
                highS1 = sp.getString("Place4", null);
                nameS1 = sp.getString("Name4", null);

                Ed.putString("Place4", Integer.toString(time));
                Ed.putString("Place5", highS1);
                Ed.putString("Name4", name);
                Ed.putString("Name5", nameS1);
                break;

            case 5:
                Ed.putString("Place5", Integer.toString(time));
                Ed.putString("Name5", name);
                break;
        }

        Ed.commit();

        score1.setText(sp.getString("Place1", null));
        score2.setText(sp.getString("Place2", null));
        score3.setText(sp.getString("Place3", null));
        score4.setText(sp.getString("Place4", null));
        score5.setText(sp.getString("Place5", null));
        name1.setText(sp.getString("Name1", null));
        name2.setText(sp.getString("Name2", null));
        name3.setText(sp.getString("Name3", null));
        name4.setText(sp.getString("Name4", null));
        name5.setText(sp.getString("Name5", null));
    }

    static Dialog alertDialog;

    private String askName() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Your Name!");

        // Set up the input
        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name = input.getText().toString();
                checkScore();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

        return m_Text;
    }

    public void onClick(View v) {
        Intent myIntent = new Intent(context, LevelChoose.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(myIntent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}

package com.nush.yetong.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
/*import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.LruCache;*/
import android.provider.Settings;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;

/**
 * Created by Yetong on 2017/2/12.
 */

class DrawingView extends View {

    Bitmap bitmap, bitmapgirl, enemyp, girlcap;
    DisplayMetrics dm = getResources().getDisplayMetrics();

    final int parentHeight = dm.heightPixels;
    final int parentWidth = dm.widthPixels;
    float x, y, movedX1, movedY1, movedX2, movedY2, exX, exY;
    int width = 10, height = 10, difficulty, mineSpeed, life = 3, time = 0, animCounter = 0, exCounter = 0, animCounter2 = 0, girlSpeed = 3;
    double counter = -300;
    ArrayList<mines> mine = new ArrayList<>();
    private StringBuilder lifeMsg = new StringBuilder();
    private Formatter formatter = new Formatter(lifeMsg);  // Formatting the statusMsg
    private StringBuilder timeMsg = new StringBuilder();
    private Formatter formatter2 = new Formatter(timeMsg);  // Formatting the statusMsg
    private boolean doubleMine = false, mineEx = false, removeMine = false, soundPlayed = false, girlStart = true, justFinish = false, alert = false, paused = false;
    private boolean ivDialog = false, ivDialog2 = false, frontOrBack = false, tripleMine = false, quadMine = false;
    ArrayList<Bitmap> mBitmaps = new ArrayList<>();
    private static DrawingView dv;
    Context context;
    SoundPlayer sP = new SoundPlayer();
    GirlPlane gP;
    EnemyPlane eP;
    Paint paint, paintText;
    ImageView playerFace1;
    Dialog alertDialog;
    private int speechCounter = 0;
    TextView speechtv;
    private boolean enemyStart = false, hatemylife = false, enemyStart2 = true, enemyPFinishMoving = true, boolForTele = false, convo2 = false, convo1 = false, endBool = false;
    public static Thread t = null, t2 = null, t3 = null, t4 = null;
    private static int counter1 = 0;
    private static int counter2 = 0;
    private int counter3 = 0;
    private static boolean music;
    private boolean lost = false;
    private static boolean stopThread = true;
    private static int stageNumber = 0;
    private int mineCountTimer, maximumAllowedMines, mineCount, widthOfBitmap;


    //TODO Set Lasers
    public DrawingView(final Context context) {
        super(context);

        this.context = context;
        initBitmaps(context);
        initVar();


        setWillNotDraw(false);
        final GlobalClass global = (GlobalClass) context.getApplicationContext();
        // MAIN THREAD -- RUNS MINE CREATION == DIALOG CREATION == BITMAP CHANGE
        t = new Thread() {

            @Override
            public void run() {
                while (stopThread) {

                    // CHANGES BITMAP CONSTANTLY
                    if (animCounter2 % 25 == 0) {
                        if (stageNumber == 1) {
                            if (gP.getY() < 0) {
                                girlStart = false;
                                boolForTele = false;
                            }
                            if (eP.getY() > 50) {
                                enemyStart2 = false;
                                enemyPFinishMoving = true;
                            }
                        }
                        changeBitmap();
                    }

                    animCounter2++;


                    //PAUSED CHECKS IF DIALOG IS RUNNING
                    if (paused == false && girlStart == false) {

                        //COUNTER IS TIMER ESSENTIALLY
                        counter += 1;

                        //DUNNO WHAT THIS DOES TBH == INCREASES SPEED
                        levelCheck(counter);

                        synchronized (mine) {
                            //CREATION OF NEW MINES
                            if (counter > 0 && (counter % difficulty) == 0) {
                                for (int i = 0; i < mineCount; i++) {
                                    mine.add(new mines(maximumAllowedMines, widthOfBitmap));
                                }
                            }
                        }

                        //TIMER
                        if (counter % 100 == 0) {
                            time++;
                            changeBitmap();
                        }

                        if (life < 1 && lost == false) {
                            lost = true;
                            MainActivity.runOnUI(new Runnable() {
                                @Override
                                public void run() {
                                    show_dialog();
                                    justFinish = true;
                                }
                            });
                        }
                    }


                    if (animCounter2 > counter1 && ivDialog == true) {
                        frontOrBack = true;
                        ivDialog = false;
                        MainActivity.runOnUI(new Runnable() {
                            @Override
                            public void run() {
                                show_dialog();
                                justFinish = true;
                            }
                        });
                    }

                    if (gP.getY() < 300 && ivDialog2 == true) {
                        ivDialog2 = false;
                        frontOrBack = true;
                        MainActivity.runOnUI(new Runnable() {
                            @Override
                            public void run() {
                                show_dialog();
                                justFinish = true;
                            }
                        });
                    }

                    if (counter > counter2 && endBool == false) {
                        endBool = true;
                        MainActivity.runOnUI(new Runnable() {
                            @Override
                            public void run() {
                                show_dialog();
                                justFinish = true;
                            }
                        });
                    }

                    Log.d("Threads", "T1 RUNNING");


                    /*//REFRESHES SCREEN
                    postInvalidate();*/
                    try {
                        sleep(10);
                    } catch (InterruptedException e) {
                        threadRestart();
                    }
                }
            }
        };
        t.start();
        t4 = new Thread() {
            @Override
            public void run() {
                while (stopThread) {
                    //REFRESHES SCREEN
                    postInvalidate();
                    Log.d("Threads", "T2 RUNNING");
                    try {
                        sleep(5);
                    } catch (InterruptedException e) {
                        threadRestart();
                    }
                }
            }
        };
        t4.start();
        t2 = new Thread() {

            @Override
            public void run() {
                while (stopThread) {
                    synchronized(mine){
                    for (Iterator<mines> iterator = mine.iterator(); iterator.hasNext(); ) {

                        synchronized (iterator) {

                            // I THINK THIS IS USELESS TOO
                            mines m = null;

                            try {
                                m = iterator.next();

                                // PUSHING MINE TO BOTTOM OF SCREEN
                                m.setY(m.getY() + mineSpeed);

                                // IF MINE CLEARS BOTTOM OF SCREEN, CLEAR IT
                                if (m.getY() > parentHeight) {
                                    iterator.remove();
                                }

                                //COLLISION DETECTION
                                if (isCollisionDetected(bitmap, (int) x, (int) y, global.bmapNoScale, m.getX(), m.getY())) {
                                    if (music) {
                                        sP.playSound(context, 1);
                                    }
                                    mineEx = true;
                                    exX = m.getX(); // GET EXPLOSION X- COORD
                                    exY = m.getY(); // GET EXPLOSION Y - COORD

                                    removeMine = true;

                                    life--; // TAKE A LIFE
                                }

                            } catch (Exception e) {
                            }
                        }
                    }
                    }

                    /*if(removeMine){
                        mine.clear();
                        removeMine = false;
                    }*/
                    Log.d("Threads", "T3 RUNNING");
                    try {
                        sleep(15);
                    } catch (InterruptedException e) {
                        threadRestart();
                    }
                }
            }
        };
        t2.start();
        t3 = new Thread() {

            @Override
            public void run() {
                while (stopThread) {
                    //EXPLOSION ANIMATION
                    if (mineEx == true) {

                        // GOTO NEXT ANIMATION PICTURE
                        if (animCounter2 % 5 == 0) {
                            exCounter++;
                        }

                        // RESET ANIMATION WHEN END
                        if (exCounter == mBitmaps.size() - 1) {
                            exCounter = 0;
                            mineEx = false;
                        }

                    }
                    Log.d("Threads", "T4 RUNNING");
                    try {
                        sleep(10);
                    } catch (InterruptedException e) {
                        threadRestart();
                    }
                }
            }
        };
        t3.start();
    }


    private void threadRestart() {
        t.interrupt();
        t.start();
    }

    private void end() {
        SharedPreferences sp = context.getSharedPreferences("clear", 0);
        SharedPreferences.Editor Ed = sp.edit();

        switch (stageNumber) {
            case 1:
                Ed.putString("Act1", "Clear");
                break;
            case 2:
                Ed.putString("Act2", "Clear");
                break;
            case 3:
                Ed.putString("Act3", "Clear");
                break;
        }

        Ed.commit();

        Intent myIntent = new Intent(context, LevelChoose.class);
        context.startActivity(myIntent);
    }

    public void initVar() {
        dv = MainActivity.getDv();

        width = bitmap.getWidth();
        height = bitmap.getHeight();
        x = parentWidth / 2 - width / 2;
        y = parentHeight - 400;
        difficulty = 200;
        mineSpeed = 5;
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.CYAN);
        girlStart = true;
        ivDialog = true;
        ivDialog2 = true;
        paintText = new Paint();
        stopThread = true;

        final GlobalClass global = (GlobalClass) context.getApplicationContext();
        widthOfBitmap = global.bmapNoScale.getWidth();

        maximumAllowedMines = getMaximumAllowedMines();
        mineCount = 1;
        mineCountTimer = 1000;

        // PUTTING TEXT
        float textSize = paintText.getTextSize();
        paintText.setTextSize(textSize * 3);

        // TEXT FOR NUMBER OF LIVES
        paintText.setColor(Color.BLACK);

        /*sP.playSound(context, 1);*/

    }

    //GLORIOUS DIALOG BOX
    public void show_dialog() {


        alertDialog = new Dialog(context);

        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View convertView = inflater.inflate(R.layout.dialog, null, false);

        Button button_hide_alertDialog = (Button) convertView.findViewById(R.id.button_hide_alertDialog);

        speechtv = (TextView) convertView.findViewById(R.id.speechID);

        playerFace1 = (ImageView) convertView.findViewById(R.id.close_dialog);

        paused = true;

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.Dialog;

        if (alertDialog != null) {
            alertDialog.dismiss();
        }

        if (convo1 == false) {
            button_hide_alertDialog.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (lost == true) {
                        if (alertDialog != null && alertDialog.isShowing()) {
                            threadChange("malechar", "NOOOO!");
                        }
                        lose();
                    } else {
                        if (alertDialog != null && alertDialog.isShowing()) {
                            switch (stageNumber) {
                                case 1:
                                    switch (speechCounter) {
                                        case 0:
                                            threadChange("femalechar", "What a good day today!");
                                            speechCounter++;
                                            break;
                                        case 1:
                                            threadChange("femalechar", "I hope nothing bad happens");
                                            enemyStart = true;
                                            enemyPFinishMoving = false;
                                            boolForTele = true;
                                            speechCounter++;
                                            break;
                                        case 2:
                                            threadChange("male2", "Time to test out my new invention: the GraviTron 3000000!");
                                            if (enemyStart) {
                                                speechCounter++;
                                            }
                                            break;
                                        case 3:
                                            threadChange("male2", "Try to get your friend back now!");
                                            speechCounter++;
                                            break;
                                        case 4:
                                            threadChange("malechar", "What is happening??");
                                            speechCounter++;
                                            break;
                                        case 5:
                                            paused = false;
                                            speechCounter = 0;
                                            convo1 = true;
                                            alertDialog.dismiss();
                                            break;
                                        default:
                                            speechCounter = 0;
                                            alertDialog.dismiss();
                                            show_dialog();
                                            break;
                                    }
                                    break;
                                case 2:
                                    switch (speechCounter) {
                                        case 0:
                                            threadChange("malechar", "Lets see if she is here...");
                                            speechCounter++;
                                            break;
                                        case 1:
                                            paused = false;
                                            speechCounter = 0;
                                            convo1 = true;
                                            girlStart = false;
                                            alertDialog.dismiss();
                                            break;
                                        default:
                                            speechCounter = 0;
                                            alertDialog.dismiss();
                                            show_dialog();
                                            break;
                                    }
                                    break;
                                case 3:
                                    switch (speechCounter) {
                                        case 0:
                                            threadChange("malechar", "AH!");
                                            speechCounter++;
                                            break;
                                        case 1:
                                            threadChange("malechar", "There she is!");
                                            speechCounter++;
                                            break;
                                        case 2:
                                            threadChange("femalechar", "Come help me!");
                                            speechCounter++;
                                            break;
                                        case 3:
                                            threadChange("male2", "You'll never get her!");
                                            speechCounter++;
                                            break;
                                        case 4:
                                            paused = false;
                                            speechCounter = 0;
                                            convo1 = true;
                                            girlStart = false;
                                            alertDialog.dismiss();
                                        default:
                                            speechCounter = 0;
                                            alertDialog.dismiss();
                                            show_dialog();
                                            break;
                                    }
                                    break;
                                case 4:
                                    switch (speechCounter) {
                                        case 0:
                                            threadChange("malechar", "Lets see if she is here...");
                                            speechCounter++;
                                            break;
                                        case 1:
                                            paused = false;
                                            speechCounter = 0;
                                            convo1 = true;
                                            girlStart = false;
                                            alertDialog.dismiss();
                                            break;
                                        default:
                                            speechCounter = 0;
                                            alertDialog.dismiss();
                                            show_dialog();
                                            break;
                                    }
                                    break;
                                default:
                                    speechCounter = 0;
                                    alertDialog.dismiss();
                                    show_dialog();
                                    break;
                            }
                        }
                    }
                }
            });
        } else if (endBool == true) {
            button_hide_alertDialog.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (lost == true) {
                        if (alertDialog != null && alertDialog.isShowing()) {
                            threadChange("malechar", "NOOOO!");
                        }
                        lose();
                    } else {
                        if (alertDialog != null && alertDialog.isShowing()) {
                            switch (stageNumber) {
                                case 1:
                                    switch (speechCounter) {
                                        case 0:
                                            threadChange("malechar", "I have to get her back, no matter what!");
                                            speechCounter++;
                                            break;
                                        case 1:
                                            threadChange("malechar", "Let me try another sector and see if she is there...");
                                            speechCounter++;
                                            break;
                                        case 2:
                                            end();
                                            alertDialog.dismiss();
                                            break;
                                        default:
                                            speechCounter = 0;
                                            alertDialog.dismiss();
                                            show_dialog();
                                            break;
                                    }
                                    break;
                                case 2:
                                    switch (speechCounter) {
                                        case 0:
                                            threadChange("malechar", "I have to get her back, no matter what!");
                                            speechCounter++;
                                            break;
                                        case 1:
                                            threadChange("malechar", "Let me try another sector and see if she is there...");
                                            speechCounter++;
                                            break;
                                        case 2:
                                            end();
                                            alertDialog.dismiss();
                                        default:
                                            if (speechtv.getText() == null || speechtv.getText() == "") {
                                                speechCounter = 0;
                                                alertDialog.dismiss();
                                                show_dialog();
                                            }
                                            break;
                                    }
                                    break;
                                case 3:
                                    switch (speechCounter) {
                                        case 0:
                                            threadChange("male2", "NOOO! MY PLANS HAVE BEEN FOILED!");
                                            speechCounter++;
                                            break;
                                        case 1:
                                            threadChange("femalechar", "Thanks for saving me :)");
                                            speechCounter++;
                                            break;
                                        case 2:
                                            paused = false;
                                            speechCounter = 0;
                                            convo2 = true;
                                            girlStart = false;
                                            end();
                                            alertDialog.dismiss();
                                            break;
                                        default:
                                            speechCounter = 0;
                                            alertDialog.dismiss();
                                            show_dialog();
                                            break;
                                    }
                                    break;
                                case 4:
                                    switch (speechCounter) {
                                        case 0:
                                            threadChange("malechar", "I have to get her back, no matter what!");
                                            speechCounter++;
                                            break;
                                        case 1:
                                            threadChange("malechar", "Let me try another sector and see if she is there...");
                                            speechCounter++;
                                            break;
                                        case 2:
                                            end();
                                            alertDialog.dismiss();
                                        default:
                                            speechCounter = 0;
                                            alertDialog.dismiss();
                                            show_dialog();
                                            break;
                                    }
                                    break;
                                default:
                                    if (speechtv.getText() == null || speechtv.getText() == "") {
                                        speechCounter = 0;
                                        alertDialog.dismiss();
                                        show_dialog();
                                    }
                                    break;
                            }
                        }
                    }
                }
            });
        } else /*if (convo2 == false)*/ {
            button_hide_alertDialog.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (lost == true) {
                        if (alertDialog != null && alertDialog.isShowing()) {
                            threadChange("malechar", "NOOOO!");
                        }
                        lose();
                    } else {
                        if (alertDialog != null && alertDialog.isShowing()) {
                            switch (speechCounter) {
                                case 0:
                                    threadChange("femalechar", "Are you going to leave me? :(");
                                    speechCounter++;
                                    break;

                                case 1:
                                    threadChange("malechar", "NO! IM COMING AFTER YOU!");
                                    enemyStart = true;
                                    enemyPFinishMoving = false;
                                    boolForTele = true;
                                    hatemylife = true;
                                    speechCounter++;
                                    break;

                                case 2:
                                    paused = false;
                                    speechCounter = 0;
                                    convo2 = true;
                                    alertDialog.dismiss();
                                    break;
                                default:
                                    speechCounter = 0;
                                    alertDialog.dismiss();
                                    show_dialog();
                                    break;
                            }
                        }
                    }
                }
            });
        }

        alertDialog.setContentView(convertView);

        // show it
        alertDialog.show();


    }


    private void threadChange(String face, String speech) {
        final String whatsFace = face;
        final String whatsSpeech = speech;
        MainActivity.runOnUI(new Runnable() {
            @Override
            public void run() {
                switch (whatsFace) {
                    case "malechar":
                        playerFace1.setImageResource(R.drawable.malechar);
                        break;
                    case "femalechar":
                        playerFace1.setImageResource(R.drawable.femalechar);
                        break;
                    case "male2":
                        playerFace1.setImageResource(R.drawable.male2);
                        break;
                }
                speechtv.setText(whatsSpeech);
            }
        });
    }

    private void lose() {
        if (stageNumber == 4) {
            SharedPreferences sp = context.getSharedPreferences("Score", 0);
            SharedPreferences.Editor Ed = sp.edit();
            Ed.putInt("NewScore", time);
            Ed.commit();

            Intent myIntent = new Intent(context, HighScore.class);
            context.startActivity(myIntent);
            if (context instanceof Activity) {
                ((Activity) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        } else {
            Intent myIntent = new Intent(context, LevelChoose.class);
            context.startActivity(myIntent);
            if (context instanceof Activity) {
                ((Activity) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }
    }


    // KILLER OF RAM
    private void initBitmaps(Context context) {

        GlobalClass global = (GlobalClass) context.getApplicationContext();

        if (global.mGlobalBitmapMain.isEmpty()) {
            /*bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.plane);
            bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.plane);
            bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.plane2);
            bitmap3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.plane3);
            bitmap4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.plane4);*/

            global.mGlobalBitmapMain.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.plane));
            global.mGlobalBitmapMain.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.plane2));
            global.mGlobalBitmapMain.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.plane3));
            global.mGlobalBitmapMain.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.plane4));
        }

        bitmap = global.mGlobalBitmapMain.get(0);

        if (global.mGlobalBitmapEx.isEmpty()) {
           /* mBitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex1));
            mBitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex2));
            mBitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex3));
            mBitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex4));
            mBitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex5));
            mBitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex6));
            mBitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex7));
            mBitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex8));
            mBitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex9));
            mBitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex10));
            mBitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex11));
            mBitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex12));
            mBitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex13));
            mBitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex14));
            mBitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex15));
            mBitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex16));*/
            global.mGlobalBitmapEx.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex1));
            global.mGlobalBitmapEx.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex2));
            global.mGlobalBitmapEx.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex3));
            global.mGlobalBitmapEx.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex4));
            global.mGlobalBitmapEx.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex5));
            global.mGlobalBitmapEx.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex6));
            global.mGlobalBitmapEx.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex7));
            global.mGlobalBitmapEx.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex8));
            global.mGlobalBitmapEx.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex9));
            global.mGlobalBitmapEx.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex10));
            global.mGlobalBitmapEx.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex11));
            global.mGlobalBitmapEx.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex12));
            global.mGlobalBitmapEx.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex13));
            global.mGlobalBitmapEx.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex14));
            global.mGlobalBitmapEx.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex15));
            global.mGlobalBitmapEx.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ex16));

            mBitmaps = global.mGlobalBitmapEx;

        }


        if (global.mGlobalBitmapGirl.isEmpty()) {
            /*bitmapgirl = BitmapFactory.decodeResource(context.getResources(), R.drawable.girlplane);
            bitmapgirl1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.girlplane);
            bitmapgirl2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.girlplane2);
            bitmapgirl3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.girlplane3);
            bitmapgirl4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.girlplane4);*/
            global.mGlobalBitmapGirl.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.girlplane));
            global.mGlobalBitmapGirl.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.girlplane2));
            global.mGlobalBitmapGirl.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.girlplane3));
            global.mGlobalBitmapGirl.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.girlplane4));
        }

        bitmapgirl = global.mGlobalBitmapGirl.get(0);

        if (global.mGlobalBitmapGirlCap.isEmpty()) {
            /*girlcap = BitmapFactory.decodeResource(context.getResources(), R.drawable.girlplanecap1);
            girlcap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.girlplanecap2);*/
            global.mGlobalBitmapGirlCap.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.girlplanecap1));
            global.mGlobalBitmapGirlCap.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.girlplanecap2));
        }

        girlcap = global.mGlobalBitmapGirlCap.get(0);

        if (global.mGlobalBitmapBadGuy.isEmpty()) {
            /*enemyp = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemyp);
            enemyp1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemyp);
            enemyp2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemyp2);
            enemyp3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemyp3);
            enemyp4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemyp4);*/
            global.mGlobalBitmapBadGuy.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.enemyp));
            global.mGlobalBitmapBadGuy.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.enemyp2));
            global.mGlobalBitmapBadGuy.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.enemyp3));
            global.mGlobalBitmapBadGuy.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.enemyp4));
        }

        enemyp = global.mGlobalBitmapBadGuy.get(0);

        /*bitmapMineNoScale = BitmapFactory.decodeResource(context.getResources(), R.drawable.mine);*/
        global.bmapNoScale = BitmapFactory.decodeResource(context.getResources(), R.drawable.mine);
        /*maleChar = BitmapFactory.decodeResource(context.getResources(), R.drawable.malechar);*/
        global.maleChar = BitmapFactory.decodeResource(context.getResources(), R.drawable.malechar);
        gP = new GirlPlane((parentWidth / 2 - width), parentHeight - 400);
        eP = new EnemyPlane((parentWidth / 2 - width), -150);
    }

    //MAKES PLANES LOOK COOL
    private void changeBitmap() {
        GlobalClass global = (GlobalClass) context.getApplicationContext();
        if (girlStart && enemyPFinishMoving && !boolForTele) {  // ANIMATION FOR PLANES -- STARTING; WHEN THERES ALL 3 PLANES
            switch (animCounter) {
                case 0:
                    bitmap = global.mGlobalBitmapMain.get(0);
                    bitmapgirl = global.mGlobalBitmapGirl.get(0);
                    enemyp = global.mGlobalBitmapBadGuy.get(0);
                    break;
                case 1:
                    bitmap = global.mGlobalBitmapMain.get(1);
                    bitmapgirl = global.mGlobalBitmapGirl.get(1);
                    enemyp = global.mGlobalBitmapBadGuy.get(1);
                    break;
                case 2:
                    bitmap = global.mGlobalBitmapMain.get(2);
                    bitmapgirl = global.mGlobalBitmapGirl.get(2);
                    enemyp = global.mGlobalBitmapBadGuy.get(2);
                    break;
                case 3:
                    bitmap = global.mGlobalBitmapMain.get(3);
                    bitmapgirl = global.mGlobalBitmapGirl.get(3);
                    enemyp = global.mGlobalBitmapBadGuy.get(3);
                    break;
                case 4:
                    bitmap = global.mGlobalBitmapMain.get(2);
                    bitmapgirl = global.mGlobalBitmapGirl.get(2);
                    enemyp = global.mGlobalBitmapBadGuy.get(2);
                    break;
                case 5:
                    bitmap = global.mGlobalBitmapMain.get(1);
                    bitmapgirl = global.mGlobalBitmapGirl.get(1);
                    enemyp = global.mGlobalBitmapBadGuy.get(1);
                    animCounter = 0;
                    break;
                default:
                    break;
            }
        } else if (enemyPFinishMoving || hatemylife) { // ANIMATION FOR MC PLANE -- ONLY PLANE THERE -- EFFICIENCY !!
            switch (animCounter) {
                case 0:
                    bitmap = global.mGlobalBitmapMain.get(0);
                    break;
                case 1:
                    bitmap = global.mGlobalBitmapMain.get(1);
                    break;
                case 2:
                    bitmap = global.mGlobalBitmapMain.get(2);
                    break;
                case 3:
                    bitmap = global.mGlobalBitmapMain.get(3);
                    break;
                case 4:
                    bitmap = global.mGlobalBitmapMain.get(2);
                    break;
                case 5:
                    bitmap = global.mGlobalBitmapMain.get(1);
                    animCounter = 0;
                    break;
                default:
                    break;
            }
        } else if (boolForTele) {  // ANIMATION FOR PLANES -- STARTING; WHEN THERES ALL 3 PLANES
            switch (animCounter) {
                case 0:
                    bitmap = global.mGlobalBitmapMain.get(0);
                    bitmapgirl = global.mGlobalBitmapGirlCap.get(0);
                    enemyp = global.mGlobalBitmapBadGuy.get(0);
                    /*bitmap = bitmap2;
                    bitmapgirl = girlcap;
                    enemyp = enemyp2;*/
                    break;
                case 1:
                    bitmap = global.mGlobalBitmapMain.get(1);
                    bitmapgirl = global.mGlobalBitmapGirl.get(1);
                    enemyp = global.mGlobalBitmapBadGuy.get(1);
                    /*bitmap = bitmap3;
                    enemyp = enemyp3;
                    bitmapgirl = girlcap2;*/
                    break;
                case 2:
                    /*bitmap = bitmap4;
                    enemyp = enemyp4;
                    bitmapgirl = girlcap;*/
                    bitmap = global.mGlobalBitmapMain.get(2);
                    bitmapgirl = global.mGlobalBitmapGirl.get(0);
                    enemyp = global.mGlobalBitmapBadGuy.get(2);
                    break;
                case 3:
                    /*bitmap = bitmap3;
                    enemyp = enemyp3;
                    bitmapgirl = girlcap2;*/
                    bitmap = global.mGlobalBitmapMain.get(3);
                    bitmapgirl = global.mGlobalBitmapGirl.get(1);
                    enemyp = global.mGlobalBitmapBadGuy.get(3);
                    break;
                case 4:
                    bitmap = global.mGlobalBitmapMain.get(2);
                    bitmapgirl = global.mGlobalBitmapGirl.get(0);
                    enemyp = global.mGlobalBitmapBadGuy.get(2);
                    /*bitmap = bitmap2;
                    bitmapgirl = girlcap;
                    enemyp = enemyp2;*/
                    break;
                case 5:
                    bitmap = global.mGlobalBitmapMain.get(1);
                    bitmapgirl = global.mGlobalBitmapGirl.get(1);
                    enemyp = global.mGlobalBitmapBadGuy.get(1);
                    /*bitmap = bitmap1;
                    bitmapgirl = girlcap2;
                    enemyp = enemyp1;*/
                    animCounter = 0;
                    break;
            }
        }
        animCounter++;
    }

    //INCREASES MINE SPEED W/ TIMES
    private void levelCheck(double counter) {
        if (counter > 0 && counter % 500 == 0) {
            if (difficulty > 75) {
                difficulty -= 20;
            }
            mineSpeed += 1;
        }

        if (maximumAllowedMines > mineCount) {

            if (counter >= mineCountTimer) {
                mineCount++;
                mineCountTimer += (500 * mineCount);
            }
        }
    }

    // CHANGES PLANE POSITION
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                movedX1 = (int) event.getX();
                movedY1 = (int) event.getY();
            }
            break;

            case MotionEvent.ACTION_MOVE: {

                if (paused == false && girlStart == false) {
                    movedX2 = (int) event.getX();
                    movedY2 = (int) event.getY();
                    x += (movedX2 - movedX1);
                    y += (movedY2 - movedY1);
                    movedX1 = movedX2;
                    movedY1 = movedY2;

                    wallCollision();

                }
            }

            break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    //PREVENTS PLANE FROM LEAVING SCREEN
    private void wallCollision() {
        // LEFT WALL
        if (x == (0) || x < (0)) {
            x = 0;
        }
        //RIGHT WALL
        if (x == (parentWidth - 100) || x > (parentWidth - 100)) {
            x = parentWidth - 100;
        }
        //TOP WALL
        if (y == 0 || y < 0) {
            y = 0;
        }
        //BOTTOM WALL
        if (y == (parentHeight - 130) || (y > parentHeight - 130)) {
            y = parentHeight - 130;
        }
    }

    // THE HEART OF THE CODE
    @Override
    public void onDraw(Canvas canvas) {

        //TODO Do Girl Plane Animation when Starting

        final GlobalClass global = (GlobalClass) context.getApplicationContext();

        // REFRESHING OF MAIN PLAYER BITMAP
        canvas.drawBitmap(bitmap, x, y, paint);


        if (paused == false && girlStart == false) {
            // SYNCHRO MINE SO IT WONT FREAKING CRASH
            synchronized (mine) {

                // IF COLLISION DETECTED PREVIOUSLY, DELETE ALL MINES
                if (removeMine) {
                    mine.clear();
                    removeMine = false;
                }

                // SETTING NEW LOCATION FOR EVERY MINE
                for (Iterator<mines> iterator = mine.iterator(); iterator.hasNext(); ) {

                    //ANOTHER F****ING SYNCHRO == I THINK ITS REDUNDANT
                    synchronized (iterator) {

                        // I THINK THIS IS USELESS TOO
                        mines m = null;

                        try {
                            m = iterator.next();

                            // DRAW THE NEW MINE LOCATION
                            canvas.drawBitmap(global.bmapNoScale, m.getX(), m.getY(), paint);

                        } catch (Exception e) {

                            // LITERALLY JUST REDRAW IF THIS S*** CRASHES
                            invalidate();
                        }
                    }
                }

                //EXPLOSION ANIMATION
                if (mineEx == true && !mBitmaps.isEmpty()) {
                    // DRAW ACTUAL EXPLOSION
                    canvas.drawBitmap(mBitmaps.get(exCounter), (exX - (width / 2)), exY, paint);
                } else if (mBitmaps.isEmpty()) {
                    mBitmaps = global.mGlobalBitmapEx;
                    canvas.drawBitmap(mBitmaps.get(exCounter), (exX - (width / 2)), exY, paint);
                }


                canvas.drawText(lifeMsg.toString(), 10, 80, paintText);

                lifeMsg.delete(0, lifeMsg.length());   // Empty buffer
                formatter.format("Life: " + life);

                canvas.drawText(timeMsg.toString(), 150, 80, paintText);

                // TEXT FOR TIME
                timeMsg.delete(0, timeMsg.length());   // Empty buffer
                formatter.format("   Time: " + time + " Seconds");

            }
        }


        // IF PAUSED
        else {
            // GIRL ANIMATION
            if (girlStart == true) {
                if (stageNumber == 1) {
                    canvas.drawBitmap(bitmapgirl, gP.getX(), gP.getY(), paint);
                    canvas.drawBitmap(enemyp, eP.getX(), eP.getY(), paint);
                    if (paused == false) {
                        if (frontOrBack == false) {
                            gP.setX(gP.getX() - girlSpeed);
                        } else {
                            gP.setY(gP.getY() - girlSpeed);
                        }

                        if (enemyStart) {
                            eP.setY(eP.getY() - girlSpeed);
                        }
                    }// TODO DO GIRL ANIM
                    else if (enemyPFinishMoving == false) {
                        if (enemyStart2) {
                            eP.setY(eP.getY() + girlSpeed);
                        }
                    }
                }
            }

            // SAME LOCATION
            for (Iterator<mines> iterator = mine.iterator(); iterator.hasNext(); ) {

                //ZZZ
                synchronized (iterator) {

                    mines m = null;

                    try {
                        m = iterator.next();

                        canvas.drawBitmap(global.bmapNoScale, m.getX(), m.getY(), paint);

                    } catch (Exception e) {

                        invalidate();
                    }

                }
            }
        }
    }

    public boolean interceptBackPressed() {
        return true;
    }


    // ENTIRE PIXEL PERFECT COLLISION METHOD
    public static boolean isCollisionDetected(Bitmap bitmap1, int x1, int y1, Bitmap bitmap2, int x2, int y2) {

        Rect bounds1 = new Rect(x1, y1, x1 + bitmap1.getWidth(), y1 + bitmap1.getHeight());
        Rect bounds2 = new Rect(x2, y2, x2 + bitmap2.getWidth(), y2 + bitmap2.getHeight());

        if (Rect.intersects(bounds1, bounds2)) {
            Rect collisionBounds = getCollisionBounds(bounds1, bounds2);
            for (int i = collisionBounds.left; i < collisionBounds.right; i++) {
                for (int j = collisionBounds.top; j < collisionBounds.bottom; j++) {
                    int bitmap1Pixel = bitmap1.getPixel(i - x1, j - y1);
                    int bitmap2Pixel = bitmap2.getPixel(i - x2, j - y2);
                    if (isFilled(bitmap1Pixel) && isFilled(bitmap2Pixel)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static Rect getCollisionBounds(Rect rect1, Rect rect2) {
        int left = (int) Math.max(rect1.left, rect2.left);
        int top = (int) Math.max(rect1.top, rect2.top);
        int right = (int) Math.min(rect1.right, rect2.right);
        int bottom = (int) Math.min(rect1.bottom, rect2.bottom);
        return new Rect(left, top, right, bottom);
    }

    private static boolean isFilled(int pixel) {
        return pixel != Color.TRANSPARENT;
    }

    public static void initSpecialVar(int i1, int i, int b) {
        counter1 = i1;
        counter2 = i;
        stageNumber = b;
    }

    public static void music(boolean b) {
        music = b;
    }

    public static boolean getMusic() {
        return music;
    }

    public static void stopTheThread() {
        if (t != null) {
            stopThread = false;
        }
    }

    public int getMaximumAllowedMines() {
        final GlobalClass global = (GlobalClass) context.getApplicationContext();

        String noOfMines = String.valueOf(parentWidth / (global.bmapNoScale.getWidth() + 10));
        int i = Integer.parseInt(noOfMines.substring(0, 1));
        return i;
    }
}
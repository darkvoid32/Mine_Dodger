package com.nush.yetong.project;

import android.content.Context;
import android.util.Log;

import java.util.Random;

/**
 * Created by Yetong on 2017/2/14.
 */

public class mines {
    int y = -20, x, space, check;
    Random ran = new Random();
    public mines(int maximumAllowedMines, int widthOfBitmap){
        space = ran.nextInt(maximumAllowedMines + 1);
        x = space * widthOfBitmap + 5;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }


}

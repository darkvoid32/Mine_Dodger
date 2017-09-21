package com.nush.yetong.project;

import android.app.Application;
import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Yetong on 2017/9/4.
 */

public class GlobalClass extends Application {
    public ArrayList<Bitmap> mGlobalBitmapMain = new ArrayList<>();
    public ArrayList<Bitmap> mGlobalBitmapGirl = new ArrayList<>();
    public ArrayList<Bitmap> mGlobalBitmapBadGuy = new ArrayList<>();
    public ArrayList<Bitmap> mGlobalBitmapEx = new ArrayList<>();
    public ArrayList<Bitmap> mGlobalBitmapGirlCap = new ArrayList<>();

    public Bitmap bmapNoScale;
    public Bitmap maleChar;
}

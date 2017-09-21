package com.nush.yetong.project;

/**
 * Created by Yetong on 2017/2/25.
 */

public class GirlPlane {
    static int y, x;
    public GirlPlane(int width, int height){
        this.x = width;
        this.y = height;
    }

    public static int getX() {
        return x;
    }

    public static int getY() {
        return y;
    }

    public static void setY(int ye) {
        y = ye;
    }

    public static void setX(int x) {
        GirlPlane.x = x;
    }
}

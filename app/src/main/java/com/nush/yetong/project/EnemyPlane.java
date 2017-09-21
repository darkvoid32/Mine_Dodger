package com.nush.yetong.project;

/**
 * Created by Yetong on 2017/2/25.
 */

public class EnemyPlane {
    static int y, x;
    public EnemyPlane(int width, int height){
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
        EnemyPlane.x = x;
    }
}

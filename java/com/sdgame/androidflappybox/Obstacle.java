package com.sdgame.androidflappybox;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Obstacle {
    private final float width = 50 * GameView.density;
    private float height;
    private Rect bound;
    private float midX;
    private boolean scored;

    private float posX;
    private float posY;
    private float posX2;
    private float posY2;

    public Obstacle(float posX, float posY, float height){
        this.posX = posX;
        this.posY = posY;
        this.height = height;
        this.posX2 = this.posX + this.width;
        this.posY2 = this.posY + this.height;
        this.bound = new Rect((int)this.posX, (int)this.posY, (int)this.posX2, (int)this.posY2);
        this.midX = this.posX + this.width/2;
        this.scored = false;
    }

    public void update(){
        posX = posX - 2* GameView.density;
        posX2 = posX+ width;
        bound.set((int)posX, (int)posY, (int)posX2, (int)posY2);
        midX = posX + width/2;
    }

    public void draw(Canvas canvas, Paint paint){
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(posX, posY, posX2, posY2, paint);

        //draw the obstacle bound
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(bound.left, bound.top, bound.right, bound.bottom, paint);
    }

    public float getPosX(){
        return posX;
    }

    public float getPosX2(){
        return posX2;
    }

    public float getPosY(){
        return posY;
    }

    public float getPosY2(){
        return posY2;
    }

    public Rect getBound(){
        return bound;
    }

    public float getMidX(){
        return midX;
    }

    public boolean getScored(){
        return scored;
    }

    public void setScored(boolean s){
        scored = s;
    }
}

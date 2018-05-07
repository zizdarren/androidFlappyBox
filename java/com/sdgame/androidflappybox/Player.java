package com.sdgame.androidflappybox;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;


public class Player {
    private float width;
    private float height;
    private Rect bound;
    private float midX;
    private float midY;

    private float posX;
    private float posY;
    private float posX2;
    private float posY2;

    private float momentum = 0;
    private float fallSpeed = 0.40f * GameView.density;
    private float maxfallSpeed = 10 * GameView.density;
    private float jumpStart = -8 * GameView.density;

    private boolean isJump = false;

    public Player(float posX, float posY, int width, int height){
        this.posX = posX * GameView.density;
        this.posY = posY * GameView.density;
        this.width = width * GameView.density;
        this.height = height * GameView.density;
        this.posX2 = this.posX + this.width;
        this.posY2 = this.posY + this.height;
        this.bound = new Rect((int)this.posX, (int)this.posY, (int) this.posX2, (int)this.posY2);
        this.midX = this.posX + this.width/2;
        this.midY = this.posY + this.height/2;
    }

    public void update(){

        if( momentum <= maxfallSpeed){
            momentum += fallSpeed;
        }

        if(isJump && momentum >=0){
            momentum = jumpStart;
        }

        if(posY >= GameView.HEIGHT * 3/4){
            posY = GameView.HEIGHT * 3/4;
            posY2 = posY + height;
            if(momentum > 0){
                momentum = 0;
            }
        }

        if(posY < 0){
            posY = 0;
            posY2 = posY + height;
        }

        this.posY += momentum;
        this.posY2 = posY + height;
        midY = posY + height/2;
        bound.set((int)posX, (int)posY, (int)posX2, (int)posY2);
    }

    public void draw(Canvas canvas, Paint paint){
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(posX, posY, posX2, posY2, paint);

        //draw the player bound
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(bound.left, bound.top, bound.right, bound.bottom, paint);

    }

    public boolean isJump(){
        return isJump;
    }

    public void setJump(boolean jump){
        isJump = jump;
    }

    public float getMidX(){
        return midX;
    }

    public float getMidY(){
        return midY;
    }

    public Rect getBound(){
        return bound;
    }
}

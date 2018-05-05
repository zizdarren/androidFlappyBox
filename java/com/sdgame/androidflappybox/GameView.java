package com.sdgame.androidflappybox;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable, SurfaceHolder.Callback{

    public static float density;

    public static int WIDTH;
    public static int HEIGHT;


    private Thread gameThread;
    private volatile Boolean isRunning = false;
    private int FPS = 60;

    public static boolean gameOver = false;
    private float obspace = 300 * GameView.density;
    private int score;

    //drawing object
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    //game objects
    Player player;

    public ArrayList<Obstacle> obs;

    private Random R;


    public GameView(Context context){
        super(context);

        density = this.getResources().getDisplayMetrics().density;
        //WIDTH = getWidth();
        //HEIGHT = getHeight();

        //start initializing drawing objects
        surfaceHolder = getHolder();
        paint = new Paint();

        //add callback to surface holder, for getting view's width and height,when surface view-
        //- change size
        this.getHolder().addCallback(this);

        start();
    }

    public void start(){
        if(gameThread == null){
            gameThread = new Thread(this);
            gameThread.start();
        }
        isRunning = true;
    }

    public void init(){
        player = new Player(100, 100, 50,50 );
        obs = new ArrayList<Obstacle>();
        score = 0;
        R = new Random();
    }


    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double nsPerUpdate = 1000000000.0/FPS;
        double updateOffsetTime = 0;
        int frames = 0;
        int updates = 0;
         long secondTimer = System.currentTimeMillis();

        init();

        while(isRunning){
            long now = System.nanoTime();
            updateOffsetTime += (now - lastTime) / nsPerUpdate;
            lastTime = now;
            boolean shouldDraw = false;   //false

            while(updateOffsetTime >= 1){
                update();
                updates ++;
                updateOffsetTime -= 1;
                shouldDraw = true;
            }


            try{
                Thread.sleep(2);
            }catch(InterruptedException e){
                e.printStackTrace();
            }


            if(shouldDraw){
                draw();
                //repaint();
                frames ++;
            }


            if (System.currentTimeMillis() - secondTimer >= 1000) {
                secondTimer += 1000;
                System.out.println("Update: " + updates + " , Frames: " + frames);
                updates = 0;
                frames = 0;

                //obstacle spawner
                if (secondTimer / 1000 % 3 == 1 && !gameOver) {  //obstacle spawner
                    System.out.println(secondTimer / 1000 % 3);
                    int r = R.nextInt(HEIGHT * 3 / 4 - (int) obspace);
                    obs.add(new Obstacle(WIDTH, 0, r));
                    obs.add(new Obstacle(WIDTH, r + obspace, HEIGHT * 3 / 4 - r - obspace));
                    System.out.println(obs.size());
                }
            }



        }
    }

    public void update(){
        player.update();

        if(!gameOver) {
            for (int i = 0; i < obs.size(); i++) {
                obs.get(i).update();
                if (Rect.intersects(player.getBound(), obs.get(i).getBound())) {
                    gameOver = true;
                }

                if (!gameOver && i % 2 == 0) {
                    if (player.getMidX() == obs.get(i).getMidX()) {
                        score++;
                    }
                }

                if (obs.get(i).getPosX() < 0 - 100) {
                    obs.remove(i);
                    i--;
                }
            }
        }

        if(player.getMidY() > HEIGHT * 3/4){
            gameOver = true;
        }
    }


    public void draw(){
        //check if surface is valid
        if(surfaceHolder.getSurface().isValid()){
            //lock the canvas
            canvas = surfaceHolder.lockCanvas();
            //set whole canvas black
            canvas.drawColor(Color.GRAY);

            //draw here
            paint.setColor(Color.WHITE);
            canvas.drawLine(0, HEIGHT * 3/4, WIDTH, HEIGHT * 3/4, paint);

            for(Obstacle o : obs){
                o.draw(canvas, paint);
                // help to test the score condition
                paint.setColor(Color.WHITE);
                canvas.drawLine(o.getMidX(), 0, o.getMidX(), HEIGHT, paint);
            }

            player.draw(canvas, paint);

            //testing player position
            canvas.drawLine(player.getMidX(), 0, player.getMidX(), HEIGHT, paint);
            canvas.drawLine(0, player.getMidY(), WIDTH,  player.getMidY(), paint);

            paint.setColor(Color.WHITE);

            paint.setTypeface(Typeface.create("arail", Typeface.BOLD));
            paint.setTextSize(20 * density);
            canvas.drawText(score+"", WIDTH/2, HEIGHT*1/8, paint);

            if(gameOver){	//testing
                paint.setColor(Color.BLACK);
                canvas.drawText("Game Over", 100 * density, 100 * density, paint);
                canvas.drawText("press space to restart", 50 * density, 150 * density, paint);
            }

            //unlock the canvas and post on surface view
            surfaceHolder.unlockCanvasAndPost(canvas);

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            if(player.getMidY() > HEIGHT * 3/4){
                if(gameOver){
                    gameOver = false;
                    init();

                    return true;
                }
            }

            if(!gameOver){
                player.setJump(true);
            }
            return true;
        }

        if(event.getAction()==MotionEvent.ACTION_UP){
            player.setJump(false);
            return false;
        }
        return false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        WIDTH = this.getWidth();
        HEIGHT = this.getHeight();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        WIDTH = this.getWidth();
        HEIGHT = this.getHeight();

    }

    public void pause(){
        isRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume(){
        gameThread = new Thread(this);
        gameThread.start();
        isRunning = true;
    }
}

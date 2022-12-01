package com.example.exe1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Timer;
import java.util.TimerTask;

public class Activity_Game extends AppCompatActivity {


    private final int ROWS = 4;
    private final int COLS = 3;
    private final int LIFE = 3;
    private int score=0;
    private final int DELAY =1000 ;

    private ShapeableImageView game_IC_heart1;
    private ShapeableImageView game_IC_heart2;
    private ShapeableImageView game_IC_heart3;
    private ShapeableImageView[] game_IC_hearts;
    private ShapeableImageView[][] game_IMG_dardasim;
    private MaterialButton game_BTN_play;
    private MaterialButton game_BTN_stop;
    private ExtendedFloatingActionButton game_BTN_left;
    private ExtendedFloatingActionButton game_BTN_right;
    private MaterialButton game_BTN_start;
    private ShapeableImageView[] game_IMG_player = new ShapeableImageView[COLS];
    private RelativeLayout game_RTL_start;
    private Timer timer;
    private ImageView game_IMG_background;


    private GameManager gameManager = new GameManager(ROWS,COLS,LIFE);

    private boolean isCrashed=false;
    private boolean isStartGame=false;



    private enum TIMER_STATUS {
        OFF,
        RUNNING,
    }
    private TIMER_STATUS timer_status = TIMER_STATUS.OFF;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        findViews();
        game_RTL_start.setVisibility(View.VISIBLE);
        initViews();

    }
    private void initViews() {
        game_BTN_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
                startTimer();
            }
        });

        game_BTN_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked(eMove.right);
            }
        });

        game_BTN_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked(eMove.left);
            }
        });

        game_BTN_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onResume();
            }
        });

        game_BTN_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPause();
            }
        });


    }

    private void clicked(eMove move) {
        game_IMG_player[gameManager.getPlayer().col].setVisibility(View.INVISIBLE);
        gameManager.movePlayer(move);
        isCrushed();
        game_IMG_player[gameManager.getPlayer().getCol()].setVisibility(View.VISIBLE);
    }


        private void startTimer() {
            if(timer_status==TIMER_STATUS.OFF) {
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                gameManager.nextMoveEnemy();
                                score++;
                                refreshUI();
                            }
                        });
                    }
                }, DELAY, DELAY);
                timer_status = TIMER_STATUS.RUNNING;
            }
            else return;
        }




    private void startGame() {
        isStartGame=true;
        game_RTL_start.setVisibility(View.INVISIBLE);
        game_IMG_player[gameManager.getPlayer().col].setVisibility(View.VISIBLE);
        refreshUI();

    }

    private void stopTimer() {
            if(timer_status==TIMER_STATUS.RUNNING) {
                timer_status = TIMER_STATUS.OFF;
                timer.cancel();
            }
        }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }


    private void refreshUI() {
        int enemyCol;
        if(isCrashed){
            createPlayerArray();
            isCrashed=false;
        }

        for (int i = 0; i <LIFE-gameManager.getLife() ; i++) {
            game_IC_hearts[i].setVisibility(View.INVISIBLE);
        }

        for (int i = 0; i <ROWS-1 ; i++) {
            for (int j = 0; j <COLS ; j++) {
                game_IMG_dardasim[i][j].setVisibility(View.INVISIBLE);
            }
        }
        for (int i = 0; i <ROWS-1; i++) {
                enemyCol = gameManager.getEnemyCol(i);
                if(enemyCol>=0)
                    game_IMG_dardasim[i][enemyCol].setVisibility(View.VISIBLE);
            }
        isCrushed();
    }

    private void isCrushed() {
        if (gameManager.isCrash()) {
            Glide.with(this).load(R.drawable.ic_boom).into(game_IMG_player[gameManager.getPlayer().col]);
            isCrashed = true;
            vibrate();

            if (gameManager.isEndGame()) {
                stopTimer();
                openScorePage(score);
            }
        }
    }



    private void findViews() {

        game_IC_hearts = new ShapeableImageView[]{
                game_IC_heart1 = findViewById(R.id.game_IC_heart1),
                game_IC_heart2 = findViewById(R.id.game_IC_heart2),
                game_IC_heart3 = findViewById(R.id.game_IC_heart3)
        };

        game_BTN_play = findViewById(R.id.game_BTN_play);
        game_BTN_stop = findViewById(R.id.game_BTN_stop);
        game_BTN_left = findViewById(R.id.game_BTN_left);
        game_BTN_right = findViewById(R.id.game_BTN_right);
        game_BTN_start = findViewById(R.id.game_BTN_start);
        game_RTL_start= findViewById(R.id.game_RTL_start);
        game_IMG_background=findViewById(R.id.game_IMG_background);
        Glide.with(this).load(R.drawable.back).into(game_IMG_background);

        createMat();
        createPlayerArray();
    }

    private void openScorePage(int score) {
        Intent intent= new Intent(this, Activity_Score.class);
        intent.putExtra(Activity_Score.KEY_SCORE,score);
        startActivity(intent);
        finish();
    }


    private void createPlayerArray() {
        int playerRow = ROWS-1;
        for (int i = 0; i <COLS ; i++) {
            game_IMG_player[i] = findViewById(getResources().getIdentifier("game_IMG_dardasim" + 3 + "" + i, "id", getPackageName()));
            Glide.with(this).load(R.drawable.img_dardas).into(game_IMG_player[i]);
            game_IMG_player[i].setVisibility(View.INVISIBLE);
        }
        game_IMG_player[gameManager.getPlayer().col].setVisibility(View.VISIBLE);
    }


    private void createMat() {
        game_IMG_dardasim= new ShapeableImageView[ROWS][COLS];

        for(int i = 0; i < ROWS; i++) {
            for (int j = 0; j <COLS; j++) {
                game_IMG_dardasim[i][j] = findViewById(getResources().getIdentifier("game_IMG_dardasim" + i + "" + j, "id", getPackageName()));
                Glide.with(this).load(R.drawable.gargamel1).into(game_IMG_dardasim[i][j]);
                game_IMG_dardasim[i][j].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void vibrate() {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                v.vibrate(500);
            }
        }

    @Override
    protected void onResume() {
        super.onResume();
        if(isStartGame==true)
            startTimer();
    }
    }


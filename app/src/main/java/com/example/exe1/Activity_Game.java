package com.example.exe1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Timer;
import java.util.TimerTask;

public class Activity_Game extends AppCompatActivity {


    private final int ROWS = 5;
    private final int COLS = 5;
    private final int LIFE = 3;
    private int score = 0;
    private int delay;
    private final int SLOW_DELAY = 1000;
    private final int FAST_DELAY = 500;

    private TextView game_TXT_score;
    private ShapeableImageView game_IC_heart1;
    private ShapeableImageView game_IC_heart2;
    private ShapeableImageView game_IC_heart3;
    private ShapeableImageView[] game_IC_hearts;
    private ShapeableImageView[][] game_IMG_dardasim;
    private ShapeableImageView[][] game_IMG_coins;
    private MaterialButton game_BTN_play;
    private MaterialButton game_BTN_stop;
    private ExtendedFloatingActionButton game_BTN_left;
    private ExtendedFloatingActionButton game_BTN_right;
    //private MaterialButton game_BTN_start;
    private ShapeableImageView[] game_IMG_player = new ShapeableImageView[COLS];
    private RelativeLayout game_RTL_start;
    private RelativeLayout game_RLT_arrows;
    private Timer timer;
    private ImageView game_IMG_background;
    MediaPlayer mp;
    float temp =0;

    private GameManager gameManager = new GameManager(ROWS, COLS, LIFE);

    private eGameMode gameMode;

    private boolean isCrashed = false;
    private boolean isStartGame = false;
    GameService gameService;


    private enum TIMER_STATUS {
        OFF,
        RUNNING,
    }

    private TIMER_STATUS timer_status = TIMER_STATUS.OFF;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent previousIntent = getIntent();
        String enum_name = previousIntent.getExtras().getString(DataManager.GAME_MODE);
        gameMode = eGameMode.valueOf(enum_name);

        gameService = new GameService(this);

        findViews();
        initViews();

        if (gameMode == eGameMode.SLOW_ARROWS || gameMode == eGameMode.FAST_ARROWS) {
            initViewArrows();
        } else if (gameMode == eGameMode.SENSOR) {
            initViewSensor();
        }

        startGame();
        stopTimer();
    }

    private void initViewSensor() {
        delay = SLOW_DELAY;
        game_RLT_arrows.setVisibility(View.INVISIBLE);
        gameService.setCallBack_moveBySensor(callBack_moveBySensor);
        gameService.sensorsUp();
    }

    private void initViewArrows() {

        game_BTN_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMove(eMove.right);
            }
        });

        game_BTN_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMove(eMove.left);
            }
        });


        if (gameMode == eGameMode.SLOW_ARROWS)
            delay = SLOW_DELAY;
        else if (gameMode == eGameMode.FAST_ARROWS)
            delay = FAST_DELAY;
    }


    private void initViews() {

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

    private void changeMove(eMove move) {
        game_IMG_player[gameManager.getPlayer().col].setVisibility(View.INVISIBLE);
        gameManager.movePlayer(move);
        isCrushed();
        game_IMG_player[gameManager.getPlayer().getCol()].setVisibility(View.VISIBLE);
    }


    private void startTimer() {
        if (timer_status == TIMER_STATUS.OFF) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gameManager.nextMoveEnemy();
                            gameManager.nextMoveCoin();
                            score++;
                            refreshUI();
                        }
                    });
                }
            }, delay, delay);
            timer_status = TIMER_STATUS.RUNNING;
        } else return;
    }


    private void startGame() {
        isStartGame = true;
        refreshUI();

    }

    private void stopTimer() {
        if (timer_status == TIMER_STATUS.RUNNING) {
            timer_status = TIMER_STATUS.OFF;
            timer.cancel();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
        if (gameMode == eGameMode.SENSOR) {
            gameService.unregister();
        }
        gameService.soundOff();
    }


    private void refreshUI() {
        int enemyCol;
        int coinsCol;
        if (isCrashed) {
            createPlayerArray();
            isCrashed = false;
        }
        game_TXT_score.setText(" "+score);

        for (int i = 0; i < LIFE - gameManager.getLife(); i++) {
            game_IC_hearts[i].setVisibility(View.INVISIBLE);
        }

        for (int i = 0; i < ROWS - 1; i++) {
            for (int j = 0; j < COLS; j++) {
                game_IMG_dardasim[i][j].setVisibility(View.INVISIBLE);
                game_IMG_coins[i][j].setVisibility(View.INVISIBLE);
            }
        }
        for (int i = 0; i < ROWS - 1; i++) {
            enemyCol = gameManager.getEnemyCol(i);
            coinsCol = gameManager.getCoinsCol(i);
            if (enemyCol >= 0)
                game_IMG_dardasim[i][enemyCol].setVisibility(View.VISIBLE);
            if (coinsCol >= 0)
                game_IMG_coins[i][coinsCol].setVisibility(View.VISIBLE);
        }

        isCrushed();
        isCatchACoin();
    }

    private void isCrushed() {
        if (gameManager.isCrash()) {
            Glide.with(this).load(R.drawable.ic_boom).into(game_IMG_player[gameManager.getPlayer().col]);
            isCrashed = true;
            gameService.vibrate();
            gameService.explosionSound();
            if (gameManager.isEndGame()) {
                stopTimer();
                openScorePage(score);
            }
        }
    }

    private void isCatchACoin() {
        if (gameManager.isCatchACoin()) {
            gameService.coinSound();
            score += 10;
        }
    }


    private void findViews() {

        game_IC_hearts = new ShapeableImageView[]{
                game_IC_heart1 = findViewById(R.id.game_IC_heart1),
                game_IC_heart2 = findViewById(R.id.game_IC_heart2),
                game_IC_heart3 = findViewById(R.id.game_IC_heart3)
        };
        game_IMG_dardasim = new ShapeableImageView[ROWS][COLS];
        game_IMG_coins = new ShapeableImageView[ROWS][COLS];
        game_BTN_play = findViewById(R.id.game_BTN_play);
        game_BTN_stop = findViewById(R.id.game_BTN_stop);
        game_BTN_left = findViewById(R.id.game_BTN_left);
        game_BTN_right = findViewById(R.id.game_BTN_right);
        game_RLT_arrows = findViewById(R.id.game_RLT_arrows);
        game_IMG_background = findViewById(R.id.game_IMG_background);
        Glide.with(this).load(R.drawable.back).into(game_IMG_background);
        game_TXT_score = findViewById(R.id.game_TXT_score);
        createMat(game_IMG_dardasim, "game_IMG_dardasim", R.drawable.gargamel1);
        createMat(game_IMG_coins, "game_IMG_coins", R.drawable.ic_coins);
        createPlayerArray();
    }

    private void openScorePage(int score) {
        Intent intent = new Intent(this, Activity_Score.class);
        intent.putExtra(Activity_Score.KEY_SCORE, score);
        startActivity(intent);
        finish();
    }

    GameService.CallBack_moveBySensor callBack_moveBySensor = (x,y) -> {
        Log.d("myLog", "x=" + x + "y=" + y + "_____");

        if( x> temp && x>0.5)
            changeMove(eMove.left);
        else if(x<temp && x<-0.5)
            changeMove(eMove.right);
        temp=x;

    };

    private void createPlayerArray() {
        int playerRow = ROWS - 1;
        for (int i = 0; i < COLS; i++) {
            game_IMG_player[i] = findViewById(getResources().getIdentifier("game_IMG_dardasim" + 4 + "" + i, "id", getPackageName()));
            Glide.with(this).load(R.drawable.img_smurf).into(game_IMG_player[i]);
            game_IMG_player[i].setVisibility(View.INVISIBLE);
        }
        game_IMG_player[gameManager.getPlayer().col].setVisibility(View.VISIBLE);
    }


    private void createMat(ShapeableImageView[][] mat, String matName, int img) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                mat[i][j] = findViewById(getResources().getIdentifier(matName + i + "" + j, "id", getPackageName()));
                Glide.with(this).load(img).into(mat[i][j]);
                mat[i][j].setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isStartGame == true) {
            startTimer();
        }
        if (gameMode == eGameMode.SENSOR) {
            gameService.register();
        }
        gameService.soundOn();

    }
    }


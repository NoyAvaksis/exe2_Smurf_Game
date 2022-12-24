package com.example.exe1;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

public class GameService {

    public interface CallBack_moveBySensor{
        void moveBySensor(float x, float y);
    }

    private static GameService _instance = null;
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private Context context;
    private MediaPlayer mediaPlayer;
    private CallBack_moveBySensor callBack_moveBySensor;

    public GameService(Context context) {
    this.context = context;
    mediaPlayer = MediaPlayer.create(this.context, R.raw.msc_the_tmurfs);
    mediaPlayer.setLooping(true);
    }

    public CallBack_moveBySensor getCallBack_moveBySensor() {
        return callBack_moveBySensor;
    }

    public static void initHelper(Context context){
        if(_instance==null) {
            _instance = new GameService(context);
        }
    }

    public GameService setCallBack_moveBySensor(CallBack_moveBySensor callBack_moveBySensor) {
        this.callBack_moveBySensor = callBack_moveBySensor;
        return this;
    }

    public GameService sensorsUp(){
        sensorManager = (SensorManager) context.getSystemService(this.context.SENSOR_SERVICE);
        sensor= sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        activateSensor();
        return this;
    }

    private void activateSensor() {
    sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

         float x = event.values[0];
         float y = event.values[1];
         callBack_moveBySensor.moveBySensor(x, y);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    }

    public static GameService getGameService(){
        return _instance;
    }

    public void vibrate() {
        Vibrator v = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    public void register(){
        sensorManager.registerListener(sensorEventListener,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }
    public void unregister() {
        sensorManager.unregisterListener(sensorEventListener);
    }


    public void soundOn() {
        mediaPlayer.start();
    }
    public void soundOff(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }

    public void explosionSound(){
        MediaPlayer mediaPlayerExplosion = MediaPlayer.create(this.context, R.raw.snd_hit);
        mediaPlayerExplosion.start();
    }
    public void coinSound(){
        MediaPlayer mediaPlayerCoin = MediaPlayer.create(this.context, R.raw.snd_coin);
        mediaPlayerCoin.start();
    }

}

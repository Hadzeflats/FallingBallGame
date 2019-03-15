package com.hadzy.fallingball;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class OrientationData implements SensorEventListener {
    private SensorManager manager;
    private Sensor accelerometer;
    private Sensor magnometer;

    private float[] accelOutput;
    private float[] magOutput;

    private float[] orientation = new float[3];

    public float[] getOrientation() {
        return orientation;
    }

    //Compare orientation to startOrientation. startOrientation is when new game is started.
    private float[] startOrientation = null;

    public float[] getStartOrientation() {
        return startOrientation;
    }

    //Resets the startOrientation every time a newGame is started. Resets referencepoint to where your phone is when you start new game.
    public void newGame() {
        startOrientation = null;
    }

    public OrientationData(){
        manager = (SensorManager)Constants.CURRENT_CONTEXT.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnometer = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }
    //Register the sensors to this class, listening for value changes in them.
    public void register(){
        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        manager.registerListener(this, magnometer, SensorManager.SENSOR_DELAY_GAME);
    }
    //unregister the listener. Prevents use of unnecessary resources.
    public void pause(){
        manager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            accelOutput = event.values;
        else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            magOutput = event.values;
        //calculates the orientation
        if (accelOutput != null && magOutput != null){
            float[] R = new float[9];
            float[] I = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, accelOutput, magOutput);
            if(success){
                SensorManager.getOrientation(R, orientation);
                if(startOrientation == null){
                    startOrientation = new float[orientation.length];
                    //can't use startOrientation = orientation
                    System.arraycopy(orientation, 0 , startOrientation, 0 , orientation.length);
                }
            }
        }

    }
}

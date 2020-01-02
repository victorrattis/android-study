package com.study.drawcircle;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private OpenGLView openGLView;
    private final static int SCALE = 1;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openGLView = findViewById(R.id.openGlView);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        openGLView.onResume();

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause(){
        super.onPause();
        openGLView.onPause();
        sensorManager.unregisterListener(this);
    }

    private int overflow;
    private final static int OVERFLOW_LIMIT = 20;
    private float[][] movingAverage = new float[2][OVERFLOW_LIMIT];

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("devlog", "onSensorChanged: " + (event.sensor.getType()));
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = Math.round(event.values[0] * 100.0) / 100f;
            float y = Math.round(event.values[1] * 100.0) / 100f;

            movingAverage[0][overflow] = x;
            movingAverage[1][overflow] = y;

            float s1 = calculateAverage(movingAverage[0]);
            float s2 = calculateAverage(movingAverage[1]);

            Log.d("devlog", "openGLView.renderer.objectsReady: " + openGLView.renderer.objectsReady);
            if (openGLView.renderer.objectsReady) {
                openGLView.renderer.getCircle().CalculatePoints(s1 / SCALE, s2 / SCALE, 0.1f, 55);
                openGLView.requestRender();
            }
        }
        overflow += 1;
        if (overflow >= OVERFLOW_LIMIT) {
            overflow = 0;
        }
    }

    private float calculateAverage(float[] input) {
        DoubleStream io = IntStream.range(0, input.length)
                .mapToDouble(i -> input[i]);
        float sum = (float)io.sum();
        return sum/OVERFLOW_LIMIT;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

package com.techtown.remotesensor1;

public interface remoteSensorEventListener {
    public abstract void onSensorChanged(float[] values);
    public abstract void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy);
}

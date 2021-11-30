package com.techtown.remotesensor1;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.renderscript.RenderScript;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.ReentrantLock;

public class RemoteSensor {
    public Socket socket;
    public DataInputStream readSocket;
    public DatagramSocket sendSocket;
    public ServerSocket serverSocket;

    public Socket clientSocket;
    public DataOutputStream writeSocket;
    public DatagramSocket recvSocket;

    public InetAddress ia;

    public Handler mHandler = new Handler();

    public String ip;
    public int tcp_port;
    public int udp_port;

    public Sensor sensor = null;
    public SensorManager sensorManager;
    public SensorEventListener serverListener;
    public remoteSensorEventListener remoteListener;

    public float[] values;
    public int sensorType;
    public int samplingPeriodUS;
    public int accuracy;
    public int valueSize = 0;

    public Context mContext;

    public ReentrantLock locker = new ReentrantLock();

    public void setServer(Context context, int tcp_port, int udp_port) {
        this.mContext = context;
        this.tcp_port = tcp_port;
        this.udp_port = udp_port;
        (new SetServer()).start();
    }

    public void closeServer() {
        (new CloseServer()).start();
    }

    public void setNetwork(String ip, int tcp_port, int udp_port) {
        this.ip = ip;
        this.tcp_port = tcp_port;
        this.udp_port = udp_port;
    }

    public void registerListener(remoteSensorEventListener listener, android.hardware.Sensor sensor, int samplingPeriodUS) {
        remoteListener = listener;
        this.sensor = sensor;
        this.samplingPeriodUS = samplingPeriodUS;

        (new Connect()).start();
        try {
            Thread.sleep(1000);
            (new recvUDP()).start();
            (new sendRequest()).start();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void unregisterListener(remoteSensorEventListener listener) {
        (new Disconnect()).start();
    }

    public class Connect extends Thread {
        public void run() {

            try {
                clientSocket = new Socket(ip, tcp_port);
                writeSocket = new DataOutputStream(clientSocket.getOutputStream());

            } catch (Exception e) {
                Log.d("Connect Error", e.getMessage());
            }

        }
    }

    public class Disconnect extends Thread {
        public void run() {
            try {
                byte[] b = new byte[100];
                String request = "#";
                b = request.getBytes(StandardCharsets.UTF_8);
                writeSocket.write(b);

                if (clientSocket != null)
                    clientSocket.close();
                if (writeSocket != null)
                    writeSocket.close();
                if (recvSocket != null)
                    recvSocket.close();

            } catch (Exception e) {
                Log.d("Unregister Error", e.getMessage());
            }
        }
    }

    public class sendRequest extends Thread {
        public void run() {

            try {
                byte[] b = new byte[100];
                String request = Integer.toString(sensor.getType()) + "@" + Integer.toString(samplingPeriodUS);
                if (request != null) {
                    b = request.getBytes(StandardCharsets.UTF_8);
                    writeSocket.write(b);
                }

            } catch (Exception e) {
                Log.d("Sending Request Error", e.getMessage());
            }

        }
    }

    public class recvUDP extends Thread {
        String text;
        DatagramPacket dp2;

        public void run() {
            try {
                recvSocket = new DatagramSocket(udp_port);

                while (true) {
                    byte[] message = new byte[1000];
                    float[] myvalue = new float[3];
                    int i = 0;

                    dp2 = new DatagramPacket(message, message.length);
                    recvSocket.receive(dp2);
                    text = new String(message, 0, dp2.getLength());

                    String[] textArr = text.split("@");

                    for (; i < textArr.length - 1; i++) {
                        myvalue[i] = Float.parseFloat(textArr[i]);
                    }
                    accuracy = Integer.parseInt(textArr[i]);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            remoteListener.onSensorChanged(myvalue);
                            remoteListener.onAccuracyChanged(sensor, accuracy);
                        }
                    });
                }

            } catch (Exception e) {
                Log.d("Receiving UDP Error", e.getMessage());
            }

        }
    }

    public class SetServer extends Thread {

        public void run() {
            int flag = 0;
            try {
                serverSocket = new ServerSocket(tcp_port);
                sendSocket = new DatagramSocket(udp_port);

                socket = serverSocket.accept();
                readSocket = new DataInputStream(socket.getInputStream());

                while (true) {
                    byte[] b = new byte[100];
                    int ac = readSocket.read(b, 0, b.length);
                    String input = new String(b, 0, b.length);
                    final String recvInput = input.trim();

                    if (recvInput.equals("#")) {
                        flag = 0;
                        (new CloseServer()).start();
                        break;
                    } else if (flag == 0 && !recvInput.isEmpty()) {
                        flag = 1;
                        String[] requestArr = new String[2];
                        requestArr = recvInput.split("@");

                        sensorType = Integer.parseInt(requestArr[0]);
                        samplingPeriodUS = Integer.parseInt(requestArr[1]);

                        sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
                        sensor = sensorManager.getDefaultSensor(sensorType);
                        serverListener = new serverSensorListener();
                        sensorManager.registerListener(serverListener, sensor, samplingPeriodUS);
                    }
                    ia = socket.getInetAddress();
                    if (ac == -1)
                        break;
                }


            } catch (Exception e) {
                Log.d("Set Sever Error", e.getMessage());
            }

        }
    }


    public class CloseServer extends Thread {
        public void run() {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
                if (sendSocket != null) {
                    sendSocket.close();
                }
                if (socket != null) {
                    socket.close();
                }
                if (sensorManager != null) {
                    sensorManager.unregisterListener(serverListener);
                }

            } catch (Exception e) {
                Log.d("Close Server Error", e.getMessage());
            }

        }
    }

    public class sendUDP extends Thread {
        public void run() {
            try {
                String msg = "";
                DatagramPacket dp;
                for (int i = 0; i < valueSize; i++) {
                    msg = msg.concat(Float.toString(values[i]));
                    msg = msg.concat("@");
                }
                msg = msg.concat(Integer.toString(accuracy));
                int length = msg.length();
                byte[] msgbyte = msg.getBytes();
                dp = new DatagramPacket(msgbyte, length, ia, udp_port);
                sendSocket.send(dp);

            } catch (Exception e) {
                Log.d("Sending UDP Error", e.getMessage());

            }
        }
    }

    public class serverSensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            valueSize = sensorEvent.values.length;
            values = new float[valueSize];

            for (int i = 0; i < valueSize; i++) {
                values[i] = sensorEvent.values[i];
            }
            accuracy = sensorEvent.accuracy;

            new sendUDP().start();
        }

        @Override
        public void onAccuracyChanged(android.hardware.Sensor sensor, int i) {

        }

    }
}
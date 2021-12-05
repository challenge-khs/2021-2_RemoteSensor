# Remote Sensor

It is an Android library that helps utilize sensor values of other devices by transmitting sensor values of one device to another.   
After adding the library to the lib folder of the desired application in Android Studio, it can be used through "jar file right-click>[Add as Library]. In addition, since this library is based on wi-fi communication, Wi-fi permission should be added to "Android Manifest.xml".
~~~ xml
  <uses-permission android:name="android.permission.INTERNET"/>
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
  <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
~~~ 
***
## Class

|Class|detail|
|------|------|
|SetServer|It provides server functionality for the device that sends sensor values.|
|CloseServer|It contains the functionality to close all sockets for the device that sends sensor values.|
|Connect|It provide connection functionality for the device that receives sensor values.|
|Disconnect|It contains the functionality to close all sockets for the device that receives sensor values.|
|sendRequest|It contains the functionality to send request sensor values with sensor type and delay to server.| 
|sendUDP|It contains the functionality to send sensor values through UDP socket.|
|recvUDP|It contains the functionality to receive sensor values through UDP socket.|
|serverSensorListener|The class is for registering sensor in remote device.|
   
   
## Interface

|interface|detail|
|------|------|
|remoteSensorEventListener|Uses like sensorEventListener, but the sensor value is called in form of float. and it contains two abstract methods (onSensorChanged, onAccuracyChanged).|

## Method

|method|detail|
|------|------|
|setServer|Opens TCP and UDP server calling "SetServer" class.|
|closeServer|Closes TCP and UDP server calling "CloseServer" class.|
|setNetwork|sets IP address, TCP port and UDP port to connect to server.|
|registerListener|Registers remote device's sensor calling "Connect", "recvUDP" and "sendRequest" classes.|
|unregisterListener|Unregisters remote device's sensor calling "Disconnect" class.|
   
</br>
   
### * **setServer**   
It opens TCP and UDP sockets for server calling "SetServer" class.
~~~ java
public void setServer(Context context, int tcp_port, int upd_port);
~~~   

#### Parameter
|parameter|type|detail|
|---------|----|------|
|context|Context|It needs *MainActivity*'s Context. If you call this method in *MainActivity*, It can be used by using "this".|
|tcp_port|int|It can be used by entering a desired number as a port number for TCP socket communication.|
|udp_port|int|It can be used by entering a desired number as a port number for UDP socket communication.|

#### Simple call
~~~ java
RemoteSensor rs = new RemoteSensor();
rs.setServer(this, 8000, 8001);
~~~

</br>

### * **closeServer**
It closes TCP and UDP sockets for server calling "CloseServer" class. 
~~~ java
public void closeServer();
~~~
  

#### Simple call
~~~ java
RemoteSensor rs = new RemoteSensor();
rs.closeServer();
~~~

</br>

### * **setNetwork**

It sets TCP and UDP port numbers to connect to the opened server using the "setServer" method. 
~~~ java
public void setNetwork(String ip, int tcp_port, int udp_port);
~~~  

#### Parameter
|parameter|type|detail|
|---------|----|------|
|ip|String|It needs the server's IP address.|
|tcp_port|int|It needs the TCP port number used by the server.|
|udp_port|int|It needs the UDP port number used by the server.|

#### Simple call
~~~ java
RemoteSensor rs = new RemoteSensor();
rs.setNetwork("192.168.0.22", 8000, 8001);
~~~

</br>

### * **registerListener**

It registers remote device's sensor calling "Connect", "recvUDP" and "sendRequest" classes. 
~~~ java
public void registerListener(remoteSensorEventListener listener, Sensor sensor, int samplingPeriodUS);
~~~  

#### Parameter
|parameter|type|detail|
|---------|----|------|
|listener|remoteSensorEventListener(interface)|It needs to implement "onSensorChanged" and "onAccuracyChanged" as a substitute for "sensorEventListener".|
|sensor|android.hardware.Sensor|It needs the sensor object you want to register.|
|samplingPeriodUS|int|It is used to set delay period for sensor.|

#### Simple call
~~~ java
RemoteSensor rs = new RemoteSensor();
SensorManager manager = (SensorManager) getSystemService(SENSOR_SERVICE);
Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); // Enter the sensor you want.
remoteSensorEventListener lis = new remoteSensorEventListener() {
  @Override
  public void onSensorChanged(float[] values) {
   // Implement what you want.
   // values array is for the sensor value.
  }
  
  @Override
  public void onAccuracyChanged(Sensor sensor, int i) {
   // Implement what you want.  
  }

rs.setNetwork("192.168.0.22", 8000, 8001);
rs.registerListener(lis, sensor, SensorManager.SENSOR_DELAY_GAME); // Enter the delay you want.
~~~


</br>

### * **unregisterListener**
It unregisters remote device's sensor calling "Disconnect" class. 
~~~ java
public void unregisterListener(remoteSensorEventListner listener);
~~~
#### Parameter
|parameter|type|detail|
|---------|----|------|
|listener|remoteSensorEventListener(interface)|It is remoteSensorEventListener to unregister.|

#### Simple call
~~~ java
RemoteSensor rs = new RemoteSensor();
// remoteSensorEventListener registration process
// ...
rs.unregisterListener(listener);
~~~

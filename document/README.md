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
   
   
### **setServer**   
~~~ java
public void setServer(Context context, int tcp_port, int upd_port);
~~~   
It opens TCP and UDP sockets for server calling "SetServer" class.

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

### **closeServer**
~~~ java
public void closeServer();
~~~
It closes TCP and UDP sockets for server calling "CloseServer" class.   

#### Simple call
~~~ java
RemoteSensor rs = new RemoteSensor();
rs.closeServer();
~~~

**Remote Sensor**
----
It is an Android library that helps utilize sensor values of other devices by transmitting sensor values of one device to another.   
After adding the library to the lib folder of the desired application in Android Studio, it can be used through "jar file right-click>[Addas Library]. In addition, since this library is based on wi-fi communication, Wi-fi permission should be added to "Android Manifest.xml".
~~~ xml
  <uses-permission android:name="android.permission.INTERNET"/>
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
  <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
~~~ 
----
* **Class:**

|interface|detail|
|------|------|
|SetServer|It provides server functionality for the device that sends sensor values.|
|CloseServer|It contains the functionality to close all sockets for the device that sends sensor values.|
|Connect|It provide connection functionality for the device that receives sensor values.|
|Disconnect|It contains the functionality to close all sockets for the device that receives sensor values.|
|sendRequest|It contains the functionality to send request sensor values with sensor type and delay to server.| 
|sendUDP|It contains the functionality to send sensor values through UDP socket.|
|recvUDP|It contains the functionality to receive sensor values through UDP socket.|
|serverSensorListener|The class is for registering sensor in remote device.|

* **interface:**

|interface|detail|
|------|------|
|remoteSensorEventListener|Uses like sensorEventListener, but the sensor value is called in form of float. and it contains two abstract methods (onSensorChanged, onAccuracyChanged).|

* **Method:**

|method|detail|
|------|------|
|setServer|Opens TCP and UDP server calling "SetServer" class.|
|closeServer|Closes TCP and UDP server calling "CloseServer" class.|
|setNetwork|sets IP address, TCP port and UDP port to connect to server.|
|registerListener|Registers remote device's sensor calling "Connect", "recvUDP" and "sendRequest" classes.|
|unregisterListener|Unregisters remote device's sensor calling "Disconnect" class.|

  ~~~ java
  public class SetServer;
  public class CloseServer;   
  public class Connect;   
  public class Disconnect;
  public class sendRequest;
  public class sendUDP;
  public class recvUDP;
  public class serverSensorListener;
  ~~~
  ~~~ java
  public interface remoteSensorEventListener {
    public abstract void onSensorChanged(float[] values);
    public abstract void onAccuracyChanged(Sensor sensor, int accuracy);
    }
  ~~~
  ~~~ java
  public void setServer(Context context, int tcp_port, int udp_port);   
  public void closeServer();   
  public void setNetwork(String ip, int tcp_port, int udp_port);   
  public void registerListener(remoteSensorEventListener listener, Sensor sensor, int samplingPeriodUs);
  public void unregisterListener(remoteSensorEventListner listener);
  ~~~




* **Data Params**

  Context context, int tcp_port, int udp_port

* **Success Response:**
  
  <_What should the status code be on success and is there any returned data? This is useful when people need to to know what their callbacks should expect!_>

  * **Code:** 200 <br />
    **Content:** `{ id : 12 }`
 
* **Error Response:**

  <_Most endpoints will have many ways they can fail. From unauthorized access, to wrongful parameters etc. All of those should be liste d here. It might seem repetitive, but it helps prevent assumptions from being made where they should be._>

  * **Code:** 401 UNAUTHORIZED <br />
    **Content:** `{ error : "Log in" }`

  OR

  * **Code:** 422 UNPROCESSABLE ENTRY <br />
    **Content:** `{ error : "Email Invalid" }`

* **Sample Call:**

  <_Just a sample call to your endpoint in a runnable format ($.ajax call or a curl request) - this makes life easier and more predictable._> 

* **Notes:**

  <_This is where all uncertainties, commentary, discussion etc. can go. I recommend timestamping and identifying oneself when leaving comments here._> 

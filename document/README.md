**Remote Sensor**
----
  한 기기의 센서 값을 다른 기기로 전송함으로써 타 기기의 센서 값을 활용할 수 있도록 돕는 안드로이드 라이브러리이다.   
  Android Studio에서 원하는 애플리케이션의 lib 폴더에 해당 라이브러리를 추가한 후 "jar 파일 우클릭>[Add as Library]"을 통해 사용이 가능하다. 또한 이 라이브러리는 wi-fi 통신을 기초로 하기 때문에 "Android Manifest.xml"에 wi-fi permission에 관한 내용을 추가해야 한다.
~~~ xml
  <uses-permission android:name="android.permission.INTERNET"/>
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
  <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
~~~ 
----
**구성**
* **Class:**
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

* **interface:**
  ~~~ java
  public interface remoteSensorEventListener {
    public abstract void onSensorChanged(float[] values);
    public abstract void onAccuracyChanged(Sensor sensor, int accuracy);
    }
  ~~~


* **Method:**

|method|detail|
|------|------|
|setServer|Opens TCP and UDP server.|
|closeServer|Closes TCP and UDP server.|
|setNetwork|Connects to server with params.|
|registerListener|Registers remote device's sensor.|
|unregisterListener|Unregisters remote device's sensor.|
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

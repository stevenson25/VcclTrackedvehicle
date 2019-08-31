package com.example.vccl_trackedvehicle;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * Created by steven on 2015/4/5.
 */

public class BluetoothService {

    private BluetoothAdapter bluetoothAdapter ;
    private Handler handler;
    private Context context;
    private static final UUID MYUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    /**
     *  連線開始與斷線處裡
     *  1. DISCONNECT 斷線狀態    0
     *  2. CONNECTED  連線中      1
     *  3. CONNECTING 嘗試連線中  2
     **/

    private static int CONNECTFLAG = 0 ;
    private static boolean isdemorun = false ;
    private final static int _CONNECTED = 0x10 ;
    private final static int _DISCONNECT = 0x20 ;
    private final static int _FAULTCONNECT = 0x30 ;

    /**
     *  連線中狀態值
     *  1. DISCONNECT 斷線狀態    0
     *  2. CONNECTED  連線中      1
     *  3. CONNECTING 嘗試連線中  2
     **/

    private static int CONNECT_STATE = 0 ;
    private final static int DISCONNECT = 0 ;
    private final static int CONNECTED = 1 ;
    private final static int CONNECTING = 2 ;

    /**
     *  連線中狀態值
     *  1. DATAWAIT   等待資料中    0
     *  2. DATAREAD   資料讀取      1
     *  3. DATAWIRTE  資料寫入      2
     **/
    private static int READORWIRTE = 0 ;
    private final static int DATAWAIT = 0 ;
    private final static int DATAREAD = 1 ;
    private final static int DATAWIRTE = 2 ;

    private Connecting connecting;
    private Connected connected;

    private static BluetoothService bluetoothService = new BluetoothService();
    private BluetoothService(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }
    public static BluetoothService  getBluetoothService(){
        return bluetoothService;
    }

    //中斷藍芽連線用
    public void DisConnect(){
        CONNECT_STATE = DISCONNECT;
        connected = null;
        connecting = null;
        handler.obtainMessage(CONNECT_STATE,0,_DISCONNECT,null).sendToTarget();
    }
    //開始藍芽連線用
    public void StartConnect(String devicename){
        bluetoothAdapter.cancelDiscovery();
        BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(devicename);
        if((connecting == null)|| (connected==null)){
            connecting = null;
            connected = null;
        }
        connecting = new Connecting(bluetoothDevice);
        connecting.start();
    }
    //測試藍芽連線用
    public void StartDemo(){
        if(!isdemorun){
            Thread TESTMODE = new TESTMODE();
            TESTMODE.start();
        }
        isdemorun = true;
    }

    //測試藍芽發送連線用
    public void StartSendDemo(){
        if(!isdemorun){
            Thread TESTMODE2 = new TESTMODE2();
            TESTMODE2.start();
        }
        isdemorun = true;
    }

    //檢查藍芽連限用
    public Boolean isConnectedCheck(){
        if(CONNECT_STATE == CONNECTED){
            return true;
        }else{
            return false;
        }

    }
    //藍芽連線用途。
    private class Connecting extends Thread {
       private String tag = "Connecting :";
       private BluetoothSocket socket;
       private BluetoothDevice devicename;
       public Connecting(BluetoothDevice devicename){
          if(connected != null){
              connected = null;
          }
          this.devicename = devicename;
       }
       @Override
       public void run() {
            super.run();
            try {
                Log.d(tag, "Start Connecting Thread");
                socket = devicename.createInsecureRfcommSocketToServiceRecord(MYUUID);
                socket.connect();
                CONNECT_STATE = CONNECTING;
                handler.obtainMessage(CONNECT_STATE).sendToTarget();
            }catch (IOException e){
                try {
                    Log.d(tag, "連線錯誤關閉連線");
                    CONNECT_STATE = DISCONNECT;
                    handler.obtainMessage(CONNECT_STATE,0,_FAULTCONNECT,null).sendToTarget();
                    socket.close();
                } catch (IOException O) {
                    Log.d(tag, "關閉錯誤");
                }
            }
           if(socket.isConnected()){
               handler.obtainMessage(CONNECT_STATE,0,_CONNECTED,null).sendToTarget();
               Log.d(tag, "Bluetooth device is Connected");
               CONNECT_STATE = CONNECTED;
               connected = new Connected(socket);
               connected.start();
           }
       }
    }
    //連線成功後，傳輸訊息用。
    private class Connected  extends Thread {
        private String tag = "Connected :";
        private String end ="\n";
        private BluetoothSocket socket;
        private InputStream datainput_int;
        private OutputStream datainput_Out;
        private StringBuilder curMsg = new StringBuilder();
        private byte[] buffer = new byte[1024];
        private int bytes;
        public Connected(BluetoothSocket socket){
            this.socket = socket;
            //destory connecting
            connecting = null;
        }

        @Override
        public  void run(){
            super.run();
            Log.i(tag, "BEGIN mConnectedThread");
            if (socket == null) {
                return;
            }
            try {
                datainput_int = socket.getInputStream();
                datainput_Out = socket.getOutputStream();
            }catch (Exception e){
                e.printStackTrace();
            }

            while (CONNECT_STATE == CONNECTED){
                try {

                    //接收資料
                    while ((bytes = datainput_int.available())>0) {
                        bytes = datainput_int.read(buffer);
                        String ss = new String(buffer, 0, bytes, Charset.forName("ISO-8859-1"));
                        curMsg.append(ss);
                        ss = null;
                        int endIdx = curMsg.indexOf(end);
                        if (endIdx != -1) {
                            String fullMessage = curMsg.substring(0, endIdx + end.length()-2);
                            curMsg.delete(0, endIdx + end.length());
                            READORWIRTE = DATAREAD;
                            handler.obtainMessage(CONNECT_STATE, READORWIRTE, 0, fullMessage).sendToTarget();
                        }
                    }

                    //發送資料

                }catch (Exception e){
                    CONNECT_STATE = DISCONNECT;
                    //handler.obtainMessage(CONNECT_STATE,0,0x30).sendToTarget();
                }
            }
            try {
                socket.close();
                socket = null;
                handler.obtainMessage(CONNECT_STATE,0,0x30).sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public void write(String data) {
            byte[] buffer;
            if(data!=null){
                buffer = data.getBytes();
                try {
                    datainput_Out.write(buffer);
                } catch (IOException e) {
                    Log.e("TAG", "Exception during write", e);
                }
            }
        }
    }

    //Demo用測試模式
    private static int a = 0;

    //測試接收線程
    private class TESTMODE extends Thread {
        private String tag = "TESTMODE :";
        private String DC1;
        private String DC2;
        private String DM1;
        private String DF1;
        private String DATA;
        public TESTMODE(){}

        @Override
        public  void run(){
            super.run();
            Log.i(tag, "BEGIN TEST MODE");
            while (true){
                try {
                    /*if(a == 0){
                        try {
                            Thread.sleep(1000);
                        }catch (Exception E) {

                        }
                        a++;
                    }else if(a == 1){
                        DATA =  "<DC1," + (int)(Math.random()*10)
                                +","+(int)(Math.random()*10)
                                +","+(int)(Math.random()*10)
                                +","+(int)((Math.random()*10))
                                +","+(int)((Math.random()*10))
                                +","+(int)(Math.random()*10)
                                +","+(int)(Math.random()*10)
                                +","+(int)(Math.random()*10)
                                +","+(int)(Math.random()*0)
                                +","+(int)(Math.random()*0);
                        a++;
                    }else if(a == 2){
                        DATA = "<DC2," + (int)(Math.random()*200)
                                +","+(int)(Math.random()*200)
                                +","+(int)(Math.random()*100)
                                +","+(int)((Math.random()*255-100))
                                +","+(int)((Math.random()*255-100))
                                +","+(int)(Math.random()*12)
                                +","+(int)(Math.random()*12)
                                +","+(int)(Math.random()*12)
                                +","+(int)(Math.random()*7)
                                +","+(int)(Math.random()*255);
                        a++;
                    }else if(a == 3){
                        DATA = "<DF1," + "20"
                                +","+(int)(Math.random()*1)
                                +","+(int)(Math.random()*65535)
                                +","+(int)((Math.random()*65535))
                                +","+(int)((Math.random()*65535))
                                +","+(int)(Math.random()*65535)
                                +","+(int)(Math.random()*0)
                                +","+(int)(Math.random()*0)
                                +","+(int)(Math.random()*0)
                                +","+(int)(Math.random()*0);
                        a++;
                    }else if(a == 4){
                        DATA = "<DM1," + (int)(Math.random()*6000)
                                +","+(int)(Math.random()*1000)
                                +","+(int)(Math.random()*1000)
                                +","+(int)((Math.random()*255-100))
                                +","+(int)((Math.random()*255-100))
                                +","+(int)(Math.random()*72)
                                +","+(int)(Math.random()*72)
                                +","+(int)(Math.random()*72)
                                +","+(int)(Math.random()*0)
                                +","+(int)(Math.random()*0);
                        a++;
                    }else if(a == 5){
                        DATA = "<GPS," + (int)(Math.random()*60)
                                + "," + (int)(Math.random()*60)
                                + "," + (int)(Math.random()*60)
                                + "," + (int)(Math.random()*60);
                        a++;
                    }else {
                        a = 0;
                    }
                    if(a!=6){
                        handler.obtainMessage(CONNECTED, DATAREAD, 0, DATA).sendToTarget();
                        System.out.println(DATA);
                    }*/
                    Thread.sleep(20);

                }catch (Exception e){
                    e.printStackTrace();
                }
                //handler.obtainMessage(CONNECT_STATE,0,0x30).sendToTarget();
            }

        }
    }

    //測試發送線程
    private class TESTMODE2 extends Thread {

        private String tag = "TESTMODE :";

        public TESTMODE2(){

        }

        @Override
        public  void run(){
            super.run();
            Log.i(tag, "BEGIN TEST MODE2");
            while (true){
                try {
                    connected.write("data123\n");
                    Thread.sleep(10);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }

    //發送數據用途
    public void write(String s){

        //Log.d("write" , s + "\n");
        connected.write(s + "\n");
    }

    public void setBtHandler(Handler handler){
        this.handler = handler;
    }
    public void setContexts(Context context){
        this.context = context;
    }
    public Boolean getDemoState(){
        return isdemorun;
    }

}

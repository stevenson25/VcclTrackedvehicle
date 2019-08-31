package com.example.vccl_trackedvehicle;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    BluetoothService bluetoothService = BluetoothService.getBluetoothService();
    Button button;
    Button sendbutton;                                                              //宣告發送按鈕
    EditText datadit;                                                                //宣告一個輸入檔
    Button straightbutton;                                                           //宣告前進按鈕
    BluetoothAdapter bluetoothAdapter;                                              //取得藍芽橋接器

    private SeekBar sb_normal;
    private TextView txt_cur;
    private Context mContext;
    TextView textView;
    public String boo = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = MainActivity.this;
        sb_normal = (SeekBar) findViewById(R.id.sb_normal);
        txt_cur = (TextView) findViewById(R.id.textView3 );
        sb_normal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txt_cur.setText("  SPEED  :" + progress + " / 100 ");
                if (progress<10)
                {
                    String str = Integer.toString(progress);
                    boo=("0"+str);

                }
                else
                {
                    String str = Integer.toString(progress);
                    // bluetoothService.write("/"+str)
                    boo=str;
                }
                if (progress==100)
                {
                    String str = Integer.toString(progress);
                    boo=("99");

                }
            }


            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(mContext, "触碰SeekBar", Toast.LENGTH_SHORT).show();
            }


            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(mContext, "放开SeekBar", Toast.LENGTH_SHORT).show();
            }
        });
        button = (Button) findViewById(R.id.connectbutton);     //BUTTON名稱
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Log:", "你按了按鈕");                     //debug模式下出現

                // 開始建立藍芽
//                bluetoothService.StartConnect("98:D3:31:90:93:29"); // 斜板藍芽  5年12月20日//14:88:2C:A0:81:A1  98:D3:31:FD:32:F4 STEVEN05 98:D3:31:80:A1:DA
                bluetoothService.StartConnect("98:D3:31:80:75:38"); // 斜板藍芽  5年12月20日

            }
        });
        button = (Button) findViewById(R.id.FORWARD);     //BUTTON名稱
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Log:", "你按了按鈕");                     //debug模式下出現蛋蛋

                // 藍芽傳A
                bluetoothService.write("1"+"/"+boo+":");

            }
        });
        button = (Button) findViewById(R.id.BACKWARD);     //BUTTON名稱
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Log:", "你按了按鈕");                     //debug模式下出現蛋蛋

                // 藍芽傳B
                bluetoothService.write("2"+"/"+boo+":");

            }
        });
        button = (Button) findViewById(R.id.LEFT);     //BUTTON名稱
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Log:", "你按了按鈕");                     //debug模式下出現蛋蛋

                // 藍芽傳C
                bluetoothService.write("3"+"/"+boo+":");

            }
        });
        button = (Button) findViewById(R.id.RIGHT);     //BUTTON名稱
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Log:", "你按了按鈕");                     //debug模式下出現蛋蛋

                // 藍芽傳D
                bluetoothService.write("4"+"/"+boo+":");

            }
        });
        button = (Button) findViewById(R.id.CW);     //BUTTON名稱
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Log:", "你按了按鈕");                     //debug模式下出現蛋蛋

                // 藍芽傳D
                bluetoothService.write("5"+"/"+boo+":");

            }
        });
        button = (Button) findViewById(R.id.CCW);     //BUTTON名稱
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Log:", "你按了按鈕");                     //debug模式下出現蛋蛋

                // 藍芽傳D
                bluetoothService.write("6"+"/"+boo+":");

            }
        });
        button = (Button) findViewById(R.id.STOP);     //BUTTON名稱//BOOST
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Log:", "你按了按鈕");                     //debug模式下出現蛋蛋

                // 藍芽傳D
                bluetoothService.write("7"+"/"+boo+":");

            }
        });
        button = (Button) findViewById(R.id.CONF);     //BUTTON名稱//BOOST
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Log:", "你按了按鈕");                     //debug模式下出現蛋蛋

                // 藍芽傳D
                bluetoothService.write("8"+"/"+boo+":");

            }
        });
        button = (Button) findViewById(R.id.CONR);     //BUTTON名稱//BOOST
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Log:", "你按了按鈕");                     //debug模式下出現蛋蛋

                // 藍芽傳D
                bluetoothService.write("9"+"/"+boo+":");

            }
        });
        button = (Button) findViewById(R.id.LOUT);     //BUTTON名稱//BOOST
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Log:", "你按了按鈕");                     //debug模式下出現蛋蛋

                // 藍芽傳D
                bluetoothService.write("A"+"/"+boo+":");

            }
        });
        button = (Button) findViewById(R.id.ROUT);     //BUTTON名稱//BOOST
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Log:", "你按了按鈕");                     //debug模式下出現蛋蛋

                // 藍芽傳D
                bluetoothService.write("B"+"/"+boo+":");

            }
        });
        textView = (TextView) findViewById(R.id.Pitch);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());                //TextView滾動設定
//        imageButton = (ImageButton) findViewById(R.id.Situation01);
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bluetoothService.write("A");
//                Log.d("Button", "A");
//                imageView2 = (ImageView) findViewById(R.id.Situation01);
//                imageView2.setImageResource(R.drawable.situation4);
//                imageView2 = (ImageView) findViewById(R.id.Situation02);
//                imageView2.setImageResource(R.drawable.situation2);
//            }
//        });

        //設定藍芽接收後的字串，進行運用
        bluetoothService.setBtHandler(BTPoccess);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    //手機頁面啟動時，呼叫
    @Override
    protected void onResume() {
        super.onResume();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Log.d("Log:", "您的裝置不支援藍芽");
        }
    }
    private Handler BTPoccess = new Handler() {

        boolean tagflag = false;
        String data;
        /**
         *  連線中狀態值
         *  1. DISCONNECT 斷線狀態    0
         *  2. CONNECTED  連線中      1
         *  3. CONNECTING 嘗試連線中  2
         **/
        private final static int DISCONNECT = 0;
        private final static int CONNECTED = 1;
        private final static int CONNECTING = 2;

        /**
         *  連線中狀態值
         *  1. DATAWAIT   等待資料中    0
         *  2. DATAREAD   資料讀取      1
         *  3. DATAWIRTE  資料寫入      2
         **/

        private final static int DATAWAIT = 0;
        private final static int DATAREAD = 1;
        private final static int DATAWIRTE = 2;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case DISCONNECT:
                    if (tagflag) Log.d("Handle", "DISCONNECT");
                    break;
                case CONNECTED:
                    //如成功連線，則拋出連線成功訊息，供使用者辨識
                    if (tagflag) Log.d("Handle", "CONNECTED");
                    if (msg.arg1 == DATAREAD) {
                        data = (String) msg.obj;
                        System.out.println(data);
                    }
                    break;
                case CONNECTING:
                    if (tagflag) Log.d("Handle", "CONNECTING");
                    break;
            }

            switch (msg.arg2) {
                case 0x20:
                    Toast.makeText(MainActivity.this, "斷開藍芽連線", Toast.LENGTH_SHORT).show();
                    break;
                case 0x10:
                    Toast.makeText(MainActivity.this, "藍芽連線成功", Toast.LENGTH_SHORT).show();
                    break;
                case 0x30:
                    Toast.makeText(MainActivity.this, "藍芽連線失敗，請確認硬體皆已就緒", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}

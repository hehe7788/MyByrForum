package byr.byrforum;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class OneActivity extends AppCompatActivity {
    String TAG  = "ONE.ACTIVITY";
    final String JSON_DATA = "jsondata";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
        final TextView title = (TextView) findViewById(R.id.one_title);
        final TextView content = (TextView) findViewById(R.id.one_content);

        Intent intent = getIntent();
        title.setText(intent.getExtras().toString());
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 124) {
                    content.setText(msg.getData().getString(JSON_DATA));
                }
            }
        };


        new Thread(){
            @Override
            public void run()
            {
                //android不允许在主线程访问网络，把网络访问的代码放在这里
                Log.e(TAG, "new thread");
                String inputLine;
                URL url;
                String data = "";
                try {
//                    properties = new URL("http://smartisian.club:5000/topic?board=wwwtechnology&id=33589&p=1");
                    url = new URL("http://192.168.191.1:5000/topic?board=wwwtechnology&id=33589&p=1");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                    while ((inputLine = reader.readLine()) != null) {
                        Log.e(TAG,inputLine);

                        data += inputLine;
                    }
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, data);
                //向主线程发消息
                Message msg = new Message();
                msg.what = 124;
                Bundle bundle = new Bundle();
                bundle.putString(JSON_DATA, data);
                msg.setData(bundle);
                handler.sendMessage(msg);


            }
        }.start();
    }
}

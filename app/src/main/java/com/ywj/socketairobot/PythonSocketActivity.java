package com.ywj.socketairobot;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class PythonSocketActivity extends AppCompatActivity {

    Button btn_start_client;

    Button btn_send;

    EditText et_history;
    EditText et_content;
    ScrollView sv_history;

    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_python_socket);


        btn_start_client = findViewById(R.id.btn_start_client);
        et_history = findViewById(R.id.et_history);
        et_content = findViewById(R.id.et_content);
        btn_send = findViewById(R.id.btn_send);
        sv_history = findViewById(R.id.sv_history);


        btn_start_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //连接远程服务器
                TcpClient.startClient("172.27.23.1", 8899);
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送数据给服务器
                String content = et_content.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    return;
                }

                TcpClient.sendTcpMessage(content);
                addToHistory("发送>>>："+content);
                et_content.setText("");
            }
        });


        //注册EventBus
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    //收到客户端的消息
    public void MessageClient(MessageClient messageEvent) {
        Log.e("msg", messageEvent.getMsg());
        addToHistory("接收<<<："+messageEvent.getMsg());
//        Toast.makeText(this, "客户端收到:" + messageEvent.getMsg(), Toast.LENGTH_SHORT).show();
    }

    private void addToHistory(String msg) {
        String historyContent = et_history.getText().toString();
        if (!TextUtils.isEmpty(historyContent)) {
            historyContent=historyContent+"\n";
        }
        historyContent = historyContent + msg;
        et_history.setText(historyContent);
        et_history.setSelection(historyContent.length());

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                sv_history.fullScroll(ScrollView.FOCUS_DOWN);
                et_content.requestFocus();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
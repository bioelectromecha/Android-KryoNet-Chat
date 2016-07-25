package com.example.roman.androidserverdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.example.roman.androidserverdemo.model.server.MessageListener;
import com.example.roman.androidserverdemo.model.server.client.ChatClient;
import com.example.roman.androidserverdemo.model.server.host.ChatServer;

import java.io.IOException;


public class MainActivity extends MyAppCompatActivity implements MessageListener {
    private TextView mMessageTextView;
    private EditText mMessageEditText;
    private Button mSendButton;
    private RadioButton mHostButton;
    private RadioButton mClientButton;
    private Mode mMode;

    private enum Mode{NONE ,HOST, CLIENT}

    private ChatServer mChatServer;
    private ChatClient mChatClient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMode = Mode.NONE;
        mMessageTextView = (TextView) findViewById(R.id.textview_message);
        mMessageEditText = (EditText) findViewById(R.id.edittext_message);
        mSendButton = (Button) findViewById(R.id.button_send);
        //server/client  setup buttons
        mHostButton = (RadioButton) findViewById(R.id.button_host);
        mClientButton = (RadioButton) findViewById(R.id.button_client);

        //create the server - don't startClient it yet
        mChatServer = new ChatServer(this);
        //create the server - don't startClient it yet
        mChatClient = new ChatClient(this);
    }

    public void onClickSendButton(View view) {
        if (clickTimeoutOverCheck()) {
            if (mMode == Mode.CLIENT){
                mChatClient.sendMessage(mMessageEditText.getText().toString());
                return;
            }
            if (mMode == Mode.HOST) {
                mChatServer.sendMessage(mMessageEditText.getText().toString());
            }
        }

    }

    public void onClickHostButton(View view) {
        if (clickTimeoutOverCheck()) {
            setServerMode(Mode.HOST);
        }
    }
    public void onClickClientButton(View view) {
        if (clickTimeoutOverCheck()) {
            setServerMode(Mode.CLIENT);
        }
    }
    private void setServerMode(Mode newMode) {
        //new mode HOST cases
        if (mMode == Mode.HOST && newMode == Mode.HOST) {
            return;
        }
        if(mMode == Mode.CLIENT && newMode == Mode.HOST) {
            mChatClient.stopClient();
            startServer();
            return;
        }
        if (mMode == Mode.NONE && newMode == Mode.HOST) {
            startServer();
            return;
        }
        //new mode CLIENT cases
        if (mMode == Mode.CLIENT && newMode == Mode.CLIENT) {
            return;
        }
        if (mMode == Mode.HOST && newMode == Mode.CLIENT) {
            mChatServer.stop();
            startClient();
            return;
        }
        if (mMode == Mode.NONE && newMode == Mode.CLIENT) {
            startClient();
            return;
        }
    }

    private void startServer(){
        try {
            mChatServer.start();
            mMode = Mode.HOST;
        } catch (IOException e) {
            LogUtils.d(e.getMessage());
            LogUtils.d(e.getStackTrace());
        }
    }
    private void startClient(){
        mChatClient.startClient();
        mMode = Mode.CLIENT;
    }

    @Override
    public void onMessageReceived(final String message) {
        LogUtils.d("Message receiveD: " + message);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMessageTextView.setText(message);
            }
        });
    }

    @Override
    public void onConnected() {
        LogUtils.d("CONNECTED!!!!");
    }

    @Override
    public void onDisconnected() {
        LogUtils.d("DISCONNECTED!!!!");
    }
}

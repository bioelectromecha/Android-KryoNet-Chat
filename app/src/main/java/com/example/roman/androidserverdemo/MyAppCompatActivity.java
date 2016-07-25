package com.example.roman.androidserverdemo;

import android.support.v7.app.AppCompatActivity;

import com.esotericsoftware.minlog.Log;

/**
 * Created by roman on 7/24/16.
 */
public class MyAppCompatActivity extends AppCompatActivity {
    // stuff to protect against button mashing
    private static final long CLICK_TIMEOUT = 700; //milliseconds
    private static long mLastClickTime = 0;

    public MyAppCompatActivity(){
        Log.set(Log.LEVEL_DEBUG);
    }
    /**
     * key mashing protection helper method
     * @return true if click timeout has passed, false otherwise
     */
    protected boolean clickTimeoutOverCheck() {

        if( System.currentTimeMillis() < mLastClickTime + CLICK_TIMEOUT ){
            mLastClickTime = System.currentTimeMillis();
            return false;
        }else{
            mLastClickTime = System.currentTimeMillis();
            return true;
        }
    }
}

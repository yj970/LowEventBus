package com.yj.loweventbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.yj.loweventbuslibrary.Event;
import com.yj.loweventbuslibrary.LowEventBus;
import com.yj.loweventbuslibrary.Subscribe;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LowEventBus.register(this);
        LowEventBus.post(new Event(10));

    }


    @Subscribe
    public void onAccept(Event event) {
        Log.d("MyTAG", "接到事件, id="+event.getId());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LowEventBus.unRegister(this);
    }
}

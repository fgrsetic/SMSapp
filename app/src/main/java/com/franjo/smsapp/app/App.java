package com.franjo.smsapp.app;

import android.app.Application;
import android.content.IntentFilter;
import android.provider.Telephony;

import com.franjo.smsapp.receiver.MessageReceiver;

public class App extends Application {

    private static App appContext;
    private MessageReceiver receiver;

    public static App getAppContext() {
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = this;
        receiver = new MessageReceiver();
        registerReceiver(receiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));
    }

    @Override
    public void onTerminate() {
        unregisterReceiver(receiver);
        super.onTerminate();
    }
}

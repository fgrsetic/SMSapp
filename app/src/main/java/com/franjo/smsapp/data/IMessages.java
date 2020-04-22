package com.franjo.smsapp.data;

import android.content.Context;

import java.util.List;

public interface IMessages {

    List<SmsData> getAllMessages(Context context);
}

package com.franjo.smsapp.data;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import java.util.List;

public interface IMessages {

    List<SmsData> getAllMessages(Context context);
    Bitmap loadContactPhoto(String phoneNumber);
    SmsData openContactDetails(SmsData smsData);
}

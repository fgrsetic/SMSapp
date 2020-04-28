package com.franjo.smsapp.data;

import android.content.Context;
import android.net.Uri;

import java.util.List;

public interface IMessages {

    List<SmsData> getAllMessages(Context context);

    Uri openContactDetails(SmsData smsData);

    List<Contact> openContactList(Context context);
}

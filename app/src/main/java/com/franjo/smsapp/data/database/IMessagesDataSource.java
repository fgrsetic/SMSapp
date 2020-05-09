package com.franjo.smsapp.data.database;

import android.database.Cursor;
import android.net.Uri;

import com.franjo.smsapp.data.model.Message;

import java.util.List;

public interface IMessagesDataSource {

    List<Message> getReceivedMessages();

    Cursor performSearch(String searchText);

    Uri openContactDetails(Message message);
}

package com.franjo.smsapp.data.receiver;

import com.franjo.smsapp.data.model.Message;

import java.util.List;

public interface IReceiver {
    List<Message> getSMSList();
}

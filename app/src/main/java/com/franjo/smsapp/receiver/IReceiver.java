package com.franjo.smsapp.receiver;

import com.franjo.smsapp.domain.Message;

import java.util.List;

public interface IReceiver {
    List<Message> getSMSList();
}

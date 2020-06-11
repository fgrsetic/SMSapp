package com.franjo.smsapp.receiver;

import com.franjo.smsapp.domain.Conversation;

import java.util.List;

public interface IReceiver {
    List<Conversation> getSMSList();
}

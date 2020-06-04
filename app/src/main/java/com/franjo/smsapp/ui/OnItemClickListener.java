package com.franjo.smsapp.ui;

import com.franjo.smsapp.domain.Conversation;

public interface OnItemClickListener {

    void onItemClick(Conversation item, int adapterPosition);

    void onContactIconClicked(Conversation item);
}

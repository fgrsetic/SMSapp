package com.franjo.smsapp.ui.messages.conversations_details;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.franjo.smsapp.R;
import com.franjo.smsapp.domain.Message;
import com.franjo.smsapp.ui.BaseViewHolder;

import java.util.List;


public class DetailsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int MESSAGE_TYPE_INBOX = 1;
    public static final int MESSAGE_TYPE_SENT = 2;


    private final AsyncListDiffer<Message> mDiffer = new AsyncListDiffer<>(this, DIFF_CALLBACK);

    private static final DiffUtil.ItemCallback<Message> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Message>() {
                @Override
                public boolean areItemsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
                    return oldItem.equals(newItem);
                }
            };


    public void submitMessagesList(List<Message> newList) {
        mDiffer.submitList(newList);
    }

    @Override
    public int getItemCount() {
        return mDiffer.getCurrentList().size();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case MESSAGE_TYPE_INBOX:
                ViewDataBinding bindingInbox = DataBindingUtil.inflate(layoutInflater, R.layout.item_details_received, parent, false);
                return new BaseViewHolder(bindingInbox);
            case MESSAGE_TYPE_SENT:
                ViewDataBinding bindingSent = DataBindingUtil.inflate(layoutInflater, R.layout.item_details_sent, parent, false);
                return new BaseViewHolder(bindingSent);
            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int listPosition) {
        Message message = mDiffer.getCurrentList().get(listPosition);
        switch (message.getMessageType()) {
            case MESSAGE_TYPE_INBOX:
            case MESSAGE_TYPE_SENT:
                holder.bind(message);
                if (listPosition >= 1) {
                    Message previousMsg = mDiffer.getCurrentList().get(listPosition - 1);
                    holder.bind(message, previousMsg);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + getItemViewType(listPosition));
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = mDiffer.getCurrentList().get(position);
        int type = message.getMessageType();
        if (type == 1) {
            return MESSAGE_TYPE_INBOX;
        } else if (type == 2) {
            return MESSAGE_TYPE_SENT;
        } else {
            return -1;
        }
    }
}

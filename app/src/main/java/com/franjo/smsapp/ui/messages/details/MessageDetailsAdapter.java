package com.franjo.smsapp.ui.messages.details;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.franjo.smsapp.data.model.Message;
import com.franjo.smsapp.databinding.ItemMessageDetailsHeaderBinding;
import com.franjo.smsapp.databinding.ItemMessagesListBinding;
import com.franjo.smsapp.databinding.ItemReceivedMessageBinding;
import com.franjo.smsapp.databinding.ItemSentMessageBinding;

import okhttp3.internal.http2.Header;

public class MessageDetailsAdapter extends ListAdapter<Message, RecyclerView.ViewHolder> {

    private static int HEADER_TIME = 0;
    private static int TYPE_SENT_MESSAGE = 1;
    private static int TYPE_RECEIVED_MESSAGE = 2;

    private static final DiffUtil.ItemCallback<Message> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Message>() {
                @Override
                public boolean areItemsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
                    return oldItem.getPhoneNumber().equals(newItem.getPhoneNumber());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
                    return oldItem.equals(newItem);
                }
            };


    MessageDetailsAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_SENT_MESSAGE) {
            return new SentMessageViewHolder(ItemSentMessageBinding.inflate(LayoutInflater.from(parent.getContext())));
        } else if (viewType == TYPE_RECEIVED_MESSAGE) {
            return new ReceivedMessageViewHolder(ItemReceivedMessageBinding.inflate(LayoutInflater.from(parent.getContext())));
        } else {
            return new HeaderViewHolder(ItemMessageDetailsHeaderBinding.inflate(LayoutInflater.from(parent.getContext())));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = getItem(position);
        if (getItemViewType(position) == TYPE_SENT_MESSAGE) {
            ((SentMessageViewHolder)holder).bind(message);
        } else if (getItemViewType(position) == TYPE_RECEIVED_MESSAGE){
            ((ReceivedMessageViewHolder)holder).bind(message);
        } else {
            ((HeaderViewHolder)holder).bind(message);
        }

    }

    @Override
    public int getItemViewType(int position) {
        Message message = getItem(position);
        if (message.isMessageSendByMe()) {
            return TYPE_SENT_MESSAGE;
        } else {
            return TYPE_RECEIVED_MESSAGE;
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {

        private ItemMessageDetailsHeaderBinding binding;

        private HeaderViewHolder(ItemMessageDetailsHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Message message) {
            binding.setMessage(message);
            binding.executePendingBindings();
        }
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {

        private ItemSentMessageBinding binding;

        private SentMessageViewHolder(ItemSentMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Message message) {
            binding.setMessage(message);
            binding.executePendingBindings();
        }

    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {

        private ItemReceivedMessageBinding binding;

        private ReceivedMessageViewHolder(ItemReceivedMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Message message) {
            binding.setMessage(message);
            binding.executePendingBindings();
        }
    }

}

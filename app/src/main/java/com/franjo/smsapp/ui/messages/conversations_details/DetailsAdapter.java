package com.franjo.smsapp.ui.messages.conversations_details;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.franjo.smsapp.databinding.ItemDetailsHeaderBinding;
import com.franjo.smsapp.databinding.ItemDetailsReceivedBinding;
import com.franjo.smsapp.databinding.ItemDetailsSentBinding;
import com.franjo.smsapp.domain.Message;

import java.util.List;


public class DetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER = 0;
    private static final int SENT = 1;
    private static final int INBOX = 2;

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

    public void submitMessagesList(List<Message> list) {
        mDiffer.submitList(list);
    }

    @Override
    public int getItemCount() {
        return mDiffer.getCurrentList().size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case HEADER:
                ItemDetailsHeaderBinding bindingHeader = ItemDetailsHeaderBinding.inflate(layoutInflater, parent, false);
                return new HeaderViewHolder(bindingHeader);
            case SENT:
                ItemDetailsSentBinding bindingSent = ItemDetailsSentBinding.inflate(layoutInflater, parent, false);
                return new SentMessageViewHolder(bindingSent);
            case INBOX:
                ItemDetailsReceivedBinding bindingInbox = ItemDetailsReceivedBinding.inflate(layoutInflater, parent, false);
                return new ReceivedMessageViewHolder(bindingInbox);
            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int listPosition) {
        Message message = mDiffer.getCurrentList().get(listPosition);
        if (message.getMessageType() != null) {
            switch (message.getMessageType()) {
                case HEADER:
                    ((HeaderViewHolder) holder).bind(message);
                    break;
                case SENT:
                    ((SentMessageViewHolder) holder).bind(message);
                    break;
                case INBOX:
                    ((ReceivedMessageViewHolder) holder).bind(message);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + getItemViewType(listPosition));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = mDiffer.getCurrentList().get(position);
        if (message.getMessageType() != null) {
            switch (message.getMessageType()) {
                case 0:
                    return HEADER;
                case 1:
                case 132:
                    return SENT;
                case 2:
                case 128:
                    return INBOX;
            }
        }
        return -1;
    }


    static class HeaderViewHolder extends RecyclerView.ViewHolder {

        private ItemDetailsHeaderBinding binding;

        private HeaderViewHolder(ItemDetailsHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Message message) {
            binding.setMessage(message);
            binding.executePendingBindings();
        }
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {

        private ItemDetailsSentBinding binding;

        private SentMessageViewHolder(ItemDetailsSentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Message message) {
            binding.setMessage(message);
            binding.executePendingBindings();
        }

    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {

        private ItemDetailsReceivedBinding binding;

        private ReceivedMessageViewHolder(ItemDetailsReceivedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Message message) {
            binding.setMessage(message);
            binding.executePendingBindings();
        }
    }

}

package com.franjo.smsapp.ui.messages.inbox;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.franjo.smsapp.databinding.ItemMessagesListBinding;
import com.franjo.smsapp.domain.Message;

import java.util.List;


public class InboxMessageAdapter extends RecyclerView.Adapter<InboxMessageAdapter.MessagesViewHolder> {

    private final AsyncListDiffer<Message> mDiffer = new AsyncListDiffer<>(this, DIFF_CALLBACK);
    private IClickListener onClickListener;

    private static final DiffUtil.ItemCallback<Message> DIFF_CALLBACK = new DiffUtil.ItemCallback<Message>() {
        @Override
        public boolean areItemsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return oldItem.getPhoneNumber().equals(newItem.getPhoneNumber());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return oldItem.equals(newItem);
        }
    };


    InboxMessageAdapter(IClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void submitList(List<Message> list) {
        mDiffer.submitList(list);
    }

    @Override
    public int getItemCount() {
        return mDiffer.getCurrentList().size();
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessagesViewHolder(ItemMessagesListBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder holder, int position) {
        Message message = mDiffer.getCurrentList().get(position);
        holder.bind(message, onClickListener);
    }

    static class MessagesViewHolder extends RecyclerView.ViewHolder {

        private ItemMessagesListBinding binding;

        private MessagesViewHolder(ItemMessagesListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Message message, IClickListener clickListener) {
            binding.setMessage(message);
            binding.setClickListener(clickListener);
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings();
        }
    }

    public interface IClickListener {

        void onClick(Message message);

        void onContactIconClicked(Message message);
    }
}
























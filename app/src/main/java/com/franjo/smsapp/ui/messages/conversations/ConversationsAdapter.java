package com.franjo.smsapp.ui.messages.conversations;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.franjo.smsapp.databinding.ItemConversationsListBinding;
import com.franjo.smsapp.domain.Conversation;

import java.util.List;


public class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.MessagesViewHolder> {

    private final AsyncListDiffer<Conversation> mDiffer = new AsyncListDiffer<>(this, DIFF_CALLBACK);
    private IClickListener onClickListener;

    private static final DiffUtil.ItemCallback<Conversation> DIFF_CALLBACK = new DiffUtil.ItemCallback<Conversation>() {
        @Override
        public boolean areItemsTheSame(@NonNull Conversation oldItem, @NonNull Conversation newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Conversation oldItem, @NonNull Conversation newItem) {
            return oldItem.equals(newItem);
        }
    };


    ConversationsAdapter(IClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void submitList(List<Conversation> list) {
        mDiffer.submitList(list);
    }

    @Override
    public int getItemCount() {
        return mDiffer.getCurrentList().size();
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessagesViewHolder(ItemConversationsListBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder holder, int position) {
        Conversation conversation = mDiffer.getCurrentList().get(position);
        holder.bind(conversation, onClickListener);
    }

    static class MessagesViewHolder extends RecyclerView.ViewHolder {

        private ItemConversationsListBinding binding;

        private MessagesViewHolder(ItemConversationsListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Conversation conversation, IClickListener clickListener) {
            binding.setConversation(conversation);
            binding.setClickListener(clickListener);
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings();
        }
    }

    public interface IClickListener {

        void onClick(Conversation conversation);

        void onContactIconClicked(Conversation conversation);
    }
}
























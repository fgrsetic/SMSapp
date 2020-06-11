package com.franjo.smsapp.ui.messages.conversations;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.franjo.smsapp.R;
import com.franjo.smsapp.domain.Conversation;
import com.franjo.smsapp.ui.OnItemClickListener;
import com.franjo.smsapp.ui.BaseViewHolder;

import java.util.List;


public class ConversationsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private OnItemClickListener listener;
    private final AsyncListDiffer<Conversation> mDiffer = new AsyncListDiffer<>(this, DIFF_CALLBACK);

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


    ConversationsAdapter(OnItemClickListener listener) {
        this.listener = listener;
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
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_conversations_list, parent, false);
        return new BaseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Conversation item = mDiffer.getCurrentList().get(position);
        holder.bind(item, listener);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item, holder.getAdapterPosition()));
    }
}
























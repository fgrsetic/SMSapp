//package com.franjo.smsapp.ui.messages.details;
//
//import android.view.LayoutInflater;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.DiffUtil;
//import androidx.recyclerview.widget.ListAdapter;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.franjo.smsapp.persistence.model.Message;
//import com.franjo.smsapp.databinding.ItemMessageDetailsHeaderBinding;
//import com.franjo.smsapp.databinding.ItemMessageDetailsReceivedBinding;
//import com.franjo.smsapp.databinding.ItemMessageDetailsSentBinding;
//
//
//public class MessageDetailsAdapter extends ListAdapter<Message, RecyclerView.ViewHolder> {
//
//    private static final int HEADER = 0;
//    private static final int SENT = 1;
//    private static final int INBOX = 2;
//
//
//    private static final DiffUtil.ItemCallback<Message> DIFF_CALLBACK =
//            new DiffUtil.ItemCallback<Message>() {
//                @Override
//                public boolean areItemsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
//                    return oldItem.getPhoneNumber().equals(newItem.getPhoneNumber());
//                }
//
//                @Override
//                public boolean areContentsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
//                    return oldItem.equals(newItem);
//                }
//            };
//
//
//    MessageDetailsAdapter() {
//        super(DIFF_CALLBACK);
//    }
//
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        switch (viewType) {
//            case HEADER:
//                ItemMessageDetailsHeaderBinding bindingHeader = ItemMessageDetailsHeaderBinding.inflate(layoutInflater, parent, false);
//                return new HeaderViewHolder(bindingHeader);
//            case SENT:
//                ItemMessageDetailsSentBinding bindingSent = ItemMessageDetailsSentBinding.inflate(layoutInflater, parent, false);
//                return new SentMessageViewHolder(bindingSent);
//            case INBOX:
//                ItemMessageDetailsReceivedBinding bindingInbox = ItemMessageDetailsReceivedBinding.inflate(layoutInflater, parent, false);
//                return new ReceivedMessageViewHolder(bindingInbox);
//            default:
//                throw new IllegalStateException("Unexpected value: " + viewType);
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        Message message = getItem(position);
//
////        if (message.isHeader()) {
//            ((HeaderViewHolder) holder).bind(message);
////        } else if (!message.isHeader() && message.isSendByMe()) {
////            ((SentMessageViewHolder) holder).bind(message);
////        } else if (!message.isSendByMe() && message.getType() == 2) {
////            ((ReceivedMessageViewHolder) holder).bind(message);;
////        } else {
////            throw new IllegalStateException("Unexpected value: " + getItemViewType(position));
////        }
//
////        switch (getItemViewType(position)) {
////            case 0:
////                ((HeaderViewHolder) holder).bind(message);
////                break;
////            case 1:
////                ((SentMessageViewHolder) holder).bind(message);
////                break;
////            case 2:
////                ((ReceivedMessageViewHolder) holder).bind(message);
////                break;
////            default:
////                throw new IllegalStateException("Unexpected value: " + getItemViewType(position));
////        }
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        Message message = getItem(position);
////        if (message.isHeader()) {
////            return HEADER;
////        } else if (message.isSendByMe()) {
////            return SENT;
////        } else if (!message.isSendByMe() && message.getType() == 2) {
////            return INBOX;
////        } else {
////            return -1;
////        }
//
//        return position;
//    }
//
//
//    static class HeaderViewHolder extends RecyclerView.ViewHolder {
//
//        private ItemMessageDetailsHeaderBinding binding;
//
//        private HeaderViewHolder(ItemMessageDetailsHeaderBinding binding) {
//            super(binding.getRoot());
//            this.binding = binding;
//        }
//
//        void bind(Message message) {
//            binding.setMessage(message);
//            binding.executePendingBindings();
//        }
//    }
//
//    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
//
//        private ItemMessageDetailsSentBinding binding;
//
//        private SentMessageViewHolder(ItemMessageDetailsSentBinding binding) {
//            super(binding.getRoot());
//            this.binding = binding;
//        }
//
//        void bind(Message message) {
//            binding.setMessage(message);
//            binding.executePendingBindings();
//        }
//
//    }
//
//    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
//
//        private ItemMessageDetailsReceivedBinding binding;
//
//        private ReceivedMessageViewHolder(ItemMessageDetailsReceivedBinding binding) {
//            super(binding.getRoot());
//            this.binding = binding;
//        }
//
//        void bind(Message message) {
//            binding.setMessage(message);
//            binding.executePendingBindings();
//        }
//    }
//
//}

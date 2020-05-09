package com.franjo.smsapp.ui.messages;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.franjo.smsapp.data.model.Message;
import com.franjo.smsapp.databinding.ItemMessagesListBinding;


public class MessagesAdapter extends ListAdapter<Message, MessagesAdapter.MessagesViewHolder> {

    private IClickListener onClickListener;

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


    MessagesAdapter(IClickListener onClickListener) {
        super(DIFF_CALLBACK);
        this.onClickListener = onClickListener;

    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessagesViewHolder(ItemMessagesListBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder holder, int position) {
        Message message = getItem(position);
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


//    //Context holds a reference to an activity which is using the custom adapter.
//    private final Context context;
//    //Original list, filtering do with this list
//    private final List<SMSData> smsList;
//    //Filtered list construct by filtering the smslist, it uses to create the list view
//    private List<SMSData> filteredList;
//    private Filter filter;
//
//
//    // The Constructor for the Adapter helps to initialize the data items and the context.
//    public ConversationAdapter(Context context, List<SMSData> smsList) {
//        super(context, R.layout.activity_inbox, smsList);
//        this.context = context;
//        this.smsList = smsList;
//        this.filteredList = smsList;
//
//        getFilter();
//
//    }

//
//    // Get size of list, return list size
//    @Override
//    public int getCount() {
//        return filteredList.size();
//    }
//
//    // Get specific item from user list, parameter position item index, return list item
//    @Override
//    public SMSData getItem(int position) {
//        return filteredList.get(position);
//    }
//
//
//
//    // Get list item id, parameter position item index, return current item id
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }

//
//    // Get custom filter
//    // Returns a filter that can be used to constrain data with a filtering pattern.
//    @NonNull
//    @Override
//    public Filter getFilter() {
//        if (filter == null) {
//            filter = new SmsFilter();
//        }
//
//        return filter;
//    }
//
//    // Custom filter for smslist
//    // Filter content in smslist according to the search text
//    private class SmsFilter extends Filter {
//
//        // Invoked in a worker thread to filter the data according to the constraint.
//        // Parametar CharSequence: the constraint used to filter the data
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            // The results of the filtering operation
//            FilterResults filterResults = new FilterResults();
//            // When the constraint is null, the original data must be restored.
//            if (constraint != null && constraint.length() > 0) {
//                filteredList = new ArrayList<>();
//
//                // Search content in smsList
//                for (SMSData smsData : smsList) {
//                    if (smsData.getNumber().toLowerCase().contains(constraint.toString().toLowerCase())) {
//                        filteredList.add(smsData);
//                    }
//                }
//
//                filterResults.count = filteredList.size();
//                filterResults.values = filteredList;
//
//            } else {
//
//                filterResults.count = smsList.size();
//                filterResults.values = smsList;
//            }
//
//            return filterResults;
//        }
//
//        //Notify about filtered list to ui, constraint text, results filtered result
//        @SuppressWarnings("unchecked")
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            filteredList = (List<SMSData>) results.values;
//            notifyDataSetChanged();
//        }
//
//    }
//























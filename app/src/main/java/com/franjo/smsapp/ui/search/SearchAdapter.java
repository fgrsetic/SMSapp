package com.franjo.smsapp.ui.search;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.franjo.smsapp.data.SmsData;
import com.franjo.smsapp.databinding.ItemSearchListBinding;
import com.franjo.smsapp.databinding.ItemSmsListBinding;

/**
 * Created by Franjo on 18.7.2016..
 */
public class SearchAdapter extends ListAdapter<SmsData, SearchAdapter.SearchResultViewHolder> {

    private IClickListener onClickListener;

    private static final DiffUtil.ItemCallback<SmsData> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<SmsData>() {
                @Override
                public boolean areItemsTheSame(@NonNull SmsData oldItem, @NonNull SmsData newItem) {
                    return oldItem.getPhoneNumber().equals(newItem.getPhoneNumber());
                }

                @Override
                public boolean areContentsTheSame(@NonNull SmsData oldItem, @NonNull SmsData newItem) {
                    return oldItem.equals(newItem);
                }
            };


    SearchAdapter(IClickListener onClickListener) {
        super(DIFF_CALLBACK);
        this.onClickListener = onClickListener;

    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchResultViewHolder(ItemSearchListBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        SmsData smsData = getItem(position);
        holder.bind(smsData, onClickListener);
    }

    static class SearchResultViewHolder extends RecyclerView.ViewHolder {

        private ItemSearchListBinding binding;

        private SearchResultViewHolder(ItemSearchListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(SmsData smsData, IClickListener clickListener) {
            binding.setSmsData(smsData);
            binding.setClickListener(clickListener);
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings();
        }

    }

    public interface IClickListener {
        void onClick(SmsData smsData);
        void onContactIconClicked(SmsData smsData);
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























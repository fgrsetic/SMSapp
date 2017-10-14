package com.example.franjo.smsapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.franjo.smsapp.ui.InboxActivity;
import com.example.franjo.smsapp.R;
import com.example.franjo.smsapp.model.SMSData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Franjo on 18.7.2016..
 */
public class InboxAdapter extends ArrayAdapter<SMSData> implements Filterable {
    //Context holds a reference to an activity which is using the custom adapter.
    private final Context context;
    //Original list, filtering do with this list
    private final List<SMSData> smsList;
    //Filtered list construct by filtering the smslist, it uses to create the list view
    private List<SMSData> filteredList;
    private Filter filter;


    //The Constructor for the CustomAdapter helps to initialize the data items and the context.
    public InboxAdapter(Context context, ArrayList<SMSData>smsList) {
        super(context, R.layout.activity_inbox, smsList);
        this.context = context;
        this.smsList = smsList;
        this.filteredList = smsList;

        getFilter();

    }
    // Get size of list, return list size
    @Override
    public int getCount() {
        return filteredList.size();
    }

    //Get specific item from user list, parametar position item index, return list item
    @Override
    public SMSData getItem(int position) {
        return filteredList.get(position);
    }

    //Get list item id, parametat position item index, return current item id
    @Override
    public long getItemId(int position) {
        return position;
    }


    //Create list row view, parametar position index, parametar convertView current list item view, parametar parent parent, return convertView
    //The getView() method is used to build the view item. This method returns a view item which is placed as a row inside the listview.
    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // A ViewHolder keeps references to children views to avoid unnecessary calls to findViewById() on each row.
        final ViewHolder holder = new ViewHolder();
        // Get the data item for this position
        final SMSData smsData = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view. If holder not exist then locate all view from UI file.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // inflate the layout for each list row
            convertView = inflater.inflate(R.layout.list_row, parent, false);

            convertView.setTag(holder);

        } else {
            // get view holder back
            convertView.getTag();

        }

        //link the cached views to the view
        holder.txtBroj = (TextView) convertView.findViewById(R.id.txtBroj);
        holder.txtPoruka = (TextView) convertView.findViewById(R.id.txtPoruka);
        holder.txtVrijeme = (TextView) convertView.findViewById(R.id.txtVrijeme);
        holder.txtMinute = (TextView) convertView.findViewById(R.id.txtMinute);
        holder.imageView = (ImageView) convertView.findViewById(R.id.contactImage);

        holder.txtBroj.setText(smsData.getNumber());

        if (!InboxActivity.showFullText()) {
            holder.txtPoruka.setSingleLine(true);
            holder.txtPoruka.setEllipsize(TextUtils.TruncateAt.END);
        }

        holder.txtPoruka.setText(smsData.getBody());
        holder.txtVrijeme.setText(smsData.getDate());
        holder.txtMinute.setText(smsData.getMinute());
        holder.imageView.setImageResource(smsData.getContactImage());

        Random rand = new Random();
//        // generate the random integers for r, g and b value
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);
        int randomColor = Color.rgb(r, g, b);
        holder.imageView.setColorFilter(randomColor, PorterDuff.Mode.MULTIPLY);


        return convertView;
    }

    //Keep reference to children view to avoid unnecessary calls
    static class ViewHolder {
        TextView txtBroj;
        ImageView imageView;
        TextView txtPoruka;
        TextView txtVrijeme;
        TextView txtMinute;

    }

    //Get custom filter, return filter
    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new SmsFilter();
        }

        return filter;
    }

    //Custom filter for smslist, Filter content in smslist according to the search text
    private class SmsFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint!=null && constraint.length()>0) {
                ArrayList<SMSData> tempList = new ArrayList<>();

                // search content in smsList
                for (SMSData smsData : smsList) {
                    if (smsData.getBody().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(smsData);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = smsList.size();
                filterResults.values = smsList;
            }

            return filterResults;
        }

        //Notify about filtered list to ui, constraint text, results filtered result
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<SMSData>) results.values;
            notifyDataSetChanged();
        }

    }

}



















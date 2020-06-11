package com.franjo.smsapp.util.binding_utils;

import android.database.Cursor;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.franjo.smsapp.app.App;
import com.franjo.smsapp.data.model.Contact;
import com.franjo.smsapp.ui.contacts.ContactsAdapter;
import com.franjo.smsapp.ui.contacts.search.SearchContactsAdapter;
import com.franjo.smsapp.util.ImageUtil;

import java.util.List;

public class BindingAdaptersContacts {

    @BindingAdapter("contactsList")
    public static void bindContactsListRecyclerView(RecyclerView recyclerView, List<Contact> data) {
        ContactsAdapter adapter = (ContactsAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.submitList(data);
        }
    }

    @BindingAdapter("contactsName")
    public static void bindContactsName(TextView textView, Contact contact) {
        textView.setText(contact.getName());
    }

    @BindingAdapter("contactsImage")
    public static void randomChangeContactImage(ImageView imageView, Contact contact) {
        if (contact.getName() == null) {
            imageView.setBackground(ImageUtil.getRandomImage("XXX"));
        } else {
            imageView.setBackground(ImageUtil.getRandomImage(contact.getName()));
        }
    }

    // Search
    @BindingAdapter("searchContactsList")
    public static void bindSearchContactsListView(ListView listView, Cursor cursor) {
        SearchContactsAdapter searchContactsAdapter = new SearchContactsAdapter(App.getAppContext(), cursor, 0, new SearchContactsAdapter.IClickListener() {
            @Override
            public void onClick(Contact contact) {
                Toast.makeText(App.getAppContext(), contact.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onContactIconClicked(Contact contact) {
                Toast.makeText(App.getAppContext(), "icon", Toast.LENGTH_SHORT).show();

            }
        });
        listView.setAdapter(searchContactsAdapter);
    }

}

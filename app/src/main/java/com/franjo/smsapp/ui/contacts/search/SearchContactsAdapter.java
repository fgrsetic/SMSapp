package com.franjo.smsapp.ui.contacts.search;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cursoradapter.widget.CursorAdapter;

import com.franjo.smsapp.app.App;
import com.franjo.smsapp.data.database.DatabaseMessagesDataSource;
import com.franjo.smsapp.data.model.Contact;
import com.franjo.smsapp.databinding.ItemContactsListBinding;
import com.franjo.smsapp.databinding.ItemSearchContactsListBinding;
import com.franjo.smsapp.util.ContactsName;

public class SearchContactsAdapter extends CursorAdapter {

    private final LayoutInflater layoutInflater;
    private ItemSearchContactsListBinding binding;
    private IClickListener onClickListener;

    public SearchContactsAdapter(Context context, Cursor c, int flags, IClickListener onClickListener) {
        super(context, c, flags);
        layoutInflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        binding = ItemSearchContactsListBinding.inflate(layoutInflater);
        return binding.getRoot();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Contact contact = new Contact();
        String number = cursor.getString(DatabaseMessagesDataSource.ColumnIndexCache.getColumnIndexOrThrow(cursor, ContactsContract.Contacts.HAS_PHONE_NUMBER));
        contact.setPhoneNumber(number);

        String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
        contact.setName(name);
        if (name == null)
            binding.contactsName.setText(contact.getPhoneNumber());
        else
            binding.contactsName.setText(contact.getName());

        binding.setContact(contact);
        binding.setClickListener(onClickListener);
    }

    public interface IClickListener {

        void onClick(Contact contact);

        void onContactIconClicked(Contact contact);
    }
}

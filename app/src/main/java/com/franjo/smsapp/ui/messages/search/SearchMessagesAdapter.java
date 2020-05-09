package com.franjo.smsapp.ui.messages.search;

import android.content.Context;
import android.database.Cursor;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cursoradapter.widget.CursorAdapter;

import com.franjo.smsapp.app.App;
import com.franjo.smsapp.data.database.DatabaseMessagesDataSource;
import com.franjo.smsapp.data.model.Message;
import com.franjo.smsapp.databinding.ItemSearchMessagesListBinding;
import com.franjo.smsapp.util.ContactsName;
import com.franjo.smsapp.util.DateFormatting;

public class SearchMessagesAdapter extends CursorAdapter {

    private final LayoutInflater layoutInflater;
    private ItemSearchMessagesListBinding binding;
    private IClickListener onClickListener;

    public SearchMessagesAdapter(Context context, Cursor c, int flags, IClickListener onClickListener) {
        super(context, c, flags);
        layoutInflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        binding = ItemSearchMessagesListBinding.inflate(layoutInflater);
        return binding.getRoot();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Message message = new Message();
        String number = cursor.getString(DatabaseMessagesDataSource.ColumnIndexCache.getColumnIndexOrThrow(cursor, Telephony.Sms.ADDRESS));
        message.setPhoneNumber(number);

        String name = ContactsName.getContactName(App.getAppContext(), number);
        message.setName(name);

        if (name == null)
            binding.phoneNumber.setText(message.getPhoneNumber());
        else
            binding.phoneNumber.setText(message.getName());

        String body = cursor.getString(DatabaseMessagesDataSource.ColumnIndexCache.getColumnIndexOrThrow(cursor, Telephony.Sms.BODY));
        message.setMessageBody(body);
        binding.message.setText(message.getMessageBody());

        String date = cursor.getString(DatabaseMessagesDataSource.ColumnIndexCache.getColumnIndexOrThrow(cursor, Telephony.Sms.DATE));
        String dateString = DateFormatting.formatDate("dd.MM", Long.parseLong((date)));
        message.setDate(dateString);
        binding.date.setText(message.getDate());

        binding.setMessage(message);
        binding.setClickListener(onClickListener);
    }

    public interface IClickListener {

        void onClick(Message message);

        void onContactIconClicked(Message message);
    }
}

package com.franjo.smsapp.ui.messages.search;

import android.content.Context;
import android.database.Cursor;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cursoradapter.widget.CursorAdapter;

import com.franjo.smsapp.app.App;
import com.franjo.smsapp.domain.Message;
import com.franjo.smsapp.databinding.ItemSearchMessagesListBinding;
import com.franjo.smsapp.util.ColumnIndexCache;
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

        String number = cursor.getString(ColumnIndexCache.getColumnIndexOrThrow(cursor, Telephony.Sms.ADDRESS));
        //String number = cursor.getString(DatabaseMessagesDataSource.ColumnIndexCache.getColumnIndexOrThrow(cursor, Telephony.Sms.ADDRESS));
        String name = ContactsName.getContactName(App.getAppContext(), number);
        String body = cursor.getString(ColumnIndexCache.getColumnIndexOrThrow(cursor, Telephony.Sms.BODY));
        String date = cursor.getString(ColumnIndexCache.getColumnIndexOrThrow(cursor, Telephony.Sms.DATE));
        String dateString = DateFormatting.formatDate(date);

        Message message = null;
        if (name != null) {
            message = new Message(number, name, body, date);
        }
        if (name == null) {
            binding.phoneNumber.setText(number);
        } else {
            binding.phoneNumber.setText(name);
        }
        binding.message.setText(body);
        binding.date.setText(dateString);
        binding.setMessage(message);
        binding.setClickListener(onClickListener);
    }

    public interface IClickListener {

        void onClick(Message message);

        void onContactIconClicked(Message message);
    }
}

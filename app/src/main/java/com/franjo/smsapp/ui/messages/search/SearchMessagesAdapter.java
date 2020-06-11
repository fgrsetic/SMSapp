package com.franjo.smsapp.ui.messages.search;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cursoradapter.widget.CursorAdapter;

import com.franjo.smsapp.databinding.ItemSearchMessagesListBinding;
import com.franjo.smsapp.domain.Conversation;

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

//        String number = cursor.getString(ColumnIndexCache.getColumnIndexOrThrow(cursor, Telephony.Sms.ADDRESS));
//        //String number = cursor.getString(DatabaseMessagesDataSource.ColumnIndexCache.getColumnIndexOrThrow(cursor, Telephony.Sms.ADDRESS));
//        String name = ContactsName.getContactName(App.getAppContext(), number);
//        String smsBody = cursor.getString(ColumnIndexCache.getColumnIndexOrThrow(cursor, Telephony.Sms.BODY));
//        String mmsBody = cursor.getString(ColumnIndexCache.getColumnIndexOrThrow(cursor, Telephony.Mms.));
//        String date = cursor.getString(ColumnIndexCache.getColumnIndexOrThrow(cursor, Telephony.Sms.DATE));
//        String dateString = DateFormatting.formatDate(date);
//
//        Conversation message = null;
//        if (name != null) {
//
//                    val smsMessageBody: String,
//                    val mmsMessageBody: String,
//                    val smsReceivedDate: String?,
//                    val mmsReceivedDate: String?
//            message = new Conversation(number, name, smsBody, date);
//        }
//        if (name == null) {
//            binding.phoneNumber.setText(number);
//        } else {
//            binding.phoneNumber.setText(name);
//        }
//        binding.message.setText(body);
//        binding.date.setText(dateString);
//        binding.setMessage(message);
//        binding.setClickListener(onClickListener);
    }

    public interface IClickListener {

        void onClick(Conversation conversation);

        void onContactIconClicked(Conversation conversation);
    }
}

package com.franjo.smsapp.ui.messages.new_message;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.franjo.smsapp.R;
import com.franjo.smsapp.databinding.FragmentNewMessageBinding;

public class NewMessageFragment extends Fragment {

    private static final int CONTACT_PICKER_RESULT = 1;


    private FragmentNewMessageBinding binding;
    private NewMessageViewModel viewModel;
    private InputMethodManager imm;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // Soft keyboard
        if (getActivity() != null) {
            imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_message, container, false);
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(this).get(NewMessageViewModel.class);
        binding.setViewModel(viewModel);

        binding.addRecipient.requestFocus();

        // Open soft keyboard
//        if (imm != null) {
//            imm.showSoftInput(etMessage, InputMethodManager.SHOW_IMPLICIT);
//        }

        sendMessage();

        return binding.getRoot();
    }

    private void sendMessage() {
        binding.sendMessage.setOnClickListener(v -> {
            if (hasNumber() && hasText()) {
                if (binding.addRecipientText.getText() != null && binding.enterMessageText.getText() != null) {
                    String phoneNumber = binding.addRecipientText.getText().toString();
                    String messageEntered = binding.enterMessageText.getText().toString();
                    try {
                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(phoneNumber, null, messageEntered, null, null);
                        Toast.makeText(getContext(), R.string.message_sent, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private boolean hasText() {
        if (binding.enterMessageText.getText() != null && binding.enterMessageText.getText().length() == 0) {
            Toast.makeText(getContext(), R.string.message_is_missing, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean hasNumber() {
        if (binding.addRecipientText.getText() != null && binding.addRecipientText.getText().length() == 0) {
            Toast.makeText(getContext(), R.string.enter_recipients, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

//    private void countSms() {
//
//        binding.enterMessageText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int aft) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                // This will show characters remaining
//                sms_count.setText(160 - s.toString().length() + getString(R.string.last_letter_count));
//            }
//        });
//
//    }

    public void pickContacts(View view) {

        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Uri contactData = data.getData();
            ContentResolver contentResolver = getActivity().getContentResolver(); // Get Content Resolver object, which will deal with Content Provider
            Cursor cursor = contentResolver.query(contactData, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {

                String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                    if (pCur != null) {

                        while (pCur.moveToNext()) {
                            String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            String name = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                            binding.addRecipientText.setText(phone);
                        }
                        cursor.close();
                        pCur.close();
                    }
                }

            }
        }

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem itemSearch = menu.findItem(R.id.search_main);
        itemSearch.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // Check if no view has focus
            if (getActivity() != null) {
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

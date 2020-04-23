package com.franjo.smsapp.ui.messages;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.franjo.smsapp.R;
import com.franjo.smsapp.data.HeadlessSmsSendService;
import com.franjo.smsapp.data.SmsData;
import com.franjo.smsapp.databinding.FragmentMessagesBinding;


public class MessagesFragment extends Fragment  {

    private static final int READ_SMS_PERMISSIONS_REQUEST = 1;
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;
    private static final int CONTACT_PICKER_RESULT = 1001;

    private Context context;
    private FragmentMessagesBinding binding;
    //    private ConversationAdapter arrayAdapter;

    //    private ImageView photoImage;
    private MessagesViewModel viewModel;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_messages, container, false);
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(this).get(MessagesViewModel.class);
        binding.setViewModel(viewModel);

        checkPermission();

        context.startService(new Intent(context, HeadlessSmsSendService.class));

        MessagesAdapter adapter = new MessagesAdapter(new MessagesAdapter.IClickListener() {
            @Override
            public void onClick(SmsData smsData) {
                viewModel.setNavigateToMessageDetails(smsData);
            }

            @Override
            public void onContactIconClicked() {
                viewModel.toContactDetailsNavigated();
            }
        });

        binding.messagesList.setAdapter(adapter);
        binding.messagesList.setHasFixedSize(true);
        binding.messagesList.setItemAnimator(new DefaultItemAnimator());



//        viewModel.loadContactPhoto().observe(getViewLifecycleOwner(), bitmap -> {
//
//        });


//
//        viewModel.showDatabaseSmsList().observe(getViewLifecycleOwner(), smsData -> {
//
//        });





//        listViewInbox = findViewById(R.id.listViewInbox);
////        listViewInbox.setTextFilterEnabled(true); // use to enable search view popup text

//        listViewInbox.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);

        // setInboxAdapter();
        // setUpListeners();
        // setupSharedPreferences();

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // List item -> to message details
        viewModel.navigateToMessageDetails().observe(getViewLifecycleOwner(), smsData -> {
            if (smsData != null) {
                MessagesFragmentDirections.MessageDetailsAction action = MessagesFragmentDirections.messageDetailsAction(smsData);
                NavHostFragment.findNavController(this).navigate(action);
                viewModel.onMessageDetailsNavigated();
            }
        });

        // Fab button -> to new Message fragment
        viewModel.navigateToNewMessage().observe(getViewLifecycleOwner(), isFabClicked -> {
            if (isFabClicked) {
                Navigation.findNavController(binding.floatingActionButton).navigate(R.id.action_navigation_messages_to_navigation_new_message);
                viewModel.doneNavigationToNewMessage();
            }
        });

        // Contact message icon -> open contacts
        viewModel.navigateToContactDetails().observe(getViewLifecycleOwner(), isContactIconClicked -> {
            if (isContactIconClicked) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, CONTACT_PICKER_RESULT);
                viewModel.doneNavigationToContactDetails();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPermissionToReadContacts();
            }
        } else if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPermissionToReadSMS();
            }
        } else {
            viewModel.showDatabaseSmsList();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getPermissionToReadContacts() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(context, R.string.allow_permission, Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_PERMISSIONS_REQUEST);
        }
    }

    // We’re checking to see whether the permission is granted already
    // if it’s not, we’re checking whether we need to explain the situation to the user
    // If so, then we’re displaying a toast message and either way, we’re then actually doing the asking
    private void getPermissionToReadSMS() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)) {
                    Toast.makeText(context, R.string.allow_permission, Toast.LENGTH_SHORT).show();
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_SMS}, READ_SMS_PERMISSIONS_REQUEST);
            }
        }
    }

    // We handle the response via onRequestPermissionResult
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_SMS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Our toast message confirms the answer and if it is positive, we’re then using our next new method
                Toast.makeText(context, R.string.sms_permission_granted, Toast.LENGTH_SHORT).show();
                viewModel.showDatabaseSmsList();
            } else {
                Toast.makeText(context, R.string.sms_permission_denied, Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void setUpListeners() {

//        listViewInbox.setOnItemClickListener((adapterView, view, position, id) -> {
//
//            Intent intent = new Intent(ConversationFragment.this, DetailsMessagesInbox.class);
//
//            // Move the cursor to required position
//            if (cursor != null) {
//                cursor.moveToPosition(position);
//            }
//
//            String senderNumber = null;
//            if (cursor != null) {
//                senderNumber = cursor.getString(cursor.getColumnIndexOrThrow("address"));
//            }
//            intent.putExtra("broj", senderNumber);
//
//            String senderNumberName = ((TextView) view.findViewById(R.id.txtBroj)).getText().toString();
//            intent.putExtra("name", senderNumberName);
//
//            String smsBody = ((TextView) view.findViewById(R.id.txtPoruka)).getText().toString();
//            intent.putExtra("poruka", smsBody);
//
//            String dateTime = ((TextView) view.findViewById(R.id.txtVrijeme)).getText().toString();
//            intent.putExtra("vrijeme", dateTime);
//
//            String minute = ((TextView) view.findViewById(R.id.txtMinute)).getText().toString();
//            intent.putExtra("minute", minute);
//
//
//            startActivity(intent);
//
//
//        });
//
//        listViewInbox.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
//            @Override
//            public void onItemCheckedStateChanged(ActionMode actionMode, int position, long l, boolean checked) {
//
//                int counter = listViewInbox.getCheckedItemCount();
//                actionMode.setTitle(counter + "");
//                selection_list.add(smsList.get(position));
//
//            }
//
//            @Override
//            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
//                // Inflate the options menu from XML
//                MenuInflater inflater = actionMode.getMenuInflater();
//                inflater.inflate(R.menu.menu_delete, menu);
//
//                return true;
//            }
//
//            @Override
//            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
//
//                return false;
//            }
//
//            @Override
//            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
//                switch (menuItem.getItemId()) {
//                    case R.id.delete:
//                        AlertDialog.Builder alert = new AlertDialog.Builder(ConversationFragment.this);
//                        alert.setIconAttribute(android.R.attr.alertDialogIcon);
//                        alert.setTitle(R.string.permanently_message_delete);
//                        alert.setMessage(R.string.delete_message);
//                        alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                for (final SMSData sms : selection_list) {
//                                    arrayAdapter.remove(sms);
//                                    // Notifies the attached observers that the underlying data has been changed
//                                    // and any View reflecting the data set should refresh itself.
//                                    arrayAdapter.notifyDataSetChanged();
//
//
//                                }
//                            }
//                        });
//
//                        alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//
//
//                        alert.create();
//                        alert.show();
//
//                        actionMode.finish();
//
//
//                        return true;
//
//                    default:
//                        return false;
//                }
//
//            }
//
//
//            @Override
//            public void onDestroyActionMode(ActionMode actionMode) {
//                actionMode.finish();
//
//            }
//
//        });

    }


//    public static boolean showFullText() {
//       // Getting the value of the full sms checkbox preference
//       return sharedPreferences.getBoolean(FULL_SMS, true);
//    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri contactData = data.getData();
            Cursor c = null;
            if (contactData != null) {
                c = context.getContentResolver().query(contactData, null, null, null, null);
            }
            if (c != null && c.moveToFirst()) {
                String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
                //                Intent intent = new Intent(CurrentActivity.this, NewActivity.class);
//                intent.putExtra("name", name);
//                startActivityForResult(intent, 0);
            }
            if (c != null) {
                c.close();
            }
        }
    }


}






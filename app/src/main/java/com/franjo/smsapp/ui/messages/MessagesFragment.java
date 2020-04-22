package com.franjo.smsapp.ui.messages;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.franjo.smsapp.R;
import com.franjo.smsapp.data.HeadlessSmsSendService;
import com.franjo.smsapp.data.SmsData;
import com.franjo.smsapp.databinding.FragmentMessagesBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MessagesFragment extends Fragment implements MessagesAdapter.OnClickListener {

    private static final int READ_SMS_PERMISSIONS_REQUEST = 1;
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;

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

        MessagesAdapter adapter = new MessagesAdapter(this);
        binding.messagesList.setAdapter(adapter);
        binding.messagesList.setHasFixedSize(true);
        binding.messagesList.setItemAnimator(new DefaultItemAnimator());

        viewModel.showDatabaseSmsList().observe(getViewLifecycleOwner(), smsData -> {

        });

        binding.floatingActionButton.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.action_navigation_messages_to_navigation_new_message));




//        listViewInbox = findViewById(R.id.listViewInbox);
////        listViewInbox.setTextFilterEnabled(true); // use to enable search view popup text

//        listViewInbox.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);

        // setInboxAdapter();
        // setUpListeners();
        // setupSharedPreferences();

        return binding.getRoot();
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

    @Override
    public void onClick(SmsData smsData) {
        viewModel.showSmsDetails(smsData);
    }


    //    private void setupSharedPreferences() {
//        // A reference to the default shared preferences from the PreferenceManager class
//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//
//    }


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

//
//    public void pickPhoto(View view) {
//        Intent intent = new Intent();
//        // Show only images, no videos or anything else
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        // Always show the chooser (if there are multiple options available)
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//
//    }


//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        Bitmap bitmap = null;
//        String imagePath = null;
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//
//            Uri uri = data.getData();
//            String[] filePath = { MediaStore.Images.Media.DATA };
//            Cursor cursor = getContentResolver().query(uri, filePath, null, null, null);
//
//            if (cursor != null) {
//                cursor.moveToFirst();
//            }
//            if (cursor != null) {
//                imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
//            }
//
//            if (cursor != null) {
//                cursor.close();
//            }
//
//            photoImage = (ImageView) findViewById(R.id.contactPhotoPick);
//            // Resample the saved image to fit the ImageView
//            bitmap = resamplePic(this, imagePath);
//            photoImage.setImageBitmap(bitmap);
//
//
//        }
//    }

//    static Bitmap resamplePic(Context context, String imagePath) {
//
//        // Get device screen size information
//        DisplayMetrics metrics = new DisplayMetrics();
//        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        manager.getDefaultDisplay().getMetrics(metrics);
//
//        int targetH = metrics.heightPixels;
//        int targetW = metrics.widthPixels;
//
//        // Get the dimensions of the original bitmap
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        bmOptions.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(imagePath, bmOptions);
//        int photoW = bmOptions.outWidth;
//        int photoH = bmOptions.outHeight;
//
//        // Determine how much to scale down the image
//        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
//
//        // Decode the image file into a Bitmap sized to fill the View
//        bmOptions.inJustDecodeBounds = false;
//        bmOptions.inSampleSize = scaleFactor;
//
//        return BitmapFactory.decodeFile(imagePath);
//    }


}






package com.franjo.smsapp.ui.messages.conversations_details;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.franjo.smsapp.R;
import com.franjo.smsapp.databinding.FragmentDetailsBinding;
import com.franjo.smsapp.domain.Conversation;

import java.util.ArrayList;

import static com.franjo.smsapp.util.SoftKeyboard.showKeyboard;

public class DetailsFragment extends Fragment {

    public static final int REQUEST_CAMERA = 0;
    public static final int SELECT_FILE = 1;

    private static int MAX_SMS_MESSAGE_LENGTH = 160;
    private static String SENT = "SMS_SENT";
    private static String DELIVERED = "SMS_DELIVERED";


    private Context context;
    private FragmentDetailsBinding binding;
    private Conversation conversation;
    private DetailsViewModel viewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false);

        if (getArguments() != null) {
            conversation = DetailsFragmentArgs.fromBundle(getArguments()).getConversation();
        }

        DetailsViewModelFactory viewModelFactory = new DetailsViewModelFactory(conversation.getThreadId());
        viewModel = new ViewModelProvider(this, viewModelFactory).get(DetailsViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DetailsAdapter detailsAdapter = new DetailsAdapter();
        binding.detailsList.setAdapter(detailsAdapter);
        binding.detailsList.setHasFixedSize(true);

        binding.enterMessage.requestFocus();
        binding.enterMessageFull.requestFocus();

        binding.fabGoToListEnd.hide();

        sendMessage();
//        animateArrow();
//        animateEditTextEnterMessage();
        showScrollFabVisibility();

        //  pickGalleryPhoto();
        //  pickCameraPhoto();

    }

    public void sendMessage() {
        binding.sendMessage.setOnClickListener(v -> {
            if (binding.enterMessage.getText().length() != 0) {
                if (conversation.getSnippet() == null) {
                    //  sendMMSMessage();
                } else {
                    sendSMSMessage();
                }
            } else {
                Toast.makeText(getContext(), R.string.message_is_missing, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendSMSMessage() {
        String phoneNumber = conversation.getRecipient();
        String messageEntered = binding.enterMessage.getText().toString();
        int length = messageEntered.length();
        try {
            PendingIntent piSent = PendingIntent.getBroadcast(context, 0, new Intent(SENT), 0);
            PendingIntent piDelivered = PendingIntent.getBroadcast(context, 0, new Intent(DELIVERED), 0);
            SmsManager smsManager = SmsManager.getDefault();
            if (length > MAX_SMS_MESSAGE_LENGTH) {
                ArrayList<String> messageList = smsManager.divideMessage(messageEntered);
                smsManager.sendMultipartTextMessage(phoneNumber, null, messageList, null, null);
            } else {
                smsManager.sendTextMessage(phoneNumber, null, messageEntered, piSent, piDelivered);
                Toast.makeText(getContext(), R.string.message_sent, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void sendMMSMessage(Uri contentUri, String locationUrl, Bundle configOverrides) {
        PendingIntent piSent = PendingIntent.getBroadcast(context, 0, new Intent(SENT), 0);
        // contentUri the content Uri from which the message pdu will be read
        // locationUrl the optional location url where message should be sent to
        // configOverrides the carrier-specific messaging configuration values to override for sending the message
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendMultimediaMessage(context, contentUri, locationUrl, configOverrides, piSent);
    }

    private void pickGalleryPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select file"), SELECT_FILE);
    }

    private void pickCameraPhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

//    private void animateArrow() {
//        binding.ivRedoArrow.setOnClickListener(v -> {
//            binding.ivApps.setVisibility(View.VISIBLE);
//            binding.ivCamera.setVisibility(View.VISIBLE);
//            binding.ivGallery.setVisibility(View.VISIBLE);
//            binding.enterMessage.setVisibility(View.VISIBLE);
//            binding.ivRedoArrow.setVisibility(View.GONE);
//            binding.enterMessageFull.setVisibility(View.GONE);
//        });
//    }
//
//    @SuppressLint("ClickableViewAccessibility")
//    private void animateEditTextEnterMessage() {
//        binding.enterMessage.setOnTouchListener((v, event) -> {
//            binding.enterMessageFull.requestFocus();
//
//            binding.llImagesRoot.setVisibility(View.GONE);
//            binding.enterMessage.setVisibility(View.GONE);
//
//            binding.ivRedoArrow.setVisibility(View.VISIBLE);
//            binding.enterMessageFull.setVisibility(View.VISIBLE);
//            showKeyboard(context);
//            return false;
//        });
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bundle bundle = null;
                if (data != null) {
                    bundle = data.getExtras();
                }
                if (bundle != null) {
                    Bitmap bmp = (Bitmap) bundle.get("data");
                }
            } else if (requestCode == SELECT_FILE) {
                if (data != null) {
                    Uri selectedImageUri = data.getData();
                }
            }

        }
    }

    private void showScrollFabVisibility() {
        binding.detailsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    binding.fabGoToListEnd.hide();
                } else if (dy < 0) {
                    binding.fabGoToListEnd.show();
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }


}

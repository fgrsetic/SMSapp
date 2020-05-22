//package com.franjo.smsapp.ui.messages.details;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.databinding.DataBindingUtil;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.franjo.smsapp.R;
//import com.franjo.smsapp.persistence.model.Message;
//import com.franjo.smsapp.databinding.FragmentMessageDetailsBinding;
//
//public class MessageDetailsFragment extends Fragment {
//
//    private Context context;
//    private MessageDetailsViewModel viewModel;
//    private FragmentMessageDetailsBinding binding;
//    private Message message;
//
//
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        this.context = context;
//    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message_details, container, false);
//        View view = binding.getRoot();
//
//        binding.setLifecycleOwner(this);
//
//        if (getArguments() != null) {
//            message = MessageDetailsFragmentArgs.fromBundle(getArguments()).getMessage();
//        }
//
//        viewModel = new ViewModelProvider(this).get(MessageDetailsViewModel.class);
//        binding.setViewModel(viewModel);
//
//        if (message != null) {
//            viewModel.setMessagesList(message.getPhoneNumber());
//        }
//
//        binding.messagesListDetails.setAdapter(new MessageDetailsAdapter());
//        binding.messagesListDetails.setHasFixedSize(true);
//
//        return view;
//    }
//
//}

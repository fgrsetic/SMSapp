package com.franjo.smsapp.ui.messages.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.franjo.smsapp.R;
import com.franjo.smsapp.databinding.FragmentMessageDetailsBinding;

public class MessageDetailsFragment extends Fragment {

    private MessageDetailsViewModel mViewModel;
    private FragmentMessageDetailsBinding binding;

    public static MessageDetailsFragment newInstance() {
        return new MessageDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message_details, container, false);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MessageDetailsViewModel.class);
        // TODO: Use the ViewModel
    }

}

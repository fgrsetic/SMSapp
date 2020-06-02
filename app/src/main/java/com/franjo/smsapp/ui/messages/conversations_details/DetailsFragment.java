package com.franjo.smsapp.ui.messages.conversations_details;

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
import com.franjo.smsapp.databinding.FragmentDetailsBinding;
import com.franjo.smsapp.domain.Conversation;

public class DetailsFragment extends Fragment {

    private Conversation conversation;
    private int conversationsThreadId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentDetailsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false);
        binding.setLifecycleOwner(this);

        if (getArguments() != null) {
            conversation = DetailsFragmentArgs.fromBundle(getArguments()).getConversation();
        }
        if (conversation != null) {
            conversationsThreadId = conversation.getThreadId();
        }

        DetailsViewModelFactory viewModelFactory = new DetailsViewModelFactory(conversationsThreadId);
        DetailsViewModel viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(DetailsViewModel.class);
        binding.setViewModel(viewModel);

        binding.detailsList.setAdapter(new DetailsAdapter());
        binding.detailsList.setHasFixedSize(true);

        return binding.getRoot();
    }

}

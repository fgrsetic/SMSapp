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

    private FragmentDetailsBinding binding;
    private int threadID;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false);

        if (getArguments() != null) {
            threadID = DetailsFragmentArgs.fromBundle(getArguments()).getThreadID();
        }

        DetailsViewModelFactory viewModelFactory = new DetailsViewModelFactory(threadID);
        DetailsViewModel viewModel = new ViewModelProvider(this, viewModelFactory).get(DetailsViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DetailsAdapter detailsAdapter = new DetailsAdapter();
        binding.detailsList.setAdapter(detailsAdapter);
        binding.detailsList.setHasFixedSize(true);
    }
}

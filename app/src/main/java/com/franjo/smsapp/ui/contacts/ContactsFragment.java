package com.franjo.smsapp.ui.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.franjo.smsapp.R;
import com.franjo.smsapp.data.Contact;
import com.franjo.smsapp.databinding.FragmentContactsBinding;

public class ContactsFragment extends Fragment {


    private FragmentContactsBinding binding;
    private ContactsViewModel viewModel;
    private ContactsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contacts, container, false);
        View view = binding.getRoot();
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(this).get(ContactsViewModel.class);
        binding.setViewModel(viewModel);

        adapter = new ContactsAdapter(contact -> {

        });

        binding.contactsList.setAdapter(adapter);
        binding.contactsList.setHasFixedSize(true);
        binding.contactsList.setItemAnimator(new DefaultItemAnimator());


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Fab button -> to new Message fragment
        viewModel.navigateToNewMessage().observe(getViewLifecycleOwner(), isFabClicked -> {
            if (isFabClicked) {
                Navigation.findNavController(binding.floatingActionButton).navigate(R.id.newMessageAction);
                viewModel.doneNavigationToNewMessage();
            }
        });
    }
}

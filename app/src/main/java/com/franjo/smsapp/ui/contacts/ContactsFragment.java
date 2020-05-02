package com.franjo.smsapp.ui.contacts;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.franjo.smsapp.R;
import com.franjo.smsapp.databinding.FragmentContactsBinding;
import com.franjo.smsapp.ui.search.SearchViewModel;

public class ContactsFragment extends Fragment {

    private Context context;
    private FragmentContactsBinding binding;
    private ContactsViewModel viewModel;
    private ContactsAdapter adapter;
    private SearchViewModel searchViewModel;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contacts, container, false);
        View view = binding.getRoot();

        setHasOptionsMenu(true);

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(this).get(ContactsViewModel.class);
        binding.setViewModel(viewModel);

        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

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
                Navigation.findNavController(binding.floatingActionButton).navigate(R.id.new_message_action);
                viewModel.doneNavigationToNewMessage();
            }
        });

//        // Search widget -> open search fragment
//        viewModel.observeSubmittedQuery().observe(this, cursor -> {
//            if (cursor != null) {
//                shareViewModel.setSubmittedCursor(cursor);
//                NavHostFragment.findNavController(this).navigate(ContactsFragmentDirections.searchAction());
//                viewModel.closeSubmittedTextSearchList();
//            }
//        });
//
//        viewModel.observeQueryChanged().observe(getViewLifecycleOwner(), cursor -> {
//            if (cursor != null) {
//                shareViewModel.setChangedTextCursor(cursor);
//                NavHostFragment.findNavController(this).navigate(ContactsFragmentDirections.searchAction());
//                viewModel.closeChangedQuerySearchList();
//            }
//        });
    }

}

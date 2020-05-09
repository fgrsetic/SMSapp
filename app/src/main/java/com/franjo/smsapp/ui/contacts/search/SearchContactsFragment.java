package com.franjo.smsapp.ui.contacts.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.franjo.smsapp.R;
import com.franjo.smsapp.databinding.FragmentSearchContactsBinding;

public class SearchContactsFragment extends Fragment implements SearchView.OnQueryTextListener {

    private SearchContactsViewModel searchContactsViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentSearchContactsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_contacts, container, false);
        View view = binding.getRoot();
        binding.setLifecycleOwner(this);

        searchContactsViewModel = new ViewModelProvider(requireActivity()).get(SearchContactsViewModel.class);
        binding.setViewModel(searchContactsViewModel);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_main).getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);

        MenuItem searchItem = menu.findItem(R.id.search_main);
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                NavHostFragment.findNavController(SearchContactsFragment.this).navigate(R.id.contacts_action);
                return true;
            }
        });

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        searchContactsViewModel.loadSearch(newText);
        return true;
    }

}
package com.franjo.smsapp.ui.messages.search;

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
import com.franjo.smsapp.databinding.FragmentSearchMessagesBinding;

public class SearchMessagesFragment extends Fragment implements SearchView.OnQueryTextListener {

    private SearchMessagesViewModel searchMessagesViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentSearchMessagesBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_messages, container, false);
        View view = binding.getRoot();
        binding.setLifecycleOwner(this);

        searchMessagesViewModel = new ViewModelProvider(requireActivity()).get(SearchMessagesViewModel.class);
        binding.setViewModel(searchMessagesViewModel);

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
                NavHostFragment.findNavController(SearchMessagesFragment.this).navigate(R.id.messages_action);
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
        searchMessagesViewModel.loadSearch(newText);
        return true;
    }

}
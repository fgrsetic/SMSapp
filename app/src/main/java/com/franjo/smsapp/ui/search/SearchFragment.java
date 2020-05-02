package com.franjo.smsapp.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.franjo.smsapp.R;
import com.franjo.smsapp.data.SmsData;
import com.franjo.smsapp.databinding.FragmentSearchBinding;

public class SearchFragment extends Fragment implements
        SearchView.OnQueryTextListener,
        SearchView.OnCloseListener {


    static SearchFragment newInstance() {
        return new SearchFragment();
    }

    private SearchViewModel searchViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentSearchBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        View view = binding.getRoot();
        binding.setLifecycleOwner(this);

        searchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        binding.setViewModel(searchViewModel);

        SearchAdapter searchAdapter = new SearchAdapter(new SearchAdapter.IClickListener() {
            @Override
            public void onClick(SmsData smsData) {

            }

            @Override
            public void onContactIconClicked(SmsData smsData) {

            }
        });

        binding.searchedList.setAdapter(searchAdapter);
        binding.searchedList.setHasFixedSize(true);
        binding.searchedList.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.setIconified(false);
    }

    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        searchViewModel.querySmsMessages(newText);
        return true;
    }

}

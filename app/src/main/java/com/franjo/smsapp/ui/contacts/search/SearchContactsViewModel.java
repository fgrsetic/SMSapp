package com.franjo.smsapp.ui.contacts.search;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.franjo.smsapp.app.AppExecutors;
import com.franjo.smsapp.data.database.DatabaseContactsDataSource;
import com.franjo.smsapp.data.database.IContactsDataSource;

public class SearchContactsViewModel extends ViewModel {

    private IContactsDataSource dataSource;
    private MutableLiveData<Cursor> searchResult;


    public SearchContactsViewModel() {
        dataSource = new DatabaseContactsDataSource();
    }

    public LiveData<Cursor> observeSearch() {
        if (searchResult == null) {
            searchResult = new MutableLiveData<>();
        }
        return searchResult;
    }

    void loadSearch(String searchText) {
        AppExecutors.getInstance().diskIO().execute(() ->
                searchResult.postValue(dataSource.performSearch(searchText)));

    }

}
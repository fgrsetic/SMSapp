package com.franjo.smsapp.ui.messages.search;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.franjo.smsapp.app.AppExecutors;

public class SearchMessagesViewModel extends ViewModel {

//    private IMessagesDataSource databaseMessages;
//    private MutableLiveData<Cursor> searchResult;
//
//
//    public SearchMessagesViewModel() {
//        databaseMessages = new DatabaseMessagesDataSource();
//    }
//
//    public LiveData<Cursor> observeSearch() {
//        if (searchResult == null) {
//            searchResult = new MutableLiveData<>();
//        }
//        return searchResult;
//    }
//
//    void loadSearch(String searchText) {
//        AppExecutors.getInstance().diskIO().execute(() -> searchResult.postValue(databaseMessages.performSearch(searchText)));
//    }


}
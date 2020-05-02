package com.franjo.smsapp.ui.search;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.franjo.smsapp.data.IMessages;
import com.franjo.smsapp.data.MessagesDatabase;
import com.franjo.smsapp.data.SmsData;

import java.util.List;

public class SearchViewModel extends AndroidViewModel {

    private Context context;
    private IMessages databaseMessages;
    private MutableLiveData<List<SmsData>> navigateToSearchFragment;


    public SearchViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
        databaseMessages = new MessagesDatabase();
    }

    // Search
    public LiveData<List<SmsData>> observeQueryChanged() {
        if (navigateToSearchFragment == null) {
            navigateToSearchFragment = new MutableLiveData<>();
        }
        return navigateToSearchFragment;
    }

    void querySmsMessages(String query) {
        navigateToSearchFragment.postValue(databaseMessages.performSearch(context, query));
    }

}

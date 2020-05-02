package com.franjo.smsapp.ui.contacts;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.franjo.smsapp.data.Contact;
import com.franjo.smsapp.data.IMessages;
import com.franjo.smsapp.data.MessagesDatabase;

import java.util.List;

public class ContactsViewModel extends AndroidViewModel {

    private Context context;
    private IMessages databaseMessages;

    private MutableLiveData<List<Contact>> navigateToContactList;
    private MutableLiveData<Boolean> navigateToNewMessage;

    private MutableLiveData<Cursor> searchSubmittedTextQuery;
    private MutableLiveData<Cursor> searchChangedTextQuery;

    public ContactsViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
        databaseMessages = new MessagesDatabase();
    }

    // Go to contact details
    public LiveData<List<Contact>> showContactList() {
        if (navigateToContactList == null) {
            navigateToContactList = new MutableLiveData<>();
            loadContactList();
        }
        return navigateToContactList;
    }

    private void loadContactList() {
        navigateToContactList.setValue(databaseMessages.openContactList(context));
    }

    void doneNavigationToContactLists() {
        navigateToContactList.setValue(null);
    }


    // Go to new message
    LiveData<Boolean> navigateToNewMessage() {
        if (navigateToNewMessage == null) {
            navigateToNewMessage = new MutableLiveData<>();
        }
        return navigateToNewMessage;
    }

    public void toNewMessageNavigated() {
        navigateToNewMessage.setValue(true);
    }

    void doneNavigationToNewMessage() {
        navigateToNewMessage.setValue(false);
    }



    // Submit search
    public LiveData<Cursor> observeSubmittedQuery() {
        if (searchSubmittedTextQuery == null) {
            searchSubmittedTextQuery = new MutableLiveData<>();
        }
        return searchSubmittedTextQuery;
    }

    public void submittedTextQuery(String query) {
       // searchSubmittedTextQuery.setValue(databaseMessages.performSubmittedQueryTextSearch(context, query));
    }

    public void closeSubmittedTextSearchList() {
        searchSubmittedTextQuery.setValue(null);
    }


    // Change query search
    public LiveData<Cursor> observeQueryChanged() {
        if (searchChangedTextQuery == null) {
            searchChangedTextQuery = new MutableLiveData<>();
        }
        return searchChangedTextQuery;
    }

    public void changedTextQuery(String query) {
       // searchChangedTextQuery.setValue(databaseMessages.performChangedQueryTextSearch(context, query));
    }

    public void closeChangedQuerySearchList() {
        searchChangedTextQuery.setValue(null);
    }

}

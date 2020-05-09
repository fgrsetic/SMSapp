package com.franjo.smsapp.ui.contacts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.franjo.smsapp.app.AppExecutors;
import com.franjo.smsapp.data.database.DatabaseContactsDataSource;
import com.franjo.smsapp.data.database.IContactsDataSource;
import com.franjo.smsapp.data.model.Contact;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContactsViewModel extends ViewModel {

    private IContactsDataSource databaseContacts;
    private MutableLiveData<List<Contact>> navigateToContactList;
    private MutableLiveData<Boolean> navigateToNewMessage;


    public ContactsViewModel() {
        databaseContacts = new DatabaseContactsDataSource();
    }

    // 1) All contacts
    public LiveData<List<Contact>> showContactList() {
        if (navigateToContactList == null) {
            navigateToContactList = new MutableLiveData<>();
            AppExecutors.getInstance().mainThread().execute(this::loadContactList);
        }
        return navigateToContactList;
    }

    private void loadContactList() {
        List<Contact> contactsList = databaseContacts.getAllContacts();
        AppExecutors.getInstance().diskIO().execute(() -> navigateToContactList.postValue(removedDuplicatesList(contactsList)));
    }


    private List<Contact> removedDuplicatesList(List<Contact> listWithDuplicates) {
        Set<String> attributes = new HashSet<>();
        List<Contact> duplicates = new ArrayList<>();

        for (Contact contact : listWithDuplicates) {
            if (attributes.contains(contact.getPhoneNumber())) {
                duplicates.add(contact);
            }
            attributes.add(contact.getPhoneNumber());
        }
        listWithDuplicates.removeAll(duplicates);
        return new ArrayList<>(listWithDuplicates);
    }


    // 2) New message
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

}

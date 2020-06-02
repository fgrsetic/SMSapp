package com.franjo.smsapp.data.device_storage.contacts;

import android.database.Cursor;
import android.net.Uri;

import com.franjo.smsapp.data.model.Contact;
import com.franjo.smsapp.domain.Conversation;

import java.util.List;

public interface IContactsDataSource {

    List<Contact> getAllContacts();

    Cursor performSearch(String searchText);

    Uri openContactDetails(Conversation conversation);
}

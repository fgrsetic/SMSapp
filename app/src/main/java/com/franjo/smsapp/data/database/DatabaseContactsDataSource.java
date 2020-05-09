package com.franjo.smsapp.data.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.franjo.smsapp.app.App;
import com.franjo.smsapp.data.model.Contact;
import com.franjo.smsapp.data.model.Message;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DatabaseContactsDataSource implements IContactsDataSource {

    private static final Uri CONTACTS_URI = ContactsContract.Contacts.CONTENT_URI;

    private static final String[] PROJECTION_CONTACTS = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.HAS_PHONE_NUMBER,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
    };

    private static final String SELECTION_CONTACTS = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?";
    private static final String SORT_ORDER_CONTACTS = "date DESC";

    private Context context;
    private ContentResolver contentResolver;


    public DatabaseContactsDataSource() {
        context = App.getAppContext();
        contentResolver = context.getContentResolver();
    }

    @Override
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<>();
        Cursor cursor = contentResolver.query(CONTACTS_URI, null, null, null, null);

        if (cursor != null && cursor.moveToNext()) {
            String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

            // Bitmap photo = loadContactPhoto(phoneNumber, context);
            Contact contact = new Contact();
            contact.setName(name);
            contact.setPhoneNumber(phoneNumber);
            // contact.setContactPicture(photo);
            contactList.add(contact);
        }
        if (cursor != null) {
            cursor.close();
        }
        return contactList;
    }

    @Override
    public Uri openContactDetails(Message data) {
        ContentResolver contentResolver = App.getAppContext().getContentResolver();
        Uri contactUri = null;
        Uri lookupUri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI, Uri.encode(data.getPhoneNumber()));
        String[] phoneNumberProjection = {
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER,
                ContactsContract.Contacts.LOOKUP_KEY
        };

        Cursor cursor = contentResolver.query(lookupUri, phoneNumberProjection, null, null, null);

        if (cursor != null && cursor.moveToNext()) {
            int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
            if (hasPhoneNumber > 0) {
                String lookupKey = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.LOOKUP_KEY));
                long contactId = cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                contactUri = ContactsContract.Contacts.getLookupUri(contactId, lookupKey);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return contactUri;

    }


    public Bitmap loadContactPhoto(String phoneNumber) {
        long contactID = getContactIDFromNumber(phoneNumber);
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactID);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = contentResolver.query(photoUri, new String[]{ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    return BitmapFactory.decodeStream(new ByteArrayInputStream(data));
                } else {
                    Toast.makeText(context, "No image", Toast.LENGTH_SHORT).show();
                }
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    // Get contact id by using the phone number
    private long getContactIDFromNumber(String contactNumber) {
        String UriContactNumber = Uri.encode(contactNumber);
        long phoneContactID = new Random().nextInt();
        Cursor contactLookupCursor = contentResolver.query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, UriContactNumber),
                new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID}, null, null, null);
        if (contactLookupCursor != null) {
            while (contactLookupCursor.moveToNext()) {
                phoneContactID = contactLookupCursor.getLong(contactLookupCursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
            }
        }
        if (contactLookupCursor != null) {
            contactLookupCursor.close();
        }
        return phoneContactID;
    }


    // Search
    @Override
    public Cursor performSearch(String searchText) {
        return getCursor(searchText);
    }

    private Cursor getCursor(String searchText) {
        String[] selectionArgs = new String[]{"%" + searchText + "%"};
        Cursor cursor = contentResolver.query(CONTACTS_URI, PROJECTION_CONTACTS, SELECTION_CONTACTS, selectionArgs, "");
        if (searchText.isEmpty()) {
            return null;
        } else {
            return cursor;
        }
    }
}

package com.example.roomdatabase.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.roomdatabase.model.Contact;
import com.example.roomdatabase.util.ContactRoomDatabase;

import java.util.List;

public class ContactRepository {

    private static ContactDao contactDao;
    private final  LiveData<List<Contact>> allContacts;

    public ContactRepository(Application application) {
        ContactRoomDatabase db = ContactRoomDatabase.getDatabase(application);
        contactDao = db.contactDao();

        allContacts = contactDao.getAllContacts();
    }

    public LiveData<List<Contact>> getAllData() {
        return allContacts;
    }

    public void insert(Contact contact) {
        ContactRoomDatabase.databaseWriterExecutor.execute(() -> {
            contactDao.insert(contact);
        });
    }

    public LiveData<Contact> get(int id) {
        return contactDao.get(id);
    }

    public void update(Contact contact) {
        ContactRoomDatabase.databaseWriterExecutor.execute(() -> {
            contactDao.update(contact);
        });
    }

    public void delete(Contact contact) {
        ContactRoomDatabase.databaseWriterExecutor.execute(() -> {
            contactDao.delete(contact);
        });
    }

    public void deleteAll() {
        ContactRoomDatabase.databaseWriterExecutor.execute(() -> contactDao.deleteAll());
    }



}

package com.example.roomdatabase.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.roomdatabase.data.ContactDao;
import com.example.roomdatabase.model.Contact;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Contact.class}, version = 1, exportSchema = false)
public abstract class ContactRoomDatabase extends RoomDatabase {

    public abstract ContactDao contactDao();

    public static final int NUMBER_OF_THREADS = 4;

    public static volatile ContactRoomDatabase INSTANCE;

    public static ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ContactRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ContactRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ContactRoomDatabase.class, "contact_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }

        }
        return INSTANCE;
    }

    //all we are doing here is we are making sure the database runs in the background. or populating(adding info to a file. eg database.) the db
    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        //override the onCreate cause we want to have some data in the db the moment the app is run.
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriterExecutor.execute(() -> {
                ContactDao contactDao = INSTANCE.contactDao();
                contactDao.deleteAll();

                Contact contact = new Contact("Jeffrey", "Software Engineer");
                contactDao.insert(contact);

                contact = new Contact("Kwakye", "UI/UX designer");
                contactDao.insert(contact);

            });
        }
    };
}

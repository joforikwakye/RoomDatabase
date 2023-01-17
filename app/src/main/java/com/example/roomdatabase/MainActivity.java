package com.example.roomdatabase;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;


import com.example.roomdatabase.adapter.RecyclerviewAdapter;
import com.example.roomdatabase.model.Contact;
import com.example.roomdatabase.model.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;


public class MainActivity extends AppCompatActivity implements RecyclerviewAdapter.OnContactClickListener {

    public static final String CONTACT_ID = "contact_id";
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int NEW_CONTACT_ACTIVITY_RESULT_CODE = -1;
                if (result.getResultCode() == NEW_CONTACT_ACTIVITY_RESULT_CODE) {
                    assert result.getData() != null;

                    String name = result.getData().getStringExtra(NewContact.NAME_REPLY);
                    String occupation = result.getData().getStringExtra(NewContact.OCCUPATION_REPLY);

                    Contact contact = new Contact(name, occupation);

                    ContactViewModel.insert(contact);

                }
            }
    );
    private ContactViewModel contactViewModel;
    private RecyclerView recyclerView;
    private RecyclerviewAdapter recyclerviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        //in order to instantiate the viewModel class, we go through a viewModel provider and invoke factory
        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(MainActivity.this
                .getApplication())
                .create(ContactViewModel.class);

        //this is where all the contacts we have created live.all the contacts we have added are inside of the list<Contact>
        contactViewModel.getAllContacts().observe(this, contacts -> {
            recyclerviewAdapter = new RecyclerviewAdapter(contacts, MainActivity.this, this);

            recyclerView.setAdapter(recyclerviewAdapter);
        });



        FloatingActionButton fab = findViewById(R.id.add_contact_fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewContact.class);
            activityResultLauncher.launch(intent);
        });
    }

    @Override
    public void onContactClick(int position) {
        Contact contact = Objects.requireNonNull(contactViewModel.getAllContacts().getValue()).get(position);

        Intent intent = new Intent(MainActivity.this,NewContact.class);
        intent.putExtra(CONTACT_ID, contact.getId());
        startActivity(intent);

    }
}
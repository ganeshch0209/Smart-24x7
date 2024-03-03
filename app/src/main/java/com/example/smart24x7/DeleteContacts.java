package com.example.smart24x7;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DeleteContacts extends AppCompatActivity {

    private EditText contactIdEditText;
    private Button deleteButton,deleteAllButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_contacts);

        contactIdEditText = findViewById(R.id.id);
        deleteButton = findViewById(R.id.btndelete);
        deleteAllButton=findViewById(R.id.btndeleteall);
        databaseHelper = new DatabaseHelper(this);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactId = contactIdEditText.getText().toString();

                if (contactId.isEmpty()) {
                    Toast.makeText(DeleteContacts.this, "Please Enter a Contact ID", Toast.LENGTH_SHORT).show();
                } else {
                    boolean contactDeleted = databaseHelper.deleteContact(Integer.parseInt(contactId));
                    if (contactDeleted) {
                        Toast.makeText(DeleteContacts.this, "Contact Deleted Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DeleteContacts.this, "Failed to Delete Contact", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean allContactsDeleted = databaseHelper.deleteAllContacts();
                if (allContactsDeleted) {
                    Toast.makeText(DeleteContacts.this, "All Contacts Deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DeleteContacts.this, "Failed to Delete Contacts", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

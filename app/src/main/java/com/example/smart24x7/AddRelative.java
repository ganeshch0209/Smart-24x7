package com.example.smart24x7;

import static com.example.smart24x7.DatabaseHelper.TABLE_NAME;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
//import android.support.v7.app.AppCompatActivity;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.smart24x7.DatabaseHelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class AddRelative extends AppCompatActivity {

    private static final int REQUEST_CALL = 1;
    DatabaseHelper myDB;
    Button btnAdd, btnView,btnDelete;
    EditText editText, editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_relative);
        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnView = (Button) findViewById(R.id.btnView);
        btnDelete =(Button)findViewById(R.id.btnDelete);
        myDB = new DatabaseHelper(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sname = editText.getText().toString();
                String snumber = editText2.getText().toString();
                if (editText.length() != 0) {
                    AddData(sname,snumber);
                    editText.setText("");
                    editText2.setText("");
                } else {
                    Toast.makeText(AddRelative.this, "Text Field is Empty!", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddRelative.this, ViewListContents.class);
                startActivity(intent);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),DeleteContacts.class));
            }
        });

    }
    public void AddData(String sname,String snumber) {
        myDB=new DatabaseHelper(this);
        String name=sname;
        String number=snumber;
        SQLiteDatabase db=myDB.getWritableDatabase();
        try{
            String ins = "INSERT INTO "+ TABLE_NAME +" values("+null+",'"+name+"',"+number+");";
            db.execSQL(ins);
            Toast.makeText(this,"Inserted Successfully !",Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
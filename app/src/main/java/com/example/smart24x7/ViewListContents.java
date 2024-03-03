package com.example.smart24x7;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.example.smart24x7.DatabaseHelper;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ViewListContents extends AppCompatActivity {

    private TableLayout tableLayout;
    private List<MyModel> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewlistcontents_layout);

        // Initialize data list
        dataList = getDataList();

        // Get TableLayout reference
        tableLayout = findViewById(R.id.tablelayout);

        // Populate table view with data
        populateTable();
    }
    private void populateTable() {
        for (MyModel mymodel : dataList) {
            TableRow tableRow = new TableRow(tableLayout.getContext());
            tableRow.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tableRow.setBackgroundColor(Color.WHITE);
            tableRow.setGravity(Gravity.CENTER_HORIZONTAL);

            // Add TextView for "Id"
            TextView idTextView = new TextView(tableLayout.getContext());
            idTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            idTextView.setText(String.valueOf(mymodel.getId()));
            idTextView.setTextColor(Color.BLACK);
            idTextView.setTextSize(18);
            idTextView.setGravity(Gravity.CENTER);
            tableRow.addView(idTextView);

            // Add TextView for "Name"
            TextView nameTextView = new TextView(tableLayout.getContext());
            nameTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            nameTextView.setText(mymodel.getName());
            nameTextView.setTextColor(Color.BLACK);
            nameTextView.setTextSize(18);
            nameTextView.setGravity(Gravity.CENTER);
            tableRow.addView(nameTextView);

            // Add TextView for "Number"
            TextView phoneNumberTextView = new TextView(tableLayout.getContext());
            phoneNumberTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            phoneNumberTextView.setText(mymodel.getPhoneNo());
            phoneNumberTextView.setTextColor(Color.BLACK);
            phoneNumberTextView.setTextSize(18);
            phoneNumberTextView.setGravity(Gravity.CENTER);
            tableRow.addView(phoneNumberTextView);

            // Add TableRow to TableLayout
            tableLayout.addView(tableRow);

        }
    }

    private List<MyModel> getDataList() {

        DatabaseHelper db=new DatabaseHelper(this);
        dataList = db.getAllData();
        return dataList;
    }
}

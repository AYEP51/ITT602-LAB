package com.example.electricitybillestimator_arif;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    ListView listViewBills;
    DatabaseHelper databaseHelper;

    ArrayList<String> billList;
    ArrayList<Integer> idList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Saved Bills");
        setContentView(R.layout.activity_list);

        listViewBills = findViewById(R.id.listViewBills);
        databaseHelper = new DatabaseHelper(this);

        loadData();

        listViewBills.setOnItemClickListener((parent, view, position, id) -> {
            if (idList.size() == 0) {
                Toast.makeText(this, "No record selected", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(ListActivity.this, DetailActivity.class);
            intent.putExtra("bill_id", idList.get(position));
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        billList = new ArrayList<>();
        idList = new ArrayList<>();

        Cursor cursor = databaseHelper.getAllData();

        if (cursor.getCount() == 0) {
            billList.add("No saved bill yet");
        } else {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String month = cursor.getString(1);
                double finalCost = cursor.getDouble(5);

                idList.add(id);
                billList.add(month + " - RM " + String.format("%.2f", finalCost));
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                billList
        );

        listViewBills.setAdapter(adapter);
    }
}
package com.example.electricitybillestimator_arif;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    Spinner spinnerEditMonth;
    EditText etEditUnit, etEditRebate;
    TextView tvDetail;
    Button btnUpdate, btnDelete;

    DatabaseHelper databaseHelper;
    int billId;

    String[] months = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Bill Detail");
        setContentView(R.layout.activity_detail);

        spinnerEditMonth = findViewById(R.id.spinnerEditMonth);
        etEditUnit = findViewById(R.id.etEditUnit);
        etEditRebate = findViewById(R.id.etEditRebate);
        tvDetail = findViewById(R.id.tvDetail);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        databaseHelper = new DatabaseHelper(this);
        billId = getIntent().getIntExtra("bill_id", -1);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                months
        );
        spinnerEditMonth.setAdapter(adapter);

        loadDetail();

        btnUpdate.setOnClickListener(v -> updateRecord());

        btnDelete.setOnClickListener(v -> confirmDelete());
    }

    private void loadDetail() {
        Cursor cursor = databaseHelper.getDataById(billId);

        if (cursor.moveToFirst()) {
            String month = cursor.getString(1);
            int unit = cursor.getInt(2);
            double totalCharges = cursor.getDouble(3);
            double rebate = cursor.getDouble(4);
            double finalCost = cursor.getDouble(5);

            for (int i = 0; i < months.length; i++) {
                if (months[i].equals(month)) {
                    spinnerEditMonth.setSelection(i);
                    break;
                }
            }

            etEditUnit.setText(String.valueOf(unit));
            etEditRebate.setText(String.valueOf(rebate));

            tvDetail.setText(
                    "Month: " + month +
                            "\nUnit Used: " + unit + " kWh" +
                            "\nTotal Charges: RM " + String.format("%.2f", totalCharges) +
                            "\nRebate: " + String.format("%.2f", rebate) + "%" +
                            "\nFinal Cost: RM " + String.format("%.2f", finalCost)
            );
        } else {
            Toast.makeText(this, "Record not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateRecord() {
        String month = spinnerEditMonth.getSelectedItem().toString();
        String unitText = etEditUnit.getText().toString().trim();
        String rebateText = etEditRebate.getText().toString().trim();

        if (unitText.isEmpty()) {
            etEditUnit.setError("Please enter electricity unit");
            etEditUnit.requestFocus();
            return;
        }

        if (rebateText.isEmpty()) {
            etEditRebate.setError("Please enter rebate percentage");
            etEditRebate.requestFocus();
            return;
        }

        int unit = Integer.parseInt(unitText);
        double rebate = Double.parseDouble(rebateText);

        if (unit < 1 || unit > 1000) {
            etEditUnit.setError("Unit must be between 1 and 1000 kWh");
            etEditUnit.requestFocus();
            return;
        }

        if (rebate < 0 || rebate > 5) {
            etEditRebate.setError("Rebate must be between 0% and 5%");
            etEditRebate.requestFocus();
            return;
        }

        double totalCharges = MainActivity.calculateCharges(unit);
        double finalCost = totalCharges - (totalCharges * rebate / 100);

        boolean isUpdated = databaseHelper.updateData(
                billId,
                month,
                unit,
                totalCharges,
                rebate,
                finalCost
        );

        if (isUpdated) {
            Toast.makeText(this, "Record updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Record");
        builder.setMessage("Are you sure you want to delete this bill record?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            boolean isDeleted = databaseHelper.deleteData(billId);

            if (isDeleted) {
                Toast.makeText(this, "Record deleted", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("No", null);
        builder.show();
    }
}
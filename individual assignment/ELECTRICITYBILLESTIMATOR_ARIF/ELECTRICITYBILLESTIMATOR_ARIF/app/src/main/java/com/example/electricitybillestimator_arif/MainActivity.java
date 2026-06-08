package com.example.electricitybillestimator_arif;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerMonth;
    EditText etUnit, etRebate;
    TextView tvResult;
    Button btnCalculate, btnViewList, btnAbout;
    DatabaseHelper databaseHelper;

    String[] months = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Electricity Bill");
        setContentView(R.layout.activity_main);

        spinnerMonth = findViewById(R.id.spinnerMonth);
        etUnit = findViewById(R.id.etUnit);
        etRebate = findViewById(R.id.etRebate);
        tvResult = findViewById(R.id.tvResult);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnViewList = findViewById(R.id.btnViewList);
        btnAbout = findViewById(R.id.btnAbout);

        databaseHelper = new DatabaseHelper(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                months
        );
        spinnerMonth.setAdapter(adapter);

        btnCalculate.setOnClickListener(v -> calculateAndSave());

        btnViewList.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            startActivity(intent);
        });

        btnAbout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });
    }

    private void calculateAndSave() {
        String month = spinnerMonth.getSelectedItem().toString();
        String unitText = etUnit.getText().toString().trim();
        String rebateText = etRebate.getText().toString().trim();

        if (unitText.isEmpty()) {
            etUnit.setError("Please enter electricity unit");
            etUnit.requestFocus();
            return;
        }

        if (rebateText.isEmpty()) {
            etRebate.setError("Please enter rebate percentage");
            etRebate.requestFocus();
            return;
        }

        int unit = Integer.parseInt(unitText);
        double rebate = Double.parseDouble(rebateText);

        if (unit < 1 || unit > 1000) {
            etUnit.setError("Unit must be between 1 and 1000 kWh");
            etUnit.requestFocus();
            return;
        }

        if (rebate < 0 || rebate > 5) {
            etRebate.setError("Rebate must be between 0% and 5%");
            etRebate.requestFocus();
            return;
        }

        double totalCharges = calculateCharges(unit);
        double finalCost = totalCharges - (totalCharges * rebate / 100);

        boolean isInserted = databaseHelper.insertData(month, unit, totalCharges, rebate, finalCost);

        if (isInserted) {
            tvResult.setText(
                    "Month: " + month +
                            "\nUnit Used: " + unit + " kWh" +
                            "\nTotal Charges: RM " + String.format("%.2f", totalCharges) +
                            "\nRebate: " + String.format("%.2f", rebate) + "%" +
                            "\nFinal Cost: RM " + String.format("%.2f", finalCost)
            );

            Toast.makeText(this, "Calculation saved successfully", Toast.LENGTH_SHORT).show();

            etUnit.setText("");
            etRebate.setText("");
        } else {
            Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show();
        }
    }

    public static double calculateCharges(int unit) {
        double total = 0;

        if (unit <= 200) {
            total = unit * 0.218;
        } else if (unit <= 300) {
            total = (200 * 0.218) + ((unit - 200) * 0.334);
        } else if (unit <= 600) {
            total = (200 * 0.218) + (100 * 0.334) + ((unit - 300) * 0.516);
        } else {
            total = (200 * 0.218) + (100 * 0.334) + (300 * 0.516) + ((unit - 600) * 0.546);
        }

        return total;
    }
}
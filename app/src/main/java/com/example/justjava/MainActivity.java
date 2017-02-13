package com.example.justjava;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Integer numberOfCoffees = 0;
    private int total = 0;
    private final String address[] = {"n0u7had7@gmail.com"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void submitOrder(View view) {

        int additionalPrice = 0;
        TextView numberOfCoffeesTextView = (TextView) findViewById(R.id.quantityNumeric);
        String coffees = (String) numberOfCoffeesTextView.getText();
        Integer coffeesNumber = Integer.parseInt(coffees);

        String topping = getString(R.string.none);

        EditText nameString = (EditText) findViewById(R.id.nameEdit);
        String name = nameString.getText().toString();

        EditText postalAddress = (EditText) findViewById(R.id.addressBar);
        String address = postalAddress.getText().toString();


        if (name.isEmpty() || address.isEmpty()) {
            Context context = getApplicationContext();
            CharSequence text = getString(R.string.toast_message);
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }
        CheckBox cream = (CheckBox) findViewById(R.id.checkBox);
        if (cream.isChecked()) {
            topping = getString(R.string.cream_topping);
            additionalPrice += 1;
        }
        CheckBox chocolate = (CheckBox) findViewById(R.id.checkBox2);
        if (chocolate.isChecked()) {
            additionalPrice += 2;
            if (cream.isChecked()) {
                topping += ",";
                topping += getString(R.string.chocolate_topping);
            } else
                topping = getString(R.string.chocolate_topping);
        }

        int coffeePrice = 3;
        total = coffeePrice + additionalPrice;

        displayPriceInfo(coffeesNumber, name, topping, total, address);

    }


    private void displayQuantity(int quantity) {
        TextView numberOfCoffeesTextView = (TextView) findViewById(R.id.quantityNumeric);
        numberOfCoffeesTextView.setText(String.valueOf(quantity));

    }


    private void displayPriceInfo(int coffeesNumber, String name, String topping, int coffeePrice, String address) {

        String orderSummury = createOrderUmmmary(coffeesNumber, name, topping, coffeePrice, address);

        show(coffeePrice, coffeesNumber);
        sendMail(orderSummury);
    }

    private void sendMail(String orderSummury) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, address);
        String subject = getString(R.string.email_subject);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, orderSummury);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void show(int price, int quantity) {
        Integer total = price * quantity;
        TextView priceTextView = (TextView) findViewById(R.id.priceNumeric);
        String display = getString(R.string.currency) + String.valueOf(total);
        priceTextView.setText(display);

    }


    public void increment(View view) {
        if (numberOfCoffees < 100) {
            numberOfCoffees++;
            displayQuantity(numberOfCoffees);
        } else {
            Context context = getApplicationContext();
            CharSequence text = getString(R.string.upper_limit);
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    public void decrement(View view) {
        if (numberOfCoffees > 1) {
            numberOfCoffees--;
            displayQuantity(numberOfCoffees);
        } else {
            Context context = getApplicationContext();
            CharSequence text =getString(R.string.lower_limit);
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    public String createOrderUmmmary(int quanitity, String name, String topping, int coffeePrice, String address) {

        String message;

        int price = coffeePrice * quanitity;

        message = getString(R.string.customer_name)+" : " + name + "\n" +
                getString(R.string.customer_address)+" : " + address + "\n" +
                getString(R.string.quantity_field)+": " + quanitity + "\n" +
                getString(R.string.topping)+" : " + topping + "\n" +
                getString(R.string.total_field)+" : "+getString(R.string.currency) + price + "\n" +
                "Thank You.";
        return message;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("quantity", numberOfCoffees);
        outState.putInt("price", total);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        numberOfCoffees = savedInstanceState.getInt("quantity");
        displayQuantity(numberOfCoffees);
        total = savedInstanceState.getInt("price");

        show(total, numberOfCoffees);

    }

    public void makeCalls(View view) {
        String phone_number = "+8801763463287";

        String uri = "tel:" + phone_number.trim();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }
}

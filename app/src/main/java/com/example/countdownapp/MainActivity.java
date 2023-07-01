package com.example.countdownapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    private EditText amountEditText;
    private Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amountEditText = findViewById(R.id.amountEditText);
        submitBtn = findViewById(R.id.submitBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = amountEditText.getText().toString();
                callApi(amount);
            }
        });
    }

    private void callApi(String amount) {
        // Create the API URL with the given amount
        String apiUrl = "https://devapi.meerolink.com/test-pay?amount=" + amount;

        // Execute the API call in an AsyncTask
        new APICallTask().execute(apiUrl);
    }

    private class APICallTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    String qrString = convertStreamToString(inputStream);
                    inputStream.close();
                    return qrString;
                }
            } catch (Exception e) {
                Log.e("APICallTask", "Error: " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String qrString) {
            if (qrString != null) {
                // Start the second activity and pass the QR string
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("qrString", qrString);
                startActivity(intent);
            }
        }

        private String convertStreamToString(InputStream inputStream) {
            java.util.Scanner scanner = new java.util.Scanner(inputStream).useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }
}

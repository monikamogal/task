package com.example.countdownapp;//package com.example.countdownapp;
//
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.WriterException;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.qrcode.QRCodeWriter;
//
//import java.util.concurrent.TimeUnit;
//
//public class SecondActivity extends AppCompatActivity {
//
//    private ImageView qrcodeImageView;
//    private TextView timerTextView;
//    private CountDownTimer countDownTimer;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_second);
//
//        qrcodeImageView = findViewById(R.id.qrcodeImageView);
//        timerTextView = findViewById(R.id.timerTextView);
//
//        // Get the QR string from the intent
//        String qrString = getIntent().getStringExtra("qrString");
//
//        // Generate the QR code image and display it
//        Bitmap qrCodeBitmap = generateQRCode(qrString);
//        qrcodeImageView.setImageBitmap(qrCodeBitmap);
//
//        // Start the countdown timer for 3 minutes
//        startCountdownTimer(3 * 60 * 1000);
//    }
//
//    private Bitmap generateQRCode(String qrString) {
//        MultiFormatWriter writer = new MultiFormatWriter();
//        try {
//            BitMatrix bitMatrix = writer.encode(qrString, BarcodeFormat.QR_CODE, 400, 400);
//            int width = bitMatrix.getWidth();
//            int height = bitMatrix.getHeight();
//            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//            for (int x = 0; x < width; x++) {
//                for (int y = 0; y < height; y++) {
//                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
//                }
//            }
//            return bitmap;
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
//    private void startCountdownTimer(long durationMillis) {
//        countDownTimer = new CountDownTimer(durationMillis, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
//                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
//                        TimeUnit.MINUTES.toSeconds(minutes);
//                String timeFormatted = String.format("%02d:%02d", minutes, seconds);
//                timerTextView.setText(timeFormatted);
//            }
//
//            @Override
//            public void onFinish() {
//                timerTextView.setText("Time's up!");
//            }
//        }.start();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (countDownTimer != null) {
//            countDownTimer.cancel();
//        }
//    }
//}
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class SecondActivity extends AppCompatActivity {

    private ImageView qrCodeImageView;
    private TextView countdownTextView;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        qrCodeImageView = findViewById(R.id.qrcodeImageView);
        countdownTextView = findViewById(R.id.timerTextView);

        String qrString = getIntent().getStringExtra("qrString");
        generateQRCode(qrString);

        startCountdownTimer();
    }

    private void generateQRCode(String qrString) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(qrString, BarcodeFormat.QR_CODE, 200, 200);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            qrCodeImageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void startCountdownTimer() {
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished / 1000) % 60;
                String timeLeft = String.format("%02d:%02d", minutes, seconds);
                countdownTextView.setText(timeLeft);
            }

            @Override
            public void onFinish() {
                qrCodeImageView.setImageBitmap(null);
                countdownTextView.setText("Time's up!");
            }
        };

        countDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}

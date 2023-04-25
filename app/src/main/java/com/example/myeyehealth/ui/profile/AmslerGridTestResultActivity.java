package com.example.myeyehealth.ui.profile;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.example.myeyehealth.R;
import com.example.myeyehealth.controller.AmslerGridMethods;
import com.example.myeyehealth.utils.SessionActivity;
import com.example.myeyehealth.utils.SessionManager;
import com.example.myeyehealth.model.User;
import com.example.myeyehealth.view.AmslerGridPlotView;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.widget.Toast;


import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class AmslerGridTestResultActivity extends SessionActivity {
    public static final String TEST_ID_KEY = "test_id";
    public static final String TEST_DATE_KEY = "test_date";

    private static final int GRID_LINES = 40;
    private static final int REQUEST_WRITE_STORAGE = 112;
    private static final int A4_WIDTH = 595;
    private static final int A4_HEIGHT = 842;

    private HashMap<String, ArrayList<Float>> leftEyeCoordinates;
    private HashMap<String, ArrayList<Float>> rightEyeCoordinates;
    private int leftEyeGridSize;
    private int rightEyeGridSize;
    private String testDate;
    private ImageButton backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amsler_grid_test_result);
        AmslerGridPlotView leftEyeAmslerGridPlotView = findViewById(R.id.leftEyeAmslerGridPlotView);
        AmslerGridPlotView rightEyeAmslerGridPlotView = findViewById(R.id.rightEyeAmslerGridPlotView);

        testDate = getIntent().getStringExtra(TEST_DATE_KEY); // Move this line to the onCreate method


        // Set the test date as the text of the results_subtitle TextView
        TextView resultsSubtitle = findViewById(R.id.results_subtitle);
        resultsSubtitle.setText(testDate);


        String testId = getIntent().getStringExtra(TEST_ID_KEY);

        SessionManager sessionManager = SessionManager.getInstance(this);
        User user = sessionManager.getUser();
        int currentUserId = user.getId();
        leftEyeGridSize = getIntent().getIntExtra("leftEyeGridSize", 0);
        rightEyeGridSize = getIntent().getIntExtra("rightEyeGridSize", 0);

        AmslerGridMethods amslerGridMethods = new AmslerGridMethods(this);
        HashMap<String, ArrayList<Float>>[] coordinates = amslerGridMethods.getAmslerGridCoordinatesForTest(currentUserId, Integer.parseInt(testId));
        int[] gridSizes = amslerGridMethods.getGridSizesForTest(currentUserId, Integer.parseInt(testId));
        leftEyeGridSize = gridSizes[0];
        rightEyeGridSize = gridSizes[1];

// Remove normalization here
        leftEyeCoordinates = coordinates[0];
        rightEyeCoordinates = coordinates[1];


        leftEyeAmslerGridPlotView.setOriginalGridSize(leftEyeGridSize);
        rightEyeAmslerGridPlotView.setOriginalGridSize(rightEyeGridSize);

        leftEyeAmslerGridPlotView.setCoordinates(leftEyeCoordinates);
        rightEyeAmslerGridPlotView.setCoordinates(rightEyeCoordinates);

        Button exportButton = findViewById(R.id.export_button);
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportAmslerGridDataToPdf();
            }
        });
        backButton = findViewById(R.id.back_button); // Retrieve the back button

        // Set the OnClickListener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Call the onBackPressed() method
            }
        });
    }


    @Override
    protected void onLoggedIn(User user) {
    }
    private void exportAmslerGridDataToPdf() {
        AmslerGridPlotView leftEyeAmslerGridPlotView = findViewById(R.id.leftEyeAmslerGridPlotView);
        AmslerGridPlotView rightEyeAmslerGridPlotView = findViewById(R.id.rightEyeAmslerGridPlotView);

        Bitmap leftEyeBitmap = Bitmap.createBitmap(leftEyeAmslerGridPlotView.getWidth(), leftEyeAmslerGridPlotView.getHeight(), Bitmap.Config.ARGB_8888);
        Bitmap rightEyeBitmap = Bitmap.createBitmap(rightEyeAmslerGridPlotView.getWidth(), rightEyeAmslerGridPlotView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas leftEyeCanvas = new Canvas(leftEyeBitmap);
        Canvas rightEyeCanvas = new Canvas(rightEyeBitmap);
        leftEyeAmslerGridPlotView.draw(leftEyeCanvas);
        rightEyeAmslerGridPlotView.draw(rightEyeCanvas);

        // Create PDF file
        File outputDir = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "AmslerGridData");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        File outputFile = new File(outputDir, "Amsler_Grid_Data_" + testDate + ".pdf");
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(A4_WIDTH, A4_HEIGHT, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);

        // 1. Add spacing between the grids
        int spacing = 50;
        int gridWidth = 250;
        int gridHeight = 250;

        // Calculate the positions of each Amsler grid
        int leftGridX = (A4_WIDTH - 2 * gridWidth - spacing) / 2;
        int rightGridX = leftGridX + gridWidth + spacing;

        // Draw the left Amsler grid
        canvas.drawBitmap(leftEyeBitmap, null, new Rect(leftGridX, 0, leftGridX + gridWidth, gridHeight), paint);

        // Draw the right Amsler grid
        canvas.drawBitmap(rightEyeBitmap, null, new Rect(rightGridX, 0, rightGridX + gridWidth, gridHeight), paint);

        // Add labels above the grids
        paint.setTextSize(24);
        canvas.drawText("Left Eye", leftGridX + gridWidth / 2 - 40, gridHeight + 30, paint);
        canvas.drawText("Right Eye", rightGridX + gridWidth / 2 - 40, gridHeight + 30, paint);

        // Add the test date on the PDF
        paint.setTextSize(18);
        canvas.drawText("Test Date: " + testDate, A4_WIDTH / 2 - 80, A4_HEIGHT - 40, paint);

        pdfDocument.finishPage(page);



        try {
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            pdfDocument.writeTo(outputStream);
            outputStream.flush();
            outputStream.close();

            Toast.makeText(this, "PDF exported to " + outputFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error exporting PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            pdfDocument.close();
        }
// Open the PDF file
        Uri outputFileUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", outputFile);
        Intent openPdfIntent = new Intent(Intent.ACTION_VIEW);
        openPdfIntent.setDataAndType(outputFileUri, "application/pdf");
        openPdfIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        openPdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(openPdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No PDF viewer app found. Install one to view the exported PDF.", Toast.LENGTH_LONG).show();
        }
// Create an image with the same structure as the PDF page
        Bitmap pageImage = Bitmap.createBitmap(A4_WIDTH, A4_HEIGHT, Bitmap.Config.ARGB_8888);
        Canvas pageCanvas = new Canvas(pageImage);
        pageCanvas.drawColor(Color.WHITE); // Set white background
        pageCanvas.drawBitmap(leftEyeBitmap, null, new Rect(leftGridX, 0, leftGridX + gridWidth, gridHeight), paint);
        pageCanvas.drawBitmap(rightEyeBitmap, null, new Rect(rightGridX, 0, rightGridX + gridWidth, gridHeight), paint);

// Save the image to the gallery
        String savedImagePath = saveImageToGallery(pageImage);

// Open the image in the gallery app
        if (savedImagePath != null) {
            openImageInGallery(savedImagePath);
        } else {
            Toast.makeText(this, "Error saving image to gallery.", Toast.LENGTH_LONG).show();
        }
    }

    private String saveImageToGallery(Bitmap image) {
        String fileName = "Amsler_Grid_Data_" + testDate + ".jpg";
        OutputStream outputStream;
        String savedImagePath = null;

        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDirectory, fileName);
        savedImagePath = imageFile.getAbsolutePath();

        try {
            outputStream = new FileOutputStream(imageFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // Add the image to the system gallery
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(savedImagePath);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);

        return savedImagePath;
    }

    private void openImageInGallery(String imagePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File imageFile = new File(imagePath);
        Uri contentUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", imageFile);

        intent.setDataAndType(contentUri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No gallery app found to view the image.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Storage permission granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Unable to export PDF without storage permission", Toast.LENGTH_LONG).show();
        }
    }

}

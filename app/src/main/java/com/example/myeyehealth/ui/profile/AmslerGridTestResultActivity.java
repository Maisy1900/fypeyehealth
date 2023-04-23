package com.example.myeyehealth.ui.profile;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.AmslerGridMethods;
import com.example.myeyehealth.data.SessionActivity;
import com.example.myeyehealth.data.SessionManager;
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
    private String testDate; // Declare testDate as an instance variable

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
        Log.d("AmslerTestResult", "Test ID: " + testId);
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

        Log.d("AmslerTestResult", "Left Eye Coordinates: " + leftEyeCoordinates);
        Log.d("AmslerTestResult", "Right Eye Coordinates: " + rightEyeCoordinates);
        Button exportButton = findViewById(R.id.export_button);
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportAmslerGridDataToPdf();
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

        // Create PDF Document
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(A4_WIDTH, A4_HEIGHT, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawBitmap(leftEyeBitmap, null, new Rect(0, 0, A4_WIDTH / 2, A4_HEIGHT), paint);
        canvas.drawBitmap(rightEyeBitmap, null, new Rect(A4_WIDTH / 2, 0, A4_WIDTH, A4_HEIGHT), paint);
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


    }



    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
            }
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

package com.example.myeyehealth.view;

import android.content.Context;
import android.util.AttributeSet;
/**
 * A custom TextView for displaying the results of an Amsler Grid test.
 * Use the setResultsText() method to update the text displayed in this view.
 * The text should be gathered from a database or other source where the test results are stored.
 */
public class AmslerResultsTextView extends androidx.appcompat.widget.AppCompatTextView {

    public AmslerResultsTextView(Context context) {
        super(context);
        init();
    }

    public AmslerResultsTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AmslerResultsTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        // Set any custom attributes or initialization code here
    }

    public void setResultsText(String resultsText) {
        setText(resultsText);
    }

    // You can add any other custom methods or attributes here as needed

}
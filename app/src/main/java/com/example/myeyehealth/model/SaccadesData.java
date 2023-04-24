package com.example.myeyehealth.model;
import java.util.ArrayList;
import java.util.Collections;

public class SaccadesData {
    private ArrayList<Integer> exerciseNumbers;
    private ArrayList<Float> completionTimes;

    public SaccadesData(ArrayList<Integer> exerciseNumbers, ArrayList<Float> completionTimes) {
        this.exerciseNumbers = exerciseNumbers;
        this.completionTimes = completionTimes;
    }

    public ArrayList<Integer> getExerciseNumbers() {
        return exerciseNumbers;
    }

    public ArrayList<Float> getCompletionTimes() {
        return completionTimes;
    }

}


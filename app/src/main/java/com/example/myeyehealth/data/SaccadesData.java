package com.example.myeyehealth.data;
import java.util.ArrayList;

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


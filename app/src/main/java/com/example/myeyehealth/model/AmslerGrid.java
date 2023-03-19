package com.example.myeyehealth.model;

/**
 * The AmslerGrid class represents an Amsler grid that can be used to test for visual distortion or other vision problems.
 * It stores information about the grid, including its size and the results of any tests performed on it.
 */
public class AmslerGrid {

    // Fields
    private int size; // The size of the grid (in pixels)
    private int[][] data; // The data representing the grid (2D array of integers)

    /**
     * Creates a new AmslerGrid object with the specified size.
     * @param size the size of the grid (in pixels)
     */
    public AmslerGrid(int size) {
        this.size = size;
        data = new int[size][size];
    }

    /**
     * Returns the size of the grid.
     * @return the size of the grid (in pixels)
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns the data representing the grid.
     * @return a 2D array of integers representing the grid
     */
    public int[][] getData() {
        return data;
    }

    /**
     * Sets the data representing the grid.
     * @param data a 2D array of integers representing the grid
     */
    public void setData(int[][] data) {
        this.data = data;
    }

    /**
     * Returns the value at the specified row and column in the grid.
     * @param row the row of the cell to retrieve
     * @param col the column of the cell to retrieve
     * @return the value at the specified row and column
     */
    public int getValueAt(int row, int col) {
        return data[row][col];
    }

    /**
     * Sets the value at the specified row and column in the grid.
     * @param row the row of the cell to set
     * @param col the column of the cell to set
     * @param value the value to set at the specified row and column
     */
    public void setValueAt(int row, int col, int value) {
        data[row][col] = value;
    }

    // You can add other methods as needed to manipulate the grid or perform tests on it

}

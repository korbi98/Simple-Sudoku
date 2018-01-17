package com.korbi.simplesudoku.logic;

import android.content.Context;
import android.util.Log;

import com.korbi.simplesudoku.R;
import com.korbi.simplesudoku.activities.GameActivity;
import com.korbi.simplesudoku.sudokuviews.SudokuCellView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by korbi on 7/21/17.
 */

public class SudokuGrid {
    private static int currentPosition = 0;
    private SudokuLogic sHelper = new SudokuLogic(this);
    private static List<Integer> faultyRows = new ArrayList<>();
    private static List<Integer> faultyColumns = new ArrayList<>();
    private static List<Integer> faultySquares = new ArrayList<>();


    private SudokuCellView[][] sudoku = new SudokuCellView[9][9];
    private Context context;

    public SudokuGrid(Context context){
        this.context = context;

        for( int y = 0; y < 9; y++){
            for( int x = 0; x < 9; x++){
                sudoku[x][y] = new SudokuCellView(context);
                sudoku[x][y].setIsPreSet(true);
                sudoku[x][y].setValue(0);
                sudoku[x][y].setBackgroundResource(R.color.cellPreset);
            }
        }
    }

    public SudokuCellView getItem(int x , int y ){
        return sudoku[x][y];
    }

    public SudokuCellView getItem( int position ){
        int x = position % 9;
        int y = position / 9;

        return sudoku[x][y];
    }

    public void clearGrid(){
        for (int i = 0; i < 81; i++){
            if (!getItem(i).isPreSet())
                getItem(i).setValue(0);
        }
    }

    public void clearCompleteGrid(){
        for (int i = 0; i < 81; i++){
            getItem(i).setValue(0);
        }
    }

    public String getGridString(){
        String gridString = "";

        for (int i = 0; i < 81; i++){
            gridString += getItem(i).getValue();
        }
        for (int i = 0; i < 81; i++){
            gridString += (getItem(i).isPreSet()) ? 1 : 0;
        }

        return gridString;
    }

    public void loadGridString(String savedGrid){

        for (int i = 0; i < 81; i++){
            Log.d("load", String.valueOf(savedGrid.charAt(i)));
            getItem(i).setValue(Character.getNumericValue(savedGrid.charAt(i)));
        }
        for (int i = 81; i < 162; i++){
            getItem(i - 81).setIsPreSet(
                    (Character.getNumericValue(savedGrid.charAt(i)) == 1));
        }

        for (int i = 0; i < 81; i++ ){
            if (!getItem(i).isPreSet()){
                getItem(i).setBackgroundResource(R.color.cellNotPreset);
            }
        }
    }

    public void highlightCells(int position){

        currentPosition = position;

        if (!getCurrentPosition().isPreSet()){

            for (int i = 0; i < 81; i++){
                if (getItem(i).isPreSet()) getItem(i).setBackgroundResource(R.color.cellPreset);
                else getItem(i).setBackgroundResource(R.color.cellNotPreset);
            }

            if(GameActivity.highlightCellPreference){
                for (int i : sHelper.getRowPositions(getCurrentRow())) {
                    getItem(i).setBackgroundResource(R.color.cellSlightHighlight);
                }
                for (int i : sHelper.getColumnPositions(getCurrentColumn())) {
                    getItem(i).setBackgroundResource(R.color.cellSlightHighlight);
                }
                for (int i : sHelper.getSquarePositions(getCurrentSquare())) {
                    getItem(i).setBackgroundResource(R.color.cellSlightHighlight);
                }
            }

            highlightFaultyCells();
            getCurrentPosition().setBackgroundResource(R.color.cellHighlight);
        }

    }

    public void highlightFaultyCells(){

        if (GameActivity.showErrorPreference){
            updateFaultyLists();

            for (int i : faultyColumns){
                for (int j : sHelper.getColumnPositions(i)){
                    getItem(j).setBackgroundResource(R.color.cellFaulty);
                }
            }

            for (int i : faultyRows){
                for (int j : sHelper.getRowPositions(i)){
                    getItem(j).setBackgroundResource(R.color.cellFaulty);
                }
            }

            for (int i : faultySquares){
                for (int j : sHelper.getSquarePositions(i)){
                    getItem(j).setBackgroundResource(R.color.cellFaulty);
                }
            }
            getCurrentPosition().setBackgroundResource(R.color.cellHighlight);
        }
    }

    public SudokuCellView getCurrentPosition() { return getItem(currentPosition); }

    private int getCurrentColumn(){
        return sHelper.getColumn(currentPosition);
    }
    private int getCurrentRow(){
        return sHelper.getRow(currentPosition);
    }
    private int getCurrentSquare(){
        return sHelper.getSquare(currentPosition);
    }

    private void updateFaultyLists(){
        boolean faultyColumn = !sHelper.checkColumn(getCurrentColumn());
        boolean faultyColumnAlreadyInList = faultyColumns.contains(getCurrentColumn());
        boolean faultyRow = !sHelper.checkRow(getCurrentRow());
        boolean faultyRowAlreadyInList = faultyRows.contains(getCurrentRow());
        boolean faultySquare = !sHelper.checkSquare(getCurrentSquare());
        boolean faultySquareAlreadyInList = faultySquares.contains(getCurrentSquare());

        if (faultyColumn && !faultyColumnAlreadyInList){
            faultyColumns.add(getCurrentColumn());
        }
        else if (!faultyColumn && faultyColumnAlreadyInList){
            faultyColumns.remove((Integer)getCurrentColumn());
            for (int i : sHelper.getColumnPositions(getCurrentColumn())) {
                getItem(i).setBackgroundResource(R.color.cellSlightHighlight);
            }
            getCurrentPosition().setBackgroundResource(R.color.cellHighlight);
        }

        if (faultyRow && !faultyRowAlreadyInList){
            faultyRows.add(getCurrentRow());
        }
        else if (!faultyRow && faultyRowAlreadyInList){
            faultyRows.remove((Integer)getCurrentRow());
            for (int i : sHelper.getRowPositions(getCurrentRow())) {
                getItem(i).setBackgroundResource(R.color.cellSlightHighlight);
            }
            getCurrentPosition().setBackgroundResource(R.color.cellHighlight);
        }

        if (faultySquare && !faultySquareAlreadyInList){
            faultySquares.add(getCurrentSquare());
        }
        else if (!faultySquare & faultySquareAlreadyInList){
            faultySquares.remove((Integer)getCurrentSquare());
            for (int i : sHelper.getSquarePositions(getCurrentSquare())) {
                getItem(i).setBackgroundResource(R.color.cellSlightHighlight);
            }
            getCurrentPosition().setBackgroundResource(R.color.cellHighlight);
        }
    }

    public void clearFaultyLists(){
        faultyColumns.clear();
        faultyRows.clear();
        faultySquares.clear();
    }

    public void initializeHighlighting(){
        clearFaultyLists();

        for(int i = 0; i < 9; i++){
            if (!sHelper.checkRow(i)){
                faultyRows.add(i);
            }
            if (!sHelper.checkColumn(i)){
                faultyColumns.add(i);
            }
            if (!sHelper.checkSquare(i)){
                faultySquares.add(i);
            }
        }
        highlightCells(currentPosition);
    }
}

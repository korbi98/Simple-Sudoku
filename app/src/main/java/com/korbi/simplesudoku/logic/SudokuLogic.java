package com.korbi.simplesudoku.logic;

import android.support.v4.app.INotificationSideChannel;
import android.util.IntProperty;
import android.util.Log;
import android.widget.Toast;

import com.korbi.simplesudoku.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;


/**
 * Created by korbi on 7/26/17.
 */

public class SudokuLogic
{
    SudokuGrid grid;
    private Random random;
    public static final int DIFFICULTY_CONSTANT = 25;

    public SudokuLogic(SudokuGrid grid){
        this.grid = grid;
        random = new Random();
    }

    public SudokuGrid generateSudoku(SudokuGrid grid) //TODO make getRowPositions etc fixed Lists, so that they dont have to be generated every time
    {
        ArrayList<ArrayList<Integer>> available = createCandidates();

        for(int i = 0; i < 81; i++)
        {
            if (available.get(i).isEmpty()) {
                available = createCandidates();
                i = 0;
                Log.d("fail", "creation failed");
            }

            int index = random.nextInt(available.get(i).size());
            int new_number = available.get(i).get(index);

            grid.getItem(i).setValue(new_number);


            for (int position : getRowPositions(getRow(i)))
            {
                if (available.get(position).contains(new_number)){
                    available.get(position).remove((Object)new_number);}
            }

            for (int position : getColumnPositions(getColumn(i)))
            {
                if (available.get(position).contains(new_number)){
                    available.get(position).remove((Object)new_number);}
            }

            for (int position : getSquarePositions(getSquare(i)))
            {
                if (available.get(position).contains(new_number)){
                    available.get(position).remove((Object)new_number);}
            }
        }
        return grid;
    }

    public SudokuGrid clearCells(SudokuGrid grid, int difficulty)
    {
        List<Integer> positions = new ArrayList<>();
        for(int i = 0; i < 81; i++) positions.add(i);
        int position;
        int cellsToRemove = DIFFICULTY_CONSTANT + 3 * difficulty;


        for(int i = cellsToRemove;i > 0; i--)
        {
            position = random.nextInt(positions.size());
            grid.getItem(positions.get(position)).setValue(0);
            grid.getItem(positions.get(position)).setIsPreSet(false);
            grid.getItem(positions.get(position)).setBackgroundResource(R.color.cellNotPreset);

            positions.remove(position);
        }
        return grid;
    }

    public boolean checkIfFilled(SudokuGrid data)
    {
        for(int i = 0; i < 9; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                if (data.getItem(j, i).getValue() == 0)
                {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkColumn(int column)
    {
        List<Integer> numberToLockFor = new ArrayList<>();
        for(int i = 0; i < 9; i++)
        {
            if(grid.getItem(column, i).getValue() != 0 ) {
                if (numberToLockFor.contains(grid.getItem(column, i).getValue())) {
                    return false;
                }
            }
            numberToLockFor.add(grid.getItem(column, i).getValue());
        }

        return true;
    }

    public boolean checkRow(int row)
    {
        List<Integer> numberToLockFor = new ArrayList<>();
        for(int j = 0; j < 9; j++)
        {
            if(grid.getItem(j, row).getValue() != 0 ) {
                if (numberToLockFor.contains(grid.getItem(j, row).getValue())) {
                    return false;
                }
            }
            numberToLockFor.add(grid.getItem(j, row).getValue());
        }
        return true;
    }

    public boolean checkSquare(int square)
    {
        List<Integer> numberToLockFor = new ArrayList<>();

        for(int i : getSquarePositions(square)){

            if(grid.getItem(i).getValue() != 0 ){
                if (numberToLockFor.contains(grid.getItem(i).getValue())) {
                    return false;
                }
            }
            numberToLockFor.add(grid.getItem(i).getValue());
        }
        return true;
    }

    public boolean checkGame(){
        for (int i = 0; i < 9; i++){
            if (!checkColumn(i)) return false;
            else if (!checkRow(i)) return false;
            else if (!checkSquare(i)) return false;
        }
        return true;
    }

    public ArrayList<ArrayList<Integer>> createCandidates()
    {
        ArrayList<ArrayList<Integer>> candidates = new ArrayList<ArrayList<Integer>>();

        for (int i = 0; i<81; i++)
        {
            ArrayList<Integer> availableNumbers = new ArrayList<>();

            for (int j = 1; j < 10; j++)
            {
                availableNumbers.add(j);
            }
            candidates.add(availableNumbers);
        }

        return candidates;
    }

    public int getColumn(int position)
    {
        position = position % 9;
        return position;
    }

    public int getRow(int position)
    {
        position = position / 9;
        return position;
    }

    public int getSquare(int position)
    {
        int squareRow = (getRow(position)/3);
        int squareColumn = (getColumn(position)/3);

        return squareRow*3 + squareColumn;
    }

    public List<Integer> getRowPositions(int row)
    {
        List<Integer> positions = new ArrayList<>();

        for (int i = 0; i < 9; i++)
        {
            positions.add(row*9 + i);
        }

        return positions;
    }

    public List<Integer> getColumnPositions(int column)
    {
        List<Integer> positions = new ArrayList<>();

        for (int i = 0; i < 9; i++)
        {
            positions.add(column + i*9);
        }

        return positions;
    }

    public List<Integer> getSquarePositions(int square)
    {
        List<Integer> positions = new ArrayList<>();
        int squareRow = square / 3;
        int squareColumn = square % 3;

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++)
            {
                positions.add(squareRow*27 + y * 9 + squareColumn * 3 + x);
            }
        }
        return positions;
    }

    private List<Integer> getAvailableNumbers(int position){
        List<Integer> availableNumbers = new ArrayList<>();
        for (int i = 1; i < 10; i++){
            availableNumbers.add(i);
        }

        for (int i : getRowPositions(getRow(position))){
            if (availableNumbers.contains(grid.getItem(i).getValue())){
                availableNumbers.remove((Integer) grid.getItem(i).getValue());
            }
        }

        for (int i : getColumnPositions(getColumn(position))){
            if (availableNumbers.contains(grid.getItem(i).getValue())){
                availableNumbers.remove((Integer) grid.getItem(i).getValue());
            }
        }

        for (int i : getSquarePositions(getSquare(position))){
            if (availableNumbers.contains(grid.getItem(i).getValue())){
                availableNumbers.remove((Integer) grid.getItem(i).getValue());
            }
        }

        return availableNumbers;
    }

    private List<Integer> getEmptyCells(SudokuGrid grid){
        List<Integer> emptyCells = new ArrayList<>();
        for (int i = 0; i < 81; i++){
            if (grid.getItem(i).getValue() == 0) emptyCells.add(i);
        }
        return emptyCells;
    }

    public boolean solveSudoku(SudokuGrid grid){
        int position = getEmptyCells(grid).get(0);

        for (int i = 1; i < 10; i++){

        }

        return false;
    }

//    public int solveSudoku(SudokuGrid grid){ // checks if there is only one solution to the sodoku
//        List<List<Integer>> numbersTried = new ArrayList<>();
//        for (int i = 0; i < 81; i++) {
//            List<Integer> numbers = new ArrayList<>();
//            numbersTried.add(numbers);
//        }
//
//        List<Integer> emptyCells = getEmptyCells(grid);
//
//        for (int i = 0; i < emptyCells.size(); i++){
//
//            i = tryInsertNmber(i, emptyCells, grid);
//        }
//
//        return 0;
//    }
//
//    private int tryInsertNmber(int i, List<Integer> emptyCells, SudokuGrid grid){
//
//        if (getAvailableNumbers(emptyCells.get(i)).isEmpty()){
//
//            i = backTrack(i, emptyCells, grid);
//
//            Log.d("position", String.valueOf(emptyCells.get(i)));
//            Log.d("available", String.valueOf(getAvailableNumbers(emptyCells.get(i))));
//
//            if (getAvailableNumbers(emptyCells.get(i)).size() > 1){
//                List<Integer> available = getAvailableNumbers(emptyCells.get(i));
//                int index = random.nextInt(available.size());
//                grid.getItem(emptyCells.get(i)).setValue(available.get(index));
//
//            } else {
//                grid.getItem(emptyCells.get(i)).
//                        setValue(getAvailableNumbers(emptyCells.get(i)).get(0));
//            }
//
//            return i;
//        }
//
//        grid.getItem(emptyCells.get(i)).
//                setValue(getAvailableNumbers(emptyCells.get(i)).get(0));
//
//        return i;
//    }
//
//    private int backTrack(int i, List<Integer> emptyCells, SudokuGrid grid){
//        if (i > 0) i--;
//        grid.getItem(emptyCells.get(i)).setValue(0);
//        if (i > 0){
//            Log.d("i", String.valueOf(i));
//            if (getAvailableNumbers(emptyCells.get(i)).size() < 2) i = backTrack(i, emptyCells, grid);
//        }
//        return i;
//    }
}

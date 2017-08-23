package com.korbi.simplesudoku.logic;

import android.util.Log;

import com.korbi.simplesudoku.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by korbi on 7/26/17.
 */

public class SudokuLogic
{
    SudokuGrid grid;
    private Random random;

    public SudokuLogic(SudokuGrid grid){
        this.grid = grid;
        random = new Random();
    }

    public SudokuGrid generateSudoku(SudokuGrid grid)
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

        for(int i = difficulty;i > 0; i--)
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

    public boolean checkColumn(SudokuGrid data)
    {
        for(int i = 0; i < 9; i++)
        {
            List<Integer> numberToLockFor = new ArrayList<>();

            for(int j = 0; j < 9; j++)
            {
                if(data.getItem(i, j).getValue() != 0 ) {
                    if (numberToLockFor.contains(data.getItem(i, j).getValue())) {
                        return false;
                    }
                }
                numberToLockFor.add(data.getItem(i, j).getValue());
            }
        }
        return true;
    }

    public boolean checkRow(SudokuGrid data)
    {
        for(int i = 0; i < 9; i++)
        {
            List<Integer> numberToLockFor = new ArrayList<>();

            for(int j = 0; j < 9; j++)
            {
                if(data.getItem(j, i).getValue() != 0 ) {
                    if (numberToLockFor.contains(data.getItem(j, i).getValue())) {
                        return false;
                    }
                }
                numberToLockFor.add(data.getItem(j, i).getValue());
            }
        }
        return true;
    }

    public boolean checkSquare(SudokuGrid data)
    {
        for(int i = 0; i<3;i++){
            for(int j = 0; j<3;j++){

                List<Integer> numberToLockFor = new ArrayList<>();

                for(int k = 0; k<3;k++){
                    for(int l = 0; l<3;l++){

                        if(data.getItem(k + i * 3, l + j * 3).getValue() != 0 ){
                            if (numberToLockFor.contains(data.getItem(k + i * 3, l + j * 3).getValue())) {
                                return false;
                            }
                        }
                        numberToLockFor.add(data.getItem(k + i*3, l + j*3).getValue());

                    }
                }
            }
        }


        return true;
    }

    public boolean checkGame(SudokuGrid data){
        if (!checkIfFilled(data)) return false;
        if (!checkColumn(data)) return false;
        else if (!checkRow(data)) return false;
        else if (!checkSquare(data)) return false;
        else return true;
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

    private int SudokuSolver(SudokuGrid grid){ // checks if there is only one solution to the sodoku
        int firstNumberTried;

        return 0;
    }

}

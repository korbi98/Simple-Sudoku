/*
 * Copyright 2019 Korbinian Moser
 *
 * Licensed under the BSD 3-Clause License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.korbi.simplesudoku.logic;

import com.korbi.simplesudoku.sudokuviews.SudokuCellView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by korbi on 7/26/17.
 * This class handles the generation and validation of the sudokus
 */

public class SudokuLogic
{
    private SudokuGrid grid;
    private Random random;
    private static final int DIFFICULTY_CONSTANT = 28;

    public SudokuLogic(SudokuGrid grid){
        this.grid = grid;
        random = new Random();
    }

    //Takes empty Grid and generates a solved one
    public SudokuGrid generateSudoku(SudokuGrid grid) //TODO make getRowPositions etc fixed Lists, so that they don't have to be generated every time
    {
        ArrayList<ArrayList<Integer>> available = createCandidates();

        for(int i = 0; i < 81; i++)
        {
            if (available.get(i).isEmpty()) {
                available = createCandidates();
                i = 0;
            }

            int index = random.nextInt(available.get(i).size());
            int new_number = available.get(i).get(index);

            grid.getItem(i).setValue(new_number);


            for (int position : getRowPositions(getRow(i)))
            {
                if (available.get(position).contains(new_number)){
                    available.get(position).remove((Integer) new_number);}
            }

            for (int position : getColumnPositions(getColumn(i)))
            {
                if (available.get(position).contains(new_number)){
                    available.get(position).remove((Integer) new_number);}
            }

            for (int position : getSquarePositions(getSquare(i)))
            {
                if (available.get(position).contains(new_number)){
                    available.get(position).remove((Integer) new_number);}
            }
        }
        return grid;
    }

    public void clearCells(SudokuGrid grid, int difficulty)
    {
        List<Integer> positions = new ArrayList<>();
        for(int i = 0; i < 81; i++) positions.add(i);
        int position;
        int cellsToRemove = DIFFICULTY_CONSTANT + 3 * difficulty;
        int cellsRemoved = 0;

        while(cellsRemoved < cellsToRemove)
        {
            /*on high difficulties, when more than 50 cells get removed, not every sudoku has
            * a unique solution with that many empty cells, therefore we might have to generate
            * a new one and try again. On high difficulties removing the cells can take quite some
            * time because of trial and error*/
            if (positions.isEmpty()){
                grid.clearCompleteGrid();
                grid = generateSudoku(grid);
                for(int i = 0; i < 81; i++) positions.add(i);
            }

            position = random.nextInt(positions.size());
            int deletedValue = grid.getItem(positions.get(position)).getValue();
            grid.getItem(positions.get(position)).setValue(0);
            grid.getItem(positions.get(position)).setIsPreSet(false);

            if(isSolutionUnique()){
                cellsRemoved++;
            }
            else {
                grid.getItem(positions.get(position)).setValue(deletedValue);
                grid.getItem(positions.get(position)).setIsPreSet(true);
            }

            positions.remove(position);
        }
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

    public boolean checkColumn(int column, SudokuGrid sGrid)
    {
        List<Integer> numberToLockFor = new ArrayList<>();
        for(int i = 0; i < 9; i++)
        {
            if(sGrid.getItem(column, i).getValue() != 0 ) {
                if (numberToLockFor.contains(sGrid.getItem(column, i).getValue())) {
                    return false;
                }
            }
            numberToLockFor.add(sGrid.getItem(column, i).getValue());
        }

        return true;
    }

    public boolean checkRow(int row, SudokuGrid sGrid)
    {
        List<Integer> numberToLockFor = new ArrayList<>();
        for(int j = 0; j < 9; j++)
        {
            if(sGrid.getItem(j, row).getValue() != 0 ) {
                if (numberToLockFor.contains(sGrid.getItem(j, row).getValue())) {
                    return false;
                }
            }
            numberToLockFor.add(sGrid.getItem(j, row).getValue());
        }
        return true;
    }

    public boolean checkSquare(int square, SudokuGrid sGrid)
    {
        List<Integer> numberToLockFor = new ArrayList<>();

        for(int i : getSquarePositions(square)){

            if(sGrid.getItem(i).getValue() != 0 ){
                if (numberToLockFor.contains(sGrid.getItem(i).getValue())) {
                    return false;
                }
            }
            numberToLockFor.add(sGrid.getItem(i).getValue());
        }
        return true;
    }

    public boolean checkGame(SudokuGrid sGrid){
        for (int i = 0; i < 9; i++){
            if (!checkColumn(i, sGrid)) return false;
            else if (!checkRow(i, sGrid)) return false;
            else if (!checkSquare(i, sGrid)) return false;
        }
        return true;
    }

    private ArrayList<ArrayList<Integer>> createCandidates()
    {
        ArrayList<ArrayList<Integer>> candidates = new ArrayList<>();

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

    public List<Integer> getAvailableNumbers(int position){
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

    /*
    * solvesudoku is a backtracking algorithm, that solves the sudoku
    * solvesudokubackward is the same algorithm with the difference that it tries the numbers from 9-1
    * instead of 1-9 by running both algorithms and comparing the result, it can be determined if
    * the sudoku has a unique solution (both solved sudokus are the same in that case)
    * */

    private boolean solveSudoku(int position){

        if (position > 80) return true; // reached end of the grid Sudoku is solved

        SudokuCellView cell = grid.getItem(position);

        if (cell.getValue() != 0){
            return solveSudoku(position + 1); // Go to next cell because current cell is already solved
        }

        for (int i = 1; i < 10; i++){

            boolean valid = getAvailableNumbers(position).contains(i); // checks if i is a possible solution to the current cell

            if (!valid){
                continue;
            }

            cell.setValue(i); // assign the value of i to the cell, because it's a possible solution

            boolean solveNext = solveSudoku(position + 1);

            if (solveNext){
                return true;
            }
            else{
                cell.setValue(0);
            }
        }

        return false;
    }

    private boolean solveSudokuBackward(int position){

        if (position > 80) return true; // reached end of the grid Sudoku is solved

        SudokuCellView cell = grid.getItem(position);

        if (cell.getValue() != 0){
            return solveSudokuBackward(position + 1); // Go to next cell because current cell is already solved
        }

        for (int i = 9; i > 0; i--){

            boolean valid = getAvailableNumbers(position).contains(i); // checks if i is a possible solution to the current cell

            if (!valid){
                continue;
            }

            cell.setValue(i); // assign the value of i to the cell, because it's a possible solution

            boolean solveNext = solveSudokuBackward(position + 1);

            if (solveNext){
                return true;
            }
            else{
                cell.setValue(0);
            }
        }

        return false;
    }

    private boolean isSolutionUnique(){
        solveSudoku(0);
        String firstSolution = grid.getGridString();
        grid.clearGrid();//resets to grid to initial grid
        solveSudokuBackward(0);
        String secondSolution = grid.getGridString();
        grid.clearGrid();

        return firstSolution.equals(secondSolution);
    }
}

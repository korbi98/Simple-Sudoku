package com.korbi.simplesudoku.logic;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.korbi.simplesudoku.sudokuviews.SudokuCellView;

import java.util.List;

/**
 * Created by korbi on 8/19/17.
 * This adapter handels the different regions (3x3 squares) of the sudoku
 * right now the my solution of showing gridviews in gridviews seems to be suboptimal, as it looks
 * like it takes quite a lot of cpu resources because the gridviews get recreated permanently
 */

public class RegionAdapter extends BaseAdapter
{
    private List<SudokuCellView> regionData;


    public RegionAdapter(List<SudokuCellView> regionData)
    {
        this.regionData = regionData;
    }

    @Override
    public int getCount()
    {
        return 9;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return  regionData.get(position);
    }
}

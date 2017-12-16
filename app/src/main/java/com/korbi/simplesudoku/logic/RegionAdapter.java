package com.korbi.simplesudoku.logic;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.korbi.simplesudoku.sudokuviews.SudokuCellView;
import java.util.List;

/**
 * Created by korbi on 8/19/17.
 */

public class RegionAdapter extends BaseAdapter
{
    private Context context;
    private List<SudokuCellView> regionData;


    public RegionAdapter(Context context, List<SudokuCellView> regionData)
    {
        this.context = context;
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

package com.korbi.simplesudoku.logic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.korbi.simplesudoku.R;
import com.korbi.simplesudoku.sudokuviews.SudokuCellView;
import com.korbi.simplesudoku.sudokuviews.SudokuGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by korbi on 7/22/17.
 */

public class SudokuAdapter  extends BaseAdapter{

    private SudokuGrid grid;
    private final SudokuGridView regions[];
    private RegionAdapter adapters[];
    private List<List<SudokuCellView>> regionData;
    private SudokuLogic sHelber;

    public SudokuAdapter(SudokuGrid grid)
    {
        this.grid = grid;
        regions = new SudokuGridView[9];
        regionData = getRegionData(grid);
        adapters = new RegionAdapter[9];
        sHelber = new SudokuLogic(grid);

        for (int i = 0; i < 9 ; i++){
            adapters[i] = new RegionAdapter(regionData.get(i));
        }

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
    public View getView(final int position, final View convertView, ViewGroup parent) //TODO find/fix reason for high cpu usage
    {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.region_grid, parent, false);
        }

        regions[position] = (SudokuGridView) view.findViewById(R.id.region_field);
        final SudokuGridView gridView = regions[position];

        gridView.setNumColumns(3);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int positionInRegion, long id) {
                //Toast.makeText(context, String.valueOf(getPosition(position, positionInRegion)), Toast.LENGTH_LONG).show();
                grid.highlightCells(getPosition(position, positionInRegion));
            }
        });
        gridView.setAdapter(adapters[position]);

        return view;
    }

    private List<List<SudokuCellView>> getRegionData(SudokuGrid grid)
    {
        List<List<SudokuCellView>> dataList = new ArrayList<>();

        for(int i = 0; i<3;i++) {
            for (int j = 0; j < 3; j++) {

                List<SudokuCellView> regionData = new ArrayList<>();

                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {

                        regionData.add(grid.getItem(l + j * 3, k + i * 3));

                    }
                }
                dataList.add(regionData);
            }
        }
        return dataList;
    }

    private int getPosition(int regionNumber, int positionInRegion){
        int regionRow = regionNumber / 3;
        int regionColumn = regionNumber % 3;
        int rowInRegion = positionInRegion / 3;
        int columnInRegion = positionInRegion % 3;
        int y = regionRow * 3 + rowInRegion;
        int x = regionColumn * 3 + columnInRegion;

        return y * 9 + x;
    }
}

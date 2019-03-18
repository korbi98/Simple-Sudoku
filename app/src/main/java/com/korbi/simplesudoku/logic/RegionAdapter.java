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

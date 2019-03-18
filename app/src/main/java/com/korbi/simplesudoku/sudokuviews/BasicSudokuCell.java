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

package com.korbi.simplesudoku.sudokuviews;

import android.content.Context;

/**
 * Created by korbi on 7/21/17.
 */

public class BasicSudokuCell extends android.support.v7.widget.AppCompatTextView
{
    private int value = 0;
    private boolean isPreSet = true;

    public BasicSudokuCell(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //noinspection SuspiciousNameCombination
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    public void setIsPreSet(boolean isPreSet) {
        this.isPreSet = isPreSet;
    }

    public boolean isPreSet() { return isPreSet; }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}

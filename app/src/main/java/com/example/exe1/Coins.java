package com.example.exe1;

public class Coins extends Characters {

    private boolean lastRow;

    public Coins(int maxRow, int maxCol, int col) {
        super(maxRow, maxCol);
        super.row = 0;
        super.setCol(col);
        lastRow = false;
    }


    public void moving() {
        if(lastRow){
            return;
        }
        else if(row==maxRow-1){
            lastRow=true;
        }
        super.row++;
    }

    public boolean isLastRow(){

        return lastRow;
    }
}


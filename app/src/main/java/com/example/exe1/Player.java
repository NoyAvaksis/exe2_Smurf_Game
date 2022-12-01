
package com.example.exe1;

public class Player extends Characters {

    public Player(int maxRow, int maxCol) {
        super(maxRow, maxCol);
        row = maxRow - 1;
    }


    @Override
    public Characters setRow(int row) {
        return super.setRow(super.maxRow-1);
    }

    public void moving(eMove move) {
        if (move == eMove.right) {
            if (col >= (maxCol-1)) {
                return;
            }
            col++;
        } else {
            if (col <= 0) {
                return;
            }
            col--;
        }

    }
}
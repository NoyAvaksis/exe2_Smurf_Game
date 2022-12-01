package com.example.exe1;


import java.util.Random;


public class Characters {

   protected int row;
   protected int col;
   protected int maxRow;
   protected int maxCol;
   protected int imgNum;

   public Characters(int maxRow, int maxCol) {
       this.maxRow = maxRow;
       this.maxCol = maxCol;
   }

    public int getMaxRow() {
        return maxRow;
    }

    public Characters setMaxRow(int maxRow) {
        this.maxRow = maxRow;
        return this;
    }

    public int getMaxCol() {
        return maxCol;
    }

    public Characters setMaxCol(int macCol) {
        this.maxCol = maxCol;
        return this;
    }

    public int getImgNum() {
        return imgNum;
    }

    public Characters setImgNum(int imgNum) {
        this.imgNum = imgNum;
        return this;
    }

    public int getRow() {
        return row;
    }


    public Characters setRow(int row) {
        if(row>=0 && row<maxCol){
            this.row = row;
            return this;
        }
        else
            return this;

    }

    public Characters setCol(int col) {
        if(col>=0 && row<maxCol){
            this.col = col;
            return this;
        }
        else
            return this;
    }
    public int getCol() {
        return col;
    }
}

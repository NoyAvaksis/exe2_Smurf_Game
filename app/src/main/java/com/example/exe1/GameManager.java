package com.example.exe1;

public class GameManager {

    private int life;
    private int rows;
    private int cols;
    private Characters bord[][];
    private Player player;

    public GameManager(int rows, int cols, int life) {
        this.life = life;
        this.cols = cols;
        this.rows = rows;
        bord = new Characters[rows][cols];
        player = (Player) new Player(rows, cols).setMaxRow(rows).setMaxCol(cols);
        startGame();
    }

    public int getEnemyCol(int row) {
        for (int i =0; i <cols ; i++) {
            if(bord[row][i] instanceof Enemy)
                return i;
        }
        return -1;
    }

    public void nextMoveEnemy() {
        for (int i = rows-1; i >= 0; i--) {
            for (int j = cols-1; j >=0; j--) {
                if(bord[i][j] instanceof Enemy) {
                    ((Enemy) bord[i][j]).moving();
                    if(i<rows-1) {
                        bord[i + 1][j] = bord[i][j];
                    }
                    bord[i][j]=null;
                }
            }
        }
        newEnemy();
    }

    public boolean isEndGame() {
         return (life==0);
    }

    public boolean isCrash() {
        int enemyCol;
        enemyCol = getEnemyCol(rows-1);
            if(enemyCol == player.getCol() && enemyCol>=0){
            life--;
            return true;
            }
            return false;
    }

    public void newEnemy(){
        int randCol = (int)(Math.random()*cols);
        bord[0][randCol] = new Enemy(rows,cols,randCol);
    }

    public void movePlayer(eMove move) {
        bord[player.row][player.col] = null;
        player.moving(move);
        bord[player.row][player.col] =player;
    }
    
    public void startGame() {
      newEnemy();
      bord[rows-1][cols/2] = player;
    }

    public int getLife() {
        return life;
    }

    public GameManager setLife(int life) {
        this.life = life;
        return this;
    }

    public int getRows() {
        return rows;
    }

    public GameManager setRows(int rows) {
        this.rows = rows;
        return this;
    }

    public int getCols() {
        return cols;
    }

    public GameManager setCols(int cols) {
        this.cols = cols;
        return this;
    }

    public Player getPlayer() {
        return player;
    }
}

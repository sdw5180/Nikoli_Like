/**
 * Adapted from the SatogaeriCell.java file from the Satogaeri puzzle generator
 * 
 * A cell for a custom Nikoli-Style puzzle, can be marked as either black or white
 */

 import java.util.Random;

public class NikoliCell{
    private static Random random = new Random();

    public int row;                                                         // location
    public int col;
    public int regionNum;
    public NikoliRegion region;                                  
    public int color = 0;                                                       // -1 = black, 1 = white


    /**
     * constructor
     */
    public NikoliCell(int r, int c, int region){
        this.row = r; this.col = c; this.regionNum = region;
    }

    /**
     * constructor
     */
    public NikoliCell(int r, int c, NikoliRegion region){
        this.row = r; this.col = c; this.regionNum = region.region;
    }

    public void setColor(int i){
        this.color = i;
    }

    /**
     * Checks neighboring cells for one assigned to a region, then creates a new cell of the same region for the 
     * specified coordinates
     * @param board         a 2d array representing the game board with existing cells
     * @param row           row of cell to be created
     * @param col           col of cell to be created
     * @return              a new cell - or null if no neighbors were found
     */
    public static NikoliCell findMatchNeighbor(NikoliCell[][] board, int row, int col){
        int[] indexes = {-1, 1};

        // check vertical and horizontal neighbors, making a new cell of the same region of a neighboring region cell is found:
        for (int i : indexes){
            if ((col + i) >= 0 && (col + i) < NikoliGen.cols){
                if (board[row][col + i] != null) { return new NikoliCell(row, col, board[row][col + i].region); } }
            if ((row + i) >= 0 && (row + i) < NikoliGen.rows){ 
                if (board[row + i][col] != null) { return new NikoliCell(row, col, board[row + i][col].region); } }
        }
        return null;
    }

    /**
     * Creates a new cell neighboring the current one
     * 
     * @param r             maximum acceptable row value
     * @param c             maximum acceptable column value
     * @param regionBoard   board of NikoliCells to verify a cell region is not already assigned
     * @return
     */
    public NikoliCell createNeighborCell(int r, int c, NikoliCell[][] regionBoard){
        int nRow;
        int nCol;
        int itr = 0; 
        int case_int = 0;

        while (itr < 10){

            nRow = this.row + (random.nextBoolean() ? 1 : 0);       // generate coordinates +- 1 from this cell
            nCol = this.col + (random.nextBoolean() ? 1 : 0);

            // avoid out of bounds indexes:
            if (nRow < 0 || nCol < 0 || nRow >= r || nCol >= c || (nRow == this.row && nCol == this.col)) { 
                itr += 1;
                continue;
            }

            // if the cell is empty, make a new cell of the same region as this:
            case_int = random.nextBoolean() ? 1 : 0;                        // https://stackoverflow.com/questions/3793650/convert-boolean-to-int-in-java
            switch (case_int) {
                case 0:
                    if (regionBoard[this.row][nCol] == null){ return new NikoliCell(this.row, nCol, this.region); }
                    break;
                case 1:
                    if (regionBoard[nRow][this.col] == null){ return new NikoliCell(nRow, this.col, this.region); }
                    break;
                default:
                    System.out.println("wuh oh, case:  " + case_int);
            }
            itr += 1;
        }
        return null;
    }
}
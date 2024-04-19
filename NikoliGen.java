import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * Adapted from the SatogaeriRegion.java file from the Satogaeri puzzle generator
 * 
 * A generator for a Nikoli-Style board game:
 * This game is played on a grid (2d-array) that is divided into regions; each region must be either shaded
 * or left unshaded in order to satisfy the following rules:
 *      - All rows and columns must have an even number of shaded and unshaded cells (cells being single boxes
 *        in the grid -- regions are made up of n touching cells)
 */

public class NikoliGen implements Runnable{
    /* Globals */
    private static int MIN_REGION_SIZE = 2;
    private static int BOARD_SIZE = 10;                        // sets the dimensions of the board
    private static int TOTAL_CELLS = BOARD_SIZE * BOARD_SIZE;
    private static int NUM_REGIONS = (int)(TOTAL_CELLS * 0.15);
    private static int CELLS_PER_REGION = (int)(TOTAL_CELLS/NUM_REGIONS);
    private static boolean DEBUG_MODE = false;
    public static int rows = BOARD_SIZE;                        // board dimensions
    public static int cols = BOARD_SIZE;
    private static Random random = new Random();

    
    private NikoliCell[][] regionBoard;                         // board displaying the regions of the board
    private int numRegions;                                     // total number of regions for the board
    
    private Map<Integer, NikoliRegion> regionsMap;              // map to fetch region by its number
    public ArrayList<NikoliRegion> regions;                     // ArrayList containing all region Objects

   
    /**
     * Constructor
     */
    public NikoliGen(){
        // no constructor contents; variables are handled in generateRegions()
    }


    /**
     * Stolen: https://www.baeldung.com/java-generating-random-numbers-in-range lol
     * Min assumed to be zero
     * @param max               maximum int value allowed
     * @return
     */
    public static int randNum(int max) {
        return (int) (Math.random() * max);
    }


    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }


    /**
     * Verifies that all cells on the board have been assigned to a region
     * @return              true if the board is complete, false otherwise
     */
    private boolean regionsComplete(){
        for (NikoliCell[] row : regionBoard){
            if (Arrays.asList(row).contains(null)){ return false; }
        }
        return true;
    }


     /**
     * Generates the region of the board by assigning region start locations then randomly expanding them. The
     * Regions are re-generated until they meet the requirements for the board:
     *      - minimum region size 
     *      - minimum number of regions
     *      - all cells are included in a region
     */
    private void generateRegions(){
        NikoliCell newCell = null;
        NikoliCell cell = null;
        int r = 0; int c = 0;

        //int totCells = rows * cols;
        //int totCells = TOTAL_CELLS;
        //numRegions = (int)(totCells * (4/10));                  // TO DO: remove magic numbers
        int numRegions = NUM_REGIONS;
        //int perRegion = (totCells / numRegions);
        int perRegion = CELLS_PER_REGION;

        boolean regionsComplete = false;
        while (!regionsComplete){
            // reset board (for in cases of reruns):
            this.regionBoard = new NikoliCell[rows][cols];
            //NikoliRegion.resetRegions();
            this.regions = new ArrayList<>();
            this.regionsMap = new HashMap<>();

            // instanciate regions:
            for (int regNum = 1; regNum < numRegions; regNum ++){
                NikoliRegion reg = new NikoliRegion(regNum);
                this.regions.add(reg);
                this.regionsMap.put(regNum, reg);
            }

            // for each region, assign it a single empty cell as a starting cell:
            for (NikoliRegion region : this.regions){
                while (this.regionBoard[r][c] != null){
                    r = randNum(rows); c = randNum(cols);
                }
                newCell = new NikoliCell(r, c, region.region);   // region.region lol
                this.regionBoard[r][c] = newCell;
                region.addCell(newCell);
            }

            // for each region, randomly expand it to fill board cells
            for (NikoliRegion region : this.regions){
                for (int i = 0; i < perRegion;){
                    cell = region.getRandomCell();
                    if (cell != null){
                        newCell = cell.createNeighborCell(rows, cols, this.regionBoard);
                        if (newCell != null) { 
                            region.addCell(newCell); 
                            this.regionBoard[newCell.row][newCell.col] = newCell;
                        }
                         i += 1;
                    }
                }
            }
            //if (DEBUG_MODE){ printRegionBoard(); System.out.println("\n"); }

            // fill remaining cells by adding them to neighboring regions
            for (int rw = 0; rw < rows; rw++){
                for (int cl = 0; cl < cols; cl++){
                    if (this.regionBoard[rw][cl] == null){
                        newCell = NikoliCell.findMatchNeighbor(this.regionBoard, rw, cl);
                        if (newCell != null){
                            this.regionBoard[rw][cl] = newCell;
                            this.regionsMap.get(newCell.regionNum).addCell(newCell);   // god, this line of code looks ugly
                        }
                    }
                }
            }

            //if (DEBUG_MODE){ printRegionBoard(); System.out.println("\n"); }
            regionsComplete = true; // assumed true until proven false

            // If any regions are too small, recreate the board:
            for (NikoliRegion reg : this.regions){ if (reg.cells.size() < MIN_REGION_SIZE){ 
                if (DEBUG_MODE){ System.out.println("Region with size 1: " + reg.region); }
                regionsComplete = false;
                            } 
            }
            // If any cells are unasigned, recreate the board:
            if (!regionsComplete()){
                if (DEBUG_MODE){ System.out.println("Unfilled space in region board"); }
                regionsComplete = false;
            }
        }

        // Output completed board:
        if (DEBUG_MODE){
            System.out.println("\n--------------\nCompleted region board:");
            printRegionBoard();
        }  
    }



    private void genBoard(){
        NikoliRegion curRegion = null;
        int color = 0;

        // generate new board:
        boolean regionsComplete = false;
        while (!regionsComplete){
            this.generateRegions();                                          // generate/reset board
            //System.out.println("re-gen");

            // attempt to retry assigning regions to the current board several times before regenerating the board:
            for (int retry_assign = 0; retry_assign < 30; retry_assign++){
                
                // randomly assign regions to be either black or white:
                for (NikoliRegion region : this.regions){ region.setColor((random.nextBoolean() ? 1 : -1)); }

                // verify possible solution:
                regionsComplete = true;                                     // assume true, set false on invalid row/column
                int rowSum = 0;
                int colSum = 0;
                // check all columns have equal num black/white cells
                for (int i = 0; i < rows; i++){         
                    colSum = 0;
                    for (int j = 0; j < cols; j++){ colSum += this.regionBoard[i][j].color; }
                    if (colSum != 0) { regionsComplete = false; break;}
                }
                // check all rows have equal num black/white cells
                if (regionsComplete){
                    for (int i = 0; i < cols; i++){     
                        rowSum = 0;
                        for (int j = 0; j < rows; j++){ rowSum += this.regionBoard[i][j].color; }
                        if (rowSum != 0) { regionsComplete = false; break;}
                    }
                }

                // no invalid rows/columns were found, don't regenerate regions:
                if (regionsComplete){ break; }
            }

        }
    }


    public static void main(String[] args) {
        //Scanner consoleIn = new Scanner(System.in);
        //rows = 15;
        //cols = 15;
        //generateRegions();   
        //System.out.println("\n--------------\nCompleted region board:");
        //genBoard();
        //printRegionBoard();      
    }


    /**
     * outputs the regionBoard to the console
     */
    private void printRegionBoard(){
        for (NikoliCell[] row : this.regionBoard){
            for (NikoliCell cell : row){
                if (cell != null){ System.out.print(cell.regionNum); }
                else{ System.out.print("_"); }
                System.out.print("\t");
            }
            System.out.print("\n");
        }
    }

    /**
     * outputs the regionBoard to the console
     */
    private void printFillBoard(){
        for (NikoliCell[] row : this.regionBoard){
            for (NikoliCell cell : row){
                if (cell != null){ 
                    if (cell.color == 1) { System.out.print(" " + cell.color);  }
                    else { System.out.print(cell.color); }
                }
                System.out.print("\t");
            }
            System.out.print("\n");
        }
    }


    @Override
    public void run(){
        //try { Thread.sleep(15000);} 
        //catch (InterruptedException e) { System.out.println("crash in genThread"); }
        //this.generateRegions();
        this.genBoard();
        System.out.println("---------------------------");
        this.printRegionBoard(); 
        System.out.println("---------------------------");
        printFillBoard();
        System.out.println("---------------------------");

    }
        

}
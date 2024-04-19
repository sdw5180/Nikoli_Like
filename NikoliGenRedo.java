import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;
import java.awt.Toolkit;


public class NikoliGenRedo implements Runnable{
    private  int[][] board;
    private static int BOARD_SIZE = 10;                        // sets the dimensions of the board
    private static boolean DEBUG_MODE = false;
    private  Random random = new Random();
    private int puzNum = 0;


    /**
     * Verifies a specific cell has at least one neighboring cell of the same value -- either black (-1)
     * or white (1). This is to prevent a region being created that is only a single-cell
     * @param i                 row num of current cell
     * @param j                 col num of current cell
     * @return                  true if the cell has at least one neighbor
     */
    private boolean hasNeighbors(int i, int j) {
        int currentCell = this.board[i][j];
        int neighborCount = 0;
        int[] indexMods = {-1, 1};
        // check each neighborung cell for equivelent values:
        for (int ind1 : indexMods){
            try{
                if (this.board[i + ind1][j] == currentCell){ neighborCount += 1; }
            }catch (IndexOutOfBoundsException e) {} // do nothing
            try{
                if (this.board[i][j + ind1] == currentCell){ neighborCount += 1; }
            }catch (IndexOutOfBoundsException e) {} // do nothing
        }
        // neighborCount will always be at least one, since (i, j) will always equal itself, so a
        // count greater than 1 indicates the cell has at least one neighboring cell of the same value:
        return (neighborCount > 1);
    }


    private void genBoard(){
        boolean regionsComplete = false;
        while (!regionsComplete){
            this.board = new int[BOARD_SIZE][BOARD_SIZE];
            // Randomly set every space on the board as a -1 or 1:
            for (int i = 0; i < BOARD_SIZE; i++){ 
                for (int j = 0; j < BOARD_SIZE; j++){
                    this.board[i][j] = (random.nextBoolean() ? 1 : -1);
                }
            }

            // verify possible solution:
            regionsComplete = true;         // assume true, set false on invalid row/column
            int rowSum = 0;
            int colSum = 0;
            // check all columns have equal num black/white cells
            for (int i = 0; i < BOARD_SIZE; i++){         
                colSum = 0;
                for (int j = 0; j < BOARD_SIZE; j++){ 
                    colSum += this.board[i][j]; 
                    // verify no single-cell regions have been created:
                    if (!hasNeighbors(i, j)) { regionsComplete = false; break; }
                }
                if (colSum != 0) { regionsComplete = false; break;}
            }
            // check all rows have equal num black/white cells
            if (regionsComplete){
                for (int i = 0; i < BOARD_SIZE; i++){     
                    rowSum = 0;
                    for (int j = 0; j < BOARD_SIZE; j++){ 
                        rowSum += this.board[i][j]; 
                    }
                    if (rowSum != 0) { regionsComplete = false; break;}
                }
            }

            // no invalid rows/columns were found, don't regenerate regions:
            if (regionsComplete){ break; }
            else if (DEBUG_MODE){ System.out.println(parsePuzzleText()); }

        }
    }


    /**
     * Saves the current puzzle board to the next puzzle save-file
     */
    private void savePuzzle() {
        //String fileName = "puzzle_concept" + puzNum;
        String fileName = "puzzle_concept" + NikoliGen.getRandomNumber(1, 100);
        File file= new File(fileName);
        //puzNum += 1;
        // Delete preexisting file if name is taken:
        if (file.delete()) { System.out.println("Previous file deleted: " + file.getName()); }
        
        // Recreate file of fileName:
        try{ if (file.createNewFile()) { System.out.println("File created: " + file.getName()); }
        } catch (IOException e) {  System.out.println("Something went wrong in file creation"); System.exit(-1); }

        // parse puzzle to string:
        String puzzleOut = this.parsePuzzleText();
        try{
            FileWriter fileWriter = new FileWriter(fileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(puzzleOut);
            printWriter.close();
            System.out.println("Saved puzzle to " + fileName + "\n");
        }
        catch (Exception e) { System.exit(-1);}
    }


    /**
     * Parses a 2d array of integers (0s and 1s) into a puzzle board string
     * @return              parsed board string
     */
    private String parsePuzzleText(){
        String out = ""; 
        String whitespace = "   ";
        for (int i = 0; i < BOARD_SIZE; i++){ 
            for (int j = 0; j < BOARD_SIZE; j++){
                if (this.board[i][j] == 1) { out += " " + this.board[i][j];  }
                    else { out += this.board[i][j]; }
                out += whitespace;
            }
            out += "\n";
        }
        return out;
    }


    @Override
    public void run() {
        Scanner consoleIn = new Scanner(System.in);
        System.out.println("Running generator... this may take a few minutes...");
        while(true){
            genBoard();
            // beep to notify of puzzle creation:
            Toolkit.getDefaultToolkit().beep();
            System.out.println(parsePuzzleText());

            savePuzzle();
            //System.out.println("Save puzzle?\t:s");
            //String save = consoleIn.nextLine();
            //switch (save) {
            //    case "s":
            //        savePuzzle();
            //        break;
            //   default:
            //        System.out.println("Did not save puzzle");
            //        break;
            // }
        }
    }


    public static void main(String[] args) {
        // nah
    }

}

import java.util.ArrayList;

/**
 * Adapted from the SatogaeriRegion.java file from the Satogaeri puzzle generator
 * 
 * A region for a custom Nikoli-Style puzzle, can be marked as either black or white
 */

 public class NikoliRegion{
    public int region;                                                      // region number
    public int color = 0;                                                       // -1 = black, 1 = white
    public boolean filled = false;
    public ArrayList<NikoliCell> cells = new ArrayList<>();                 // ArrayList containing the regions cells

    /**
     * Constructor
     * @param r     region number
     */
    public NikoliRegion(int r){
        this.region = r;
        // regions.add(this);
    }

    /**
     * Resets the regions' arraylist for a board reset
     */
    //public static void resetRegions(){
    //    regions = new ArrayList<>();
    //}

    /**
     * Marks the region as complete
     */
    public boolean fill(){
        if (!this.filled) { 
            this.filled = true; 
            return this.filled;
        }
        else {
            return false;
        }
    }


    /**
     * Checks if the region is complete
     */
    public boolean isComplete() {
        return this.filled;
    }


    /**
     * Returns the list of cells for this region
     */
    public ArrayList<NikoliCell> getCells(){
        return this.cells;
    }

        /**
     * Adds a cell to the region
     * @param cell          cell to be added
     */
    public void addCell(NikoliCell cell){
        this.cells.add(cell);
        cell.region = this;
    }


    /**
     * Creates a new cell object and adds it to the region
     * @param r             row of cell to be created
     * @param c             col of cell to be created
     */
    public void addCell(int r, int c){
        NikoliCell cell = new NikoliCell(r, c, this.region);
        cell.region = this;
        this.addCell(cell);
    }


    /**
     * Gets a random cell from the region and returns it
     */
    public NikoliCell getRandomCell(){
        int index = (int)(Math.random() * this.cells.size());
        return this.cells.get(index);
    }


    /**
     * Sets the color for all cells and the region to the integer i
     * @param i             Integer, presumed 1 or -1 (0 means unasigned)
     */
    public void setColor(int i){
        for (NikoliCell cell : cells){
            cell.setColor(i);
        }
        this.color = i;
        
        if (i != 0){ this.fill(); }
    }
 }
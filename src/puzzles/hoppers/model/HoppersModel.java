package puzzles.hoppers.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Aniruddha Roy
 * ar7475
 */

public class HoppersModel {
    /** the collection of observers of this model */
    private final List<Observer<HoppersModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private HoppersConfig currentConfig;
    private String fname;
    public LinkedList<Configuration> path;
    private int curR = -1;
    private int curC = -1;

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<HoppersModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String msg) {
        for (var observer : observers) {
            observer.update(this, msg);
        }
    }
    /**

     Constructs a new HoppersModel object with the specified filename.
     @param filename the name of the file to be loaded for the game configuration
     @throws IOException if there is an error reading the file
     */
    public HoppersModel(String filename) throws IOException {
        try{
            currentConfig = new HoppersConfig(filename);
            alertObservers("Loaded: " + filename);
        } catch (Throwable throwable){
            alertObservers("Failed to load: "+ throwable.getMessage());
        }
        this.fname = filename;

    }

    /**
     Returns the current configuration of the Hoppers game.
     @return the current configuration of the Hoppers game as a HoppersConfig object
     */
    public HoppersConfig currConfig(){
        return currentConfig;
    }


    /**
     * Generates a hint for the next move to solve the puzzle.
     * Uses breadth-first search to find the shortest path to the next move.
     * If no solution is found, alerts the observers with "No solution" message.
     * If the puzzle is already solved, alerts the observers with "Already solved" message.
     * Updates the current configuration to the next move and alerts the observers with "Next step!" message.
     */
    public void hint(){
        this.path = new Solver().bfs(currentConfig);

        if (path.isEmpty()){
            alertObservers("No solution");
        } else if (path.size()==1) {
            alertObservers("Already solved");
        }
        else {
            currentConfig = (HoppersConfig) path.get(1);
            alertObservers("Next step!");
        }
    }

    /**
     * Loads a puzzle from a file with the given filename.
     *
     * @param filename the name of the file containing the puzzle to be loaded
     */
    public void loadPuzzle(String filename) {
        try {
            this.fname = filename;
            currentConfig = new HoppersConfig(fname);

            this.alertObservers("Loaded: " + fname);
            System.out.println(ptuiToString());

        } catch (IOException e) {
            this.alertObservers("Failed to load: "+ e.getMessage());
        }
    }


    /**
     * Selects the cell at the specified row and column.
     *
     * @param row the row of the cell to be selected
     * @param col the column of the cell to be selected
     */
    public void selectCell(int row, int col) {
    selectHelp(row,col);
    }


    /**
     * Resets the puzzle by reloading the original puzzle file specified in the constructor.
     * Notifies the observers that the puzzle has been reset.
     */
    public void reset(){
        loadPuzzle(fname);
        this.alertObservers("Puzzle reset!");
    }


    /**
     Converts the current configuration of the Hoppers game to a StringBuilder object
     that represents the plain text user interface (PTUI) of the game.
     @return a StringBuilder object that represents the PTUI of the game
     */
    public StringBuilder ptuiToString(){
        StringBuilder s = new StringBuilder("   ");
        for (int c = 0; c < currentConfig.getNumCols(); c++){
            s.append(c);
            if (c <= currentConfig.getNumCols()){
                s.append(" ");
            }
        }
        s.append("\n  ");
        s.append("--".repeat(Math.max(0, currentConfig.getNumCols())));
        s.append("\n");
        for (int r = 0; r < currentConfig.getNumRows(); r++){
            s.append(r).append("|");
            for (int c = 0; c < currentConfig.getNumCols(); c++){
                s.append(" ").append(currentConfig.getGrid()[r][c]);
            }
            s.append("\n");
        }
        return s;
    }


    /**
     Selects a cell in the Hoppers game grid for a possible frog jump or to indicate a selected frog.
     If there is no previously selected frog, the cell is selected if it contains a frog (either green or red).
     If there is a previously selected frog, the cell is selected as the landing position for the frog jump, if the jump is valid.
     If the jump is not valid, the previous selection is cleared and an error message is sent to the observers.
     @param r the row number of the selected cell
     @param c the column number of the selected cell
     */
    public void selectHelp(int r, int c) {
        if (curR == -1 && curC == -1) {
            // First select
            if (Objects.equals(currentConfig.getGrid()[r][c], "*")) {
                alertObservers("Invalid selection at (" + r + ", " + c + ")");
            } else if (Objects.equals(currentConfig.getGrid()[r][c], "G") || Objects.equals(currentConfig.getGrid()[r][c], "R")) {
                curR = r;
                curC = c;
                alertObservers("Selected (" + r + ", " + c + ")");
            }

            else {
                alertObservers("No frog at (" + r + ", " + c + ")");
            }
        } else {
            // Second select
            if (Objects.equals(currentConfig.getGrid()[r][c], "*")) {
                alertObservers("Can't jump from (" + curR + ", " + curC + ") to (" + r + ", " + c + ")");
                curR = -1;
                curC = -1;
            } else if (Objects.equals(currentConfig.getGrid()[r][c], "R") || Objects.equals(currentConfig.getGrid()[r][c], "G")) {
                alertObservers("Can't jump from (" + curR + ", " + curC + ") to (" + r + ", " + c + ")");
                curR = -1;
                curC = -1;
            }
            else {
                // Check if there is a frog in the path and if the landing position is free
                int midR = (curR + r) / 2;
                int midC = (curC + c) / 2;

                if (Objects.equals(currentConfig.getGrid()[midR][midC], "*")) {
                    alertObservers("Can't jump over water to (" + r + ", " + c + ")");
                    curR = -1;
                    curC = -1;

                }
                else if (Objects.equals(currentConfig.getGrid()[midR][midC], "R")) {
                    alertObservers("Can't land on (" + r + ", " + c + ")");
                    curR = -1;
                    curC = -1;

                }else if (!Objects.equals(currentConfig.getGrid()[midR][midC], "G")) {
                    alertObservers("No frog to jump over at (" + midR + ", " + midC + ")");
                    curR = -1;
                    curC = -1;

                } else if (!Objects.equals(currentConfig.getGrid()[r][c], ".")) {
                    alertObservers("Can't land on (" + r + ", " + c + ")");
                    curR = -1;
                    curC = -1;
                }

                else {

                    if (Math.abs(r-curR) == Math.abs(c-curC)){
                        if (Math.abs(r-curR) != 2 && Math.abs(c-curC) != 2) {
                            alertObservers("Can't land on (" + r + ", " + c + ")");
                            curR = -1;
                            curC = -1;
                            return;
                        }
                    }

                    else if (Math.abs(r-curR) != Math.abs(c-curC)){
                        if (Math.abs(r-curR) != 4 && Math.abs(c-curC) != 4) {
                            alertObservers("Can't land on (" + r + ", " + c + ")");
                            curR = -1;
                            curC = -1;
                            return;
                        }
                    }
                    // Jump the frog
                    if (Objects.equals(currentConfig.getGrid()[curR][curC], "R")) {
                        currentConfig.getGrid()[r][c] = "R";
                    } else {
                        // Otherwise, toggle the value of the element between "RED_FROG" and "GREEN_FROG"
                        currentConfig.getGrid()[r][c] = "G";
                    }

                    currentConfig.getGrid()[curR][curC] = ".";
                    currentConfig.getGrid()[midR][midC] = ".";

                    alertObservers("Jumped from (" + curR + ", " + curC + ") to (" + r + ", " + c + ")");
                    curR = -1;
                    curC = -1;
                }
            }
        }
    }
}
package puzzles.hoppers.model;

import puzzles.common.solver.Configuration;


import puzzles.common.Coordinates;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Aniruddha Roy
 * ar7475
 */



public class HoppersConfig implements Configuration {
    /**
     * The rectangular grid of the Hoppers puzzle game.
     */
    private final String[][] grid;

    /**
     * The number of rows of the grid.
     */
    private final int numRows;

    /**
     * The number of columns of the grid.
     */
    private final int numCols;

    /**
     * A constant string that represents a green frog.
     */
    public static String GREEN_FROG = "G";

    /**
     * A constant string that represents a red frog.
     */
    public static String RED_FROG = "R";

    /**
     * A constant string that represents an empty cell.
     */
    public static String LILLY_PAD = ".";

    /**
     * A constant string that represents an inaccessible cell.
     */
    public static String WATER = "*";



    /**
     * Constructs a HoppersConfig object by reading the grid from a file.
     * The file should have the following format:
     * - The first line contains two integers separated by a space: the number of rows and columns of the grid.
     * - The next numRows lines each contain numCols characters that represent the cells of the grid.
     *   Each character should be one of the following:
     *   - LILLY_PAD: ".": represents an empty cell
     *   - WATER: "*": represents an inaccessible cell
     *   - GREEN_FROG: "G": represents a green frog
     *   - RED_FROG: "R": represents a red frog
     *
     * @param filename The name of the file to read from.
     * @throws IOException If an error occurs while reading from the file.
     */
    public HoppersConfig(String filename) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {


            String[] dims = bufferedReader.readLine().split("\\s+");
            this.numRows = Integer.parseInt(dims[0]);
            this.numCols = Integer.parseInt(dims[1]);
            this.grid = new String[this.numRows][this.numCols];


            for (int r = 0; r < this.numRows; r++) {
                String[] nextLine = bufferedReader.readLine().split("\\s+");
                for (int c = 0; c < this.numCols; c++) {
                    this.grid[r][c] = String.valueOf((nextLine[c].charAt(0)));

                }
            }
        }
    }

    /**

     Represents the configuration of a game of Hoppers.
     The configuration includes the number of rows and columns in the game board,
     as well as the contents of each square on the board. The contents are represented
     as a 2D array of strings, where each string represents the piece on the corresponding square.
     This constructor creates a new HoppersConfig object based on the current configuration,
     but with a few changes:
     The square at the "remove" coordinates is replaced with a lilly pad.
     The piece at the "jumpStart" coordinates is temporarily removed from the board.
     The piece is placed on the square at the "jumpEnd" coordinates.
     @param current The current HoppersConfig object to be modified.
     @param remove The coordinates of the square to be replaced with a lilly pad.
     @param jumpStart The coordinates of the square containing the piece to be moved.
     @param jumpEnd The coordinates of the square where the piece should be moved.
     */
    public HoppersConfig(HoppersConfig current, Coordinates remove, Coordinates jumpStart, Coordinates jumpEnd) {
        this.numRows = current.numRows;
        this.numCols = current.numCols;
        this.grid = new String[this.numRows][this.numCols];
        for (int r = 0; r < numRows; r++) {
            System.arraycopy(current.grid[r], 0, this.grid[r], 0, this.numCols);
        }
        this.grid[remove.row()][remove.col()] = LILLY_PAD;
        String frog = this.grid[jumpStart.row()][jumpStart.col()];
        this.grid[jumpStart.row()][jumpStart.col()] = LILLY_PAD;
        this.grid[jumpEnd.row()][jumpEnd.col()] = String.valueOf(frog);
    }

    /**

     Determines whether the current state of the Hoppers puzzle is a valid solution.
     A valid solution is one where there are no green frogs remaining on the game board.
     @return true if the current state of the Hoppers puzzle is a valid solution, false otherwise
     */
    @Override
    public boolean isSolution() {
        for (int i = 0; i < this.numRows; ++i) {

            for (int j = 0; j < this.numCols; ++j) {

                if (this.grid[i][j].equals(GREEN_FROG)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     Returns a collection of all the neighboring configurations.
     @return a collection of neighboring configurations
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        LinkedList<Configuration> neighbors = new LinkedList<>();
        int[][] diagJumps = new int[][]{{-1, 1}, {1, -1}, {-1, -1}, {1, 1}};
        int[][] vertHorsJumps = new int[][]{{2, 0}, {0, 2}, {-2, 0}, {0, -2}};
        for (int r = 0; r < this.numRows; r++) {
            for (int c = 0; c < this.numCols; c++) {

                for (int[] jump : diagJumps) {
                    int frogRow = r + jump[0];
                    int frogCol = c + jump[1];
                    int destRow = r + 2 * jump[0];
                    int destCol = c + 2 * jump[1];
                    if (Objects.equals(this.grid[r][c], LILLY_PAD) || Objects.equals(this.grid[r][c], WATER)) continue;
                    if (isInBoundCell(frogRow, frogCol) && isInBoundCell(destRow, destCol) && isValid(destRow, destCol,
                            frogRow, frogCol)) {
                        neighbors.add(new HoppersConfig(this, new Coordinates(frogRow, frogCol),
                                new Coordinates(r, c), new Coordinates(destRow, destCol)));
                    }
                }
                if (r % 2 == 0 && c % 2 == 0) {
                    for (int[] jump : vertHorsJumps) {
                        int frogRow = r + jump[0];
                        int frogCol = c + jump[1];
                        int destRow = r + jump[0] * 2;
                        int destCol = c + jump[1] * 2;
                        if (Objects.equals(this.grid[r][c], LILLY_PAD) || Objects.equals(this.grid[r][c], WATER))
                            continue;
                        if (isInBoundCell(frogRow, frogCol) && isInBoundCell(destRow, destCol) && isValid(destRow,
                                destCol, frogRow, frogCol)) {
                            neighbors.add(new HoppersConfig(this, new Coordinates(frogRow, frogCol),
                                    new Coordinates(r, c), new Coordinates(destRow, destCol)));
                        }
                    }
                }
            }
        }
        return neighbors;
    }


    /**
     Checks if a given move is valid.
     @param destRow the row number of the destination cell
     @param destCol the column number of the destination cell
     @param frogRow the row number of the frog cell
     @param frogCol the column number of the frog cell
     @return true if the move is valid, false otherwise
     */
    public boolean isValid(int destRow, int destCol, int frogRow, int frogCol) {
        return this.grid[destRow][destCol].equals(LILLY_PAD) && this.grid[frogRow][frogCol].equals(GREEN_FROG);
    }


    /**
     Checks if the given row and column values correspond to a cell that is within the bounds of the game board.
     @param row the row index of the cell to check
     @param col the column index of the cell to check
     @return true if the cell is within the bounds of the game board, false otherwise
     */
    public boolean isInBoundCell(int row, int col) {
        return (row >= 0 && col >= 0 && row < numRows && col < numCols);
    }



    /**
     Overrides the equals method to check for equality between two HoppersConfig objects.
     @param other the object to compare for equality
     @return true if the two objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true; // Same object reference, hence equal
        }
        if (other instanceof HoppersConfig o) {
            if (this.numRows != o.numRows || this.numCols != o.numCols) {
                return false; // Different dimensions, hence not equal
            }
            for (int r = 0; r < this.numRows; r++) {
                for (int c = 0; c < this.numCols; c++) {
                    if (!this.grid[r][c].equals(o.grid[r][c])) {
                        return false; // Elements at the same position are not equal, hence not equal
                    }
                }
            }
            return true; // Everything matched, hence equal
        }
        return false; // Not an instance of HoppersConfig, hence not equal
    }


    /**
     Returns the hash code value for this object, which is computed as the hash code of the string representation
     of this object.
     @return the hash code value for this object
     */
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    /**
     Returns a string representation of the current configuration, where each cell is separated by a space and each
     row is separated by a newline character.
     @return a string representation of the current configuration
     */
    @Override
    public String toString() {
        StringBuilder layout = new StringBuilder();
        for (int r = 0; r < this.numRows; r++) {
            for (int c = 0; c < this.numCols; c++) {
                if (c != 0) {
                    layout.append(" ");
                }
                layout.append(this.grid[r][c]);
            }
            if (r != this.numRows - 1) {
                layout.append("\n");
            }
        }
        return layout.toString();
    }

    /**
     * Returns the number of columns in the grid of this Hoppers configuration.
     *
     * @return the number of columns in the grid
     */
    public int getNumCols() {
        return numCols;
    }

    /**
     * Returns the number of rows in the Hoppers board.
     *
     * @return the number of rows
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     * Returns the grid of the Hoppers configuration.
     *
     * @return the grid of the Hoppers configuration as a two-dimensional array of strings,
     *         where each string represents the content of the corresponding cell
     */
    public String[][] getGrid(){
        return this.grid;
    }
}
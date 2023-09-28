package puzzles.chess.model;

import puzzles.common.Observer;
import puzzles.common.solver.Solver;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import static puzzles.chess.model.ChessConfig.PIECES;

/**
 * @author gonzaloestrella
 */

public class ChessModel {
    /** the collection of observers of this model */
    private final List<Observer<ChessModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private ChessConfig currentConfig;
    private ChessConfig initialConfig;

    private boolean isSelected;
    private int rowSelected;
    private int colSelected;
    public final String HORI_WALL = "-";
    private String filename;
    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<ChessModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }

    /**
     * toString override
     * @return String
     */
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder("   ");
        for (int col=0; col< currentConfig.getColumns(); ++col) {
            result.append(col).append(" ");
        }
        result.append(System.lineSeparator()).append("  ");
        result.append(HORI_WALL.repeat(Math.max(0, currentConfig.getColumns()*2-1)));
        result.append(System.lineSeparator());
        for(int y=0; y< this.currentConfig.getRows(); y++){
            result.append(y + "| ");
            for(int x=0; x< this.currentConfig.getColumns(); x++){
                result.append(currentConfig.getInitialConfiguration()[y][x] + " ");
            }
            if(y < this.currentConfig.getRows()){
                result.append("\n");
            }
        }
        return result.toString();
    }

    /**
     * ChessModel constructor
     * @param filename String
     * @throws IOException
     */
    public ChessModel(String filename) throws IOException {
        this.initialConfig = new ChessConfig(filename);
        this.currentConfig = new ChessConfig(filename);
        this.isSelected = false;
        this.rowSelected =0;
        this.colSelected =0;
    }

    /**
     * resets the board
     */
    public void reset(){
        this.currentConfig = initialConfig;
        this.alertObservers("Puzzle reset!");
    }

    /**
     * loads the file
     * @param filename String
     * @throws IOException
     */
    public void load(String filename) throws IOException {
        this.initialConfig = new ChessConfig(filename);
        this.currentConfig = this.initialConfig;
        this.alertObservers("Loaded: " + filename);
    }

    /**
     * calls bfs and returns a hint for the current configuration
     */
    public void hint(){
        if(this.currentConfig.isSolution()){
            this.alertObservers("Already Solved!");
        }else{
            Solver solver = new Solver();
            if(solver.bfs(currentConfig).isEmpty()){
                this.alertObservers("No solution.");
            }else{
                currentConfig = (ChessConfig) solver.bfs(currentConfig).get(1);
                this.alertObservers("Next Step!");
            }
        }
    }

    /**
     * getter for rows
     * @return
     */
    public int getRows(){
        return initialConfig.getRows();
    }

    /**
     * getter for cols
     * @return
     */
    public int getCol(){
        return initialConfig.getColumns();
    }

    /**
     * getter current config
     * @return
     */
    public ChessConfig getCurrentConfig(){
        return currentConfig;
    }

    /**
     * checks if it is a valid capture if it is it simulates the capturing
     * @param piece char
     * @param row row
     * @param col int
     */
    public void validCapture(char piece, int row, int col){
        if(piece == 'P'){
            if(this.rowSelected-1 == row && this.colSelected -1 == col){
                this.currentConfig.setCell('P', row, col);
                this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
            } else if (this.rowSelected-1 == row && this.colSelected+1 ==col) {

                // 1,1  -1,-1  1,-1   -1,1
                this.currentConfig.setCell('P', row, col);
                this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
            }else{
                this.alertObservers("Can't capture from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
            }
        } else if( piece == 'K') {
            boolean found = false;
            for(int i=0; i < 3; i++){
                if(this.rowSelected-1 == row && (this.colSelected-1)+i == col){
                    this.currentConfig.setCell('K', row, col);
                    this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                    this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
                    found = true;
                    break;
                }else if(this.rowSelected+1 == row && (this.colSelected-1)+i == col){
                    this.currentConfig.setCell('K', row, col);
                    this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                    this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
                    found = true;
                    break;
                }
            }
            if(!found){
                if(this.rowSelected == row && this.colSelected-1 == col){
                    this.currentConfig.setCell('K', row, col);
                    this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                    this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
                }else if(this.rowSelected == row && this.colSelected+1 == col){
                    this.currentConfig.setCell('K', row, col);
                    this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                    this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
                }else{
                    this.alertObservers("Can't capture from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
                }
            }
        } else if( piece == 'R') {
            boolean found = false;
            for(int i = this.colSelected + 1; i < this.currentConfig.getColumns(); i ++){
                if(PIECES.contains(this.currentConfig.getInitialConfiguration()[this.rowSelected][i]) && this.rowSelected == row && i == col){
                    this.currentConfig.setCell('R', row, col);
                    this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                    this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
                    found = true;
                    break;
                } else if(PIECES.contains(this.currentConfig.getInitialConfiguration()[this.rowSelected][i])) {
                    break;
                }
            }
            for(int i = this.colSelected - 1; i >= 0; i --){
                if(PIECES.contains(this.currentConfig.getInitialConfiguration()[this.rowSelected][i]) && this.rowSelected == row && i== col){
                    this.currentConfig.setCell('R', row, col);
                    this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                    this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
                    found = true;
                    break;
                } else if(PIECES.contains(this.currentConfig.getInitialConfiguration()[this.rowSelected][i])) {
                    break;
                }
            }
            for(int i = this.rowSelected + 1; i < this.currentConfig.getRows(); i++){
                if(PIECES.contains(this.currentConfig.getInitialConfiguration()[i][this.colSelected]) && i == row && this.colSelected== col){
                    this.currentConfig.setCell('R', row, col);
                    this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                    this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
                    found = true;
                    break;
                } else if(PIECES.contains(this.currentConfig.getInitialConfiguration()[this.rowSelected][i])) {
                    break;
                }
            }
            for(int i = this.rowSelected - 1; i >= 0; i--){
                if(PIECES.contains(this.currentConfig.getInitialConfiguration()[i][this.colSelected]) && i == row && this.colSelected== col){
                    this.currentConfig.setCell('R', row, col);
                    this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                    this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
                    found = true;
                    break;
                } else if(PIECES.contains(this.currentConfig.getInitialConfiguration()[this.rowSelected][i])) {
                    break;
                }
            }
            if(!found){
                this.alertObservers("Can't capture from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
            }
        } else if( piece == 'B') {
            boolean found = false;
            int fr = 0;
            if(this.currentConfig.getColumns() > this.currentConfig.getRows()){
                fr = this.currentConfig.getColumns();
            }else{
                fr = this.currentConfig.getRows();
            }

            for(int i=1; i < fr; i++){
                if(currentConfig.inBounds(this.rowSelected+i, this.colSelected+i)){
                    if(PIECES.contains(this.currentConfig.getInitialConfiguration()[this.rowSelected+i][this.colSelected+i])){
                        if(this.rowSelected+i == row && this.colSelected+i == col){
                            this.currentConfig.setCell('B', row, col);
                            this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                            this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
                            found = true;
                            break;
                        }
                    }
                }else{
                    break;
                }
            }
            for(int i=1; i < fr; i++){
                if(currentConfig.inBounds(this.rowSelected-i, this.colSelected-i)){
                    if(PIECES.contains(this.currentConfig.getInitialConfiguration()[this.rowSelected-i][this.colSelected-i])){
                        if(this.rowSelected-i == row && this.colSelected-i == col){
                            this.currentConfig.setCell('B', row, col);
                            this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                            this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
                            found = true;
                            break;
                        }
                    }
                }else{
                    break;
                }
            }
            for(int i=1; i < fr; i++){
                if(currentConfig.inBounds(this.rowSelected-i, this.colSelected+i)){
                    if(PIECES.contains(this.currentConfig.getInitialConfiguration()[this.rowSelected-i][this.colSelected+i])){
                        if(this.rowSelected-i == row && this.colSelected+i == col){
                            this.currentConfig.setCell('B', row, col);
                            this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                            this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
                            found = true;
                            break;
                        }
                    }
                }else{
                    break;
                }
            }
            for(int i=1; i < fr; i++){
                if(currentConfig.inBounds(this.rowSelected+i, this.colSelected-i)){
                    if(PIECES.contains(this.currentConfig.getInitialConfiguration()[this.rowSelected+i][this.colSelected-i])){
                        if(this.rowSelected+i == row && this.colSelected-i == col){
                            this.currentConfig.setCell('B', row, col);
                            this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                            this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
                            found = true;
                            break;
                        }
                    }
                }else{
                    break;
                }
            }
            if(!found){
                this.alertObservers("Can't capture from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
            }
        } else if( piece == 'N') {
            if(this.rowSelected - 2 == row && this.colSelected-1== col){
                this.currentConfig.setCell('N', row, col);
                this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
            }else if(this.rowSelected - 2 == row && this.colSelected+1== col){
                this.currentConfig.setCell('N', row, col);
                this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
            }else if(this.rowSelected + 2 == row && this.colSelected-1== col){
                this.currentConfig.setCell('N', row, col);
                this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
            }else if(this.rowSelected + 2 == row && this.colSelected+1== col){
                this.currentConfig.setCell('N', row, col);
                this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
            }else if(this.rowSelected - 1 == row && this.colSelected+2== col){
                this.currentConfig.setCell('N', row, col);
                this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
            }else if(this.rowSelected - 1 == row && this.colSelected-2== col){
                this.currentConfig.setCell('N', row, col);
                this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
            }else if(this.rowSelected + 1 == row && this.colSelected+2== col){
                this.currentConfig.setCell('N', row, col);
                this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
            }else if(this.rowSelected + 1 == row && this.colSelected-2== col){
                this.currentConfig.setCell('N', row, col);
                this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
            }else{
                this.alertObservers("Can't capture from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
            }
        } else if( piece == 'Q') {
            boolean found = false;
            for(int i = this.colSelected + 1; i < this.currentConfig.getColumns(); i ++){
                if(PIECES.contains(this.currentConfig.getInitialConfiguration()[this.rowSelected][i]) && this.rowSelected == row && i == col){
                    this.currentConfig.setCell('Q', row, col);
                    this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                    this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
                    found = true;
                    break;
                } else if(PIECES.contains(this.currentConfig.getInitialConfiguration()[this.rowSelected][i])) {
                    break;
                }
            }
            for(int i = this.colSelected - 1; i >= 0; i --){
                if(PIECES.contains(this.currentConfig.getInitialConfiguration()[this.rowSelected][i]) && this.rowSelected == row && i== col){
                    this.currentConfig.setCell('Q', row, col);
                    this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                    this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
                    found = true;
                    break;
                } else if(PIECES.contains(this.currentConfig.getInitialConfiguration()[this.rowSelected][i])) {
                    break;
                }
            }
            for(int i = this.rowSelected + 1; i < this.currentConfig.getRows(); i++){
                if(PIECES.contains(this.currentConfig.getInitialConfiguration()[i][this.colSelected]) && i == row && this.colSelected== col){
                    this.currentConfig.setCell('Q', row, col);
                    this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                    this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
                    found = true;
                    break;
                } else if(PIECES.contains(this.currentConfig.getInitialConfiguration()[this.rowSelected][i])) {
                    break;
                }
            }
            for(int i = this.rowSelected - 1; i >= 0; i--){
                if(PIECES.contains(this.currentConfig.getInitialConfiguration()[i][this.colSelected]) && i == row && this.colSelected== col){
                    this.currentConfig.setCell('Q', row, col);
                    this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                    this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
                    found = true;
                    break;
                } else if(PIECES.contains(this.currentConfig.getInitialConfiguration()[this.rowSelected][i])) {
                    break;
                }
            }
            int fr = 0;
            if(this.currentConfig.getColumns() > this.currentConfig.getRows()){
                fr = this.currentConfig.getColumns();
            }else{
                fr = this.currentConfig.getRows();
            }

            for(int i=1; i < fr; i++){
                if(currentConfig.inBounds(this.rowSelected+i, this.colSelected+i)){
                    if(PIECES.contains(this.currentConfig.getInitialConfiguration()[this.rowSelected+i][this.colSelected+i])){
                        if(this.rowSelected+i == row && this.colSelected+i == col){
                            this.currentConfig.setCell('Q', row, col);
                            this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                            this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
                            found = true;
                            break;
                        }
                    }
                }else{
                    break;
                }
            }
            for(int i=1; i < fr; i++){
                if(currentConfig.inBounds(this.rowSelected-i, this.colSelected-i)){
                    if(PIECES.contains(this.currentConfig.getInitialConfiguration()[this.rowSelected-i][this.colSelected-i])){
                        if(this.rowSelected-i == row && this.colSelected-i == col){
                            this.currentConfig.setCell('Q', row, col);
                            this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                            this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
                            found = true;
                            break;
                        }
                    }
                }else{
                    break;
                }
            }
            for(int i=1; i < fr; i++){
                if(currentConfig.inBounds(this.rowSelected-i, this.colSelected+i)){
                    if(PIECES.contains(this.currentConfig.getInitialConfiguration()[this.rowSelected-i][this.colSelected+i])){
                        if(this.rowSelected-i == row && this.colSelected+i == col){
                            this.currentConfig.setCell('Q', row, col);
                            this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                            this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
                            found = true;
                            break;
                        }
                    }
                }else{
                    break;
                }
            }
            for(int i=1; i < fr; i++){
                if(currentConfig.inBounds(this.rowSelected+i, this.colSelected-i)){
                    if(PIECES.contains(this.currentConfig.getInitialConfiguration()[this.rowSelected+i][this.colSelected-i])){
                        if(this.rowSelected+i == row && this.colSelected-i == col){
                            this.currentConfig.setCell('Q', row, col);
                            this.currentConfig.setCell('.', this.rowSelected, this.colSelected);
                            this.alertObservers("Captured from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
                            found = true;
                            break;
                        }
                    }
                }else{
                    break;
                }
            }
            if(!found){
                this.alertObservers("Can't capture from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
            }
        }
        this.isSelected = false;
    }

    /**
     * selects a coordinate, if the coordinate is in bounds then test if the selected coordinate is a piece it stores it, if there is already a selected piece it eats the coordinates
     * @param row int
     * @param col int
     */
    public void select(int row, int col){
        if(this.currentConfig.inBounds(row, col)){
            if(this.isSelected){
                if((PIECES).contains(this.currentConfig.getInitialConfiguration()[row][col])){
                    validCapture(this.currentConfig.getInitialConfiguration()[this.rowSelected][this.colSelected], row, col);
                }else{
                    this.alertObservers("Can't capture from ("+this.rowSelected + ", "+this.colSelected+") to ("+row+", "+col+")");
                }
            }else{
                if((PIECES).contains(this.currentConfig.getInitialConfiguration()[row][col])){
                    this.alertObservers("Selected ("+row +", "+col +")");
                    this.rowSelected = row;
                    this.colSelected = col;
                    this.isSelected = true;
                }else{
                    this.alertObservers("Invalid Selection ("+row + ", "+col+")");
                }
            }
        }else{
            this.alertObservers("Invalid Selection ("+row + ", "+col+")");
        }
    }
}

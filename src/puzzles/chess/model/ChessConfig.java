package puzzles.chess.model;

import puzzles.common.solver.Configuration;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author gonzaloestrella
 */

public class ChessConfig implements Configuration {
    private static int rows;
    private static int columns;
    private char[][] initialConfiguration;
    public final static ArrayList<Character> PIECES = new ArrayList<>(Arrays.asList('B', 'K', 'N', 'P', 'Q', 'R'));

    /**
     * ChessConfig constructor
     * @param filename String
     * @throws IOException
     */
    public ChessConfig(String filename) throws IOException {
        Scanner out = new Scanner(new File(filename));
        String line[] = out.nextLine().split(" ");
        rows = Integer.parseInt(line[0]);
        columns = Integer.parseInt(line[1]);
        this.initialConfiguration = new char[rows][columns];
        for(int y = 0; y < rows; y++){
            line = out.nextLine().split(" ");
            for(int x = 0; x < line.length; x++){
                initialConfiguration[y][x] = line[x].charAt(0);
            }
        }
    }

    /**
     * ChessConfig second constructor
     * @param other ChessConfig
     */
    public ChessConfig(ChessConfig other){
        this.initialConfiguration = new char[rows][columns];
        for(int y = 0; y < rows; y++){
            for(int x =0; x < columns; x++){
                this.initialConfiguration[y][x] = other.initialConfiguration[y][x];
            }
        }
    }

    /**
     * getter for initialConfiguration
     * @return
     */
    public char[][] getInitialConfiguration(){
        return this.initialConfiguration;
    }

    /**
     * setter for cell
     * @param piece char
     * @param row int
     * @param col col
     */
    public void setCell(char piece, int row, int col){
        initialConfiguration[row][col] = piece;
    }

    /**
     * getter for rows
     * @return int
     */
    public int getRows(){
        return rows;
    }

    /**
     * getter for columns
     * @return int
     */
    public int getColumns(){
        return columns;
    }

    /**
     * checks if the instance object is the same as an other object
     * @param other
     * @return boolean
     */
    @Override
    public boolean equals(Object other){
        if(other instanceof  ChessConfig){
            for (int y = 0; y < rows; y++){
                for(int x = 0; x < columns; x++){
                    if(((ChessConfig) other).initialConfiguration[y][x] != this.initialConfiguration[y][x]){
                        return false;
                    }
                }
            }
        }else{
            return false;
        }
        return true;
    }

    /**
     * hashes the double array
     * @return int
     */
    @Override
    public int hashCode(){
        return Arrays.deepHashCode(this.initialConfiguration);
    }

    /**
     * toString
     * @return String
     */
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        result.append("\n");
        for(int y=0; y< rows; y++){
            for(int x=0; x< columns; x++){
                result.append(initialConfiguration[y][x] + " ");
            }
            if(y < rows-1){
                result.append("\n");
            }
        }
        return result.toString();
    }

    /**
     * checks if currentConfiguration is the solution
     * @return
     */
    @Override
    public boolean isSolution() {
        int counter=0;
        for(int y=0; y < rows; y++){
            for(int x=0; x < columns; x++){
                if(PIECES.contains(this.initialConfiguration[y][x])){
                    counter++;
                }
                if(counter>1){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * checks if the row and column are inside the bounds
     * @param row int
     * @param col int
     * @return boolean
     */
    public boolean inBounds(int row , int col){
        return (col >= 0 && col < columns) && (row >= 0 && row < rows);
    }

    /**
     * gets the neighbor of the current configuration
     * @return Collection<Configuration>
     */
    @Override
    public Collection<Configuration> getNeighbors(){
        Collection<Configuration> neighbors = new LinkedList<>();
        for(int y =0; y < rows; y++){
            for(int x =0; x < columns; x++){
                if(this.initialConfiguration[y][x] == '.'){

                }else if(this.initialConfiguration[y][x] == 'P'){
                    if(inBounds(y-1, x-1)){ // left neighbor
                        if(PIECES.contains(this.initialConfiguration[y-1][x-1])){
                            ChessConfig pawn1 = new ChessConfig(this);
                            (pawn1.initialConfiguration)[y][x] ='.';
                            (pawn1.initialConfiguration)[y-1][x-1] = 'P';
                            neighbors.add(pawn1);
                        }
                    }
                    if(inBounds(y-1, x+1)) { //right neighbor
                        if(PIECES.contains(this.initialConfiguration[y-1][x+1])){
                            ChessConfig pawn2 = new ChessConfig(this);
                            (pawn2.initialConfiguration)[y][x] ='.';
                            (pawn2.initialConfiguration)[y-1][x+1] = 'P';
                            neighbors.add(pawn2);
                        }
                    }
                }else if(this.initialConfiguration[y][x] == 'K'){
                    for(int i=0; i < 3; i++){
                        if(inBounds(y-1, (x-1)+i)){
                            if(PIECES.contains(this.initialConfiguration[y-1][(x-1)+i])){
                                ChessConfig king1 = new ChessConfig(this);
                                (king1.initialConfiguration)[y][x] ='.';
                                (king1.initialConfiguration)[y-1][(x-1)+i] = 'K';
                                neighbors.add(king1);
                            }
                        }
                        if(inBounds(y+1, (x-1)+i)){
                            if(PIECES.contains(this.initialConfiguration[y+1][(x-1)+i])){
                                ChessConfig king2 = new ChessConfig(this);
                                (king2.initialConfiguration)[y][x] ='.';
                                (king2.initialConfiguration)[y+1][(x-1)+i] = 'K';
                                neighbors.add(king2);
                            }
                        }
                    }
                    if(inBounds(y, x-1)){
                        if(PIECES.contains(this.initialConfiguration[y][x-1])){
                            ChessConfig king3 = new ChessConfig(this);
                            (king3.initialConfiguration)[y][x] ='.';
                            (king3.initialConfiguration)[y][x-1] = 'K';
                            neighbors.add(king3);
                        }
                    }
                    if(inBounds(y, x+1)){
                        if(PIECES.contains(this.initialConfiguration[y][x+1])){
                            ChessConfig king4 = new ChessConfig(this);
                            (king4.initialConfiguration)[y][x] ='.';
                            (king4.initialConfiguration)[y][x+1] = 'K';
                            neighbors.add(king4);
                        }
                    }
                }else if(this.initialConfiguration[y][x] == 'R'){
                    for(int i = x+1; i < columns; i++){
                        if(PIECES.contains(this.initialConfiguration[y][i])){
                            ChessConfig rook1 = new ChessConfig(this);
                            (rook1.initialConfiguration)[y][x] ='.';
                            (rook1.initialConfiguration)[y][i] = 'R';
                            neighbors.add(rook1);
                            break;
                        }
                    }
                    for(int i = x-1; i >= 0; i--){
                        if(PIECES.contains(this.initialConfiguration[y][i])){
                            ChessConfig rook2 = new ChessConfig(this);
                            (rook2.initialConfiguration)[y][x] ='.';
                            (rook2.initialConfiguration)[y][i] = 'R';
                            neighbors.add(rook2);
                            break;
                        }
                    }
                    for(int i = y+1; i < rows; i++){
                        if(PIECES.contains(this.initialConfiguration[i][x])){
                            ChessConfig rook3 = new ChessConfig(this);
                            (rook3.initialConfiguration)[y][x] ='.';
                            (rook3.initialConfiguration)[i][x] = 'R';
                            neighbors.add(rook3);
                            break;
                        }
                    }
                    for(int i = y-1; i >= 0; i--){
                        if(PIECES.contains(this.initialConfiguration[i][x])){
                            ChessConfig rook4 = new ChessConfig(this);
                            (rook4.initialConfiguration)[y][x] ='.';
                            (rook4.initialConfiguration)[i][x] = 'R';
                            neighbors.add(rook4);
                            break;
                        }
                    }
                }else if(this.initialConfiguration[y][x] == 'B'){
                    int fr = 0;
                    if(columns > rows){
                        fr = columns;
                    }else{
                        fr = rows;
                    }
                    for(int i=1; i < fr; i++){
                        if(inBounds(y-i, x-i)){
                            if(PIECES.contains(this.initialConfiguration[y -i][x -i])){
                                ChessConfig bishop1 = new ChessConfig(this);
                                (bishop1.initialConfiguration)[y][x] ='.';
                                (bishop1.initialConfiguration)[y-i][x-i] = 'B';
                                neighbors.add(bishop1);
                                break;
                            }
                        }else{
                            break;
                        }
                    }
                    for(int i=1; i < fr; i++){
                        if(inBounds(y-i, x+i)){
                            if(PIECES.contains(this.initialConfiguration[y-i][x+i])){
                                ChessConfig bishop2 = new ChessConfig(this);
                                (bishop2.initialConfiguration)[y][x] ='.';
                                (bishop2.initialConfiguration)[y-i][x+i] = 'B';
                                neighbors.add(bishop2);
                                break;
                            }
                        }else{
                            break;
                        }
                    }
                    for(int i=1; i < fr; i++){
                        if(inBounds(y+i, x-i)){
                            if(PIECES.contains(this.initialConfiguration[y+i][x-i])){
                                ChessConfig bishop3 = new ChessConfig(this);
                                (bishop3.initialConfiguration)[y][x] ='.';
                                (bishop3.initialConfiguration)[y+i][x-i] = 'B';
                                neighbors.add(bishop3);
                                break;
                            }
                        }else{
                            break;
                        }
                    }
                    for(int i=1; i < fr; i++){
                        if(inBounds(y+i, x+i)){
                            if(PIECES.contains(this.initialConfiguration[y+i][x+i])){
                                ChessConfig bishop4 = new ChessConfig(this);
                                (bishop4.initialConfiguration)[y][x] ='.';
                                (bishop4.initialConfiguration)[y+i][x+i] = 'B';
                                neighbors.add(bishop4);
                                break;
                            }
                        }else{
                            break;
                        }
                    }
                }else if(this.initialConfiguration[y][x] == 'N'){
                    if(inBounds(y-2, x-1)){
                        if(PIECES.contains(this.initialConfiguration[y-2][x-1])){
                            ChessConfig knight1 = new ChessConfig(this);
                            (knight1.initialConfiguration)[y][x] ='.';
                            (knight1.initialConfiguration)[y-2][x-1] = 'N';
                            neighbors.add(knight1);
                        }
                    }
                    if(inBounds(y-2, x+1)){
                        if(PIECES.contains(this.initialConfiguration[y-2][x+1])){
                            ChessConfig knight2 = new ChessConfig(this);
                            (knight2.initialConfiguration)[y][x] ='.';
                            (knight2.initialConfiguration)[y-2][x+1] = 'N';
                            neighbors.add(knight2);
                        }
                    }
                    if(inBounds(y+2, x-1)){
                        if(PIECES.contains(this.initialConfiguration[y+2][x-1])){
                            ChessConfig knight3 = new ChessConfig(this);
                            (knight3.initialConfiguration)[y][x] ='.';
                            (knight3.initialConfiguration)[y+2][x-1] = 'N';
                            neighbors.add(knight3);
                        }
                    }
                    if(inBounds(y+2, x+1)){
                        if(PIECES.contains(this.initialConfiguration[y+2][x +1])){
                            ChessConfig knight4 = new ChessConfig(this);
                            (knight4.initialConfiguration)[y][x] ='.';
                            (knight4.initialConfiguration)[y +2][x +1] = 'N';
                            neighbors.add(knight4);
                        }
                    }
                    if(inBounds(y-1, x-2)){
                        if(PIECES.contains(this.initialConfiguration[y-1][x-2])){
                            ChessConfig knight5 = new ChessConfig(this);
                            (knight5.initialConfiguration)[y][x] ='.';
                            (knight5.initialConfiguration)[y-1][x-2] = 'N';
                            neighbors.add(knight5);
                        }
                    }
                    if(inBounds(y+1, x-2)){
                        if(PIECES.contains(this.initialConfiguration[y+1][x-2])){
                            ChessConfig knight6 = new ChessConfig(this);
                            (knight6.initialConfiguration)[y][x] ='.';
                            (knight6.initialConfiguration)[y+1][x-2] = 'N';
                            neighbors.add(knight6);
                        }
                    }
                    if(inBounds(y-1, x+2)){
                        if(PIECES.contains(this.initialConfiguration[y-1][x+2])){
                            ChessConfig knight7 = new ChessConfig(this);
                            (knight7.initialConfiguration)[y][x] ='.';
                            (knight7.initialConfiguration)[y-1][x+2] = 'N';
                            neighbors.add(knight7);
                        }
                    }
                    if(inBounds(y+1, x+2)){
                        if(PIECES.contains(this.initialConfiguration[y+1][x+2])){
                            ChessConfig knight8 = new ChessConfig(this);
                            (knight8.initialConfiguration)[y][x] ='.';
                            (knight8.initialConfiguration)[y+1][x+2] = 'N';
                            neighbors.add(knight8);
                        }
                    }
                }else if(this.initialConfiguration[y][x] == 'Q'){
                    for(int i = x+1; i < columns; i++){
                        if(PIECES.contains(this.initialConfiguration[y][i])){
                            ChessConfig rook1 = new ChessConfig(this);
                            (rook1.initialConfiguration)[y][x] ='.';
                            (rook1.initialConfiguration)[y][i] = 'Q';
                            neighbors.add(rook1);
                            break;
                        }
                    }
                    for(int i = x-1; i >= 0; i--){
                        if(PIECES.contains(this.initialConfiguration[y][i])){
                            ChessConfig rook2 = new ChessConfig(this);
                            (rook2.initialConfiguration)[y][x] ='.';
                            (rook2.initialConfiguration)[y][i] = 'Q';
                            neighbors.add(rook2);
                            break;
                        }
                    }
                    for(int i = y+1; i < rows; i++){
                        if(PIECES.contains(this.initialConfiguration[i][x])){
                            ChessConfig rook3 = new ChessConfig(this);
                            (rook3.initialConfiguration)[y][x] ='.';
                            (rook3.initialConfiguration)[i][x] = 'Q';
                            neighbors.add(rook3);
                            break;
                        }
                    }
                    for(int i = y-1; i >= 0; i--){
                        if(PIECES.contains(this.initialConfiguration[i][x])){
                            ChessConfig rook4 = new ChessConfig(this);
                            (rook4.initialConfiguration)[y][x] ='.';
                            (rook4.initialConfiguration)[i][x] = 'Q';
                            neighbors.add(rook4);
                            break;
                        }
                    }
                    int fr = 0;
                    if(columns > rows){
                        fr = columns;
                    }else{
                        fr = rows;
                    }
                    for(int i=1; i < fr; i++){
                        if(inBounds(y-i, x-i)){
                            if(PIECES.contains(this.initialConfiguration[y-i][x-i])){
                                ChessConfig bishop1 = new ChessConfig(this);
                                (bishop1.initialConfiguration)[y][x] ='.';
                                (bishop1.initialConfiguration)[y-i][x-i] = 'Q';
                                neighbors.add(bishop1);
                                break;
                            }
                        }else{
                            break;
                        }
                    }
                    for(int i=1; i < fr; i++){
                        if(inBounds(y-i, x+i)){
                            if(PIECES.contains(this.initialConfiguration[y-i][x+i])){
                                ChessConfig bishop2 = new ChessConfig(this);
                                (bishop2.initialConfiguration)[y][x] ='.';
                                (bishop2.initialConfiguration)[y-i][x+i] = 'Q';
                                neighbors.add(bishop2);
                                break;
                            }
                        }else{
                            break;
                        }
                    }
                    for(int i=1; i < fr; i++){
                        if(inBounds(y+i, x-i)){
                            if(PIECES.contains(this.initialConfiguration[y+i][x-i])){
                                ChessConfig bishop3 = new ChessConfig(this);
                                (bishop3.initialConfiguration)[y][x] ='.';
                                (bishop3.initialConfiguration)[y+i][x-i] = 'Q';
                                neighbors.add(bishop3);
                                break;
                            }
                        }else{
                            break;
                        }
                    }
                    for(int i=1; i < fr; i++){
                        if(inBounds(y+i, x+i)){
                            if(PIECES.contains(this.initialConfiguration[y+i][x+i])){
                                ChessConfig bishop4 = new ChessConfig(this);
                                (bishop4.initialConfiguration)[y][x] ='.';
                                (bishop4.initialConfiguration)[y+i][x+i] = 'Q';
                                neighbors.add(bishop4);
                                break;
                            }
                        }else{
                            break;
                        }
                    }
                }
            }
            }
        return neighbors;
    }
}

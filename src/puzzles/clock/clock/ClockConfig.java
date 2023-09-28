package puzzles.clock.clock;

import puzzles.common.solver.Configuration;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

/**
 * @author gonzaloestrella
 */

public class ClockConfig implements Configuration {
    private final int hours;
    private final int start;
    private final int end;

    /**
     * constructor for ClockConfig
     * @param hours int
     * @param start int
     * @param end int
     */
    public ClockConfig(int hours, int start, int end){
        this.hours = hours;
        this.start = start;
        this.end = end;
    }

    /**
     * getter for hours
     * @return int
     */
    public int getHours(){
        return this.hours;
    }

    /**
     * getter for start
     * @return int
     */
    public int getStart(){
        return this.start;
    }

    /**
     * getter for end
     * @return int
     */
    public int getEnd(){
        return this.end;
    }

    /**
     * checks if the start for this configuration is the end
     * @return boolean
     */
    @Override
    public boolean isSolution() {
        return this.start == this.end;
    }

    /**
     * gets the neighbors for the current configuration
     * @return LinkedList<Configuration>
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        LinkedList<Configuration> neighbors = new LinkedList<>();
        if(this.start == this.hours){
            neighbors.add(new ClockConfig(this.hours, 1, this.end));
            neighbors.add(new ClockConfig(this.hours, this.start-1, this.end));
        }else if(this.start == 1){
            neighbors.add(new ClockConfig(this.hours, this.start+1, this.end));
            neighbors.add(new ClockConfig(this.hours, this.hours, this.end));
        }else{
            neighbors.add(new ClockConfig(this.hours, this.start+1, this.end));
            neighbors.add(new ClockConfig(this.hours, this.start-1, this.end));
        }
        return neighbors;
    }

    /**
     * checks if the current instance is equal to another object
     * @param other Object
     * @return boolean
     */
    @Override
    public boolean equals(Object other) {
        if(other instanceof ClockConfig){
            if(((ClockConfig) other).hours == this.hours && ((ClockConfig) other).start == this.start && ((ClockConfig) other).end == this.end){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    /**
     * creates a hash code for the current instance
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.hours, this.start, this.end);
    }

    /**
     * object to string
     * @return String
     */
    @Override
    public String toString() {
        return "Hours: "+ this.hours + " Start: "+this.start+" End: "+ this.end;
    }
}

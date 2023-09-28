package puzzles.strings;

import puzzles.common.solver.Configuration;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author gonzaloestrella
 */

public class StringsConfig implements Configuration {
    private String initialConfiguration;
    private String finalConfiguration;

    /**
     * constructor for StringConfig
     * @param initialConfiguration String
     * @param finalConfiguration String
     */
    public StringsConfig(String initialConfiguration, String finalConfiguration){
        this.initialConfiguration = initialConfiguration;
        this.finalConfiguration = finalConfiguration;
    }

    /**
     * checks if the current instance is the solution
     * @return boolean
     */
    @Override
    public boolean isSolution() {
        return this.initialConfiguration.equals(this.finalConfiguration);
    }

    /**
     * gets the neighbors of the current instance
     * @return LinkedList<Configuration>
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        LinkedList<Configuration> neighbors = new LinkedList<>();
        for(int i=0; i < initialConfiguration.length(); i++){
            char[] config1 = initialConfiguration.toCharArray();
            char[] config2 = initialConfiguration.toCharArray();
            if(config1[i] == 'Z'){
                config1[i] = 'A';
                config2[i] --;
                neighbors.add(new StringsConfig(String.valueOf(config1), this.finalConfiguration));
                neighbors.add(new StringsConfig(String.valueOf(config2), this.finalConfiguration));
            }else  if(config1[i] == 'A'){
                config1[i] = 'Z';
                config2[i] ++;
                neighbors.add(new StringsConfig(String.valueOf(config1), this.finalConfiguration));
                neighbors.add(new StringsConfig(String.valueOf(config2), this.finalConfiguration));
            }else{
                config1[i]++;
                config2[i]--;
                neighbors.add(new StringsConfig(String.valueOf(config1), this.finalConfiguration));
                neighbors.add(new StringsConfig(String.valueOf(config2), this.finalConfiguration));
            }
        }
        return neighbors;
    }

    /**
     * checks if the current instance of StringConfig equals another Object
     * @param other
     * @return
     */
    @Override
    public boolean equals(Object other) {
        if(other instanceof StringsConfig){
            return ((StringsConfig) other).initialConfiguration.equals(this.initialConfiguration) && ((StringsConfig) other).finalConfiguration.equals(this.finalConfiguration);
        }else{
            return false;
        }
    }

    /**
     * generates a hash code for the current instance
     * @return
     */
    @Override
    public int hashCode() {
        return this.initialConfiguration.hashCode();
    }

    /**
     * to string of StringConfiguration
     * @return
     */
    @Override
    public String toString() {
        return "Start: "+ this.initialConfiguration + ", End: "+ this.finalConfiguration;
    }

    /**
     * getter for start
     * @return
     */
    public String getStart(){
        return initialConfiguration;
    }
}

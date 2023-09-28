package puzzles.strings;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.LinkedList;

/**
 * @author gonzaloestrella
 */

public class Strings {

    /**
     * main for String puzzle
     * @param args
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println(("Usage: java Strings start finish"));
        } else {
            String initialConfigString = args[0];
            String finalConfigString = args[1];
            StringsConfig initialConfig = new StringsConfig(initialConfigString, finalConfigString);
            Solver solver = new Solver();
            System.out.println(initialConfig);
            LinkedList<Configuration> path= solver.solve(initialConfig);
            if(path.size() == 0){
                System.out.println("No solution");
            }else{
                for(int i = 0; i < path.size(); i++){
                    System.out.println("Step "+ i+": "+((StringsConfig) path.get(i)).getStart());
                }
            }
        }
    }
}

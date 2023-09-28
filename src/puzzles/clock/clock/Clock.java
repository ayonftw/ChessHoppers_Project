package puzzles.clock.clock;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.LinkedList;

/**
 * @author gonzaloestrella
 */

public class Clock {

    /**
     * main for the Clock Puzzle
     * @param args
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println(("Usage: java Clock start stop"));
        } else {
            int hours = Integer.parseInt(args[0]);
            int start = Integer.parseInt(args[1]);
            int end = Integer.parseInt(args[2]);
            ClockConfig initialConfig = new ClockConfig(hours, start, end);
            Solver solver = new Solver();
            System.out.println(initialConfig);
            LinkedList<Configuration> path= solver.solve(initialConfig);
            if(path.size() <= 0){
                System.out.println("No solution");
            }else{
                for(int i = 0; i < path.size(); i++){
                    System.out.println("Step "+ i+": "+((ClockConfig) path.get(i)).getStart());
                }
            }

        }
    }
}

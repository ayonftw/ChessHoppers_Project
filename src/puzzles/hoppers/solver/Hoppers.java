package puzzles.hoppers.solver;
import puzzles.clock.ClockConfig;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Aniruddha Roy
 * ar7475
 */

public class Hoppers {

    /**
     The main method of the Hoppers puzzle solver program.
     Takes in a file containing the initial configuration of the Hoppers board as a command line argument.
     Prints out the solution path if one exists, or "No solution" otherwise.
     @param args the command line arguments passed to the program
     @throws IOException if an I/O error occurs while reading the input file
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Hoppers filename");
        }

        HoppersConfig config = new HoppersConfig(args[0]);
        Solver solver = new Solver();
        System.out.println(config);

        LinkedList<Configuration> path = solver.solve(config);
        int steps = 0;
        if (path.isEmpty( )) {
            System.out.println("No solution");
        } else {
            for (Configuration configuration : path) {
                System.out.println("Step "+ steps++ +":" );
                System.out.println(configuration.toString()); // print each configuration in the path
                System.out.println();
            }
        }
    }
}
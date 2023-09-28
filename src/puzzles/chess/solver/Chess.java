package puzzles.chess.solver;

import puzzles.chess.model.ChessConfig;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import java.io.IOException;
import java.util.LinkedList;

public class Chess {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Chess filename");
        }else{
            ChessConfig initialConfig = new ChessConfig(args[0]);
            System.out.print(args[0]);
            System.out.println(initialConfig);
            Solver solver = new Solver();
            LinkedList<Configuration> path= solver.solve(initialConfig);
            if(path.size() <= 0){
                System.out.println("No solution");
            }else{
                for(int i = 0; i < path.size(); i++){
                    System.out.println("Step "+ i+": "+((ChessConfig) path.get(i))+"\n");
                }
            }
        }
    }
}

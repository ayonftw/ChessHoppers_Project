package puzzles.common.solver;

import java.util.*;

/**
 * @author Gonzalo Estrella && Aniruddha Roy
 */

public class Solver {

    /**
     * Solver constructor
     */
    public Solver(){
    }

    /**
     * Uses BFS to get the shortest path to the solution;
     * @param initialConfiguration
     * @return
     */
    public LinkedList<Configuration> solve(Configuration initialConfiguration){
        int totalConfigs = 1;
        int uniqueConfigs = 1;
        LinkedList<Configuration> path = new LinkedList<>();
        List<Configuration> queue = new LinkedList<>();
        Map<Configuration, Configuration> predecessors = new HashMap<>();
        queue.add(initialConfiguration);
        predecessors.put(initialConfiguration, initialConfiguration);
        while (!queue.isEmpty()) {
            Configuration current = queue.remove(0);
            uniqueConfigs++;
            if (current.isSolution()) {
                if(predecessors.containsKey(current)) {
                    Configuration currNode = current;
                    while (!currNode.equals(initialConfiguration)) {
                        path.add(0, currNode);
                        currNode = predecessors.get(currNode);
                    }
                    path.add(0, initialConfiguration);
                }
                break;
            }
            Collection<Configuration> neighbors = current.getNeighbors();
            for (Configuration nbr : neighbors) {
                if(!predecessors.containsKey(nbr)) {
                    predecessors.put(nbr, current);
                    queue.add(nbr);
                }
                totalConfigs++;
            }
        }
        System.out.println("Total configs: "+ totalConfigs);
        System.out.println("Unique configs: "+ uniqueConfigs);
        return path;
    }

    public LinkedList<Configuration> bfs(Configuration initialConfiguration){
        LinkedList<Configuration> path = new LinkedList<>();
        List<Configuration> queue = new LinkedList<>();
        Map<Configuration, Configuration> predecessors = new HashMap<>();
        queue.add(initialConfiguration);
        predecessors.put(initialConfiguration, initialConfiguration);
        while (!queue.isEmpty()) {
            Configuration current = queue.remove(0);
            if (current.isSolution()) {
                if(predecessors.containsKey(current)) {
                    Configuration currNode = current;
                    while (!currNode.equals(initialConfiguration)) {
                        path.add(0, currNode);
                        currNode = predecessors.get(currNode);
                    }
                    path.add(0, initialConfiguration);
                }
                break;
            }
            Collection<Configuration> neighbors = current.getNeighbors();
            for (Configuration nbr : neighbors) {
                if(!predecessors.containsKey(nbr)) {
                    predecessors.put(nbr, current);
                    queue.add(nbr);
                }
            }
        }
        return path;
    }
}
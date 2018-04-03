import ACO.ACO_TSP;
import GA.TSP_GA;

import java.io.File;


/**
 * The TSPLauncher class is used to call the different algorithm engines in order to run and give the shortest Route
 * length that it finds , for each algorithm. It also asks the user at which parameters he wants the algorithm to run. When
 * asking for these values , the best values are displayed to the user in order to guide him through the use of the
 * algorithm.
 */

public class TSPLauncher {
    public static void main(String args[]) {
        //TSP_GA geneticAlgorithm = new TSP_GA(0.01,30,0.9,0.1,
                //new File("./burma14.tsp"));
        //System.out.println("The shortest path is :"+geneticAlgorithm.GA_Engine());

        ACO_TSP aco = new ACO_TSP();
        System.out.println("Shortest Route has length "+aco.ACO_Engine());
    }

}


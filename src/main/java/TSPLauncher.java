import GA.TSP_GA;
import TSP_Graph.City;
import TSP_Graph.Graph;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Dylan Galea on 28/02/2018.
 */
public class TSPLauncher {
    public static void main(String args[]) {
        TSP_GA geneticAlgorithm = new TSP_GA();
        System.out.println("The shortest path is :"+geneticAlgorithm.GA_Engine());
    }

}


import GA.TSP_GA;
import ACO.ACO_TSP;

/**
 * Created by Dylan Galea on 28/02/2018.
 */
public class TSPLauncher {
    public static void main(String args[]) {
        //TSP_GA geneticAlgorithm = new TSP_GA();
        //System.out.println("The shortest path is :"+geneticAlgorithm.GA_Engine());

        ACO_TSP aco = new ACO_TSP();
        System.out.println("Shortest Route has length "+aco.ACO_Engine());
    }

}


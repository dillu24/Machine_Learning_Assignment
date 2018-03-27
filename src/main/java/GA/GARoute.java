package GA;

import TSP_Graph.Graph;
import java.util.ArrayList;

/**
 * The GARoute class encodes the representation of the GA Chromosome which was chosen to be a permutation of city ID's
 */

public class GARoute {
    /**
     * The cityIndex array list stores the permutation representation of the cities , i.e the representation of the GA's
     * chromosome . It contains the index of each city in the list of cities of the class Graph.
     * The fitnessScore variable stores the fitness value of the individual given by the fitness function.
     */
    ArrayList<Integer> cityIndex;
    double fitnessScore;

    /**
     * The GARoute default constructor gives memory to an empty array list
     */

    GARoute(){
        cityIndex = new ArrayList<Integer>();
    }


    /**
     * This method calculates the route's length in the passed graph as parameter
     * @param g
     * Stores the graph that the route will be computed on
     * @return
     * The route length
     */

    double getRouteCost(Graph g){
        double totalCost = 0;
        double matrixWeights[][] = g.getMatrixOfWeights(); //get the matrix of weights from the class Graph
        for(int i=0;i<cityIndex.size()-1;i++){
            //the cost is calculated by computing the route from one index to the next since the representation is
            // a permutation
            totalCost = totalCost + matrixWeights[cityIndex.get(i)][cityIndex.get(i+1)];
        }
        totalCost = totalCost+matrixWeights[cityIndex.get(cityIndex.size()-1)][cityIndex.get(0)]; //To Return back from the
                                                                                          // last to the first city
        return totalCost;
    }

    /**
     * The setFitnessScore function is used to give a value to the fitnessScore since it has private access
     * @param score
     * Stores the fitness score to be assigned to the fitnessScore variable
     */

    void setFitnessScore(double score){
        fitnessScore = score;
    }

    /**
     * the getFitnessScore function returns the fitnessScore value since it has private access
     * @return
     * The fitness score value
     */

    double getFitnessScore(){
        return fitnessScore;
    }

}

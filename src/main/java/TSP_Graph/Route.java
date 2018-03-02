package TSP_Graph;

import TSP_Graph.Graph;

/**
 * Created by Dylan Galea on 02/03/2018.
 */
public class Route {
    private int cityIndex[];
    private double fitnessScore;

    public Route(){}

    public Route(int size,int input[]){
        setListOfCities(size,input);
    }

    public void setListOfCities(int size,int input[]){
        cityIndex = new int[size];
        for(int i=0;i<size;i++){
            cityIndex[i] = input[i];
        }
    }

    public int[] getListOfCities(){
        return cityIndex;
    }

    public double getRouteCost(Graph g){
        double totalCost = 0;
        double matrixWeights[][] = g.getMatrixOfWeights();
        for(int i=0;i<cityIndex.length-1;i++){
            totalCost = totalCost + matrixWeights[cityIndex[i]][cityIndex[i+1]];
        }
        totalCost = totalCost+matrixWeights[cityIndex[cityIndex.length-1]][cityIndex[0]]; //To Return back;
        return totalCost;
    }

    public void setFitnessScore(double score){
        fitnessScore = score;
    }

    public double getFitnessScore(){
        return fitnessScore;
    }

    public void setElementInCityIndex(int n,int value){
        cityIndex[n] = value;
    }
}

package ACO;

import GA.GARoute;
import TSP_Graph.City;
import TSP_Graph.Graph;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Dylan Galea on 06/03/2018.
 */
public class ACO_TSP {
    private ArrayList<Ant> ants;
    private int numberOfAnts;
    private double q0;
    private double alpha;
    private double t0;
    private double Beta;
    private Graph g;
    private double pheromoneMatrix[][];

    public ACO_TSP(){
        ants = new ArrayList<Ant>();
        g = new Graph(new File(
                "C:/Users/Dylan Galea/IdeaProjects/MachineLearning/src/main/java/burma14.tsp"));
        numberOfAnts = 2; //
        q0 = 0.9; //0.9
        alpha = 0.1; //0.1
        Beta = 2; //2
        t0 = 1/(g.getListOfCities().size()*TourLengthUsingNearestNeighbourHeuristic()); //By paper
        pheromoneMatrix = new double [g.getListOfCities().size()][g.getListOfCities().size()];
        for(int i=0;i<g.getListOfCities().size();i++){
            for(int j=0;j<g.getListOfCities().size();j++){
                pheromoneMatrix[i][j] = 0.05;
            }
        }
    }

    public ACO_TSP(int numberOfAnts,double q0,double alpha, File filepath,double t0,double Beta){ //done for convenience
        this.numberOfAnts = numberOfAnts;
        this.q0 = q0;
        this.alpha = alpha;
        this.t0 = t0;
        this.Beta = Beta;
        g = new Graph(filepath);
        pheromoneMatrix = new double [g.getListOfCities().size()][g.getListOfCities().size()];
        for(int i=0;i<g.getListOfCities().size();i++){
            for(int j=0;j<g.getListOfCities().size();j++){
                pheromoneMatrix[i][j] = 0.05;
            }
        }
    }
    private double TourLengthUsingNearestNeighbourHeuristic(){
        Random rand = new Random(System.currentTimeMillis());
        int  startingCityIndex = rand.nextInt(g.getListOfCities().size());
        Ant ant = new Ant(g.getListOfCities().get(startingCityIndex),startingCityIndex);
        while (!ant.completedTour(g)){
            int nearestIndex = 0;
            double smallestDistance = Double.MAX_VALUE;
            for(int i=0;i<g.getListOfCities().size();i++){
                if(g.getListOfCities().get(i).getID()==ant.currentCity.getID() || ant.route.visitedCities.contains(g.getListOfCities()
                .get(i))){
                }else{
                    if(g.getMatrixOfWeights()[ant.currentCity.getID()-1][i] < smallestDistance){
                        nearestIndex = i;
                        smallestDistance = g.getMatrixOfWeights()[ant.currentCity.getID()-1][i];
                    }
                }
            }
            ant.visitCity(g.getListOfCities().get(nearestIndex),g);
        }
        System.out.println(ant.getRouteLength());
        return ant.getRouteLength();
    }

    private void createAnts(){ //Supposedly they can start from the same place
        Random rand = new Random(System.currentTimeMillis());
        int startingCityIndex;
        for(int i=0;i<numberOfAnts;i++){
            startingCityIndex = rand.nextInt(g.getListOfCities().size());
            ants.add(new Ant(g.getListOfCities().get(startingCityIndex),startingCityIndex));
        }
    }

    private double probabilityFunction(Ant ant, City nextCity){
        if(ant.route.visitedCities.contains(nextCity)){
            return 0.0;
        }else {
            ArrayList<City> citiesToCalculateFrom = new ArrayList<City>();
            for (int i = 0; i < g.getListOfCities().size(); i++) { //Determine which cities are to be chosen from
                if (!ant.route.visitedCities.contains(g.getListOfCities().get(i))) {
                    citiesToCalculateFrom.add(g.getListOfCities().get(i));
                }
            }
            double denominator = 0;
            for (City aCitiesToCalculateFrom : citiesToCalculateFrom) {
                int currentCityIndex = ant.currentCity.getID() - 1;
                int nextCityIndex = aCitiesToCalculateFrom.getID() - 1;
                denominator += pheromoneMatrix[currentCityIndex][nextCityIndex] *
                        Math.pow(1 / g.getMatrixOfWeights()[currentCityIndex][nextCityIndex], Beta);
            }
            int currentCityIndex = ant.currentCity.getID() - 1;
            int nextCityIndex = nextCity.getID()-1;
            return (pheromoneMatrix[currentCityIndex][nextCityIndex] *
                    Math.pow(1 / g.getMatrixOfWeights()[currentCityIndex][nextCityIndex], Beta))/denominator;
        }
    }

    private int Part2OfProbabilisticFormula(Ant ant){ //mhux cert hawn dit ar roulette
        double val = Math.random();
        double totalProbability = 0.0;
        for (int i=0;i<g.getListOfCities().size();i++) {
            totalProbability += probabilityFunction(ant,g.getListOfCities().get(i));
            if (val <= totalProbability) {
                return i;
            }
        }
        System.out.println("Something wrong with part 2");
        return -1;
    }

    private int Part1OfProbabilisticFormula(Ant ant){
        ArrayList<City> citiesToCalculateFrom = new ArrayList<City>();
        for(int i=0;i<g.getListOfCities().size();i++){ //Determine which cities are to be chosen from
            if(!ant.route.visitedCities.contains(g.getListOfCities().get(i))){
                citiesToCalculateFrom.add(g.getListOfCities().get(i));
            }
        }
        double maxValue = -1;
        int maxIndex = -1;
        for (City aCitiesToCalculateFrom : citiesToCalculateFrom) {
            int currentCityIndex = ant.currentCity.getID() - 1;
            int nextCityIndex = aCitiesToCalculateFrom.getID() - 1;
            double value = pheromoneMatrix[currentCityIndex][nextCityIndex] *
                    Math.pow(1 / g.getMatrixOfWeights()[currentCityIndex][nextCityIndex], Beta);
            if (value > maxValue) {
                maxValue = value;
                maxIndex = aCitiesToCalculateFrom.getID() - 1;
            }
        }
        if(maxIndex ==-1){
            System.out.println("Something is wrong in prob1");
        }
        return maxIndex;
    }

    private void moveAnt(Ant ant){ //ToDo in other parts check that cities not already visited
        Random rand = new Random(System.currentTimeMillis());
        int indexOfChosenCity;
        double q = rand.nextDouble();
        if(q<=q0){
            indexOfChosenCity = Part1OfProbabilisticFormula(ant);
        }else{
            indexOfChosenCity = Part2OfProbabilisticFormula(ant);
        }
        localPheromoneUpdate(ant.currentCity.getID()-1,indexOfChosenCity);
        ant.visitCity(g.getListOfCities().get(indexOfChosenCity),g);
    }

    private void localPheromoneUpdate(int currentCityIndex,int nextCityIndex){
        pheromoneMatrix[currentCityIndex][nextCityIndex] = (1-alpha)*pheromoneMatrix[currentCityIndex][nextCityIndex]
                + alpha*t0;
        pheromoneMatrix[nextCityIndex][currentCityIndex] = (1-alpha)*pheromoneMatrix[nextCityIndex][currentCityIndex]
                + alpha*t0;
    }

    private void globalPheromoneUpdate(int currentCityIndex,int nextCityIndex,double shortestTourLength){
        pheromoneMatrix[currentCityIndex][nextCityIndex] = (1-alpha)*pheromoneMatrix[currentCityIndex][nextCityIndex]
                + alpha*(1/shortestTourLength);
        pheromoneMatrix[nextCityIndex][currentCityIndex] = (1-alpha)*pheromoneMatrix[nextCityIndex][currentCityIndex]
                + alpha*(1/shortestTourLength);
    }

    public double ACO_Engine(){
        double answer =Double.MAX_VALUE;
        for(int i=0;i<10000;i++){
            createAnts();
            int antsCompletedTour = 0;
            while(antsCompletedTour < numberOfAnts){
                for(int j=0;j<numberOfAnts;j++){
                    if(!ants.get(j).completedTour(g)){
                        moveAnt(ants.get(j));
                    }else{
                        antsCompletedTour++;
                    }
                }
            }
            double smallestDistance = Double.MAX_VALUE;
            int bestAntIndex = -1;
            for(int j=0;j<numberOfAnts;j++){
                if(ants.get(j).getRouteLength()<smallestDistance){
                    bestAntIndex = j;
                    smallestDistance = ants.get(j).getRouteLength();
                }
            }
            if(smallestDistance<answer){
                answer = smallestDistance;
            }
            System.out.println("iteration "+i+"smallest distance : "+answer);
            for(int j=0;j<ants.get(bestAntIndex).route.visitedCities.size()-1;j++){
                int currentCityIndex = ants.get(bestAntIndex).route.visitedCities.get(j).getID()-1;
                int nextCityIndex = ants.get(bestAntIndex).route.visitedCities.get(j+1).getID()-1;
                globalPheromoneUpdate(currentCityIndex,nextCityIndex,ants.get(bestAntIndex).getRouteLength());
            }
            //to go back; // trial best better than global best
            int currentCityIndex = ants.get(bestAntIndex).route.visitedCities.get(
                    ants.get(bestAntIndex).route.visitedCities.size()-1).getID()-1;
            int nextCityIndex = ants.get(bestAntIndex).route.visitedCities.get(0).getID()-1;
            globalPheromoneUpdate(currentCityIndex,nextCityIndex,ants.get(bestAntIndex).getRouteLength());
            ants.clear();
        }
        return answer;
    }
}

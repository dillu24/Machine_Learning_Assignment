package GA;

import TSP_Graph.Graph;
import TSP_Graph.Route;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Dylan Galea on 02/03/2018.
 */

public class TSP_GA {
    private ArrayList <Route> listOfRoutes = new ArrayList<Route>();
    private double mutationRate;
    private int populationSize;
    private double crossOverRate;
    private Graph g;

    public TSP_GA(){
        mutationRate = 0.2; // highering mutation rate bounces off optima but too high creates too much random ..low population higher mutation
                            // Greater instance => less pop size greater mutation since we need more variety
        populationSize = 10000; //Increasing population also increases chance always finds
        crossOverRate = 0.7; //Lowering cross over rate improves .. highering too much bounce of the optimal
        g = new Graph(new File(
                "C:/Users/Dylan Galea/IdeaProjects/MachineLearning/src/main/java/burma14.tsp"));
    }

    public TSP_GA(double mutationRate,int populationSize,double crossOverRate, File filepath){
        this.mutationRate = mutationRate;
        this.populationSize = populationSize;
        this.crossOverRate = crossOverRate;
        new Graph(filepath);
    }

    private void initializeListOfRoutes(){
        Random rand = new Random(System.currentTimeMillis()); //For Seed

        for(int i=0;i<populationSize;i++){
            int input[] = new int[g.getListOfCities().size()];

            for(int j=0;j<input.length;j++){
                input[j] = -1;
            }
            for(int j=0;j<g.getListOfCities().size();j++){
                int  newNumber = rand.nextInt(g.getListOfCities().size());
                while (checkIfElementOf(input,newNumber)){
                    newNumber = rand.nextInt(g.getListOfCities().size());
                }
                input[j] = newNumber;
            }
            listOfRoutes.add(new Route(g.getListOfCities().size(),input));
        }
    }

    private boolean checkIfElementOf(int input[],int element){
        for (int anInput : input)
            if (anInput == element) {
                return true;
            }
        return false;
    }

    private double fitnessFunction(Route route){
        return 1/(route.getRouteCost(g));
    }

    private void computeFitnessOfEachRoute(){
        for (Route route : listOfRoutes) {
            route.setFitnessScore(fitnessFunction(route));
        }
    }

    private int[] Selection(int n){
        if(n>populationSize){
            return null;
        }else if(n==populationSize){
            int result[] = new int[populationSize];
            for(int i=0;i<populationSize;i++){
                result[i]= i;
            }
            return result;
        }else{
            int tournamentSize;
            if(populationSize>20){
                tournamentSize=5; //Be careful other wise run into problems since choose the same
            }else{
                tournamentSize=populationSize/4;
            }
            //Important to converge early..the best almost always chosen like this .. if small alternates .. but small enough
            //more variation to get closer to optimum value.
            int selectedIndices[] = new int[n];
            for(int j=0;j<n;j++){
                selectedIndices[j]=-1;
            }
            Random rand = new Random(System.currentTimeMillis());
            for(int i=0;i<n;i++){
                int tournamentIndices[] = new int[tournamentSize];
                for(int j=0;j<tournamentSize;j++){
                    tournamentIndices[j]=-1;
                }
                for(int j=0;j<tournamentSize;j++){
                    int newNumber = rand.nextInt(populationSize);
                    while(checkIfElementOf(tournamentIndices,newNumber) || checkIfElementOf(selectedIndices,newNumber)){
                        newNumber = rand.nextInt(populationSize);
                    }
                    tournamentIndices[j] = newNumber;
                }
                int bestIndex =tournamentIndices[0];
                for(int j=0;j<tournamentSize;j++){
                    if(listOfRoutes.get(tournamentIndices[j]).getFitnessScore()>listOfRoutes.get(bestIndex).getFitnessScore()){
                        bestIndex = tournamentIndices[j];
                    }
                }
                selectedIndices[i] = bestIndex;
            }
            return selectedIndices;
        }
    }

    private Route[] crossOver(Route route1,Route route2) {
        Random rand = new Random(System.currentTimeMillis());
        int childRoute1Cities [] = new int[route1.getListOfCities().length];
        int childRoute2Cities [] = new int[route1.getListOfCities().length];

        for(int i=0;i<childRoute1Cities.length;i++){
            childRoute1Cities[i] = -1;
            childRoute2Cities[i] = -1;
        }

        int newNumber = rand.nextInt(g.getListOfCities().size()/2)+1;

        for(int i=newNumber;i<route1.getListOfCities().length;i++){
            childRoute1Cities[i] = route1.getListOfCities()[i];
            childRoute2Cities[i-newNumber] = route2.getListOfCities()[i-newNumber];
        }
        int j=0;
        for(int i=0;i<childRoute1Cities.length;i++){
            if(childRoute1Cities[i] != -1){
               continue;
            }
            while(checkIfElementOf(childRoute1Cities,route2.getListOfCities()[j])){
                j++;
            }
            childRoute1Cities[i]= route2.getListOfCities()[j];
        }
        j=0;
        for(int i=0;i<childRoute2Cities.length;i++){
            if(childRoute2Cities[i] != -1){
                continue;
            }
            while(checkIfElementOf(childRoute2Cities,route1.getListOfCities()[j])){
                j++;
            }
            childRoute2Cities[i]= route1.getListOfCities()[j];
        }
        Route childRoute1 = new Route(childRoute1Cities.length,childRoute1Cities);
        Route childRoute2 = new Route(childRoute2Cities.length,childRoute2Cities);
        return new Route[]{childRoute1, childRoute2};
    }

    private Route mutate(Route route){
        Random rand = new Random(System.currentTimeMillis());
        int indicesForMutation[] = new int[2];
        for(int i=0;i<2;i++){
            indicesForMutation[i] = rand.nextInt(route.getListOfCities().length);
        }
        int tempVar = route.getListOfCities()[indicesForMutation[0]];
        route.setElementInCityIndex(indicesForMutation[0],route.getListOfCities()[indicesForMutation[1]]);
        route.setElementInCityIndex(indicesForMutation[1],tempVar);
        return route;
    }

    private Route createRandomRoute(){
        Random rand = new Random(System.currentTimeMillis()); //For Seed
        int input[] = new int[g.getListOfCities().size()];
        for(int j=0;j<input.length;j++){
            input[j] = -1; // To accept index 0
        }
        for(int j=0;j<g.getListOfCities().size();j++){
            int  newNumber = rand.nextInt(g.getListOfCities().size());
            while (checkIfElementOf(input,newNumber)){
                newNumber = rand.nextInt(g.getListOfCities().size());
            }
            input[j] = newNumber;
        }
        return new Route(g.getListOfCities().size(),input);
    }

    public double GA_Engine(){
        ArrayList<Route> nextGenRoutes = new ArrayList<Route>();
        initializeListOfRoutes();
        computeFitnessOfEachRoute();
        for(int i=0;i<1000000;i++){
            int indicesAutomaticallySelected [] = Selection((int)Math.ceil((1-crossOverRate)*populationSize));
            if(indicesAutomaticallySelected==null){
                break;
            }
            for(int j=0;j<indicesAutomaticallySelected.length;j++){
                nextGenRoutes.add(listOfRoutes.get(indicesAutomaticallySelected[j]));
            }

            int indicesForCrossOver [] = Selection((int)Math.ceil(crossOverRate*populationSize));
            if(indicesForCrossOver==null){
                break;
            }
            for(int j=0;j<indicesForCrossOver.length-1;j++){
                nextGenRoutes.add(crossOver(listOfRoutes.get(indicesForCrossOver[j]),
                        listOfRoutes.get(indicesForCrossOver[j+1]))[0]);
                nextGenRoutes.add(crossOver(listOfRoutes.get(indicesForCrossOver[j]),
                        listOfRoutes.get(indicesForCrossOver[j+1]))[1]);
                j++;
            }
            for(int j=nextGenRoutes.size();j<populationSize;j++){
                nextGenRoutes.add(createRandomRoute());
            }
            int indicesForMutation [] = Selection((int)Math.ceil(mutationRate*populationSize));
            if(indicesForMutation==null){
                break;
            }
            for(int j=0;j<indicesForMutation.length;j++){
                nextGenRoutes.set(indicesForMutation[j],mutate(nextGenRoutes.get(indicesForMutation[j])));
            }
            listOfRoutes.clear();
            for(int j=0;j<nextGenRoutes.size();j++){
                listOfRoutes.add(nextGenRoutes.get(j));
            }
            computeFitnessOfEachRoute();
            nextGenRoutes.clear();
            double shortestPath= Double.MAX_VALUE;
            for(int j=0;j<listOfRoutes.size();j++){
                if(listOfRoutes.get(j).getRouteCost(g)<shortestPath){
                    shortestPath = listOfRoutes.get(j).getRouteCost(g);
                }
            }
            System.out.println(shortestPath);
        }
        double shortestPath= Double.MAX_VALUE;
        for(int i=0;i<listOfRoutes.size();i++){
            if(listOfRoutes.get(i).getRouteCost(g)<shortestPath){
                shortestPath = listOfRoutes.get(i).getRouteCost(g);
            }
        }
        return shortestPath;
    }
}

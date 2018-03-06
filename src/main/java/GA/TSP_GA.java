package GA;

import TSP_Graph.Graph;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
//For short converges faster ... if instances grow we may need to reduce poualtion nad increase iterations sine faster
/**
 * Created by Dylan Galea on 02/03/2018.
 */

public class TSP_GA {
    private ArrayList <GARoute> listOfRoutes = new ArrayList<GARoute>();
    private double mutationRate;
    private int populationSize;
    private double crossOverRate;
    private Graph g;
    //best 0.02,25,0.8
    public TSP_GA(){
        mutationRate = 0.02; // highering mutation rate bounces off optima but too high creates too much random ..low population higher mutation
                            // Greater instance => less pop size greater mutation since we need more variety
        populationSize = 25; //Increasing population also increases chance always finds
        crossOverRate = 0.8; //Lowering cross over rate improves .. highering too much bounce of the optimal
        g = new Graph(new File(
                "C:/Users/Dylan Galea/IdeaProjects/MachineLearning/src/main/java/burma14.tsp"));
    }

    public TSP_GA(double mutationRate,int populationSize,double crossOverRate, File filepath){
        this.mutationRate = mutationRate;
        this.populationSize = populationSize;
        this.crossOverRate = crossOverRate;
        g = new Graph(filepath);
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
            listOfRoutes.add(new GARoute(g.getListOfCities().size(),input));
        }
    }

    private boolean checkIfElementOf(int input[],int element){
        for (int anInput : input)
            if (anInput == element) {
                return true;
            }
        return false;
    }

    private double fitnessFunction(GARoute route){
        return 1/(route.getRouteCost(g));
    }

    private void computeFitnessOfEachRoute(){
        for (GARoute route : listOfRoutes) {
            route.setFitnessScore(fitnessFunction(route));
        }
    }

    private double probabilityRouteSelected(GARoute route){
        double totalProb = 0;
        for(int i=0;i<listOfRoutes.size();i++){
            totalProb=totalProb+listOfRoutes.get(i).getFitnessScore();
        }
        return route.getFitnessScore()/totalProb;

    }

    private int[] CrossOverSelection(int n) {
        Random rand = new Random(System.currentTimeMillis());
        int tournamentSize = 5;
        int resultArray[] = new int[n];

        for(int i=0;i<resultArray.length;i++){
            resultArray[i]=-1;
        }

        int newNumber;
        for (int i = 0; i < n; i++) {
            int bestIndex=-1;
            for (int j = 0; j < tournamentSize; j++) {
                newNumber = rand.nextInt(populationSize);
                if (bestIndex == -1 || probabilityRouteSelected(listOfRoutes.get(newNumber)) >
                        probabilityRouteSelected(listOfRoutes.get(bestIndex))) {
                    bestIndex = newNumber;
                }
            }
            resultArray[i] = bestIndex;
        }
        return resultArray;
    }

    private int[] AutomaticSelection(int n) { //to prevent multiple selections
        Random rand = new Random(System.currentTimeMillis());
        int tournamentSize = 5; //to preserve diversity
        int resultArray[] = new int[n];

        for(int i=0;i<resultArray.length;i++){
            resultArray[i]=-1;
        }

        int newNumber;
        for (int i = 0; i < n; i++) {
            int bestIndex=-1;
            for (int j = 0; j < tournamentSize; j++) {
                newNumber = rand.nextInt(populationSize);

                while(checkIfElementOf(resultArray,newNumber)){
                    newNumber = rand.nextInt(populationSize);
                }

                if (bestIndex == -1 || probabilityRouteSelected(listOfRoutes.get(newNumber)) >
                        probabilityRouteSelected(listOfRoutes.get(bestIndex))) {
                    bestIndex = newNumber;
                }
            }
            resultArray[i] = bestIndex;
        }
        return resultArray;
    }

    private GARoute[] crossOver(GARoute route1, GARoute route2) { //ordered 2 point crossover
        Random rand = new Random(System.currentTimeMillis());
        int firstNum = rand.nextInt(route1.getListOfCities().length);
        int secondNum = rand.nextInt(route1.getListOfCities().length);
        while (secondNum==firstNum){
            secondNum = rand.nextInt(route1.getListOfCities().length);
        }
        if(firstNum>secondNum){
            int temp = firstNum;
            firstNum = secondNum;
            secondNum = temp;
        }
        int child1cities[] = new int[route1.getListOfCities().length];
        int child2cities[] = new int[route2.getListOfCities().length];
        for(int i=0;i<child1cities.length;i++){
            child1cities[i] = -1;
            child2cities[i] = -1;
        }
        for(int i=firstNum;i<=secondNum;i++){
            child1cities[i] = route1.getListOfCities()[i];
            child2cities[i] = route2.getListOfCities()[i];
        }
        int j=0;
        for(int i=0;i<route1.getListOfCities().length;i++){
            while(j<child1cities.length && child1cities[j] != -1){
                j++;
            }
            if(!checkIfElementOf(child1cities,route2.getListOfCities()[i])){
                child1cities[j] = route2.getListOfCities()[i];
            }
        }
        j=0;
        for(int i=0;i<route2.getListOfCities().length;i++){
            while(j<child2cities.length && child2cities[j] != -1){
                j++;
            }
            if(!checkIfElementOf(child2cities,route1.getListOfCities()[i])){
                child2cities[j] = route1.getListOfCities()[i];
            }
        }
        GARoute child1 = new GARoute(child1cities.length,child1cities);
        GARoute child2 = new GARoute(child2cities.length,child2cities);
        return new GARoute[]{child1, child2};
    }

    private int[] reverse(int array[]){
        int result[] = new int[array.length];
        for(int i=0;i<array.length;i++){
            result[i] = array[array.length-i-1];
        }
        return result;
    }

    private GARoute mutate(GARoute route){ //inversion
        Random rand = new Random(System.currentTimeMillis());
        int firstNum = rand.nextInt(route.getListOfCities().length);
        int secondNum = rand.nextInt(route.getListOfCities().length);
        while(secondNum==firstNum){
            secondNum = rand.nextInt(route.getListOfCities().length);
        }
        if(firstNum >secondNum){
            int temp = firstNum;
            firstNum=secondNum;
            secondNum=temp;
        }
        int arrayToReverse[] = new int[secondNum-firstNum+1];
        for(int i=0;i<arrayToReverse.length;i++){
            arrayToReverse[i] = route.getListOfCities()[firstNum+i];
        }
        arrayToReverse = reverse(arrayToReverse);
        for(int i=firstNum;i<=secondNum;i++){
            route.setElementInCityIndex(i,arrayToReverse[i-firstNum]);
        }
        return route;
    }

    private GARoute createRandomRoute(){
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
        return new GARoute(g.getListOfCities().size(),input);
    }

    public double GA_Engine(){
        ArrayList<GARoute> nextGenRoutes = new ArrayList<GARoute>();
        initializeListOfRoutes();
        computeFitnessOfEachRoute();
        for(int i=0;i<10000000;i++){
            int indicesAutomaticallySelected [] = AutomaticSelection((int)Math.ceil((1-crossOverRate)*populationSize));
            for(int j=0;j<indicesAutomaticallySelected.length;j++){
                nextGenRoutes.add(listOfRoutes.get(indicesAutomaticallySelected[j]));
            }

            int indicesForCrossOver [] = CrossOverSelection((int)Math.ceil(crossOverRate*populationSize));
            for(int j=0;j<indicesForCrossOver.length-1;j++){
               GARoute children[]=
                       crossOver(listOfRoutes.get(indicesForCrossOver[j]),listOfRoutes.get(indicesForCrossOver[j+1]));
               nextGenRoutes.add(children[0]);
               nextGenRoutes.add(children[1]);
               j++;
            }
            for(int j=nextGenRoutes.size();j<populationSize;j++){
                nextGenRoutes.add(createRandomRoute());
            }
            int indicesForMutation [] = AutomaticSelection((int)Math.ceil(mutationRate*populationSize));
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
            System.out.println(i+" "+shortestPath);
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

package GA;

import TSP_Graph.Graph;
import TSP_Graph.Route;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Dylan Galea on 02/03/2018.
 */

//TODO Make Them Spit Indexes instead

public class TSP_GA {
    private ArrayList <Route> listOfRoutes = new ArrayList<Route>();
    private double mutationRate;
    private double populationSize;
    private double crossOverRate;
    private Graph g;

    TSP_GA(){
        mutationRate = 0.2;
        populationSize = 10;
        crossOverRate = 0.3;
    }

    TSP_GA(double mutationRate,double populationSize,double crossOverRate){
        this.mutationRate = mutationRate;
        this.populationSize = populationSize;
        this.crossOverRate = crossOverRate;
    }

    private void initializeListOfRoutes(){
        Random rand = new Random(System.currentTimeMillis()); //For Seed
        int input[] = new int[g.getListOfCities().size()];
        for(int i=0;i<populationSize;i++){
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
        for (Route listOfRoute : listOfRoutes) {
            listOfRoute.setFitnessScore(fitnessFunction(listOfRoute));
        }
    }


    private Route[] Selection(int n){
        Route finalResult[] = new Route[n];
        Random rand = new Random(System.currentTimeMillis());
        int bestIndex = 0;
        for(int i=0;i<n;i++){
            int selectedIndexes[] = new int[7];
            Route selectedIndividuals[] = new Route[7];
            for(int j=0;j<7;j++){
                int newNumber = rand.nextInt(listOfRoutes.size());
                while(checkIfElementOf(selectedIndexes,newNumber)){
                    newNumber = rand.nextInt(listOfRoutes.size());
                }
                selectedIndexes[j] = newNumber;
                selectedIndividuals[j] = listOfRoutes.get(newNumber);
            }
            for(int j=0;j<6;j++){
                if(selectedIndividuals[j].getFitnessScore() < selectedIndividuals[j+1].getFitnessScore()){
                    bestIndex = j+1;
                }
            }
            finalResult[i] = listOfRoutes.get(bestIndex);
            bestIndex = 0;
        }
        return finalResult;
    }

    private Route crossOver(Route route1,Route route2) {
        Random rand = new Random(System.currentTimeMillis());
        Route childRoute = new Route();
        int resultRoute[] = new int[route1.getListOfCities().length];

        for(int i=0;i<resultRoute.length;i++){
            resultRoute[i] = -1;
        }

        int numberOfElementsFrom1stParent = rand.nextInt(route1.getListOfCities().length)+1;

        for(int i=0;i<numberOfElementsFrom1stParent;i++){
            int chosenElement = rand.nextInt(route1.getListOfCities().length);
            while(resultRoute[chosenElement]==-1){
                chosenElement = rand.nextInt(route1.getListOfCities().length);
            }
            resultRoute[i] = route1.getListOfCities()[chosenElement];
        }
        int j=0;
        for(int i=0;i<route2.getListOfCities().length;i++){
            while(resultRoute[j] != -1){
                j++;
            }

            if(j>=resultRoute.length){
                break;
            }

            if(checkIfElementOf(resultRoute,route2.getListOfCities()[i])){
            }else{
                resultRoute[j] = route2.getListOfCities()[i];
            }
        }
        childRoute.setListOfCities(route1.getListOfCities().length,resultRoute);
        return childRoute;
    }

    private Route mutate(Route route){
        Random rand = new Random(System.currentTimeMillis());
        int num1 = rand.nextInt(route.getListOfCities().length);
        int num2 = rand.nextInt(route.getListOfCities().length);
        while(num1==num2){
            num1 = rand.nextInt(route.getListOfCities().length);
        }
        int temp = route.getListOfCities()[num1];
        route.setElementInCityIndex(num1,route.getListOfCities()[num2]);
        route.setElementInCityIndex(num2,temp);
        return route;
    }

    public double GA_Engine(){
        initializeListOfRoutes();
        computeFitnessOfEachRoute();
        for(int i=0;i<100;i++){
           Route selectedImmediately[] = Selection((int)((1-crossOverRate)*populationSize));
           Route selectedForCrossover[] = Selection((int)(crossOverRate*populationSize));
           ArrayList <Route> offSpringList = new ArrayList<Route>();
           for(int j=0;j<selectedForCrossover.length-1;j++){
               offSpringList.add(crossOver(selectedForCrossover[j],selectedForCrossover[j+1]));
           }
           listOfRoutes.clear();
            for (Route anOffSpringList : offSpringList) {
                listOfRoutes.add(anOffSpringList);
            }
            for (Route aSelectedImmediately : selectedImmediately) {
                listOfRoutes.add(aSelectedImmediately);
            }
            Route selectedForMutation [] = Selection((int)(mutationRate*populationSize));

        }
        return 0.0;
    }
}

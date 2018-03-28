package GA;

import TSP_Graph.Graph;
import java.io.File;
import java.util.*;

/**
 * This class encodes all the logic and design of the Genetic Algorithm that was applied for TSP symmetric instances
 */

public class TSP_GA {
    /**
     * The listOfRoutes array list stores the population of individuals which are solutions to the given problem
     * The mutationRate variable stores the fraction from the created children to be mutated
     * The crossOver rate variable stores the fraction from the population to be paired to create offspring
     * The populationSize variable stores the number of chromosomes to be maintained between each iteration
     * The variable g stores the graph that has been encoded from the TSP Instance file , on which the algorithm
     * will be executed.
     * The variable randomRouteRate stores the fraction of the population that will be replaced at each iteration with
     * randomly generated chromosomes , this is used to enhance diversity.
     */
    private ArrayList <GARoute> listOfRoutes = new ArrayList<GARoute>();
    private double mutationRate;
    private int populationSize;
    private double crossOverRate;
    private double randomRouteRate;
    private Graph g;

    /**
     * The TSP_GA() Constructor is the default constructor with default parameters set , which are the most
     * effective parameters as suggested by papers and as noted by experiment.
     */
    public TSP_GA(){
        mutationRate = 0.2;
        populationSize = 100;
        crossOverRate = 0.9;
        randomRouteRate= 0.3;
        g = new Graph(new File(
                "C:/Users/Dylan Galea/IdeaProjects/MachineLearning/src/main/java/burma14.tsp"));
    }

    /**
     * This constructor is used to set up the parameters as desired by the user
     * @param mutationRate
     * Stores the mutation rate value to be set to the mutationRate variable
     * @param populationSize
     * Stores the population size value to be set to the populationSize variable
     * @param crossOverRate
     * Stores the cross over rate value to be set to the crossOverRate variable
     * @param filepath
     * Stores the filepath of the TSP file instance on disc in order to create a new graph instance.
     */

    public TSP_GA(double mutationRate,int populationSize,double crossOverRate,double randomRouteRate, File filepath){
        this.mutationRate = mutationRate;
        this.populationSize = populationSize;
        this.crossOverRate = crossOverRate;
        this.randomRouteRate = randomRouteRate;
        g = new Graph(filepath);
    }

    /**
     * This method is used to initialize the population with randomly generated chromosomes . The mechanism of random
     * generation was preferred from that of population seeding because in the approach taken in our genetic algorithm
     * diversity is important , thus using some algorithm to narrow down the search space would result into premature
     * convergence.
     */

    private void GenerateRandomPopulation(){
        Set<ArrayList<Integer>> permutationList = new HashSet<ArrayList<Integer>>(); //Stores the unique list of permutations to be
                                                                    // included in the population
        for(int i=listOfRoutes.size();i<populationSize;i++){
            GARoute newRoute = new GARoute();
            for(int j=0;j<g.getListOfCities().size();j++){
                newRoute.cityIndex.add(j);//create a permutation smallest to largest
            }
            do{
                Collections.shuffle(newRoute.cityIndex); //shuffle the permutation until it is unique in our population
            }while (permutationList.contains(newRoute.cityIndex));
            permutationList.add(newRoute.cityIndex); // add the chromosome to the population and add it to the
                                                     // permuationList in order not to create the same chromosome again
            listOfRoutes.add(newRoute);
        }
    }

    /**
     * This function encodes the fitness function for our genetic algorithm . Since the fitness function must be maximized
     * , the fitness function was chosen to be the inverse of the distances of the routes since the genetic algorithm
     * must find the shortest path.
     * @param route
     * Stores the route whose fitness is to be computed
     * @return
     * The fitness score of the passed route.
     */
    private double fitnessFunction(GARoute route){
        return 1/route.getRouteCost(g);
    }

    /**
     * This function computes the fitness of each member of the population
     */
    private void computeFitnessOfPopulation(){
        for (GARoute listOfRoute : listOfRoutes) {
            listOfRoute.setFitnessScore(fitnessFunction(listOfRoute));
        }
    }

    /**
     * This function encodes the selection mechanism of our genetic algorithm . Tournament selection was chosen because
     * it gives the best diversity amongst roulette wheel selection. The reason is that since the population would
     * have a lot of bad chromosomes (in terms of fitness) , this would allow the bad chromosomes to be chosen more often.
     * Also the tournament size was chosen to be 2 in order to enhance diversity again . Diversity is important because
     * it doesn't narrow down the search.
     * @return
     * The chosen route.
     */
    private GARoute tournamentSelection(){
        int tournamentSize =2; //stores the tournament size
        int bestIndex = -1; //stores the best index in the list of population that was chosen
        Set<Integer> tournamentRoutes = new HashSet<Integer>(); // stores the routes in the tournament in order to not choose
                                                          // between the same routes
        Random rand = new Random(System.currentTimeMillis());
        for(int i=0;i<tournamentSize;i++){
            int newRouteIndex;
            do{
                newRouteIndex = rand.nextInt(listOfRoutes.size()); //chose an index which was not already chosen
            }while (tournamentRoutes.contains(newRouteIndex));
            tournamentRoutes.add(newRouteIndex); //add it to mark it as visited
            if(bestIndex == -1 || listOfRoutes.get(newRouteIndex).fitnessScore > listOfRoutes.get(bestIndex).fitnessScore){
                bestIndex = newRouteIndex; // if this chosen index has better fitness score , save it
            }
        }
        return listOfRoutes.get(bestIndex); // return the chosen route by indexing the list of chromosomes.
    }

    /**
     * This function encodes the cross over operator for our genetic algorithm . The implemented mechanism was 2-point
     * ordered cross over since the classical operators used for the genetic algorithm could not be applied due to the
     * importance of order in our representation . In this representation 2 children are created from the parents.
     * @param parent1
     * Stores one of the parents
     * @param parent2
     * Stores one of the parents
     * @return
     * An array list of 2 children .
     */

    private ArrayList<GARoute> crossOver(GARoute parent1 ,GARoute parent2) {
        Random rand = new Random(System.currentTimeMillis());
        ArrayList<GARoute> childrenList = new ArrayList<GARoute>(); //stores the children created
        GARoute child1 = new GARoute(); //stores the first child
        GARoute child2 = new GARoute();//stores the second child
        for (int i = 0; i < parent1.cityIndex.size(); i++) { // fill both array lists of the children with -1 in order to
                                                             // access the indexes
            child1.cityIndex.add(-1);
            child2.cityIndex.add(-1);
        }
        int firstCrossOverPoint = rand.nextInt(g.getListOfCities().size()); //chose the first cross over point
        int secondCrossOverPoint = rand.nextInt(g.getListOfCities().size()); //chose the second cross over point
        while (firstCrossOverPoint >= secondCrossOverPoint) { // check that the first cross over point is smaller ,
                                                              // otherwise get them in order
            firstCrossOverPoint = rand.nextInt(g.getListOfCities().size());
            secondCrossOverPoint = rand.nextInt(g.getListOfCities().size());
        }
        for (int i = firstCrossOverPoint; i <= secondCrossOverPoint; i++) {
            //Copy a direct subset between these 2 cross over points into the children , one from the first parent and
            //the other from the second parent.
            child1.cityIndex.set(i, parent1.cityIndex.get(i));
            child2.cityIndex.set(i, parent2.cityIndex.get(i));
        }
        int nextFreeIndex = 0; //stores the next free index in the children in order to copy from the second parent
        for (int i = 0; i < parent2.cityIndex.size(); i++) {
            if (child1.cityIndex.contains(parent2.cityIndex.get(i))) {
                continue; //if from the second parent the current element is found in the child skip it to preserve repetition
            }
            while (child1.cityIndex.get(nextFreeIndex) != -1) {
                nextFreeIndex++; //find the next slot the child where the element from the second parent should be inserted
            }
            child1.cityIndex.set(nextFreeIndex, parent2.cityIndex.get(i));
        }
        nextFreeIndex = 0;
        for (int i = 0; i < parent1.cityIndex.size(); i++) { //same comments hold but in reverse using parent 1 for child2
            if (child2.cityIndex.contains(parent1.cityIndex.get(i))) {
                continue;
            }
            while (child2.cityIndex.get(nextFreeIndex) != -1) {
                nextFreeIndex++;
            }
            child2.cityIndex.set(nextFreeIndex, parent1.cityIndex.get(i));
        }
        childrenList.add(child1);
        childrenList.add(child2);
        return childrenList;
    }

    /**
     * This method encodes the mutation function used in our genetic algorithm . The chosen method was Inversion Mutation
     * which from the read papers gives the best performance to the genetic algorithm .
     * @param route
     * The route to be mutated
     * @return
     * The mutated route
     */

    private GARoute mutation(GARoute route){
        Random rand = new Random(System.currentTimeMillis());
        int firstPoint = rand.nextInt(g.getListOfCities().size()); //stores the first point from which the route is to be mutated
        int secondPoint = rand.nextInt(g.getListOfCities().size());//stores the second point from which the route is to be mutated
        while (firstPoint >= secondPoint){ // get the correct order .
            firstPoint = rand.nextInt(g.getListOfCities().size());
            secondPoint = rand.nextInt(g.getListOfCities().size());
        }
        ArrayList<Integer> listToBeReversed = new ArrayList<Integer>();//stores the subset to be reversed
        for(int i=firstPoint;i<=secondPoint;i++){
            listToBeReversed.add(route.cityIndex.get(i));
        }
        Collections.reverse(listToBeReversed); //reverse the subset
        for(int i=0;i<listToBeReversed.size();i++){ //replace the subset in the original list to the reversed one
            route.cityIndex.set(i+firstPoint,listToBeReversed.get(i));
        }
        return route;
    }

    /**
     * This method is used to generate a random tour in the population after choosing the best individuals in our
     * merged population . This mechanism is important because it gives more diversity to the algorithm and thus makes
     * the approximation much better.
     * @return
     * The randomly generated route.
     */
    private GARoute generateRandomTour(){
        Random rand = new Random(System.currentTimeMillis());
        Set<Integer> visitedCityList = new HashSet<Integer>(); //stores the cities that have been visited in order to preserve repetition
        GARoute newRoute = new GARoute(); // the route to be returned
        for(int i=0;i<g.getListOfCities().size();i++){
            int newCityIndex = rand.nextInt(g.getListOfCities().size()); //choose a city
            while (visitedCityList.contains(newCityIndex)){ //if this city has been visited choose another one
                newCityIndex = rand.nextInt(g.getListOfCities().size());
            }
            visitedCityList.add(newCityIndex); //mark this as visited
            newRoute.cityIndex.add(newCityIndex); //add it to the route
        }
        return newRoute;
    }

    /**
     * This method is used to give the index from the population list of the least fit individual , this method is used
     * in order to replace the least fit with a random tour at every iteration.
     * @return
     * an index in the population list to the least fit individual
     */
    private int selectLessFit(){
        int bestIndex =-1; //stores the index of the least fit individual
        for(int i=0;i<listOfRoutes.size();i++){
            if(bestIndex==-1 || listOfRoutes.get(i).fitnessScore < listOfRoutes.get(bestIndex).fitnessScore){
                bestIndex = i; //if fitness score is less than that of the best one then the best is the current one
            }
        }
        return bestIndex;
    }

    /**
     * This method holds the genetic algorithm logic and represents the strategy used as explained in the documentation.
     * @return
     * The shortest tour in the last generation which is said to be the shortest tour due to converging.
     */

    public double GA_Engine(){
        GenerateRandomPopulation(); //Generates the initial population randomly
        computeFitnessOfPopulation(); //compute fitness of each individual
        Random rand = new Random(System.currentTimeMillis());
        for(int i=0;i<10000;i++){ //for a predefined number of iterations (since we do not know the optimal solution)
            ArrayList<GARoute> nextPopulation = new ArrayList<GARoute>(); //stores all the children created from parents
            ArrayList<Double> fitnessScoresList = new ArrayList<Double>(); //stores the fitnesses already stored in order
                                                                           // to not have duplicates in order to avoid
                                                                           // premature convergence.
            for (GARoute listOfRoute : listOfRoutes) { //mark the fitness already in the population, in order not to create
                                                       //children like parents.
                fitnessScoresList.add(listOfRoute.getFitnessScore());
            }
            for(int j=0;j<(crossOverRate*populationSize)/2;j++){ //Create children according to cross over rate.. division
                //by 2 since each pair gives 2 children.
                GARoute parent1; //stores the first parent that was chosen
                GARoute parent2;//stores the second parent that was chosen
                parent1 = tournamentSelection(); //select the first parent using tournament selection
                listOfRoutes.remove(parent1); //remove parent in order to not be chosen again
                parent2 = tournamentSelection(); //select second parent using tournament selection
                ArrayList<GARoute> children = crossOver(parent1,parent2); //create children
                children.get(0).setFitnessScore(fitnessFunction(children.get(0))); //set their fitness score
                children.get(1).setFitnessScore(fitnessFunction(children.get(1)));
                while (fitnessScoresList.contains(children.get(0).getFitnessScore())){ //if these children are already
                    //present in the next generation , mutate them in order to make them unique
                    children.set(0,mutation(children.get(0)));
                    children.get(0).setFitnessScore(fitnessFunction(children.get(0)));
                }
                while (fitnessScoresList.contains(children.get(1).getFitnessScore())){
                    children.set(1,mutation(children.get(1)));
                    children.get(1).setFitnessScore(fitnessFunction(children.get(1)));
                }
                listOfRoutes.add(parent1); //add parent back so that he can be chosen again
                nextPopulation.add(children.get(0)); //add both children to the children list
                nextPopulation.add(children.get(1));
                fitnessScoresList.add(children.get(0).getFitnessScore()); //record the finesses in order not to be chosen again
                fitnessScoresList.add(children.get(1).getFitnessScore());
            }
            for(int j=0;j<mutationRate*nextPopulation.size();j++){ //according to the mutation rate mutate the children
                int selectedIndex = rand.nextInt(nextPopulation.size()); //select a random index from the children list
                GARoute selectedForMutation= nextPopulation.get(selectedIndex);
                selectedForMutation = mutation(selectedForMutation); //mutate the individual
                selectedForMutation.setFitnessScore(fitnessFunction(selectedForMutation)); //set his fitness score
                while (fitnessScoresList.contains(selectedForMutation.getFitnessScore())){ //if due to mutation we
                    //created an already created individual , mutate him again.
                    selectedForMutation= mutation(selectedForMutation);
                    selectedForMutation.setFitnessScore(fitnessFunction(selectedForMutation));
                }
                fitnessScoresList.add(selectedForMutation.getFitnessScore()); //record the fitness for uniqueness
                nextPopulation.set(selectedIndex,selectedForMutation); //add to the children list
            }
            for (GARoute aNextPopulation : nextPopulation) {
                listOfRoutes.add(aNextPopulation); //add all the children to population list to create a merged population
            }
            computeFitnessOfPopulation(); //compute fitness of new population
            for(int k=listOfRoutes.size();k>populationSize;k--){ //remove the less fit individuals from the merged population
                listOfRoutes.remove(listOfRoutes.get(selectLessFit()));
            }
            for(int k=0;k<randomRouteRate*populationSize;k++){ //this method is used to select the less fit from the
                                                               //chosen next generation
                int worstIndex = selectLessFit();
                listOfRoutes.remove(worstIndex);
            }
            for(int k=listOfRoutes.size();k<populationSize;k++){ //maintain the population size by adding random route to
                                                                 //preserve diversity.
                GARoute random = generateRandomTour();
                random.setFitnessScore(fitnessFunction(random));
                while (fitnessScoresList.contains(random.getFitnessScore())){ //check that random route has not already been generated
                    random=generateRandomTour();
                    random.setFitnessScore(fitnessFunction(random));
                }
                fitnessScoresList.add(random.getFitnessScore());
                listOfRoutes.add(random);
            }
            computeFitnessOfPopulation(); //compute fitness
            int bestIndex = -1; //display best of this generation
            for(int k=0;k<listOfRoutes.size();k++){
                if(bestIndex ==-1 || listOfRoutes.get(k).fitnessScore> listOfRoutes.get(bestIndex).fitnessScore){
                    bestIndex = k;
                }
            }
            System.out.println("In generation "+i+" best route length is "+listOfRoutes.get(bestIndex).getRouteCost(g));
        }
        int bestIndex = -1;
        for(int i=0;i<listOfRoutes.size();i++){
            if(bestIndex ==-1 || listOfRoutes.get(i).fitnessScore> listOfRoutes.get(bestIndex).fitnessScore){
                bestIndex = i;
            }
        }
        return listOfRoutes.get(bestIndex).getRouteCost(g); //return best route .
    }
}

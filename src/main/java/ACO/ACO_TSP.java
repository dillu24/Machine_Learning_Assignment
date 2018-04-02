package ACO;

import TSP_Graph.City;
import TSP_Graph.Graph;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class encodes all the logic of the ACO proposed by Dorigo's paper
 */

public class ACO_TSP {
    /**
     * ants ->an array list storing the ants traversing the graph
     * numberOfAnts -> Stores the number of ants in the algorithm
     * q0 -> A stores the value to be used as a bias to choose which vertex to visit
     * alpha -> level of pheromone evaporation
     * Beta -> parameter giving importance to pheromone level and distance between cities
     * g -> graph to be traversed
     * pheromoneMatrix -> the matrix of pheromone levels between all cities
     */
    private ArrayList<Ant> ants;
    private int numberOfAnts;
    private double q0;
    private double alpha;
    private double t0;
    private double Beta;
    private Graph g;
    private double pheromoneMatrix[][];

    /**
     * The constructor used to initialize the algorithm with the best parameters given in the dorigo paper , by default
     * the file used will be burma14.tsp
     */

    public ACO_TSP(){
        ants = new ArrayList<Ant>();
        g = new Graph(new File(
                "C:/Users/Dylan Galea/IdeaProjects/MachineLearning/src/main/java/gil262.tsp"));
        numberOfAnts = 10;
        q0 = 0.9;
        alpha = 0.1;
        Beta = 2;
        t0 = 1/(g.getListOfCities().size()*TourLengthUsingNearestNeighbourHeuristic()); //as suggested by Dorigo
        pheromoneMatrix = new double [g.getListOfCities().size()][g.getListOfCities().size()];
        for(int i=0;i<g.getListOfCities().size();i++){ //initialize pheromone matrix with value 0.05
            for(int j=0;j<g.getListOfCities().size();j++){
                pheromoneMatrix[i][j] = 0.00005;
            }
        }
    }

    /**
     * Ths constructor will be used to initialize the algorithm with parameters given by the user
     * @param numberOfAnts
     * Stores the value representing the number of ants to be assigned to this.numberOfAnts
     * @param q0
     * Stores the value representing the bias to be assigned to this.q0
     * @param alpha
     * Stores the value representing the pheromone evaporation to be assigned to this.alpha
     * @param filepath
     * Stores the filepath from where the graph will be read
     * @param t0
     * Stores the value of how much pheromone will be given to an edge
     * @param Beta
     * Stores the value of importance of pheromone and distance to the algorithm to be assigned to this.Beta
     */
    public ACO_TSP(int numberOfAnts,double q0,double alpha, File filepath,double t0,double Beta){
        this.numberOfAnts = numberOfAnts;
        ants = new ArrayList<Ant>();
        this.q0 = q0;
        this.alpha = alpha;
        this.t0 = t0;
        this.Beta = Beta;
        g = new Graph(filepath);
        pheromoneMatrix = new double [g.getListOfCities().size()][g.getListOfCities().size()]; //as suggested by dorigo
        for(int i=0;i<g.getListOfCities().size();i++){ //initialize pheromone matrix with small value of 0.05
            for(int j=0;j<g.getListOfCities().size();j++){
                pheromoneMatrix[i][j] = 0.00005;
            }
        }
    }

    /**
     * This function is used to compute a tour length using the nearest neighbour Heuristic , this is used in order
     * to determine the value of t0. The nearest neighbour heuristic gives a route length starting from any random vertex
     * and then always visiting the nearest vertex with no repetition in the tour until all vertices are visited.
     * @return
     * Returs the tour length using the nearest neighbour heuristic
     */
    private double TourLengthUsingNearestNeighbourHeuristic(){
        Random rand = new Random(System.currentTimeMillis());
        int  startingCityIndex = rand.nextInt(g.getListOfCities().size()); //start from any random vertex
        Ant ant = new Ant(g.getListOfCities().get(startingCityIndex),startingCityIndex);
        while (!ant.completedTour(g)){ //until the ant completes the tour
            int nearestIndex = 0;
            double smallestDistance = Double.MAX_VALUE;
            for(int i=0;i<g.getListOfCities().size();i++){
                if(g.getListOfCities().get(i).getID()==ant.currentCity.getID() || ant.route.visitedCities.contains(g.getListOfCities()
                .get(i))){ //if the vertex has already been visited or is the current vertex ignore it
                }else{
                    if(g.getMatrixOfWeights()[ant.currentCity.getID()-1][i] < smallestDistance){ // if we found a new smaller distance
                        nearestIndex = i; //store the index
                        smallestDistance = g.getMatrixOfWeights()[ant.currentCity.getID()-1][i]; //store the smallest distance for comparison
                    }
                }
            }
            ant.visitCity(g.getListOfCities().get(nearestIndex),g); //visit the nearest city
        }
        return ant.getRouteLength(); //return the route length found
    }

    /**
     * This method creates the ants in the algorithm and gives a random starting position to each ant , in this method
     * it is assumed that an ant can have a same starting position as an other ant.
     */
    private void createAnts(){
        Random rand = new Random(System.currentTimeMillis());
        int startingCityIndex;
        for(int i=0;i<numberOfAnts;i++){
            startingCityIndex = rand.nextInt(g.getListOfCities().size());//get random starting position
            ants.add(new Ant(g.getListOfCities().get(startingCityIndex),startingCityIndex)); //create ant based on this starting position
        }
    }

    /**
     * This method is used to calculate the probability that the ant will visit an unvisited vertex from the current
     * city it is in
     * @param ant
     * Stores the ant in question
     * @param nextCity
     * Stores the next city whose probability of visiting must be calculated
     * @return
     * The probability value of visiting nextCity
     */
    private double probabilityFunction(Ant ant, City nextCity){
        if(ant.route.visitedCities.contains(nextCity)){ //if the city is already visited it must not be chosen , therefore prob 0
            return 0.0;
        }else {
            ArrayList<City> citiesToCalculateFrom = new ArrayList<City>(); //Stores the cities in question
            for (int i = 0; i < g.getListOfCities().size(); i++) { //Determine which cities are to be chosen from
                if (!ant.route.visitedCities.contains(g.getListOfCities().get(i))) {
                    citiesToCalculateFrom.add(g.getListOfCities().get(i));
                }
            }
            double denominator = 0; //stores the denominator of the probability value
            for (City aCitiesToCalculateFrom : citiesToCalculateFrom) { //calculate denominator of probability value as
                                                                        // suggested by Dorigo
                int currentCityIndex = ant.currentCity.getID() - 1;
                int nextCityIndex = aCitiesToCalculateFrom.getID() - 1;
                denominator += pheromoneMatrix[currentCityIndex][nextCityIndex] *
                        Math.pow(1 / g.getMatrixOfWeights()[currentCityIndex][nextCityIndex], Beta);
            }
            //now to calculate the probability only the numerator needs to be calculated
            int currentCityIndex = ant.currentCity.getID() - 1;
            int nextCityIndex = nextCity.getID()-1;
            return (pheromoneMatrix[currentCityIndex][nextCityIndex] *
                    Math.pow(1 / g.getMatrixOfWeights()[currentCityIndex][nextCityIndex], Beta))/denominator;
        }
    }

    /**
     * This function encodes the random variable S in Dorigo's paper when the bias fails , this chooses which city
     * to visit based on roulette wheel selection where the probability value is given by a random variable.
     * @param ant
     * Stores the ant that is being considered
     * @return
     * the index of the chosen vertex to move to
     */
    private int Part2OfProbabilisticFormula(Ant ant){ //mhux cert hawn dit ar roulette
        double val = Math.random();
        double totalProbability = 0.0;
        for (int i=0;i<g.getListOfCities().size();i++) {
            totalProbability += probabilityFunction(ant,g.getListOfCities().get(i));
            if (val <= totalProbability) {
                return i;
            }
        }
        return -1;
    }

    /**
     * This function encodes the first part of the formula suggested by Dorigo whenever the bias does not fail , i.e
     * when q<=q0 . This means that the ant chooses which city to go to depending who has the highest number of
     * pheromone level and shortest distance combination.
     * @param ant
     * Stores the ant being considered
     * @return
     * The index of the chosen city.
     */
    private int Part1OfProbabilisticFormula(Ant ant){
        ArrayList<City> citiesToCalculateFrom = new ArrayList<City>(); //Stores the cities not visited
        for(int i=0;i<g.getListOfCities().size();i++){ //Determine which cities are to be chosen from
            if(!ant.route.visitedCities.contains(g.getListOfCities().get(i))){
                citiesToCalculateFrom.add(g.getListOfCities().get(i));
            }
        }
        double maxValue = -1;
        int maxIndex = -1;
        for (City aCitiesToCalculateFrom : citiesToCalculateFrom) { //calculate the maximum value as suggested by Dorigo
            int currentCityIndex = ant.currentCity.getID() - 1;
            int nextCityIndex = aCitiesToCalculateFrom.getID() - 1;
            double value = pheromoneMatrix[currentCityIndex][nextCityIndex] *
                    Math.pow(1 / g.getMatrixOfWeights()[currentCityIndex][nextCityIndex], Beta);
            if (value > maxValue) {
                maxValue = value;
                maxIndex = aCitiesToCalculateFrom.getID() - 1;
            }
        }
        return maxIndex;
    }

    /**
     * This method is used to encode what happens whenever an ant moves from one city to the next. Whenever this happens
     * local pheromone update must be done and the ant.visitCity() function must be called in order to update the ant's
     * current city index and the ant's route length
     * @param ant
     * Stores the ant in question
     */
    private void moveAnt(Ant ant){
        Random rand = new Random(System.currentTimeMillis());
        int indexOfChosenCity; //stores the index of the chosen city
        double q = rand.nextDouble(); //stores the random number to be compared to the bias
        if(q<=q0){ // if number generated smaller than bias
            indexOfChosenCity = Part1OfProbabilisticFormula(ant); //choose based on the highest combination of pheromone and vicinity
        }else{ // if bias fails
            indexOfChosenCity = Part2OfProbabilisticFormula(ant); //chose based on random variable
        }
        localPheromoneUpdate(ant.currentCity.getID()-1,indexOfChosenCity); //update pheromone level between
        //current index and next city index
        ant.visitCity(g.getListOfCities().get(indexOfChosenCity),g); //update the ant's fields
    }

    /**
     * This encodes the local pheromone update as suggester by Dorigo , note that 2 updates need to be done to the matrix
     * since the problem is symmetric , this could have done better using a linked list . However this gives us faster
     * retrieval and access.
     * @param currentCityIndex
     * Stores the current city index the ant is in
     * @param nextCityIndex
     * Stores the index of the next city to be traversed
     */
    private void localPheromoneUpdate(int currentCityIndex,int nextCityIndex){
        pheromoneMatrix[currentCityIndex][nextCityIndex] = (1-alpha)*pheromoneMatrix[currentCityIndex][nextCityIndex]
                + alpha*t0;
        pheromoneMatrix[nextCityIndex][currentCityIndex] = (1-alpha)*pheromoneMatrix[nextCityIndex][currentCityIndex]
                + alpha*t0;
    }

    /**
     * This function encodes the global pheromone update as suggester by Dorigo , again the matrix is updated twice for
     * the reason mentioned above in the local pheromone update.
     * @param currentCityIndex
     * Stores the index of the current city the ant is in.
     * @param nextCityIndex
     * Stores the index of the city to be visited
     * @param shortestTourLength
     * Stores the length of the shortest tour by the ants in the same iteration.
     */
    private void globalPheromoneUpdate(int currentCityIndex,int nextCityIndex,double shortestTourLength){
        pheromoneMatrix[currentCityIndex][nextCityIndex] = (1-alpha)*pheromoneMatrix[currentCityIndex][nextCityIndex]
                + alpha*(1/shortestTourLength);
        pheromoneMatrix[nextCityIndex][currentCityIndex] = (1-alpha)*pheromoneMatrix[nextCityIndex][currentCityIndex]
                + alpha*(1/shortestTourLength);
    }

    /**
     * This method encodes all the AC0 logic as suggested by Dorigo , the approach is such that each ant at every time
     * step moves one step updating locally the edge's used pheromone level . This continues until all ants have completed
     * the tour. When this happens the ant who gave the best tour globally updates the pheromone of it's edges used. Then
     * the shortest path is recorded so that from each iteration to the next the shortest path found is not lost.
     * @return
     * The shortest path found in all iterations of the algorithm
     */

    public double ACO_Engine(){
        double answer =Double.MAX_VALUE; //stores the shortest path solution
        for(int i=0;i<100000;i++){ //for a predefined number of iterations
            createAnts(); //create new ants so that they are given new starting cities
            int antsCompletedTour = 0; //stores the number of ants that completed a tour
            while(antsCompletedTour < numberOfAnts){ //while not all ants completed the tour
                for(int j=0;j<numberOfAnts;j++){ //move each ant to a new city
                    if(!ants.get(j).completedTour(g)){
                        moveAnt(ants.get(j));
                    }else{
                        antsCompletedTour++;
                    }
                }
            }
            double smallestDistance = Double.MAX_VALUE; //stores the smallest distance found in some iteration
            int bestAntIndex = -1; //stores the index of the ant that found the shortest path in some iteration
            for(int j=0;j<numberOfAnts;j++){
                if(ants.get(j).getRouteLength()<smallestDistance){ // get smallest distance in current iteration
                    bestAntIndex = j; //store it's index
                    smallestDistance = ants.get(j).getRouteLength(); //store smallest distance
                }
            }
            if(smallestDistance<answer){ //if smallest distance in current iteration is better than global update global
                answer = smallestDistance;
            }
            System.out.println("iteration "+i+"smallest distance : "+answer); //jew smallest disntace
            for(int j=0;j<ants.get(bestAntIndex).route.visitedCities.size()-1;j++){
                //globally update the pheromone levels of edges used by the best any
                int currentCityIndex = ants.get(bestAntIndex).route.visitedCities.get(j).getID()-1;
                int nextCityIndex = ants.get(bestAntIndex).route.visitedCities.get(j+1).getID()-1;
                globalPheromoneUpdate(currentCityIndex,nextCityIndex,ants.get(bestAntIndex).getRouteLength());
            }
            //update the pheromone level from the last city to the starting city (to go back)
            int currentCityIndex = ants.get(bestAntIndex).route.visitedCities.get(
                    ants.get(bestAntIndex).route.visitedCities.size()-1).getID()-1;
            int nextCityIndex = ants.get(bestAntIndex).route.visitedCities.get(0).getID()-1;
            globalPheromoneUpdate(currentCityIndex,nextCityIndex,ants.get(bestAntIndex).getRouteLength());
            ants.clear();
        }
        return answer;
    }
}

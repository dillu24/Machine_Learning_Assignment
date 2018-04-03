package ACO;
import TSP_Graph.City;
import TSP_Graph.Graph;

/**
 * This class encodes the Ant for the Ant Colony optimization algorithm
 */

class Ant {
    /**
     * Each ant has a route so that it keeps track of the cities already visited ,
     * currentCity in order to update the pheromone levels
     * a startingCityIndex to keep track of the starting city so that the ant can go back to the starting city and thus
     * update the pheromone of the last edge.
     */
    ACORoute route;
    City currentCity;
    int startingCityIndex;

    /**
     * This constructor is used to initialize an ant giving it a starting city and store the require information in the
     * Ant's fields.
     * @param startCity
     * Stores the city value where the ant will start it's exploration.
     * @param startingCityIndex
     * Stores the startingCityIndex value where the ant will start it's exploration.
     */

    Ant(City startCity, int startingCityIndex){
        route = new ACORoute();
        route.visitedCities.add(startCity); // mark starting city as visited
        currentCity = startCity; //make the current city the starting city
        this.startingCityIndex = startingCityIndex; //update index accordingly
    }

    /**
     * This method is used to move from one city to the next updating city related fields and information such as the
     * current route length so far that only concern the Ant. Note that pheromone update is done in the algorithm not
     * by the ant.
     * @param city
     * Stores the city to be visited next
     * @param g
     * Stores the graph we are traversing , this is needed to get the edge weight since we the route length is updated
     */
    void visitCity(City city, Graph g){
        route.visitedCities.add(city);
        route.setRouteLength(route.getRouteLength()+g.getMatrixOfWeights()[currentCity.getID()-1][city.getID()-1]);
        currentCity = city;
    }

    /**
     * This method is used in the game engine in order to check if the ant completed a tour . This happens whenever the
     * visited cities has size equal to the number of cities in the graph.
     * @param g
     * The graph who's size needs to be checked
     * @return
     * True  -> if ant completed tour
     * False -> otherwise
     */

    boolean completedTour(Graph g){
        if (g.getListOfCities().size() == route.visitedCities.size()){
            route.setRouteLength(route.getRouteLength() + g.getMatrixOfWeights()[currentCity.getID()-1][startingCityIndex]);
            return true;
        }else{
            return false;
        }
    }

    /**
     * This getter gets the route length
     * @return
     * The route length
     */
    double getRouteLength(){
        return route.getRouteLength();
    }
}

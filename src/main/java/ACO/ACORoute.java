package ACO;

import TSP_Graph.City;
import java.util.ArrayList;

/**
 * This class encodes a route for ACO , it contains a list of cities that have already been visited and the current route
 * length
 */

class ACORoute {
    /**
     * visitedCities -> Stores the cities visited so far
     * routeLength   -> Stores the current route length
     */
    ArrayList<City> visitedCities;
    private double routeLength;

    /**
     * This constructor is used to initialize a new route having no cities visited and route length 0.
     */

    ACORoute(){
        visitedCities = new ArrayList<City>();
        routeLength = 0.0;
    }

    /**
     * This method is used as a setter to give the route length a value
     * @param value
     * Stores the route length that will be assigned to this.routeLength
     */

    void setRouteLength(double value){
        routeLength = value;
    }

    /**
     * This is a getter and is used to get the route length since this.routeLength is set to private
     * @return
     * The value of this.routeLength
     */

    double getRouteLength(){
        return routeLength;
    }
}

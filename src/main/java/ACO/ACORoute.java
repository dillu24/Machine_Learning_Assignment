package ACO;

import TSP_Graph.City;

import java.util.ArrayList;

/**
 * Created by Dylan Galea on 06/03/2018.
 */

class ACORoute {
    ArrayList<City> visitedCities;
    private double routeLength;

    ACORoute(){
        visitedCities = new ArrayList<City>();
        routeLength = 0.0;
    }

    void setRouteLength(double value){
        routeLength = value;
    }

    double getRouteLength(){
        return routeLength;
    }
}

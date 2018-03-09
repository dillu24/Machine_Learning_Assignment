package ACO;


import TSP_Graph.City;
import TSP_Graph.Graph;


/**
 * Created by Dylan Galea on 06/03/2018.
 */
public class Ant {
    protected ACORoute route;
    protected City currentCity;
    protected int startingCityIndex;

    public Ant(){
        route = new ACORoute();
        currentCity = null;
        startingCityIndex = -1;
    }

    public Ant(City startCity,int startingCityIndex){
        route = new ACORoute();
        route.visitedCities.add(startCity);
        currentCity = startCity;
        this.startingCityIndex = startingCityIndex;
    }

    public void visitCity(City city, Graph g){
        route.visitedCities.add(city);
        route.setRouteLength(route.getRouteLength()+g.getMatrixOfWeights()[currentCity.getID()-1][city.getID()-1]);
        currentCity = city;
    }

    public boolean completedTour(Graph g){
        if (g.getListOfCities().size() == route.visitedCities.size()){
            route.setRouteLength(route.getRouteLength() + g.getMatrixOfWeights()[currentCity.getID()-1][startingCityIndex]);
            return true;
        }else{
            return false;
        }
    }

    public double getRouteLength(){
        return route.getRouteLength();
    }
}

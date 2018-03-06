package ACO;


import TSP_Graph.City;
import TSP_Graph.Graph;

/**
 * Created by Dylan Galea on 06/03/2018.
 */
public class Ant {
    private ACORoute route;
    protected City currentCity;

    public Ant(){
        route = new ACORoute();
        currentCity = null;
    }

    public Ant(City startCity){
        route = new ACORoute();
        route.visitedCities.add(startCity);
        currentCity = startCity;
    }

    public void visitCity(City city, Graph g){
        route.visitedCities.add(city);
        route.setRouteLength(route.getRouteLength()+g.getMatrixOfWeights()[currentCity.getID()-1][city.getID()-1]);
        currentCity = city;
    }

    public boolean completedTour(Graph g){
        if (g.getListOfCities().size() == route.visitedCities.size()){
            return true;
        }else{
            return false;
        }
    }

    public double getRouteLength(){
        return route.getRouteLength();
    }
}

import java.util.ArrayList;

/**
 * Created by Dylan Galea on 28/02/2018.
 */
public class Graph {
    private ArrayList<City> listOfCities;
    private double matrixOfWeights[][];

    //ToDo make getters and setters of both array list and map , and do constructors etc.

    public double geometricDistanceBetween2Cities(City city1,City city2){
        return 0.0; //ToDo make the geometric distance function
    }

    public double euclideanDistanceBetween2Cities(City city1,City city2){
        return Math.sqrt(square(city1.getX()-city2.getX())+
                square(city1.getY()-city2.getY()));
    }

    private double square(double input){
        return input*input;
    }
}

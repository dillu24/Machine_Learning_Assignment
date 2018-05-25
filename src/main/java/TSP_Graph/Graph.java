package TSP_Graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.System.exit;

/**
 * This class defines the graph class which is used to encode the Complete Graph of the different cities that were
 * inputted from the TSP file instance . The distances between the cities can be encoded both in Euclidean distance
 * and geometric distance as if the points given in the TSP instance file are co-ordinates on the globe. The graph
 * class also keeps a list of cities that are encoded from the TSP instance file . It also contains a 2D matrix of
 * distances between cities , where the index of the matrix gives the ID of the city being encoded.Note that this
 * might not be the most efficient way to encode the distances , however since the matrix of weights does not change
 * it gives the algorithms faster access to the distances without the need to compute at every step the distance between
 * each city.
 */

public class Graph {

    /**
     * The listOfCities array list stores the cities that are being encoded from the TSP instance file.
     * The matrixOfWeights 2D Array stores the distances between the cities were each city is identified by it's index
     * in the 2D array . Thus since our TSP is symmetric this stores double the values it should store , however as
     * already described it will give us faster access.
     */

    private ArrayList<City> listOfCities = new ArrayList<City>();
    private double matrixOfWeights[][];

    /**
     * The constructor used to create the graph given the TSP instance file. Use this path to create the list of cities
     * and then to set the matrix of weights in any distance metric desired.
     * @param pathname
     * The pathname parameter stores the pathname of the file from which the data will be generated
     */

    public Graph(File pathname){
        setListOfCities(pathname);
        setMatrixOfWeightsGeometric();
    }

    /**
     * The setListOfCities method initializes the list of cities depending on the file instance found
     * @param pathname
     * Stores the path name of the file to be read
     */

    private void setListOfCities(File pathname){
        try{
            Scanner in = new Scanner(pathname); //create a new scanner instance to read line by line

            String line = "";

            while(!line.equals("NODE_COORD_SECTION")){
                line = in.nextLine(); //Skip lines until meeting the NODE_COORD_SECTION line which means that
                                      // co-ordinate data is next to be read.
            }

            line = in.nextLine(); //Read next line which contains co-ordinate data

            while(!line.equals("EOF")){ //Until an EOF line is met
                String splitString[] = line.split(" "); // Split the line read according to spaces found
                String numbersOnly[] = new String[3]; //store only the numbers in this array , where according to the
                                                      //file instance the only numbers are the id value and x,y co ordinates
                int j =0;
                for (String aSplitString : splitString) {
                    if (!aSplitString.equals("")) { //store numbers in number array
                        numbersOnly[j] = aSplitString; //To avoid having "" as input
                        j++;
                    }
                }

                int newID = Integer.parseInt(numbersOnly[0]); // convert id from string to number
                double newX = Double.parseDouble(numbersOnly[1]); //convert x-co ordinate from string to number
                double newY = Double.parseDouble(numbersOnly[2]); //convert y-co ordinate from string to number
                listOfCities.add(new City(newX,newY,newID)); //add the new city to the list of cities depending on the
                                                             //  read input
                line = in.nextLine(); // read next line
            }
        } catch (FileNotFoundException fnf) { // if the file could not be opened an error is displayed to the user
            System.out.println("Error opening file");
            exit(-1);
        }
    }

    /**
     * This method is used to set the distance 2D array in geometric distances , this is used whenever we have
     * geo type distances in the TSP instance file , i.e we have actual co-ordinates on the globe.
     */

    public void setMatrixOfWeightsGeometric(){
        matrixOfWeights = new double[listOfCities.size()][listOfCities.size()];
        for(int i=0;i<listOfCities.size();i++){
            for(int j=0;j<listOfCities.size();j++){
                matrixOfWeights[i][j] = geometricDistanceBetween2Cities(listOfCities.get(i),listOfCities.get(j));
            }
        }
    }

    /**
     * This method is used to set the distance 2D array in euclidean distances , this is used whenever we have
     * EUC_2D type distances in the TSP instance file , i.e we have actual co-ordinates in the plane.
     */

    private void setMatrixOfWeightsEuclidean(){
        matrixOfWeights = new double[listOfCities.size()][listOfCities.size()];
        for(int i=0;i<listOfCities.size();i++){
            for(int j=0;j<listOfCities.size();j++){
                matrixOfWeights[i][j] = euclideanDistanceBetween2Cities(listOfCities.get(i),listOfCities.get(j));
            }
        }
    }

    /**
     * This method is used to return the list of cities due to the fact that it has private access
     * @return
     * The list of cities read from the TSP instance file
     */

    public ArrayList<City> getListOfCities(){
        return listOfCities;
    }

    /**
     * This method is used to return the 2D array of distances due to the fact that is has private access
     * @return
     * The 2D array of distances.
     */

    public double[][] getMatrixOfWeights(){
        return matrixOfWeights;
    }

    /**
     * This method is used to calculate the geometric distance between 2 cities on the globe
     * @param city1
     * Stores one of the cities whose distance from another city will be calculated
     * @param city2
     * Stores one of the cities whose distance from another city will be calculated
     * @return
     * The geometric distance between the 2 cities.
     */


    private double geometricDistanceBetween2Cities(City city1, City city2){
        //First convert each x and y co ordinate of the 2 cities to radian and put them in separate arrays
        double cityX[] = {computeCoordinateToRadian(city1.getX()),computeCoordinateToRadian(city2.getX())};
        double cityY[] = {computeCoordinateToRadian(city1.getY()),computeCoordinateToRadian(city2.getY())};
        double RRR = 6378.388; // constant used to compute

        double q1 = Math.cos(cityY[0]-cityY[1]); //compute the distance as specified by TSPLIB FAQ
        double q2 = Math.cos(cityX[0]-cityX[1]);
        double q3 = Math.cos(cityX[0]+cityX[1]);
        return (int)( RRR * Math.acos( 0.5*((1.0+q1)*q2 - (1.0-q1)*q3) ) + 1.0); //return the distance
    }

    /**
     * This method is used to convert the x-co ordinates to radians
     * @param input
     * stores the input to be converted
     * @return
     * The corresponding radian value.
     */

    private double computeCoordinateToRadian(double input){
        int degree = (int) input; //Convert as specified in TSPLIB FAQ
        double min = input-degree;
        return Math.PI * (degree + 5.0*(min/3.0)) / 180.0;
    }

    /**
     * This method computes the euclidean distances between 2 cities
     * @param city1
     * Stores one of the cities whose distance from another city will be calculated
     * @param city2
     * Stores one of the cities whose distance from another city will be calculated
     * @return
     * The euclidean distance between these 2 cities
     */

    private double euclideanDistanceBetween2Cities(City city1, City city2){
        return Math.round(Math.sqrt(square(city1.getX()-city2.getX())+
                square(city1.getY()-city2.getY())));
    }

    /**
     * This method computes the square of a number, which is used to compute the euclidean distance
     * @param input
     * The value to be squared
     * @return
     * The squared value of the given input
     */

    private double square(double input){
        return input*input;
    }
}

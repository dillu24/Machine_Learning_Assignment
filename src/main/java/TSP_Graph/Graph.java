package TSP_Graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Dylan Galea on 28/02/2018.
 */
public class Graph {
    //ToDo After make matrix direct input aswell

    private ArrayList<City> listOfCities = new ArrayList<City>();
    private double matrixOfWeights[][];

    public Graph(){}

    public Graph(File pathname){
        setListOfCities(pathname);
        setMatrixOfWeightsEuclidean();
    }

    public void setListOfCities(File pathname){
        try{
            Scanner in = new Scanner(pathname);

            String line = "";

            while(!line.equals("NODE_COORD_SECTION")){
                line = in.nextLine();
            }

            line = in.nextLine();

            while(!line.equals("EOF")){
                String splitString[] = line.split(" ");
                String numbersOnly[] = new String[3];
                int j =0;
                for(int i=0;i<splitString.length;i++){
                    if(!splitString[i].equals("")){
                        numbersOnly[j] = splitString[i]; //To avoid having "" as input
                        j++;
                    }
                }

                int newID = Integer.parseInt(numbersOnly[0]);
                double newX = Double.parseDouble(numbersOnly[1]);
                double newY = Double.parseDouble(numbersOnly[2]);
                listOfCities.add(new City(newX,newY,newID));
                line = in.nextLine();
            }
        } catch (FileNotFoundException fnf) {
            System.out.println("Error opening file");
        }
    }

    public void setMatrixOfWeightsGeometric(){
        matrixOfWeights = new double[listOfCities.size()][listOfCities.size()];
        for(int i=0;i<listOfCities.size();i++){
            for(int j=0;j<listOfCities.size();j++){
                matrixOfWeights[i][j] = geometricDistanceBetween2Cities(listOfCities.get(i),listOfCities.get(j));
            }
        }
    }

    private void setMatrixOfWeightsEuclidean(){
        matrixOfWeights = new double[listOfCities.size()][listOfCities.size()];
        for(int i=0;i<listOfCities.size();i++){
            for(int j=0;j<listOfCities.size();j++){
                matrixOfWeights[i][j] = euclideanDistanceBetween2Cities(listOfCities.get(i),listOfCities.get(j));
            }
        }
    }

    public ArrayList<City> getListOfCities(){
        return listOfCities;
    }

    public double[][] getMatrixOfWeights(){
        return matrixOfWeights;
    }


    private double geometricDistanceBetween2Cities(City city1, City city2){
        //ToDo make the geometric distance function
        double cityX[] = {computeCoordinateToRadian(city1.getX()),computeCoordinateToRadian(city2.getX())};
        double cityY[] = {computeCoordinateToRadian(city1.getY()),computeCoordinateToRadian(city2.getY())};
        double RRR = 6378.388;

        double q1 = Math.cos(cityY[0]-cityY[1]);
        double q2 = Math.cos(cityX[0]-cityX[1]);
        double q3 = Math.cos(cityX[0]+cityX[1]);
        return (int)( RRR * Math.acos( 0.5*((1.0+q1)*q2 - (1.0-q1)*q3) ) + 1.0);
    }

    private double computeCoordinateToRadian(double input){
        int degree = (int) input;
        double min = input-degree;
        return Math.PI * (degree + 5.0*(min/3.0)) / 180.0;
    }

    private double euclideanDistanceBetween2Cities(City city1, City city2){
        return Math.round(Math.sqrt(square(city1.getX()-city2.getX())+
                square(city1.getY()-city2.getY())));
    }

    private double square(double input){
        return input*input;
    }
}

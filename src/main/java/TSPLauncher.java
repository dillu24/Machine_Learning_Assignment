import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Dylan Galea on 28/02/2018.
 */
public class TSPLauncher {
    public static void main(String args[]) {
        Graph g = new Graph(new File(
                "C:/Users/Dylan Galea/IdeaProjects/MachineLearning/src/main/java/burma14.tsp"));
        ArrayList<City> listOfCities = g.getListOfCities();
        double matrixOfWeights[][] = g.getMatrixOfWeights();
        for(int i=0;i<listOfCities.size();i++){
            System.out.println(listOfCities.get(i).getID()+" "+listOfCities.get(i).getX()+" "
                    +listOfCities.get(i).getY());
        }

        for(int i=0;i<listOfCities.size();i++){
            for(int j=0;j<listOfCities.size();j++){
                System.out.print(matrixOfWeights[i][j]+" ");
            }
            System.out.println();
        }

    }
}


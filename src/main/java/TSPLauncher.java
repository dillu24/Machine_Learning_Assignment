import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Dylan Galea on 28/02/2018.
 */
public class TSPLauncher {
    public static void main(String args[]) {
        //ToDo add this in graph.
        ArrayList<City> listOfCities = new ArrayList<City>();
        try{
            Scanner in = new Scanner(new File
                    ("C:/Users/Dylan Galea/IdeaProjects/MachineLearning/src/main/java/burma14.tsp"));

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

            for(int i=0;i<listOfCities.size();i++){
                System.out.println(listOfCities.get(i).getID()+" "+listOfCities.get(i).getX()+" "
                        +listOfCities.get(i).getY());
            } //For test only
        } catch (FileNotFoundException fnf) {
            System.out.println("Error opening file");
        }

    }
}


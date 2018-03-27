package TSP_Graph;

/**
 * The City class which encodes the co-ordinates of the different cities in the TSP file being inputted and giving
 * some identification to each city according to the order they appear in the file . Some methods are also created
 * in order to retrieve and set the data.Note that in this implementation only TSP instance files that support
 * co-ordinates can be used , other files that support input from matrices cannot be used for correct use.
 */

public class City {
    /**
     * Variable ID Stores the identification number of each city , i.e the order in which they appear in the TSP
     * Instance file
     * Variable x Stores the x-co ordinates of the TSP file instance
     * Variable y stores the y-co ordinates of the TSP file instance
     */
    private int ID;
    private double x;
    private double y;

    /**
     * The constructor used to initialise a city directly with the co-ordinate values and the identification value
     * @param x
     * Stores the x-co ordinate that will be assigned to the new city
     * @param y
     * Stores the y-co ordinate that will be assigned to the new city
     * @param ID
     * Stores the city identification value that will be assigned to the new city.
     */

    City(double x, double y, int ID){
        setX(x);
        setY(y);
        setID(ID);
    }

    /**
     * The setX method is used to assign the x-co ordinate variable new values since the access type is private
     * @param value
     * Stores the x-co ordinate value that will be assigned to the city's x-co ordinate variable.
     */

    public void setX(double value){
        x = value;
    }

    /**
     * The setID method is used to assign the ID variable new values since the access type is private
     * @param ID
     * Stores the ID value that will be assigned to the city's ID variable
     */

    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * The setY method is used to assign the y-co ordinate variable new values since the access type is private
     * @param value
     * Stores the y-co ordinate value that will be assigned to the city's y-co ordinate variable.
     */

    public void setY(double value){
        y = value;
    }

    /**
     * The getX method is used to get the x-co ordinate value of the City
     * @return
     * The x-co ordinate value of the city
     */

    public double getX(){
        return x;
    }

    /**
     * The getY method is used to get the y-co ordinate value of the City
     * @return
     * The y-co ordinate value of the city
     */

    public double getY(){
        return y;
    }

    /**
     * The getID method is used to get the ID value of the City
     * @return
     * The ID value of the city
     */

    public int getID(){
        return ID;
    }
}

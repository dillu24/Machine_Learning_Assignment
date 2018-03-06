package TSP_Graph;

/**
 * Created by Dylan Galea on 28/02/2018.
 */
public class City {
    private int ID;
    private double x;
    private double y;

    public City(){}

    public City(double x, double y, int ID){
        setX(x);
        setY(y);
        setID(ID);
    }

    public void setX(double value){
        x = value;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setY(double value){
        y = value;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public int getID(){
        return ID;
    }
}

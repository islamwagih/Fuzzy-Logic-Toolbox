package FuzzySystem;


enum FuzzyType{
    TRI, TRAP
}
public class FuzzySet {
    public String name;
    public FuzzyType type;
    public double values[];
    FuzzySet(String name, String Type, double values[]){

        this.name = name;

        if(Type.equalsIgnoreCase("TRI")) this.type = FuzzyType.TRI;
        else this.type = FuzzyType.TRAP;

        this.values = values;


    }

    public double getMemberShipValue(double val) {
        if(inRange(val, values[0], values[values.length-1] )){
            for(int i =0 ; i < values.length-1 ; i++){
                if(inRange(val, values[i], values[i+1])){
                    return getIntersection(val, i, i+1);
                }
            }
        }
        return 0.0;
    }

    private double getIntersection(double val, int i1, int i2) {
        int y1 = this.type == FuzzyType.TRI ? i1 == 1 ? 1 : 0 : i1 == 1 || i1 == 2 ? 1 : 0;
        int y2 = this.type == FuzzyType.TRI ? i2 == 1 ? 1 : 0 : i2 == 1 || i2 == 2 ? 1 : 0;
        double x1 = values[i1];
        double x2 = values[i2];
        double m = (y1 - y2)/(x1 - x2);
        double yIntersection = y1 - ((x1 - val) * m);
        return yIntersection;
    }

    private boolean inRange(double val, double min, double max) {
        if(val >= min && val <= max) return true;
        return false;
    }
}

package FuzzySystem;

import java.util.ArrayList;
import java.util.Scanner;

enum VarType{
    IN, OUT
}
public class Variable
{
    public String name;
    public VarType type;
    public double min, max;

    public Membership[] memberships;
    public ArrayList<FuzzySet> fuzzySets;
    private double curValue;
    Variable(){
        fuzzySets = new ArrayList<FuzzySet>();
        curValue = -1.0;
    }

    public void addFuzzySet(String line) {
            String info[] = line.split(" ");
            String fuzzyName = info[0];
            String type = info[1];
            if(type.equalsIgnoreCase("TRI") || type.equalsIgnoreCase("TRAP")){
                fuzzySets.add(new FuzzySet(fuzzyName, type, getValues(info)));
            }
            else {
                System.out.println("Invalid Type");
            }

    }
    private boolean inRange(double value){
        if(value>max ||value<min)return false;
        return true;
    }

    private double[] getValues(String[] info) {
        double[] result = new double[info.length-2];
        for(int i =2 ; i < info.length ; i++){
            double value = Double.parseDouble(info[i]);
            if(inRange(value))  result[i-2] = value;
            else {
                throw new RuntimeException("the value out of range");
            }
        }
        return result;
    }

    public void setMembershipsForValue(double val){
        curValue = val;
        memberships = new Membership[fuzzySets.size()];
        for(int i =0; i < memberships.length ; i++){
            if(val == -1) {
                memberships[i] = new Membership(fuzzySets.get(i).name, -1);
            }
            memberships[i] = new Membership(fuzzySets.get(i).name, fuzzySets.get(i).getMemberShipValue(val));
        }
    }
    public String GetHighestMember(){
        double max = memberships[0].degreeOfMembership;
        String mem = memberships[0].name;
        for(int i =1 ; i < memberships.length ; i++){
            if(memberships[i].degreeOfMembership > max) {
                max = memberships[i].degreeOfMembership;
                mem = memberships[i].name;
            }
            else if (memberships[i].degreeOfMembership == max)
                mem += ", " + memberships[i].name;
        }
        return mem;
    }

}

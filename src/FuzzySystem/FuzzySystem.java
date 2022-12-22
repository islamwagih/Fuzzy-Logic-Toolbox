package FuzzySystem;

import java.io.Console;
import java.text.DecimalFormat;
import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.Scanner;

public class FuzzySystem {
    private String name;
    private String description;
    private ArrayList<Variable> variables;
    private Scanner scanner;
    private ArrayList<FuzzyRule> rules;

    private double values[];
    private boolean fuzzySetsAdded;
    FuzzySystem(String name, String description){
        this.name = name;
        this.description = description;
        variables = new ArrayList<Variable>();
        rules = new ArrayList<FuzzyRule>();
        fuzzySetsAdded = false;
    }

    public void MainMenu() {
        boolean exit=false;
        scanner = new Scanner(System.in);
        while (!exit) {


            System.out.println("FuzzySystem.Main Menu:\n" +
                    "==========\n" +
                    "1- Add variables.\n" +
                    "2- Add fuzzy sets to an existing variable.\n" +
                    "3- Add rules.\n" +
                    "4- Run the simulation on crisp values.");
            String read = scanner.nextLine();
            if(read.equalsIgnoreCase("close")){
                exit = true;
                continue;
            }
            int answer = Integer.parseInt(read);

            switch (answer) {
                case 1: {
                    System.out.println("Enter the variable’s name, type (IN/OUT) and range ([lower, upper]):" +
                            "(Press x to finish)");
                    while (true) {
                        String line = scanner.nextLine();
                        if (line.equalsIgnoreCase("x")) break;
                        String info[] = line.split(" ");

                        if (info.length != 4) {
                            System.out.println("Please enter valid information");
                            continue;
                        }
                        Variable newVariable = getVariableWithName(info[0]);
                        if (newVariable == null) {
                            newVariable = new Variable();
                            //assigning name
                            newVariable.name = info[0];
                            //assigning type
                            if (info[1].equalsIgnoreCase("IN")) {
                                newVariable.type = VarType.IN;
                            } else if (info[1].equalsIgnoreCase("OUT")) {
                                newVariable.type = VarType.OUT;
                            } else {
                                System.out.println("Invalid input");
                                continue;
                            }
                            //assigning range
                            String range[] = {info[2], info[3]};
                            String low = range[0].replace("[", "");
                            //low.replace(",")
                            low = low.substring(0, low.length() - 1);

                            newVariable.min = Double.parseDouble(low);
                            String high = range[1].replace("]", "");
                            newVariable.max = Double.parseDouble(high);

                        } else {
                            System.out.println("Already Exists");
                            continue;
                        }
                        variables.add(newVariable);
                    }
                    break;
                }
                case 2: {
                    System.out.println("Enter the variable’s name:");
                    System.out.println("--------------------------");
                    String line;
                    String name = scanner.nextLine();
                    Variable var = getVariableWithName(name);
                    if (var != null) {
                        System.out.println("Enter the fuzzy set name, type (TRI/TRAP) and values: (Press x to finish)\n" +
                                "-----------------------------------------------------");
                        while (true) {
                            line = scanner.nextLine();
                            if (!line.equalsIgnoreCase("x")) {
                                try {
                                    var.addFuzzySet(line);
                                    fuzzySetsAdded = true;
                                } catch (RuntimeException e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                            else {
                                break;
                            }
                        }
                    } else {
                        System.out.println("the variable doesn't exist");
                    }
                    break;
                }
                case 3: {

                    System.out.println("Enter the rules in this format: (Press x to finish)\n" +
                            "IN_variable set operator IN_variable set => OUT_variable set");
                    String line = scanner.nextLine();
                    while (!line.equalsIgnoreCase("x")) {
                        String tokens[] = line.split(" ");
                        FuzzyRule currRule = new FuzzyRule();
                        int i = 0, arrow = 0;
                        for (int j = 0; j < tokens.length; j++) {
                            if (tokens[j].equals("=>")) {
                                arrow = j;
                                break;
                            }
                        }
                        //input
                        while (i < arrow) {
                            Variable currVar = getVariableWithName(tokens[i]);
                            if (currVar == null) {

                                System.out.println("Unmatched variable name");
                                break;
                            }
                            //go to value
                            i++;
                            currRule.addInputRule(currVar.name, tokens[i]);
                            //go to optional symbol
                            i++;
                            if (i < arrow && (tokens[i].equalsIgnoreCase("or") || tokens[i].equalsIgnoreCase("and")
                                    || tokens[i].equalsIgnoreCase("or_not") || tokens[i].equalsIgnoreCase("and_not"))) {
                                currRule.addInSymbol(tokens[i]);
                                i++;
                            }
                        }

                        //ouput
                        i = arrow + 1;
                        while (i < tokens.length) {
                            Variable currVar = getVariableWithName(tokens[i]);
                            if (currVar == null) {
                                System.out.println("Unmatched variable name");
                                break;
                            }
                            //go to value
                            i++;
                            currRule.addOutputRule(currVar.name, tokens[i]);
                            //go to optional symbol
                            i++;
                            if (i < tokens.length && (tokens[i].equalsIgnoreCase("or") || tokens[i].equalsIgnoreCase("and")
                                    || tokens[i].equalsIgnoreCase("or_not") || tokens[i].equalsIgnoreCase("and_not"))) {
                                currRule.addOutSymbol(tokens[i]);
                                i++;
                            }
                        }
                        rules.add(currRule);
                        line = scanner.nextLine();
                    }
                    break;
                }
                case 4: {
                    if (variables.size() < 1 || rules.size() < 1 || !fuzzySetsAdded) {
                        System.out.println("CAN’T START THE SIMULATION! Please add the fuzzy sets and rules first.");
                        break;
                    }
                    int inVariablesSize = 0;
                    for(Variable variable : variables)
                        if(variable.type == VarType.IN)
                            inVariablesSize++;

                    values = new double[inVariablesSize];
                    System.out.println("Enter the crisp values:\n" +
                            "-----------------------");
                    //getting the crisp values for our variables.
                    int index =0;
                    for(Variable variable : variables){
                        if(variable.type == VarType.OUT) continue;
                        System.out.print(variable.name + ": ");
                        values[index++] = Double.parseDouble(scanner.nextLine());
                    }
                    index =0;
                    System.out.println("Running the simulation...");

                    //Fuzzification
                    for (int i = 0; i < variables.size(); i++) {
                        if(variables.get(i).type == VarType.OUT)    variables.get(i).setMembershipsForValue(-1);
                        else    variables.get(i).setMembershipsForValue(values[index++]);
                    }
                    System.out.println("Fuzzification => done");

                    //Inference
                    inference();
                    System.out.println("Inference => done");

                    //Defuzzification
                    String outPut = defuzzification();
                    System.out.println("Defuzzification => done");

                    //print predicated output.
                    System.out.print(outPut);
                    break;
                }
                default: {
                    exit=true;
                    break;
                }
            }
        }
}
        private Variable getVariableWithName (String s){
            for (int i = 0; i < variables.size(); i++) {
                if (variables.get(i).name.equals(s)) {
                    return variables.get(i);
                }
            }
            return null;
        }
        private double getMembershipWithName (String s, Variable var){
            for (int i = 0; i < var.memberships.length; i++) {
                if (var.memberships[i].name.equalsIgnoreCase(s)) {
                    return var.memberships[i].degreeOfMembership;
                }
            }
            return 0.0;
        }
        private double getDegree (String name, String val){
            return getMembershipWithName(val, getVariableWithName(name));
        }
        private Membership getHighestmember (Variable var){
            Membership result = new Membership("temp", -55555);
            for (Membership mem : var.memberships) {
                if (mem.degreeOfMembership > result.degreeOfMembership) {
                    result = mem;
                }
            }
            return result;
        }
        private void inference () {
            double runningValue;
            String symbol;
            for (FuzzyRule rule : rules) {
                runningValue = getDegree(rule.input.get(0).name, rule.input.get(0).value);
                for (int i = 1; i < rule.input.size(); i++) {
                    symbol = rule.inSymbol.get(i - 1);
                    if (symbol.equalsIgnoreCase("or")) {
                        runningValue = Math.max(runningValue, getDegree(rule.input.get(i).name, rule.input.get(i).value));
                    } else if (symbol.equalsIgnoreCase("or_not")) {
                        runningValue = 1 - Math.max(runningValue, getDegree(rule.input.get(i).name, rule.input.get(i).value));
                    } else if (symbol.equalsIgnoreCase("and")) {
                        runningValue = Math.min(runningValue, getDegree(rule.input.get(i).name, rule.input.get(i).value));
                    } else if (symbol.equalsIgnoreCase("and_not")) {
                        runningValue = Math.min(runningValue, 1 - getDegree(rule.input.get(i).name, rule.input.get(i).value));
                    }
                }
                Membership members[] = getVariableWithName(rule.output.get(0).name).memberships;
                for (int i = 0; i < members.length; i++) {
                    Membership member = members[i];
                    if (member.name.equalsIgnoreCase(rule.output.get(0).value)) {
                        if(runningValue > member.degreeOfMembership)
                            member.degreeOfMembership = runningValue;
                        break;
                    }

                }
            }

        }
    private ArrayList<Double> calculateCenter(Variable var){
        ArrayList<Double> result=new ArrayList<>();
        double sum;
        for (FuzzySet set:var.fuzzySets)
        {
            sum=0.0;
            for(int j=0;j<set.values.length;j++){
                sum+=set.values[j];
            }
            result.add(sum/set.values.length);
        }
        return result;
    }
    private double getpridict (Variable var){
        double result=0.0;
        double divided=0.0;
        ArrayList<Double> center = calculateCenter(var);
        for (int i=0;i<center.size();i++){
            result+=center.get(i)*var.memberships[i].degreeOfMembership;
            divided+=var.memberships[i].degreeOfMembership;
        }
        return result/divided;
    }
    private String defuzzification () {
        StringBuilder s = new StringBuilder();
        Membership mem;
        for (Variable var : variables) {
            if (var.type == VarType.OUT) {
                var.setMembershipsForValue(Approximate(getpridict(var)));

                s.append("The predicted ");
                s.append(var.name);
                s.append(" is ");
                s.append(var.GetHighestMember());
                s.append(" (");
                s.append(getpridict(var));
                s.append(")\n");
            }
        }
        return s.toString();
    }

    private double Approximate(double getpridict) {
        DecimalFormat f = new DecimalFormat("##.0");
        return Double.parseDouble(f.format(getpridict));
    }

}

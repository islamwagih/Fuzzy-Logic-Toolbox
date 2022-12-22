package FuzzySystem;

import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("Fuzzy Logic Toolbox\n" +
                "===================\n" +
                "1- Create a new fuzzy system\n" +
                "2- Quit");
        int answer = scanner.nextInt();
        if(answer == 1){
            CreateNewFuzzySystem();
        }
    }

    private static void CreateNewFuzzySystem() {
        System.out.println("Enter the system’s name and a brief description:\n" +
                "------------------------------------------------");
        String name = scanner.nextLine();
        String des = scanner.nextLine();
        FuzzySystem fuzzySystem = new FuzzySystem(name,des);

        fuzzySystem.MainMenu();
    }
}
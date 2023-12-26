package pl.coderslab;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TaskManager {
    public static void main(String[] args) {
        showMenu();
    }

    public static void showMenu(){
        int counter = 1;
        List<String> menuItems = Arrays.asList("List Tasks", "Add Task", "Remove Task", "Exit");
        System.out.println(ConsoleColors.BLUE_BOLD + "TASKINATOR MENU: ");
        System.out.println(ConsoleColors.RESET);
        for(String menuItem : menuItems){
            System.out.println(counter + ". " + menuItem);
            counter++;
        }
        System.out.println();
        menuAction(menuChoice());
    }

    public static int menuChoice(){
        Scanner sc = new Scanner(System.in);
                System.out.print("Please enter menu choice in format 1-4 or list/add/remove/exit >> ");
                String choice = sc.nextLine();
                if (choice.matches("[1-4]")) {
                    return Integer.parseInt(choice);
                }
                else {
                    return switch (choice.toLowerCase().trim()) {
                        case "list" -> 1;
                        case "add" -> 2;
                        case "remove" -> 3;
                        case "exit" -> 4;
                        default -> menuChoice();
                    };
                }
        }

    public static void menuAction(int menuChoice){
        switch (menuChoice){
            case 1 -> listTasks();
            case 2 -> addTask();
            case 3 -> removeTask();
            case 4 -> {
                System.out.println(ConsoleColors.BLUE_UNDERLINED + "Bye bye!");
                System.exit(0);
            }
            default -> System.out.println("Invalid value.");
        };
    }

    public static void listTasks(){
        Path path = Paths.get("tasks.csv");
        try {
            System.out.println();
            int counter = 1;
            System.out.printf(ConsoleColors.BLUE_BOLD + "%-10s%-42s%-22s%-22s\n","Task No.","Task description","Task Due Date", "Is task v. important");
            System.out.println(ConsoleColors.RESET);
            for(String line : Files.readAllLines(path)){
                String[] lineArray = line.split(",");
                System.out.printf("%-10s%-42s%-22s%-22s\n",counter, lineArray[0].trim(), lineArray[1].trim(), lineArray[2].trim());
                counter++;
            }
        } catch (IOException e){
            System.out.println("Error loading file: " + path);
        }
        System.out.println();
        showMenu();
    }

    public static void addTask(){
        Scanner sc = new Scanner(System.in);
        Path path = Paths.get("tasks.csv");
        String taskDueDate = "";
        String isTaskImportant = "";
        System.out.print("Please enter the description of your task >> ");
        String taskDescription = sc.nextLine();
        do {
            System.out.print("Please enter the task due date (format yyyy-mm-dd) >> ");
            String dateInput = sc.nextLine();
            String datePattern = "\\d{4}-\\d{2}-\\d{2}";
            if(dateInput.matches(datePattern)){
                taskDueDate = dateInput;
                break;
            }
        } while(true);
        do {
            System.out.print("Please specify if the taks is important (true/false) >> ");
            String important = sc.nextLine();
            if(important.equalsIgnoreCase("true") || important.equalsIgnoreCase("false")){
                isTaskImportant = important;
                break;
            }
        } while(true);
        String taskToAdd = taskDescription.trim() + "," + taskDueDate.trim() + "," + isTaskImportant.trim();
        try {
            Files.writeString(path, taskToAdd + System.lineSeparator(), StandardOpenOption.APPEND);
        } catch (IOException e){
            System.out.println("Error writing to the file: " + path);
        }
        System.out.println("Task: " + taskDescription + " " + taskDueDate + " " + isTaskImportant + " added.");
        showMenu();
    }

    public static void removeTask(){
        Scanner sc = new Scanner(System.in);
        Path path = Paths.get("tasks.csv");
        int numberOfTasks = 0;

        try {
            List<String> wholeFile = new ArrayList<>(Files.readAllLines(path));
            numberOfTasks = wholeFile.size();
            System.out.println(wholeFile.size());
            do {
                System.out.print(ConsoleColors.RED + "What task do you want to remove (Task No.) >> ");
                String taskToRemove = sc.nextLine();
                System.out.print(ConsoleColors.RESET);
                if (taskToRemove.matches("[0-9]+") && Integer.parseInt(taskToRemove) > 0 && Integer.parseInt(taskToRemove) <= numberOfTasks) {
                    int listPositionOfTaskToRemove = Integer.parseInt(taskToRemove);
                    wholeFile.remove(listPositionOfTaskToRemove - 1);
                    System.out.println("\t" + "Task " + taskToRemove + " removed.");
                    break;
                }
            } while (true);
            try {
                Files.write(path, wholeFile);
            } catch (IOException e){
                System.out.println("Error writing to file: " + path);
            }
        } catch (IOException e) {
            System.out.println("Error reading from file: " + path);
        }
        showMenu();
    }

}

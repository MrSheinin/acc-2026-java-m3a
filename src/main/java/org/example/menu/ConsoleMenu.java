package org.example.menu;

import org.example.model.*;
import org.example.shelter.Shelter;

import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {

    private final Shelter<Animal> shelter;

    private final Scanner scanner =  new Scanner(System.in);

    private static final List<MenuOption> OPTIONS = List.of(
            new MenuOption(1, "Add animal"),
            new MenuOption(2, "List all animals"),
            new MenuOption(3, "Find animals by species"),
            new MenuOption(4, "List available animals"),
            new MenuOption(5, "Mark animal as adopted"),
            new MenuOption(0, "Exit")
    );

    public ConsoleMenu(Shelter<Animal> shelter) {
        this.shelter = shelter;
    }

    public void start(){
        boolean running = true;
        while (running){
            printMenu();
            int choice = readInt("Choose an option: ");

            switch (choice) {
                case 1 -> addAnimal();
                case 2 -> printAllAnimals();
                case 3 -> findAnimalsBySpecies();
                case 4 -> printAvailableAnimals();
                case 5 -> markAnimalAsAdopted();
                case 0 -> {
                    running = false;
                    System.out.println("Exiting...");
                }
                default -> System.out.println("Unknown option. Try again.");
            }
        }
    }

    private void addAnimal(){
        System.out.println("Chose animal species: Bird | Cat | Dog");
        String species = scanner.nextLine().trim();

        System.out.println("Name: ");
        String name = scanner.nextLine().trim();
        if(name.isBlank()){
            System.out.println("Name must not be blank");
            return;
        }

        int age = readInt("Age: ");
        if (age < 0){
            System.out.println("Age must not be negative");
            return;
        }

        AnimalId id = new AnimalId();

        // null indicates an unsupported species; handled below with a user-friendly message
        Animal animal = switch (species.toLowerCase()) {
            case "bird" -> new Bird(id, name, age);
            case "dog" -> new Dog(id, name, age);
            case "cat" -> new Cat(id, name, age);
            default -> null;
        };

        if (animal == null){
            System.out.println("Unsupported species " + species);
            return;
        }

        shelter.addAnimal(animal);
        System.out.println("Animal successfully added " + animal);
    }

    private void printAllAnimals(){
        List<Animal> animals =  shelter.getAllAnimals();
        if (animals.isEmpty()) {
            System.out.println("No animals found");
            return;
        }
        animals.forEach(System.out::println);
    }

    private void findAnimalsBySpecies(){
        System.out.println("Chose animal species: Bird | Cat | Dog");
        String species = scanner.nextLine().trim();

        List<Animal> animals =  shelter.findBySpecies(species);
        if (animals.isEmpty()) {
            System.out.println("No animals found in species: " + species);
            return;
        }
        animals.forEach(System.out::println);
    }

    private void printAvailableAnimals(){
        List<Animal> animals = shelter.findAvailableAnimals();
        if (animals.isEmpty()){
            System.out.println("No animals are available");
            return;
        }
        animals.forEach(System.out::println);
    }

    private void markAnimalAsAdopted(){
        System.out.println("Enter animal ID: ");
        String id = scanner.nextLine().trim();

        try {
            shelter.markAsAdopted(id);
            System.out.println("Animal marked as adopted.");
        } catch (IllegalArgumentException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void printMenu(){
        for (MenuOption option : OPTIONS){
            System.out.println(option.number() + ". " + option.label());
        }
    }

    // Read a full line and parse as int to avoid leftover newline issues and non-numeric crashes
    private int readInt(String prompt){
        while (true) {
            System.out.println(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e){
                System.out.println("Please enter valid number.");
            }
        }
    }
}

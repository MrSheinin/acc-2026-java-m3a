package org.example.shelter;

import org.example.model.AdoptionStatus;
import org.example.model.Animal;

import java.util.ArrayList;
import java.util.List;

public class Shelter <T extends Animal>{
    private final List<T> animals = new ArrayList<>();

    public void addAnimal(T animal){
        animals.add(animal);
    }

    // Return an unmodifiable copy so callers cannot mutate the internal list directly
    public List<T> getAllAnimals(){
        return List.copyOf(animals);
    }

    public List<T> findBySpecies(String species){
        if (species == null || species.isBlank()){
            return List.of();
        }
        return animals.stream()
                .filter(animal -> animal.getSpecies().equalsIgnoreCase(species))
                .toList();
    }

    public List<T> findAvailableAnimals(){
        return animals.stream()
                .filter(animal -> animal.getAdoptionStatus().equals(AdoptionStatus.AVAILABLE))
                .toList();
    }

    // Throw IllegalArgumentException on invalid input or missing ID so callers can handle it explicitly
    public void markAsAdopted(String id){
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Id must not be null or blank");
        }
        T found = animals.stream()
                .filter(animal -> animal.getId() != null && animal.getId().toString().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Animal with id not found: " + id));
        found.markAsAdopted();
    }
}

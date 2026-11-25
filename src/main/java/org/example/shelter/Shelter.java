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

    public List<T> getAllAnimals(){
        return List.copyOf(animals);
    }

    public List<T> findBySpecies(String species){
        if (species == null || species.isBlank()){
            return List.of();
        }
        String target = species.trim();
        return animals.stream()
                .filter(animal -> animal.getSpecies().equalsIgnoreCase(target))
                .toList();
    }

    public List<T> findAvailableAnimals(){
        return animals.stream()
                .filter(animal -> animal.getAdoptionStatus().equals(AdoptionStatus.AVAILABLE))
                .toList();
    }

    public void markAsAdopted(String id){
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Id must not be null or blank");
        }
        String targetId = id.trim();
        Animal found = animals.stream()
                .filter(animal -> animal.getId() != null && animal.getId().toString().equals(targetId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Animal with id not found: " + targetId));
        found.markAsAdopted();
    }
}

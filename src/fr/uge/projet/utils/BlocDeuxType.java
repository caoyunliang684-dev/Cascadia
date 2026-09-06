package fr.uge.projet.utils;

import java.util.List;
import java.util.Objects;

import fr.uge.projet.animal.Animal;
import fr.uge.projet.tile.Tile;

public class BlocDeuxType {
  private final List<Tile> tile;
  
  
  private Animal animal;
  
  public BlocDeuxType(List<Tile> tile) {
    Objects.requireNonNull(tile);
    
    this.tile = tile;
    this.animal = null;
  }
  
  public Animal getAnimal() {
    return this.animal;
  }
  
  public boolean acceptOrNotThisAnimal(Animal animal) {
    Objects.requireNonNull(animal, "L'animal ne peut pas être nul.");

    return tile.stream()
               .anyMatch(t -> t.allowedAnimals().contains(animal));
}
  public void placeAnimal(Animal animal) {
    Objects.requireNonNull(animal);
    this.animal = animal;
  }
  public List<Tile> getTile() {
    return this.tile;
  }
  
  
}

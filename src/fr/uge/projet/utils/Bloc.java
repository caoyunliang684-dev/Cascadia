package fr.uge.projet.utils;

import java.util.Objects;

import fr.uge.projet.animal.Animal;
import fr.uge.projet.tile.Tile;

public class Bloc {
  private final Tile tile;
  
 
  private Animal animal;

  
  
  
  
  //On cree d'abord un bloc seulement avec tile
  //ensuite on ajoute un animal si besoin.
  
  public Bloc(Tile tile) {
    Objects.requireNonNull(tile);
    
    this.tile = tile;
    this.animal = null;
  }
  
  //ordre 2
  //verifier si la animal qui va placer sur le tile 
  //est acceptable ou pas.
  public Boolean acceptOrNotThisAnimal(Animal animal) {
    Objects.requireNonNull(animal);
    for(var allowedAnimals : tile.allowedAnimals()) {
      if(allowedAnimals.equals(animal)) {
        
        return true;
      }
    }
    return false;
  }
  /**
   * Places the specified animal on this tile.
   *
   * @param animal the animal to be placed on the tile; must not be {@code null}.
   * @throws NullPointerException if {@code animal} is {@code null}.
   */
  public void placeAnimal(Animal animal) {
    Objects.requireNonNull(animal);
    this.animal = animal;
  }
  /**
   * Retrieves the tile associated with this object.
   *
   * @return the {@link Tile} associated with this object.
   */
  public Tile getTile() {
    return this.tile;
  }
  /**
   * Retrieves the animal currently placed on the tile, if any.
   *
   * @return the {@link Animal} placed on the tile, or {@code null} if no animal is present.
   */
  public Animal getAnimal() {
    return this.animal;
  }
  /**
   * Displays a formatted string representing the state of the tile and its animal.
   * 
   * <p>The format includes:
   * <ul>
   *   <li>If an animal exists, its type, the tile type, and the allowed animals on the tile.</li>
   *   <li>If no animal exists, a blank space followed by the tile type and allowed animals.</li>
   * </ul>
   *
   * @return a {@link String} representation of the tile and its animal.
   */
  public String display() {
    if(animal != null) {
      return "["+animal.animalType()+tile.tileType()+ tile.allowedAnimalsInChar()+"]";
    }else {
      return "["+' '+""+tile.tileType()+tile.allowedAnimalsInChar()+"]";
    }
  }

  /**
   * Checks whether an animal is currently placed on the tile.
   *
   * @return {@code true} if an animal is present; {@code false} otherwise.
   */
  public Boolean isExistAnimal() {
    return animal != null;
  }
}

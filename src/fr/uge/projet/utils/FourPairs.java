package fr.uge.projet.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.uge.projet.animal.Animal;
import fr.uge.projet.tile.Tile;

public class FourPairs {
  private final List<Pair> pairs;
  /**
   * Constructs an empty list of pairs.
   */
  public FourPairs() {
    pairs = new ArrayList<>();
  }
  /**
   * Initializes the list with four pairs, each consisting of an {@link Animal} and a {@link Tile}.
   *
   * @param totalAnimal the source of animals; must not be {@code null}.
   * @param totalTile   the source of tiles; must not be {@code null}.
   * @throws NullPointerException if {@code totalAnimal} or {@code totalTile} is {@code null}.
   */
  public void init(TotalAnimal totalAnimal, TotalTile totalTile) {
    pairs.add(new Pair(totalAnimal.getAnimal(), totalTile.getTile()));
    pairs.add(new Pair(totalAnimal.getAnimal(), totalTile.getTile()));
    pairs.add(new Pair(totalAnimal.getAnimal(), totalTile.getTile()));
    pairs.add(new Pair(totalAnimal.getAnimal(), totalTile.getTile()));
  }
  /**
   * Retrieves and removes the pair at the specified index.
   *
   * @param index the 1-based index of the pair to retrieve.
   * @return the {@link Pair} at the specified index.
   * @throws IllegalArgumentException if the index is out of bounds.
   */
  public Pair getPair(int index) {
    if (index < 1 || index > pairs.size()) {
      throw new IllegalArgumentException("Index must be between 1 and " + pairs.size());
  }
  return pairs.remove(index - 1);
  }
  /**
   * Adds a new pair of an {@link Animal} and a {@link Tile}.
   *
   * @param animal the animal to add; must not be {@code null}.
   * @param tile   the tile to add; must not be {@code null}.
   * @throws NullPointerException if {@code animal} or {@code tile} is {@code null}.
   */
  public void addPair(Animal animal, Tile tile) {
    Objects.requireNonNull(animal, "animal must not be null");
    Objects.requireNonNull(tile, "tile must not be null");
    pairs.add(new Pair(animal, tile));
  }
  /**
   * Sets a pair at the specified index.
   *
   * @param pair  the pair to set; must not be {@code null}.
   * @param index the index where the pair should be set.
   * @throws NullPointerException if {@code pair} is {@code null}.
   */
  public void setIndexPair(Pair pair, int index) {
    Objects.requireNonNull(pair, "pair must not be null");
    pairs.set(index, pair);
  }
  /**
   * Displays all pairs in the list, showing their animal and tile types.
   */
  public void display() {
      for (int i = 0; i < pairs.size(); i++) {
        System.out.println("tile,animal Pair " + (i + 1) + ":" + pairs.get(i).tile().tileType()+pairs.get(i).animal().animalType());
          
        
      
  }
  }
  /**
   * Retrieves an array of the animal types in the pairs.
   *
   * @return a {@link String} array of animal types.
   */
  public String[] showAnimals() {
    String[] charStrings = new String[4];
    int count = 0;
    for(Pair pair:pairs) {
      charStrings[count] = String.valueOf(  pair.animal().animalType());
      count++;
    }
    return charStrings;
  }
  /**
   * Returns a copy of the list of all pairs.
   *
   * @return an immutable {@link List} of pairs.
   */
  public List<Pair> getFourPairs(){
    return List.copyOf(pairs);
  }
  /**
   * Resets the list of pairs with a new list of pairs.
   *
   * @param newPairs the new list of pairs; must not be {@code null}.
   * @throws NullPointerException if {@code newPairs} is {@code null}.
   */
  public void resetPairs(List<Pair> newPairs) {
    Objects.requireNonNull(newPairs, "newPairs must not be null");
    pairs.clear();
    pairs.addAll(newPairs);
}
  
  
}

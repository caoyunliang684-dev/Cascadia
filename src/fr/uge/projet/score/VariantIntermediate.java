package fr.uge.projet.score;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.uge.projet.animal.Animal;
import fr.uge.projet.tile.Tile;
import fr.uge.projet.utils.Bloc;
import fr.uge.projet.utils.Coordinate;





public class VariantIntermediate {

  private final Map<Coordinate, Bloc> map;

  public VariantIntermediate(Map<Coordinate, Bloc> map) {
    this.map = map;
  }
  
  /**
   * Find all connected tiles.
   *
   * @param cordonnate, adresse of the bloc
   * @param animal, the type of animal
   * @param reachedCordonnates, the blocs which have been reached, so dont need explore again
   * @param temperate, the blocs which is attached to the bloc orignal, and will be changed in the function
   */
  public void findAllConnected(Coordinate cordonnate,Animal animal,Set<Coordinate> reachedCordonnates, Set<Coordinate> temperate) {
    //S'il est deja dedans||s'il n'y a ce bloc
    if(reachedCordonnates.contains(cordonnate)||!map.containsKey(cordonnate) || animal == null) {
      return;
    }
    //S'il n'est pas la meme type
    if(!animal.equals(map.get(cordonnate).getAnimal())  ) {
      
      return;
    }
    reachedCordonnates.add(cordonnate);
    temperate.add(cordonnate);
    
    for(Coordinate fourCordonnate: cordonnate.fourSidesCordonnates()) {
      findAllConnected(fourCordonnate, animal, reachedCordonnates, temperate);
    }
    
  }
  /**
   * Calculates the score for a group of connected coordinates based on its size.
   *
   * <p>The scoring rules are:
   * <ul>
   *   <li>12 points for groups of size 4 or more.</li>
   *   <li>8 points for groups of size 3.</li>
   *   <li>5 points for groups of size 2.</li>
   *   <li>0 points for groups of size 1 or empty groups.</li>
   * </ul>
   *
   * @param temperate the set of connected {@link Coordinate} objects; must not be {@code null}.
   * @return the score based on the group's size.
   * @throws NullPointerException if {@code group} is {@code null}.
   */
  
  public int countThisGroup(Set<Coordinate> temperate) {
    //termperate name renommer
    if(temperate.size() >= 4) {
     return 12;
    }else if(temperate.size() == 3){
      return 8;
    }else if(temperate.size() == 2){
      return 5;
    }
    return 0;
  }
  /**
   * juste return the final score of animals
   *
   * 
   * @return the score of the animals 
   */
  
  public int scoreFinalAnimal() {
    Set <Coordinate> reachedCordonnates = new HashSet<>();
    //c'est hashset pas 
    int count = 0;
    Set<Coordinate> temperate = new HashSet<>();
    
    for(Coordinate cordonnate:map.keySet()) {
      if(!reachedCordonnates.contains(cordonnate)) {
        temperate.clear();
        findAllConnected(cordonnate, map.get(cordonnate).getAnimal(), reachedCordonnates, temperate);
        count += countThisGroup(temperate);
    
    
    
  }
  
  
      
    }
    return count;
  }
  /**
   * Finds the size of the largest connected group of tiles of the specified type.
   *
   * <p>This method iterates through all coordinates in the map and uses a depth-first search
   * to determine the size of the largest connected region of the given tile type.
   *
   * @param tile the {@link Tile} type to search for; must not be {@code null}.
   * @return the size of the largest connected group of tiles of the specified type.
   * @throws NullPointerException if {@code tile} is {@code null}.
   */
public int findNumberOfBiggerstConnectedTile(Tile tile){
    
    Set <Coordinate> reachedCordonnates = new HashSet<>();
    Set<Coordinate> biggerstNumberCordonnates = new HashSet<>();
    Set<Coordinate> temperate = new HashSet<>();
    
    for(Coordinate cordonnate:map.keySet()) {
      //Pour s'assurer qu'il s'agit du même type
      
      
      
      //utilise equals pour comparer???
      if( tile.equals(map.get(cordonnate).getTile()) ) {
        if(!reachedCordonnates.contains(cordonnate)) {
          temperate.clear();
          findAllConnectedTile(cordonnate,tile,reachedCordonnates,temperate);
        
          if (temperate.size() > biggerstNumberCordonnates.size()) {
            biggerstNumberCordonnates = new HashSet<>(temperate) ;
          }
        
        }
      }
    }
    return biggerstNumberCordonnates.size();
  }
  
/**
 * Recursively finds and collects all connected tiles of the same type starting from a given coordinate.
 *
 * <p>This method performs a depth-first search to find all tiles connected to the starting coordinate
 * that have the same type as the specified {@code tile}. It uses two sets:
 * <ul>
 *   <li>{@code reachedCordonnates} to keep track of already visited coordinates.</li>
 *   <li>{@code temperate} to store the coordinates of the current connected group.</li>
 * </ul>
 *
 * @param cordonnate          the starting {@link Coordinate} to explore; must not be {@code null}.
 * @param tile                the {@link Tile} type to search for; must not be {@code null}.
 * @param reachedCordonnates  the set of already visited coordinates; must not be {@code null}.
 * @param temperate           the set to store coordinates of the current connected group; must not be {@code null}.
 * @throws NullPointerException if any of the parameters is {@code null}.
 */
  public void findAllConnectedTile(Coordinate cordonnate,Tile tile,Set<Coordinate> reachedCordonnates, Set<Coordinate> temperate) {
    //S'il est deja dedans||s'il n'y a ce bloc
    if(reachedCordonnates.contains(cordonnate)||!map.containsKey(cordonnate)) {
      return;
    }
    //S'il n'est pas la meme type
    if(!tile.equals(map.get(cordonnate).getTile())) {
      return;
    }
    reachedCordonnates.add(cordonnate);
    temperate.add(cordonnate);
    
    for(Coordinate fourCordonnate: cordonnate.fourSidesCordonnates()) {
      findAllConnectedTile(fourCordonnate, tile, reachedCordonnates, temperate);
    }
    
  }
  
  /**
   * return the biggest score of each type of tile.
   *
   * 
   * @return a map ,Tile is the type, and integer contains the score
   */
  public Map<Tile, Integer> scoreOfEachTile(){
    var result = new HashMap<Tile,Integer>();
    result.put(Tile.FOREST, findNumberOfBiggerstConnectedTile(Tile.FOREST));
    result.put(Tile.MOUNTAIN, findNumberOfBiggerstConnectedTile(Tile.MOUNTAIN));
    result.put(Tile.PRAIRIE, findNumberOfBiggerstConnectedTile(Tile.PRAIRIE));
    result.put(Tile.RIVER, findNumberOfBiggerstConnectedTile(Tile.RIVER));
    result.put(Tile.SWAMP, findNumberOfBiggerstConnectedTile(Tile.SWAMP));
    
    return result;
  }
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
}

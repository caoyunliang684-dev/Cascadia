package fr.uge.projet.utils;


import java.awt.Graphics2D;
import java.awt.desktop.AboutHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import fr.uge.projet.animal.Animal;
import fr.uge.projet.score.VariantIntermediate;
import fr.uge.projet.tile.Tile;
import fr.uge.projet.zen.Circle;
import fr.uge.projet.zen.Hexagon;
import fr.uge.projet.zen.Shape;
import fr.uge.projet.zen.ShowType;
import fr.uge.projet.zen.Square;


/**
 * Represents a placement manager for handling a map of coordinates to blocks,
 * including operations for adding, retrieving, and verifying placements.
 */
public class PlayerInformation {
  private final Map<Coordinate, Bloc> map;


  public PlayerInformation() {
    map = new HashMap<>();

  }
  
  /**
   * Initialise a T(group of three randomly) for the Terminal version, put them in
   * the map
   *
   * @param totalTile which contains all the tile
   */
  public void initPlacement(TotalTile totalTile) {
    Objects.requireNonNull(totalTile, "totalTile must not be null");
    map.put(new Coordinate(1,1), new Bloc(totalTile.getTile()));
    map.put(new Coordinate(1,2), new Bloc(totalTile.getTile()));
    map.put(new Coordinate(2,1), new Bloc(totalTile.getTile()));
    }
 
  //verifier n'est pas ajouter un tile
  //dans un bloc existe deja.
  //il assure aussi on ajoute un animal 
  //sur un tile existe.
  /**
   * Checks if a coordinate exists in the map.
   *
   * @param cordonnate the coordinate to check; must not be {@code null}.
   * @return {@code true} if the coordinate exists, {@code false} otherwise.
   * @throws NullPointerException if {@code cordonnate} is {@code null}.
   */
  public Boolean isExistCordonnate(Coordinate cordonnate) {
    Objects.requireNonNull(cordonnate);
    return map.containsKey(cordonnate);
  }
  //verifer ce coordonne est attache au un des tiles existent deja
  /**
   * Checks if a coordinate is adjacent to any existing tile in the map.
   *
   * @param cordonnate the coordinate to check; must not be {@code null}.
   * @return {@code true} if the coordinate is adjacent, {@code false} otherwise.
   * @throws NullPointerException if {@code cordonnate} is {@code null}.
   */
  public Boolean isPlacedAttachedTilesExist(Coordinate cordonnate) {
    Objects.requireNonNull(cordonnate, "cordonnate must not be null");
    var set = new HashSet<Coordinate>();
    for(var cor : map.keySet()) {
      for(var corFour : cor.fourSidesCordonnates() ) {
        set.add(corFour);
      }
    }
    return set.contains(cordonnate);
    
    
    
  }
  /**
   * Retrieves a map of coordinates to tiles.
   *
   * @return a map of coordinates to their associated tiles.
   */
  public Map<Coordinate, Tile> getTileMap() {
    /* Map<Cordonnate, Tile> tileMap = new HashMap<>();
     for (Map.Entry<Cordonnate, Bloc> entry : map.entrySet()) {
         tileMap.put(entry.getKey(), entry.getValue().getTile());
     }
     return tileMap;*/
     
     return map.entrySet().stream()
                  .collect(Collectors.toMap(Map.Entry::getKey, entry->entry.getValue().getTile()));
 }
  
  
  
  //premier version pas 2 animal 
  /**
   * Checks if an animal exists at a specific coordinate.
   *
   * @param cordonnate the coordinate to check; must not be {@code null}.
   * @return {@code true} if an animal exists, {@code false} otherwise.
   * @throws NullPointerException if {@code cordonnate} is {@code null}.
   */
  public Boolean isExistAnimal(Coordinate cordonnate) {
    Objects.requireNonNull(cordonnate);
    return map.get(cordonnate).isExistAnimal();
    }
  /**
   * Checks if an animal can be placed at a specific coordinate.
   *
   * @param cordonnate the coordinate to check; must not be {@code null}.
   * @param animal     the animal to check; must not be {@code null}.
   * @return {@code true} if the animal can be placed, {@code false} otherwise.
   * @throws NullPointerException if {@code cordonnate} or {@code animal} is {@code null}.
   */
  public Boolean acceptOrNotThisAnimal(Coordinate cordonnate, Animal animal) {
    Objects.requireNonNull(cordonnate);
    Objects.requireNonNull(animal);
    return map.get(cordonnate).acceptOrNotThisAnimal(animal);
  }
  
  //add new tile 
  //ordre 1
  /**
   * Adds a new tile at the specified coordinate.
   *
   * @param cordonnate the coordinate to add the tile at; must not be {@code null}.
   * @param bloc       the block representing the tile; must not be {@code null}.
   * @throws NullPointerException if {@code cordonnate} or {@code bloc} is {@code null}.
   */
  public void addNewTile(Coordinate cordonnate,Bloc bloc) {
    Objects.requireNonNull(bloc);
    Objects.requireNonNull(cordonnate);
    map.put(cordonnate, bloc);
  }

  /**
   * Adds a new animal at the specified coordinate.
   *
   * @param cordonnate the coordinate to add the animal at; must not be {@code null}.
   * @param animal     the animal to add; must not be {@code null}.
   * @throws NullPointerException if {@code cordonnate} or {@code animal} is {@code null}.
   */
  public void addNewAnimal(Coordinate cordonnate,Animal animal) {
    
    Objects.requireNonNull(animal);
    Objects.requireNonNull(cordonnate);

    map.get(cordonnate).placeAnimal(animal);
  }
  /**
   * Locates the tile corresponding to the given coordinate and shape type.
   *
   * @param target    the target coordinate to locate; must not be {@code null}.
   * @param shapeType the shape type (e.g., SQUARE or HEXAGON); must not be {@code null}.
   * @return the {@link Coordinate} of the tile if found, or {@code null} if no matching tile exists.
   * @throws NullPointerException     if {@code target} or {@code shapeType} is {@code null}.
   * @throws IllegalArgumentException if {@code shapeType} is not supported.
   */
  public Coordinate locateTheTileZen(Coordinate target, ShowType shapeType) {
    Objects.requireNonNull(target, "target must not be null");
    Objects.requireNonNull(shapeType, "shapeType must not be null");
    for (var entry : map.entrySet()) {
        Coordinate cordonnate = entry.getKey();
        Bloc bloc = entry.getValue();

     // Create the appropriate shape based on the shape type
        Shape shape;
        if (shapeType == ShowType.HEXAGON) {
            shape = new Hexagon(cordonnate, bloc.getTile());
        } else if (shapeType == ShowType.SQUARE) {
            shape = new Square(cordonnate, bloc.getTile());
        } else {
            throw new IllegalArgumentException("Unsupported ShapeType: " + shapeType);
        }

     // Check if the shape contains the target coordinate
        if (shape.contains(target)) {
            return cordonnate;
        }
    }
    return null; 
}
  /**
   * Checks if a coordinate is adjacent to any tile using a specific graphical shape type.
   *
   * @param cordonnate the coordinate to check; must not be {@code null}.
   * @param shapeType  the shape type (e.g., SQUARE or HEXAGON); must not be {@code null}.
   * @return {@code true} if the coordinate is adjacent to an existing tile, {@code false} otherwise.
   * @throws NullPointerException     if {@code cordonnate} or {@code shapeType} is {@code null}.
   * @throws IllegalArgumentException if {@code shapeType} is not supported.
   */
  public Boolean isPlacedAttachedTilesExistGraphicShape(Coordinate cordonnate, ShowType shapeType) {
    Objects.requireNonNull(cordonnate, "cordonnate must not be null");
    Objects.requireNonNull(shapeType, "shapeType must not be null");
    var set = new HashSet<Coordinate>();


    // Add adjacent coordinates based on shape type
    for (var cor : map.keySet()) {
        if (shapeType == ShowType.SQUARE) {
            set.addAll(cor.fourSidesCordonnatesGraphicSquare());
        } else if (shapeType == ShowType.HEXAGON) {
            set.addAll(Hexagon.sixSidesCordonnates(cor)); 
        } else {
            throw new IllegalArgumentException("Unsupported ShapeType: " + shapeType);
        }
    }

 // Check if the coordinate is adjacent

    for (var i : set) {
        Shape shape;
        if (shapeType == ShowType.SQUARE) {
            shape = new Square(i);
        } else if (shapeType == ShowType.HEXAGON) {
            shape = new Hexagon(i);
        } else {
            throw new IllegalArgumentException("Unsupported ShapeType: " + shapeType);
        }

        if (shape.contains(cordonnate) && !isExistCordonnate(cordonnate)) {
            return true;
        }
    }

    return false;
}

  /**
   * Retrieves all coordinates adjacent to existing tiles, using a specific graphical shape type.
   *
   * @param shapeType the shape type (e.g., SQUARE or HEXAGON); must not be {@code null}.
   * @return a {@link Set} of coordinates adjacent to existing tiles.
   * @throws NullPointerException     if {@code shapeType} is {@code null}.
   * @throws IllegalArgumentException if {@code shapeType} is not supported.
   */
  public Set<Coordinate> AllAttachedTiles(ShowType shapeType) {
    var set = new HashSet<Coordinate>();

  
    for (var cor : map.keySet()) {
       
        if (shapeType == ShowType.SQUARE) {
            set.addAll(cor.fourSidesCordonnatesGraphicSquare());
        } else if (shapeType == ShowType.HEXAGON) {
            set.addAll(Hexagon.sixSidesCordonnates(cor)); 
        } else {
            throw new IllegalArgumentException("Unsupported ShapeType: " + shapeType);
        }
    }

    return set;
}

  /**
   * Corrects and retrieves the adjacent coordinate for a square-based shape.
   *
   * @param cordonnate the target coordinate to check; must not be {@code null}.
   * @return the corrected {@link Coordinate} if the input is adjacent to an existing square, or {@code null} otherwise.
   * @throws NullPointerException if {@code cordonnate} is {@code null}.
   */
  public Coordinate CorrectedPlacedAttachedTilesExistGraphicSquare(Coordinate cordonnate) {
    var set = new HashSet<Coordinate>();
    for(var cor : map.keySet()) {
      for(var corFour : cor.fourSidesCordonnatesGraphicSquare() ) {
        set.add(corFour);
      }
    }
    for(var i : set) {
      var p = new Square(i);
      if(p.contains(cordonnate)) {
        return p.cordonnate();
      }
    }
    return null;
    
    
    
  }
  /**
   * Corrects and retrieves the adjacent coordinate for a hexagon-based shape.
   *
   * @param cordonnate the target coordinate to check; must not be {@code null}.
   * @return the corrected {@link Coordinate} if the input is adjacent to an existing hexagon, or {@code null} otherwise.
   * @throws NullPointerException if {@code cordonnate} is {@code null}.
   */
  public Coordinate CorrectedPlacedAttachedTilesExistGraphicHexagon(Coordinate cordonnate) {
    var set = new HashSet<Coordinate>();
    for(var cor : map.keySet()) {
      for(var corFour : Hexagon.sixSidesCordonnates(cor) ) {
        set.add(corFour);
      }
    }
    for(var i : set) {
      var p = new Hexagon(i);
      if(p.contains(cordonnate)) {
        return p.cordonnate();
      }
    }
    return null;
    
    
    
  }
  
  
  
  /**
   * Calculates the total score for all animals in the placement.
   *
   * @return the total score for animals.
   */
  public int socreAnimal() {
    VariantIntermediate variantIntermediate = new VariantIntermediate(map);
    return variantIntermediate.scoreFinalAnimal();
  }
  //Pour comparer
  /**
  * Calculates the score for each tile in the placement.
  *
  * @return a map of tiles to their scores.
  */
  public Map<Tile, Integer> socreOfEachTuile() {
    VariantIntermediate variantIntermediate = new VariantIntermediate(map);
    return variantIntermediate.scoreOfEachTile();
  }
  /**
   * Calculates the total score across all tiles in the placement.
   *
   * @return the total score across all tiles.
   */
  public int socreTotalTuile() {
    VariantIntermediate variantIntermediate = new VariantIntermediate(map);
    int count = 0;
    for( var i: variantIntermediate.scoreOfEachTile().values()) {
      count += i;
    };
    return count;
  }
  /**
   * to display at the terminal a table size dynamique.
   *
   */
  //afficher les blocs sur terminal
  public void display() {
    int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
    
    for(Coordinate cordonnate : map.keySet()) {
      if (cordonnate.x() < minX) minX = cordonnate.x();
      if (cordonnate.x() > maxX) maxX = cordonnate.x();
      if (cordonnate.y() < minY) minY = cordonnate.y();
      if (cordonnate.y() > maxY) maxY = cordonnate.y();
    }
    
    for (int y = minY; y <= maxY; y++) {
      for (int x = minX; x <= maxX; x++) {
          Coordinate cordonnate = new Coordinate(x, y);
          Bloc bloc = map.getOrDefault(cordonnate,null);
          if (bloc != null) {
              System.out.print(bloc.display() + " ");
          } else {
              System.out.print("[    ] "); 
          }
      }
      System.out.println();  
  }
  }
  /**
   * Retrieves the map of all coordinates to blocks.
   *
   * @return the map of coordinates to blocks.
   */

  public Map<Coordinate, Bloc> getMap() {
    return map;
}
  
  //nouvelle
  // return map collection to animal not null in bloc
  // use for score Variant Family
  /**
   * Retrieves a map of all coordinates to animals that are not {@code null}.
   *
   * @return a map of coordinates to non-null animals.
   */
  public Map<Coordinate, Animal> getAnimalMap() {
  Map<Coordinate, Animal> newmap = map.entrySet().stream()
                           .filter(entry-> entry.getValue().getAnimal() != null)
                           .collect(Collectors.toMap(Map.Entry::getKey,entry->entry.getValue().getAnimal()));
      
    return newmap;
  }
  
    
}


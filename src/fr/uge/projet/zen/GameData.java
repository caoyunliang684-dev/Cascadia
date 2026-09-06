package fr.uge.projet.zen;

import fr.uge.projet.animal.Animal;
import fr.uge.projet.score.ScoreManager;
import fr.uge.projet.score.VariantType;
import fr.uge.projet.tile.Tile;
import fr.uge.projet.utils.Bloc;
import fr.uge.projet.utils.Coordinate;
import fr.uge.projet.utils.FourPairs;
import fr.uge.projet.utils.Pair;
import fr.uge.projet.utils.PlayerInformation;
import fr.uge.projet.utils.TotalAnimal;
import fr.uge.projet.utils.TotalTile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class GameData {
  private final List<PlayerInformation> listPlayers;
  private final TotalAnimal totalAnimal;
  private final TotalTile totalTile;
  private final FourPairs fourPairs;
  private static final int SQUARE_SIZE = 50;
  private static final int CIRCLE_RADIUS = 25;
  private static final int MARGIN = 10;
  private final List<Square> squares;
  private final List<Circle> circles;
  private final List<Coordinate> initialSquareCoordinates = new ArrayList<>();
  private final List<Coordinate> initialCircleCoordinates = new ArrayList<>();
  private final VariantType variantType;
  private final ShowType showType; //terminal , hexagone ,carré
  private final String configuration; // option score carte A-B-C-D

  
  public GameData(int nomberOfPlayers, int width, int height,ShowType type,VariantType variantType,String configuration) {
    
    this.showType = Objects.requireNonNull(type);
    this.variantType = Objects.requireNonNull(variantType);
    this.configuration = Objects.requireNonNull(configuration);
    listPlayers = new ArrayList<>();
    totalAnimal = new TotalAnimal();
    totalTile = new TotalTile();
    fourPairs = new FourPairs();

    for (int i = 0; i < nomberOfPlayers; i++) {
      PlayerInformation player = new PlayerInformation();

      if (type == ShowType.SQUARE) {
         
          player.addNewTile(new Coordinate(width / 2, height / 2), new Bloc(totalTile.getTile()));
          player.addNewTile(new Coordinate(width / 2 + 50, height / 2), new Bloc(totalTile.getTile()));
          player.addNewTile(new Coordinate(width / 2, height / 2 + 50), new Bloc(totalTile.getTile()));
      } else if (type == ShowType.HEXAGON) {
        int radius = 30;
        int dx = (int) (1.5 * radius);
        int dy = (int) (Math.sqrt(3) * radius / 2); 

     
        player.addNewTile(new Coordinate(width / 2, height / 2), new Bloc(totalTile.getTile()));          // 中心
        player.addNewTile(new Coordinate(width / 2 + dx, height / 2 - dy), new Bloc(totalTile.getTile())); // 右上
        player.addNewTile(new Coordinate(width / 2 + dx, height / 2 + dy), new Bloc(totalTile.getTile())); // 右下
      } else {
          throw new IllegalArgumentException("Unsupported ShapeType: " + type);
      }

      listPlayers.add(player);
  }

    
    for (int i = 0; i < 4; i++) {
      fourPairs.addPair(totalAnimal.getAnimal(), totalTile.getTile());
  }
    
    
    squares = new ArrayList<>();
    circles = new ArrayList<>();

    int startX = width - SQUARE_SIZE - CIRCLE_RADIUS * 2 - MARGIN * 2; 
    int startY = height / 2 - (4 * Math.max(SQUARE_SIZE, CIRCLE_RADIUS * 2) + 3 * MARGIN) / 2;

    for (int i = 0; i < 4; i++) {
        int y = startY + i * (Math.max(SQUARE_SIZE, CIRCLE_RADIUS * 2) + MARGIN);

        Coordinate cordonnateS = new Coordinate(startX, y);
        Coordinate cordonnateC = new Coordinate(startX + SQUARE_SIZE + MARGIN, y);

        initialSquareCoordinates.add(cordonnateS); 
        initialCircleCoordinates.add(cordonnateC); 

        squares.add(new Square(cordonnateS, fourPairs.getFourPairs().get(i).tile()));
        circles.add(new Circle(cordonnateC, fourPairs.getFourPairs().get(i).animal()));
  }
    
  }
  /**
   * Refreshes the current set of four pairs (animal and tile) and updates the associated
   * graphical representation (squares and circles).
   *
   * <p>This method performs the following steps:
   * <ul>
   *   <li>Returns the current animals and tiles in the four pairs back to their respective pools.</li>
   *   <li>Generates a new set of four pairs from the pools.</li>
   *   <li>Updates the graphical representation of the pairs using the initial coordinates.</li>
   * </ul>
   */
  public void refreshFourPairs() {
  
    List<Pair> currentPairs = new ArrayList<>(fourPairs.getFourPairs()); // 复制当前对

  
    for (Pair pair : currentPairs) {
        totalAnimal.putBackAnimal(pair.animal());
        totalTile.putBackTile(pair.tile());
    }


    List<Pair> newPairs = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
        newPairs.add(new Pair(totalAnimal.getAnimal(), totalTile.getTile()));
    }


    fourPairs.resetPairs(newPairs);


    squares.clear();
    circles.clear();

    for (int i = 0; i < 4; i++) {
        squares.add(new Square(initialSquareCoordinates.get(i), newPairs.get(i).tile()));
        circles.add(new Circle(initialCircleCoordinates.get(i), newPairs.get(i).animal()));
    }
}



  /**
   * Checks if a coordinate is adjacent to any existing tile for the specified player and shape type.
   *
   * @param cor   the coordinate to check; must not be {@code null}.
   * @param i     the player index.
   * @param type  the shape type (e.g., SQUARE, HEXAGON); must not be {@code null}.
   * @return {@code true} if the coordinate is adjacent to an existing tile, {@code false} otherwise.
   * @throws NullPointerException if {@code cor} or {@code type} is {@code null}.
   */
  public boolean isPlacedAttachedTilesExistGraphicShape(Coordinate cor, int i,ShowType type) {
    return listPlayers.get(i).isPlacedAttachedTilesExistGraphicShape(cor,type);
  }
  /**
   * Retrieves all coordinates adjacent to existing tiles for the specified player and shape type.
   *
   * @param i    the player index.
   * @param type the shape type (e.g., SQUARE, HEXAGON); must not be {@code null}.
   * @return a {@link Set} of coordinates adjacent to the player's existing tiles.
   * @throws NullPointerException if {@code type} is {@code null}.
   */
  public Set<Coordinate> AllAttachedTiles( int i,ShowType type){
    return listPlayers.get(i).AllAttachedTiles(type);
  }
  /**
   * Checks if an animal exists at a specific coordinate for the specified player.
   *
   * @param cordonnate the coordinate to check; must not be {@code null}.
   * @param i          the player index.
   * @return {@code true} if an animal exists at the specified coordinate, {@code false} otherwise.
   * @throws NullPointerException if {@code cordonnate} is {@code null}.
   */
   public Boolean isExistAnimal(Coordinate cordonnate, int i) {
     return listPlayers.get(i).isExistAnimal(cordonnate);
   }
   /**
    * Determines if a specific animal can be placed at a coordinate for the specified player.
    *
    * @param cor    the coordinate to check; must not be {@code null}.
    * @param animal the animal to check; must not be {@code null}.
    * @param i      the player index.
    * @return {@code true} if the animal can be placed, {@code false} otherwise.
    * @throws NullPointerException if {@code cor} or {@code animal} is {@code null}.
    */
   public Boolean acceptOrNotThisAnimal(Coordinate cor, Animal animal, int i) {
     return listPlayers.get(i).acceptOrNotThisAnimal(cor, animal);
   }
   /**
    * Adds a new tile to the specified coordinate for the specified player, based on the shape type.
    *
    * @param cor   the coordinate where the tile should be placed; must not be {@code null}.
    * @param tile  the tile to place; must not be {@code null}.
    * @param i     the player index.
    * @param type  the shape type (e.g., SQUARE, HEXAGON); must not be {@code null}.
    * @throws NullPointerException if {@code cor}, {@code tile}, or {@code type} is {@code null}.
    */
  public void addNewTileShape(Coordinate cor, Tile tile,int i, ShowType type) {
    if (type == ShowType.HEXAGON) {
      listPlayers.get(i).addNewTile(listPlayers.get(i).CorrectedPlacedAttachedTilesExistGraphicHexagon(cor), new Bloc(tile));
    }else {
    listPlayers.get(i).addNewTile(listPlayers.get(i).CorrectedPlacedAttachedTilesExistGraphicSquare(cor), new Bloc(tile));
      }
  
    }
  /**
   * Retrieves an immutable list of square objects for graphical representation.
   *
   * @return an immutable {@link List} of {@link Square} objects.
   */
  public List<Square> getListSquares(){
    return List.copyOf(squares);//need to be protected
  }
  /**
   * Retrieves an immutable list of circle objects for graphical representation.
   *
   * @return an immutable {@link List} of {@link Circle} objects.
   */
  public List<Circle> getListCircle(){
    return List.copyOf(circles);
  }
  /**
   * Locates the tile corresponding to the given coordinate for the specified player and shape type.
   *
   * @param cor   the coordinate to locate; must not be {@code null}.
   * @param i     the player index.
   * @param type  the shape type (e.g., SQUARE, HEXAGON); must not be {@code null}.
   * @return the {@link Coordinate} of the located tile, or {@code null} if no tile matches.
   * @throws NullPointerException if {@code cor} or {@code type} is {@code null}.
   */
  public Coordinate locateTheTileZen(Coordinate cor, int i,ShowType type) {
    return listPlayers.get(i).locateTheTileZen(cor,type);
  }
  /**
   * Retrieves the current placement map of the specified player.
   *
   * @param index the player index.
   * @return a map of {@link Coordinate} to {@link Bloc} for the player's placements.
   */

  public Map<Coordinate,Bloc> displayZen(int index) {
    return listPlayers.get(index).getMap();
}
  /**
   * Retrieves the initial coordinates of the square objects used for graphical representation.
   *
   * @return an immutable {@link List} of {@link Coordinate} objects representing the initial square coordinates.
   */
  public List<Coordinate> getInitialSquareCoordinates() {
    return List.copyOf(initialSquareCoordinates);
}

  /**
   * Retrieves the initial coordinates of the circle objects used for graphical representation.
   *
   * @return an immutable {@link List} of {@link Coordinate} objects representing the initial circle coordinates.
   */
public List<Coordinate> getInitialCircleCoordinates() {
    return List.copyOf(initialCircleCoordinates);
}
/**
 * Adds a new animal to the specified coordinate for the specified player and shape type.
 *
 * @param cor   the coordinate where the animal should be placed; must not be {@code null}.
 * @param animal the animal to place; must not be {@code null}.
 * @param i     the player index.
 * @param type  the shape type (e.g., SQUARE, HEXAGON); must not be {@code null}.
 * @throws NullPointerException if {@code cor}, {@code animal}, or {@code type} is {@code null}.
 */

  public void addNewAnimal(Coordinate cor,Animal animal,int i,ShowType type) {
    listPlayers.get(i).addNewAnimal( listPlayers.get(i).locateTheTileZen(cor, type), animal);
  }
  /**
   * Retrieves the next available animal from the pool.
   *
   * @return the next {@link Animal} from the pool, or {@code null} if no animals are available.
   */
  public Animal getNextAnimal() {
    return totalAnimal.getAnimal(); 
}
  /**
   * Retrieves the next available tile from the pool.
   *
   * @return the next {@link Tile} from the pool, or {@code null} if no tiles are available.
   */
public Tile getNextTile() {
    return totalTile.getTile(); 
}
/**
 * Replaces an existing pair of tile and animal at the specified index with a new pair.
 *
 * <p>This method performs the following steps:
 * <ul>
 *   <li>Updates the {@link FourPairs} object with the new pair.</li>
 *   <li>Updates the graphical representation (squares and circles) at the specified index.</li>
 * </ul>
 *
 * @param index   the index of the pair to replace.
 * @param newPair the new {@link Pair} to replace the existing pair; must not be {@code null}.
 * @throws NullPointerException if {@code newPair} is {@code null}.
 * @throws IndexOutOfBoundsException if the index is out of bounds.
 */
  public void replacePair(int index, Pair newPair) {
   
    fourPairs.setIndexPair(newPair, index);
   
    Coordinate squareCoordinate = initialSquareCoordinates.get(index);
    Coordinate circleCoordinate = initialCircleCoordinates.get(index);

    squares.set(index, new Square(squareCoordinate, newPair.tile()));
    circles.set(index, new Circle(circleCoordinate, newPair.animal()));
}
  /**
   * Calculates and prints the total score for all players based on the current game state.
   *
   * <p>This method uses the {@link ScoreManager} to compute scores for each player
   * based on the configured variant type and game layout.
   *
   * @param nomberPlayers the total number of players in the game.
   */
  public void countScore(int nomberPlayers) {
    var scoreManager = new ScoreManager(showType, configuration);
    for(var i = 0; i<nomberPlayers;i++) {
      var result = scoreManager.calculateTotalScore(variantType,listPlayers.get(i));
      System.out.println("player "+ i+"score:"+ result);
    }

  }
  
  
}

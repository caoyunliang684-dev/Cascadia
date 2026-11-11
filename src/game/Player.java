package game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a player in the game, maintaining their tiles, positions, and turn information.
 */
public class Player {
  private final String name;
  private final ArrayList<Tile> tiles;
  private final Map<Tile, Point> Position;
  private final Map<Tile, Animal> Animalset;
  private int turn;

  /**
   * Constructs a new Player with the given name.
   * 
   * @param name The name of the player.
   */
  public Player(String name) {
    Objects.requireNonNull(name);
    this.name = name;
    this.tiles = new ArrayList<>();
    this.Position = new HashMap<>();
    this.Animalset = new HashMap<>();
    this.turn = 1;
  }

  /**
   * Determines whether a tile can be placed at the specified coordinates.
   * 
   * @param x The x-coordinate of the position.
   * @param y The y-coordinate of the position.
   * @return true if the tile can be placed, false otherwise.
   */
  public boolean canPlaceTile(int x, int y) {
    if (Position.isEmpty()) {
      return true; // Any position is valid if no tiles are placed yet.
    }

    // Check if the position is already occupied.
    for (Point point : Position.values()) {
      if (point != null && point.x == x && point.y == y) {
        return false;
      }
    }

    // Check if the position is adjacent to an existing tile.
    for (Point point : Position.values()) {
      int dx = Math.abs(point.x - x);
      int dy = Math.abs(point.y - y);
      if ((dx == 1 && dy == 0) || (dx == 0 && dy == 1)) {
        return true; 
      }
    }

    return false;
  }

  /**
   * Assigns an animal to a tile located at the specified coordinates.
   * 
   * @param animal The animal to place.
   * @param x The x-coordinate of the position.
   * @param y The y-coordinate of the position.
   */
  public void setAnimalonTile(Animal animal, int x, int y) {
    Objects.requireNonNull(animal);
    for (Tile tile : tiles) {
      Point pos = Position.get(tile);
      if (pos != null && pos.x == x && pos.y == y) { 
        Animalset.put(tile, animal);
      }
    }
  }

  /**
   * Adds an animal to the specified tile.
   * 
   * @param tile The tile to which the animal is added.
   * @param animal The animal to add to the tile.
   */
  public void addAnimalToTile(Tile tile, Animal animal) {
    Objects.requireNonNull(tile);
    Objects.requireNonNull(animal);
    Animalset.put(tile, animal);
  }

  /**
   * Retrieves the animal placed on a specific tile.
   * 
   * @param tile The tile for which the animal is requested.
   * @return The animal on the tile, or null if none exists.
   */
  public Animal AnimalonTile(Tile tile) {
    Objects.requireNonNull(tile);
    return Animalset.get(tile);
  }

  /**
   * Sets the position of a tile in the player's area.
   * 
   * @param tile The tile to position.
   * @param x The x-coordinate of the position.
   * @param y The y-coordinate of the position.
   */
  public void setPosition(Tile tile, int x, int y) {
    Objects.requireNonNull(tile);
    Position.put(tile, new Point(x, y));
    Animalset.put(tile, null);
  }

  /**
   * Retrieves the mapping of tiles to their corresponding animals.
   * 
   * @return An unmodifiable map of tiles to animals.
   */
  public Map<Tile, Animal> getAnimalset() {
    return Collections.unmodifiableMap(Animalset);
  }

  /**
   * Retrieves the mapping of tiles to their positions in the player's area.
   * 
   * @return An unmodifiable map of tiles to positions.
   */
  public Map<Tile, Point> getPosition() {
    return Collections.unmodifiableMap(Position);
  }

  /**
   * Returns the name of the player.
   * 
   * @return The player's name.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the current turn count of the player.
   * 
   * @return The player's current turn.
   */
  public int getturn() {
    return turn;
  }

  /**
   * Increases the player's turn count by one.
   */
  public void addturn() {
    turn++;
  }

  /**
   * Resets the player's turn count to one.
   */
  public void initialturn() {
    turn = 1;
  }

  /**
   * Adds a tile to the player's collection of tiles.
   * 
   * @param tile The tile to be added.
   */
  public void addTile(Tile tile) {
    Objects.requireNonNull(tile);
    tiles.add(tile);
  }

  /**
   * Returns an unmodifiable list of tiles owned by the player.
   * 
   * @return The list of tiles.
   */
  public List<Tile> getTiles() {
    return Collections.unmodifiableList(tiles);
  }

  /**
   * Places a tile at the specified position and adds it to the player's area.
   * 
   * @param tile The tile to be placed.
   * @param x The x-coordinate where the tile will be placed.
   * @param y The y-coordinate where the tile will be placed.
   */
  public void placeTile(Tile tile, int x, int y) {
    Objects.requireNonNull(tile);
    setPosition(tile, x, y);
    tiles.add(tile);
  }
}

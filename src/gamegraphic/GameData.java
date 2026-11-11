package gamegraphic;

import game.Animal;
import game.Player;
import game.Tile;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import score.ScoreCalculate;

/**
 * Represents the game data, including tile pool, animal pool, players, and the current game state.
 */
public class GameData {
  private final ArrayList<Tile> tilePool;      // Tile pool
  private final ArrayList<Tile> choosetile;    // Currently available tiles to choose
  private final ArrayList<Animal> animalPool;  // Animal pool
  private final ArrayList<Animal> chooseanimal;
  private final ArrayList<Player> playerlist;
  private final HashMap <Tile,Animal> choosepartner;
  private final HashMap <String,ScoreCalculate> animalScoringMap;
  private Player currentplayer;
  private String currentStep;
  private String currentStep2;
  private int playernumber;
  private int error;
  private int isInitial;
  private int isSetAnimal;
  private int specialid;
  private int gameturn;
  private int turnover;
  private int gamemode;
  private Tile selectedtile;
  private Animal selectedanimal;
  /**
   * Constructs a new GameData object and initializes tile pool, animal pool, and players.
   */
  public GameData() {
    tilePool = new ArrayList<>();
    choosetile = new ArrayList<>();
    animalPool = new ArrayList<>();
    chooseanimal = new ArrayList<>();
    playerlist = new ArrayList<>();
    choosepartner = new HashMap<>();
    animalScoringMap = new HashMap<>();
    currentStep = null;
    currentStep2 = null;
    selectedtile = null;
    selectedanimal = null;
    currentplayer = null;
    error = 0;
    isInitial = 0;
    isSetAnimal = 0;
    specialid = 100;
    gameturn = 0;
    turnover = 0;
    gamemode = 0;
    playernumber = 0;
  }
  
  /**
   * Sets the scoring rules for different animal types.
   *
   * @param map A map where the key is the animal type and the value is the scoring calculation logic.
   */
  public void setScoreMap(Map<String,ScoreCalculate> map) {
    Objects.requireNonNull(map);
    animalScoringMap.putAll(map);
  }
  
  /**
   * Retrieves the scoring rules for different animal types.
   *
   * @return An unmodifiable map of scoring rules for animal types.
   */
  public Map<String,ScoreCalculate> getScoreMap() {
    return Collections.unmodifiableMap(animalScoringMap);
  }
  
  /**
   * Sets the current game mode.
   *
   * @param i The game mode to set.
   */
  public void setGamemode(int i) {
    gamemode = i;
  }
  
  /**
   * Retrieves the current game mode.
   *
   * @return The current game mode.
   */
  public int getGamemode() {
    return gamemode;
  }
  
  /**
   * Creates a hexagonal polygon for tile rendering based on its center coordinates and radius.
   *
   * @param cx The x-coordinate of the center.
   * @param cy The y-coordinate of the center.
   * @param R  The radius of the hexagon.
   * @return A Polygon object representing the hexagon.
   */
  public Polygon createHextop(int cx, int cy, int R) {
    int[] xPoints = new int[6];
    int[] yPoints = new int[6];
    for (int k = 0; k < 6; k++) {
      double angle = -Math.PI/2 + k * (Math.PI / 3); 
      xPoints[k] = cx + (int)(R * Math.cos(angle));
      yPoints[k] = cy + (int)(R * Math.sin(angle));
    }
    return new Polygon(xPoints, yPoints, 6);
}
  
  /**
   * Retrieves the current player's mapping of tiles to animals.
   *
   * @return An unmodifiable map of tiles to animals for the current player.
   */
  public Map<Tile,Animal> getAnimalSet(){
    return currentplayer.getAnimalset();
  }
  
  /**
   * Retrieves the list of all players in the game.
   *
   * @return An unmodifiable list of all players.
   */
  public List<Player> getAllPlayers(){
    return Collections.unmodifiableList(playerlist);
  }
  
  /**
   * Sets the number of players in the game.
   *
   * @param i The number of players to set.
   */
  public void setplayernumber(int i) {
    playernumber = i;
  }
  
  /**
   * Retrieves the number of players in the game.
   *
   * @return The number of players.
   */
  public int getplayernumber() {
    return playernumber;
  }
  
  /**
   * Creates player objects based on the specified number of players.
   */
  public void createplayer() {
    for (int i = 1 ; i <= playernumber; i++) {
      playerlist.add(new Player("Player" + i));
    }
    currentplayer = playerlist.get(0);
  }
  
  /**
   * Removes a chosen animal from the list of available animals.
   *
   * @param animal The animal to remove.
   */
  public void removeChooseAnimal(Animal animal) {
    Objects.requireNonNull(animal);
    chooseanimal.remove(animal);
  }
  
  /**
   * Sets the mapping of tiles to animals for the current selection phase.
   */
  public void setpartner() {
    for (int i = 0; i < choosetile.size(); i++) {
      choosepartner.put(choosetile.get(i), chooseanimal.get(i));
    }
  }
  
  /**
   * Retrieves the paired animal for a specific tile during the selection phase.
   *
   * @param tile The tile for which the paired animal is requested.
   * @return The paired animal, or null if none exists.
   */
  public Animal getpartner(Tile tile) {
    Objects.requireNonNull(tile);
    return choosepartner.get(tile);
  }
  
  /**
   * Clears the mapping of tiles to animals for the current selection phase.
   */
  public void cleanpartner(){
    choosepartner.clear();
  }
    
  /**
   * Adds back the chosen tiles to the tile pool.
   */
  public void readdtilepool() {
    for(Tile tile : choosetile) {
      tilePool.add(tile);
    }
  }
  
  /**
   * Adds back the chosen animals to the animal pool.
   */
  public void readdanimalpool() {
    for(Animal animal : chooseanimal) {
      animalPool.add(animal);
    }
  }
  
  /**
   * Sets the turnover flag to indicate that the current turn is complete.
   */
  public void setturnover() {
    turnover = 1;
  }
  
  /**
   * Clears the turnover flag, resetting it to indicate a new turn.
   */
  public void cleanturnover() {
    turnover = 0;
  }
  
  /**
   * Retrieves the turnover flag.
   *
   * @return The turnover flag (1 if the turn is complete, 0 otherwise).
   */
  public int getturnover() {
    return turnover;
  }
  
  
  /**
   * Increments the game turn counter by one.
   */
  public void addGameTurn() {
    gameturn++;
  }
  
  /**
   * Retrieves the total number of turns in the game.
   *
   * @return The total number of game turns.
   */
  public int getGameTurn() {
    return gameturn;
  }
  
  /**
   * Checks if all players have completed their turns for the current round.
   *
   * @return true if all players have completed their turns, false otherwise.
   */
  public boolean isTurnFinish() {
    int firstPlayerTurn = playerlist.get(0).getturn(); 
    if (firstPlayerTurn == 0) {
        return false; 
    }
    for (Player player : playerlist) {
        if (player.getturn() != firstPlayerTurn) {
            return false;
        }
    }
    return true; 
}
  
  /**
   * Places an animal on a specific tile located at the specified coordinates.
   *
   * @param animal The animal to place.
   * @param x The x-coordinate of the tile.
   * @param y The y-coordinate of the tile.
   */
  public void setAnimalonTile(Animal animal,int x,int y) {
    Objects.requireNonNull(animal);
    currentplayer.setAnimalonTile(animal,x,y);
  }
  
  /**
   * Retrieves and increments the special ID for newly created tiles.
   *
   * @return The next unique special ID.
   */
  public int getspecialid() {
    return specialid++;
  }
  
  /**
   * Retrieves the animal placed on a specific tile.
   *
   * @param tile The tile to check.
   * @return The animal placed on the tile, or null if none exists.
   */
  public Animal AnimalonTile(Tile tile) {
    Objects.requireNonNull(tile);
    return currentplayer.AnimalonTile(tile);
  }
  
  /**
   * Checks if a tile has already been placed on the board.
   *
   * @param tile The tile to check.
   * @return true if the tile has been placed, false otherwise.
   */
  public boolean isTileplaced(Tile tile) {
    Objects.requireNonNull(tile);
    Animal animal = currentplayer.getAnimalset().get(tile);
    if (animal == null) {
      return false;
    }
    else {
      return true;
    }
  }
  
  /**
   * Retrieves the mapping of tiles to their positions on the board for the current player.
   *
   * @return An unmodifiable map of tiles to positions.
   */
  public Map<Tile,Point> getPosition() {
    return currentplayer.getPosition();
  }
  
  /**
   * Assigns a specific position to a tile on the board for the current player.
   *
   * @param tile The tile to place.
   * @param x The x-coordinate where the tile will be placed.
   * @param y The y-coordinate where the tile will be placed.
   */
  public void setPosition(Tile tile,int x, int y) {
    Objects.requireNonNull(tile);
    currentplayer.setPosition(tile, x, y);
  }
  
  /**
   * Retrieves the currently selected tile.
   *
   * @return The currently selected tile, or null if none is selected.
   */
  public Tile getselectedtile() {
    return selectedtile;
  }
  
  /**
   * Retrieves the currently selected animal marker.
   *
   * @return The currently selected animal marker, or null if none is selected.
   */
  public Animal getselectedanimal() {
    return selectedanimal;
  }
  
  /**
   * Clears the currently selected tile, setting it to null.
   */
  public void setselectedtilenull() {
    selectedtile = null;
  }
  
  /**
   * Clears the currently selected animal marker, setting it to null.
   */
  public void setselectedanimalnull() {
    selectedanimal = null;
  }
  
  /**
   * Sets the currently selected tile.
   *
   * @param tile The tile to set as selected.
   */
  public void setselectedtile(Tile tile) {
    Objects.requireNonNull(tile);
    selectedtile = tile;
  }
  
  /**
   * Sets the currently selected animal marker.
   *
   * @param animal The animal marker to set as selected.
   */
  public void setselectedanimal(Animal animal) {
    Objects.requireNonNull(animal);
    selectedanimal = animal;
  }
  
  /**
   * Marks that an animal has been set in the current step.
   */
  public void setAnimal() {
    isSetAnimal = 1;
  }
  

  /**
   * Clears the marker indicating that an animal has been set in the current step.
   */
  public void cleanAnimal() {
    isSetAnimal = 0;
  }
  
  /**
   * Checks whether an animal marker has been set in the current step.
   *
   * @return 1 if an animal marker has been set, 0 otherwise.
   */
  public int getisSetAnimal() {
    return isSetAnimal;
  }
  
  /**
   * Increments the counter for initialized tiles.
   */
  public void addisInitial() {
    isInitial++;
  }
  
  /**
   * Retrieves the number of tiles that have been initialized.
   *
   * @return The count of initialized tiles.
   */
  public int getisIntiail() {
    return isInitial;
  }
  
  /**
   * Sets the error flag for the current step.
   */
  public void seterror() {
    error = 1; 
  }
  
  /**
   * Clears the error flag for the current step.
   */
  public void cleanerror() {
    error = 0;
  }
  
  /**
   * Retrieves the error flag for the current step.
   *
   * @return The error flag (1 if an error occurred, 0 otherwise).
   */
  public int geterror() {
    return error;
  }
  
  /**
   * Adds a tile to the tile pool.
   * 
   * @param tile The tile to add to the pool.
   */
  public void addTileToPool(Tile tile) {
    Objects.requireNonNull(tile);
    tilePool.add(tile);
  }

  /**
   * Returns an unmodifiable list of tiles in the tile pool.
   * 
   * @return The list of tiles in the tile pool.
   */
  public List<Tile> getTilePool() {
    return Collections.unmodifiableList(tilePool);
  }

  /**
   * Shuffles the tiles in the tile pool.
   */
  public void ChuffleTilePool() {
    Collections.shuffle(tilePool);
  }
  
  /**
   * Shuffles the animals in the animal pool.
   */
  
  public void ChuffleAnimalPool() {
    Collections.shuffle(animalPool);
  }
  
  /**
   * Removes a specific tile from the tile pool.
   * 
   * @param tile The tile to remove from the pool.
   */
  public void removeTilePool(Tile tile) {
    Objects.requireNonNull(tile);
    tilePool.remove(tile);
  }
  
  /**
   * Removes a specific animal from the animal pool.
   * 
   * @param animal The animal to remove from the pool.
   */
  public void removeAnimalPool(Animal animal) {
    Objects.requireNonNull(animal);
    animalPool.remove(animal);
  }
  
  /**
   * Adds a tile to the choose pool (currently available tiles).
   * 
   * @param tile The tile to add to the choose pool.
   */
  public void addTileToChooseTile(Tile tile) {
    Objects.requireNonNull(tile);
    choosetile.add(tile);
  }
  
  /**
   * Adds an animal to the choose pool (currently available animals).
   * 
   * @param animal The animal to add to the choose pool.
   */
  public void addAnimalToChooseAnimal(Animal animal) {
    Objects.requireNonNull(animal);
    chooseanimal.add(animal);
  }
  
  /**
   * Returns an unmodifiable list of tiles currently available to choose.
   * 
   * @return The list of currently available tiles.
   */
  public List<Tile> getChooseTile() {
    return Collections.unmodifiableList(choosetile);
  }
  
  /**
   * Returns an unmodifiable list of animals currently available to choose.
   * 
   * @return The list of currently available animals.
   */
  public List<Animal> getChooseAnimal() {
    return Collections.unmodifiableList(chooseanimal);
  }

  /**
   * Clears the list of tiles available for choosing.
   */
  public void clearChooseTile() {
    choosetile.clear();
  }
  
  /**
   * Clears the list of animals available for choosing.
   */
  public void clearChooseAnimal() {
    chooseanimal.clear();
  }
  /**
   * Removes a specific tile from the choose pool.
   * 
   * @param tile The tile to remove from the choose pool.
   */
  public void removeChooseTile(Tile tile) {
    Objects.requireNonNull(tile);
    choosetile.remove(tile);
  }

  /**
   * Adds an animal to the animal pool.
   * 
   * @param animal The animal to add to the pool.
   */
  public void addAnimaltoPool(Animal animal) {
    Objects.requireNonNull(animal);
    animalPool.add(animal);
  }

  /**
   * Returns an unmodifiable list of animals in the animal pool.
   * 
   * @return The list of animals in the animal pool.
   */
  public List<Animal> getAnimalPool() {
    return Collections.unmodifiableList(animalPool);
  }

  /**
   * Initializes the turn count for the current player.
   * 
   * @param playername The name of the player whose turn count is to be initialized.
   */
  public void initialCurrentPlayerTurn(String playername) {
    Objects.requireNonNull(playername);
    for (Player player : playerlist) {
        if (player.getName().equals(playername)) {
            player.initialturn(); 
            return; 
        }
    }
}

  /**
   * Returns the name of the current player.
   * 
   * @return The current player's name.
   */
  public String getCurrentPlayerName() {
    return currentplayer.getName();
  }

  /**
   * Returns the current turn count of the current player.
   * 
   * @return The current turn count of the current player.
   */
  public int getCurrentPlayerTurn() {
    return currentplayer.getturn();
  }

  /**
   * Switches the current player to the other player.
   */
  public void changerCurrentPlayer() {
    int currentIndex = playerlist.indexOf(currentplayer);
    currentplayer = playerlist.get((currentIndex + 1) % playerlist.size());
}

  /**
   * Increases the turn count for the current player by one.
   */
  public void addCurrentPlayerTurn() {
    currentplayer.addturn();
  }
  
  /**
   * Retrieves the list of tiles owned by a specific player.
   *
   * @param playername The name of the player.
   * @return The list of tiles owned by the player.
   */
  public List<Tile> getCurrentPlayerTiles(String playername) {
    Objects.requireNonNull(playername);
    for (Player player : playerlist) {
        if (player.getName().equals(playername)) {
            return player.getTiles();
        }
    }
    return new ArrayList<>(); 
}

  /**
   * Returns the current step description.
   * 
   * @return The current step description.
   */
  public String getCurrentStep() {
    return currentStep;
  }

  /**
   * Sets the current step description.
   * 
   * @param currentStep The description of the current step.
   */
  public void setCurrentStep(String currentStep) {
    Objects.requireNonNull(currentStep);
    this.currentStep = currentStep;
  }

  /**
   * Returns the secondary step description.
   * 
   * @return The secondary step description.
   */
  public String getCurrentStep2() {
    return currentStep2;
  }

  /**
   * Sets the secondary step description.
   * 
   * @param currentStep The description of the secondary step.
   */
  public void setCurrentStep2(String currentStep) {
    Objects.requireNonNull(currentStep);
    this.currentStep2 = currentStep;
  }

  /**
   * Adds a tile to the current player's collection of tiles.
   * 
   * @param tile The tile to add.
   */
  public void addTile(Tile tile) {
    Objects.requireNonNull(tile);
    currentplayer.addTile(tile);
  }
  
}

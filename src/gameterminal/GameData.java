package gameterminal;

import game.Tile;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import game.Animal;
import game.Player;


/**
 * Represents the game data, including tile pool, animal pool, players, and the current game state.
 */
public class GameData {

    private final ArrayList<Tile> tilePool;       // Main tile pool
    private final ArrayList<Tile> initialTilePool; // Initial tile pool
    private final ArrayList<Animal> animalPool;   // Animal token pool

    private int playernumber;                     // Number of players
    private final ArrayList<Player> playerlist;   // List of players

    private final ArrayList<Tile> choosetile;     // Available tiles for selection

    private final Player player1;                 // Player 1
    private final Player player2;                 // Player 2

    private Player currentplayer;                 // Current player

    private String currentStep;                   // Current step in the game
    private String currentStep2;                  // Additional step tracker

    private int error;                            // Error flag
    private int isInitial;                        // Initialization status
    private int isSetAnimal;                      // Animal placement flag
    private Tile selectedtile;                    // Currently selected tile

    // Landscape types and their supported animals
    private final String[] landscapes = {"Forest", "Grassland", "Wetland", "Mountain", "River"};
    private final Animal[][] animalsPerLandscape = {
        {new Animal("Bear"), new Animal("Fox")},       // Forest: Bear, Fox
        {new Animal("Deer"), new Animal("Eagle")},     // Grassland: Deer, Eagle
        {new Animal("Salmon"), new Animal("Fox")},     // Wetland: Salmon, Fox
        {new Animal("Bear"), new Animal("Deer")},      // Mountain: Bear, Deer
        {new Animal("Salmon"), new Animal("Deer")}     // River: Salmon, Deer
    };

    /**
     * Initializes the game data, including tile pools, animal pool, and player setup.
     */
    public GameData() {
        tilePool = new ArrayList<>();
        initialTilePool = new ArrayList<>();
        choosetile = new ArrayList<>();
        animalPool = new ArrayList<>();
        playerlist = new ArrayList<>();

        player1 = new Player("Player 1");
        player2 = new Player("Player 2");

        currentStep = null;
        currentStep2 = null;
        selectedtile = null;

        currentplayer = player1;

        error = 0;
        isInitial = 0;
        isSetAnimal = 0;
    }

    /**
     * Checks if a tile already has an animal placed on it.
     *
     * @param tile The tile to check.
     * @return true if an animal is placed, false otherwise.
     */
    public boolean isTileplaced(Tile tile) {
    	Objects.requireNonNull(tile, "Tile must not be null");
    	
        Animal animal = currentplayer.getAnimalset().get(tile);
        return animal != null;
    }

    /**
     * Gets the current player's tile positions.
     *
     * @return A map of tile positions.
     */
    public Map<Tile, Point> getPosition() {
    	

        return currentplayer.getPosition();
    }

    /**
     * Sets the position of the selected tile.
     *
     * @param x X-coordinate.
     * @param y Y-coordinate.
     */
    public void setPosition(int x, int y) {
        currentplayer.setPosition(selectedtile, x, y);
    }

    /**
     * Gets the currently selected tile.
     *
     * @return The selected tile.
     */
    public Tile getselectedtile() {
        return selectedtile;
    }

    /**
     * Clears the selected tile.
     */
    public void setselectedtilenull() {
        selectedtile = null;
    }

    /**
     * Sets the selected tile.
     *
     * @param tile The tile to select.
     */
    public void setselectedtile(Tile tile) {
    	Objects.requireNonNull(tile, "Tile must not be null");
    	
        selectedtile = tile;
    }

    /**
     * Marks that an animal has been placed.
     */
    public void setAnimal() {
        isSetAnimal = 1;
    }

    /**
     * Gets the animal placement status.
     *
     * @return 1 if an animal is placed, 0 otherwise.
     */
    public int getisSetAnimal() {
        return isSetAnimal;
    }

    /**
     * Increases the initialization count.
     */
    public void addisInitial() {
        isInitial++;
    }

    /**
     * Gets the initialization count.
     *
     * @return The number of initializations.
     */
    public int getisIntiail() {
        return isInitial;
    }

    /**
     * Sets the error flag.
     */
    public void seterror() {
        error = 1;
    }

    /**
     * Clears the error flag.
     */
    public void cleanerror() {
        error = 0;
    }

    /**
     * Gets the error flag status.
     *
     * @return 1 if an error occurred, 0 otherwise.
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
	Objects.requireNonNull(tile, "Tile must not be null");
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
   * Removes a specific tile from the tile pool.
   * 
   * @param tile The tile to remove from the pool.
   */
  public void removeTilePool(Tile tile) {
	Objects.requireNonNull(tile, "Tile must not be null");
    tilePool.remove(tile);
  }

  /**
   * Adds a tile to the choose pool (currently available tiles).
   * 
   * @param tile The tile to add to the choose pool.
   */
  public void addTileToChoosePool(Tile tile) {
	  
	Objects.requireNonNull(tile, "Tile must not be null");  
    choosetile.add(tile);
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
   * Clears the list of tiles available for choosing.
   */
  public void clearChooseTile() {
    choosetile.clear();
  }

  /**
   * Removes a specific tile from the choose pool.
   * 
   * @param tile The tile to remove from the choose pool.
   */
  public void removeChooseTile(Tile tile) {
	  
	Objects.requireNonNull(tile, "Tile must not be null");  
    choosetile.remove(tile);
  }

  /**
   * Adds an animal to the animal pool.
   * 
   * @param animal The animal to add to the pool.
   */
  public void addAnimalToPool(Animal animal) {
	Objects.requireNonNull(animal, "animal must not be null");
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
   * Removes a specific animal from the animal pool.
   * 
   * @param animal The animal to remove from the pool.
   */
  public void removeAnimal(Animal animal) {
    animalPool.remove(animal);
  }

  /**
   * Initializes the turn count for the current player.
   * 
   * @param playername The name of the player whose turn count is to be initialized.
   */
  public void initialCurrentPlayerTurn(String playername) {
	Objects.requireNonNull(playername, "playername must not be null");
    if (playername.equals("Player 1")) {
      player1.initialturn();
    } else {
      player2.initialturn();
    }
  }

  /**
   * Returns the name of the current player.
   * 
   * @return The current player's name.
   */
  public Player getCurrentPlayer() {
	    return currentplayer; // 返回当前玩家
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
   * Increases the turn count for the current player by one.
   */
  public void addCurrentPlayerTurn() {
    currentplayer.addturn();
  }

  /**
   * Returns the list of tiles owned by the specified player.
   * 
   * @param playername The name of the player.
   * @return The list of tiles owned by the specified player.
   */
  public List<Tile> getCurrentPlayerTiles(String playername) {
    Objects.requireNonNull(playername, "playername must not be null");
    if (playername.equals("Player 1")) {
      return player1.getTiles();
    } else {
      return player2.getTiles();
    }
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
	Objects.requireNonNull(currentStep, "currentstep must not be null");  
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
	  
	Objects.requireNonNull(currentStep, "currentstep must not be null");  
    this.currentStep2 = currentStep;
  }

  /**
   * Adds a tile to the current player's collection of tiles.
   * 
   * @param tile The tile to add.
   */
  public void addTile(Tile tile) {
	Objects.requireNonNull(tile, "tile must not be null");  
    currentplayer.addTile(tile);
  }
  
  
  
  
  //*************************************new add method====================================
  
  
  
  
  /**
   * Retrieves all landscape types.
   *
   * @return an array of landscape types.
   */
  public String[] getLandscapes() {
      return landscapes;
  }

  
  /**
   * Retrieves the animals supported for a specific landscape by index.
   *
   * @param index the index of the landscape.
   * @return an array of animals supported by the specified landscape.
   * @throws IllegalArgumentException if the index is invalid.
   */
  public Animal[] getAnimalsForLandscape(int index) {
	  
      if (index < 0 || index >= animalsPerLandscape.length) {
          throw new IllegalArgumentException("Invalid landscape index");
      }
      return animalsPerLandscape[index];
  }

  /**
   * Shuffles the animal pool randomly.
   */
  public void ChuffleAnimalPool() {
      Collections.shuffle(animalPool);
  }
  

  
  /**
   * Finds a tile on the board by player and position.
   *
   * @param player the player owning the tile.
   * @param x the x-coordinate.
   * @param y the y-coordinate.
   * @return the tile at the specified position or null if not found.
   */
  public Tile findTileByPosition(Player player, int x, int y) {
	  Objects.requireNonNull(player, "player must not be null");  
	  
      Map<Tile, Point> positions = player.getPosition();
      for (Map.Entry<Tile, Point> entry : positions.entrySet()) {
          Point point = entry.getValue();
          if (point.x == x && point.y == y) {
              return entry.getKey();
          }
      }
      return null;
  }
  
  
  /**
   * Retrieves the initial tile pool as an unmodifiable list.
   *
   * @return the initial tile pool.
   */
  public List<Tile> getInitialTilePool() {
      return Collections.unmodifiableList(initialTilePool);
  }

  /**
   * Adds a tile to the initial tile pool.
   *
   * @param tile the tile to add.
   */
  public void addTileToInitialPool(Tile tile) {
	  Objects.requireNonNull(tile, "tile must not be null");  
      initialTilePool.add(tile);
  }

  /**
   * Shuffles the initial tile pool randomly.
   */
  public void shuffleInitialTilePool() {
      Collections.shuffle(initialTilePool);
  }

  /**
   * Removes the first tile with the specified landscape from the initial tile pool.
   *
   * @param landscape the landscape type to search for.
   * @return the removed tile or null if not found.
   */
  public Tile removeFirstTileByLandscape(String landscape) {
	  Objects.requireNonNull(landscape, "landscape must not be null");  
      for (Tile tile : initialTilePool) {
          if (tile.landscape().equals(landscape)) {
              initialTilePool.remove(tile);
              return tile;
          }
      }
      return null;
  }

  
  /**
   * Removes the last tile from the tile pool.
   *
   * @return the removed tile or null if the pool is empty.
   */
  public Tile removeLastTileFromPool() {
      if (!tilePool.isEmpty()) {
          return tilePool.remove(tilePool.size() - 1);
      }
      return null;
  }

  /**
   * Removes the last animal from the animal pool.
   *
   * @return the removed animal or null if the pool is empty.
   */
  public Animal removeLastAnimalFromBag() {
      if (!animalPool.isEmpty()) {
          return animalPool.remove(animalPool.size() - 1);
      }
      return null;
  }
  
  /**
   * Generates a new combination of a tile and an animal from their respective pools.
   *
   * @return a map entry containing the new tile and animal, or null if either pool is empty.
   */
  public Map.Entry<Tile, Animal> generateNewCombination() {
      if (!tilePool.isEmpty() && !animalPool.isEmpty()) {
          Tile newTile = removeLastTileFromPool();
          Animal newAnimal = removeLastAnimalFromBag();
          return Map.entry(newTile, newAnimal);
      }
      return null;
  }

  /**
   * Retrieves the player list as an unmodifiable list.
   *
   * @return the list of players.
   */
  public List<Player> getPlayerList() {
      return Collections.unmodifiableList(playerlist);
  }

  /**
   * Sets the number of players.
   *
   * @param i the number of players.
   */
  public void setplayernumber(int i) {
      playernumber = i;
  }

  /**
   * Retrieves the number of players.
   *
   * @return the number of players.
   */
  public int getPlayerNumber() {
      return playernumber;
  }

  /**
   * Creates players and adds them to the player list.
   */
  public void createplayer() {
      for (int i = 1; i <= playernumber; i++) {
          playerlist.add(new Player("Player" + i));
      }
      currentplayer = playerlist.get(0);
  }
  
  /**
   * Switches the current player to the other player.
   */
  public void changerCurrentPlayer() {

	    int currentIndex = playerlist.indexOf(currentplayer);
	    currentplayer = playerlist.get((currentIndex + 1) % playerlist.size());
	}
  
  /**
   * Checks if an animal is valid for a specific tile.
   *
   * @param tile the tile to check.
   * @param animal the animal to check.
   * @return true if the animal is valid for the tile, false otherwise.
   */
  public boolean isAnimalValidForTile(Tile tile, Animal animal) {
	  Objects.requireNonNull(tile, "tile must not be null");  
	  Objects.requireNonNull(animal, "animal must not be null");  
	    
	    String landscape = tile.landscape();

	    
	    for (int i = 0; i < landscapes.length; i++) {
	        if (landscapes[i].equals(landscape)) {
	            // check
	            for (Animal validAnimal : animalsPerLandscape[i]) {
	                if (validAnimal.equals(animal)) {
	                    return true; 
	                }
	            }
	        }
	    }

	    return false; 
	}
  
  
  /**
   * Determines if a player can place an animal on the board.
   *
   * @param player the player attempting to place the animal.
   * @param animal the animal to place.
   * @return true if the player can place the animal, false otherwise.
   */
  public boolean canPlaceAnimalOnBoard(Player player, Animal animal) {
	  
	  Objects.requireNonNull(player, "tile must not be null");  
	  Objects.requireNonNull(animal, "animal must not be null");  
	  
	    for (Tile tile : player.getTiles()) {

	        if (!isTileplaced(tile) && isAnimalValidForTile(tile, animal)) {
	            return true;  
	        }
	    }
	    return false;  
	}


  
}



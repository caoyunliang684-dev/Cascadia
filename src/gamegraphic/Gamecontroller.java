package gamegraphic;

import bearscore.*;
import deerscore.*;
import eaglescore.*;
import foxscore.*;
import game.Player;
import game.Animal;
import game.Tile;
import salmonscore.*;
import score.FamilyScore;
import score.MediumScore;
import score.ScoreCalculate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.awt.Color;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;
/**
 * Represents the game controller which handles the main game logic and player interactions.
 */
public class Gamecontroller {
  private static final int WINDOW_HEIGHT = 800;
  private static final int TILE_SIZE = 100;
  private static final int CELL_SIZE = 80;
  
  private final GameData gamedata;
  private final Gameview gameview;
  
  /**
   * Constructs a new Gamecontroller with specified game data and view.
   *
   * @param gamedata The game data object.
   * @param gameview The game view object.
   * @throws NullPointerException if gamedata or gameview is null.
   */
  public Gamecontroller(GameData gamedata,Gameview gameview) {
    this.gamedata = Objects.requireNonNull(gamedata);
    this.gameview = Objects.requireNonNull(gameview);
  }
  
  /**
   * Starts the game by initializing resources and setting up the main loop.
   */
  public void start() {
    initializeGame();
    try {
      chooseGameMode();
    } catch (IOException e) {
      e.printStackTrace();
    }
    Application.run(Color.WHITE, context -> {
      gamedata.setplayernumber(2);
      gamedata.createplayer();
      handleInitialAssignment(context);
      handleGame(context);
      socrecalculate();
      context.dispose();
    });
  }
  
  /**
   * Prompts the player to choose the game mode and sets the appropriate configuration.
   *
   * @throws IOException If an I/O error occurs during input.
   */
  private void chooseGameMode() throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    int choice = 0;
    while (true) {
      System.out.println("Please choose Game Mode: ");
      System.out.println("1. Family Mode");
      System.out.println("2. Intermediate Mode");
      System.out.println("3. Single Animal Scoring Card Mode");
      try {
        String input = reader.readLine(); 
        choice = Integer.parseInt(input); 
        if (choice >= 1 && choice <= 3) {
          break; 
        } else {
          System.out.println("Invalid input. Number must be between 1 and 3.");
        }
      } catch (NumberFormatException e) {
          System.out.println("Invalid input. Please enter a valid number.");
          continue;
      }
    }
    if (gamedata.getGamemode() == 3) {
      gamedata.setScoreMap(chooseAnimalScoringCards());
    }
    gamedata.setGamemode(choice);
}
  
  /**
   * Calculates the score for all players based on the selected game mode.
   */
  private void socrecalculate() {
    int mode = gamedata.getGamemode();
    if (mode == 1) {
      ScoreCalculate scoreCalculator = new FamilyScore();
      for (Player player : gamedata.getAllPlayers()) {
        int score = scoreCalculator.calculateScore(player);
        System.out.println("Player " + player.getName() + " => score: " + score);
      }
    }
    else if (mode == 2) {
      ScoreCalculate scoreCalculator = new MediumScore();
      for (Player player : gamedata.getAllPlayers()) {
        int score = scoreCalculator.calculateScore(player);
        System.out.println("Player " + player.getName() + " => score: " + score);
      }
    }
      else {
        int total = 0;
        for (Player player : gamedata.getAllPlayers()) {
          for (Map.Entry<String, ScoreCalculate> entry : gamedata.getScoreMap().entrySet()) {
            String animal = entry.getKey();               
            ScoreCalculate calculator = entry.getValue(); 
            int animalScore = calculator.calculateScore(player);
            System.out.println(player.getName() + " score for " + animal + " is" + animalScore);
            total += animalScore;
          } 
        }
        System.out.println("Total score :" + total);
      }
}
  
  /**
   * Allows players to choose scoring strategies for specific animals in single animal scoring card mode.
   *
   * @return A map associating each animal with its chosen scoring strategy.
   */
  private Map<String, ScoreCalculate> chooseAnimalScoringCards() {
    Map<String, ScoreCalculate> animalScoringMap = new HashMap<>();
    String[] animals = {"Bear", "Deer", "Fox", "Salmon", "Eagle"};
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    for (String animal : animals) {
      System.out.println("Pleasec choose mode for" + animal);
      System.out.println("A. mode A");
      System.out.println("B. mode B");
      System.out.println("C. mode C");
      System.out.println("D. mode D");
      char choice = ' ';
      while (true) {
        try {
          System.out.print("Please input A/B/C/D: ");
          String input = reader.readLine().trim().toUpperCase();
        if (input.length() == 1 && "ABCD".contains(input)) {
          choice = input.charAt(0);
          break;
        } else {
          System.out.println("Invalid input,choose A/B/C/D！");
        }
        } catch (IOException e) {
            System.out.println("Error！");
            continue;
        }
      animalScoringMap.put(animal, getScoringStrategy(animal, choice));
      }
    }
   return animalScoringMap;
  }

  /**
   * Returns the scoring strategy for a specific animal and choice.
   *
   * @param animal The name of the animal.
   * @param choice The choice for the scoring strategy.
   * @return The corresponding scoring strategy.
   */
  private ScoreCalculate getScoringStrategy(String animal, char choice) {
    switch (animal) {
      case "Bear":
        return switch (choice) {
          case 'A' -> new BearA();
          case 'B' -> new BearB();
          case 'C' -> new BearC();
          case 'D' -> new BearD();
          default -> null;
        };
      case "Deer":
        return switch (choice) {
          case 'A' -> new DeerA();
          case 'B' -> new DeerB();
          case 'C' -> new DeerC();
          case 'D' -> new DeerD();
          default -> null;
        };
      case "Fox":
        return switch (choice) {
          case 'A' -> new FoxA();
          case 'B' -> new FoxB();
          case 'C' -> new FoxC();
          case 'D' -> new FoxD();
          default -> null;
        };
      case "Salmon":
        return switch (choice) {
          case 'A' -> new SalmonA();
          case 'B' -> new SalmonB();
          case 'C' -> new SalmonC();
          case 'D' -> new SalmonD();
          default -> null;
        };
      case "Eagle":
        return switch (choice) {
          case 'A' -> new EagleA();
          case 'B' -> new EagleB();
          case 'C' -> new EagleC();
          case 'D' -> new EagleD();
          default -> null;
        };
      default:
        return null;
    }
}
  
  /**
   * Handles the main game loop, including player turns and rendering the game view.
   *
   * @param context The application context used for rendering and events.
   */
  private void handleGame(ApplicationContext context) {
    SetTilepool();
    SetAnimalpool();
    gameview.renderGame(context);
    while(true) {
      while (!checkabletoput()) {
        SetTilepool();
        SetAnimalpool();
      }
      gamedata.setpartner();
      gameview.renderGame(context);
      if(checkevent(context)) {
        continue;
      }
      if(gamedata.getselectedanimal() != null) {
        setCurrentStep("Please set： "+ gamedata.getselectedanimal().name());
      }
      gameview.renderGame(context);
      checkPlayerTurn(context);
      if (gamedata.getGameTurn() == 40) {
        break;
      }
    }
  }
  
  /**
   * Checks if there are valid placements available for the current player.
   *
   * @return true if there are valid placements, false otherwise.
   */
  private boolean checkabletoput() {
    List<Tile> tiles = gamedata.getCurrentPlayerTiles(gamedata.getCurrentPlayerName());
    Set<Animal> animals = new HashSet<>();
    for (Tile tile : tiles) {
      if (gamedata.getAnimalSet().get(tile) == null) {
        for (Animal animal : tile.animals()) { 
          animals.add(animal);
        }
      }
    } 
    for (int i = gamedata.getChooseAnimal().size() - 1; i >= 0; i--) {
      Animal curAnimal = gamedata.getChooseAnimal().get(i);
      Tile   curTile   = gamedata.getChooseTile().get(i);
      if (!animals.contains(curAnimal)) {
        gamedata.addAnimaltoPool(curAnimal); 
        gamedata.addTileToPool(curTile);
        gamedata.removeChooseAnimal(curAnimal);
        gamedata.removeChooseTile(curTile);
      }
  }
    if(gamedata.getChooseAnimal().isEmpty() && gamedata.getChooseTile().isEmpty()) {
      return false;
    }
    return true;
}
  
  /**
   * Handles the end of a player's turn and sets up for the next turn.
   *
   * @param context The application context used for rendering.
   */
  private void checkPlayerTurn(ApplicationContext context) {
    if(gamedata.geterror() ==1) {
      setCurrentStep2("Invalid area");
    }
    else if(gamedata.getturnover() == 1){
      setCurrentStep("Set successfully, continue after 5 seconds");
      setCurrentStep2("Now it's turn:"+ gamedata.getCurrentPlayerTurn());
      gamedata.addCurrentPlayerTurn(); 
      gameview.renderGame(context);
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      ChangeTurn();
    }
  }
  
  /**
   * Switches to the next player's turn and resets relevant game data.
   */
  private void ChangeTurn() {
    gamedata.changerCurrentPlayer();
    setCurrentStep("Choose tile");
    setCurrentStep2("Now it's turn:"+ gamedata.getCurrentPlayerTurn());
    gamedata.readdanimalpool();
    gamedata.readdtilepool();
    gamedata.clearChooseTile();
    gamedata.clearChooseAnimal();
    gamedata.cleanpartner();
    gamedata.addGameTurn();
    gamedata.cleanturnover();
    SetTilepool();
    SetAnimalpool();
  }
  
  /**
   * Fills the tile pool with tiles available for the current turn.
   */
  private void SetTilepool() {
    if (gamedata.getTilePool().size() < 4) {
      throw new IllegalStateException("The number of tile is less than 4");
  }
    gamedata.ChuffleTilePool();
    for (int i = 0; i < 4 ; i++) {
     gamedata.addTileToChooseTile(gamedata.getTilePool().get(i));
     gamedata.removeTilePool(gamedata.getTilePool().get(i));
    }
  }
  
  /**
   * Fills the animal pool with animals available for the current turn.
   */
  private void SetAnimalpool() {
    if (gamedata.getAnimalPool().size() < 4) {
      throw new IllegalStateException("The number of animal mark is less than 4");
    }
    int ss = 0;
    while(ss == 0) {
      gamedata.ChuffleAnimalPool();
      for (int i = 0 ; i < 4 ;i ++) {
        gamedata.addAnimalToChooseAnimal(gamedata.getAnimalPool().get(i));
        gamedata.removeAnimalPool(gamedata.getAnimalPool().get(i));
      }
      if (Collections.frequency(gamedata.getChooseAnimal(), gamedata.getChooseAnimal().get(0)) == 4) {     
        gamedata.readdanimalpool();
        gamedata.clearChooseAnimal();
      }else {
        ss = 1;
      }
    }
  }
  
  /**
   * Handles the initial setup of the game, including assigning initial tiles to players.
   *
   * @param context The application context used for rendering and events.
   */
  private void handleInitialAssignment(ApplicationContext context){
    setCurrentStep("Choose initial tile");
    gameview.renderGame(context);
    while(true) {
      gameview.renderGame(context);
      if (checkevent(context)) {
        continue;
      }
      gameview.renderGame(context);
      Initialchoose(context);
      if (gamedata.getisIntiail() == 2) {
        gamedata.clearChooseTile();
        break;
      }
    }  
  }
  
  /**
   * Handles player interactions during the initial tile placement phase.
   *
   * @param context The application context used for rendering.
   */
  private void Initialchoose(ApplicationContext context) {
    if (gamedata.geterror() == 1) {
      setCurrentStep2("Invalid area");
    }
    else if (gamedata.getCurrentPlayerTurn() <= 3) {
      setCurrentStep2("Continue");
    } else {
      setCurrentStep("Set successfully, continue after 5 seconds");
      setCurrentStep2("Good!");
      initialCurrentPlayerTurn();
      gamedata.addisInitial();
      gameview.renderGame(context);
      try {
        Thread.sleep(5000); 
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      gamedata.changerCurrentPlayer();
      setCurrentStep("Choose initial tile");
      setCurrentStep2("Good!");
      gamedata.clearChooseTile();
      InitialTiles(gamedata.getTilePool());
    }
  }
  
  /**
   * Checks for and processes user input events during the game.
   *
   * @param context The application context used for event polling.
   * @return true if no significant event occurred, false otherwise.
   */
  private boolean checkevent(ApplicationContext context) {
    var event = context.pollOrWaitEvent(10);
    if (event == null) {
      return true;
    } else if (event instanceof KeyboardEvent ke) {
      if (ke.key() == KeyboardEvent.Key.Q) { 
        context.dispose();
        System.exit(0);
      }
    } else if (event instanceof PointerEvent pe) {
      if (pe.action() == PointerEvent.Action.POINTER_UP) {
        handlePointerEvent(context, pe);
      }
    }
    return false;
  }
  
  /**
   * Handles mouse click events for tile and animal placement.
   *
   * @param context The application context used for rendering.
   * @param event   The pointer event triggered by a mouse click.
   */
  private void handlePointerEvent(ApplicationContext context,PointerEvent event) {
    if(gamedata.getisIntiail() < 2 ) {
      handlePointerEventTile(context, event);
    }
    else {
      if(gamedata.getisSetAnimal() == 1) {
        handlePointerEventAnimal(context,event);
      }
      else {
        handlePointerEventTile(context,event);
      }
    }   
  }
  
  /**
   * Handles animal placement when the player selects an animal.
   *
   * @param context The application context used for rendering.
   * @param event   The pointer event triggered by a mouse click.
   */
  private void handlePointerEventAnimal(ApplicationContext context,PointerEvent event) {
    var location = event.location();
    int x = (int) location.x();
    int y = (int) location.y();
    handlePlayerAnimalClick(x,y,gamedata.getCurrentPlayerName());
  }
  
  /**
   * Places a animal mark for the player based on the given mouse coordinates.
   *
   * @param x     The x-coordinate of the mouse click.
   * @param y     The y-coordinate of the mouse click.
   * @param player The current player's name.
   */
  private void handlePlayerAnimalClick(int x, int y , String player) {
    int animalX = (x - 100)/CELL_SIZE;
    int animalY = (y - 100)/CELL_SIZE;
    placeAnimal(player,gamedata.getselectedanimal(),animalX,animalY);
  }
  
  /**
   * Handles tile placement when the player selects a tile.
   *
   * @param context The application context used for rendering.
   * @param event   The pointer event triggered by a mouse click.
   */
  private void handlePointerEventTile(ApplicationContext context, PointerEvent event) {
    var location = event.location();
    int x = (int) location.x();
    int y = (int) location.y();
    if (gamedata.getselectedtile() == null) {
      handleTileSelection(x, y);
    } else {
      handlePlayerTileClick(x, y, gamedata.getCurrentPlayerName());
    }
  }
  
  /**
   * Selects a tile based on the given mouse coordinates.
   *
   * @param x The x-coordinate of the mouse click.
   * @param y The y-coordinate of the mouse click.
   */
  private void handleTileSelection(int x, int y) {
    int startX = 50;
    int startY = WINDOW_HEIGHT - 150;
    for (int i = 0; i < gamedata.getChooseTile().size(); i++) {
      int tileX = startX + i * (TILE_SIZE + 20);
      if (x >= tileX && x < tileX + TILE_SIZE && y >= startY && y < startY + TILE_SIZE) {
        gamedata.setselectedtile(gamedata.getChooseTile().get(i));
        setCurrentStep("Select tile: " + gamedata.getselectedtile().landscape());
        gamedata.cleanerror();
        break;
      } else {
        gamedata.seterror();
      }
    }
  }
  
  /**
   * Places a tile for the player based on the given mouse coordinates.
   *
   * @param x     The x-coordinate of the mouse click.
   * @param y     The y-coordinate of the mouse click.
   * @param player The current player's name.
   */
  private void handlePlayerTileClick(int x, int y, String player) {
    int gridX = (x - 100) / CELL_SIZE;
    int gridY = (y - 100) / CELL_SIZE;
    placeTile(player, gamedata.getselectedtile(), gridX, gridY);
    gamedata.setselectedtilenull();
  }

  /**
   * Places a tile in the player's area at the specified coordinates.
   * 
   * @param player The player placing the tile.
   * @param chosenTile The tile to be placed.
   * @param x The x-coordinate of the placement.
   * @param y The y-coordinate of the placement.
   */
  private void placeTile(String player, Tile chosenTile, int x, int y) {
    if (isPlacementValidTile(player, x, y) && x >= 0 && x < 8 && y >= 0 && y < 5) {
      if(gamedata.getisIntiail() < 2) {
        Tile tile = new Tile(chosenTile.landscape(),chosenTile.animals(),gamedata.getspecialid());
        gamedata.setPosition(tile,x, y);
        gamedata.addCurrentPlayerTurn(); 
        gamedata.addTile(tile);
      }else
      {
        gamedata.setPosition(chosenTile, x, y);
        gamedata.addTile(chosenTile);
      }
      gamedata.setCurrentStep2("Set successfully at " + chosenTile.landscape() + "(" + x + ", " + y + ")");
      gamedata.setselectedanimal(gamedata.getpartner(gamedata.getselectedtile()));
      if (gamedata.getisIntiail() == 2) {
        gamedata.removeTilePool(chosenTile);
        gamedata.setAnimal();
      }
    } else {
      gamedata.setCurrentStep("Cannot set at (" + x + ", " + y + ")");
    }
  }
  
  /**
   * Places an animal in the player's area at the specified coordinates.
   *
   * @param player       The player placing the animal.
   * @param chosenAnimal The animal to be placed.
   * @param x            The x-coordinate of the placement.
   * @param y            The y-coordinate of the placement.
   */
  private void placeAnimal(String player, Animal chosenAnimal, int x , int y) {
    if(isPlacementValidAnimal(chosenAnimal,player,x,y)) {
      gamedata.setAnimalonTile(chosenAnimal,x,y);
      gamedata.setCurrentStep("Set successfully" + chosenAnimal.name());
      gamedata.removeAnimalPool(chosenAnimal);
      gamedata.cleanAnimal();
      gamedata.setturnover();
      gamedata.setselectedanimalnull();
      gamedata.cleanerror();
    }
    else {
      gamedata.seterror();
    }
  }
  
  
  /**
   * Initializes the game resources, including generating initial tiles and animal pool.
   */
  private void initializeGame() {
    gamedata.ChuffleTilePool();
    InitialTiles(gamedata.getTilePool());
  }

  /**
   * Checks if a placement is valid for the player at the specified coordinates.
   * 
   * @param player The current player.
   * @param x The x-coordinate of the placement.
   * @param y The y-coordinate of the placement.
   * @return true if the placement is valid, false otherwise.
   */
  private boolean isPlacementValidTile(String player, int x, int y) {
    if (gamedata.getCurrentPlayerTiles(player).isEmpty()) {
      return true;
    }
    boolean adjacencyFound = false;  
    for (Map.Entry<Tile, Point> entry : gamedata.getPosition().entrySet()) {
      Point point = entry.getValue();
      if (point.x == x && point.y == y) {
        return false;
      }
      if (areTilesAdjacent(point, x, y)) {
        adjacencyFound = true;
      }
    }
    return adjacencyFound;
}

  /**
   * Checks if an animal placement is valid for the current player.
   *
   * @param animal The animal to be placed.
   * @param player The current player.
   * @param x      The x-coordinate of the placement.
   * @param y      The y-coordinate of the placement.
   * @return true if the placement is valid, false otherwise.
   */
  private boolean isPlacementValidAnimal(Animal animal,String player, int x, int y) {
    for (Map.Entry<Tile, Point> entry : gamedata.getPosition().entrySet()) {
      Point point = entry.getValue();
      if (point.x == x && point.y == y) {
        Tile tile = entry.getKey();
        int contain = 0;
        for (Animal animals : tile.animals()) {
          if (animal.equals(animals)) {
            contain = 1;
          }
      }
        if (gamedata.AnimalonTile(tile) == null && contain == 1) {
          return true;
        }
      }    
    }
    return false;
  }
   
  
  
  /**
   * Determines if two tiles are adjacent based on their coordinates.
   * 
   * @param tile1 The existing tile.
   * @param x The x-coordinate of the new tile.
   * @param y The y-coordinate of the new tile.
   * @return true if the tiles are adjacent, false otherwise.
   */
  private boolean areTilesAdjacent(Point point, int x, int y) {
    int dx = point.x - x;
    int dy = point.y - y;
    if (dx == 0 && dy == 0) {
      return false;
    }
    if ((dx == 0 && Math.abs(dy) == 1) || (dy == 0 && Math.abs(dx) == 1)) {
      return true;
    }
    return false;
  }

  /**
   * Initializes tiles for the game by adding them to the initial tile pool.
   * 
   * @param tilePool The list of tiles to be initialized.
   */
  private void InitialTiles(List<Tile> tilePool) {
    ArrayList<String> initialtile = new ArrayList<>();
    for (Tile tile : tilePool) {
      if (!initialtile.contains(tile.landscape())) {
        initialtile.add(tile.landscape());
        gamedata.addTileToChooseTile(tile);
      }
      if (initialtile.size() == 6) {
        break;
      }
    }
  }
  
  /**
   * Sets the current step description in the game data.
   *
   * @param text The description of the current step.
   */
  private void setCurrentStep(String text) {
    gamedata.setCurrentStep(gamedata.getCurrentPlayerName() + text);
  }
  
  /**
   * Sets the secondary step description in the game data.
   *
   * @param text The description of the secondary step.
   */
  private void setCurrentStep2(String text) {
    gamedata.setCurrentStep2(gamedata.getCurrentPlayerName() + text);
  }
  
  /**
   * Initializes the current player's turn.
   */
  private void initialCurrentPlayerTurn() {
    gamedata.initialCurrentPlayerTurn(gamedata.getCurrentPlayerName());
  }
  
}

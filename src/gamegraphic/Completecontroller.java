package gamegraphic;

import game.Tile;
import game.Animal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;
/**
 * Represents the game controller, managing the main game logic and player interactions for the complete version.
 */
public class Completecontroller {
  private static final int WINDOW_HEIGHT = 800;
  
  private final GameData gamedata;
  private final Completeview completeview;
  
  /**
   * Constructs a new Completecontroller with the specified game data and view.
   *
   * @param gamedata       The game data object.
   * @param completeview   The view object for rendering the game.
   *  @throws NullPointerException if gamedata or completeview is null.
   */
  public Completecontroller(GameData gamedata,Completeview completeview) {
    this.gamedata = Objects.requireNonNull(gamedata);
    this.completeview = Objects.requireNonNull(completeview);
  }

  /**
   * Starts the game by initializing resources and running the main game loop.
   *
   * @throws IOException If an I/O error occurs while getting player input.
   */
  public void start() throws IOException {
    gamedata.setplayernumber(getPlayerCount());
    gamedata.createplayer();
    initializeGame();
    Application.run(Color.WHITE, context -> {
      handleInitialAssignment(context);
      handleGame(context);
      context.dispose();
    });
  }
  
  /**
   * Prompts the user to enter the number of players and validates the input.
   *
   * @return The number of players (between 2 and 4).
   * @throws IOException If an I/O error occurs while reading input.
   */
  private int getPlayerCount() throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    int playerCount = 0;
    while (true) {
        System.out.print("Please enter the number of players (2-4): ");
        try {
            String input = reader.readLine(); 
            playerCount = Integer.parseInt(input); 
            if (playerCount >= 2 && playerCount <= 4) {
                break; 
            } else {
                System.out.println("Invalid input. Number of players must be between 2 and 4.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            continue;
        }
    }

    return playerCount;
}
  
  /**
   * Handles the main game logic, including managing player turns and rendering updates.
   *
   * @param context The application context for rendering and event handling.
   */
  private void handleGame(ApplicationContext context) {
    SetTilepool();
    SetAnimalpool();
    completeview.renderGame(context);
    while(true) {
      while (!checkabletoput()) {
        SetTilepool();
        SetAnimalpool();
      }
      gamedata.setpartner();
      completeview.renderGame(context);
      if(checkevent(context)) {
        continue;
      }
      if(gamedata.getselectedanimal() != null) {
        setCurrentStep("Please set： "+ gamedata.getselectedanimal().name());
      }
      completeview.renderGame(context);
      checkPlayerTurn(context);
      if (gamedata.getGameTurn() == 20 * gamedata.getplayernumber()) {
        break;
      }
    }
  }
  
  /**
   * Checks if there are valid tiles and animals available for placement.
   *
   * @return true if there are valid choices; false otherwise.
   */
  private boolean checkabletoput() {
    List<Tile> tiles = gamedata.getCurrentPlayerTiles(gamedata.getCurrentPlayerName());
    Set<Animal> animals = new HashSet<>();
    for (Tile tile : tiles) {
      if (!gamedata.isTileplaced(tile)) {
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
   * Checks if the current player's turn is complete and updates the game state accordingly.
   *
   * @param context The application context for rendering.
   */
  private void checkPlayerTurn(ApplicationContext context) {
    if(gamedata.geterror() ==1) {
      setCurrentStep2("Invalid area");
    }
    else if(gamedata.getturnover() == 1){
      setCurrentStep("Set completed, continue after 5 seconds");
      setCurrentStep2("Now it's turn:" + gamedata.getCurrentPlayerTurn());
      gamedata.addCurrentPlayerTurn();
      completeview.renderGame(context);
      try {
        Thread.sleep(5000); 
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      ChangeTurn();
    }
  }
  
  /**
   * Switches the turn to the next player and resets relevant game data.
   */
  private void ChangeTurn() {
    gamedata.changerCurrentPlayer();
    setCurrentStep("Choose Tile : ");
    setCurrentStep2("Now it's turn:" + gamedata.getCurrentPlayerTurn());
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
   * Initializes the tile pool with shuffled tiles.
   */
  private void SetTilepool() {
    if (gamedata.getTilePool().size() < 4) {
      throw new IllegalStateException("The number of Tile is less than 4");
  }
    gamedata.ChuffleTilePool();
    for (int i = 0; i < 4 ; i++) {
     gamedata.addTileToChooseTile(gamedata.getTilePool().get(i));
     gamedata.removeTilePool(gamedata.getTilePool().get(i));
    }
  }
  
  /**
   * Initializes the animal pool with shuffled animal markers.
   */
  private void SetAnimalpool() {
    if (gamedata.getAnimalPool().size() < 4) {
      throw new IllegalStateException("The number of Animalmark is less than 4");
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
   * Handles the initial assignment of tiles to players.
   *
   * @param context The application context for rendering.
   */
  private void handleInitialAssignment(ApplicationContext context){
    setCurrentStep("Choose initial tile");
    completeview.renderGame(context);
    while(true) {
      completeview.renderGame(context);
      if (checkevent(context)) {
        continue;
      }
      completeview.renderGame(context);
      Initialchoose(context);
      if (gamedata.getisIntiail() == gamedata.getplayernumber()) {
        gamedata.clearChooseTile();
        break;
      }
    }  
  }
  
  /**
   * Handles the logic for players to choose their initial tiles.
   *
   * @param context The application context for rendering and event handling.
   */
  private void Initialchoose(ApplicationContext context) {
    if (gamedata.geterror() == 1) {
      setCurrentStep2("Invalid area");
    }
    else if (gamedata.getCurrentPlayerTurn() <= 3) {
      setCurrentStep2("Continue");
    } else {
      setCurrentStep("Set completed, continue after 5 seconds");
      setCurrentStep2("Good !");
      initialCurrentPlayerTurn();
      gamedata.addisInitial();
      completeview.renderGame(context);
      try {
        Thread.sleep(5000); 
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      gamedata.changerCurrentPlayer();
      setCurrentStep("Choose initial tile");
      setCurrentStep2("Good !");
      gamedata.clearChooseTile();
      InitialTiles(gamedata.getTilePool());
    }
  }
  
  /**
   * Checks for user input events and handles them accordingly.
   *
   * @param context The application context for rendering and event handling.
   * @return true if there are no events to process; false otherwise.
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
   * Handles pointer events for initial turn and main game.
   *
   * @param context The application context for rendering.
   * @param event   The pointer event to handle.
   */
  private void handlePointerEvent(ApplicationContext context,PointerEvent event) {
    if(gamedata.getisIntiail() < gamedata.getplayernumber() ) {
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
   * Handles pointer events specific to animal placement.
   *
   * @param context The application context for rendering.
   * @param event   The pointer event to handle.
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
    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        int R = 40;
        double cx = 100 + col * (Math.sqrt(3)*R);
        double cy = 100 + row * (1.5*R);
        if (row % 2 == 1) {
            cx += (Math.sqrt(3)/2)*R;
        }
        Polygon hex = gamedata.createHextop((int)cx, (int)cy, R);
        if(hex.contains(x,y)) {
          placeAnimal(player,gamedata.getselectedanimal(),col,row);
          return;
        }
       }
    }
    setCurrentStep("The poisition is not in Player area");
  }
  
  /**
   * Handles pointer events specific to tile selection and placement.
   *
   * @param context The application context for rendering.
   * @param event   The pointer event to handle.
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
   * @param mx The x-coordinate of the mouse click.
   * @param my The y-coordinate of the mouse click.
   */
  private void handleTileSelection(int mx, int my) {
    int R = 40; 
    int startX = 100;
    int startY = WINDOW_HEIGHT - 150;
    List<Tile> chooseTiles = gamedata.getChooseTile();
    for (int i = 0; i < chooseTiles.size(); i++) {
        Tile tile = chooseTiles.get(i);
        int cx = startX + i * 100; 
        int cy = startY;
        Polygon hex = gamedata.createHextop(cx, cy, R);
        if (hex.contains(mx, my)) {
            gamedata.setselectedtile(tile);
            setCurrentStep("Selecting tile: " + tile.landscape());
            gamedata.cleanerror();
            return;
        }
    }
    gamedata.seterror();
}

  /**
   * Places a tile for the player based on the given mouse coordinates.
   *
   * @param mx     The x-coordinate of the mouse click.
   * @param my     The y-coordinate of the mouse click.
   * @param player The current player's name.
   */
  private void handlePlayerTileClick(int mx, int my, String player) {
    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        int R = 40;
        double cx = 100 + col * (Math.sqrt(3)*R);
        double cy = 100 + row * (1.5*R);
        if (row % 2 == 1) {
            cx += (Math.sqrt(3)/2)*R;
        }
        Polygon hex = gamedata.createHextop((int)cx, (int)cy, R);
        if (hex.contains(mx, my)) {
          placeTile(player, gamedata.getselectedtile(), col, row);
          gamedata.setselectedtilenull();
          return;
        }
      }
    }
    setCurrentStep("The poisition is not in Player area");
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
    if (isPlacementValidTile(player, x, y) && x >= 0 && x < 8 && y >= 0 && y < 8) {
      if(gamedata.getisIntiail() < gamedata.getplayernumber()) {
        Tile tile = new Tile(chosenTile.landscape(),chosenTile.animals(),gamedata.getspecialid());
        gamedata.setPosition(tile,x, y);
        gamedata.addCurrentPlayerTurn(); 
        gamedata.addTile(tile);
      }else
      {
        gamedata.setPosition(chosenTile, x, y);
        gamedata.addTile(chosenTile);
      }
      setCurrentStep2("Set successfully at " + chosenTile.landscape() + "(" + x + ", " + y + ")");
      gamedata.setselectedanimal(gamedata.getpartner(gamedata.getselectedtile()));
      if (gamedata.getisIntiail() == gamedata.getplayernumber()) {
        gamedata.removeTilePool(chosenTile);
        gamedata.setAnimal();
      }
    } else {
      gamedata.setCurrentStep("Cannot set at (" + x + ", " + y + ")");
    }
  }
  
  /**
   * Places an animal token on the board at the specified position.
   *
   * @param player       The name of the current player.
   * @param chosenAnimal The animal token to be placed.
   * @param x            The x-coordinate for placement.
   * @param y            The y-coordinate for placement.
   */
  private void placeAnimal(String player, Animal chosenAnimal, int x , int y) {
    if(isPlacementValidAnimal(chosenAnimal,player,x,y)) {
      gamedata.setAnimalonTile(chosenAnimal,x,y);
      gamedata.setCurrentStep("Set successfully at: " + chosenAnimal.name());
      gamedata.cleanAnimal();
      gamedata.setturnover();
      gamedata.setselectedanimalnull();
      gamedata.cleanerror();
    }
    else {
      gamedata.setCurrentStep("Cannot set at (" + x + ", " + y + ")");
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
   * Checks whether an animal can be placed at the specified position for the given player.
   *
   * @param animal The animal token to be placed.
   * @param player The name of the current player.
   * @param x      The x-coordinate of the position.
   * @param y      The y-coordinate of the position.
   * @return true if the placement is valid; false otherwise.
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
   * Determines whether two tiles are adjacent based on their coordinates.
   *
   * @param point The point representing the position of an existing tile.
   * @param x     The x-coordinate of the new tile.
   * @param y     The y-coordinate of the new tile.
   * @return true if the tiles are adjacent; false otherwise.
   */
   private boolean areTilesAdjacent(Point point, int x, int y) {   
     int dx = point.x - x;
     int dy = point.y - y;
     if (point.x == x && point.y == y) {
         return false;
     }
     if (y % 2 == 1) {
         if (
             (dx == 0 && dy == -1)  || 
             (dx == 1 && dy == -1)  ||
             (dx == 1 && dy == 0)   ||
             (dx == 1 && dy == 1)   ||
             (dx == 0 && dy == 1)   ||
             (dx == -1 && dy == 0) 
         ) {
             return true;
         }
     } else {
         if (
             (dx == 0 && dy == -1)  || 
             (dx == 1 && dy == 0)   ||
             (dx == 0 && dy == 1)   ||
             (dx == -1 && dy == 1)  ||
             (dx == -1 && dy == -1) ||
             (dx == -1 && dy == 0) 
         ) {
             return true;
         }
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
   * Updates the current step message displayed to the player.
   *
   * @param text The step message to display.
   */
  private void setCurrentStep(String text) {
    gamedata.setCurrentStep(gamedata.getCurrentPlayerName() + text);
  }
  
  /**
   * Updates the secondary step message displayed to the player.
   *
   * @param text The secondary step message to display.
   */
  private void setCurrentStep2(String text) {
    gamedata.setCurrentStep2(gamedata.getCurrentPlayerName() + text);
  }
  
  /**
   * Initializes the turn for the current player.
   */
  private void initialCurrentPlayerTurn() {
    gamedata.initialCurrentPlayerTurn(gamedata.getCurrentPlayerName());
  }
  
}

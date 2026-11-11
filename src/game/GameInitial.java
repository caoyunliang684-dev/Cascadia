package game;

import gamegraphic.GameData;
import java.awt.Color;
import java.util.Objects;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

/**
 * Represents the initial setup of the game. This class provides a graphical interface for users to 
 * select between different game modes: terminal, basic graphical, or complete graphical versions.
 */
public class GameInitial {
  private final GameData gamedata;

  /**
   * Constructs the GameInitial instance with the specified game data.
   * 
   * @param gamedata the GameData object that holds game-related data and state.
   */
  public GameInitial(GameData gamedata) {
    this.gamedata = Objects.requireNonNull(gamedata);
  }

  /**
   * Starts the initial game setup process. This provides the user with options to choose between 
   * terminal and graphical versions of the game.
   * 
   * @return an integer representing the user's choice:
   *         1 - Terminal version.
   *         2 - Basic graphical version.
   *         3 - Complete graphical version.
   */
  public int start() {
    int[] choose = {0};
    generateInitialTiles();
    generateAnimalPool();
    Application.run(Color.WHITE, context -> {
      while (true) {
        renderGame(context);
        var event = context.pollOrWaitEvent(10);
        if (event == null) {
          continue;
        } else if (event instanceof KeyboardEvent ke) {
          if (ke.key() == KeyboardEvent.Key.Q) {
            context.dispose();
            System.exit(0);
          }
        } else if (event instanceof PointerEvent pe) {
          if (pe.action() == PointerEvent.Action.POINTER_UP) {
            var location = pe.location();
            int x = (int) location.x();
            int y = (int) location.y();
            int i = handleMouseClick(context, x, y);
            if (i > 0) {
              choose[0] = i;
              break;
            }
          }
        }
      }
      context.dispose();
    });
    return choose[0];
  }

  /**
   * Renders the initial setup screen where the user can choose the game mode.
   * 
   * @param context the ApplicationContext used for rendering the graphical interface.
   */
  private static void renderGame(ApplicationContext context) {
    context.renderFrame(graphics -> {
      graphics.setColor(Color.WHITE);
      graphics.fillRect(0, 0, 800, 600);

      graphics.setColor(Color.BLACK);
      graphics.drawString("Game Initial", 350, 100);
      
      graphics.setColor(Color.RED);
      graphics.fillRect(200, 200, 150, 50);
      graphics.setColor(Color.BLACK);
      graphics.drawString("Terminal", 230, 230);
           
      graphics.setColor(Color.BLUE);
      graphics.fillRect(450, 200, 150, 50);
      graphics.setColor(Color.BLACK);
      graphics.drawString("Basic graphic", 480, 230);
      
      graphics.setColor(Color.GREEN);
      graphics.fillRect(700, 200, 150, 50);
      graphics.setColor(Color.BLACK);
      graphics.drawString("Complete graphic", 730, 230);
    });
  }

  /**
   * Handles mouse click events on the game mode selection screen.
   * 
   * @param context the ApplicationContext used for rendering and handling events.
   * @param x the x-coordinate of the mouse click.
   * @param y the y-coordinate of the mouse click.
   * @return an integer representing the selected game mode:
   *         1 - Terminal version.
   *         2 - Basic graphical version.
   *         3 - Complete graphical version.
   *         0 - Invalid click.
   */
  private int handleMouseClick(ApplicationContext context, int x, int y) {
    if (x >= 200 && x <= 350 && y >= 200 && y <= 250) {
      return 1;
    } else if (x >= 450 && x <= 600 && y >= 200 && y <= 250) {
      return 2;
    } else if (x >= 700 && x <= 850 && y >= 200 && y <= 250) {
      return 3;
    } else {
      return 0;
    }
  }

  /**
   * Generates the initial set of tiles to populate the game. Each tile is associated with a specific
   * landscape type and a set of animals.
   */
  private void generateInitialTiles() {
    String[] landscapes = {"Forest", "Grassland", "Wetland", "Mountain", "River"};
    String[][] animals = {
      {"Bear", "Fox"},
      {"Deer", "Eagle"},
      {"Salmon", "Fox"},
      {"Bear", "Deer"},
      {"Salmon", "Eagle"}
    };
    int count = 0;
    for (int i = 0; i < landscapes.length; i++) {
      for (int j = 0; j < 17; j++) {
        Animal[] animalArray = new Animal[animals[i].length];
        for (int k = 0; k < animals[i].length; k++) {
          animalArray[k] = new Animal(animals[i][k]); 
        }
        gamedata.addTileToPool(new Tile(landscapes[i], animalArray, count));
        count++;
      }
    }
  }

  /**
   * Generates the initial pool of animal tokens. Each type of animal has 20 tokens.
   */
  private void generateAnimalPool() {
    String[] animals = {"Bear", "Fox", "Deer", "Eagle", "Salomn"};
    for (String animal : animals) {
      for (int i = 0; i < 20; i++) {
        gamedata.addAnimaltoPool(new Animal(animal));
      }
    }
  }
}

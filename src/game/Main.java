package game;

import gamegraphic.Gamecontroller;
import gamegraphic.Gameview;
import gamegraphic.GameData;
import gameterminal.GameControl;
import gamegraphic.Completeview;
import gamegraphic.Completecontroller;
import java.io.IOException;
/**
 * The main class that serves as the entry point for the game application.
 */
public class Main {
	
	/**
     * Constructs a new instance of {@code Main}.
     * This is a default constructor with no additional initialization.
     */
    public Main() {
        // No initialization required
    }
  /**
   * The main method that launches the game application.
   * It provides an initial option to choose between the graphical version and terminal version.
   * Depending on the user's choice, it either starts the graphical game view or the terminal-based game control.
   *
   * @param args Command line arguments (not used).
   * @throws IOException when invalid input occurs
   */
  public static void main(String[] args) throws IOException {
    GameData gamedata = new GameData();
    GameInitial initial = new GameInitial(gamedata);
    int choisir = initial.start();
    if (choisir == 2) {
      Gameview gameview = new Gameview(gamedata);
      Gamecontroller gamecontroller = new Gamecontroller(gamedata,gameview);
      gamecontroller.start();
    } else if(choisir == 1){
      GameControl game = new GameControl();
      game.startGame();
    }
    else if (choisir == 3){
      Completeview completeview = new Completeview(gamedata);
      Completecontroller completecontroller = new Completecontroller(gamedata,completeview);
      completecontroller.start();
    }
    System.out.println("Game over！");
  }
}

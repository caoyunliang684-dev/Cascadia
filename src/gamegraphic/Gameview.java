package gamegraphic;

import game.Animal;
import game.Tile;
import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.github.forax.zen.ApplicationContext;

/**
 * Represents the game view which is responsible for rendering the game and handling user interactions.
 */
public class Gameview {
  private static final int WINDOW_WIDTH = 1000;
  private static final int WINDOW_HEIGHT = 800;
  private static final int TILE_SIZE = 100;
  private static final int CELL_SIZE = 80;

  private final GameData gamedata;

  /**
   * Constructs a new Gameview with the specified game data.
   * 
   * @param gamedata The game data used to render the view.
   * @throws NullPointerException if gamedata is null.
   */
  public Gameview(GameData gamedata) {
    this.gamedata = Objects.requireNonNull(gamedata);
  }
  
  /**
   * Draws the player's area, including tiles and grids.
   * 
   * @param graphics The graphics context used to draw.
   */
  private void drawPlayer(java.awt.Graphics2D graphics) {
    drawPlayerplace(graphics);
    for (Map.Entry<Tile,Point> entry : gamedata.getPosition().entrySet()) {
      Tile tile = entry.getKey();
      Point point = entry.getValue();
      int tileX = 100 + point.x * CELL_SIZE;
      int tileY = 100 + point.y * CELL_SIZE;
      graphics.setColor(tile.color());
      graphics.fill(new Rectangle2D.Float(tileX, tileY, CELL_SIZE, CELL_SIZE));
      if (!gamedata.isTileplaced(tile)) {
        drawAnimalMarkersinTile(graphics, tile.animals(), tileX, tileY);
      }
      else {
        Animal[] animal = new Animal[1];
        animal[0] = gamedata.AnimalonTile(tile);
        drawAnimalMarkersinTile(graphics,animal,tileX,tileY);
      }
    }
  }
  
  /**
   * Draws the grid area where players can place their tiles.
   * 
   * @param graphics The graphics context used to draw.
   */
  private void drawPlayerplace(java.awt.Graphics2D graphics){
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 5; j++) {
        int x = 100 + i * CELL_SIZE;
        int y = 100 + j * CELL_SIZE;
        graphics.setColor(Color.BLACK);
        graphics.draw(new Rectangle2D.Float(x, y, 80, 80));
      }
    }
  }
  /**
   * Draws the initial tiles available in the pool.
   * 
   * @param graphics The graphics context used to draw.
   * @param tilePool The list of tiles to be drawn.
   */
  private void drawTiles(java.awt.Graphics2D graphics, List<Tile> tilePool) {
    int x = 50;
    int y = WINDOW_HEIGHT - 150;
    for (Tile tile : tilePool) {
      graphics.setColor(tile.color());
      graphics.draw(new Rectangle2D.Float(x, y, TILE_SIZE, TILE_SIZE));
      drawAnimalMarkersinTile(graphics, tile.animals(), x, y);
      x += TILE_SIZE + 20;
    }
  }
  
  /**
   * Draws animal tokens available in the pool.
   * 
   * @param graphics The graphics context used to draw.
   * @param animalpool The list of animals to be drawn.
   */
  private void drawAnimals(java.awt.Graphics2D graphics, List<Animal> animalpool) {
    int markerRadius = 20;
    int x = 70 ;
    int y = WINDOW_HEIGHT - 40;
    for (Animal animal  : animalpool){
      graphics.setColor(animal.color());
      graphics.fill(new Ellipse2D.Float(x,y,markerRadius,markerRadius));
      x += 120;
    }
  }


  /**
   * Draws the animal markers on the tile.
   * 
   * @param graphics The graphics context used to draw.
   * @param animals The array of animals to be drawn.
   * @param x The x-coordinate for the tile.
   * @param y The y-coordinate for the tile.
   */
  private void drawAnimalMarkersinTile(java.awt.Graphics2D graphics, Animal[] animals, int x, int y) {
    int markerRadius = 20;
    int offsetX = TILE_SIZE / 4;
    int offsetY = TILE_SIZE / 4;
    for (int i = 0; i < animals.length; i++) {
      graphics.setColor(animals[i].color());
      graphics.fill(new Ellipse2D.Float(
        x + offsetX + (i * (TILE_SIZE / 2 - markerRadius)),
        y + offsetY,
        markerRadius, markerRadius
      ));
    }
  }
   
  /**
   * Draws the current step information on the screen.
   * 
   * @param graphics The graphics context used to draw.
   */
  private void drawCurrentStep(java.awt.Graphics2D graphics) {
    String currentStep = gamedata.getCurrentStep();
    if (currentStep != null) {
      graphics.setColor(Color.BLACK);
      graphics.drawString(currentStep, 10, 10);
    }
  }

  /**
   * Draws the secondary step information on the screen.
   * 
   * @param graphics The graphics context used to draw.
   */
  private void drawCurrentStep2(java.awt.Graphics2D graphics) {
    String currentStep = gamedata.getCurrentStep2();
    if (currentStep != null) {
      graphics.setColor(Color.BLACK);
      graphics.drawString(currentStep, 400, 10);
    }
  }

  /**
   * Renders the entire game view, including background, tiles, player area, and step information.
   * 
   * @param context The application context used for rendering.
   */
  public void renderGame(ApplicationContext context) {
    context.renderFrame(graphics -> {
      graphics.setColor(Color.WHITE);
      graphics.fill(new Rectangle2D.Float(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT));
      if(gamedata.getisIntiail() < 2) {
        drawTiles(graphics, gamedata.getChooseTile());
      }
      else {
        drawTiles(graphics,gamedata.getChooseTile());
        drawAnimals(graphics,gamedata.getChooseAnimal());
      }
      drawPlayer(graphics);
      drawCurrentStep(graphics);
      drawCurrentStep2(graphics);
    });
  }
  

}

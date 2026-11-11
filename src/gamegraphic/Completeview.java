package gamegraphic;

import game.Tile;
import game.Animal;
import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.github.forax.zen.ApplicationContext;

/**
 * Represents the complete game view for rendering the game board and managing user interactions.
 */
public class Completeview {
  private static final int WINDOW_WIDTH = 1000;
  private static final int WINDOW_HEIGHT = 800;

  private final GameData gamedata;

  /**
   * Constructs a new Completeview with the specified game data.
   * 
   * @param gamedata The game data used to render the view.
   * @throws NullPointerException if gamedata is null.
   */
  public Completeview(GameData gamedata) {
    this.gamedata = Objects.requireNonNull(gamedata);
  }
  
  /**
   * Draws the player's area, including the placed tiles and their corresponding animal markers.
   * 
   * @param graphics The graphics context used to draw.
   */
  private void drawPlayer(java.awt.Graphics2D graphics) {
    drawPlayerplace(graphics);
    for (Map.Entry<Tile, Point> entry : gamedata.getPosition().entrySet()) {
      Tile tile = entry.getKey();
      Point gridPos = entry.getValue(); 
      int R = 40;                  
      int col = gridPos.x;
      int row = gridPos.y;
      int startX = 100;              
      int startY = 100;             
      double cx = startX + col * (Math.sqrt(3) * R);
      double cy = startY + row * (1.5 * R);
      if ((row % 2) == 1) {
        cx += (Math.sqrt(3) / 2) * R;
      }
      Polygon hex = gamedata.createHextop((int)cx, (int)cy, R);
      graphics.setColor(tile.color());
      graphics.fill(hex); 
      graphics.setColor(Color.BLACK);
      graphics.draw(hex);
      if (!gamedata.isTileplaced(tile)) {
        drawAnimalMarkersinTile(graphics, tile.animals(), (int)cx, (int)cy);
      } else {
        Animal placed = gamedata.AnimalonTile(tile);
        Animal[] oneAnimal = new Animal[]{ placed };
        drawAnimalMarkersinTile(graphics, oneAnimal, (int)cx, (int)cy);
      }
  }
}

  /**
   * Draws the hexagonal grid for player placement.
   * 
   * @param graphics The graphics context used to draw.
   */
  private void drawPlayerplace(java.awt.Graphics2D graphics) {
    int R = 40; 
    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        double cx = 100 + col * (Math.sqrt(3) * R);
        double cy = 100 + row * (1.5 * R);
          if ((row % 2) == 1) {
            cx += Math.sqrt(3)/2 * R;             
          }
          Polygon hex = gamedata.createHextop((int)cx, (int)cy, R);
          graphics.draw(hex);
        }
    }

}

  /**
   * Draws the hexagonal tiles available in the pool.
   * 
   * @param graphics The graphics context used to draw.
   * @param tilePool The list of tiles to be drawn.
   */
  private void drawTiles(java.awt.Graphics2D graphics, List<Tile> tilePool) {
    int R = 40; 
    int x = 100;
    int y = WINDOW_HEIGHT - 150;
    for (Tile tile : tilePool) {
      Polygon hex = gamedata.createHextop(x, y, R);
      graphics.setColor(tile.color());
      graphics.fill(hex); 
      graphics.setColor(Color.BLACK);
      graphics.draw(hex); 
      drawAnimalMarkersinTile(graphics, tile.animals(), x, y);
      x += 100;
    }
}

  /**
   * Draws the animal markers available in the pool.
   * 
   * @param graphics The graphics context used to draw.
   * @param animalpool The list of animal markers to be drawn.
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
   * Draws animal markers within a specific tile.
   * 
   * @param g The graphics context used to draw.
   * @param animals The array of animals to be drawn.
   * @param cx The x-coordinate of the tile's center.
   * @param cy The y-coordinate of the tile's center.
   */
  private void drawAnimalMarkersinTile(java.awt.Graphics2D g, Animal[] animals, int cx, int cy) {
    int markerRadius = 20;
    int spacing = markerRadius + 10; 
    int totalWidth = animals.length * spacing;
    int startX = cx - (totalWidth / 2) + (spacing / 2);
    int centerY = cy - markerRadius / 2;
    for (int i = 0; i < animals.length; i++) {
      int markerCenterX = startX + i * spacing;
      int drawX = markerCenterX - markerRadius / 2;
      int drawY = centerY;
      g.setColor(animals[i].color());
      g.fill(new java.awt.geom.Ellipse2D.Float(
          drawX, drawY, markerRadius, markerRadius
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
    Objects.requireNonNull(context);
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

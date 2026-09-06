package fr.uge.projet.zen;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.forax.zen.ApplicationContext;

import fr.uge.projet.utils.Bloc;
import fr.uge.projet.utils.Coordinate;
import fr.uge.projet.utils.FourPairs;
import fr.uge.projet.utils.PlayerInformation;
/**
 * Provides utility methods for rendering game components such as shapes, tiles, and UI elements.
 */
public class GameViewer {
  private static final int SQUARE_SIZE = 50;
  private static final int CIRCLE_RADIUS = 20;
  private static final int HEX_RADIUS = 30;
  private static final int MARGIN = 10;
  private static final int BUTTON_WIDTH = 100;
  private static final int BUTTON_HEIGHT = 40;
  
  /**
   * Initializes the game rendering by drawing the initial background and shapes.
   *
   * @param context  the application context used for rendering; must not be {@code null}.
   * @param width    the width of the rendering area.
   * @param height   the height of the rendering area.
   * @param squares  the list of squares to draw; must not be {@code null}.
   * @param circles  the list of circles to draw; must not be {@code null}.
   */
  public static void initialisationGame(ApplicationContext context,int width, int height,List<Square> squares, List<Circle> circles) {
    Color lightCyan = new Color(224, 255, 255);
    context.renderFrame(graphics -> {
      
      
      graphics.setColor(lightCyan);
      graphics.fill(new Rectangle2D.Float(0, 0, width, height));
      for (Square square : squares) {
        square.draw(graphics);
    }
      for (Circle circle : circles) {
        circle.draw(graphics);
    }
    });
  }

  /**
   * Continuously renders the game loop, updating the display with dynamic elements.
   *
   * @param context   the application context for rendering; must not be {@code null}.
   * @param width     the width of the rendering area.
   * @param height    the height of the rendering area.
   * @param shapes    the list of shapes to render; must not be {@code null}.
   * @param circles   the list of circles to render; must not be {@code null}.
   * @param index     the index used to retrieve game data.
   * @param data      the game data to use for rendering; must not be {@code null}.
   * @param shapeType the type of shape to render (e.g., SQUARE, HEXAGON); must not be {@code null}.
   * @param fieldName the name of the field to display; must not be {@code null}.
   */
  public static void loopGame(ApplicationContext context, int width, int height, List<Square> shapes, List<Circle> circles, int index, GameData data, ShowType shapeType,String fieldName) {
    Color lightCyan = new Color(224, 255, 255);
    context.renderFrame(graphics -> {
        graphics.setColor(lightCyan);
        graphics.fill(new Rectangle2D.Float(0, 0, width, height));

        //les tiles blanche
        var set = data.AllAttachedTiles(index, shapeType);
        for (var cor : set) {
            Shape shape;
            if (shapeType == ShowType.SQUARE) {
                shape = new Square(cor);
            } else if (shapeType == ShowType.HEXAGON) {
                shape = new Hexagon(cor);
            } else {
                throw new IllegalArgumentException("Unsupported ShapeType: " + shapeType);
            }
            shape.draw(graphics);
        }

        //les deux a droit
        for (Shape shape : shapes) {
            shape.draw(graphics);
        }

      
        for (Circle circle : circles) {
            circle.draw(graphics);
        }
        drawFieldName(graphics, fieldName, width);
        //tous les donnes
        displayZen(graphics, data.displayZen(index), shapeType);
        drawRefreshButton(graphics, width, height);
        drawReleaseButton(graphics, width, height);
    });
}

  /**
   * Dynamically displays the current state of the game using the provided shapes and data.
   *
   * @param g         the graphics object to use for drawing; must not be {@code null}.
   * @param map       the map of coordinates to blocks to display; must not be {@code null}.
   * @param shapeType the type of shapes to use for rendering (e.g., SQUARE, HEXAGON); must not be {@code null}.
   */
  public static void displayZen(Graphics2D g, Map<Coordinate, Bloc> map, ShowType shapeType) {
    for (var cordonnate : map.keySet()) {
        Bloc temperateBloc = map.get(cordonnate);

        
        Shape shape;
        if (shapeType == ShowType.SQUARE) {
            shape = new Square(cordonnate, temperateBloc.getTile());
        } else if (shapeType == ShowType.HEXAGON) {
            shape = new Hexagon(cordonnate, temperateBloc.getTile());
        } else {
            throw new IllegalArgumentException("Unsupported ShapeType: " + shapeType);
        }

        shape.draw(g);

      
        if (temperateBloc.isExistAnimal()) {
            Circle circle = new Circle(cordonnate, temperateBloc.getAnimal());
            if(shapeType == ShowType.SQUARE) {
              circle.draw(g);
            }else {
            drawCircle(g, circle);
            }
        }
    }
}

  /**
   * Draws a square on the screen.
   *
   * @param g      the graphics object to use for drawing; must not be {@code null}.
   * @param square the square to draw; must not be {@code null}.
   */
  public static void drawSquare(Graphics2D g, Square square) {
    int centerX = square.cordonnate().x(); 
    int centerY = square.cordonnate().y(); 
    int size = 50; 
    int halfSize = size / 2; 


    g.setColor(square.tile() != null ? square.tile().tileColor() : Color.BLACK);
    g.fill(new Rectangle2D.Double(centerX - halfSize, centerY - halfSize, size, size));
}

  /**
   * Draws a circle representing an animal at the specified location.
   *
   * @param g      the graphics object to use for drawing; must not be {@code null}.
   * @param circle the circle to draw; must not be {@code null}.
   */
  public static void drawCircle(Graphics2D g, Circle circle) {
    int centerX = circle.cordonnate().x(); 
    int centerY = circle.cordonnate().y();
    int radius = CIRCLE_RADIUS; 

  
    g.setColor(circle.animal().animalColor());
    g.fill(new Ellipse2D.Double(centerX - radius, centerY - radius, radius * 2, radius * 2));

  
    g.setColor(Color.BLACK);
    g.drawString(
        String.valueOf(circle.animal().animalType()), 
        centerX - 5,  
        centerY + 5   
    );
}
  /**
   * Draws a hexagon tile at the specified location.
   *
   * @param g       the graphics object to use for drawing; must not be {@code null}.
   * @param hexagon the hexagon to draw; must not be {@code null}.
   */
  public static void drawHexagon(Graphics2D g, Hexagon hexagon) {
    int cx = hexagon.cordonnate().x();
    int cy = hexagon.cordonnate().y();
    Polygon hex = createHexagon(cx, cy);
    g.setColor(hexagon.tile() != null ? hexagon.tile().tileColor() : Color.WHITE);
    g.fill(hex);
    g.setColor(Color.BLACK);
    g.draw(hex);
  }
  

  private static Polygon createHexagon(int x, int y) {
    int[] xPoints = new int[6];
    int[] yPoints = new int[6];
    for (int i = 0; i < 6; i++) {
      double angle = Math.toRadians(60 * i);
      xPoints[i] = (int) (x + HEX_RADIUS * Math.cos(angle));
      yPoints[i] = (int) (y + HEX_RADIUS * Math.sin(angle));
    }
    return new Polygon(xPoints, yPoints, 6);
  }
  /**
   * Draws the "Release" button on the screen.
   *
   * @param graphics the graphics object to use for drawing; must not be {@code null}.
   * @param width    the width of the rendering area.
   * @param height   the height of the rendering area.
   */
  public static void drawReleaseButton(Graphics2D graphics, int width, int height) {
    int buttonX = width - 120; 
    int buttonY = 70;         
    int buttonWidth = 100;
    int buttonHeight = 40;


    graphics.setColor(Color.LIGHT_GRAY);
    graphics.fillRect(buttonX, buttonY, buttonWidth, buttonHeight);

  
    graphics.setColor(Color.BLACK);
    graphics.drawRect(buttonX, buttonY, buttonWidth, buttonHeight);

   
    graphics.drawString("Release", buttonX + 20, buttonY + 25);
}
  /**
   * Draws the "Refresh" button on the screen.
   *
   * @param graphics the graphics object to use for drawing; must not be {@code null}.
   * @param width    the width of the rendering area.
   * @param height   the height of the rendering area.
   */
  private static void drawRefreshButton(Graphics2D graphics, int width, int height) {
    int buttonX = width - BUTTON_WIDTH - 20; 
    int buttonY = 20; 


    graphics.setColor(Color.LIGHT_GRAY);
    graphics.fillRect(buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT);


    graphics.setColor(Color.BLACK);
    graphics.drawRect(buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT);


    graphics.drawString("Refresh", buttonX + 20, buttonY + 25);
}
  /**
   * Validates that a value is within a specified range.
   *
   * @param min   the minimum allowed value.
   * @param value the value to check.
   * @param max   the maximum allowed value.
   * @throws IllegalArgumentException if {@code value} is out of range.
   */
  public static void checkRange(double min, double value, double max) {
    if (value < min || value > max) {
      throw new IllegalArgumentException("Invalid coordinate: " + value);
    }
  }

  /**
   * Displays the field name and rules on the screen.
   *
   * @param graphics  the graphics object to use for drawing; must not be {@code null}.
   * @param fieldName the name of the field to display; must not be {@code null}.
   * @param width     the width of the rendering area.
   */

  public static void drawFieldName(Graphics2D graphics, String fieldName, int width) {
    int fieldX = 20; // 字段的 X 坐标
    int fieldY = 20; // 字段的 Y 坐标
    
    graphics.setFont(graphics.getFont().deriveFont(25f));
    graphics.setColor(Color.BLACK);
    graphics.drawString("Field: " + fieldName, 400, fieldY+160);
    
    String rule = "A Square is a jeton nature and A Circle is a jeton animal";
    String a = "Blue Square is Forest, it likes B(Bear) and E(Elk)";
       String b = "RedBrown Square is MOUNTAIN, it likes B(BEAR) and H(HAWK)";
       String c = "Yellow Square is PRAIRIE, it likes F(FOX) and H(HAWK)";
       String d =    " Green Square is RIver, it likes S(Salmon) and E(ELK)";
       String f = "Pink Square is Swamp, it likes F(Fox) and E(ELK)";
       graphics.setFont(graphics.getFont().deriveFont(18f));
    graphics.drawString(rule, fieldX, fieldY + 20);
    graphics.setColor(new Color(146, 168, 209));
    graphics.drawString(a, fieldX, fieldY + 40);
    graphics.setColor(new Color(165, 42, 42));
    graphics.drawString(b, fieldX, fieldY + 60);
    graphics.setColor(new Color(245, 225, 164));
    graphics.drawString(c, fieldX, fieldY + 80);
    graphics.setColor(new Color(152, 221, 222));
    graphics.drawString(d, fieldX, fieldY + 100);
    graphics.setColor( Color.PINK);
    graphics.drawString(f, fieldX, fieldY + 120);
    
}
}

package fr.uge.projet.utils;

import java.util.List;
import java.util.Objects;
/**
 * Represents a coordinate with x and y values and provides utility methods for coordinate manipulation.
 *
 * @param x the x-coordinate.
 * @param y the y-coordinate.
 */
public record Coordinate(int x, int y) {
  
  /**
   * Retrieves the four cardinal neighbors of this coordinate.
   *
   * @return a {@link List} of the four coordinates surrounding this coordinate.
   */
  public List<Coordinate> fourSidesCordonnates(){
    return List.of(new Coordinate(x+1, y),new Coordinate(x-1, y),new Coordinate(x, y+1),new Coordinate(x, y-1));
  }
  /**
   * Retrieves the four cardinal neighbors of this coordinate with a graphical offset of 50 units.
   *
   * @return a {@link List} of the four coordinates with a graphical offset.
   */
  public List<Coordinate> fourSidesCordonnatesGraphicSquare(){
    return List.of(new Coordinate(x+50, y),new Coordinate(x-50, y),new Coordinate(x, y+50),new Coordinate(x, y-50));
  }
  /**
   * Retrieves the vertical neighbors of this coordinate.
   *
   * @return a {@link List} of the two vertical neighboring coordinates.
   */
  public List<Coordinate> verticalCordonnates(){
    return List.of(new Coordinate(x+1, y),new Coordinate(x-1, y));
  }
  /**
   * Retrieves the horizontal neighbors of this coordinate.
   *
   * @return a {@link List} of the two horizontal neighboring coordinates.
   */
  public List<Coordinate> horizontalCordonnaltes(){
    return List.of(new Coordinate(x, y+1),new Coordinate(x, y-1));
  }
  /**
   * Adds this coordinate to another coordinate.
   *
   * @param other the {@link Coordinate} to add to this coordinate; must not be {@code null}.
   * @return a new {@link Coordinate} that is the result of the addition.
   * @throws NullPointerException if {@code other} is {@code null}.
   */
//Méthode add pour additionner deux coordonnées
  public Coordinate add(Coordinate other) {
  Objects.requireNonNull(other);
    return new Coordinate(this.x + other.x, this.y + other.y);
  }
}

// Shape.java
package fr.uge.projet.zen;

import java.awt.Graphics2D;
import fr.uge.projet.utils.Coordinate;

/**
 * An interface for all shapes (e.g., Circle, Square, Hexagon).
 */
public interface Shape {
    /**
     * Gets the coordinates of the shape.
     * @return the center coordinate of the shape
     */
    Coordinate cordonnate();

    /**
     * Draws the shape on the provided graphics context.
     * @param g the graphics context
     */
    void draw(Graphics2D g);

    /**
     * Checks whether a given point is inside the shape.
     * @param point the point to check
     * @return true if the point is inside the shape, false otherwise
     */
    boolean contains(Coordinate point);
}
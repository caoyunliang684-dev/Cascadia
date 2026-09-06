package fr.uge.projet.zen;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import fr.uge.projet.animal.Animal;
import fr.uge.projet.utils.Coordinate;

/**
 * Represents a circle with coordinates, color, label, and visibility state.
 */
public record Circle(Coordinate cordonnate, Animal animal) implements Shape {
    private static final int CIRCLE_RADIUS = 25; // Radius of the circle

    /**
     * Checks if a given point (clickX, clickY) is inside the circle.
     * @param cordonnate the point to check
     * @return true if the point is inside the circle, false otherwise
     */
    @Override
    public boolean contains(Coordinate cordonnate) {
        int clickX = cordonnate.x();
        int clickY = cordonnate.y();
        int dx = clickX - (this.cordonnate.x() + CIRCLE_RADIUS);
        int dy = clickY - (this.cordonnate.y() + CIRCLE_RADIUS);
        return dx * dx + dy * dy <= CIRCLE_RADIUS * CIRCLE_RADIUS;
    }

    /**
     * Draws the circle and its label if visibility is enabled.
     * @param g the graphics context
     */
    @Override
    public void draw(Graphics2D g) {
        // Draw the filled circle
        g.setColor(animal.animalColor());
        g.fill(new Ellipse2D.Double(cordonnate.x(), cordonnate.y(), CIRCLE_RADIUS * 2, CIRCLE_RADIUS * 2));

        // Draw the label at the center of the circle
        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(animal.animalType()), cordonnate.x() + CIRCLE_RADIUS - 5, cordonnate.y() + CIRCLE_RADIUS + 5);
    }
}

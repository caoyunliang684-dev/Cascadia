package fr.uge.projet.zen;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import fr.uge.projet.animal.Animal;
import fr.uge.projet.tile.Tile;
import fr.uge.projet.utils.Coordinate;



/**
 * Represents a square with coordinates, a tile, and visibility state.
 */
public record Square(Coordinate cordonnate, Tile tile) implements Shape {
    private static final int SQUARE_SIZE = 50;

    /**
     * Constructor accepting only Cordonnate, initializing Tile to null.
     * @param cordonnate the coordinates of the square
     */
    public Square(Coordinate cordonnate) {
        this(cordonnate, null);
    }

    /**
     * Checks if a given point (clickX, clickY) is inside the square.
     * @param cordonnate the point to check
     * @return true if the point is inside the square, false otherwise
     */
    @Override
    public boolean contains(Coordinate cordonnate) {
        int cx = cordonnate.x(); 
        int cy = cordonnate.y(); 

        return cx >= this.cordonnate.x() && cx <= this.cordonnate.x() + SQUARE_SIZE &&
               cy >= this.cordonnate.y() && cy <= this.cordonnate.y() + SQUARE_SIZE;
    }

    /**
     * Draws the square, using white color if Tile is null.
     * @param g the graphics context
     */
    @Override
    public void draw(Graphics2D g) {
        g.setColor(tile != null ? tile.tileColor() : Color.WHITE); 
        g.fill(new Rectangle2D.Double(cordonnate.x(), cordonnate.y(), SQUARE_SIZE, SQUARE_SIZE));
    }
}
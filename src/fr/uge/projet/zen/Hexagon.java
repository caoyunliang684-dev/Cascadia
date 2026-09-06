
//Hexagon.java
package fr.uge.projet.zen;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.List;

import fr.uge.projet.tile.Tile;
import fr.uge.projet.utils.Coordinate;


/**
 * Represents a hexagon with coordinates, a tile, and visibility state.
 */
public record Hexagon(Coordinate cordonnate, Tile tile) implements Shape {
    private static final int HEX_RADIUS = 30; // Radius of the hexagon

    /**
     * Constructor accepting only Cordonnate, initializing Tile to null.
     * @param cordonnate the coordinates of the hexagon
     */
    public Hexagon(Coordinate cordonnate) {
        this(cordonnate, null);
    }

    /**
     * Checks if a given point is inside the hexagon.
     * @param point the point to check
     * @return true if the point is inside the hexagon, false otherwise
     */
    @Override
    public boolean contains(Coordinate point) {
        int cx = cordonnate.x();
        int cy = cordonnate.y();
        Polygon hex = createHexagon(cx, cy);
        return hex.contains(point.x(), point.y());
    }

    /**
     * Draws the hexagon using the tile's color or white if the tile is null.
     * @param g the graphics context
     */
    @Override
    public void draw(Graphics2D g) {
        int cx = cordonnate.x();
        int cy = cordonnate.y();
        Polygon hex = createHexagon(cx, cy);
        g.setColor(tile != null ? tile.tileColor() : Color.WHITE);
        g.fill(hex);
        g.setColor(Color.BLACK);
        g.draw(hex);
    }

    /**
     * Creates the shape of the hexagon based on its center coordinates.
     * @param x the x-coordinate of the hexagon center
     * @param y the y-coordinate of the hexagon center
     * @return a Polygon representing the hexagon
     */
    private Polygon createHexagon(int x, int y) {
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
     * Retourne les coordonnées des six côtés de l'hexagone autour d'une Cordonnate donnée.
     * Utilise le rayon défini dans la classe.
     * @param cordonnate les coordonnées centrales de l'hexagone
     * @return une liste de Cordonnate représentant les six côtés
     */
    public static List<Coordinate> sixSidesCordonnates(Coordinate cordonnate) {
        int dx = (int) (1.5 * HEX_RADIUS); // Décalage horizontal
        int dy = (int) (Math.sqrt(3) * HEX_RADIUS / 2); // Décalage vertical

        return List.of(
            new Coordinate(cordonnate.x() + dx, cordonnate.y() - dy),  // Haut-droit
            new Coordinate(cordonnate.x() + dx, cordonnate.y() + dy),  // Bas-droit
            new Coordinate(cordonnate.x(), cordonnate.y() + 2 * dy),   // Directement en bas
            new Coordinate(cordonnate.x() - dx, cordonnate.y() + dy),  // Bas-gauche
            new Coordinate(cordonnate.x() - dx, cordonnate.y() - dy),  // Haut-gauche
            new Coordinate(cordonnate.x(), cordonnate.y() - 2 * dy)    // Directement en haut
        );
    }

}
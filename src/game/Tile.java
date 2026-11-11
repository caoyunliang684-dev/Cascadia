package game;

import java.awt.Color;
import java.util.Objects;


/**
 * Represents a tile in the game with a specific landscape, possible animals, and other properties.
 * 
 * @param landscape the landscape of the tile
 * @param animals the animals available on the tile
 * @param id the id of the tile
 * @param color the color of the tile
 */
public record Tile(String landscape, Animal[] animals, int id,Color color) {

    /**
     * Constructs a new Tile with the given landscape and possible animals.
     * 
     * @param landscape The type of landscape of the tile.
     * @param animals The types of animals that can be placed on the tile.
     * @param id each tile has only one id to identify
     */
  public Tile(String landscape, Animal[] animals, int id) {
    this(Objects.requireNonNull(landscape, "Landscape cannot be null"),
         Objects.requireNonNull(animals, "Animals cannot be null"),
         id,
         assignLandscapeColor(landscape));
}

    /**
     * Assigns a color to the landscape type.
     * 
     * @param landscape The landscape type.
     * @return The color representing the landscape.
     */
    private static Color assignLandscapeColor(String landscape) {
        return switch (landscape) {
            case "Forest" -> new Color(34, 139, 34);     
            case "Grassland" -> new Color(124, 252, 0);    
            case "Wetland" -> new Color(30, 144, 255);     
            case "Mountain" -> new Color(169, 169, 169);  
            case "River" -> new Color(70, 130, 180);    
            default -> Color.GRAY;                    
        };
    }
    
}

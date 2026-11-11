package game;

import java.awt.Color;
import java.util.Objects;

/**
 * Represents an animal in the game with a specific name and color.
 * 
 * @param name The name of the animal
 * @param color The color of the animal
 */
public record Animal(String name, Color color) {

    /**
     * Constructs a new Animal with the given name.
     * Assigns a color to the animal based on its name.
     * 
     * @param name The name of the animal.
     */
  public Animal(String name) {
    this(Objects.requireNonNull(name, "Animal name cannot be null"), assignAnimalColor(name));
  }



    /**
     * Assigns a color to the animal based on its name.
     * 
     * @param name The name of the animal.
     * @return The color representing the animal.
     */
    private static Color assignAnimalColor(String name) {
        return switch (name) {
            case "Bear" -> new Color(139, 69, 19);  
            case "Fox" -> Color.ORANGE;          
            case "Deer" -> Color.RED;           
            case "Eagle" -> Color.BLACK;           
            case "Salmon" -> Color.PINK;            
            default -> Color.GRAY;                
        };
    }
}

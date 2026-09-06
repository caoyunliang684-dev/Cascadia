package fr.uge.projet.tile;

import java.awt.Color;
import java.util.List;

import fr.uge.projet.animal.Animal;

//public interface Tile {
//  
//  //premier etape c'est seulement deux
//  List<Animal> allowedAnimals();
//  
//  //juste pour terminal
//  String allowedAnimalsInChar();
//  char tileType();
//  
//  Color tileColor();
//  
//}


public enum Tile {
    FOREST('F', new Color(146, 168, 209), List.of(Animal.BEAR, Animal.ELK)),
    MOUNTAIN('M', new Color(165, 42, 42), List.of(Animal.BEAR, Animal.HAWK)),
    PRAIRIE('P', new Color(245, 225, 164), List.of(Animal.FOX, Animal.HAWK)),
    RIVER('R', new Color(152, 221, 222), List.of(Animal.SALMON, Animal.ELK)),
    SWAMP('S', Color.PINK, List.of(Animal.FOX, Animal.ELK));

    private final char type;
    private final Color color;
    private final List<Animal> allowedAnimals;

    Tile(char type, Color color, List<Animal> allowedAnimals) {
        this.type = type;
        this.color = color;
        this.allowedAnimals = allowedAnimals;
    }

    public char tileType() {
        return type;
    }

    public Color tileColor() {
        return color;
    }

    public List<Animal> allowedAnimals() {
        return allowedAnimals;
    }

    public String allowedAnimalsInChar() {
        StringBuilder sb = new StringBuilder();
        for (Animal animal : allowedAnimals) {
            sb.append(animal.animalType());
        }
        return sb.toString();
    }
}

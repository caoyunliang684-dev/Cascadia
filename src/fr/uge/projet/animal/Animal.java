package fr.uge.projet.animal;

import java.awt.Color;

public enum Animal {
    BEAR('B', new Color(255, 111, 97)),      // Red
    ELK('E', new Color(107, 91, 149)),       // Purple
    FOX('F', new Color(255, 180, 0)),        // Yellow
    HAWK('H', new Color(136, 176, 75)),      // Green
    SALMON('S', new Color(247, 202, 201));   // Salmon pink

    private final char type;
    private final Color color;

    Animal(char type, Color color) {
        this.type = type;
        this.color = color;
    }

    public char animalType() {
        return type;
    }

    public Color animalColor() {
        return color;
    }
}


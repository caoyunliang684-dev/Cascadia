package fr.uge.projet.score;

import fr.uge.projet.utils.PlayerInformation;
import fr.uge.projet.zen.Hexagon;
import fr.uge.projet.zen.ShowType;
import fr.uge.projet.utils.Coordinate;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import fr.uge.projet.animal.Animal;


public class IntermediateVariantScorer {
	private final ShowType showType;
	/**
   * Constructs a scorer for the Intermediate Variant with the specified shape type.
   *
   * @param showType the shape type used in the game (e.g., SQUARE, HEXAGON); must not be {@code null}.
   * @throws NullPointerException if {@code showType} is {@code null}.
   */
	public IntermediateVariantScorer(ShowType showType) {
	  this.showType = Objects.requireNonNull(showType);
	}
	/**
   * Calculates the total score for a player's placement based on the Intermediate Variant rules.
   *
   * @param placement the player's current placement information, including the animal map; must not be {@code null}.
   * @return the total score for the placement.
   * @throws NullPointerException if {@code placement} is {@code null}.
   */
    public int calculateScore(PlayerInformation placement) {
        Map<Coordinate, Animal> animalMap = placement.getAnimalMap();
        int totalScore = 0;

        // Regrouper les animaux par espèces
        for (var animal : Animal.values()) {
            Set<Set<Coordinate>> groups = findGroups(animalMap, animal);
            for (Set<Coordinate> group : groups) {
                int size = group.size();
                if (size == 2) {
                    totalScore += 5; // 5 points pour deux animaux
                } else if (size == 3) {
                    totalScore += 8; // 8 points pour trois animaux
                } else if (size >= 4) {
                    totalScore += 12; // 12 points pour quatre animaux ou plus
                }
            }
        }

        return totalScore;
    }
    /**
     * Finds all groups of connected animals of the specified species in the given map.
     *
     * @param animalMap   a map of coordinates to animals representing the game board; must not be {@code null}.
     * @param targetAnimal the animal species to group; must not be {@code null}.
     * @return a {@link Set} of {@link Set}s, where each inner set represents a group of connected animals.
     */
    private Set<Set<Coordinate>> findGroups(Map<Coordinate, Animal> animalMap, Animal targetAnimal) {
        Set<Set<Coordinate>> groups = new HashSet<>();
        Set<Coordinate> visited = new HashSet<>();

        for (Coordinate coord : animalMap.keySet()) {
            if (animalMap.get(coord) == targetAnimal && !visited.contains(coord)) {
                Set<Coordinate> group = new HashSet<>();
                exploreConnectedAnimals(coord, targetAnimal, animalMap, group);
                groups.add(group);
                visited.addAll(group);
            }
        }

        return groups;
    }
    /**
     * Recursively explores and collects all connected animals of the same species starting from a given coordinate.
     *
     * @param coord      the starting coordinate; must not be {@code null}.
     * @param targetAnimal the animal species to group; must not be {@code null}.
     * @param animalMap   a map of coordinates to animals representing the game board; must not be {@code null}.
     * @param group       a set to store the coordinates of connected animals; must not be {@code null}.
     */
    private void exploreConnectedAnimals(Coordinate coord, Animal targetAnimal, Map<Coordinate, Animal> animalMap, Set<Coordinate> group) {
        if (!animalMap.containsKey(coord) || group.contains(coord) || animalMap.get(coord) != targetAnimal) {
            return;
        }

        group.add(coord);
        var neighbors = showType.equals(ShowType.HEXAGON) ? Hexagon.sixSidesCordonnates(coord) : coord.fourSidesCordonnates();
        for (Coordinate neighbor : neighbors) {
            exploreConnectedAnimals(neighbor, targetAnimal, animalMap, group);
        }
    }
}



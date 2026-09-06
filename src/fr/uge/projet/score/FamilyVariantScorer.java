package fr.uge.projet.score;

import fr.uge.projet.utils.PlayerInformation;

import fr.uge.projet.utils.Coordinate;
import fr.uge.projet.animal.Animal;

import java.util.*;
/**
 * Handles scoring for the "Family Variant" in the game.
 *
 * <p>Scoring in the Family Variant is based on grouping animals by species:
 * <ul>
 *   <li>1 point for a single animal of a species.</li>
 *   <li>5 points for a group of two animals of the same species.</li>
 *   <li>9 points for a group of three or more animals of the same species.</li>
 * </ul>
 */
public class FamilyVariantScorer {
  /**
   * Calculates the total score for a player's placement based on the Family Variant rules.
   *
   * @param placement the player's current placement information, including the animal map; must not be {@code null}.
   * @return the total score for the placement.
   * @throws NullPointerException if {@code placement} is {@code null}.
   */
    public int calculateScore(PlayerInformation placement) {
        Map<Coordinate, Animal> animalMap = placement.getAnimalMap();
        int totalScore = 0;

        // Regrouper les animaux par espèces
        for (Animal animal : Animal.values()) {
            Set<Set<Coordinate>> groups = ScoreManager.findGroups(animalMap, animal);
            for (Set<Coordinate> group : groups) {
                int size = group.size();
                if (size == 1) {
                    totalScore += 1; // 1 point pour un seul animal
                } else if (size == 2) {
                    totalScore += 5; // 5 points pour deux animaux
                } else if (size >= 3) {
                    totalScore += 9; // 9 points pour trois animaux ou plus
                }
            }
        }

        return totalScore;
    }


}


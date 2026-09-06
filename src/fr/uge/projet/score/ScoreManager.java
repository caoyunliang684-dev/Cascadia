package fr.uge.projet.score;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import fr.uge.projet.animal.Animal;
import fr.uge.projet.utils.Coordinate;
import fr.uge.projet.utils.PlayerInformation;
import fr.uge.projet.zen.ShowType;

public class ScoreManager {
  private final String configuration;
  private final ShowType showType;
  
  // surcharge de constructeur 
  /**
   * Constructs a new {@code ScoreManager} with the specified shape type and configuration.
   *
   * @param showType      the shape type used in the game (e.g., SQUARE, HEXAGON); must not be {@code null}.
   * @param configuration the scoring configuration; must not be {@code null}.
   * @throws NullPointerException if {@code showType} or {@code configuration} is {@code null}.
   */
  public ScoreManager(ShowType showType, String configuration) {
	this.showType = Objects.requireNonNull(showType);
    this.configuration = Objects.requireNonNull(configuration);
  }
  

  /**
   * Calculates the total score for a player's placement, including animal and tile scores.
   *
   * <p>The animal score is calculated based on the specified variant type, and the tile score
   * is calculated using {@link AccountTileScore}.
   *
   * @param type      the scoring variant type (e.g., FAMILY, INTERMEDIATE, CARD); must not be {@code null}.
   * @param placement the player's current placement information, including the animal map; must not be {@code null}.
   * @return the total score for the player's placement.
   * @throws NullPointerException     if {@code type} or {@code placement} is {@code null}.
   * @throws IllegalArgumentException if {@code type} is an unexpected value.
   */
  public int calculateTotalScore(VariantType type, PlayerInformation placement) {
	Objects.requireNonNull(placement);
    Objects.requireNonNull(type);
	var scoreAnimal = switch(type) {
	    case FAMILY -> new FamilyVariantScorer().calculateScore(placement);
	    case INTERMEDIATE -> new IntermediateVariantScorer(showType).calculateScore(placement);
	    case CARD -> new CardVariantScorer(configuration,showType).calculateTotalScore(placement);
	    default -> throw new IllegalArgumentException("Unexpected value: " + type);
	  };
	return scoreAnimal + new AccountTileScore().calculateTileScore(placement);
  }
  //Identifier tous les animaux connectés d'une même espèce, à partir d'une coordonnée donnée.
  private static void exploreConnectedAnimals(Coordinate coord, Animal targetAnimal, Map<Coordinate, Animal> animalMap, Set<Coordinate> group) {
      if (!animalMap.containsKey(coord) || group.contains(coord) || animalMap.get(coord) != targetAnimal) {
          return;
      }

      group.add(coord);
      for (Coordinate neighbor : coord.fourSidesCordonnates()) {
          exploreConnectedAnimals(neighbor, targetAnimal, animalMap, group);
      }
  }
  
  // find a group for score .
  /**
   * Identifies all groups of connected animals of the specified species on the game board.
   *
   * @param animalMap   a map of coordinates to animals representing the game board; must not be {@code null}.
   * @param targetAnimal the animal species to group; must not be {@code null}.
   * @return a {@link Set} of {@link Set}s, where each inner set represents a group of connected animals.
   * @throws NullPointerException if {@code animalMap} or {@code targetAnimal} is {@code null}.
   */
  public static Set<Set<Coordinate>> findGroups(Map<Coordinate, Animal> animalMap, Animal targetAnimal) {
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
  
}

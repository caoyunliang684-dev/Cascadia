package bearscore;

import game.Animal;
import game.Player;
import game.Tile;
import java.awt.Point;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import score.ScoreCalculate;

/**
 * BearA implements the scoring rule where pairs of adjacent bears score points.
 * A valid pair of bears must not be adjacent to any other bear.
 */
public class BearA implements ScoreCalculate {
	
	 /**
     * Constructs a new instance of {@code BearA}.
     * This is a default constructor with no additional initialization.
     */
    public BearA() {
        // No initialization required
    }
    
    /**
     * Calculates the player's score based on valid pairs of adjacent bears.
     * Each valid pair scores 4 points.
     *
     * @param player the player whose score is being calculated.
     * @return the total score.
     */
    @Override
    public int calculateScore(Player player) {
        Objects.requireNonNull(player);
        Map<Tile, Animal> animalSet = player.getAnimalset();
        Set<Tile> visitedTiles = new HashSet<>();
        int totalScore = 0;

        for (Tile tile : animalSet.keySet()) {
            Animal animal = animalSet.get(tile);

            // Check if the tile has a bear and hasn't been visited
            if (animal != null && animal.name().equals("Bear") && !visitedTiles.contains(tile)) {
                
                // Get adjacent bears
                Set<Tile> adjacentBears = getAdjacentAnimalTiles(player, tile, "Bear");

                // If exactly one adjacent bear is found, check for valid pairing
                if (adjacentBears.size() == 1) {
                    Tile partnerTile = null;

                    // Retrieve the only adjacent bear tile
                    for (Tile adjacentTile : adjacentBears) {
                        partnerTile = adjacentTile;
                    }

                    // Check if the pair is not adjacent to any other bear
                    if (getAdjacentAnimalTiles(player, partnerTile, "Bear").size() == 1) {
                        totalScore += 4;  // Valid pair, award 4 points
                        visitedTiles.add(tile);
                        visitedTiles.add(partnerTile);
                    }
                } else {
                    // If more than one adjacent bear, mark as visited but no points
                    visitedTiles.add(tile);
                }
            }
        }

        return totalScore;
    }

    /**
     * Retrieves adjacent tiles containing the specified animal.
     *
     * @param player     the player whose board is being checked.
     * @param tile       the tile to check around.
     * @param animalName the name of the animal to match.
     * @return a set of adjacent tiles with the specified animal.
     */
    private Set<Tile> getAdjacentAnimalTiles(Player player, Tile tile, String animalName) {
        Set<Tile> adjacentTiles = new HashSet<>();
        Map<Tile, Point> positions = player.getPosition();
        Point currentPos = positions.get(tile);

        for (Map.Entry<Tile, Point> entry : positions.entrySet()) {
            Tile adjacentTile = entry.getKey();
            Point pos = entry.getValue();

            // Check adjacency (up, down, left, right)
            if ((Math.abs(pos.x - currentPos.x) == 1 && pos.y == currentPos.y) ||
                (Math.abs(pos.y - currentPos.y) == 1 && pos.x == currentPos.x)) {

                Animal adjacentAnimal = player.getAnimalset().get(adjacentTile);
                if (adjacentAnimal != null && adjacentAnimal.name().equals(animalName)) {
                    adjacentTiles.add(adjacentTile);
                }
            }
        }

        return adjacentTiles;
    }
}

package foxscore;

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
 * FoxA implements the scoring rule for foxes.
 * A fox earns points based on the number of unique adjacent animal types.
 */
public class FoxA implements ScoreCalculate {
	
	/**
     * Constructs a new instance of {@code FoxA}.
     * This is a default constructor with no additional initialization.
     */
    public FoxA() {
        // No initialization required
    }
    /**
     * Calculates the total score for the player based on fox tiles.
     * Each fox scores points for each unique adjacent animal type.
     *
     * @param player the player whose score is being calculated.
     * @return the total score.
     */
    @Override
    public int calculateScore(Player player) {
        Objects.requireNonNull(player);
        Map<Tile, Animal> animalSet = player.getAnimalset();
        int totalScore = 0;

        // Iterate through all tiles to find foxes
        for (Tile tile : animalSet.keySet()) {
            Animal animal = animalSet.get(tile);

            // If the tile has a fox
            if (animal != null && animal.name().equals("Fox")) {
                // Calculate the score for this fox
                int foxScore = calculateFoxScore(player, tile, animalSet);
                totalScore += foxScore;
            }
        }

        return totalScore;
    }

    /**
     * Calculates the score for a fox based on unique adjacent animal types.
     *
     * @param player    the current player.
     * @param foxTile   the tile with the fox.
     * @param animalSet the player's animal set.
     * @return the score for the fox.
     */
    private int calculateFoxScore(Player player, Tile foxTile, Map<Tile, Animal> animalSet) {
        Set<String> uniqueAnimalTypes = new HashSet<>();  // Store unique animal types

        Point foxPos = player.getPosition().get(foxTile);

        // Four directions: up, down, left, right
        int[][] directions = {
            {0, -1},  // Up
            {0, 1},   // Down
            {-1, 0},  // Left
            {1, 0}    // Right
        };

        // Check adjacent tiles
        for (int[] dir : directions) {
            int adjacentX = foxPos.x + dir[0];
            int adjacentY = foxPos.y + dir[1];

            // Get adjacent tile
            Tile adjacentTile = findTileByPosition(player, adjacentX, adjacentY);

            if (adjacentTile != null) {
                Animal adjacentAnimal = animalSet.get(adjacentTile);

                // Add unique adjacent animal type
                if (adjacentAnimal != null) {
                    uniqueAnimalTypes.add(adjacentAnimal.name());
                }
            }
        }

        // 1 point for each unique adjacent animal type
        return uniqueAnimalTypes.size();
    }

    /**
     * Finds a tile by its coordinates on the player's board.
     *
     * @param player the current player.
     * @param x      the x-coordinate.
     * @param y      the y-coordinate.
     * @return the tile at the given position, or null if not found.
     */
    private Tile findTileByPosition(Player player, int x, int y) {
        for (Map.Entry<Tile, Point> entry : player.getPosition().entrySet()) {
            Point pos = entry.getValue();
            if (pos.x == x && pos.y == y) {
                return entry.getKey();
            }
        }
        return null;
    }
}

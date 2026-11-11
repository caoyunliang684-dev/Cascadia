package foxscore;

import game.Animal;
import game.Player;
import game.Tile;
import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import score.ScoreCalculate;

/**
 * FoxD implements a scoring rule for FoxB(As the square cannot be completed).
 */
public class FoxD implements ScoreCalculate {
	
	/**
     * Constructs a new instance of {@code FoxD}.
     * This is a default constructor with no additional initialization.
     */
    public FoxD() {
        // No initialization required
    }
    /**
     * Calculates the total score for the player based on fox tiles.
     * Each fox scores points for adjacent animal pairs.
     *
     * @param player the player whose score is being calculated.
     * @return the total score.
     */
    @Override
    public int calculateScore(Player player) {
        Objects.requireNonNull(player);
        Map<Tile, Animal> animalSet = player.getAnimalset();
        int totalScore = 0;

        // Iterate through all fox tiles
        for (Tile tile : animalSet.keySet()) {
            Animal animal = animalSet.get(tile);

            // If the tile has a fox, calculate its score based on adjacent pairs
            if (animal != null && animal.name().equals("Fox")) {
                int pairs = countAnimalPairs(player, tile);
                totalScore += calculateScoreByPairs(pairs);
            }
        }

        return totalScore;
    }

    /**
     * Counts the number of different animal pairs adjacent to a fox.
     *
     * @param player  the current player.
     * @param foxTile the tile with the fox.
     * @return the number of animal pairs.
     */
    private int countAnimalPairs(Player player, Tile foxTile) {
        Map<String, Integer> animalCountMap = new HashMap<>();

        // Get adjacent tiles around the fox
        for (Tile adjacentTile : getAdjacentTiles(player, foxTile)) {
            Animal adjacentAnimal = player.getAnimalset().get(adjacentTile);

            // Exclude empty tiles and other foxes
            if (adjacentAnimal != null && !adjacentAnimal.name().equals("Fox")) {
                String animalName = adjacentAnimal.name();
                animalCountMap.put(animalName, animalCountMap.getOrDefault(animalName, 0) + 1);
            }
        }

        // Count how many animal types appear in pairs
        int pairs = 0;
        for (int count : animalCountMap.values()) {
            if (count >= 2) {
                pairs++;
            }
        }

        return pairs;
    }

    /**
     * Retrieves adjacent tiles (up, down, left, right) of a given tile.
     *
     * @param player the current player.
     * @param tile   the tile to check around.
     * @return a set of adjacent tiles.
     */
    private Set<Tile> getAdjacentTiles(Player player, Tile tile) {
        Set<Tile> adjacentTiles = new HashSet<>();
        Map<Tile, Point> positions = player.getPosition();
        Point pos = positions.get(tile);

        // Four directions: up, down, left, right
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

        for (int[] dir : directions) {
            int newX = pos.x + dir[0];
            int newY = pos.y + dir[1];

            Tile adjacentTile = findTileByPosition(player, newX, newY);
            if (adjacentTile != null) {
                adjacentTiles.add(adjacentTile);
            }
        }

        return adjacentTiles;
    }

    /**
     * Calculates the score based on the number of adjacent animal pairs.
     *
     * @param pairs the number of animal pairs.
     * @return the score for the fox.
     */
    private int calculateScoreByPairs(int pairs) {
        switch (pairs) {
            case 1:
                return 3;  // 1 pair → 3 points
            case 2:
                return 5;  // 2 pairs → 5 points
            default:
                return 0;  // No pairs → 0 points
        }
    }

    /**
     * Finds a tile by its coordinates on the player's board.
     *
     * @param player the current player.
     * @param x      the x-coordinate.
     * @param y      the y-coordinate.
     * @return the tile at the given position or null if not found.
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

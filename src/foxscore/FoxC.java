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
 * FoxC implements the scoring rule for foxes based on the most frequent adjacent animal type.
 * A fox scores points equal to the highest number of the same animal type adjacent to it.
 */
public class FoxC implements ScoreCalculate {
	
	/**
     * Constructs a new instance of {@code FoxC}.
     * This is a default constructor with no additional initialization.
     */
    public FoxC() {
        // No initialization required
    }
    /**
     * Calculates the total score for the player based on fox tiles.
     * Each fox scores points equal to the most frequent adjacent animal type count.
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

            // If the tile has a fox, calculate its score
            if (animal != null && animal.name().equals("Fox")) {
                int scoreForFox = calculateMaxAnimalCountAroundFox(player, tile);
                totalScore += scoreForFox;
            }
        }

        return totalScore;
    }

    /**
     * Calculates the score for a fox based on the most frequent adjacent animal type.
     *
     * @param player  the current player.
     * @param foxTile the tile with the fox.
     * @return the score for the fox.
     */
    private int calculateMaxAnimalCountAroundFox(Player player, Tile foxTile) {
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

        // Find the highest count of the same animal type
        int maxCount = 0;
        for (int count : animalCountMap.values()) {
            if (count > maxCount) {
                maxCount = count;
            }
        }

        return maxCount;  // Score equals the highest adjacent animal count
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

package eaglescore;

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
 * EagleD implements the scoring rule for EagleA(As the square cannot be completed).
 */
public class EagleD implements ScoreCalculate {
	
	/**
     * Constructs a new instance of {@code EagleD}.
     * This is a default constructor with no additional initialization.
     */
    public EagleD() {
        // No initialization required
    }
    /**
     * Calculates the player's total score based on isolated eagles.
     *
     * @param player the player whose score is being calculated.
     * @return the total score.
     */
    @Override
    public int calculateScore(Player player) {
        Objects.requireNonNull(player);
        Map<Tile, Animal> animalSet = player.getAnimalset();
        Set<Tile> visitedTiles = new HashSet<>();

        int isolatedEagleCount = 0;

        // Iterate through all tiles to find eagles
        for (Tile tile : animalSet.keySet()) {
            Animal animal = animalSet.get(tile);

            // Check if the tile has an eagle and hasn't been visited
            if (animal != null && animal.name().equals("Eagle") && !visitedTiles.contains(tile)) {
                if (isIsolatedEagle(player, tile)) {
                    isolatedEagleCount++;  // Increment isolated eagle count
                    visitedTiles.add(tile);  // Mark as visited
                }
            }
        }

        // Calculate score based on the number of isolated eagles
        return calculateScoreByIsolatedEagleCount(isolatedEagleCount);
    }

    /**
     * Checks if the eagle on the given tile is isolated (no adjacent eagles).
     *
     * @param player    the current player.
     * @param eagleTile the tile with the eagle.
     * @return true if the eagle is isolated; false otherwise.
     */
    private boolean isIsolatedEagle(Player player, Tile eagleTile) {
        Set<Tile> adjacentTiles = getAdjacentTiles(player, eagleTile);

        for (Tile adjacentTile : adjacentTiles) {
            Animal adjacentAnimal = player.getAnimalset().get(adjacentTile);
            if (adjacentAnimal != null && adjacentAnimal.name().equals("Eagle")) {
                return false;  // Not isolated if adjacent eagle exists
            }
        }
        return true;  // No adjacent eagles, hence isolated
    }

    /**
     * Retrieves adjacent tiles (up, down, left, right) of a given tile.
     *
     * @param player the current player.
     * @param tile   the tile to check.
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
     * @return the tile at the specified position or null if not found.
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

    /**
     * Calculates the score based on the number of isolated eagles.
     *
     * @param isolatedCount the number of isolated eagles.
     * @return the calculated score.
     */
    private int calculateScoreByIsolatedEagleCount(int isolatedCount) {
        switch (isolatedCount) {
            case 0:
                return 0;
            case 1:
                return 2;
            case 2:
                return 5;
            case 3:
                return 8;
            case 4:
                return 11;
            case 5:
                return 14;
            case 6:
                return 18;
            case 7:
                return 22;
            case 8:
                return 26;
            default:
                return 26;  // Max score capped at 26
        }
    }
}

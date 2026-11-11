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
 * BearD implements a scoring rule where connected groups of bears score points.
 * Groups of 2 to 4 adjacent bears earn different points based on their size.
 */
public class BearD implements ScoreCalculate {
	
	 /**
     * Constructs a new instance of {@code BearD}.
     * This is a default constructor with no additional initialization.
     */
    public BearD() {
        // No initialization required
    }
    /**
     * Calculates the player's total score based on bear groups of size 2 to 4.
     *
     * @param player the player whose score is being calculated.
     * @return the total score.
     */
    @Override
    public int calculateScore(Player player) {
        Objects.requireNonNull(player);
        Map<Tile, Animal> animalSet = player.getAnimalset();
        HashSet<Tile> visitedTiles = new HashSet<>();

        int totalScore = 0;

        // Iterate through all bear tiles
        for (Tile tile : animalSet.keySet()) {
            Animal animal = animalSet.get(tile);

            // Check if the tile has a bear and hasn't been visited
            if (animal != null && animal.name().equals("Bear") && !visitedTiles.contains(tile)) {
                // Count the size of the connected bear group
                int groupSize = countBearGroup(player, tile, visitedTiles);

                // Score groups of size 2 to 4
                if (groupSize >= 2 && groupSize <= 4) {
                    totalScore += calculateScoreByGroupSize(groupSize);
                }
            }
        }

        return totalScore;
    }

    /**
     * Recursively counts the size of the connected bear group starting from a tile.
     *
     * @param player       the current player.
     * @param tile         the starting tile.
     * @param visitedTiles the set of visited tiles.
     * @return the size of the connected bear group.
     */
    private int countBearGroup(Player player, Tile tile, Set<Tile> visitedTiles) {
        // Return 0 if the tile has been visited
        if (visitedTiles.contains(tile)) {
            return 0;
        }

        Animal animal = player.getAnimalset().get(tile);
        if (animal == null || !animal.name().equals("Bear")) {
            return 0;
        }

        // Mark the current tile as visited
        visitedTiles.add(tile);

        int groupSize = 1; // Count the current bear

        // Recursively count adjacent bears
        for (Tile adjacentTile : getAdjacentBearTiles(player, tile)) {
            groupSize += countBearGroup(player, adjacentTile, visitedTiles);
        }
        return groupSize;
    }

    /**
     * Calculates the score based on the size of the bear group.
     *
     * @param groupSize the size of the bear group.
     * @return the score for the group.
     */
    private int calculateScoreByGroupSize(int groupSize) {
        switch (groupSize) {
            case 2:
                return 5;   // Group of 2 bears → 5 points
            case 3:
                return 8;   // Group of 3 bears → 8 points
            case 4:
                return 13;  // Group of 4 bears → 13 points
            default:
                return 0;   // Groups larger than 4 → no points
        }
    }

    /**
     * Retrieves adjacent tiles that contain bears.
     *
     * @param player the current player.
     * @param tile   the tile to check around.
     * @return a set of adjacent bear tiles.
     */
    private HashSet<Tile> getAdjacentBearTiles(Player player, Tile tile) {
        HashSet<Tile> adjacentTiles = new HashSet<>();
        Map<Tile, Point> positions = player.getPosition();
        Point currentPos = positions.get(tile);

        for (Map.Entry<Tile, Point> entry : positions.entrySet()) {
            Tile adjacentTile = entry.getKey();
            Point pos = entry.getValue();

            // Check adjacency (up, down, left, right)
            if ((Math.abs(pos.x - currentPos.x) == 1 && pos.y == currentPos.y) ||
                (Math.abs(pos.y - currentPos.y) == 1 && pos.x == currentPos.x)) {

                Animal adjacentAnimal = player.getAnimalset().get(adjacentTile);
                if (adjacentAnimal != null && adjacentAnimal.name().equals("Bear")) {
                    adjacentTiles.add(adjacentTile);
                }
            }
        }

        return adjacentTiles;
    }
}

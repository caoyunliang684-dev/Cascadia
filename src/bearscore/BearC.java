package bearscore;

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
 * BearC implements a scoring rule where bear groups of sizes 1, 2, or 3 earn points.
 * An additional bonus is awarded if the player has at least one group of each size.
 */
public class BearC implements ScoreCalculate {
	
	 /**
     * Constructs a new instance of {@code BearC}.
     * This is a default constructor with no additional initialization.
     */
    public BearC() {
        // No initialization required
    }
    /**
     * Calculates the player's score based on bear groups of size 1, 2, or 3.
     * A bonus of 3 points is awarded if the player has at least one group of each size.
     *
     * @param player the player whose score is being calculated.
     * @return the total score.
     */
    @Override
    public int calculateScore(Player player) {
        Objects.requireNonNull(player);
        Map<Tile, Animal> animalSet = player.getAnimalset();
        Set<Tile> visitedTiles = new HashSet<>();
        HashMap<Integer, Integer> groupCountMap = new HashMap<>();  // Tracks the number of each group size

        int totalScore = 0;

        // Iterate through all bear tiles
        for (Tile tile : animalSet.keySet()) {
            Animal animal = animalSet.get(tile);

            // Check if the tile has a bear and hasn't been visited
            if (animal != null && animal.name().equals("Bear") && !visitedTiles.contains(tile)) {
                // Count the size of the connected bear group
                int groupSize = countBearGroup(player, tile, visitedTiles);

                // Score groups of size 1, 2, or 3
                if (groupSize >= 1 && groupSize <= 3) {
                    totalScore += calculateScoreByGroupSize(groupSize);

                    // Record the group size count for bonus calculation
                    groupCountMap.put(groupSize, groupCountMap.getOrDefault(groupSize, 0) + 1);
                }
            }
        }

        // Bonus: 3 points for having groups of size 1, 2, and 3
        if (groupCountMap.containsKey(1) && groupCountMap.containsKey(2) && groupCountMap.containsKey(3)) {
            totalScore += 3;
            System.out.println("Congratulations! You have one group each of 1, 2, and 3 bears, earning 3 points!");
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
            case 1:
                return 2;  // Group of 1 bear → 2 points
            case 2:
                return 5;  // Group of 2 bears → 5 points
            case 3:
                return 8;  // Group of 3 bears → 8 points
            default:
                return 0;  // Groups larger than 3 → no points
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

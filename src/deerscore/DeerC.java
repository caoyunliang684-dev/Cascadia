package deerscore;

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
 * ElkC implements the scoring rule for connected groups of deer.
 * Points are awarded based on the size of connected deer groups.
 */
public class DeerC implements ScoreCalculate {
	
	 /**
     * Constructs a new instance of {@code DeerC}.
     * This is a default constructor with no additional initialization.
     */
    public DeerC() {
        // No initialization required
    }
    /**
     * Calculates the player's total score based on connected deer groups.
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

        // Iterate through all tiles and calculate scores for deer groups
        for (Tile tile : animalSet.keySet()) {
            Animal animal = animalSet.get(tile);

            // Process only deer and skip other animals
            if (animal != null && animal.name().equals("Deer") && !visitedTiles.contains(tile)) {
                // Calculate the size of the connected deer group
                int groupSize = calculateElkGroupSize(player, tile, visitedTiles);

                // Add score based on group size
                totalScore += calculateScoreByGroupSize(groupSize);
            }
        }

        return totalScore;
    }

    /**
     * Recursively calculates the size of a connected deer group.
     *
     * @param player       the current player.
     * @param tile         the starting tile.
     * @param visitedTiles the set of visited tiles.
     * @return the size of the connected deer group.
     */
    private int calculateElkGroupSize(Player player, Tile tile, Set<Tile> visitedTiles) {
        // Return 0 if the tile has been visited
        if (visitedTiles.contains(tile)) {
            return 0;
        }

        Animal animal = player.getAnimalset().get(tile);
        if (animal == null || !animal.name().equals("Deer")) {
            return 0;
        }

        // Mark the current tile as visited
        visitedTiles.add(tile);
        int groupSize = 1;

        // Recursively expand to adjacent deer tiles
        for (Tile adjacentTile : getAdjacentElkTiles(player, tile)) {
            groupSize += calculateElkGroupSize(player, adjacentTile, visitedTiles);
        }

        return groupSize;
    }

    /**
     * Retrieves adjacent tiles that contain deer.
     *
     * @param player the current player.
     * @param tile   the tile to check around.
     * @return a set of adjacent deer tiles.
     */
    private Set<Tile> getAdjacentElkTiles(Player player, Tile tile) {
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
                if (adjacentAnimal != null && adjacentAnimal.name().equals("Deer")) {
                    adjacentTiles.add(adjacentTile);
                }
            }
        }

        return adjacentTiles;
    }

    /**
     * Calculates the score based on the size of the deer group.
     *
     * @param groupSize the size of the connected deer group.
     * @return the score for the group.
     */
    private int calculateScoreByGroupSize(int groupSize) {
        switch (groupSize) {
            case 1:
                return 2;
            case 2:
                return 4;
            case 3:
                return 7;
            case 4:
                return 10;
            case 5:
                return 14;
            case 6:
                return 18;
            case 7:
                return 23;
            case 8:
                return 28;
            default:
                return 28;  // Maximum score for groups larger than 8
        }
    }
}

package salmonscore;

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
 * FishC implements the scoring rule for connected groups of salmon.
 * Points are awarded based on the size of connected salmon groups.
 */
public class SalmonC implements ScoreCalculate {
	
	/**
     * Constructs a new instance of {@code SalmonC}.
     * This is a default constructor with no additional initialization.
     */
    public SalmonC() {
        // No initialization required
    }
    /**
     * Calculates the total score for a player based on connected salmon groups.
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

        // Iterate through all tiles to find salmon groups
        for (Tile tile : animalSet.keySet()) {
            Animal animal = animalSet.get(tile);

            // Check if the tile has salmon and hasn't been visited
            if (animal != null && animal.name().equals("Salmon") && !visitedTiles.contains(tile)) {
                // Count the size of the connected salmon group
                int groupSize = countSalmonGroup(player, tile, visitedTiles);

                // Add score based on group size
                totalScore += calculateScoreByGroupSize(groupSize);
            }
        }

        return totalScore;
    }

    /**
     * Counts the size of the connected salmon group.
     *
     * @param player       the current player.
     * @param tile         the starting tile.
     * @param visitedTiles the set of visited tiles.
     * @return the size of the salmon group.
     */
    private int countSalmonGroup(Player player, Tile tile, Set<Tile> visitedTiles) {
        Set<Tile> groupTiles = new HashSet<>();
        exploreSalmonGroup(player, tile, groupTiles);

        // Mark all tiles in the group as visited
        visitedTiles.addAll(groupTiles);

        return groupTiles.size();
    }

    /**
     * Recursively explores the connected salmon group using DFS.
     *
     * @param player     the current player.
     * @param tile       the current tile.
     * @param groupTiles the set of connected salmon tiles.
     */
    private void exploreSalmonGroup(Player player, Tile tile, Set<Tile> groupTiles) {
        if (groupTiles.contains(tile)) return;

        groupTiles.add(tile);

        // Get adjacent salmon tiles
        Set<Tile> adjacentSalmonTiles = getAdjacentSalmonTiles(player, tile);

        // Recursively explore adjacent salmon tiles
        for (Tile adjacentTile : adjacentSalmonTiles) {
            exploreSalmonGroup(player, adjacentTile, groupTiles);
        }
    }

    /**
     * Retrieves adjacent tiles that contain salmon.
     *
     * @param player the current player.
     * @param tile   the tile to check around.
     * @return a set of adjacent salmon tiles.
     */
    private Set<Tile> getAdjacentSalmonTiles(Player player, Tile tile) {
        Set<Tile> adjacentTiles = new HashSet<>();
        Map<Tile, Point> positions = player.getPosition();
        Point currentPos = positions.get(tile);

        for (Map.Entry<Tile, Point> entry : positions.entrySet()) {
            Point pos = entry.getValue();
            Tile adjacentTile = entry.getKey();

            // Check adjacency (up, down, left, right)
            if ((Math.abs(pos.x - currentPos.x) == 1 && pos.y == currentPos.y) ||
                (Math.abs(pos.y - currentPos.y) == 1 && pos.x == currentPos.x)) {
                
                Animal adjacentAnimal = player.getAnimalset().get(adjacentTile);

                if (adjacentAnimal != null && adjacentAnimal.name().equals("Salmon")) {
                    adjacentTiles.add(adjacentTile);
                }
            }
        }

        return adjacentTiles;
    }

    /**
     * Calculates the score based on the size of the salmon group.
     *
     * @param groupSize the size of the salmon group.
     * @return the score for the group.
     */
    private int calculateScoreByGroupSize(int groupSize) {
        switch (groupSize) {
            case 0:
                return 0; 
            case 1:
                return 0;  
            case 2:
                return 0;  
            case 3:
                return 10;  
            case 4:
                return 12; 
            case 5:
                return 15; 

            default:
                return 17; 
        }
    }
}

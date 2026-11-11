package score;

import game.Animal;
import game.Player;
import game.Tile;
import java.awt.Point;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * FamilyScore calculates the player's score based on groups of adjacent tiles with matching animal tokens.
 * Implements the ScoreCalculate interface.
 */
public class FamilyScore implements ScoreCalculate {
	
	/**
     * Constructs a new instance of {@code FamilyScore}.
     * This is a default constructor with no additional initialization.
     */
    public FamilyScore() {
        // No initialization required
    }
    /**
     * Calculates the total score for a player based on connected animal groups.
     *
     * @param player the player whose score is being calculated.
     * @return the total score.
     */
    @Override
    public int calculateScore(Player player) {
        Objects.requireNonNull(player);
        int totalScore = 0;

        // Get the player's animal tokens
        Map<Tile, Animal> animalSet = player.getAnimalset();

        // Track visited tiles to avoid duplicate scoring
        HashSet<Tile> visitedTiles = new HashSet<>();

        // Iterate through each tile
        for (Tile tile : animalSet.keySet()) {
            if (!visitedTiles.contains(tile)) {
                // Calculate the size of the connected group
                int groupSize = calculateGroupSize(player, tile, animalSet, visitedTiles);

                // Add the score based on group size
                totalScore += calculateScoreByGroupSize(groupSize);
            }
        }

        return totalScore;
    }

    /**
     * Finds the size of a connected group of adjacent tiles with the same animal.
     *
     * @param player       the current player.
     * @param startTile    the starting tile.
     * @param animalSet    the player's animal tokens.
     * @param visitedTiles the set of visited tiles.
     * @return the size of the connected group.
     */
    private int calculateGroupSize(Player player, Tile startTile, Map<Tile, Animal> animalSet, Set<Tile> visitedTiles) {
        // Get the animal on the starting tile
        Animal startAnimal = animalSet.get(startTile);
        if (startAnimal == null) {
            return 0;
        }

        // Mark the starting tile as visited
        visitedTiles.add(startTile);

        // Initialize the group size
        int groupSize = 1;

        // Expand to adjacent tiles
        boolean newTileFound;
        do {
            newTileFound = false;

            // Iterate through all tiles
            for (Tile tile : animalSet.keySet()) {
                if (!visitedTiles.contains(tile)) {
                    // Check if the tile is adjacent to any visited tile
                    for (Tile visitedTile : visitedTiles) {
                        if (isAdjacent(player, tile, visitedTile) && startAnimal.equals(animalSet.get(tile))) {
                            // Add adjacent matching tile to the group
                            visitedTiles.add(tile);
                            groupSize++;
                            newTileFound = true;
                            break; // Stop after adding one tile
                        }
                    }
                }
            }
        } while (newTileFound);

        return groupSize;
    }

    /**
     * Calculates the score based on the group size according to the game rules.
     *
     * @param groupSize the size of the group.
     * @return the score for the group.
     */
    private int calculateScoreByGroupSize(int groupSize) {
        switch (groupSize) {
            case 0:
                return 0;
            case 1:
                return 2;
            case 2:
                return 5;
            case 3:
                return 9;
            default:
                return 9; // 3 or more tiles give 9 points
        }
    }

    /**
     * Checks if two tiles are adjacent on the board.
     *
     * @param player the current player.
     * @param tile1  the first tile.
     * @param tile2  the second tile.
     * @return true if adjacent; false otherwise.
     */
    private boolean isAdjacent(Player player, Tile tile1, Tile tile2) {
        Point pos1 = player.getPosition().get(tile1);
        Point pos2 = player.getPosition().get(tile2);

        if (pos1 == null || pos2 == null) {
            return false;
        }

        // Check adjacency (up, down, left, right)
        return (Math.abs(pos1.x - pos2.x) == 1 && pos1.y == pos2.y) ||  // Left/Right
               (Math.abs(pos1.y - pos2.y) == 1 && pos1.x == pos2.x);    // Up/Down
    }
}


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
 * ElkB implements the scoring rule for vertical lines of connected deer.
 * Points are awarded based on the length of continuous vertical deer groups.
 */
public class DeerB implements ScoreCalculate {
	
	 /**
     * Constructs a new instance of {@code DeerB}.
     * This is a default constructor with no additional initialization.
     */
    public DeerB() {
        // No initialization required
    }
    /**
     * Calculates the player's total score based on vertical deer groups.
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

        // Iterate through all deer tiles
        for (Tile tile : animalSet.keySet()) {
            Animal animal = animalSet.get(tile);

            // Check if the tile has a deer and hasn't been visited
            if (animal != null && animal.name().equals("Deer") && !visitedTiles.contains(tile)) {
                // Find vertical line of connected deer
                int verticalLineLength = findVerticalLine(player, tile, animalSet, visitedTiles);

                // Add score based on the line length
                totalScore += calculateScoreByLineLength(verticalLineLength);
            }
        }

        return totalScore;
    }

    /**
     * Finds the length of a vertical line of connected deer.
     *
     * @param player       the current player.
     * @param startTile    the starting tile.
     * @param animalSet    the player's animal tokens.
     * @param visitedTiles the set of visited tiles.
     * @return the length of the vertical deer line.
     */
    private int findVerticalLine(Player player, Tile startTile, Map<Tile, Animal> animalSet, Set<Tile> visitedTiles) {
        int lineLength = 1;  // Include the starting tile

        // Extend upward
        lineLength += countElkInDirection(player, startTile, 0, 1, animalSet, visitedTiles);

        // Extend downward
        lineLength += countElkInDirection(player, startTile, 0, -1, animalSet, visitedTiles);

        // Mark the entire vertical line as visited
        markLineAsVisited(player, startTile, 0, 1, lineLength, visitedTiles, animalSet);

        return lineLength;
    }

    /**
     * Counts the number of consecutive deer in a given direction.
     *
     * @param player       the current player.
     * @param startTile    the starting tile.
     * @param dx           x-direction increment.
     * @param dy           y-direction increment.
     * @param animalSet    the player's animal tokens.
     * @param visitedTiles the set of visited tiles.
     * @return the number of consecutive deer in the specified direction.
     */
    private int countElkInDirection(Player player, Tile startTile, int dx, int dy, Map<Tile, Animal> animalSet, Set<Tile> visitedTiles) {
        int count = 0;
        Point currentPos = player.getPosition().get(startTile);

        while (true) {
            // Calculate the next position
            Point nextPos = new Point(currentPos.x + dx, currentPos.y + dy);
            Tile nextTile = findTileByPosition(player, nextPos.x, nextPos.y);

            // Stop if no tile exists
            if (nextTile == null) {
                break;
            }

            Animal nextAnimal = animalSet.get(nextTile);

            // Stop if the next tile is not a deer or already visited
            if (nextAnimal == null || !nextAnimal.name().equals("Deer") || visitedTiles.contains(nextTile)) {
                break;
            }

            // Continue if the next tile is a deer
            count++;
            currentPos = nextPos;
            visitedTiles.add(nextTile);
        }

        return count;
    }

    /**
     * Marks all deer tiles in the vertical line as visited.
     *
     * @param player       the current player.
     * @param startTile    the starting tile.
     * @param dx           x-direction increment.
     * @param dy           y-direction increment.
     * @param lineLength   the length of the line.
     * @param visitedTiles the set of visited tiles.
     * @param animalSet    the player's animal tokens.
     */
    private void markLineAsVisited(Player player, Tile startTile, int dx, int dy, int lineLength, Set<Tile> visitedTiles, Map<Tile, Animal> animalSet) {
        Point currentPos = player.getPosition().get(startTile);
        visitedTiles.add(startTile);

        for (int i = 1; i < lineLength; i++) {
            Point nextPos = new Point(currentPos.x + (dx * i), currentPos.y + (dy * i));
            Tile nextTile = findTileByPosition(player, nextPos.x, nextPos.y);

            if (nextTile != null && animalSet.get(nextTile) != null && animalSet.get(nextTile).name().equals("Deer")) {
                visitedTiles.add(nextTile);
            }
        }
    }

    /**
     * Calculates the score based on the length of the vertical deer line.
     *
     * @param lineLength the length of the deer line.
     * @return the score for the line.
     */
    private int calculateScoreByLineLength(int lineLength) {
        switch (lineLength) {
            case 1:
                return 2;
            case 2:
                return 5;
            case 3:
                return 9;
            case 4:
                return 13;
            default:
                return 0;
        }
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

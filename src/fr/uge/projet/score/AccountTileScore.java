package fr.uge.projet.score;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.uge.projet.tile.Tile;
import fr.uge.projet.utils.Coordinate;
import fr.uge.projet.utils.PlayerInformation;


public class AccountTileScore {
  /**
   * Calculates the total score for tiles in the given placement.
   *
   * <p>The score is determined by finding the largest connected region of each tile type
   * and summing their sizes.
   *
   * @param placement the {@link PlayerInformation} object representing the player's current tile layout; must not be {@code null}.
   * @return the total tile score based on the largest connected regions.
   * @throws NullPointerException if {@code placement} is {@code null}.
   */
	 public int calculateTileScore(PlayerInformation placement) {
	        var score = 0;
	        Map<Coordinate, Tile> tileMap = placement.getTileMap();

	        // Example: Scoring for the largest connected region of each tile type
	        Map<Tile, Integer> largestRegions = new HashMap<>();

	        for (Tile tile : Tile.values()) {
	            largestRegions.put(tile, findLargestConnectedRegion(tile, tileMap));
	        }

	        for (int regionSize : largestRegions.values()) {
	            score += regionSize; // 1 point per tile in the largest region
	        }

	        return score;
	    }
	  /**
    * Finds the size of the largest connected region for a specific tile type.
    *
    * @param tile    the {@link Tile} type to evaluate.
    * @param tileMap a map of {@link Coordinate} to {@link Tile} representing the placement layout.
    * @return the size of the largest connected region for the given tile type.
    */
	    private int findLargestConnectedRegion(Tile tile, Map<Coordinate, Tile> tileMap) {
	        Set<Coordinate> visited = new HashSet<>();
	        int largestRegion = 0;

	        for (Coordinate coord : tileMap.keySet()) {
	            if (tileMap.get(coord) == tile && !visited.contains(coord)) {
	                Set<Coordinate> region = new HashSet<>();
	                exploreConnectedTiles(coord, tile, tileMap, region);
	                visited.addAll(region);
	                largestRegion = Math.max(largestRegion, region.size());
	            }
	        }

	        return largestRegion;
	    }
	    /**
	     * Explores and collects all connected tiles of the same type starting from a given coordinate.
	     *
	     * @param current   the current {@link Coordinate} to explore.
	     * @param tile      the {@link Tile} type being evaluated.
	     * @param tileMap   a map of {@link Coordinate} to {@link Tile} representing the placement layout.
	     * @param connected a set of {@link Coordinate} to store the connected tiles.
	     */
	    private void exploreConnectedTiles(Coordinate current, Tile tile, Map<Coordinate, Tile> tileMap, Set<Coordinate> connected) {
	        if (!tileMap.containsKey(current) || connected.contains(current) || tileMap.get(current) != tile) {
	            return;
	        }
	        connected.add(current);
	        for (Coordinate neighbor : current.fourSidesCordonnates()) {
	            exploreConnectedTiles(neighbor, tile, tileMap, connected);
	        }
	    }
}

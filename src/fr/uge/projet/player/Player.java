package fr.uge.projet.player;

import fr.uge.projet.utils.Bloc;
import fr.uge.projet.utils.Coordinate;
import fr.uge.projet.utils.PlayerInformation;
import fr.uge.projet.utils.TotalTile;

public class Player {
  private final String name;
  private final PlayerInformation placement;
  /**
   * Constructs a new {@code Player} with the specified name and placement information.
   *
   * @param name      the name of the player; must not be {@code null}.
   * @param placement the player's placement information; must not be {@code null}.
   * @throws NullPointerException if {@code name} or {@code placement} is {@code null}.
   */
  public Player(String name, PlayerInformation placement){
	this.name = name;
	this.placement = placement;
  }
  /**
   * Initializes the player's placement with three tiles from the given total tile pool.
   *
   * <p>The tiles are placed at predefined coordinates: (1,1), (1,2), and (2,1).
   *
   * @param totalTile the {@link TotalTile} object providing tiles for placement; must not be {@code null}.
   * @throws NullPointerException if {@code totalTile} is {@code null}.
   */
  public void initPlacement(TotalTile totalTile) {
	placement.addNewTile(new Coordinate(1,1), new Bloc(totalTile.getTile()));
	placement.addNewTile(new Coordinate(1,2), new Bloc(totalTile.getTile()));
	placement.addNewTile(new Coordinate(2,1), new Bloc(totalTile.getTile()));
  }
  /**
   * Retrieves the player's placement information.
   *
   * @return the {@link PlayerInformation} object representing the player's placement.
   */
  public PlayerInformation getPlacement() {
    return placement;
  }
  @Override
  public String toString() {
	// TODO Auto-generated method stub
    return "Player : " + name ;
  }
}

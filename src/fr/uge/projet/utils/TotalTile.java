package fr.uge.projet.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.uge.projet.animal.Animal;

import fr.uge.projet.tile.Tile;

public class TotalTile {
  private final List<Tile> tiles = new ArrayList<>();
  private Random random = new Random();
  
  //initilization: ajouter 85 Tuiles.
  public TotalTile() {
    for(int i = 0; i<12;i++) {
      tiles.add(Tile.FOREST);
      tiles.add(Tile.MOUNTAIN);
      tiles.add(Tile.PRAIRIE);
      tiles.add(Tile.RIVER);
      tiles.add(Tile.SWAMP);
    }
  }
  
  //On le sorte du sac, et on le supprime dans la sac

  /**
   * Retrieves a random tile from the bag and removes it.
   *
   * @return the randomly selected {@link Tile}, or {@code null} if the bag is empty.
   */
  public Tile getTile() {
    if(tiles.isEmpty()) {
      return null;
    }
    int i = random.nextInt(tiles.size());
    return tiles.remove(i);
  }
  /**
   * Returns a tile back to the bag.
   *
   * @param tile the {@link Tile} to return; must not be {@code null}.
   * @throws NullPointerException if {@code tile} is {@code null}.
   */
  public void putBackTile(Tile tile) {
    tiles.add(tile);
  }
}

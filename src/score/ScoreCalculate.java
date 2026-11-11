package score;

import game.Player;

/**
 * ScoreCalculate is an interface for calculating a player's score.
 * Different scoring strategies should implement this interface.
 */
public interface ScoreCalculate {

    /**
     * Calculates the score for a given player.
     *
     * @param player the player whose score is being calculated.
     * @return the calculated score.
     */
    int calculateScore(Player player);
}

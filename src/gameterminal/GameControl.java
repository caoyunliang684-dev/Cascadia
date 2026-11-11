package gameterminal;

//import Score.*;
import game.Tile;
import salmonscore.*;
import score.FamilyScore;
import score.MediumScore;
import score.ScoreCalculate;
import game.Player;
import bearscore.*;
import deerscore.*;
import eaglescore.*;
import foxscore.*;
import game.Animal;

import java.awt.Point;
import java.util.Scanner;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



/**
 * Controls the game flow and player interactions.
 */
public class GameControl {

    private final GameData gameData; // Game data
    private final Scanner scanner;   // User input

    /**
     * Constructs a GameControl object.
     * Initializes game data and user input scanner.
     */
    public GameControl() {
        this.gameData = new GameData();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Initializes the initial tile pool for the game.
     * <p>
     * This method creates 12 tiles for each landscape type (Forest, Prairie, Wetland, Mountain, River),
     * each with a unique ID starting from 100. The tiles are then added to the initial tile pool.
     * </p>
     *
     * Example:
     * <ul>
     *     <li>Forest: 12 tiles</li>
     *     <li>Prairie: 12 tiles</li>
     *     <li>Wetland: 12 tiles</li>
     *     <li>Mountain: 12 tiles</li>
     *     <li>River: 12 tiles</li>
     * </ul>
     * 
     * Total: 60 tiles.
     */
    private void initializeInitialTilePool() {
        System.out.println("Initializing the initial tile pool...");

        int id = 100; // Initial tile ID starts from 100
        String[] landscapes = gameData.getLandscapes();

        for (int i = 0; i < landscapes.length; i++) {
            for (int j = 0; j < 12; j++) {
                Animal[] supportedAnimals = gameData.getAnimalsForLandscape(i);
                Tile tile = new Tile(landscapes[i], supportedAnimals, id++);
                gameData.addTileToInitialPool(tile);
            }
        }

        System.out.println("Initial tile pool initialized! Total tiles: " + gameData.getInitialTilePool().size());
    }

    /**
     * Initializes the main game tile pool.
     * <p>
     * This method creates 17 tiles for each landscape type (Forest, Prairie, Wetland, Mountain, River),
     * each with a unique ID starting from 0. The tiles are then added to the main tile pool.
     * </p>
     *
     * Example:
     * <ul>
     *     <li>Forest: 17 tiles</li>
     *     <li>Prairie: 17 tiles</li>
     *     <li>Wetland: 17 tiles</li>
     *     <li>Mountain: 17 tiles</li>
     *     <li>River: 17 tiles</li>
     * </ul>
     * 
     * Total: 85 tiles.
     */
    private void initializeTilePool() {
        System.out.println("Initializing the main game tile pool...");

        String[] landscapes = gameData.getLandscapes();
        int id = 0; // Unique ID starting value

        for (int i = 0; i < landscapes.length; i++) {
            for (int j = 0; j < 17; j++) {
                Animal[] supportedAnimals = gameData.getAnimalsForLandscape(i);
                Tile tile = new Tile(landscapes[i], supportedAnimals, id++);
                gameData.addTileToPool(tile);
            }
        }

        gameData.ChuffleTilePool();
        System.out.println("Main game tile pool initialized! Total tiles: " + gameData.getTilePool().size());
    }

    /**
     * Initializes the animal token pool.
     * <p>
     * This method creates 20 animal tokens for each animal type (Bear, Fox, Salmon, Deer, Eagle).
     * All tokens are added to the animal pool and shuffled.
     * </p>
     *
     * Example:
     * <ul>
     *     <li>Bear: 20 tokens</li>
     *     <li>Fox: 20 tokens</li>
     *     <li>Salmon: 20 tokens</li>
     *     <li>Deer: 20 tokens</li>
     *     <li>Eagle: 20 tokens</li>
     * </ul>
     * 
     * Total: 100 tokens.
     */
    private void initializeAnimalPool() {
        System.out.println("Initializing the animal token pool...");
        String[] animalNames = {"Bear", "Fox", "Salmon", "Deer", "Eagle"};

        for (String name : animalNames) {
            for (int i = 0; i < 20; i++) {
                Animal animal = new Animal(name);
                gameData.addAnimalToPool(animal);
            }
        }

        gameData.ChuffleAnimalPool();
        System.out.println("Animal token pool initialized! Total tokens: " + gameData.getAnimalPool().size());
    }
    
    
    /**
     * Checks if all animal tokens in the combination pool are the same.
     *
     * @param currentCombinations A map of the current tile-animal combinations.
     *                            The key is a {@link Tile}, and the value is an {@link Animal}.
     * @return {@code true} if all animal tokens in the pool are the same; {@code false} otherwise.
     */
    private boolean areAllAnimalsSame(HashMap<Tile, Animal> currentCombinations) {
        if (currentCombinations.isEmpty()) {
            return false;
        }

        Animal firstAnimal = null;
        for (Animal animal : currentCombinations.values()) {
            if (firstAnimal == null) {
                firstAnimal = animal;  // Select the first animal for comparison
            } else {
                if (!firstAnimal.equals(animal)) {
                    return false;  // Return false if any animal is different
                }
            }
        }
        return true;  // All animals are the same
    }
    
    /**
     * Checks if there are at least three identical animal tokens in the combination pool.
     *
     * @param currentCombinations A map of the current tile-animal combinations.
     *                            The key is a {@link Tile}, and the value is an {@link Animal}.
     * @return {@code true} if at least three animal tokens are the same; {@code false} otherwise.
     */
    private boolean hasThreeSameAnimals(HashMap<Tile, Animal> currentCombinations) {
        if (currentCombinations.isEmpty()) {
            return false;
        }

        // Count occurrences of each animal
        HashMap<Animal, Integer> animalCount = new HashMap<>();
        for (Animal animal : currentCombinations.values()) {
            animalCount.put(animal, animalCount.getOrDefault(animal, 0) + 1);
            
            // If any animal appears 3 times or more, return true
            if (animalCount.get(animal) == 3) {
                return true;
            }
        }

        return false;  // No animal appears 3 times
    }


    /**
     * Initializes the combination pool with 4 random tile-animal combinations.
     * Ensures that the combinations do not have three or four identical animals.
     *
     * @param currentCombinations A map to store the initialized tile-animal combinations.
     */
    private void initializeCombinations(HashMap<Tile, Animal> currentCombinations) {
        currentCombinations.clear();  // Clear existing combinations

        boolean validCombination = false;

        while (!validCombination) {
            currentCombinations.clear();  // Clear combinations for each draw

            // Draw 4 combinations
            for (int i = 0; i < 4; i++) {
                Map.Entry<Tile, Animal> newCombination = gameData.generateNewCombination();

                if (newCombination != null) {
                    currentCombinations.put(newCombination.getKey(), newCombination.getValue());
                } else {
                    System.out.println("The tile pool or animal pool is empty. Cannot generate combinations!");
                    return;
                }
            }

            // 检查是否有四个相同的动物
            if (areAllAnimalsSame(currentCombinations)) {
                System.out.println("Detected 4 identical animal tokens. Regenerating combinations...");
                // Return animals to the pool and reshuffle
                for (Animal animal : currentCombinations.values()) {
                    gameData.addAnimalToPool(animal);
                }
                gameData.ChuffleAnimalPool();
                continue;  // Retry combination generation
            }

            // 检查是否有三个相同的动物
            if (hasThreeSameAnimals(currentCombinations)) {
                System.out.println("Detected 3 identical animal tokens in the initial combinations.");
                System.out.print("Do you want to regenerate the combinations? (Enter 1 for Yes, 0 for No): ");
                int regenerate = scanner.nextInt();

                if (regenerate == 1) {
                    for (Animal animal : currentCombinations.values()) {
                        gameData.addAnimalToPool(animal);
                    }
                    gameData.ChuffleAnimalPool();
                    continue;  // Retry combination generation
                }
            }

            validCombination = true;
            System.out.println("Successfully initialized 4 new combinations!");
        }
    }

    /**
     * Refills the combination pool to ensure it contains 4 tile-animal combinations.
     * Ensures that the combinations do not have three or four identical animals.
     *
     * @param currentCombinations A map to store the refilled tile-animal combinations.
     */
    private void refillCombinations(HashMap<Tile, Animal> currentCombinations) {
        boolean validCombination = false;

        while (!validCombination) {
            // Fill up to 4 combinations
            while (currentCombinations.size() < 4) {
                Map.Entry<Tile, Animal> newCombination = gameData.generateNewCombination();

                if (newCombination != null) {
                    currentCombinations.put(newCombination.getKey(), newCombination.getValue());

                    // Print the newly added combination
                    System.out.println("Successfully added a new combination: Landscape - " 
                        + newCombination.getKey().landscape() + ", Animal Token - " + newCombination.getValue().name());
                } else {
                    System.out.println("The tile pool or animal pool is empty. Cannot refill combinations!");
                    return;
                }
            }

            // 检查是否有四个相同的动物
            if (areAllAnimalsSame(currentCombinations)) {
                System.out.println("Detected 4 identical animal tokens. Regenerating combinations...");
                for (Animal animal : currentCombinations.values()) {
                    gameData.addAnimalToPool(animal);
                }
                gameData.ChuffleAnimalPool();
                currentCombinations.clear();
                continue;
            }

            // 检查是否有三个相同的动物
            if (hasThreeSameAnimals(currentCombinations)) {
                System.out.println("Detected 3 identical animal tokens in the refilled combinations.");
                System.out.print("Do you want to regenerate the combinations? (Enter 1 for Yes, 0 for No): ");
                int regenerate = scanner.nextInt();

                if (regenerate == 1) {
                    for (Animal animal : currentCombinations.values()) {
                        gameData.addAnimalToPool(animal);
                    }
                    gameData.ChuffleAnimalPool();
                    currentCombinations.clear();
                    continue;
                }
            }

            validCombination = true;
        }
    }


    /**
     * Draws the current state of the player's board with horizontal and vertical grid lines.
     * <p>
     * The landscape type is represented by its first letter (e.g., "Forest" -> "F"),
     * and if there is an animal on the tile, its first letter is also displayed (e.g., "Bear" -> "B").
     * </p>
     *
     * @param player The player whose board will be displayed.
     */
    public void drawPlayerBoard(Player player) {
        int width = 10;  // Board width (x-axis range: 0 to 7)
        int height = 10; // Board height (y-axis range: 0 to 4)

        // Get the player's tile positions and animal placements
        Map<Tile, Point> positions = player.getPosition();
        Map<Tile, Animal> animalSet = player.getAnimalset();

        // Print top row (column labels)
        System.out.print("   "); // Top-left corner space
        for (int x = 0; x < width; x++) {
            System.out.printf("| %2d  ", x); // Column headers
        }
        System.out.println("|");
        System.out.println("   +" + "-----+".repeat(width)); // Top border

        // Draw each row of the board
        for (int y = 0; y < height; y++) {
            System.out.printf("%2d ", y); // Row labels (y-axis)
            for (int x = 0; x < width; x++) {
                boolean found = false;
                // Check if a tile exists at the current position
                for (Map.Entry<Tile, Point> entry : positions.entrySet()) {
                    Point point = entry.getValue();
                    if (point.x == x && point.y == y) {
                        Tile tile = entry.getKey();
                        Animal animal = animalSet.get(tile);

                        // Get the first letter of the landscape
                        String landscapeInitial = tile.landscape().substring(0, 1).toUpperCase();

                        // Get the first letter of the animal (if any)
                        String animalInitial = (animal != null) ? animal.name().substring(0, 1).toUpperCase() : " ";

                        // Display the tile and animal
                        System.out.printf("| %s%s  ", landscapeInitial, animalInitial);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.print("|     "); // Empty space if no tile is placed
                }
            }
            System.out.println("|");
            System.out.println("   +" + "-----+".repeat(width)); // Row divider
        }
    }

    
    /**
     * Allows players to select and place their initial 3 tiles on the board.
     * <p>
     * Players are presented with five landscape options to choose from. Once selected, 
     * the corresponding tile is removed from the initial tile pool. Players then place the tile 
     * on their board, ensuring it's placed adjacent to existing tiles or as the first tile.
     * </p>
     */
    private void setupInitialTiles() {
        System.out.println("\nPlayers are selecting their initial tiles...");

        for (Player player : gameData.getPlayerList()) {
            System.out.println(player.getName() + ", please select your initial 3 tiles:");
            drawPlayerBoard(player);

            for (int i = 0; i < 3; i++) {
                // Display landscape options
                String[] landscapes = gameData.getLandscapes();
                System.out.println("Available landscape types:");
                for (int j = 0; j < landscapes.length; j++) {
                    System.out.println((j + 1) + ". " + landscapes[j]);
                }

                // Player selects a landscape option
                int choice;
                while (true) {
                    System.out.print("Please select a landscape (enter a number between 1 and " + landscapes.length + "): ");
                    choice = scanner.nextInt();
                    if (choice >= 1 && choice <= landscapes.length) {
                        break; // Valid input
                    } else {
                        System.out.println("Invalid input, please try again!");
                    }
                }

                // Draw a tile of the chosen landscape from the initial tile pool
                String selectedLandscape = landscapes[choice - 1];
                Tile selectedTile = gameData.removeFirstTileByLandscape(selectedLandscape);

                if (selectedTile == null) {
                    System.out.println("No more tiles of this landscape type. Please choose again!");
                    i--; // Repeat this step
                    continue;
                }

                // Player chooses where to place the tile
                System.out.print("Enter the x-coordinate to place the tile: ");
                int x = scanner.nextInt();
                System.out.print("Enter the y-coordinate to place the tile: ");
                int y = scanner.nextInt();

                // Validate placement position
                if (!player.canPlaceTile(x, y)) {
                    System.out.println("Invalid position! Tiles must be placed adjacent to existing tiles or as the first tile.");
                    i--; // Repeat this step
                    continue;
                }

                // Place the tile on the player's board
                player.placeTile(selectedTile, x, y);
                drawPlayerBoard(player);

                System.out.println("Tile successfully placed!");
            }
        }
    }



    /**
     * Manages the main game loop, handling player turns and game progression.
     *
     * @throws IllegalStateException if the game cannot continue due to empty tile or animal pools.
     */
    private void gameRound() {
        System.out.println("\nThe game has officially started...");

        // Initialize the tile pool and animal token pool
        HashMap<Tile, Animal> currentCombinations = new HashMap<>(); // Current available combinations
        initializeCombinations(currentCombinations);

        // Initialize round count for each player
        int maxRounds = 20; // Maximum rounds per player
        HashMap<Player, Integer> playerRounds = new HashMap<>();
        for (Player player : gameData.getPlayerList()) {
            playerRounds.put(player, 0); // Initialize round count to 0
        }

        // Main game loop
        while (true) {
            Player currentPlayer = gameData.getCurrentPlayer();
            int currentPlayerRounds = playerRounds.get(currentPlayer);

            // Check if all players have completed the maximum rounds
            if (currentPlayerRounds == maxRounds && isGameOver(playerRounds, maxRounds)) {
                System.out.println("\nAll players have completed 20 rounds. The game is over!");
                break;
            }

            System.out.println("\nCurrent player: " + currentPlayer.getName());
            System.out.println("Current round: " + (currentPlayerRounds + 1));

            drawPlayerBoard(currentPlayer);

            // Display current tile-animal combinations
            System.out.println("Current available combinations:");
            int index = 1;
            Map<Integer, Tile> indexToTileMap = new HashMap<>();
            for (Map.Entry<Tile, Animal> entry : currentCombinations.entrySet()) {
                System.out.printf("%d. Landscape: %s, Animal: %s%n", index,
                    entry.getKey().landscape(), entry.getValue().name());
                indexToTileMap.put(index, entry.getKey());
                index++;
            }

            // Player selects a combination
            int choice;
            while (true) {
                System.out.print("Please select a combination (enter a number between 1-4): ");
                choice = scanner.nextInt();
                if (indexToTileMap.containsKey(choice)) {
                    break;
                } else {
                    System.out.println("Invalid input, please try again!");
                }
            }

            // Get the selected tile and animal
            Tile selectedTile = indexToTileMap.get(choice);
            Animal selectedAnimal = currentCombinations.remove(selectedTile);

            while (true) {
                // Player places the tile
                System.out.print("Enter the x-coordinate to place the tile: ");
                int x = scanner.nextInt();
                System.out.print("Enter the y-coordinate to place the tile: ");
                int y = scanner.nextInt();
                currentPlayer.placeTile(selectedTile, x, y);

                // Check if the placement is valid
                if (currentPlayer.canPlaceTile(x, y)) {
                    System.out.println("Invalid position! The tile must be placed adjacent to existing tiles. Please enter again.");
                    continue;
                } else {
                    break;
                }
            }

            drawPlayerBoard(currentPlayer);

            // Player chooses whether to place the animal token
            System.out.print("Do you want to place the animal token? (Enter 1 to place, 0 to skip): ");
            int placeAnimal = scanner.nextInt();

            if (placeAnimal == 1) {
                // Check if the animal token can be placed on any tile
                if (!gameData.canPlaceAnimalOnBoard(currentPlayer, selectedAnimal)) {
                    System.out.println("No valid tiles available to place this animal token!");

                    // Automatically return the animal token to the pool
                    gameData.addAnimalToPool(selectedAnimal);
                    gameData.ChuffleAnimalPool();
                } else {
                    while (true) {
                        // Allow the player to select a target tile for the animal token
                        System.out.print("Enter the x-coordinate to place the animal token: ");
                        int targetX = scanner.nextInt();
                        System.out.print("Enter the y-coordinate to place the animal token: ");
                        int targetY = scanner.nextInt();

                        // Find the target tile
                        Tile targetTile = gameData.findTileByPosition(currentPlayer, targetX, targetY);
                        if (targetTile == null) {
                            System.out.println("No tile found at the target position, please choose again!");
                        } else if (gameData.isTileplaced(targetTile)) {
                            System.out.println("This tile already has an animal token. You can't place another one!");
                        } else if (!gameData.isAnimalValidForTile(targetTile, selectedAnimal)) {
                            System.out.println("The animal token doesn't match the tile's landscape requirement. Please choose again!");
                        } else {
                            // Place the animal token
                            currentPlayer.addAnimalToTile(targetTile, selectedAnimal);
                            drawPlayerBoard(currentPlayer);
                            System.out.println("Animal token placed successfully!");
                            break;
                        }
                    }
                }
            } else if (placeAnimal == 0) {
                // Player chooses not to place the animal token
                System.out.println("Animal token not placed. Returning it to the animal pool...");
                gameData.addAnimalToPool(selectedAnimal);
                gameData.ChuffleAnimalPool();
            } else {
                // Handle invalid input
                System.out.println("Invalid input. Please enter 1 or 0!");
            }

            // Refill the combinations pool
            refillCombinations(currentCombinations);

            // Update the current player's round count
            playerRounds.put(currentPlayer, currentPlayerRounds + 1);

            // Switch to the next player
            gameData.changerCurrentPlayer();
        }
    }


    
    /**
     * Checks if the game is over by verifying if all players have completed the maximum number of rounds.
     *
     * @param playerRounds A map containing each player and their corresponding number of completed rounds.
     * @param maxRounds The maximum number of rounds allowed per player.
     * @return {@code true} if all players have completed the maximum rounds; {@code false} otherwise.
     */
    private boolean isGameOver(Map<Player, Integer> playerRounds, int maxRounds) {
        for (int rounds : playerRounds.values()) {
            if (rounds < maxRounds) {
                return false; // If any player hasn't completed the maximum rounds, the game is not over
            }
        }
        return true; // All players have completed the maximum rounds, the game is over
    }
    

    /**
     * Ends the game and calculates the final scores for each player.
     *
     * @param scoreCalculator The scoring strategy for unified scoring modes (family or intermediate mode).
     * @param animalScoringMap The map of animal names to their specific scoring strategies (used in animal scoring card mode).
     * @param gameMode The current game mode (1: Family, 2: Intermediate, 3: Animal Scoring Card).
     */
    private void endGame(ScoreCalculate scoreCalculator, Map<String, ScoreCalculate> animalScoringMap, int gameMode) {
        System.out.println("The game is over! Now calculating the final scores...");

        for (Player player : gameData.getPlayerList()) {
            System.out.println("\n" + player.getName() + "'s final board:");
            drawPlayerBoard(player);

            int totalScore = 0;

            if (gameMode == 1 || gameMode == 2) {
                // Family mode or Intermediate mode: Unified scoring method
                totalScore = scoreCalculator.calculateScore(player);
                System.out.println(player.getName() + "'s total score: " + totalScore);
            } else if (gameMode == 3) {
                // Animal Scoring Card mode: Calculate scores for each animal separately
                for (Map.Entry<String, ScoreCalculate> entry : animalScoringMap.entrySet()) {
                    String animal = entry.getKey();               // Animal name (Bear, Deer, Fox, Salmon, Eagle)
                    ScoreCalculate calculator = entry.getValue(); // Corresponding scoring strategy

                    int animalScore = calculator.calculateScore(player);
                    System.out.println(player.getName() + "'s score for " + animal + ": " + animalScore);

                    totalScore += animalScore; // Accumulate each animal's score
                }

                System.out.println(player.getName() + "'s total score: " + totalScore);
            }

            // Call the function to determine the winner(s)
            List<Player> winners = determineWinners(gameData.getPlayerList(), scoreCalculator, animalScoringMap, gameMode);

            // Display the winner(s)
            if (winners.size() == 1) {
                System.out.println("The game is over! The winner is: " + winners.get(0).getName() + "! Congratulations!");
            } else {
                System.out.print("The game is over! The winners are: ");
                for (int i = 0; i < winners.size(); i++) {
                    System.out.print(winners.get(i).getName());
                    if (i != winners.size() - 1) {
                        System.out.print(" and ");
                    }
                }
                System.out.println("Congratulations!");
            }
        }
    }

    /**
     * Determines the winner(s) based on the highest total score.
     * Supports multiple winners in case of a tie.
     *
     * @param playerList List of players.
     * @param scoreCalculator Scoring method for family or intermediate modes.
     * @param animalScoringMap Scoring rules for animal scoring mode.
     * @param gameMode Game mode (1: Family, 2: Intermediate, 3: Animal Scoring).
     * @return List of players with the highest score.
     */
    private List<Player> determineWinners(List<Player> playerList, ScoreCalculate scoreCalculator,
                                          Map<String, ScoreCalculate> animalScoringMap, int gameMode) {
        ArrayList<Player> winners = new ArrayList<>();
        int highestScore = -1;

        for (Player player : playerList) {
            int totalScore = 0;

            if (gameMode == 1 || gameMode == 2) {
                // Unified scoring for family or intermediate mode
                totalScore = scoreCalculator.calculateScore(player);
            } else if (gameMode == 3) {
                // Animal scoring mode: calculate score for each animal
                for (Map.Entry<String, ScoreCalculate> entry : animalScoringMap.entrySet()) {
                    totalScore += entry.getValue().calculateScore(player);
                }
            }

            // Determine highest scoring player(s)
            if (totalScore > highestScore) {
                highestScore = totalScore;
                winners.clear();
                winners.add(player);
            } else if (totalScore == highestScore) {
                winners.add(player);
            }
        }

        return winners;
    }

    /**
     * Prompts the user to select the number of players (2-4).
     *
     * @return Number of players selected.
     */
    private int chooseNumberOfPlayers() {
        int numPlayers;

        while (true) {
            System.out.print("Please select the number of players (2-4): ");
            numPlayers = scanner.nextInt();

            if (numPlayers >= 2 && numPlayers <= 4) {
                break;  // Valid input
            } else {
                System.out.println("Invalid input! Please enter a number between 2 and 4.");
            }
        }

        return numPlayers;
    }

    /**
     * Allows the player to select a scoring method (A/B/C/D) for each animal.
     *
     * @return A map of animals and their selected scoring methods.
     */
    private Map<String, ScoreCalculate> chooseAnimalScoringCards() {
        Map<String, ScoreCalculate> animalScoringMap = new HashMap<>();
        String[] animals = {"Bear", "Deer", "Fox", "Salmon", "Eagle"};

        for (String animal : animals) {
            System.out.println("Please choose the scoring method for " + animal + ":");
            System.out.println("A. Scoring Method A");
            System.out.println("B. Scoring Method B");
            System.out.println("C. Scoring Method C");
            System.out.println("D. Scoring Method D");

            char choice;
            while (true) {
                System.out.print("Please enter A/B/C/D: ");
                choice = scanner.next().toUpperCase().charAt(0);
                if (choice == 'A' || choice == 'B' || choice == 'C' || choice == 'D') {
                    break;
                } else {
                    System.out.println("Invalid input! Please enter A/B/C/D.");
                }
            }

            // Assign scoring strategy to the selected animal
            animalScoringMap.put(animal, getScoringStrategy(animal, choice));
        }

        return animalScoringMap;
    }

    /**
     * Returns the scoring strategy based on the selected animal and scoring method.
     *
     * @param animal The animal name.
     * @param choice The scoring method selected (A/B/C/D).
     * @return The corresponding scoring strategy.
     */
    private ScoreCalculate getScoringStrategy(String animal, char choice) {
        switch (animal) {
            case "Bear":
                return switch (choice) {
                    case 'A' -> new BearA();
                    case 'B' -> new BearB();
                    case 'C' -> new BearC();
                    case 'D' -> new BearD();
                    default -> null;
                };
            case "Deer":
                return switch (choice) {
                    case 'A' -> new DeerA();
                    case 'B' -> new DeerB();
                    case 'C' -> new DeerC();
                    case 'D' -> new DeerD();
                    default -> null;
                };
            case "Fox":
                return switch (choice) {
                    case 'A' -> new FoxA();
                    case 'B' -> new FoxB();
                    case 'C' -> new FoxC();
                    case 'D' -> new FoxD();
                    default -> null;
                };
            case "Salmon":
                return switch (choice) {
                    case 'A' -> new SalmonA();
                    case 'B' -> new SalmonB();
                    case 'C' -> new SalmonC();
                    case 'D' -> new SalmonD();
                    default -> null;
                };
            case "Eagle":
                return switch (choice) {
                    case 'A' -> new EagleA();
                    case 'B' -> new EagleB();
                    case 'C' -> new EagleC();
                    case 'D' -> new EagleD();
                    default -> null;
                };
            default:
                return null;
        }
    }

    
    
    
//*********************************************************************game logical
    
    /**
     * Allows the player to select the game type: Solo or Multiplayer.
     *
     * @return 1 for Solo Mode, 2 for Multiplayer Mode.
     */
    private int chooseGameType() {
        System.out.println("Please choose the game type:");
        System.out.println("1. Solo Mode");
        System.out.println("2. Multiplayer Mode");

        int choice;
        while (true) {
            System.out.print("Enter a number to select the mode (1 or 2): ");
            choice = scanner.nextInt();
            if (choice == 1 || choice == 2) {
                break;
            } else {
                System.out.println("Invalid input! Please enter 1 or 2.");
            }
        }
        return choice;
    }

    /**
     * Allows the player to select the scoring mode: Family, Intermediate, or Single Animal Scoring.
     *
     * @return 1 for Family Mode, 2 for Intermediate Mode, 3 for Single Animal Scoring Mode.
     */
    private int chooseScoringMode() {
        System.out.println("Please choose the scoring mode:");
        System.out.println("1. Family Mode");
        System.out.println("2. Intermediate Mode");
        System.out.println("3. Single Animal Scoring Card Mode");

        int choice;
        while (true) {
            System.out.print("Enter a number to select the mode (1/2/3): ");
            choice = scanner.nextInt();
            if (choice >= 1 && choice <= 3) {
                break;
            } else {
                System.out.println("Invalid input! Please enter 1, 2, or 3.");
            }
        }
        return choice;
    }

    /**
     * Starts the game based on the selected game type and scoring mode.
     * Initializes resources and runs the game loop.
     */
    public void startGame() {
        System.out.println("Welcome to the Cascadia Game!");

        // 1. Player selects the game type (Solo or Multiplayer)
        int gameType = chooseGameType();

        // 2. Player selects the scoring mode
        int scoringMode = chooseScoringMode();

        // 3. Solo Mode
        if (gameType == 1) {
            System.out.println("You have chosen Solo Mode!");
            GameControlSolo soloGame = new GameControlSolo();

            // Apply scoring rules based on selected mode
            if (scoringMode == 1) {
                System.out.println("You have selected Family Scoring Mode!");
                soloGame.startGame(new FamilyScore());
            } else if (scoringMode == 2) {
                System.out.println("You have selected Intermediate Scoring Mode!");
                soloGame.startGame(new MediumScore());
            } else {
                System.out.println("You have selected Single Animal Scoring Card Mode!");
                Map<String, ScoreCalculate> animalScoringMap = chooseAnimalScoringCards();
                soloGame.startGame(animalScoringMap);
            }
        }

        // 4. Multiplayer Mode
        else {
            System.out.println("You have chosen Multiplayer Mode!");

            int numPlayers = chooseNumberOfPlayers();
            gameData.setplayernumber(numPlayers);
            gameData.createplayer();

            // Define scoring strategy
            ScoreCalculate scoreCalculator = null;
            Map<String, ScoreCalculate> animalScoringMap = null;

            if (scoringMode == 1) {
                System.out.println("You have selected Family Scoring Mode!");
                scoreCalculator = new FamilyScore();
            } else if (scoringMode == 2) {
                System.out.println("You have selected Intermediate Scoring Mode!");
                scoreCalculator = new MediumScore();
            } else {
                System.out.println("You have selected Single Animal Scoring Card Mode!");
                animalScoringMap = chooseAnimalScoringCards();
            }

            // Initialize game resources
            initializeTilePool();
            initializeInitialTilePool();
            initializeAnimalPool();
            setupInitialTiles();

            // Start the game loop
            gameRound();

            // End game and calculate scores
            if (scoringMode == 1 || scoringMode == 2) {
                endGame(scoreCalculator, null, scoringMode);
            } else {
                endGame(null, animalScoringMap, scoringMode);
            }
        }

        scanner.close();
    }

}

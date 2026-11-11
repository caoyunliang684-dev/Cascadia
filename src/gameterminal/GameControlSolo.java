package gameterminal;

import game.Tile;
import score.ScoreCalculate;
import game.Animal;
import game.Player;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;



/**
 * Game controller for Solo Mode in Cascadia.
 * Handles game flow, resource initialization, and scoring in single-player mode.
 */
public class GameControlSolo extends GameControl {

    private final GameData gameData;  // Game data
    private final Scanner scanner;    // User input

    /**
     * Constructs the Solo Mode game controller and initializes game data and input.
     */
    public GameControlSolo() {
        this.gameData = new GameData();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the Solo Mode game using standard scoring rules.
     *
     * @param scoreCalculator The scoring strategy used for calculating the final score.
     */
    public void startGame(ScoreCalculate scoreCalculator) {
        System.out.println("Welcome to Cascadia Solo Mode!");

        // Initialize game resources
        initializeTilePool();
        initializeInitialTilePool();
        initializeAnimalPool();

        // Set up the single player
        gameData.setplayernumber(1);
        gameData.createplayer();

        // Player selects and places initial tiles
        setupInitialTiles();

        // Start the game flow
        soloGameRound();

        // End the game and calculate the score
        endGame(scoreCalculator);

        scanner.close();
    }

    /**
     * Starts the Solo Mode game using the Animal Scoring Card mode.
     *
     * @param animalScoringMap The scoring strategies for each animal type.
     */
    public void startGame(Map<String, ScoreCalculate> animalScoringMap) {
        System.out.println("Welcome to Cascadia Solo Mode (Animal Scoring Card Mode)!");

        // Initialize game resources
        initializeTilePool();
        initializeInitialTilePool();
        initializeAnimalPool();

        // Set up the single player
        gameData.setplayernumber(1);
        gameData.createplayer();

        // Player selects and places initial tiles
        setupInitialTiles();

        // Start the game flow
        soloGameRound();

        // End the game and calculate the score
        endGame(animalScoringMap);

        scanner.close();
    }


    /**
     * Executes the main game loop for Solo Mode.
     * Handles tile and animal token placement, combination management, and round progression.
     */
    private void soloGameRound() {
        System.out.println("\nStarting Solo Mode Game...");

        // Use LinkedHashMap to maintain the order of combinations
        LinkedHashMap<Tile, Animal> currentCombinations = new LinkedHashMap<>();
        initializeCombinations(currentCombinations);  // Initialize 4 combinations

        Player player = gameData.getPlayerList().get(0);  // Only one player in Solo Mode

        int maxRounds = 20;  // Maximum number of rounds

        for (int round = 1; round <= maxRounds; round++) {
            System.out.println("\n===== Round " + round + " / " + maxRounds + " =====");
            drawPlayerBoard(player);

            // Show current available combinations
            System.out.println("Current Available Combinations:");
            displayCombinations(currentCombinations);

            // Player selects a combination
            int choice = selectCombination(currentCombinations);

            // Get the selected tile and animal
            Tile selectedTile = getTileByChoice(currentCombinations, choice);
            Animal selectedAnimal = currentCombinations.remove(selectedTile);

            // Player places the selected tile
            while (true) {
                System.out.print("Enter the x-coordinate to place the tile: ");
                int x = scanner.nextInt();
                System.out.print("Enter the y-coordinate to place the tile: ");
                int y = scanner.nextInt();

                if (player.canPlaceTile(x, y)) {
                    player.placeTile(selectedTile, x, y);
                    drawPlayerBoard(player);
                    System.out.println("Tile placed successfully!");
                    break;
                } else {
                    System.out.println("Invalid position! Please try again.");
                }
            }

            // Player decides whether to place the animal token
            System.out.print("Do you want to place the animal token? (1-Yes, 0-No): ");
            int placeAnimal = scanner.nextInt();

            if (placeAnimal == 1) {
                // Check if the animal token can be placed on a tile
                if (!gameData.canPlaceAnimalOnBoard(player, selectedAnimal)) {
                    System.out.println("Cannot place the animal token, returning it to the animal pool...");
                    gameData.addAnimalToPool(selectedAnimal);
                    gameData.ChuffleAnimalPool();
                } else {
                    while (true) {
                        System.out.print("Enter the x-coordinate to place the animal token: ");
                        int targetX = scanner.nextInt();
                        System.out.print("Enter the y-coordinate to place the animal token: ");
                        int targetY = scanner.nextInt();

                        Tile targetTile = gameData.findTileByPosition(player, targetX, targetY);
                        if (targetTile != null && !gameData.isTileplaced(targetTile) &&
                                gameData.isAnimalValidForTile(targetTile, selectedAnimal)) {
                            player.addAnimalToTile(targetTile, selectedAnimal);
                            drawPlayerBoard(player);
                            System.out.println("Animal token placed successfully!");
                            break;
                        } else {
                            System.out.println("Cannot place the animal token here, please choose again.");
                        }
                    }
                }
            } else {
                System.out.println("Skipped placing the animal token, returning it to the animal pool...");
                gameData.addAnimalToPool(selectedAnimal);
                gameData.ChuffleAnimalPool();
            }

            // Solo Mode logic: Remove the rightmost combination
            removeRightmostCombination(currentCombinations);

            // Refill with two new combinations at the front
            refillSoloCombinations(currentCombinations);
            System.out.println("The current combinations have been updated!");
        }

        // End of the game, start scoring
        System.out.println("Congratulations on completing all rounds! The game is over, starting the scoring...");
    }

    
    
    /**
     * Ends the game in Solo Mode and calculates the total score for Family or Intermediate mode.
     *
     * @param scoreCalculator The scoring strategy used for calculating the total score.
     */
    private void endGame(ScoreCalculate scoreCalculator) {
        System.out.println("\nGame Over! Now calculating the score...");

        Player player = gameData.getPlayerList().get(0);  // Solo mode has only one player
        drawPlayerBoard(player);  // Display the player's final board

        int totalScore = scoreCalculator.calculateScore(player);  // Calculate total score
        System.out.println(player.getName() + "'s Total Score: " + totalScore);
    }

    /**
     * Ends the game in Solo Mode and calculates the total score using individual animal scoring cards.
     *
     * @param animalScoringMap A map linking animal types to their scoring strategies.
     */
    private void endGame(Map<String, ScoreCalculate> animalScoringMap) {
        System.out.println("\nGame Over! Now calculating the score...");

        Player player = gameData.getPlayerList().get(0);  // Solo mode has only one player
        drawPlayerBoard(player);  // Display the player's final board

        int totalScore = 0;

        // Calculate the score for each animal type
        for (Map.Entry<String, ScoreCalculate> entry : animalScoringMap.entrySet()) {
            String animal = entry.getKey();
            ScoreCalculate calculator = entry.getValue();

            int animalScore = calculator.calculateScore(player);
            System.out.println(player.getName() + "'s " + animal + " Score: " + animalScore);

            totalScore += animalScore;  // Sum up the total score
        }

        System.out.println(player.getName() + "'s Total Score: " + totalScore);
    }



    
    // ==================== the special method for solo ====================*********************

    
    
    /**
     * Removes the rightmost combination from the current combinations without returning it to the pool.
     * The remaining combinations are shifted to the end of the list.
     *
     * @param currentCombinations The current set of tile-animal combinations.
     */
    private void removeRightmostCombination(LinkedHashMap<Tile, Animal> currentCombinations) {
        if (!currentCombinations.isEmpty()) {
            // 1. Remove the rightmost combination (the last one)
            Tile rightmostTile = null;
            for (Tile tile : currentCombinations.keySet()) {
                rightmostTile = tile;
            }
            if (rightmostTile != null) {
                currentCombinations.remove(rightmostTile);
                System.out.println("The rightmost combination has been removed!");
            }

            // 2. Shift the remaining combinations to the end (3rd and 4th positions)
            LinkedHashMap<Tile, Animal> shiftedCombinations = new LinkedHashMap<>();
            for (Map.Entry<Tile, Animal> entry : currentCombinations.entrySet()) {
                shiftedCombinations.put(entry.getKey(), entry.getValue());
            }

            // Clear the original combinations and wait for new combinations to be added
            currentCombinations.clear();

            // Temporarily store and insert remaining combinations after new ones
            currentCombinations.putAll(shiftedCombinations);
        }
    }

    /**
     * Refills the current combinations with two new combinations added to the front.
     *
     * @param currentCombinations The current set of tile-animal combinations.
     */
    private void refillSoloCombinations(LinkedHashMap<Tile, Animal> currentCombinations) {
        LinkedHashMap<Tile, Animal> updatedCombinations = new LinkedHashMap<>();

        // 1. Add two new combinations to the front (1st and 2nd positions)
        while (updatedCombinations.size() < 2) {
            Map.Entry<Tile, Animal> newCombination = gameData.generateNewCombination();
            if (newCombination != null) {
                updatedCombinations.put(newCombination.getKey(), newCombination.getValue());
                System.out.println("Added new combination: " + newCombination.getKey().landscape() + " - " + newCombination.getValue().name());
            } else {
                System.out.println("Failed to add new combination, resources are exhausted!");
                break;
            }
        }

        // 2. Move the remaining combinations to the back (3rd and 4th positions)
        updatedCombinations.putAll(currentCombinations);

        // 3. Update the combination pool
        currentCombinations.clear();
        currentCombinations.putAll(updatedCombinations);
    }

    /**
     * Displays the current available tile-animal combinations for selection.
     *
     * @param combinations The current set of tile-animal combinations.
     */
    private void displayCombinations(LinkedHashMap<Tile, Animal> combinations) {
        int index = 1;
        for (Map.Entry<Tile, Animal> entry : combinations.entrySet()) {
            System.out.printf("%d. Landscape: %s, Animal: %s%n", index++, entry.getKey().landscape(), entry.getValue().name());
        }
    }

    /**
     * Allows the player to select a combination from the current options.
     *
     * @param combinations The current set of tile-animal combinations.
     * @return The selected combination index.
     */
    private int selectCombination(LinkedHashMap<Tile, Animal> combinations) {
        int choice;
        while (true) {
            System.out.print("Please select a combination (1-4): ");
            choice = scanner.nextInt();
            if (choice >= 1 && choice <= combinations.size()) {
                break;
            } else {
                System.out.println("Invalid input, please select again!");
            }
        }
        return choice;
    }

    /**
     * Retrieves a tile based on the player's selection.
     *
     * @param combinations The current set of tile-animal combinations.
     * @param choice The player's selected combination index.
     * @return The selected tile or null if not found.
     */
    private Tile getTileByChoice(LinkedHashMap<Tile, Animal> combinations, int choice) {
        int index = 1;
        for (Tile tile : combinations.keySet()) {
            if (index == choice) {
                return tile;
            }
            index++;
        }
        return null;
    }

    
   
    
    //***************************************************************************方法复用
    
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


}



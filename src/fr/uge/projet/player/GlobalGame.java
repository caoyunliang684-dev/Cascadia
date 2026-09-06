package fr.uge.projet.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.uge.projet.player.Player;
import fr.uge.projet.utils.Bloc;
import fr.uge.projet.utils.Coordinate;
import fr.uge.projet.utils.FourPairs;
import fr.uge.projet.utils.Pair;
import fr.uge.projet.utils.PlayerInformation;
import fr.uge.projet.utils.TotalAnimal;
import fr.uge.projet.utils.TotalTile;

public class GlobalGame {
  private final ArrayList<PlayerInformation> players;
  private final TotalAnimal totalAnimal;
  private final TotalTile totalTile;
  private final FourPairs fourPairs;
  private int round = 20;
  
  public GlobalGame() {
    players = new ArrayList<>();
    totalAnimal = new TotalAnimal();
    totalTile = new TotalTile();
    fourPairs = new FourPairs();
  }
  
  // initialisation de jeux
  // création des joueurs 
  /**
   * create two players and display initialise the four options
   *
   */
  public void startGame() {
	var player1 = new PlayerInformation();
	player1.initPlacement(totalTile);
	var player2 = new PlayerInformation(); 
	player2.initPlacement(totalTile);
	
	fourPairs.init(totalAnimal, totalTile);
	players.add(player1);
	players.add(player2);
	
	
  }
  
  // Demande de valeur dans la ligne de commande
  private String askValueInPrompt() throws IOException {
	var reader = new BufferedReader(new InputStreamReader(System.in));
    var input = reader.readLine();
    return input;
  }
  
  //liste de valeur de coordonnée
  private Coordinate askCoordonate() throws IOException {
	var prompt = askValueInPrompt();
	Objects.requireNonNull(prompt);
	var array = prompt.split(" ");
	var numberFirst = Integer.parseInt(array[0]);
	var numberSecond = Integer.parseInt(array[1]);
	return new Coordinate(numberFirst, numberSecond);
  }
  
  //Affichage de joueur et son écosystème
  private void showPlayer(Player player) {
    System.out.println(player);
    player.getPlacement().display();  
  }
  
  //Choix de pair sur le plateau 
  private Pair choosePairInTray() throws NumberFormatException, IOException {
	System.out.println("faire ton choix,commence par 1,1");
	var number = Integer.parseInt(askValueInPrompt());
	var pair = fourPairs.getPair(number);
	fourPairs.addPair(totalAnimal.getAnimal(), totalTile.getTile());
	return pair;
  }
  
  //Ajout de tuile sur le placement
  private void putTileInPlacement(PlayerInformation player,Pair pair) throws IOException {
	Coordinate cordonnate;
    do {
      System.out.println("faire ton choix de tuile");
      cordonnate = askCoordonate();
    }while(player.isExistCordonnate(cordonnate)|| !player.isPlacedAttachedTilesExist(cordonnate));
    player.addNewTile(cordonnate, new Bloc(pair.tile()));
  }
  
  //Ajout d'animal sur placement
  private void putAnimalInPlacement(PlayerInformation player, Pair pair) throws IOException {
	System.out.println("Voulez vous placer ou pas? y/n");
	var input = askValueInPrompt();
	if(input.equals("n")) {
	  totalAnimal.putBackAnimal(pair.animal());
	}else {
	  Coordinate cordonnate;
	  do {
		  System.out.println("Faire Placer ton choix de animal");
		  cordonnate = askCoordonate();
	  }while(!player.isExistCordonnate(cordonnate)||player.isExistAnimal(cordonnate)||! player.acceptOrNotThisAnimal(cordonnate, pair.animal()));
	  player.addNewAnimal(cordonnate, pair.animal());
	}
  }
  /**
   * start the game 
   *
   * @throws NumberFormatException IOException
   */
  public void mainGame() throws NumberFormatException, IOException {
    while(round > 0) {
      
      int i = 1;
      for(var player : players) {
        fourPairs.display();
        System.out.println("Player "+i);
        player.display();
        var pair = choosePairInTray();
        putTileInPlacement(player, pair);
        //partie animal commence
        putAnimalInPlacement(player, pair);
        i++;
        System.out.println(player.socreAnimal());
        System.out.println(player.socreTotalTuile());
      }
      round --;
    }
  }
  
}

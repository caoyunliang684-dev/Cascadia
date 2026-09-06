package fr.uge.projet.main;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.github.forax.zen.Application;

import fr.uge.projet.player.GameTerminal;
import fr.uge.projet.score.VariantType;
import fr.uge.projet.zen.GameController;
import fr.uge.projet.zen.GameData;
import fr.uge.projet.zen.GameViewer;
import fr.uge.projet.zen.ShowType;

public class Main {
 
  private static void showInitialGame() {
    System.out.println("================= Bienvenue au jeu Cascadia ================");
    System.out.println("                  -------- Tapez-----------                  ");
    System.out.println("                1 => Jeu sur Terminal");
    System.out.println("                2 => Jeu sur Graphique Carré");
    System.out.println("                3 => Jeu sur Graphique Hexagone");
    System.out.println("                  --------------------------                 ");
  }
  private static void showChoiceScoreGame() {
    System.out.println("     -------- Choix de décompte de Score , Tapez-----------");
    System.out.println("                1 => Variante Familiale");
    System.out.println("                2 => variante Intermédiaire");
    System.out.println("                3 => Carte de décompte de A-B-C-D");
    System.out.println("                  --------------------------                 ");
  }
  private static String askValuePrompt() throws IOException {
    var reader = new BufferedReader(new InputStreamReader(System.in));
	var input = reader.readLine();
	return input;
  }
  
  private static String optionCard(int option) throws IOException {
   if (option == 3) {
	  System.out.println("Choisir type de carte : Répondre A ou B ou C ou D");
      var configCard = askValuePrompt();
      return configCard;
    }
    return " ";
  }
  
  private static VariantType valueVariantType(int option) {
	 
	 return switch (option) {
		case 1 -> VariantType.FAMILY;
		case 2 -> VariantType.INTERMEDIATE;
		case 3 -> VariantType.CARD;
		default -> throw new IllegalArgumentException("Unexpected value: " + option);
	  };
  }
  public static void main(String[] args) throws NumberFormatException, IOException {
	  
	  // choice graphic's game 
	  showInitialGame();
	  var choice = Integer.parseInt(askValuePrompt());
	  
	  //number of player
	  System.out.println(" Entrer le nombre de joueur : ");
	  var numberOfPlayers = Integer.parseInt(askValuePrompt());
	  
	  
	  //  choice score decompte
	  showChoiceScoreGame();
	 var option = Integer.parseInt(askValuePrompt());
	  var variantType = valueVariantType(option);
	  var configCard= optionCard(option);
	  
	  
	  if(choice == 1) {
		  var game = new GameTerminal(numberOfPlayers);
		  game.startGame();
	      game.mainGame(variantType, configCard);
	  }else if (choice == 2) {
		   Color lightCyan = new Color(224, 255, 255);

		
		    Application.run(lightCyan, context -> {

		        var screenInfo = context.getScreenInfo();
		        int width = screenInfo.width();
		        int height = screenInfo.height();

		      
		  
		        GameData data = new GameData(numberOfPlayers, width, height,ShowType.SQUARE,variantType,configCard);

		
		        GameViewer viewer = new GameViewer();
		         

		        GameController.gameLoop(context, viewer, data, width, height, numberOfPlayers,ShowType.SQUARE); 
		    });
	  }else if (choice == 3) {
		  
		   Color lightCyan = new Color(224, 255, 255);


		    Application.run(lightCyan, context -> {
	
		        var screenInfo = context.getScreenInfo();
		        int width = screenInfo.width();
		        int height = screenInfo.height();


		        GameData data = new GameData(numberOfPlayers, width, height,ShowType.HEXAGON,variantType,configCard);


		        GameViewer viewer = new GameViewer();
		         

		        GameController.gameLoop(context, viewer, data, width, height, numberOfPlayers,ShowType.HEXAGON);
		    });
	  }else {
		  System.out.println(" Erreur de choix !!!");
	  }	 
   }
}
    
    
  



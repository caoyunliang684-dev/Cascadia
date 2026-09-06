package fr.uge.projet.zen;

import java.awt.Color;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;
import com.github.forax.zen.KeyboardEvent.Action;

import fr.uge.projet.utils.Coordinate;
import fr.uge.projet.utils.Pair;



public class GameController {
  
  
  

  /**
   * Main game loop that handles user interactions and game updates.
   *
   * @param context          the application context for rendering; must not be {@code null}.
   * @param viewer           the game viewer for rendering the game; must not be {@code null}.
   * @param data             the game data containing the current game state; must not be {@code null}.
   * @param width            the width of the rendering area.
   * @param height           the height of the rendering area.
   * @param nomberOfPlayers  the number of players in the game.
   * @param type             the shape type used in the game (e.g., SQUARE, HEXAGON); must not be {@code null}.
   * @throws NullPointerException if {@code context}, {@code viewer}, {@code data}, or {@code type} is {@code null}.
   */
  public static void gameLoop(ApplicationContext context, GameViewer viewer, GameData data,int width, int height ,int nomberOfPlayers,ShowType type){
    Circle animalChoisedCircle = null;//Si on a choisi un animal
    Square tileChoisedSquare = null;//Si on a choisi un jeton nature
    int tourAnimalOrTile = 1;// 1-> tile, 2->animal
    int indexPlayer = 0; // 0 1 2 3 
   
    boolean evenHandled = false;
    var counter = 0;
    int buttonX = width - 120; 
    int buttonY = 20; 
    int buttonWidth = 100;
    int buttonHeight = 40;

    int releaseButtonX = width - 120; 
    int releaseButtonY = 70;       
    String hint = "Game Start! There are " + nomberOfPlayers + " Players";
    
    
    GameViewer.initialisationGame(context, width, height, data.getListSquares(),data.getListCircle() );
    while(true) {
      
      var event = context.pollOrWaitEvent(10);
      if (event == null) {
        evenHandled = false;
        continue;
      }
      if(evenHandled) {
        continue;
      }
      switch (event) {
      case PointerEvent e:
        if (e.action() != PointerEvent.Action.POINTER_DOWN) {
          
          break;
      }
        var location = e.location();
        var locationCordonnate = new Coordinate(location.x(), location.y());
        GameViewer.checkRange(0, location.x(), width);
        GameViewer.checkRange(0, location.y(), height);
        
         //button release, Il mettra à jour l'animal choisi.
        if (location.x() >= releaseButtonX && location.x() <= releaseButtonX + buttonWidth &&
            location.y() >= releaseButtonY && location.y() <= releaseButtonY + buttonHeight) {
            
            if (animalChoisedCircle != null) {
                int removedIndex = -1;
                for (int i = 0; i < 4; i++) {
                    if (data.getListCircle().get(i).animal() == animalChoisedCircle.animal()) {
                        removedIndex = i;
                        break;
                    }
                }

                
             // Remplacer le Pair sélectionné et mettre à jour les données

                if (removedIndex != -1) {
                    Pair newPair = new Pair(data.getNextAnimal(), data.getNextTile());
                    data.replacePair(removedIndex, newPair);
                }

                animalChoisedCircle = null; 
                hint = "Player "+indexPlayer%nomberOfPlayers+  " : YOU have took a release! Now you can have a new animal to put!";
                GameViewer.loopGame(context, width, height, data.getListSquares(), data.getListCircle(), indexPlayer % nomberOfPlayers, data,type,hint);
            }
            evenHandled = true;
            break;
        }
        
        if (location.x() >= buttonX && location.x() <= buttonX + buttonWidth &&
            location.y() >= buttonY && location.y() <= buttonY + buttonHeight) {
           
            data.refreshFourPairs();
            hint = "Player "+indexPlayer%nomberOfPlayers+  " : YOU have took a refresh! Now Four Pairs have been all refreshed!";
            GameViewer.loopGame(context, width, height, data.getListSquares(), data.getListCircle(), indexPlayer % nomberOfPlayers, data,type,hint);
            tileChoisedSquare = null;
            animalChoisedCircle = null;
            tourAnimalOrTile = 1;
            
            evenHandled = true;
            break;
        }
        
        //Tour de Tile
        if(tourAnimalOrTile == 1) {
          if(tileChoisedSquare != null) {
            
            if(data.isPlacedAttachedTilesExistGraphicShape(locationCordonnate, indexPlayer%nomberOfPlayers,type) ) {
              data.addNewTileShape(locationCordonnate, tileChoisedSquare.tile(), indexPlayer % nomberOfPlayers,type);
              tileChoisedSquare= null;
              tourAnimalOrTile = 2;
              hint = "Player "+indexPlayer%nomberOfPlayers+ " : YOU have placed a jeton Tile!";
            }
            else {hint = "Player "+indexPlayer%nomberOfPlayers+ " : Not at the right place!! Place on the white jeton!";}

          } else {
            //On obtien la tile choisi
            for(Square square:data.getListSquares()) {
              if(square.contains(locationCordonnate)) {
                tileChoisedSquare = square;
                hint = "Player "+indexPlayer%nomberOfPlayers+ " : YOU have choised a jeton square!";
              }
          }
            
            
        }
        }else {
          if(animalChoisedCircle != null) {
            if(data.locateTheTileZen(locationCordonnate, indexPlayer % nomberOfPlayers,type) != null ) {
              
              if(data.acceptOrNotThisAnimal(data.locateTheTileZen(locationCordonnate, indexPlayer % nomberOfPlayers,type), animalChoisedCircle.animal(), indexPlayer % nomberOfPlayers) && data.isExistAnimal(data.locateTheTileZen(locationCordonnate, indexPlayer % nomberOfPlayers,type), indexPlayer % nomberOfPlayers) == false) {
              data.addNewAnimal(locationCordonnate,  animalChoisedCircle.animal(), indexPlayer % nomberOfPlayers,type);
              hint = "Player "+indexPlayer%nomberOfPlayers+ " :YOU have placed a jeton animal!";
              data.countScore(nomberOfPlayers);
              int removedIndex = -1;
              for (int i = 0; i < 4; i++) {
                  if (data.getListCircle().get(i).animal() == animalChoisedCircle.animal()) {
                      removedIndex = i;
                      break;
                  }
              }

         
              if (removedIndex != -1) {
                  Pair newPair = new Pair(data.getNextAnimal(), data.getNextTile());
                  data.replacePair(removedIndex, newPair);
              }
              animalChoisedCircle = null;
              tourAnimalOrTile = 1;
              indexPlayer++;
              }
              else { hint = "Player "+indexPlayer%nomberOfPlayers+ " : This jeton Nature doesn't accept this kind of Animal! You have to choise another one!";
              }
              }
           
          }
          else {
            for(Circle circle: data.getListCircle()) {
              if(circle.contains(locationCordonnate)) {
                animalChoisedCircle = circle;
                hint = "Player "+indexPlayer%nomberOfPlayers+ " : YOU have choised a animal!";
              }
          }
        }
        }

        
        GameViewer.loopGame(context, width, height,data.getListSquares(),data.getListCircle()  , indexPlayer % nomberOfPlayers, data,type,hint);
        
        
        evenHandled = true;
       
        break;
      case KeyboardEvent e:
        switch (e.action()) {
        case Action.KEY_RELEASED:
          counter++;
          if (counter == 2) {
            context.dispose();
            return;
          }
        case Action.KEY_PRESSED:
          System.out.println(e.key());
        default:
        }
        ;
    }
  
  }
  
  }
  

  
}
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  

  


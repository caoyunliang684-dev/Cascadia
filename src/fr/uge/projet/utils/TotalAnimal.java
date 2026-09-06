package fr.uge.projet.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.uge.projet.animal.Animal;



//un paquet de animals 100
public class TotalAnimal {
  private final List<Animal> animals = new ArrayList<>();
  private Random random = new Random();
  
  //initilization: ajouter 100 animals.
  public TotalAnimal() {
    for(int i = 0; i<20;i++) {
      animals.add(Animal.BEAR);
      animals.add(Animal.ELK);
      animals.add(Animal.FOX);
      animals.add(Animal.HAWK);
      animals.add(Animal.SALMON);
    }
  }
  // 
  
  //On le sorte du sac, et on le supprime dans la sac
  /**
   * Retrieves a random animal from the bag and removes it.
   *
   * @return the randomly selected {@link Animal}, or {@code null} if the bag is empty.
   */
  public Animal getAnimal() {
    if(animals.isEmpty()) {
      return null;
    }
    int i = random.nextInt(animals.size());
    return animals.remove(i);
  }
  
  //poll
  /**
   * Returns an animal back to the bag.
   *
   * @param animal the {@link Animal} to return; must not be {@code null}.
   * @throws NullPointerException if {@code animal} is {@code null}.
   */
  public void putBackAnimal(Animal animal) {
    animals.add(animal);
  }
}

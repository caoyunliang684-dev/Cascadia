package fr.uge.projet.score;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import fr.uge.projet.animal.Animal;
import fr.uge.projet.utils.Coordinate;
import fr.uge.projet.utils.PlayerInformation;
import fr.uge.projet.zen.Hexagon;
import fr.uge.projet.zen.ShowType;

public class CardVariantScorer {
	private final String configuration;
	private final ShowType showType;
	/**
   * Constructs a new {@code CardVariantScorer} with the specified configuration and shape type.
   *
   * @param config   the scoring configuration; must not be {@code null}.
   * @param showType the shape type used in the game (e.g., SQUARE, HEXAGON); must not be {@code null}.
   * @throws NullPointerException if {@code config} or {@code showType} is {@code null}.
   */
	public CardVariantScorer(String config,ShowType showType) {
	  this.configuration = Objects.requireNonNull(config);
	  this.showType = Objects.requireNonNull(showType);
	}

  /**
   * Calculates the total score for a placement based on the current configuration.
   *
   * @param placement the placement object containing the animals on the game board; must not be {@code null}.
   * @return the total score calculated for all animals.
   * @throws NullPointerException if {@code placement} is {@code null}.
   */
	public int calculateTotalScore(PlayerInformation placement) {
		var animalMap = placement.getAnimalMap();
	    int totalScore = 0;

	    for (var animal : Animal.values()) {
	       
	        switch (animal) {
	            case BEAR -> totalScore += scoreBears(animalMap);
	            case SALMON -> totalScore += scoreSalmon(animalMap, configuration);
	            case FOX -> totalScore += scoreFoxes(animalMap, configuration);
	            case ELK -> totalScore += scoreElk(animalMap, configuration);
	            case HAWK -> totalScore += scoreHawks(animalMap, configuration);
	        }
	    }

	    return totalScore;
	}

	// Used to count a score for pair Bear (card A)
	private int calculateScoreForPairs(Set<Set<Coordinate>> groups) {
	  int score = 0;
	  for (Set<Coordinate> group : groups) {
	    int pairs = group.size() / 2; // Chaque paire d'ours
	    score += pairs * 10; // 10 points par paire
	  }
	    return score;
	}
	
	// Used to count a score for Bear (card B)
	private int calculateScoreForExactSize(Set<Set<Coordinate>> groups, int exactSize, int points) {
	    int score = 0;
	    for (Set<Coordinate> group : groups) {
	        if (group.size() == exactSize) {
	            score += points;
	        }
	    }
	    return score;
	}
	
	// Used to count a score for Bear (card C)
	private int calculateScoreForMultipleSizes(Set<Set<Coordinate>> groups) {
	    int score = 0;
	    boolean hasSize1 = false, hasSize2 = false, hasSize3 = false;

	    for (Set<Coordinate> group : groups) {
	        int size = group.size();
	        if (size == 1) {
	            score += 5;
	            hasSize1 = true;
	        } else if (size == 2) {
	            score += 10;
	            hasSize2 = true;
	        } else if (size == 3) {
	            score += 15;
	            hasSize3 = true;
	        }
	    }

	    // Bonus si au moins un groupe de chaque taille est présent
	    if (hasSize1 && hasSize2 && hasSize3) {
	        score += 3;
	    }

	    return score;
	}

    // Used to count a score for Bear (card C) 
	private int calculateScoreForRangeSizes(Set<Set<Coordinate>> groups, int minSize, int maxSize) {
	    int score = 0;

	    for (Set<Coordinate> group : groups) {
	        int size = group.size();
	        if (size >= minSize && size <= maxSize) {
	            score += size * 5; // 5 points par ours
	        }
	    }

	    return score;
	}
	 /**
   * Calculates the score for bears based on the specified configuration.
   *
   * <p>Each configuration defines different rules for scoring:
   * <ul>
   *   <li>Configuration "A": Pairs of bears.</li>
   *   <li>Configuration "B": Groups of exact size 3.</li>
   *   <li>Configuration "C": Groups of various sizes with bonuses.</li>
   *   <li>Configuration "D": Groups within a size range (2-4).</li>
   * </ul>
   *
   * @param animalMap a map of coordinates to animals representing the game board.
   * @return the score calculated for bears.
   */
	private int scoreBears(Map<Coordinate, Animal> animalMap) {
	    int score = 0;

	    // Trouver les groupes d'ours
	    Set<Set<Coordinate>> bearGroups = ScoreManager.findGroups(animalMap, Animal.BEAR);

	    // Calculer le score en fonction de la configuration
	    switch (configuration) {
	        case "A" -> score += calculateScoreForPairs(bearGroups);
	        case "B" -> score += calculateScoreForExactSize(bearGroups, 3, 15);
	        case "C" -> score += calculateScoreForMultipleSizes(bearGroups);
	        case "D" -> score += calculateScoreForRangeSizes(bearGroups, 2, 4);
	    }

	    return score;
	}
	
	
	///// Counter for score Elk (Wapitis)///////////
	////////////////////////////////////////////////
	
	
	private int calculateScoreForLines(Set<Set<Coordinate>> groups) {
	  int score = 0;
	    for (Set<Coordinate> group : groups) {
	      if (isLine(group)) {
	        score += 10; // 10 points par groupe en ligne droite
	      }
	    }

	  return score;
	}

	// Vérifie si un groupe est en ligne droite
	private boolean isLine(Set<Coordinate> group) {
	  if (group.size() < 2) return false;

	  // Extraire les coordonnées x et y
	  var xCoords = group.stream().map(Coordinate::x).sorted().toList();
	  var yCoords = group.stream().map(Coordinate::y).sorted().toList();

	  // Vérifier si les coordonnées forment une ligne horizontale, verticale ou diagonale
	  var horizontal = yCoords.stream().distinct().count() == 1;
	  var vertical = xCoords.stream().distinct().count() == 1;
	  var diagonal = Math.abs(xCoords.get(xCoords.size() - 1) - xCoords.get(0)) ==
	                       Math.abs(yCoords.get(yCoords.size() - 1) - yCoords.get(0));

	  return horizontal || vertical || diagonal;
	}
	
	private int calculateScoreForSpecificShapes(Set<Set<Coordinate>> groups) {
	    int score = 0;

	    for (Set<Coordinate> group : groups) {
	        if (matchesSpecificShapes(group)) {
	            score += 15; // 15 points par groupe correspondant à une forme spécifique
	        }
	    }

	    return score;
	}

	private Set<Coordinate> normalizeGroup(Set<Coordinate> group) {
	  if (group.isEmpty()) return group;

	  // Trouver les coordonnées minimales (x, y)
	  var minX = group.stream().mapToInt(Coordinate::x).min().orElse(0);
	  var minY = group.stream().mapToInt(Coordinate::y).min().orElse(0);

	    // Normaliser chaque coordonnée par rapport aux minimums
	  var normalizedGroup = new HashSet<Coordinate>();
	    for (Coordinate coord : group) {
	      normalizedGroup.add(new Coordinate(coord.x() - minX, coord.y() - minY));
	    }

	    return normalizedGroup;
	}

	// Vérifie si un groupe correspond à une forme spécifique
	private boolean matchesSpecificShapes(Set<Coordinate> group) {
	    // Ajouter les formes spécifiques ici
	    List<Set<Coordinate>> specificShapes = List.of(
	        Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, 1)), // Triangle
	        Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 0), new Coordinate(1, 1)) // Carré
	    );

	    Set<Coordinate> normalizedGroup = normalizeGroup(group);

	    return specificShapes.stream().anyMatch(shape -> shape.equals(normalizedGroup));
	}

	// card C for ELK (Wapitis) 
	private int calculateScoreForSizes(Set<Set<Coordinate>> groups) {
	  var score = 0;

	  for (Set<Coordinate> group : groups) {
	    score += group.size() * 5; // 5 points par wapiti dans le groupe
	  }

	  return score;
	}
	
	// Vérifie si un groupe forme un cercle
	private boolean isCircle(Set<Coordinate> group) {
	    // Vérification simplifiée pour un cercle
	    var edges = 0;
	    
	    for (var coord : group) {
	    	
	    	//retrieve a friends of animal
	    	var neighbors = showType.equals(ShowType.HEXAGON) ? Hexagon.sixSidesCordonnates(coord) : coord.fourSidesCordonnates();
	        for (var neighbor : neighbors) {
	            if (group.contains(neighbor)) {
	                edges++;
	            }
	        }
	    }

	    // Un cercle approximé a environ 2 fois plus d’arêtes que de nœuds
	    return edges / 2 == group.size();
	}
	
	// card D for ELK (Wapitis)
	private int calculateScoreForCircles(Set<Set<Coordinate>> groups) {
	    var score = 0;

	    for (Set<Coordinate> group : groups) {
	        if (isCircle(group)) {
	            score += 20; // 20 points par groupe en cercle
	        }
	    }

	    return score;
	}

	
	private int scoreElk(Map<Coordinate, Animal> animalMap, String configuration) {
	  var score = 0;

	  // Trouver les groupes de wapitis
	  var elkGroups = ScoreManager.findGroups(animalMap, Animal.ELK);

	    // Calculer le score en fonction de la configuration
	    switch (configuration) {
	        case "A" -> score += calculateScoreForLines(elkGroups);
	        case "B" -> score += calculateScoreForSpecificShapes(elkGroups);
	        case "C" -> score += calculateScoreForSizes(elkGroups);
	        case "D" -> score += calculateScoreForCircles(elkGroups);
	    }

	    return score;
	}


    ///// Counter for score Salmon (Saumon)///////////
	////////////////////////////////////////////////
	
	private int calculateScoreForBankSize(Set<Set<Coordinate>> banks, int maxSize) {
	    int score = 0;

	    for (Set<Coordinate> bank : banks) {
	        int size = Math.min(bank.size(), maxSize); // Taille maximale prise en compte
	        score += size; // Ajouter des points selon la taille
	    }

	    return score;
	}

	private int calculateScoreForBankRange(Set<Set<Coordinate>> banks, int minSize, int maxSize) {
	    int score = 0;

	    for (Set<Coordinate> bank : banks) {
	        int size = bank.size();
	        if (size >= minSize && size <= maxSize) {
	            score += size; // Ajouter des points selon la taille
	        }
	    }

	    return score;
	}
	private int calculateScoreForBanksWithFauna(Set<Set<Coordinate>> banks, Map<Coordinate, Animal> animalMap) {
	    int score = 0;

	    for (Set<Coordinate> bank : banks) {
	        score += bank.size(); // 1 point par saumon
	        for (Coordinate coord : bank) {
	            score += findFaunaTokensAround(coord, animalMap); // Ajouter 1 point pour chaque jeton Faune adjacent
	        }
	    }

	    return score;
	}

	private int findFaunaTokensAround(Coordinate coord, Map<Coordinate, Animal> animalMap) {
	    int faunaTokens = 0;
	    var neighbors = showType.equals(ShowType.HEXAGON) ? Hexagon.sixSidesCordonnates(coord) : coord.fourSidesCordonnates();
	    for (Coordinate neighbor : neighbors) {
	        if (animalMap.containsKey(neighbor) && animalMap.get(neighbor) != Animal.SALMON) {
	            faunaTokens++;
	        }
	    }
	    return faunaTokens;
	}
	
	 private void exploreSalmonBank(Coordinate coord, Map<Coordinate, Animal> animalMap, Set<Coordinate> bank, Set<Coordinate> visited) {
		    // Arrêt si déjà visité ou non valide
		    if (visited.contains(coord) || animalMap.get(coord) != Animal.SALMON) {
		        return;
		    }

		    // Ajouter la coordonnée au banc actuel
		    bank.add(coord);
		    visited.add(coord);

		    // Vérifier les voisins valides (maximum 2 saumons adjacents)
		    int connectedSalmon = 0;
		    
		    var neighbors = showType.equals(ShowType.HEXAGON) ?Hexagon.sixSidesCordonnates(coord) : coord.fourSidesCordonnates();
		    for (Coordinate neighbor : neighbors) {
		        if (animalMap.containsKey(neighbor) && animalMap.get(neighbor) == Animal.SALMON) {
		            connectedSalmon++;
		            if (connectedSalmon > 2) {
		                break; // Un banc ne peut pas avoir plus de 2 connexions
		            }
		            exploreSalmonBank(neighbor, animalMap, bank, visited);
		        }
		    }
		}

	private Set<Set<Coordinate>> findSalmonBanks(Map<Coordinate, Animal> animalMap) {
	    Set<Set<Coordinate>> banks = new HashSet<>();
	    Set<Coordinate> visited = new HashSet<>();

	    // Parcourir toutes les coordonnées contenant des saumons
	    for (Coordinate coord : animalMap.keySet()) {
	        if (animalMap.get(coord) == Animal.SALMON && !visited.contains(coord)) {
	            Set<Coordinate> bank = new HashSet<>();
	            exploreSalmonBank(coord, animalMap, bank, visited);
	            banks.add(bank);
	        }
	    }

	    return banks;
	}

	private int scoreSalmon(Map<Coordinate, Animal> animalMap, String configuration) {
	    int score = 0;

	    // Trouver les bancs de saumons
	    Set<Set<Coordinate>> salmonBanks = findSalmonBanks(animalMap);

	    // Calculer le score selon la carte
	    switch (configuration) {
	        case "A" -> score += calculateScoreForBankSize(salmonBanks, 7);
	        case "B" -> score += calculateScoreForBankSize(salmonBanks, 5);
	        case "C" -> score += calculateScoreForBankRange(salmonBanks, 3, 5);
	        case "D" -> score += calculateScoreForBanksWithFauna(salmonBanks, animalMap);
	    }
	    
	    return score;
	}
	  
     ///// Counter for score Hawks (Buse)///////////
	////////////////////////////////////////////////
	///
	private int calculateScoreForIsolatedHawks(Map<Coordinate, Animal> animalMap) {
	    int score = 0;

	    for (Coordinate coord : animalMap.keySet()) {
	        if (animalMap.get(coord) == Animal.HAWK && isIsolated(coord, animalMap)) {
	            score += 5; // 5 points par buse isolée
	        }
	    }

	    return score;
	}

	// Vérifie si une buse est isolée
	private boolean isIsolated(Coordinate coord, Map<Coordinate, Animal> animalMap) {
		
		var neighbors = showType.equals(ShowType.HEXAGON) ? Hexagon.sixSidesCordonnates(coord) : coord.fourSidesCordonnates();
	    for (Coordinate neighbor : neighbors) {
	        if (animalMap.containsKey(neighbor) && animalMap.get(neighbor) == Animal.HAWK) {
	            return false; // La buse a une autre buse voisine
	        }
	    }
	    return true;
	}

	private int calculateScoreForHawksWithLineOfSight(Map<Coordinate, Animal> animalMap) {
	    int score = 0;

	    for (Coordinate coord : animalMap.keySet()) {
	        if (animalMap.get(coord) == Animal.HAWK && hasLineOfSight(coord, animalMap)) {
	            score += 7; // 7 points par buse
	        }
	    }

	    return score;
	}

	// Vérifie si une buse a une autre buse en ligne de mire
	private boolean hasLineOfSight(Coordinate coord, Map<Coordinate, Animal> animalMap) {
	    for (Coordinate direction : List.of(
	            new Coordinate(1, 0), new Coordinate(-1, 0),
	            new Coordinate(0, 1), new Coordinate(0, -1))) {
	        Coordinate current = coord;
	        while (true) {
	            current = current.add(direction);
	            if (!animalMap.containsKey(current)) break;
	            if (animalMap.get(current) == Animal.HAWK) {
	                return true; // Une autre buse est visible
	            }
	        }
	    }
	    return false;
	}

	private int calculateScoreForHawkPairs(Map<Coordinate, Animal> animalMap) {
	    int score = 0;
	    Set<Coordinate> visited = new HashSet<>();

	    for (Coordinate coord : animalMap.keySet()) {
	        if (animalMap.get(coord) == Animal.HAWK && !visited.contains(coord)) {
	            // Chercher une paire
	            for (Coordinate neighbor : findLineOfSightPairs(coord, animalMap)) {
	                int uniqueAnimals = countUniqueAnimalsBetween(coord, neighbor, animalMap);
	                score += uniqueAnimals * 2; // 2 points par animal unique
	                visited.add(neighbor);
	            }
	        }
	    }

	    return score;
	}

	// Trouve les paires en ligne de mire
	private List<Coordinate> findLineOfSightPairs(Coordinate coord, Map<Coordinate, Animal> animalMap) {
	    List<Coordinate> pairs = new ArrayList<>();

	    for (Coordinate direction : List.of(
	            new Coordinate(1, 0), new Coordinate(-1, 0),
	            new Coordinate(0, 1), new Coordinate(0, -1))) {
	        Coordinate current = coord;
	        while (true) {
	            current = current.add(direction);
	            if (!animalMap.containsKey(current)) break;
	            if (animalMap.get(current) == Animal.HAWK) {
	                pairs.add(current);
	                break;
	            }
	        }
	    }

	    return pairs;
	}

	// Compte les animaux uniques entre deux coordonnées
	private int countUniqueAnimalsBetween(Coordinate start, Coordinate end, Map<Coordinate, Animal> animalMap) {
	    Set<Animal> uniqueAnimals = new HashSet<>();
	    for (Coordinate coord : findCoordinatesBetween(start, end)) {
	        if (animalMap.containsKey(coord) && animalMap.get(coord) != Animal.HAWK) {
	            uniqueAnimals.add(animalMap.get(coord));
	        }
	    }
	    return uniqueAnimals.size();
	}

	// Trouve toutes les coordonnées entre deux points
    private List<Coordinate> findCoordinatesBetween(Coordinate start, Coordinate end) {
	    // Implémentation pour trouver toutes les coordonnées entre start et end (ligne droite)
	    return new ArrayList<>(); // Placeholder
	}

	
	private int scoreHawks(Map<Coordinate, Animal> animalMap, String configuration) {
	    int score = 0;

	    // Calculer le score en fonction de la carte
	    switch (configuration) {
	        case "A" -> score += calculateScoreForIsolatedHawks(animalMap);
	        case "B" -> score += calculateScoreForHawksWithLineOfSight(animalMap);
	        //case "C" -> score += calculateScoreForLinesOfSight(animalMap);
	        case "D" -> score += calculateScoreForHawkPairs(animalMap);
	    }

	    return score;
	}
	
	///////////////Counter for Fox ////////// 
	////////////////////////////////////////
	

	private int calculateScoreForIndividualFoxes(List<Coordinate> foxes, Map<Coordinate, Animal> animalMap, Function<Set<Animal>, Integer> scoringFunction) {
      var score = 0;
	  for (var fox : foxes) {
	    // Trouver les espèces autour du renard
		Set<Animal> neighbors = findNeighbors(fox, animalMap);

		// Appliquer la fonction de scoring correspondante
		score += scoringFunction.apply(neighbors);
	  }

	  return score;
	}

		// Trouve les animaux voisins d'une coordonnée
		private Set<Animal> findNeighbors(Coordinate coord, Map<Coordinate, Animal> animalMap) {
			var neighbors = showType.equals(ShowType.HEXAGON) ? Hexagon.sixSidesCordonnates(coord) : coord.fourSidesCordonnates();
		    return neighbors.stream()
		        .filter(animalMap::containsKey)
		        .map(animalMap::get)
		        .collect(Collectors.toSet());
		}
		
		private int countUniqueSpecies(Set<Animal> neighbors) {
		  return neighbors.size();
		}
		
		private int countPairsOfSpecies(Set<Animal> neighbors) {
		  Map<Animal, Long> counts = neighbors.stream()
		        .collect(Collectors.groupingBy(animal -> animal, Collectors.counting()));
		  return (int) counts.values().stream().mapToLong(count -> count / 2).sum();
		}

		private int countMostFrequentSpecies(Set<Animal> neighbors) {
		  Map<Animal, Long> counts = neighbors.stream()
				  							  .collect(Collectors.groupingBy(animal -> animal, Collectors.counting()));
		  return counts.values().stream().mapToInt(Long::intValue).max().orElse(0);
		}
		
		private int calculateScoreForFoxPairs(List<Coordinate> foxes, Map<Coordinate, Animal> animalMap) {
		    int score = 0;

		    for (int i = 0; i < foxes.size(); i++) {
		        for (int j = i + 1; j < foxes.size(); j++) {
		            Coordinate fox1 = foxes.get(i);
		            Coordinate fox2 = foxes.get(j);

		            // Vérifier si les deux renards forment une paire
		            if (areNeighbors(fox1, fox2)) {
		                Set<Animal> animalsBetween = findAnimalsBetween(fox1, fox2, animalMap);
		                score += countPairsOfSpecies(animalsBetween);
		            }
		        }
		    }

		    return score;
		}

		// Vérifie si deux renards sont voisins
		private boolean areNeighbors(Coordinate coord1, Coordinate coord2) {
		    return coord1.fourSidesCordonnates().contains(coord2);
		}

		// Trouve les animaux entre deux renards
		private Set<Animal> findAnimalsBetween(Coordinate fox1, Coordinate fox2, Map<Coordinate, Animal> animalMap) {
		    Set<Animal> animalsBetween = new HashSet<>();

		    // Calculer les différences en x et y
		    int dx = Integer.signum(fox2.x() - fox1.x());
		    int dy = Integer.signum(fox2.y() - fox1.y());

		    // Vérifier si fox1 et fox2 sont alignés
		    if (dx != 0 && dy != 0 && Math.abs(fox2.x() - fox1.x()) != Math.abs(fox2.y() - fox1.y())) {
		        return animalsBetween; // Pas alignés en ligne droite
		    }

		    // Parcourir les coordonnées entre fox1 et fox2
		    Coordinate current = new Coordinate(fox1.x() + dx, fox1.y() + dy);
		    while (!current.equals(fox2)) {
		        if (animalMap.containsKey(current) && animalMap.get(current) != Animal.FOX) {
		            animalsBetween.add(animalMap.get(current)); // Ajouter l'animal (hors renards)
		        }
		        current = new Coordinate(current.x() + dx, current.y() + dy); // Avancer dans la direction
		    }

		    return animalsBetween;
		}
		
		
		private int scoreFoxes(Map<Coordinate, Animal> animalMap, String configuration) {
		    var score = 0;

		    // Filtrer les renards dans la carte
		    List<Coordinate> foxes = animalMap.entrySet().stream()
		        .filter(entry -> entry.getValue() == Animal.FOX)
		        .map(Map.Entry::getKey)
		        .toList();

		    // Calculer le score en fonction de la configuration
		    switch (configuration) {
		        case "A" -> score += calculateScoreForIndividualFoxes(foxes, animalMap, this::countUniqueSpecies);
		        case "B" -> score += calculateScoreForIndividualFoxes(foxes, animalMap, this::countPairsOfSpecies);
		        case "C" -> score += calculateScoreForIndividualFoxes(foxes, animalMap, this::countMostFrequentSpecies);
		        case "D" -> score += calculateScoreForFoxPairs(foxes, animalMap);
		    }

		    return score;
		}




}

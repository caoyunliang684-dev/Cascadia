# Cascadia

A Java implementation of the tile-placement board game Cascadia, playable in the terminal or in a graphical window.

Two-person course project at Université Gustave Eiffel by Xiang Cui and Yunliang Cao. In-game text is in French.

## Features

- Three ways to play, chosen at start-up:
  - terminal (text) mode
  - graphical mode with square tiles
  - graphical mode with hexagonal tiles
- Several players, with the number entered at start-up.
- Three scoring options: the family variant, the intermediate variant, and animal scoring cards A, B, C or D.
- Each turn a player picks a habitat tile and wildlife token pair, places the tile next to an existing one, and may place the token on a tile that accepts that animal.

The graphical modes use the [Zen](https://github.com/forax/zen) library, included in `lib/`.

## Run

Requires Java 23 or later.

```sh
java -jar Cascadia.jar
```

Run it from the repository root so that the jar finds `lib/zen-6.0.jar`, then follow the prompts to choose the display mode, the number of players and the scoring option.

## Build

With [Apache Ant](https://ant.apache.org/):

```sh
ant jar        # compile and rebuild Cascadia.jar
ant javadoc    # generate the API documentation in docs/doc
```

The repository also opens directly as an Eclipse project.

## Layout

| Package | Purpose |
| --- | --- |
| `fr.uge.projet.main` | Entry point and start-up menus |
| `fr.uge.projet.player` | Players and the terminal game loop |
| `fr.uge.projet.tile`, `fr.uge.projet.animal` | Habitat tiles and wildlife tokens |
| `fr.uge.projet.score` | Scoring for the three variants |
| `fr.uge.projet.zen` | Graphical modes: game data, view and controller, square and hexagonal shapes |
| `fr.uge.projet.utils` | Coordinates, tile and token pools, shared helpers |

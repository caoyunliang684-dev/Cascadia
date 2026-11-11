# Cascadia

A Java implementation of the tile-placement board game Cascadia, playable in the terminal or in a graphical window.

Two-person course project at Université Gustave Eiffel by Xiang Cui and Yunliang Cao. The manuals are in French.

## Features

- Three ways to play, chosen at start-up:
  - **Terminal mode**: solo or multiplayer with 2 to 4 players
  - **Square-grid graphical mode**: two players, with mouse-driven placement, placement checks and automatic scoring
  - **Hexagonal graphical mode**: 2 to 4 players; tile and token placement work, final scoring is not implemented yet
- Three scoring options:
  - family variant
  - intermediate variant
  - animal scoring cards, with a choice of card A, B, C or D for each of the five animals (bear, deer, eagle, fox, salmon)
- Each turn a player picks one of four habitat tile and wildlife token pairs, places the tile next to an existing one, and may place the token on a tile that accepts that animal. A game lasts 20 turns per player.

Not implemented: nature tokens, tiles with more than one habitat, and rotation of hexagonal tiles.

## Run

Requires Java 21 or later.

```sh
java -jar Cascadia.jar
```

Run it from the repository root so that the jar finds `lib/zen-6.0.jar`, then follow the prompts. See the [user guide](docs/user.pdf) for a walkthrough.

## Build

With [Apache Ant](https://ant.apache.org/):

```sh
ant jar        # compile into classes/ and rebuild Cascadia.jar
ant javadoc    # generate the API documentation in docs/doc
```

## Structure

The code follows a basic model-view-controller split. The graphical modes use the [Zen](https://github.com/forax/zen) library, included in `lib/`.

| Package | Purpose |
| --- | --- |
| `game` | Entry point, start-up menus, players, tiles and animals |
| `gameterminal` | Terminal mode: game data and control for solo and multiplayer games |
| `gamegraphic` | Graphical modes: game data, plus a view and a controller for each of the square and hexagonal boards |
| `score` | Scoring interface, family and intermediate variants |
| `bearscore`, `deerscore`, `eaglescore`, `foxscore`, `salmonscore` | Animal scoring cards A to D |

More detail is in the [developer manual](docs/dev.pdf).

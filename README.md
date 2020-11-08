# MonSim 

MonSim is a text-based Monopoly simulator written in Kotlin.

The program makes it easy to automatically run a game scenario and determine
which player will win given the properties and assets the player already possesses.
The program supports AI automated players who will automatically roll, buy properties,
build houses, and collect rent.  Thus the program is especially useful for determining if a trade 
is advantageous to one player or the other.

In addition, most aspects about the game are customizable including:
* rents, property prices, and house prices
* board layout (where properties, taxes, chance spaces, and free parking spaces are)
* chance & community cards
* the amount for passing go or paying bail
* tax amounts
* number of players, their starting cash, and starting properties
* and much more... (See Rule.kt and DefaultGameValues.kt for all options)

## Getting Started

1. Install the JDK
1. Install Apache Maven
1. Download or clone the project
1. Open a command prompt
1. change directory to wherever you have code
1. ***mvn test -Dtest=Example1***

The above command runs the code below.
A simple Monopoly scenario is run, where there are only 2 players.  
Each player starts with only $100, with Player#1 owning Water Works, 
and Player#2 owning B&O Railroad.
By changing the parameters to Rule, we simplify the game for demo purposes: 
 * Buying properties is disabled
 * Mortgaging and unmortgaging is disabled
 * There is no $200 for passing go
 * There is no bail required to get out of jail
 * There are no chance or community chests cards.  If a player lands on a chance space, nothing happens.

*Note that the game can be easily changed to run in its full-featured mode by simply changing the parameters back.* 
The players are controlled by AI, and each of them automatically rolls the dice and collects rent.
(the players can also be easily configured to buy property and houses, but we explicitly disabled it in the Rule parameters.)  
The player who avoids going bankrupt wins.
To avoid an unending game, if more than "maxTotalRolls" is exceeded (set to 5000 in Rule), 
the player with the highest net worth is declared the winner.  Thus at max the dice will be rolled 5000 times.
(**Net worth** is defined as the player's cash, plus all the cash that would be received if he mortgaged all of
his unmortgaged properties and sold all of his houses back to the bank.)


Example1.kt:
```kotlin
        val player1 = Player(id = 501, cash = 100)
        val player2 = Player(id = 502, cash = 100)
        val game = Game(
                rule = Rule(
                        maxTotalRolls = 5000,
                        isBuyingHousesDisabled = true,
                        isMortgagingDisabled = true,
                        isBuyingPropertiesDisabled = true,
                        cashForPassingGo = 0,
                        bail = 0,
                        seed = 12345
                ),
                cards = listOf(),
                taxes = listOf(),
                piles = listOf(),
                players = listOf(player1, player2)
        )
        gameUtil.propertyWithName("Water Works", game).ownerId = player1.id
        gameUtil.propertyWithName("B&O RR", game).ownerId = player2.id
        val aiRunner:AiRunner = AiRunnerImpl()
        aiRunner.autoPlayGameUsingAi(game)
```

The following output is printed:
```
[main] INFO org.monsim.impl.ai.AiRunnerImpl - auto running a game.  Seed:12345
[main] INFO org.monsim.impl.ai.AiRunnerImpl - Game over. Player#502 won.   WinnersTotalAssets:$375.  GameTotalRolls=339 Seed=12345
```

Thus, with this seed and random simulation, it would appear that 
B&O RR is a more valuable property than Water Works.
Player#502 ends up with all assets (a net worth of $375), which is computed from total cash ($200)
plus the mortgaged values of Water Works and B&O Railroad ($75 + $100).
In this simple game scenario, since there is no income from passing GO, nor any taxes or chance cards,
then the amount of assets at the beginning of the game is the same as at the end of the game.

## Example#2

Run:
***mvn test -Dtest=Example2***

Example#2 is similar to the first example, except that Player#501 starts with all the properties in 
the DarkBlue group (Park Place & Boardwalk), and Player#502 starts with all the properties in the 
Pink group (St. Charles Pl, States Ave, Virginia Ave).  The simulator's AI players automatically buy 
houses, and automatically roll.  After dozens of dice rolls, Player#502 ends up winning.
This is counterintuitive, as Boardwalk and Park Place are often the most coveted properties 
in the game, but the player with the Pink group monopoly ends up winning.
```
[main] INFO org.monsim.impl.ai.AiRunnerImpl - auto running a game.  Seed:12348
[main] INFO org.monsim.impl.ai.AiRunnerImpl - Game over. Player#502 won.   WinnersTotalAssets:$4095.  GameTotalRolls=72 Seed=12348
```

## Example#3

Run: ***mvn test -Dtest=Example3***

Example#3 is mostly like Example#2, except that we run the same game with 10 different seeds.  As it turns out,
sometimes the first player wins.  The output is as follows:
```
[main] INFO org.monsim.impl.ai.AiRunnerImpl - auto running a game.  Seed:10000
[main] INFO org.monsim.impl.ai.AiRunnerImpl - Game over. Player#502 won.   WinnersTotalAssets:$3145.  GameTotalRolls=25 Seed=10000
[main] INFO org.monsim.impl.ai.AiRunnerImpl - auto running a game.  Seed:10001
[main] INFO org.monsim.impl.ai.AiRunnerImpl - Game over. Player#501 won.   WinnersTotalAssets:$2395.  GameTotalRolls=28 Seed=10001
...
[main] INFO org.monsim.impl.ai.AiRunnerImpl - auto running a game.  Seed:10009
[main] INFO org.monsim.impl.ai.AiRunnerImpl - Game over. Player#502 won.   WinnersTotalAssets:$3495.  GameTotalRolls=56 Seed=10009
[main] INFO org.monsim.examples.Example3 - Player#1 won 3 out of 10 runs
```
## Default Values

The default properties, spaces, taxes, and cards included in a standard monopoly game
(like "You have won second prize in a beauty contest – Collect $10") are all included in this program.
To create a game with the default values, simply create a Game object and don't override 
the cards and taxes with an empty list:
```
val game = Game(
        rule = Rule(  //uses default rules (like cashForPassingGo=200) unless overwritten here
                seed = 12345
        ),
        //cards = listOf(), //don't uncomment to use default list of community & chance cards
        //taxes = listOf(), //...
        //piles = listOf(), //...
        players = listOf(player1, player2) //the 2 players as defined in example 1
)
```

## Limitations

* Auctions aren't supported.  
  *  If a player declines to buy a property, the property stays unowned and the next player rolls.
  *  If a player resigns and owes money to the bank, all the player's properties become unowned.
* Hotels and houses aren't limited (The real monopoly game only has 32 houses and 12 hotels.)
* Income tax is always $200 (paying 10% of the player's assets isn't an option.)
* The players (all AI) don't trade properties with each other

## Log Details

Details about the simulation can be seen by changing the log level from INFO to DEBUG in pom.xml.

pom.xml:
```
<org.slf4j.simpleLogger.defaultLogLevel>debug</org.slf4j.simpleLogger.defaultLogLevel>
```

The will result in detailed information being logged about what is happening in the simulation, such as:
```
[main] DEBUG org.monsim.impl.play.MoverImpl - player#501 is moving 6 spaces to SpaceId#1019(Tennessee Ave) (last space was #1013) 
[main] DEBUG org.monsim.impl.play.RenterImpl - no rent charged to player#501 for space#1019('Tennessee Ave') because property has no owner
```
 

## Commands

Run a simulation (such as Example1)):
***mvn test -Dtest=Example1***

Run all tests:
***mvn test***

Create a tar.gz backup:
***tar czf monsim_$(date -u +"%Y%m%dT%H%M%SZ").tar.gz src .gitignore LICENSE.txt pom.xml README.md***

## Contact

Questions or comments?
Feel free to contact me via email -- my email address is in the pom.xml file.
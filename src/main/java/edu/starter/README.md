# Week 1
Project Description
This is a game where the player can choose to attack, defend, recover, or use a special attack against an enemy. 
The player has a certain amount of HP and resource points that can be used for special attacks. 
The goal is to eliminate the enemy while managing your own HP and resources.

Compile
javac edu/starter/Main.java

Run 
java edu.starter.Main

Verified the program is working as expected by running the following sequences:
W01-ATK-01
    Player HP: 100
    Enemy HP: 30
    Resource count: 5
    Pending defence: false
    Enter your action (attack, special, defend, recover): attack
    Player HP: 90
    Enemy HP: 20
    Resource count: 5
    Pending defence: false

W01-DEF-01
    Player HP: 100
    Enemy HP: 30
    Resource count: 5
    Pending defence: false
    Enter your action (attack, special, defend, recover): defend
    Player HP: 95
    Enemy HP: 30
    Resource count: 5
    Pending defence: false

 W01-SPC-01
    Player HP: 100
    Enemy HP: 30
    Resource count: 5
    Pending defence: false
    Enter your action (attack, special, defend, recover): special
    Player HP: 90
    Enemy HP: 15
    Resource count: 2
    Pending defence: false

 W01-RES-01
    Player HP: 100
    Enemy HP: 30
    Resource count: 9
    Pending defence: false
    Enter your action (attack, special, defend, recover): recover
    Player HP: 90
    Enemy HP: 30
    Resource count: 10
    Pending defence: false

W01-INV-01
    Player HP: 100
    Enemy HP: 30
    Resource count: 5
    Pending defence: false
    Enter your action (attack, special, defend, recover): ski-jump
    Invalid action. Please try again.
    Player HP: 100
    Enemy HP: 30
    Resource count: 5
    Pending defence: false

Python vs Java
Java requires compilation before execution, while Python is interpreted and can be run directly. 
Python has a simpler syntax and uses dynamic typing when Java is statically typed.

Design Decisions
We set the initial values with local variables in the main method representing the state and for every potential action we set a method that updates the state accordingly.
In every turn the player is selecting an action and the state is updated accordingly. 
The turns are managed by iterating through a loop until either the player or the enemy reaches 0 HP.
Some of the edge cases are managed in the Main method, for example if the enemy is defeated the player will not be counter attacked.
Others are handled in the methods, for example if the player is defending the counter attack will be reduced by 5 HP.

AI-use note:
When the program did not compile, I used the AI assistant to help identify and fix the issue.
In some cases the AI assistant provided suggestions that helped me handle the edge cases.
Finally I used it to extract the counter_attack_if_enemy_alive method to avoid code duplication.
The assistant suggested that when the special can't be applied due to low resource points the counter attack should still be applied, but it contradicted the game rules, so i rejected the suggestion.

# Week 2 — Two Encounters, One Evolving Player

A Java console game with two consecutive one-on-one battles. The same player carries surviving HP and resources from the first encounter into the second, while each enemy is a separate object.

## Project structure

```text
src/main/java/edu/starter/
  Main.java           Starts the adventure and sequences the encounters
  Battle.java         Runs one encounter and handles player actions
  Combatant.java      Stores and protects HP, resources and defense
  CombatantTest.java  Standalone checks of combatant state and boundaries
```

## Requirements

- Java JDK installed (the project has been run using JDK 25).
- Run commands from the project root (`student-starter`).
- The commands below assume the starter project's `pom.xml` uses Maven.

## Compile and run

In the VS Code terminal (PowerShell):

```powershell
mvn compile
java -cp target/classes edu.starter.Main
```

To run the independent combatant checks:

```powershell
java -cp target/classes edu.starter.CombatantTest
```

Alternatively, open `Main.java` or `CombatantTest.java` in VS Code and choose **Run** above its `main` method.

## Game rules

- Encounter 1: enemy starts with 30 HP. Encounter 2: a new enemy starts with 45 HP.
- Player starts with 100 HP and 5 resources, retaining remaining values between encounters.
- `attack`: deal 10 damage. If the enemy survives, it counterattacks for 10.
- `defend`: reduce the next direct hit by 5. Defense does not stack.
- `special`: spend 3 resources and deal 15 damage. With fewer than 3 resources, the action fails without a counterattack.
- `recover`: restore 3 resources, up to 10. Recovery at 10 is rejected without a counterattack.
- Invalid inputs do not change combat state or trigger a counterattack.
- An enemy reduced to 0 HP cannot counterattack. Losing the first encounter ends the adventure.

## Example: verify player-state persistence

Run the game and enter these actions in Encounter 1:

```text
special
attack
attack
```

Expected start of Encounter 2:

```text
=== Encounter 2 ===
Player HP: 80
Enemy HP: 45
Resources: 2
```

See `TEST_EVIDENCE.md` for the verified acceptance tests and boundary checks.

# Week 2 — Class Design Note

For Week 2, I refactored my original game into three classes: `Main`, `Battle`, and `Combatant`. Each class has a different responsibility, making the program easier to understand and maintain.

The `Combatant` class represents both the player and enemies. It stores information such as name, current HP, maximum HP, resources, attack damage, and pending defense. These fields are private to prevent other classes from changing them directly. Instead, methods like `takeDamage()`, `spendResource()`, and `recoverResource()` control how values change and ensure they stay within their legal limits.

The `Battle` class manages a single encounter. It handles user input, processes the four actions, calculates when the enemy can counterattack, and determines whether the player wins or loses.

The `Main` class coordinates the adventure by creating the player and two enemies and running the encounters in sequence.

I create the player object only once and pass the same reference into both battles. This preserves the player's HP and resources, while each enemy has its own independent state.

I chose not to put keyboard input inside `Combatant`, because reading commands is the battle's responsibility, not an individual character's responsibility.

# Week 2 — AI Reflection

1. **Useful suggestion:** After I decided to use constructor overloading, AI suggested using `this()` to avoid duplicating initialization code between constructors, which I implemented.
2. **Rejected suggestion:** AI recommended keeping attack damage fixed at 10 in the `Battle` class, but I chose to store `attackDamage` in `Combatant` to support different attack strengths.
3. **Verification:** I tested that damaging Enemy A reduced its HP from 30 to 20 while Enemy B remained at 45 HP, confirming that each object has independent state.
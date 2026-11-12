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

# Java OOP Adventure — Weeks 3

## Project Overview

This project is a Java console-based combat game developed throughout the first three weeks of the Java OOP course.

The game consists of two consecutive encounters. The player starts with 100 HP and 5 resources, fights an enemy with 30 HP, chooses a reward after winning, and continues to a second enemy with 45 HP.

The same player object is used throughout the adventure, preserving its HP, resources, and upgrades between encounters.

### Combat Actions

The player can choose between four actions:

- **Attack:** Deals 10 damage initially. Can be upgraded to 12 through the Power reward.
- **Defend:** Reduces the next incoming attack by 5 damage.
- **Special:** Costs 3 resources and deals 15 damage.
- **Recover:** Restores 3 resources, up to a maximum of 10.

The enemy counterattacks for 10 damage after a successful player action if it survives. Invalid actions do not trigger a counterattack.

### Week 3 — Reward System

After winning Encounter 1, the player chooses exactly one reward:

1. **Recovery:** Restores 25 HP, capped at the maximum HP of 100.
2. **Power:** Permanently increases basic Attack damage by 2.

The reward is available only after winning the first encounter.

Invalid reward choices are rejected without changing the player's state, and the player is asked to choose again.

The `rewardClaimed` boolean prevents multiple rewards from being claimed through the same Battle object.

Choosing Recovery at full HP still consumes the reward.

## Project Structure

```text
src/main/java/edu/starter/
    Main.java
    Battle.java
    Combatant.java
    CombatantTest.java
    Week03Tests.java
```

### Class Responsibilities

**Main.java**

Creates the player and enemies, manages the two encounters, and determines the overall game outcome. It also creates the Scanner shared between battles.

**Battle.java**

Manages the battle loop, user input, combat actions, counterattacks, and reward selection after the first battle.

**Combatant.java**

Stores each character's state, including HP, maximum HP, resources, attack damage, and pending defense. It provides methods for safely modifying these values.


## How to Compile and Run

### Requirements
- Java JDK 17 or later
- Java installed and available in PATH
- VS Code or another Java IDE

### Compile

Run from the project root:

javac -d target/classes src/main/java/edu/starter/*.java

### Run the game

java -cp target/classes edu.starter.Main

### Run Week 3 tests

java -cp target/classes edu.starter.Week03Tests

## Example Gameplay

Starting state:

```text
Player HP: 100
Enemy HP: 30
Resources: 5
```

Encounter 1:

```text
special
attack
attack
```

Result:

```text
Player HP: 80
Resources: 2
Enemy HP: 0
```

The player then selects a reward.

Choosing Power:

```text
2
```

Encounter 2 begins with:

```text
Player HP: 80
Resources: 2
Attack Damage: 12
Enemy HP: 45
```

Choosing Recovery instead would start Encounter 2 with 100 HP, 2 resources, and Attack damage of 10.

## Testing

The project includes tests for:

- Independent enemy objects
- HP and resource persistence
- Recovery and Power rewards
- Invalid reward selections
- Prevention of duplicate reward claims
- Resource limits and HP boundaries
- Defense reset
- Failed Special attacks
- Victory and defeat behavior

The Week 3 test suite covers T1–T9 and regression checks from earlier weeks.

See `TEST_EVIDENCE.md` for the detailed results.

---

## Week 3 — Design Note

For Week 3, I extended my existing three-class design by adding a reward system while keeping the combat mechanics unchanged. I decided to place the `reward()` method in `Battle` rather than `Main`, because I consider choosing a reward part of completing a battle. `Main` still controls the sequence of encounters and calls the reward method only after the first victory.

I used `rewardClaimed` to prevent a reward from being selected twice within the same battle object. I also replaced the recursive retry logic with a `while` loop, allowing invalid choices to be retried without modifying the player's state. Recovery and Power modify the existing player object, preserving its identity and resources.
I also introduced a Reward enum to represent the two valid reward types (RECOVERY and POWER), separating input interpretation from reward execution and avoiding the use of raw string values in the reward logic.

I documented preconditions, postconditions, and invariants in `Combatant`. Preconditions define the caller's responsibilities, postconditions describe the guaranteed results, and invariants define valid object states, such as HP and resource limits.

I chose to express these rules through general contracts rather than relying only on defensive checks or individual assertions. Contracts describe the expected behavior for all valid inputs instead of checking only specific edge cases. This keeps responsibilities clear and avoids unnecessary duplicated validation. However, I still used tests to verify that the implementation follows these contracts.

## Week 3 — AI Reflection

1. **Useful suggestion:** AI suggested adding a `rewardClaimed` boolean to prevent multiple rewards from being selected. I implemented it and used it as the condition for the reward-selection loop.
2. **Rejected suggestion:** AI recommended moving reward management to `Main`, but I chose to keep the `reward()` method in `Battle` because I consider reward selection part of finishing an encounter.
3. **Verification:** I independently tested that invalid reward input does not change the player's state and that a second reward attempt is rejected. I also verified that Power increases basic Attack damage to 12 while Special remains at 15.

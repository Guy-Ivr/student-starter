
package edu.starter;

public class CombatantTest {

    public static void main(String[] args) {
// Week 3 - T1: Recovery from 90 HP
Combatant player1 = new Combatant("Player1", 100, 5, 10);
player1.takeDamage(10);
player1.increaseHp(25);
System.out.println("T1 HP: " + player1.getHp());

// Week 3 - T2: Recovery at full HP
Combatant player2 = new Combatant("Player2", 100, 5, 10);
boolean changed = player2.increaseHp(25);
System.out.println("T2 HP: " + player2.getHp());
System.out.println("T2 HP changed: " + changed);
    }
}

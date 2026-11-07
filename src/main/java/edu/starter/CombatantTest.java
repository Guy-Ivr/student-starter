
package edu.starter;

public class CombatantTest {

    public static void main(String[] args) {

        // T1: Enemy independence
        Combatant enemyA = new Combatant("Enemy A", 30);
        Combatant enemyB = new Combatant("Enemy B", 45);

        enemyA.takeDamage(10);

        System.out.println("T1 - Enemy A HP: " + enemyA.getHp());
        System.out.println("T1 - Enemy B HP: " + enemyB.getHp());

        // T4: Defense reset
        Combatant player = new Combatant("Player", 100, 5, 10);

        player.setGuard();
        player.clearDefense();
        player.takeDamage(10);

        System.out.println("T4 - Player HP: " + player.getHp());

        // Resource boundary: Special with 2 resources
        Combatant player2 = new Combatant("Player", 100, 2, 10);

        boolean specialFailed = player2.spendResource(3);

        System.out.println("Special with 2: " + specialFailed);
        System.out.println("Remaining resources: " + player2.getResource());

        // Resource boundary: Special with exactly 3
        Combatant player3 = new Combatant("Player", 100, 3, 10);

        boolean specialSucceeded = player3.spendResource(3);

        System.out.println("Special with 3: " + specialSucceeded);
        System.out.println("Remaining resources: " + player3.getResource());

        // Recover from 9 resources
        Combatant player4 = new Combatant("Player", 100, 9, 10);

        player4.recoverResource();

        System.out.println("Recover from 9: " + player4.getResource());

        // Recover when already at 10
        boolean recoverAtMax = player4.recoverResource();

        System.out.println("Recover at 10: " + recoverAtMax);

        // HP cannot become negative
        Combatant enemyC = new Combatant("Enemy C", 5);

        enemyC.takeDamage(10);

        System.out.println("HP after 10 damage: " + enemyC.getHp());
    }
}

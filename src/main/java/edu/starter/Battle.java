package edu.starter;

import java.util.Scanner;

public class Battle {
    private enum Reward {
    RECOVERY,
    POWER
}
    private Scanner scanner;
    private Combatant player;
    private Combatant enemy;
    private boolean rewardClaimed = false;

    public Battle(Combatant player, Combatant enemy, Scanner scanner) {
        this.player = player;
        this.enemy = enemy;
        this.scanner = scanner;
    }

    public boolean play() {
        while (player.getHp() > 0 && enemy.getHp() > 0) {

            System.out.println("Player HP: " + player.getHp());
            System.out.println("Enemy HP: " + enemy.getHp());
            System.out.println("Resources: " + player.getResource());

            System.out.print("Enter action: ");
            String action = scanner.nextLine();

            if (action.equals("attack")) {
                enemy.takeDamage(player.getAttackDamage());

                if (enemy.getHp() > 0) {
                    player.takeDamage(enemy.getAttackDamage());
                }
            }
            else if (action.equals("defend")) {
                player.setGuard();
                player.takeDamage(enemy.getAttackDamage());
            } 
            else if (action.equals("special")) {
                if (player.spendResource(3)) {
                    enemy.takeDamage(15);

                    if (enemy.getHp() > 0) {
                        player.takeDamage(enemy.getAttackDamage());
                    }
                } else {
                    System.out.println("Not enough resources for special attack.");
                }
            }
            else if (action.equals("recover")) {
                if (player.recoverResource()) {
                    player.takeDamage(enemy.getAttackDamage());
                } else {
                    System.out.println("Resource is already full.");
                }
            }
            else {
                System.out.println("Invalid action.");
            }
        }

    return player.getHp() > 0;
    }


/**
 * Preconditions:
 * - Encounter 1 was won.
 * - Player is alive.
 * - No reward has been claimed.
 *
 * Postconditions:
 * - Exactly one valid reward is claimed.
 *
 * RECOVERY:
 * - hp = min(maxHp, oldHp + 25)
 * - Resource and attack damage remain unchanged.
 * - Reward is consumed even if HP was already full.
 *
 * POWER:
 * - attackDamage = oldAttackDamage + 2
 * - HP and resource remain unchanged.
 *
 * Invalid input does not change player state.
 * A second reward attempt is rejected.
 */

    public void reward() {
        if (enemy.getHp() > 0 || player.getHp() <= 0 || rewardClaimed) {
        System.out.println("Reward not available.");
        return;}
        while (!rewardClaimed) {
        System.out.println("Choose a reward, choose 1 or 2:\n1.recovery - Increase current HP by 25\n2.power - Increase attack damage by 2\nenter choice: ");
        String choice = scanner.nextLine();
        Reward selectedReward;

        switch (choice) {
            case "1":
            case "recovery":
                selectedReward = Reward.RECOVERY;
                break;
            case "2":
            case "power":
                selectedReward = Reward.POWER;
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                continue;
        }
        switch (selectedReward) {
            case RECOVERY:
                player.increaseHp(25);
                break;
            case POWER:
                player.increaseAttackDamage(2);
                break;
        }

        rewardClaimed = true;
    }
}
}


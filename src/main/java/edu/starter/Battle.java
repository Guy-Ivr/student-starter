package edu.starter;

import java.util.Scanner;

public class Battle {
    private Scanner scanner;
    private Combatant player;
    private Combatant enemy;

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
}

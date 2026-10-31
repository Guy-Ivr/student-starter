package edu.starter;

import java.util.Scanner;

/** Student-owned game entry point; replace this message with your own implementation. */
public final class Main {
    static int attack(int enemy_HP) {
        return Math.max(0, enemy_HP - 10);
    }

    static int counter_attack(int player_HP, boolean pending_defence) {
        if (pending_defence) {
            return Math.max(0, player_HP - 5);
        }
        return Math.max(0, player_HP - 10);
    }

    static int special(int enemy_HP) {
        return Math.max(0, enemy_HP - 15);
    }

    static int recover(int resource_count) {
        return Math.min(10, resource_count + 3);
    }

    public static void main(String[] args) {
        int player_HP = 100;
        int enemy_HP = 30;
        int resource_count = 5;
        boolean pending_defence = false;

        try (Scanner scanner = new Scanner(System.in)) {
            while (player_HP > 0 && enemy_HP > 0) {
                System.out.println("Player HP: " + player_HP);
                System.out.println("Enemy HP: " + enemy_HP);
                System.out.println("Resource count: " + resource_count);
                System.out.println("Pending defence: " + pending_defence);

                System.out.print("Enter your action (attack, special, defend, recover): ");
                String action = scanner.nextLine();

                if (action.equals("attack")) {
                    enemy_HP = attack(enemy_HP);
                    if (enemy_HP > 0) {
                        player_HP = counter_attack(player_HP, pending_defence);
                    }
                } else if (action.equals("special")) {
                    if (resource_count >= 3) {
                        resource_count -= 3;
                        enemy_HP = special(enemy_HP);
                        if (enemy_HP > 0) {
                            player_HP = counter_attack(player_HP, pending_defence);
                        }
                    } else {
                        System.out.println("Not enough resources for special attack.");
                    }
                } else if (action.equals("defend")) {
                    pending_defence = true;
                    player_HP = counter_attack(player_HP, pending_defence);
                } else if (action.equals("recover")) {
                    resource_count = recover(resource_count);
                    player_HP = counter_attack(player_HP, pending_defence);
                } else {
                    System.out.println("Invalid action. Please try again.");
                }

                // Reset pending defence after the turn
                pending_defence = false;
            }
        }

        if (player_HP > 0) {
            System.out.println("You win!");
        } else {
            System.out.println("You lose!");
        }
    }

}

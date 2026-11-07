public static void main(String[] args) {
    try (Scanner scanner = new Scanner(System.in)) {
    Combatant player = new Combatant("Player", 100, 5, 10);
    Combatant enemyA = new Combatant("Enemy A", 30);

    Battle battle = new Battle(player, enemyA, scanner);

    boolean won = battle.play();

    if (won) {
        System.out.println("You won first battle!");
        player.clearDefense();
        Combatant enemyB = new Combatant("Enemy B", 45);
        Battle battle2 = new Battle(player, enemyB, scanner);
        boolean won2 = battle2.play();
        if (won2) {
            System.out.println("You won second battle!");
        } else {
            System.out.println("You lose after second battle!");
        }
    } else {
        System.out.println("You lose!");
    }
}
}

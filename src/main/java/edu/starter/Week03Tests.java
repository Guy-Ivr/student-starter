package edu.starter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/** Week 3 acceptance tests without JUnit or changes to game code. */
public class Week03Tests {
    private static int passed = 0;
    private static int failed = 0;

    private static void check(String name, boolean ok) {
        System.out.println((ok ? "PASS " : "FAIL ") + name);
        if (ok) passed++; else failed++;
    }

    private static Scanner input(String actions) {
        return new Scanner(actions);
    }

    private static String capture(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (PrintStream stream = new PrintStream(buffer, true, StandardCharsets.UTF_8)) {
            System.setOut(stream);
            action.run();
        } finally {
            System.setOut(original);
        }
        return buffer.toString(StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

    private static String runMain(String commands) {
        var oldIn = System.in;
        try {
            System.setIn(new ByteArrayInputStream(commands.getBytes(StandardCharsets.UTF_8)));
            return capture(() -> Main.main(new String[0]));
        } finally {
            System.setIn(oldIn);
        }
    }

    public static void main(String[] args) {
        // T1: Recovery from 90 HP -> 100; resource and Attack unchanged.
        Combatant p1 = new Combatant("P", 100, 5, 10);
        p1.takeDamage(10);
        Combatant e1 = new Combatant("E", 30);
        e1.takeDamage(30);  // Simulate first-encounter victory.
        Battle b1 = new Battle(p1, e1, input("1\n"));
        capture(b1::reward);
        check("T1 Recovery at 90 HP", p1.getHp() == 100 && p1.getResource() == 5 && p1.getAttackDamage() == 10);

        // T2: At full HP the Recovery reward is still consumed.
        Combatant p2 = new Combatant("P", 100, 5, 10);
        Combatant e2 = new Combatant("E", 30);
        e2.takeDamage(30);
        Battle b2 = new Battle(p2, e2, input("1\n"));
        capture(b2::reward);
        String second = capture(b2::reward);
        check("T2 Recovery at max HP consumed", p2.getHp() == 100 && p2.getResource() == 5
                && p2.getAttackDamage() == 10 && second.contains("Reward not available"));

        // T3: POWER boosts basic Attack to 12, not Special (15).
        Combatant p3 = new Combatant("P", 100, 5, 10);
        Combatant e3 = new Combatant("E", 30);
        e3.takeDamage(30);
        Battle b3 = new Battle(p3, e3, input("2\n"));
        capture(b3::reward);
        Combatant attackTarget = new Combatant("Attack target", 45);
        Battle powerAttack = new Battle(p3, attackTarget, input("attack\nattack\nattack\nattack\n"));
        capture(powerAttack::play);
        Combatant specialTarget = new Combatant("Special target", 15);
        Battle specialBattle = new Battle(p3, specialTarget, input("special\n"));
        int resourceBefore = p3.getResource();
        capture(specialBattle::play);
        check("T3 Power 12; Special still 15", p3.getAttackDamage() == 12
                && attackTarget.getHp() == 0 && specialTarget.getHp() == 0
                && p3.getResource() == resourceBefore - 3);

        // T4: Invalid reward entry must retry with no state change, then POWER once.
        Combatant p4 = new Combatant("P", 100, 5, 10);
        Combatant e4 = new Combatant("E", 30);
        e4.takeDamage(30);
        Battle b4 = new Battle(p4, e4, input("invalid\n2\n"));
        String t4out = capture(b4::reward);
        check("T4 Invalid choice retries then POWER", t4out.contains("Invalid choice")
                && p4.getAttackDamage() == 12 && p4.getHp() == 100 && p4.getResource() == 5);

        // T5: Reward before victory is rejected.
        Combatant p5 = new Combatant("P", 100, 5, 10);
        Battle b5 = new Battle(p5, new Combatant("E", 30), input("2\n"));
        String t5out = capture(b5::reward);
        check("T5 No reward before victory", t5out.contains("Reward not available")
                && p5.getAttackDamage() == 10 && p5.getHp() == 100 && p5.getResource() == 5);
        // T6: A second reward attempt on the same battle must fail.
        Combatant p6 = new Combatant("P", 100, 5, 10);
        Combatant e6 = new Combatant("E", 30);
        e6.takeDamage(30);
        Battle b6 = new Battle(p6, e6, input("2\n1\n"));
        capture(b6::reward);
        String t6out = capture(b6::reward);
        check("T6 Second reward attempt rejected", t6out.contains("Reward not available")
                && p6.getAttackDamage() == 12 && p6.getHp() == 100);

        // T7: Losing encounter 1 ends adventure before reward and encounter 2.
        String t7out = runMain("defend\n".repeat(20));
        check("T7 Lose first encounter: stop", t7out.contains("You lose!")
                && !t7out.contains("Choose a reward") && !t7out.contains("=== Encounter 2 ==="));

        // T8: Same player survives to encounter 2 at 80 HP / resource 2, POWER adds 2 Attack.
        String t8out = runMain("special\nattack\nattack\n2\nattack\nattack\nattack\nattack\n");
        int secondStart = t8out.indexOf("=== Encounter 2 ===");
        String afterSecondStart = secondStart < 0 ? "" : t8out.substring(secondStart);
        check("T8 Preserve 80 HP / 2 resources", afterSecondStart.contains("Player HP: 80\nEnemy HP: 45\nResources: 2"));

        // T9: Four POWER attacks (12 each) defeat a 45-HP enemy.
        // Only the first three permit counterattacks: player 100 -> 70, NOT 60.
        Combatant t9player = new Combatant("P", 100, 5, 12);
        Combatant t9enemy = new Combatant("Enemy B", 45);
        Battle t9battle = new Battle(t9player, t9enemy, input("attack\n".repeat(4)));
        final boolean[] t9Won = new boolean[1];
        capture(() -> t9Won[0] = t9battle.play());
        check("T9 Final victory / no finishing counterattack", t8out.contains("You won second battle!")
                && t9Won[0] && t9enemy.getHp() == 0 && t9player.getHp() == 70);

        // Earlier-week regression: independent objects, invalid Special, resource cap, HP bound, defense clear.
        Combatant a = new Combatant("A", 30), b = new Combatant("B", 45);
        a.takeDamage(10);
        check("Regression: enemy independence", a.getHp() == 20 && b.getHp() == 45);
        Combatant lowResource = new Combatant("P", 100, 2, 10);
        Battle failedSpecial = new Battle(lowResource, new Combatant("E", 10), input("special\nattack\n"));
        capture(failedSpecial::play);
        check("Regression: failed Special has no counterattack", lowResource.getHp() == 100 && lowResource.getResource() == 2);
        Combatant res = new Combatant("P", 100, 9, 10);
        boolean firstRecover = res.recoverResource(), secondRecover = res.recoverResource();
        check("Regression: resource capped at 10", firstRecover && !secondRecover && res.getResource() == 10);
        Combatant hp = new Combatant("E", 5);
        hp.takeDamage(10);
        check("Regression: HP capped at 0", hp.getHp() == 0);
        Combatant guard = new Combatant("P", 100, 5, 10);
        guard.setGuard(); guard.clearDefense(); guard.takeDamage(10);
        check("Regression: cleared defense", guard.getHp() == 90);

        System.out.println("\nRESULT: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }
}

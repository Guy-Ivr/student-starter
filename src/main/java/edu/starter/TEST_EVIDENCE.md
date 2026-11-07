# Week 2 — Test Evidence

Tests for *Two Encounters, One Evolving Player*. These results were checked during development using the console game and a separate `CombatantTest.java` runner. The commands below assume the project is compiled with Maven (`mvn compile`) and run from the project root.

## Acceptance tests (T1–T7)

| Test | Reproducible setup / input | Expected result | Observed result | Status |
| --- | --- | --- | --- | --- |
| T1 / W02-OBJ-01 | Create Enemy A at 30 HP and Enemy B at 45 HP; apply `enemyA.takeDamage(10)` | A=20, B=45 | A=20, B=45 | PASS |
| T2 / W02-ENC-01 | In Encounter 1 enter `special`, `attack`, `attack` | Encounter 2: player HP=80, resource=2 | Earlier console run showed HP=80, resource=2 | PASS |
| T3 / W02-ENC-02 | Defeat Enemy A, start Encounter 2 | New enemy HP=45 | Console showed Enemy B at 45 HP | PASS |
| T4 / W02-ENC-03 | `setGuard()`, `clearDefense()`, then `takeDamage(10)` on a 100-HP player | Defense cleared, HP=90 | Output `true`, `false`, `90` in focused check; expanded test HP=90 | PASS |
| T5 / W01-INV-01 | In Encounter 1 enter `special`, then `special` again | Second attempt rejected with player HP=90, resource=2, enemy HP=15; no counterattack | Console showed rejection and unchanged state | PASS |
| T6 / W01-END | From Enemy A=30 HP, enter `attack` three times | Enemy reaches 0; player remains at 80 HP | Console showed victory and Encounter 2 starting with player HP=80 | PASS |
| T7 / TERM | In Encounter 1 enter `defend` 20 times | Player reaches 0 HP; no Encounter 2 | Confirmed loss and no second encounter | PASS |

## Combatant boundary checks

Run `CombatantTest.java`:

```powershell
mvn compile
java -cp target/classes edu.starter.CombatantTest
```

Observed output:

```text
T1 - Enemy A HP: 20
T1 - Enemy B HP: 45
T4 - Player HP: 90
Special with 2: false
Remaining resources: 2
Special with 3: true
Remaining resources: 0
Recover from 9: 10
Recover at 10: false
HP after 10 damage: 0
```

These checks show that:

- Two combatants have independent HP.
- Defense can be explicitly cleared before the next hit.
- Spending 3 resources fails at 2, succeeds at 3, and never reduces resources below zero.
- Recovering from 9 caps resources at 10; recovering at 10 is rejected.
- Damage cannot make HP negative (a 5-HP enemy receiving 10 damage ends at 0 HP).

## Invalid input regression

In the console game, entering `10`, `speical`, and `pending defence` produced `Invalid action.` with unchanged HP/resources and no counterattack. This verifies that an invalid choice does not consume a turn.

## Notes

- `T4` was verified through a deliberately prepared combatant state: under the current battle rules, using Defend immediately causes a counterattack that consumes pending defense. The separate check proves that `clearDefense()` works even if defense remains set before a new encounter.
- `T7` was checked in the interactive console run; its full 20-turn transcript was not retained in this file. The result was reported as passing.
- Constructor validation (nonpositive maximum HP, resource outside 0–10, negative attack damage) exists in `Combatant.java` but has **not** been separately recorded as an executed negative-input test here.
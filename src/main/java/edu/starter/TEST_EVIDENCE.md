# Week 3 — Test Evidence

## Project: Java OOP Adventure

### Overview

This document describes the acceptance tests and regression tests for Week 3.

The goal is to verify that the reward system works correctly, that contracts and invariants are preserved, and that all combat mechanics from Weeks 1 and 2 continue to work.

Tests are implemented in `Week03Tests.java`, using the existing `Combatant`, `Battle`, and `Main` classes.

## 1. Week 3 Acceptance Tests

### T1 — Recovery Reward at 90 HP

**Initial state:**
- Player HP: 90/100
- Resources: 5
- Attack damage: 10

**Action:** Select Recovery (+25 HP).

**Expected result:**
- HP becomes 100 (capped at maxHp).
- Resources remain 5.
- Attack damage remains 10.

**Result:** PASS — HP restored to 100 without exceeding the maximum.

### T2 — Recovery Reward at Full HP

**Initial state:**
- Player HP: 100/100
- Resources: 5
- Attack damage: 10

**Action:** Select Recovery.

**Expected result:**
- HP remains 100.
- Resources and Attack remain unchanged.
- The reward is consumed even though HP did not increase.

**Result:** PASS — Recovery leaves HP unchanged and the reward is considered claimed.

### T3 — Power Reward

**Initial state:**
- Basic Attack: 10
- Special damage: 15

**Action:** Select Power (+2 Attack).

**Expected result:**
- Basic Attack becomes 12.
- Special still deals exactly 15 damage.
- HP and resources remain unchanged.

**Result:** PASS — Basic Attack increased to 12 while Special remained 15.

### T4 — Invalid Reward Input

**Initial state:**
- Encounter 1 completed successfully.
- No reward claimed.

**Input:**
1. Enter an invalid choice.
2. Enter `2` for Power.

**Expected result:**
- Invalid input displays an error.
- No player state changes after the invalid input.
- The reward menu is displayed again.
- Power is applied exactly once after the valid choice.

**Result:** PASS — Invalid input was rejected and a valid choice was accepted afterward.

### T5 — Reward Before Victory

**Initial state:**
- Encounter 1 has not been won.
- Enemy is still alive.

**Action:** Attempt to request a reward.

**Expected result:**
- Reward is rejected.
- HP, resources, and Attack remain unchanged.

**Result:** PASS — Reward was unavailable before victory.

### T6 — Duplicate Reward Attempt

**Initial state:**
- Encounter 1 completed successfully.
- One reward already claimed.

**Action:** Attempt to claim another reward using the same Battle object.

**Expected result:**
- Second request is rejected.
- No additional HP or Attack increase.
- `rewardClaimed` remains true.

**Result:** PASS — A second reward request on the same Battle object was rejected.

**Design note:** `rewardClaimed` belongs to each Battle instance. The current game flow only offers a reward through the first Battle object.

### T7 — Losing Encounter 1

**Initial state:**
- Player HP: 100
- Enemy A HP: 30

**Input:** Repeat Defend until the player reaches 0 HP.

**Expected result:**
- The player loses Encounter 1.
- No reward is offered.
- Encounter 2 does not start.

**Result:** PASS — The adventure ended after the first defeat.

### T8 — Player State Persistence

**Initial state:**
- Player HP: 100
- Resources: 5
- Enemy A HP: 30

**Input:**
1. `special`
2. `attack`
3. `attack`

**Expected result after Encounter 1:**
- Player HP: 80
- Resources: 2
- Enemy A HP: 0

**Expected Encounter 2 state:**

If Power is selected:
- HP: 80
- Resources: 2
- Attack: 12

If Recovery is selected:
- HP: 100
- Resources: 2
- Attack: 10

Enemy B starts with 45 HP in both cases.

**Result:** PASS — Player state persisted, with only the selected reward changing the appropriate field.

### T9 — Final Victory and Counterattack Prevention

**Initial state:**
- Encounter 2 is active.
- Player and enemy are alive.

**Action:** Defeat Enemy B with a finishing attack.

**Expected result:**
- Enemy HP becomes 0.
- Defeated enemy does not counterattack.
- The adventure ends with a victory message.
- No additional turn or reward is offered.

**Result:** PASS — Finishing attack ended combat without a counterattack.

---

## 2. Regression Tests — Weeks 1 and 2

### R1 — Independent Enemy Objects

**Setup:**
- Enemy A starts at 30 HP.
- Enemy B starts at 45 HP.

**Action:** Deal 10 damage to Enemy A.

**Expected:**
- Enemy A: 20 HP
- Enemy B: 45 HP

**Result:** PASS

### R2 — Special With Insufficient Resources

**Setup:** Player has 2 resources.

**Action:** Attempt Special (cost: 3).

**Expected:**
- Special is rejected.
- Resources remain 2.
- Enemy HP is unchanged.
- No counterattack occurs.

**Result:** PASS

### R3 — Resource Cap

**Setup:** Player has 9 resources.

**Action:** Use Recover.

**Expected:**
- Resources become 10, not 12.
- Recover at 10 resources is rejected.

**Result:** PASS

### R4 — HP Lower Bound

**Setup:** Enemy has 5 HP.

**Action:** Deal 10 damage.

**Expected:**
- Enemy HP becomes 0, not -5.

**Result:** PASS

### R5 — Defense Reset

**Setup:**
- Player activates pending defense.
- Defense is cleared before the next encounter.

**Action:** Enemy deals 10 damage.

**Expected:**
- Full 10 damage is applied.
- No defense carries into the next encounter.

**Result:** PASS

---

## 3. Contracts and Invariants Verification

The tests also verify the important invariants and postconditions defined in `Combatant.java`.

### HP Invariant

`0 <= hp <= maxHp`

Verified through:
- Damage that would otherwise reduce HP below zero.
- Recovery that would otherwise exceed maximum HP.

### Resource Invariant

`0 <= resource <= 10`

Verified through:
- Special with insufficient resources.
- Recover near maximum resources.
- Recover at maximum resources.

### Attack Damage Contract

Power increases basic Attack damage by 2 without changing Special damage, HP, or resources.

### Reward Contract

The reward system rejects invalid input, requires victory before offering a reward, and prevents repeated rewards on the same Battle object.

---

## 4. Test Execution

The tests are implemented without JUnit, using Java's existing classes and simulated console input.

### Compile

```bash
mvn compile
```

### Run

```bash
java -cp target/classes edu.starter.Week03Tests
```

### Reported Test Summary

| Test group | Passed | Failed |
|---|---:|---:|
| Week 3 acceptance tests T1–T9 | 9 | 0 |
| Regression tests R1–R5 | 5 | 0 |
| **Total** | **14** | **0** |

The test suite was previously reported as passing all 14 checks against the supplied Java implementation.

---

## 5. Conclusion

The tests demonstrate the expected behavior of the Week 3 reward system and verify that the original combat mechanics remain functional.

The implementation maintains the same player object across both encounters, applies the selected reward to that object, and preserves the legal HP and resource boundaries.

The project uses contracts to describe expected behavior and automated tests to check that the implementation follows those contracts.

## 6. Week03Tests Results
PASS T1 Recovery at 90 HP
PASS T2 Recovery at max HP consumed
PASS T3 Power 12; Special still 15
PASS T4 Invalid choice retries then POWER
PASS T5 No reward before victory
PASS T6 Second reward attempt rejected
PASS T7 Lose first encounter: stop
PASS T8 Preserve 80 HP / 2 resources
PASS T9 Final victory / no finishing counterattack
PASS Regression: enemy independence
PASS Regression: failed Special has no counterattack
PASS Regression: resource capped at 10
PASS Regression: HP capped at 0
PASS Regression: cleared defense

RESULT: 14 passed, 0 failed
package edu.starter;
/**
 * Class Invariants:
 * 1. 0 <= hp <= maxHp
 * 2. 0 <= resource <= 10
 * 3. attackDamage is a non-negative integer that never decreases
 * 4. pendingDefense is true only if the combatant has chosen to defend and has not yet taken damage
 * 5. name is a non-null, non-empty string
 * 6. maxHp is a positive integer
 */
public class Combatant {

        private String name;
        private int hp;
        private int maxHp;
        private int resource;
        private boolean pendingDefense;
        private int attackDamage;

public Combatant(String name, int maxHp) {
    this(name, maxHp, 0, 10);
}

/**
 * Preconditions:
 * - name != null && !name.isBlank()
 * - maxHp > 0
 * - 0 <= resource <= 10
 * - attackDamage >= 0
 *
 * Postconditions:
 * - hp == maxHp
 * - pendingDefense == false
 * - All fields are initialized.
 */
public Combatant(String name, int maxHp, int resource, int attackDamage) {
    this.name = name;
    this.maxHp = maxHp;
    this.hp = maxHp;
    this.resource = resource;
    this.pendingDefense = false;
    this.attackDamage = attackDamage;
}

public String getName() {
    return name;
}

public int getHp() {
    return hp;
}

public int getMaxHp() {
    return maxHp;
}

public int getAttackDamage() {
    return attackDamage;
}

public int getResource() {
    return resource;
}

public boolean isPendingDefense() {
    return pendingDefense;
}

/**
 * Precondition:
 * damage >= 0
 *
 * Postconditions:
 * hp = max(0, oldHp - damage) if pendingDefense is false
 * hp = max(0, oldHp - max(0, damage - 5))
 *      if pendingDefense was true.
* pendingDefense = false if it was active before the hit.
 * resource and attackDamage remain unchanged.
 *
 * Invariant:
 * 0 <= hp <= maxHp
 */
public void takeDamage(int damage) {
    if (pendingDefense) {
        damage = Math.max(0, damage - 5);
        pendingDefense = false;
    }

    hp = Math.max(0, hp - damage);
}

public void setGuard() {
    pendingDefense = true;
}

public boolean spendResource(int amount) {
    if (amount < 0 || resource < amount) {
        return false;
    }

    resource -= amount;
    return true;
}
/**
 * Postconditions:
 * - If resource < 10:
 *   resource = min(10, oldResource + 3)
 *   returns true.
 * - If resource == 10:
 *   resource remains unchanged
 *   returns false.
 * - HP and attackDamage remain unchanged.
 *
 * Invariant:
 * 0 <= resource <= 10
 */
public boolean recoverResource() {
    if (resource >= 10) {
        return false;
    }

    resource = Math.min(10, resource + 3);
    return true;
}

public void clearDefense() {
    pendingDefense = false;
}

/**
 * Precondition:
 * amount >= 0
 *
 * Postconditions:
 * attackDamage = oldAttackDamage + amount
 * hp and resource remain unchanged.
 *
 * Invariant:
 * attackDamage never decreases.
 */
public void increaseAttackDamage(int amount) {
    attackDamage += amount;
}

/**
 * Precondition:
 * amount >= 0
 *
 * Postconditions:
 * hp = min(maxHp, oldHp + amount)
 * resource and attackDamage remain unchanged.
 * if hp was already at maxHp, it remains unchanged and the method returns false.
 * 
 * Invariant:
 * 0 <= hp <= maxHp
 */
public boolean increaseHp(int amount) {
    if (hp >= maxHp) {
        return false;
    }
    hp += Math.min(amount, maxHp - hp);
    return true;
}
}
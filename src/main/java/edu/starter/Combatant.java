package edu.starter;

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
public Combatant(String name, int maxHp, int resource, int attackDamage) {
    if (maxHp <= 0) {
        throw new IllegalArgumentException("Max HP must be positive");
    }

    if (resource < 0 || resource > 10) {
        throw new IllegalArgumentException("Resources must be between 0 and 10");
    }

    if (attackDamage < 0) {
        throw new IllegalArgumentException("Attack damage cannot be negative");
    }

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


public void takeDamage(int damage) {
    if (damage < 0) {
        throw new IllegalArgumentException("Damage cannot be negative");
    }

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
    }   
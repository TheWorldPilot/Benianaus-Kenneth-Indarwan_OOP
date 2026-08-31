package com.benianaus.frontend;

public class Test {
    public static void main(String[] args) {
        System.out.println("=== TOUHOU OOP PRACTICUM - MODULE 1: BASIC CLASSES & OBJECT INTERACTION ===");

        // Instantiating objects (Player and Enemy)
        Player reimu = new Player("Reimu Hakurei", 100, 15, 3);
        Enemy fairyBoss = new Enemy("Cirno (Stage 2 Boss)", 50);

        System.out.println("\n--- Initial Battle State ---");
        System.out.println("Player: " + reimu.name + " | HP: " + reimu.hp + " | Power: " + reimu.power + " | SpellCards: " + reimu.spellCards);
        System.out.println("Enemy:  " + fairyBoss.name + " | HP: " + fairyBoss.hp);

        System.out.println("\n--- Turn 1: Player Shoots Enemy ---");
        reimu.shoot(fairyBoss);

        System.out.println("\n--- Turn 2: Enemy Counter-attacks ---");
        fairyBoss.attack(reimu, 30);

        System.out.println("\n--- Turn 3: Player Shoots Enemy Finishing Blow ---");
        reimu.shoot(fairyBoss);

        System.out.println("\n--- Turn 4: Enemy Deals Fatal Damage to Reimu ---");
        fairyBoss.attack(reimu, 80);

        System.out.println("\n=== Battle Simulation Complete ===");
    }
}

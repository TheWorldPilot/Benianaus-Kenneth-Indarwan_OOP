package com.benianaus.frontend;


public class Player {

    public String name;
    public int hp;
    public int power;
    public int spellCards;

    public Player(String name, int hp, int power, int spellCards) {
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
    }

    public void takeDamage(int damage) {
        // 1. Kurangi hp sebesar nilai damage.
        // 2. HP tidak boleh bernilai negatif.
        // 3. Jika HP masih lebih dari 0, tampilkan HP yang tersisa dalam format: [PlayerName] took [damage] damage! Remaining HP: [hp]
        // 4. Jika HP menjadi 0, tampilkan pesan bahwa Player telah dikalahkan.

        this.hp = this.hp - damage;

        if (this.hp < 0) {
            this.hp = 0;
        }

        if (this.hp > 0) {
            System.out.println(this.name + " took " + damage + " damage! Remaining HP: " + this.hp);
        } else {
            System.out.println("Player lost. Look, i'm sorry!");
        }

    }

    public void shoot(Enemy target) {
        // 1. Buat int bernama damage yang dihitung dengan menambahkan power sebanyak 10.
        // 2. Tampilkan informasi bahwa Player menembak Enemy dalam format: [name] shoots [TargetName] dealing [damage] DMG!
        // 3. Panggil method takeDamage() milik object Enemy.

        int damage = power + 10;

        System.out.println(name + " shoots " + target + " dealing " + damage + " DMG!");

        target.takeDamage(damage);
    }

    public boolean isAlive() {
        // 1. Kembalikan true jika hp > 0 dan false jika sebaliknya
        if (hp > 0) {
            return true;
        } else {
            return false;
        }
    }
}

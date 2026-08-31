package com.benianaus.frontend;

public class Enemy {

    String name;
    int hp;
    int maxHp;

    public Enemy(String name, int hp) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
    }

    public void takeDamage(int damage) {
        // 1. Kurangi hp sebesar nilai damage.
        // 2. HP tidak boleh kurang dari 0.
        // 3. Tampilkan HP saat ini dalam format: [EnemyName] took [damage] damage! HP: [currentHP]/[maxHP]
        // 4. Jika HP mencapai 0, tampilkan bahwa Enemy telah dikalahkan dalam format: [EnemyName] was defeated!

        this.hp = this.hp - damage;

        if (this.hp < 0) {
            this.hp = 0;
        }

        if (this.hp > 0) {
            System.out.println(this.name + " took " + damage + " damage! Remaining HP: " + this.hp + "/" +this.maxHp);
        } else {
            System.out.println(this.name + " was defeated!");
        }
    }

    public void attack(Player player, int damage) {
        // 1. Tampilkan informasi bahwa Enemy menyerang Player dalam format: [EnemyName] unleashes bullet barrage on [PlayerName]!
        // 2. Panggil takeDamage() milik Player menggunakan damage yang diberikan.
        System.out.println(name + " unleashes bullet barrage on " + player.name);

        player.takeDamage(damage);
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

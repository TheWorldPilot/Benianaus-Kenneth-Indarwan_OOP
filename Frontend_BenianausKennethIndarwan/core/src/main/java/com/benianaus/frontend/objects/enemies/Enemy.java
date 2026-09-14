package com.benianaus.frontend.objects.enemies;
import com.badlogic.gdx.graphics.Color;
import com.benianaus.frontend.objects.GameObject;
import com.benianaus.frontend.objects.Player;

public class Enemy extends GameObject {

    protected String name;
    protected int hp;
    protected int maxHp;
    protected long scoreValue;

    public Enemy (String name, int hp) {
        super(200, 380, 24, 24, 0, Color.PINK);
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        scoreValue = 100;
    }

    public Enemy(float x, float y, float width, float height, Color color, String name, int hp, long scoreValue){
        super(x, y, width, height, 0, color);
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = scoreValue;
    }

    public boolean takeDamage(int damage) {
        boolean wasAlive = isAlive();
        setHp(getHp() - damage);
        System.out.println(getName() + " took " + damage + " damage! HP: " + getHp() + "/" + getMaxHp());
        if (wasAlive && getHp() == 0) {
            System.out.println(getName() + " was defeated!");
            return true;
        }
        return false;
    }

    public void attack(Player player, int damage) {
        // 1. Tampilkan informasi bahwa Enemy menyerang Player dalam format: [EnemyName] unleashes bullet barrage on [PlayerName]!
        // 2. Panggil takeDamage() milik Player menggunakan damage yang diberikan.
        System.out.println(this.name + " unleashes bullet barrage on " + player.getName());

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

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp){
        this.hp = Math.max(0, hp);
    }

    public int getMaxHp() {
        return maxHp;
    }

    public long getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(long scoreValue){
        this.scoreValue = scoreValue;
    }
}

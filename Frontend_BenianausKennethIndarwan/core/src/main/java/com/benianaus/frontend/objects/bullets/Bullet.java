package com.benianaus.frontend.objects.bullets;

import com.benianaus.frontend.objects.Collidable;
import com.benianaus.frontend.objects.GameObject;
import com.badlogic.gdx.graphics.Color;
import com.benianaus.frontend.objects.enemies.Enemy;

public class Bullet extends GameObject {
    private BulletType bulletType;
    private int damage;

    public Bullet(float x, float y, BulletType bulletType, int damage) {
        super(x, y, 16, 16, 400f, Color.YELLOW);
        // Inisialisasi bulletType dan damage dari parameter
        this.bulletType = bulletType;
        this.damage = damage;
    }

    public Bullet(float x, float y, float speed, BulletType bulletType, int damage) {
        super(x, y, 16, 16, speed, Color.YELLOW);
        // Inisialisasi bulletType dan damage dari parameter
        this.bulletType = bulletType;
        this.damage = damage;
    }

    @Override
    public void update(float delta) {
        // Posisi y bertambah sebesar speed * delta (bullet bergerak ke atas)
        y += speed*delta;
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Enemy enemy) {
            // 1. Tampilkan pesan bahwa Bullet mengenai Enemy dalam format:
            //    Bullet hit [EnemyName] for [damage] DMG!
            System.out.println("Bullet hit " + enemy.getName() + " for " + damage + " DMG!");

            // 2. Panggil takeDamage() milik Enemy dengan damage milik Bullet ini.
            enemy.takeDamage(damage);

            // 3. Bikin si bullet hancur (destroy) setelah mengenai Enemy, apapun hasilnya
            //    (baik enemy kalah atau masih hidup), krn satu bullet cuma boleh kena satu target.
            destroy();
        }
    }

    public BulletType getBulletType() {
        return bulletType;
    }

    public int getDamage() {
        return damage;
    }

}

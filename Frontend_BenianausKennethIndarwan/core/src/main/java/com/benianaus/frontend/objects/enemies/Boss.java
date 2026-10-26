package com.benianaus.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.benianaus.frontend.objects.Collidable;
import com.benianaus.frontend.objects.Player;
import com.benianaus.frontend.objects.patterns.ShootingPattern;
import com.benianaus.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.benianaus.frontend.systems.BulletManager;

public class Boss extends Enemy{
    private int collisionCooldown;
    private long currTime;

    // Deklarasikan field private `shootingPattern` bertipe ShootingPattern
    // dan langsung inisialisasi (new) dengan `SpreadShot` yang baru saja kita buat.
    // Pastikan speed-nya bernilai 200f, angle spread-nya 30f, dan ada 3 peluru (num) yang ditembak.
    private ShootingPattern shootingPattern = new SpreadShot(200f, 3, 30f);

    public Boss(String name, int hp){
        super(380, 400, 64, 64, Color.BLUE, name, hp, 5000L);
    }

    public Boss(float x, float y, String name, int hp){
        super(x, y, 64, 64, Color.BLUE, name, hp, 5000L);
    }

    // Tipe inheritancenya adalah tipe inheritance Hierarchical karena Boss dan Fairy merupakan hasil dari satu Superclass yaitu GameObject->Enemy.

    @Override
    public void onCollision(Collidable other) {
        // Cek apakah other yang diterima method ini adalah Player
        if (other instanceof Player){
            // Cetak "Player touches boss"
            if (collisionCooldown == 0) {
                System.out.println("Player touches boss");
                collisionCooldown = 1;
                currTime = System.currentTimeMillis();
            }
            if (System.currentTimeMillis() - currTime >= 1000){
                collisionCooldown = 0;
            }
        }
    }

    // TODO: Buat method shootBullet(BulletManager bulletManager):
    public void shootBullet(BulletManager bulletManager){
        if (shootingPattern != null && super.isAlive()){
            shootingPattern.execute((x+width/2-8), y, bulletManager, false);
        }
    }
    //       Jika shootingPattern tidak null dan bos belum hancur,
    //       maka eksekusi shootingPattern pada koordinat *cone* tengah bos (x + width / 2 - 8, y)

    // Getter dan setter untuk shootingPattern
    public ShootingPattern getShootingPattern(){
        return shootingPattern;
    }

    public void setShootingPattern(ShootingPattern shootingPattern){
        this.shootingPattern = shootingPattern;
    }

}

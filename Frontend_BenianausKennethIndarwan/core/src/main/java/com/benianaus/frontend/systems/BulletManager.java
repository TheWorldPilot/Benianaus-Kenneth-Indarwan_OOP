package com.benianaus.frontend.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.benianaus.frontend.objects.bullets.Bullet;
import com.benianaus.frontend.objects.bullets.BulletType;

import java.util.*;

public class BulletManager {
    // 1. Antrean Pool untuk Daur Ulang Objek (Object Pool Pattern)
    private Queue<Bullet> playerBulletPool;
    private Queue<Bullet> enemyBulletPool;

    // 2. Daftar Peluru Aktif di Layar
    private List<Bullet> activePlayerBullets;
    private List<Bullet> activeEnemyBullets;

    public BulletManager() {
        playerBulletPool = new LinkedList<>();      // 1. Inisialisasi playerBulletPool dan enemyBulletPool sebagai LinkedList kosong
        enemyBulletPool = new LinkedList<>();
        activePlayerBullets = new ArrayList<>();    // 2. Inisialisasi activePlayerBullets dan activeEnemyBullets sebagai ArrayList kosong
        activeEnemyBullets = new ArrayList<>();
    }

    // Peminjaman bullet Pemain dari Pool
    public Bullet spawnPlayerBullet(float x, float y, float vx, float vy, int damage) {
        Bullet bullet;
        if (!playerBulletPool.isEmpty()) {                           // 1. Cek apakah playerBulletPool tidak kosong.
            bullet = playerBulletPool.poll();                     // Kalau ya, poll dari pool. Kalau tidak, buat baru menggunakan Factory.
        } else {
            bullet = EntityFactory.createPlayerBullet(x, y, damage);
        }
        bullet.init(x, y, vx, vy, BulletType.AMULET, damage, true);     // 2. Re-initialize bullet.
        activePlayerBullets.add(bullet);    // 3. Tambahkan bullet ke dalam daftar activePlayerBullets.
        return bullet;                      // 4. Kembalikan instance bullet tersebut.
    }

    // Peminjaman bullet Musuh dari Pool
    public Bullet spawnEnemyBullet(float x, float y, float vx, float vy, int damage) {
        Bullet bullet;
        if (!enemyBulletPool.isEmpty()) {                         // 1. Cek apakah enemyBulletPool tidak kosong:
            bullet = enemyBulletPool.poll();                   // Kalau ya, poll dari pool. Kalau tidak, buat baru menggunakan Factory.
        } else {
            bullet = EntityFactory.createEnemyBullet(x, y, damage);
        }
        bullet.init(x, y, vx, vy, BulletType.DANMAKU, damage, false);     // 2. Re-initialize bullet.
        activeEnemyBullets.add(bullet);     // 3. Tambahkan bullet ke dalam daftar activeEnemyBullets
        return bullet;                      // 4. Kembalikan instance bullet tersebut.
    }

    // Metode pembantu akses pool dan daftar aktif
    public List<Bullet> getActivePlayerBullets() { return activePlayerBullets; }
    public List<Bullet> getActiveEnemyBullets() { return activeEnemyBullets; }
    public int getPlayerPoolSize() { return playerBulletPool.size(); }
    public int getEnemyPoolSize() { return enemyBulletPool.size(); }

    public void update(float delta, float screenWidth, float screenHeight) {
        // Daur ulang & pergerakan bullet pemain
        Iterator<Bullet> it_player = activePlayerBullets.iterator();

        while(it_player.hasNext()){
            Bullet currElement = it_player.next();
            currElement.update(delta);
            if (currElement.isOffScreen(screenWidth, screenHeight) || currElement.isDestroyed()){
                currElement.destroy();
                it_player.remove();
                playerBulletPool.offer(currElement);
            }
        }

        // Daur ulang & pergerakan bullet musuh
        Iterator<Bullet> it_enemy = activeEnemyBullets.iterator();

        while(it_enemy.hasNext()){
            Bullet currElement = it_enemy.next();
            currElement.update(delta);
            if (currElement.isOffScreen(screenWidth, screenHeight) || currElement.isDestroyed()){
                currElement.destroy();
                it_enemy.remove();
                enemyBulletPool.offer(currElement);
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (Bullet b : activePlayerBullets) {
            if (!b.isDestroyed()) b.render(batch);
        }
        for (Bullet b : activeEnemyBullets) {
            if (!b.isDestroyed()) b.render(batch);
        }
    }
}

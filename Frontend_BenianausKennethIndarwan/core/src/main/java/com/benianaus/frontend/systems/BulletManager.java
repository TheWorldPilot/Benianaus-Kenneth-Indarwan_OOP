package com.benianaus.frontend.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.benianaus.frontend.objects.GameObject;
import com.benianaus.frontend.objects.bullets.Bullet;
import com.benianaus.frontend.objects.bullets.BulletType;
import com.benianaus.frontend.objects.items.ItemType;
import com.benianaus.frontend.objects.patterns.bulletStrategy.FantasySealMovement;
import com.benianaus.frontend.objects.patterns.bulletStrategy.HomingBulletMovement;
import com.benianaus.frontend.objects.patterns.shootingStrategy.RingShot;

import java.util.*;

public class BulletManager {
    // 1. Antrean Pool untuk Daur Ulang Objek (Object Pool Pattern)
    private Queue<Bullet> playerBulletPool;
    private Queue<Bullet> enemyBulletPool;

    // 2. Daftar Peluru Aktif di Layar
    private List<Bullet> activePlayerBullets;
    private List<Bullet> activeEnemyBullets;

    private List<GameObject> entities;

    public BulletManager() {
        playerBulletPool = new LinkedList<>();      // 1. Inisialisasi playerBulletPool dan enemyBulletPool sebagai LinkedList kosong
        enemyBulletPool = new LinkedList<>();
        activePlayerBullets = new ArrayList<>();    // 2. Inisialisasi activePlayerBullets dan activeEnemyBullets sebagai ArrayList kosong
        activeEnemyBullets = new ArrayList<>();
    }

    // Peminjaman bullet Pemain dari Pool
    public Bullet spawnPlayerBullet(float x, float y, float vx, float vy, int damage, String spriteKey) {
        String key = (spriteKey != null) ? spriteKey : "bullet_amulet";
        Bullet bullet;
        if (!playerBulletPool.isEmpty()) {                           // 1. Cek apakah playerBulletPool tidak kosong.
            bullet = playerBulletPool.poll();                     // Kalau ya, poll dari pool. Kalau tidak, buat baru menggunakan Factory.
            bullet.init(x, y, vx, vy, BulletType.AMULET, damage, true);
            bullet.setSprite(AssetManager.getInstance().getTextureRegion(key));
        } else {
            bullet = EntityFactory.createPlayerBullet(x, y, damage, key);
            bullet.init(x, y, vx, vy, BulletType.AMULET, damage, true);
        }
        activePlayerBullets.add(bullet);    // 3. Tambahkan bullet ke dalam daftar activePlayerBullets.
        return bullet;                      // 4. Kembalikan instance bullet tersebut.
    }

    public Bullet spawnPlayerBullet(float x, float y, float vx, float vy, int damage) {
        return spawnPlayerBullet(x, y, vx, vy, damage, "bullet_amulet");
    }

    // Peminjaman bullet Musuh dari Pool
    public Bullet spawnEnemyBullet(float x, float y, float vx, float vy, int damage, String spriteKey) {
        String key = (spriteKey != null) ? spriteKey : "bullet_danmaku";
        Bullet bullet;
        if (!enemyBulletPool.isEmpty()) {                         // 1. Cek apakah enemyBulletPool tidak kosong:
            bullet = enemyBulletPool.poll();                   // Kalau ya, poll dari pool. Kalau tidak, buat baru menggunakan Factory.
            bullet.init(x, y, vx, vy, BulletType.DANMAKU, damage, false);
            bullet.setSprite(AssetManager.getInstance().getTextureRegion(key));
        } else {
            bullet = EntityFactory.createEnemyBullet(x, y, damage, key);
            bullet.init(x, y, vx, vy, BulletType.DANMAKU, damage, false);
        }
        activeEnemyBullets.add(bullet);     // 3. Tambahkan bullet ke dalam daftar activeEnemyBullets
        return bullet;                      // 4. Kembalikan instance bullet tersebut.
    }

    public Bullet spawnEnemyBullet(float x, float y, float vx, float vy, int damage) {
        return spawnEnemyBullet(x, y, vx, vy, damage, "bullet_danmaku");
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

            if (entities != null && currElement.getMovementPattern() instanceof HomingBulletMovement homing && homing.getEntities() == null) {
                homing.setEntities(entities);
            }

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

    // Eksekusi Spell Card (Bomb): Hapus seluruh peluru musuh aktif dan ubah menjadi drop item POINT!
    public void clearEnemyBullets(List<GameObject> entities) {
        // TODO:
        // 1. Buat Iterator<Bullet> untuk menelusuri activeEnemyBullets secara aman.
        // 2. Selama masih ada elemen berikutnya di iterator:
        //    - Ambil objek Bullet saat ini.
        //    - Jika parameter entities tidak bernilai null:
        //      * Buat objek Item bertipe ItemType.POINT menggunakan EntityFactory.createItem(...)
        //        pada posisi (bullet.getX(), bullet.getY()), lalu tambahkan ke dalam list entities.
        //    - Tandai bullet sebagai hancur (bullet.destroy()).
        //    - Hapus bullet dari activeEnemyBullets menggunakan remove() pada iterator di step 1.
        //    - Kembalikan bullet ke dalam antrean enemyBulletPool menggunakan offer().
        // TIP: Lihat rough outlinenya di `clearPlayerBullets()` di bawah
        Iterator<Bullet> it_bullet = activeEnemyBullets.iterator();
        while (it_bullet.hasNext()) {
            Bullet bullet = it_bullet.next();
            if (entities != null) {
                entities.add(EntityFactory.createItem(bullet.getX(), bullet.getY(), ItemType.POINT));
                bullet.destroy();
                it_bullet.remove();
                enemyBulletPool.offer(bullet);
            }
        }
    }

    public void clearEnemyBullets() {
        clearEnemyBullets(null);
    }

    // Pembersihan seluruh peluru pemain saat restart permainan (Copy-Paste):
    public void clearPlayerBullets() {
        Iterator<Bullet> pIter = activePlayerBullets.iterator();
        while (pIter.hasNext()) {
            Bullet bullet = pIter.next();
            bullet.destroy();
            pIter.remove();
            playerBulletPool.offer(bullet);
        }
    }

    public List<GameObject> getEntities() { return entities; }
    public void setEntities(List<GameObject> entities) { this.entities = entities; }

    public void spawnBombOrbs(float originX, float originY, GameObject targetEnemy, List<GameObject> entities) {
        int orbCount = 8;
        int orbDamage = 35; // 35 damage x 8 orbs = 280 total spell card damage potential!

        List<GameObject> targetEntities = (entities != null) ? entities : this.entities;
        // TODO (2 poin): Terapkan Strategy Composition — buat objek RingShot dengan bulletSpeed 150f, jumlah peluru orbCount, damage orbDamage, dan strategi pergerakan peluru berupa FantasySealMovement(targetEnemy, targetEntities), lalu panggil execute(originX, originY, this, true)
        RingShot ringShot = new RingShot(150f, orbCount, orbDamage, new FantasySealMovement(targetEnemy, targetEntities));
        ringShot.execute(originX, originY, this, true);

        System.out.println("[BulletManager] Spawned 8 Bomb Spirit Orbs via RingShot composed with FantasySealMovement!");
    }

    public void spawnBombOrbs(float originX, float originY, GameObject targetEnemy) {
        spawnBombOrbs(originX, originY, targetEnemy, null);
    }
}

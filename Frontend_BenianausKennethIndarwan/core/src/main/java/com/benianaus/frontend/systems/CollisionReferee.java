package com.benianaus.frontend.systems;

import com.benianaus.frontend.objects.GameObject;
import com.benianaus.frontend.objects.Player;
import com.benianaus.frontend.objects.bullets.Bullet;
import com.benianaus.frontend.objects.enemies.Enemy;
import com.benianaus.frontend.objects.items.Item;
import com.benianaus.frontend.objects.items.ItemType;

import java.util.ArrayList;
import java.util.List;

public class CollisionReferee {

    public void resolveCollisions(Player player, List<GameObject> entities, BulletManager bulletManager) {
        if (player == null || entities == null || bulletManager == null) return;

        List<GameObject> newDrops = new ArrayList<>();

        // Logika tabrakan dibagi menjadi 3 tahap:
        // 1. Evaluasi bullet Pemain dan Musuh
        for (Bullet listElement : bulletManager.getActivePlayerBullets()) {
            if (listElement.isDestroyed()) continue;
            for (GameObject listEntity : entities){
                if (listEntity instanceof Enemy && !listEntity.isDestroyed()){
                    if (listEntity.getCoreHitbox().overlaps(listElement.getCoreHitbox())){
                        boolean defeated = listEntity.takeDamage(bullet.getDamage());
                        if (defeated){
                            player.addScore(((Enemy) listEntity).getScoreValue());
                            newDrops.add(EntityFactory.createItem(listEntity.getX(), listEntity.getY(), ItemType.POWER));
                        }
                    }
                }
            }
        }

        // 2. Evaluasi bullet Musuh vs Pemain (Core Hit vs Graze Hit)
        for (GameObject listElement : bulletManager.getActivePlayerBullets()) {
            if (listElement.isDestroyed()) continue;
            if (player.isAlive()){
                if (player.getCoreHitbox().overlaps(listElement.getCoreHitbox())){
                    player.takeDamage(bullet.getDamage());
                    bullet.destroy();
                } else if (bullet.hasBeenGrazed() && bullet.getCoreHitbox().overlaps(player.getGrazeHitbox())){
                    bullet.hasBeenGrazed = true;
                    player.addScore(50);
                }

            }
        }

        // 3. Evaluasi Pemain vs Item dan Kontak Fisik Musuh
        for (GameObject listEntity : entities) {
            if (listEntity.isDestroyed()) continue;

            if (listEntity instanceof Item item){
                if (item.getCoreHitbox().overlaps(listEntity.getCoreHitbox()) || item.getCoreHitbox().overlaps(player.getGrazeHitbox())){
                    player.collectItem(item);
                }
            } else if (listEntity instanceof Enemy enemy){
                if (player.getCoreHitbox().overlaps(enemy.getCoreHitbox())){
                    player.takeDamage(10);
                }
            }
        }

        entities.addAll(newDrops);
    }
}

package com.benianaus.frontend.systems;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.benianaus.frontend.objects.Player;
import com.benianaus.frontend.objects.bullets.Bullet;
import com.benianaus.frontend.objects.bullets.BulletType;
import com.benianaus.frontend.objects.enemies.Boss;
import com.benianaus.frontend.objects.enemies.Fairy;
import com.benianaus.frontend.objects.items.Item;
import com.benianaus.frontend.objects.items.ItemType;

public class EntityFactory {

    // Membuat Player dan memasang animasi 'player_idle' dari AssetManager
    public static Player createPlayer(float x, float y, String name, int hp, int power, int spellCards) {
        Player player = new Player(x, y, name, hp, power, spellCards);
        Animation<TextureRegion> anim = AssetManager.getInstance().getAnimation("player_idle");
        player.setAnimation(anim);
        return player;
    }

    // Membuat Item dan memasang sprite sesuai jenis ItemType dari AssetManager
    public static Item createItem(float x, float y, ItemType itemType) {
        Item item = new Item(x, y, itemType);
        String key = switch (itemType) {
            case POWER -> "item_power";
            case POINT -> "item_point";
            case BOMB  -> "item_bomb";
            case LIFE  -> "item_life";
        };
        TextureRegion sprite = AssetManager.getInstance().getTextureRegion(key);
        item.setSprite(sprite);
        return item;
    }

    // Membuat peluru musuh (tipe DANMAKU, speed 0f, sprite bullet_danmaku)
    public static Bullet createEnemyBullet(float x, float y, int damage) {
        Bullet bullet = new Bullet(x, y, 0f, BulletType.DANMAKU, damage);
        TextureRegion sprite = AssetManager.getInstance().getTextureRegion("bullet_danmaku");
        bullet.setSprite(sprite);
        return bullet;
    }

    public static Boss createBoss(float x, float y, String name, int hp) {
        // TODO:
        // 1. Buat Boss baru dengan x, y, name, dan hp dari parameter;
        //    simpan pada variabel lokal bernama `boss`.
        // 2. Ambil animasi "boss_idle" melalui getAnimation(...)
        //    dari AssetManager.getInstance(). Simpan hasilnya pada
        //    variabel lokal bernama `idleAnim`.
        // 3. Pasang idleAnim pada boss melalui boss.setAnimation(...).
        // 4. Kembalikan boss.
        Boss boss = new Boss(x, y, name, hp);
        Animation<TextureRegion> idleAnim = AssetManager.getInstance().getAnimation("rumiaIdleAnim");
        boss.setAnimation(idleAnim);
        return boss;
    }

    public static Fairy createFairy(float x, float y, String name, int hp) {
        // TODO:
        // 1. Buat Fairy baru dengan x, y, name, dan hp dari parameter;
        //    simpan pada variabel lokal bernama `fairy`.
        // 2. Ambil animasi "fairy_idle_red" melalui getAnimation(...)
        //    dari AssetManager.getInstance(). Simpan hasilnya pada
        //    variabel lokal bernama `idleAnim`.
        // 3. Pasang idleAnim pada fairy melalui fairy.setAnimation(...).
        // 4. Kembalikan fairy.
        Fairy fairy = new Fairy(x, y, name, hp);
        Animation<TextureRegion> idleAnim = AssetManager.getInstance().getAnimation("fairy_idle_red");
        fairy.setAnimation(idleAnim);
        return fairy;
    }

    public static Fairy createFairy(float x, float y, String name, int hp, String keyString) {
        // TODO:
        // 1. Buat Fairy baru dengan x, y, name, dan hp dari parameter;
        //    simpan pada variabel lokal bernama `fairy`.
        // 2. Ambil animasi untuk keyString melalui getAnimation(...)
        //    dari AssetManager.getInstance(). Simpan hasilnya pada
        //    variabel lokal bernama `idleAnim`.
        // 3. Pasang idleAnim pada fairy melalui fairy.setAnimation(...).
        // 4. Kembalikan fairy.
        Fairy fairy = new Fairy(x, y, name, hp);
        Animation<TextureRegion> idleAnim = AssetManager.getInstance().getAnimation(keyString);
        fairy.setAnimation(idleAnim);
        return fairy;
    }

    public static Bullet createPlayerBullet(float x, float y, int damage, String spriteKey) {
        // TODO 1: Ambil TextureRegion untuk spriteKey melalui getTextureRegion dari
        // AssetManager.getInstance(), lalu masukkan ke variabel lokal `sprite`.
        TextureRegion sprite = AssetManager.getInstance().getTextureRegion(spriteKey);

        // TODO 2:
        // Buat Bullet baru dengan x, y, BulletType.AMULET, dan damage;
        // simpan pada variabel lokal `bullet`.
        Bullet bullet = new Bullet(x, y, BulletType.AMULET, damage);
        // TODO 3:
        // Pasang sprite pada bullet melalui bullet.setSprite(...).
        bullet.setSprite(sprite);

        // TODO 4:
        // Kembalikan bullet.
        return bullet;
    }

    public static Bullet createPlayerBullet(float x, float y, int damage) {
        // TODO 5:
        // Kembalikan hasil call function createPlayerBullet sebelumnya tapi dengan parameter spriteKey diganti dengan "bullet_amulet".
        return createPlayerBullet(x,y, damage, "bullet_amulet");
    }

}

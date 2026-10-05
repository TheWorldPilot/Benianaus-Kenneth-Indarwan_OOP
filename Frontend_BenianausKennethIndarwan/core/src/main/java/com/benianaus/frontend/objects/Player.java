package com.benianaus.frontend.objects;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.benianaus.frontend.objects.bullets.Bullet;
import com.benianaus.frontend.objects.bullets.BulletType;
import com.benianaus.frontend.objects.enemies.Enemy;
import com.benianaus.frontend.objects.items.Item;
import com.benianaus.frontend.objects.items.ItemType;
import com.benianaus.frontend.systems.AssetManager;

import static com.benianaus.frontend.systems.EntityFactory.createPlayerBullet;

public class Player extends GameObject {

    private String name;
    private int hp;
    private int power;
    private int spellCards;
    private long score;
    private int currentDir;

    public Player(String name, int hp, int power, int spellCards) {
        super(280, 40, 32, 48, 200, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }

    public Player(float x, float y, String name, int hp, int power, int spellCards) {
        super(x, y, 32, 48, 200, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }

    public void takeDamage(int damage) {
        // 1. Kurangi hp sebesar nilai damage.
        // 2. HP tidak boleh bernilai negatif.
        // 3. Jika HP masih lebih dari 0, tampilkan HP yang tersisa dalam format: [PlayerName] took [damage] damage! Remaining HP: [hp]
        // 4. Jika HP menjadi 0, tampilkan pesan bahwa Player telah dikalahkan.

        setHp(getHp() - damage);

        if (getHp() > 0) {
            System.out.println(getName() + " took " + damage + " damage! Remaining HP: " + getHp());
        } else {
            System.out.println("Player lost. Look, i'm sorry!");
        }

    }

    public void shoot(Enemy target) {
        // 1. Buat int bernama damage yang dihitung dengan menambahkan power sebanyak 10.
        // 2. Tampilkan informasi bahwa Player menembak Enemy dalam format: [name] shoots [TargetName] dealing [damage] DMG!
        // 3. Panggil method takeDamage() milik object Enemy.

        int damage = 10 + getPower();
        System.out.println(getName() + " shoots " + target.getName() + " dealing " + damage + " DMG!");

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

    public void addScore(long points) {
        if (points > 0) {
            this.score += points;
            System.out.println(getName() + " gained " + points + " pts! Total Score: " + this.score);
        }
    }

    @Override
    public void update(float delta){
        if (Gdx.input != null) {
            if(Gdx.input.isKeyPressed(Input.Keys.W)){
                y += speed * delta;
            } else if (Gdx.input.isKeyPressed(Input.Keys.S)){
                y -= speed * delta;
            } else if (Gdx.input.isKeyPressed(Input.Keys.A)){
                x -= speed * delta;
            } else if (Gdx.input.isKeyPressed(Input.Keys.D)){
                x += speed * delta;
            }
        }
        // TODO-DONE 1: panggil update(delta) milik GameObject melalui super.
        super.update(delta);

        // TODO-DONE 2: Siapkan variabel lokal float dx dengan nilai awal 0
        // (dx = delta x, mencatat perubahan arah horizontal untuk animasi)
        float dx = 0;

        if (Gdx.input != null) {
            if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                y += speed * delta;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                y -= speed * delta;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                x -= speed * delta;
                // TODO 3: Ganti nilai dx sesuai dengan arahnya.
                // (Kalau ke kiri, maka dx ke mana ya?)
                dx = x;

            }
            if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                x += speed * delta;
                // TODO 4: Ganti nilai dx sesuai dengan arahnya.
                // (Kalau ke kanan, maka dx ke mana ya?)
                dx = -x;
            }
        }

        // TODO 5-DONE: Panggil updateAnimationState(dx)
        updateAnimationState(dx);
    }

    public void updateAnimationState(float dx) {
        AssetManager assets = AssetManager.getInstance();
        if (dx < 0) {
            // TODO:
            // 1. Lanjutkan perubahan hanya jika currentDir bukan -1.
            // 2. Ubah currentDir menjadi -1.
            // 3. Ambil animasi "player_left" melalui assets.getAnimation(...).
            //    Simpan pada variabel lokal bertipe Animation<TextureRegion> bernama anim.
            // 4. Jika anim tidak null, pasang anim melalui setAnimation(...).
            currentDir = -1;
            Animation<TextureRegion> anim = assets.getAnimation("player_left");
            if (anim != null){
                setAnimation(anim);
            }
        } else if (dx > 0) {
            // TODO:
            // 1. Lanjutkan perubahan hanya jika currentDir bukan 1.
            // 2. Ubah currentDir menjadi 1.
            // 3. Ambil animasi "player_right" melalui assets.getAnimation(...).
            //    Simpan pada variabel lokal bertipe Animation<TextureRegion> bernama anim.
            // 4. Jika anim tidak null, pasang anim melalui setAnimation(...).
            currentDir = 1;
            Animation<TextureRegion> anim = assets.getAnimation("player_right");
            if (anim != null){
                setAnimation(anim);
            }
        } else {
            // TODO:
            // 1. Lanjutkan perubahan hanya jika currentDir bukan 0.
            // 2. Ubah currentDir menjadi 0.
            // 3. Ambil animasi "player_idle" melalui assets.getAnimation(...).
            //    Simpan pada variabel lokal bertipe Animation<TextureRegion> bernama anim.
            // 4. Jika anim tidak null, pasang anim melalui setAnimation(...).
            currentDir = 0;
            Animation<TextureRegion> anim = assets.getAnimation("player_idle");
            if (anim != null){
                setAnimation(anim);
            }
        }
    }


    @Override
    public void onCollision(Collidable other) {
        // Cek apakah other yang diterima method ini adalah Item
        if (other instanceof Item && !((Item) other).collected){
            // Cetak "Player touches items" lalu panggil collectItem((Item) other)
            System.out.println("Player touches items");
            collectItem((Item) other);
            ((Item) other).collected = true;
        }
    }

    public Bullet shootBullet() {
        int damage = 10 + power;
        System.out.println(name + " shoots bullet dealing " + damage + " DMG!");
        // kembalikan Bullet baru, diposisikan di tengah atas Player
        // (x + width/2 - 4, y + height), bertipe BulletType.AMULET, dengan damage di atas
        return createPlayerBullet(x+width/2-4, y+height, damage);
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

    public int getPower() {
        return power;
    }

    public void setPower(int power){
        this.power = power;
    }

    public int getSpellCards() {
        return spellCards;
    }

    public void setSpellCards(int spellCards){
        this.spellCards = spellCards;
    }

    public long getScore() {
        return score;
    }

    public void collectItem(Item item) {
        if (item.isDestroyed()) return;

        System.out.println(getName() + " collected " + item.getItemType() + "!");

        ItemType type = item.getItemTypeEnum();
        if (type != null) {
            switch (type) {
                case POWER -> {
                    // 1. Tambahkan power sebesar type.getPowerBonus() lewat this.power
                    this.power += type.getPowerBonus();
                    // 2. Tambahkan score sebesar item.getScoreValue() lewat addScore() (addScore() sudah otomatis mencetak "gained X pts!")
                    addScore(item.getScoreValue());
                    // 3. Cetak: [name] collected POWER item! Power increased to [power]
                    System.out.println(name + " collected POWER item! Power increased to " + power);
                }
                case POINT -> {
                    // 1. Tambahkan score sebesar item.getScoreValue() lewat addScore()
                    addScore(item.getScoreValue());
                    // 2. Cetak: [name] collected POINT item!
                    System.out.println(name + " collected POINT item!");
                }
                case BOMB -> {
                    // 1. Tambahkan spellCards sebesar 1
                    spellCards++;
                    // 2. Tambahkan score sebesar item.getScoreValue() lewat addScore()
                    addScore(item.getScoreValue());
                    // 3. Cetak: [name] collected BOMB item! SpellCards: [spellCards]
                    System.out.println(name + " collected BOMB item! SpellCards: " + spellCards);
                }
                case LIFE -> {
                    // 1. Tambahkan hp sebesar 20
                    hp += 20;
                    // 2. Tambahkan score sebesar item.getScoreValue() lewat addScore()
                    addScore(item.getScoreValue());
                    // 3. Cetak: [name] collected LIFE item! HP: [hp]
                    System.out.println(name + " collected LIFE item! HP: " + hp);
                }
            }
        } else {
            addScore(item.getScoreValue());
            System.out.println(name + " collected " + item.getItemType() + "!");
        }

        // Tandai item ini sebagai destroyed agar nanti dihapus oleh Iterator
        // Panggil method destroy() milik item di sini!
        item.destroy();
    }





}

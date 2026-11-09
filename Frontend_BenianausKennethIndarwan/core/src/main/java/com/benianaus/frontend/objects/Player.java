package com.benianaus.frontend.objects;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.benianaus.frontend.objects.bullets.Bullet;
import com.benianaus.frontend.objects.bullets.BulletType;
import com.benianaus.frontend.objects.enemies.Enemy;
import com.benianaus.frontend.objects.items.Item;
import com.benianaus.frontend.objects.items.ItemType;
import com.benianaus.frontend.objects.patterns.ShootingPattern;
import com.benianaus.frontend.objects.patterns.bulletStrategy.HomingBulletMovement;
import com.benianaus.frontend.objects.patterns.entityStrategy.FixedMovement;
import com.benianaus.frontend.objects.patterns.entityStrategy.LinearEntityMovement;
import com.benianaus.frontend.objects.patterns.shootingStrategy.CompositeShootingPattern;
import com.benianaus.frontend.objects.patterns.shootingStrategy.FrontShot;
import com.benianaus.frontend.objects.patterns.shootingStrategy.SideShot;
import com.benianaus.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.benianaus.frontend.observers.GameObserver;
import com.benianaus.frontend.systems.AssetManager;

import com.badlogic.gdx.math.Rectangle;
import com.benianaus.frontend.systems.BulletManager;
import com.benianaus.frontend.systems.SoundManager;

import java.util.ArrayList;
import java.util.List;

import static com.benianaus.frontend.systems.EntityFactory.createPlayerBullet;

public class Player extends EntityShooter {

    private String name;
    private int hp;
    private int lives = 3;
    private int power;
    private int spellCards;
    private long score;
    private int currentDir;
    private int grazeCount = 0;
    private boolean focused = false;

    private float shootCooldown = 5f / 60f; // 5 frame pada 60 FPS (12 tembakan/detik)
    private float shootTimer = shootCooldown;
    private float homingCooldown = 30f / 60f; // 30 frame (0.5s) atau 15 frame (0.25s)
    private float homingShootTimer = homingCooldown;

    private ShootingPattern mainShootingPattern;
    private ShootingPattern sideShootingPattern;

    private float invulnerableTimer = 0f;

    private ShootingPattern shootingPattern;

    // TODO: Deklarasikan atribut private List<GameObserver> observers dan inisialisasi dengan ArrayList kosong
    private List<GameObserver> observers = new ArrayList<>();

    public Player(String name, int hp, int power, int spellCards) {
        super(280, 40, 32, 48, 240f, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
        setMovementPattern(new FixedMovement());
        updatePowerStrategy();
    }

    public Player(float x, float y, String name, int hp, int power, int spellCards) {
        super(x, y, 32, 48, 240f, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
        setMovementPattern(new FixedMovement());
        updatePowerStrategy();
    }


    public void shoot(Enemy target) {
        // 1. Buat int bernama damage yang dihitung dengan menambahkan power sebanyak 10.
        // 2. Tampilkan informasi bahwa Player menembak Enemy dalam format: [name] shoots [TargetName] dealing [damage] DMG!
        // 3. Panggil method takeDamage() milik object Enemy.

        int damage = 10 + getPower();
        System.out.println(getName() + " shoots " + target.getName() + " dealing " + damage + " DMG!");

        target.takeDamage(damage);
    }

    public void addScore(long points) {
        if (points > 0) {
            this.score += points;
            notifyScoreChanged();
            System.out.println(getName() + " gained " + points + " pts! Total Score: " + this.score);
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta); // Advances stateTime for idle animations
        shootTimer += delta;
        homingShootTimer += delta;

        if (shootTimer > 0.15f) {
            shootTimer = shootCooldown;
            homingShootTimer = homingCooldown;
        }

        if (invulnerableTimer > 0f) {
            invulnerableTimer -= delta;
            if (invulnerableTimer < 0f) invulnerableTimer = 0f;
        }
    }

    public void updateAnimationState(float dx) {
        AssetManager assets = AssetManager.getInstance();
        if (dx < 0) {
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

    @Override
    public void shootBullet(BulletManager bulletManager) {
        if (bulletManager == null) return;
        if (shootTimer >= shootCooldown) {
            // TODO (2 poin): Di line ini, cek jika mainShootingPattern tidak null. Apabila ya, maka panggil mainShootingPattern.execute(x + width / 2f - 8f, y + height, bulletManager, true) di dalamnya. Kemudian ubah `if` di bawah ini (yang di kalian persis mengecek `shootingPattern != null`) menjadi `else if`
            if (mainShootingPattern != null){
                mainShootingPattern.execute(x + width / 2f - 8f, y + height, bulletManager, true);
            }
            else if (shootingPattern != null) {
                shootingPattern.execute(x + width / 2f - 8f, y + height, bulletManager, true);
            } else {
                bulletManager.spawnPlayerBullet(x + width / 2f - 8f, y + height, 0, 400f, 10 + power);
            }
            shootTimer = 0f;
        }

        // TODO (2 poin): Jika sideShootingPattern tidak null dan homingShootTimer lebih dari sama dengan homingCooldown,
        // panggil sideShootingPattern.execute(x + width / 2f - 8f, y + height, bulletManager, true) lalu reset homingShootTimer ke nol.
        if (sideShootingPattern != null && homingShootTimer >= homingCooldown){
            sideShootingPattern.execute(x + width / 2f - 8f, y + height, bulletManager, true);
            homingShootTimer = 0;
        }
    }

    public ShootingPattern getShootingPattern() { return shootingPattern; }

    public void setShootingPattern(ShootingPattern shootingPattern) { this.shootingPattern = shootingPattern; }

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
        notifyHpChanged();
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power){
        this.power = power;
        updatePowerStrategy();
        notifyPowerChanged();
    }

    public int getSpellCards() {
        return spellCards;
    }

    public void setSpellCards(int spellCards){
        this.spellCards = spellCards;
        notifySpellCardsChanged();
    }

    public long getScore() {
        return score;
    }


    @Override
    public Rectangle getCoreHitbox() {
        // Kembalikan objek Rectangle baru berukuran 8x8 yang berpusat tepat di tengah sprite:
        return new Rectangle((x+(width/2f)-4f), (y+(height/2f)-4f), 8f, 8f);   // - koordinat X: x + (width / 2f) - 4f
                                                                    // - koordinat Y: y + (height / 2f) - 4f
                                                                    // - lebar: 8f, tinggi: 8f
    }

    @Override
    public Rectangle getGrazeHitbox() {
        // Kembalikan objek Rectangle baru dengan koordinat posisi (x, y) dan ukuran seluas sprite visual karakter (width, height)
        return new Rectangle(x, y, width, height);
    }

    // Eksekusi pergerakan dari MoveCommand dengan pembatasan area bermain (Playfield Clamping)
    public void move(float dx, float dy) {
        this.x += dx*speed; // 1. Tambahkan posisi x sebesar dx dikali speed (this.x += dx * speed)
        this.y += dy*speed; // 2. Tambahkan posisi y sebesar dy dikali speed (this.y += dy * speed)
        clampToPlayfield(); // 3. Panggil metode clampToPlayfield()
    }

    private void clampToPlayfield() {
        float minX = 32;
        float maxX = 416 - width;
        float minY = 16;
        float maxY = 560 - height;

        if (this.x < minX) this.x = 32;             // Batasi nilai x agar tidak kurang dari minX dan tidak lebih dari maxX,
        if (this.x > maxX) this.x = 416-width;
        if (this.y < minY) this.y = 16;             // serta nilai y agar tidak kurang dari minY dan tidak lebih dari maxY.
        if (this.y > maxY) this.y = 560-height;
    }

    // Eksekusi Spell Card (Bomb) dari BombCommand
    public void useBomb(BulletManager bulletManager, List<GameObject> entities) {
        if (spellCards > 0) {
            spellCards--;
            invulnerableTimer = 5.0f; // 5 seconds invulnerability during Fantasy Seal!
            notifySpellCardsChanged();
            if (bulletManager != null) {
                bulletManager.clearEnemyBullets(entities);

                // Cari target musuh aktif untuk dikejar (Boss atau Fairy)
                GameObject target = null;
                if (entities != null) {
                    for (GameObject obj : entities) {
                        if (obj instanceof Enemy enemy && !enemy.isDestroyed()) {
                            target = enemy;
                            break;
                        }
                    }
                }

                // TODO (2 poin): Panggil metode spawnBombOrbs pada bulletManager dari titik tengah Player (x + width / 2f, y + height / 2f) dengan argumen target dan entities
                bulletManager.spawnBombOrbs(x + width / 2f, y + height / 2f, target, entities);
            }
            System.out.println(name + " unleashes SPELL CARD: Spirit Sign Fantasy Seal (霊符「夢想封印」)! All enemy bullets cleared & 8 homing orbs launched!");
        } else {
            System.out.println("No Spell Cards (Bombs) remaining!");
        }
    }

    public void useBomb(BulletManager bulletManager) {
        useBomb(bulletManager, null);
    }

    public void addGraze() {
        grazeCount++;
        addScore(50);
        notifyGrazeChanged();
    }

    @Override
    public void render(SpriteBatch batch) {
        if (batch != null && active && !isDestroyed()) {
            // Animasi berkedip frekuensi tinggi saat kebal (i-frames respawn / bomb)
            if (invulnerableTimer > 0f) {
                boolean visible = ((int) (invulnerableTimer * 16)) % 2 == 0;
                if (!visible) return; // Lewati gambar frame agar terjadi efek kedip
            }

            if (focused) {
                batch.setColor(1f, 1f, 1f, 0.5f); // Opasitas 50% saat Focus Mode aktif
            }
            super.render(batch);
            if (focused) {
                batch.setColor(1f, 1f, 1f, 1.0f); // Kembalikan opasitas ke 100%
            }
        }
    }

    // Menggambar titik indikator Core Hitbox saat Focus Mode (Shift) aktif
    public void renderFocusIndicator(ShapeRenderer shapeRenderer) {
        if (focused && shapeRenderer != null && active) {
            Rectangle core = getCoreHitbox();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.circle(core.x + core.width / 2f, core.y + core.height / 2f, 4f);
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.circle(core.x + core.width / 2f, core.y + core.height / 2f, 2f);
            shapeRenderer.end();
        }
    }

    public boolean isFocused(){
        return focused;
    }

    public void setFocused(boolean focused){
        if (this.focused != focused) {
            this.focused = focused;
            updatePowerStrategy();
        }
    }

    public int getGrazeCount(){
        return grazeCount;
    }

    public void takeDamage(int damage) {
        takeDamage(damage, null, null);
    }

    public void takeDamage(int damage, BulletManager bulletManager, List<GameObject> entities) {
        if (isInvulnerable() || isDestroyed()) {
            if (isInvulnerable()) {
                System.out.println(name + " is INVULNERABLE! (0 damage taken)");
            }
            return;
        }
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }
        System.out.println(name + " took " + damage + " damage! Remaining HP: " + this.hp);
        if (this.hp == 0) {
            System.out.println(name + " was defeated (Pichuun~)! ");
            loseLife(bulletManager, entities);
        }
        notifyHpChanged();
    }

    public void loseLife(BulletManager bulletManager, List<GameObject> entities) {
        this.lives--;
        notifyLivesChanged();

        if (bulletManager != null) {
            bulletManager.clearEnemyBullets();
        }

        if (this.lives > 0) {
            if (bulletManager != null) {
                System.out.println("[Player] " + name + " lost a life! Remaining Lives: " + this.lives + ". Respawning at (200, 50)...");
            }
            respawn();
        } else {
            if (bulletManager != null) {
                System.out.println("[Player] GAME OVER! " + name + " has run out of lives!");
            }
            destroy();
        }
    }

    public void respawn() {
        this.active = true;
        this.x = 200;
        this.y = 50;
        this.hp = 100;
        this.invulnerableTimer = 3.0f;
        this.power = Math.max(10, this.power - 16); // Penalti death Touhou
        updatePowerStrategy();
        this.spellCards = 3; // Reset bomb menjadi 3 saat respawn

    }

    public void resetGame() {
        this.lives = 3;
        this.hp = 100;
        this.power = 15;
        updatePowerStrategy();
        this.spellCards = 3;
        this.score = 0;
        this.grazeCount = 0;
        this.active = true;
        this.x = 200;
        this.y = 50;
        this.invulnerableTimer = 3.0f;
        notifyLivesChanged();
        notifyHpChanged();
        notifyPowerChanged();
        notifySpellCardsChanged();
        notifyScoreChanged();
        notifyGrazeChanged();
    }

    public void collectItem(Item item) {
        if (item.isDestroyed()) return;

        ItemType type = item.getItemTypeEnum();
        if (type != null) {
            switch (type) {
                case POWER -> {
                    this.power += type.getPowerBonus();
                    updatePowerStrategy();
                    notifyPowerChanged();
                    addScore(item.getScoreValue());
                    System.out.println(name + " collected POWER item! Power increased to " + power);
                }
                case POINT -> {
                    addScore(item.getScoreValue());
                    System.out.println(name + " collected POINT item!");
                }
                case BOMB -> {
                    this.spellCards++;
                    notifySpellCardsChanged();
                    addScore(item.getScoreValue());
                    System.out.println(name + " collected BOMB item! SpellCards: " + spellCards);
                }
                case LIFE -> {
                    this.lives++;   // Perhatikan! Ada tambahan ini!
                    notifyLivesChanged();
                    this.hp = 100;
                    notifyHpChanged();
                    addScore(item.getScoreValue());
                    System.out.println(name + " collected LIFE item! Remaining Lives: " + lives);
                }
            }
        } else {
            addScore(item.getScoreValue());
            System.out.println(name + " collected " + item.getItemType() + "!");
        }

        item.destroy();
    }

    public boolean isAlive() {
        return this.hp > 0 && this.lives > 0 && !isDestroyed();
    }

    public boolean isGameOver() {
        return this.lives <= 0;
    }

    public boolean isInvulnerable() {
        return invulnerableTimer > 0f;
    }

    public float getInvulnerableTimer() {
        return invulnerableTimer;
    }

    public int getLives() { return lives; }
    public void setLives(int lives) {
        this.lives = lives;
        notifyLivesChanged();
    }

    // Registrasi & Penghapusan Observer (4 poin)
    public void registerObserver(GameObserver observer) {
        // TODO:
        // 1. Cek apakah observer tidak null DAN belum ada di dalam list observers (!observers.contains(observer)).
        // 2. Jika memenuhi syarat:
        //    - Tambahkan observer ke dalam list observers.
        //    - Pancarkan seluruh state awal pemain saat ini ke observer tersebut:
        //      * observer.onScoreChanged(score);
        //      * observer.onHpChanged(hp);
        //      * observer.onLivesChanged(lives);
        //      * observer.onSpellCardsChanged(spellCards);
        //      * observer.onPowerChanged(power);
        //      * observer.onGrazeChanged(grazeCount);
        if (observer != null && !observers.contains(observer)){
            observers.add(observer);
            observer.onScoreChanged(score);
            observer.onHpChanged(hp);
            observer.onLivesChanged(lives);
            observer.onSpellCardsChanged(spellCards);
            observer.onPowerChanged(power);
            observer.onGrazeChanged(grazeCount);
        }
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    // Metode Pemancar Notifikasi ke Seluruh Observer (4 poin)
    // TODO: Buat 6 metode private void berikut yang MELAKUKAN LOOP KE SELURUH ELEMEN DI 'observers'
    //       dan memanggil metode callback yang bersesuaian:
    // 1. notifyScoreChanged()      -> memanggil obs.onScoreChanged(score)
    // 2. notifyHpChanged()         -> memanggil obs.onHpChanged(hp)
    // 3. notifyLivesChanged()      -> memanggil obs.onLivesChanged(lives) (Sistem Nyawa Touhou)
    // 4. notifySpellCardsChanged() -> memanggil obs.onSpellCardsChanged(spellCards)
    // 5. notifyPowerChanged()      -> memanggil obs.onPowerChanged(power)
    // 6. notifyGrazeChanged()      -> memanggil obs.onGrazeChanged(grazeCount)
    private void notifyScoreChanged(){
        for (GameObserver obs : observers){
            obs.onScoreChanged(score);
        }
    }

    private void notifyHpChanged(){
        for (GameObserver obs : observers){
            obs.onHpChanged(hp);
        }
    }

    private void notifyLivesChanged(){
        for (GameObserver obs : observers){
            obs.onLivesChanged(lives);
        }
    }

    private void notifySpellCardsChanged(){
        for (GameObserver obs : observers){
            obs.onSpellCardsChanged(spellCards);
        }
    }

    private void notifyPowerChanged(){
        for (GameObserver obs : observers){
            obs.onPowerChanged(power);
        }
    }

    private void notifyGrazeChanged(){
        for (GameObserver obs : observers){
            obs.onGrazeChanged(grazeCount);
        }
    }

    // Terapkan Dynamic Strategy Swapping pada Player Reimu
    public void updatePowerStrategy() {
        float spreadAngle = focused ? 4f : 18f;
        float dualSpreadAngle = focused ? 3f : 12f;
        float sideHOffset = focused ? 8f : 24f;
        float sideVOffset = focused ? 16f : -16f;
        float leftAngle = focused ? 100f : 120f;
        float rightAngle = focused ? 80f : 60f;

        float normalSpeed = 720f;
        float homingSpeed = 500f;

        // 2. Evaluasi jenjang nilai power pemain (Authentic Touhou 6 EoSD Power Ranks):
        if (power < 8) {
            // Rank 1 (Power 0 - 7): 1 Tembakan Lurus Tengah
            mainShootingPattern = new FrontShot(normalSpeed, 48);
            sideShootingPattern = null;
            homingCooldown = 30f / 60f;
        } else if (power < 16) {
            // Rank 2 (Power 8 - 15): 1 Tembakan Lurus + 2 Jimat Sayap Terpandu (Homing SideShot)
            mainShootingPattern = new FrontShot(normalSpeed, 48);
            sideShootingPattern = new SideShot(homingSpeed, 14, sideHOffset, sideVOffset, leftAngle, rightAngle, true, new HomingBulletMovement(360f), "bullet_amulet_homing");
            homingCooldown = 30f / 60f;
        } else if (power < 32) {
            // Rank 3 (Power 16 - 31): 2 Tembakan Kipas Lurus Rapat + 2 Jimat Sayap Terpandu
            mainShootingPattern = new SpreadShot(normalSpeed, 2, dualSpreadAngle, 30, 8f, null, null, null);
            sideShootingPattern = new SideShot(homingSpeed, 14, sideHOffset, sideVOffset, leftAngle, rightAngle, true, new HomingBulletMovement(360f), "bullet_amulet_homing");
        } else if (power < 48) {
            // Rank 4 (Power 32 - 47): 3 Tembakan Kipas Menyebar + 2 Jimat Sayap Terpandu
            mainShootingPattern = new SpreadShot(normalSpeed, 3, spreadAngle, 26, 0, null, null, null);
            sideShootingPattern = new SideShot(homingSpeed, 14, sideHOffset, sideVOffset, leftAngle, rightAngle, true, new HomingBulletMovement(360f), "bullet_amulet_homing");
            homingCooldown = 30f / 60f;
        } else if (power < 64) {
            // Rank 5 (Power 48 - 63): 3 Tembakan Kipas + Jimat Terpandu (Cadence dipercepat ke 15 frames / 0.25s!)
            mainShootingPattern = new SpreadShot(normalSpeed, 3, spreadAngle, 26, 0, null, null, null);
            sideShootingPattern = new SideShot(homingSpeed, 12, sideHOffset, sideVOffset, leftAngle, rightAngle, true, new HomingBulletMovement(360f), "bullet_amulet_homing");
            homingCooldown = 15f / 60f;
        } else if (power < 80) {
            // Rank 6 (Power 64 - 79): 3 Tembakan Kipas + 4 Jimat Terpandu (Composite Pattern)
            mainShootingPattern = new SpreadShot(normalSpeed, 3, spreadAngle, 26, 0, null, null, null);
            sideShootingPattern = new CompositeShootingPattern(new SideShot(homingSpeed, 9, sideHOffset, sideVOffset, leftAngle, rightAngle, true, new HomingBulletMovement(360f), "bullet_amulet_homing"), new SideShot(homingSpeed, 12, sideHOffset + 4f, sideVOffset, leftAngle + 30f, rightAngle - 30f, true, new HomingBulletMovement(360f), "bullet_amulet_homing"));
            homingCooldown = 15f / 60f;
        } else if (power < 128) {
            // Rank 7 & 8 (Power 80 - 127): 3 Tembakan Kipas + 4 Jimat Terpandu Sudut Rapat
            mainShootingPattern = new SpreadShot(normalSpeed, 3, spreadAngle, 26, 0, null, null, null);
            sideShootingPattern = new CompositeShootingPattern(new SideShot(homingSpeed, 8, sideHOffset, sideVOffset, leftAngle - 10f, rightAngle + 10f, true, new HomingBulletMovement(360f), "bullet_amulet_homing"), new SideShot(homingSpeed, 8, sideHOffset + 4f, sideVOffset, leftAngle + 10f, rightAngle - 10f, true, new HomingBulletMovement(360f), "bullet_amulet_homing"));
            homingCooldown = 15f / 60f;
        } else {
            // Rank 9 (Power 128 MAX): 4 Tembakan Kipas Berjarak + 6 Jimat Terpandu Maksimal
            mainShootingPattern = new SpreadShot(normalSpeed, 4, spreadAngle*1.2f, 24, 16f, null, null, null);
            sideShootingPattern = new CompositeShootingPattern(new SideShot(homingSpeed, 8, sideHOffset, sideVOffset, leftAngle - 10f, rightAngle + 10f, true,  new HomingBulletMovement (360f), "bullet_amulet_homing"),
                                                                new SideShot(homingSpeed, 8, sideHOffset + 4f, sideVOffset, leftAngle + 10f, rightAngle - 10f, true, new HomingBulletMovement(360f), "bullet_amulet_homing"),
                                                                new SideShot(homingSpeed, 8, sideHOffset + 8f, sideVOffset, leftAngle + 30f, rightAngle - 30f, true, new HomingBulletMovement(360f), "bullet_amulet_homing"));
            homingCooldown = 15f / 60f;
        }

        // 3. Pasang strategi komposit ke atribut superclass EntityShooter
        if (sideShootingPattern != null) {
            setShootingPattern(new CompositeShootingPattern(mainShootingPattern, sideShootingPattern));
        } else {
            setShootingPattern(mainShootingPattern);
        }
    }

    public ShootingPattern getMainShootingPattern() { return mainShootingPattern; }
    public ShootingPattern getSideShootingPattern() { return sideShootingPattern; }
    public float getShootCooldown() { return shootCooldown; }
    public float getHomingCooldown() { return homingCooldown; }
}

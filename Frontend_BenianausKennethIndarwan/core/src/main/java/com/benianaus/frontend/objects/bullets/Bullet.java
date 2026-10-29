package com.benianaus.frontend.objects.bullets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.benianaus.frontend.objects.Collidable;
import com.benianaus.frontend.objects.GameObject;
import com.badlogic.gdx.graphics.Color;
import com.benianaus.frontend.objects.enemies.Enemy;

public class Bullet extends GameObject {
    private BulletType bulletType;
    private int damage;
    private float vx, vy;           // 1. float vx dan vy (komponen kecepatan vektor 2D)
    private float angle = 90f;      // 2. float angle dengan nilai awal 90f (sudut rotasi visual peluru dalam derajat)
    private boolean isPlayerBullet; // 3. boolean isPlayerBullet (penanda apakah peluru ini milik player atau musuh)
    private boolean grazed;         // 4. boolean grazed (penanda apakah peluru ini sudah pernah di-graze oleh player)

    public Bullet(float x, float y, BulletType bulletType, int damage) {
        super(x, y, 16, 16, 400f, Color.YELLOW);
        this.bulletType = bulletType;   // 1. Inisialisasi atribut bulletType dan damage dari parameter
        this.damage = damage;
        this.vx = 0;                    // 2. Beri kecepatan awal vx sebagai nol
        this.vy = 400f;                 // dan vy sebagai 400f (bergerak lurus ke atas)
        this.angle = 90f;               // 3. Set sudut rotasi visual angle agar 90 derajat ke atas
        this.isPlayerBullet = true;     // 4. Tandai peluru ini sebagai dirinya adalah milik player (isPlayerBullet),
        this.grazed = false;            // lalu tandai status graze dalam keadaan tidak aktif
    }

    public Bullet(float x, float y, float speed, BulletType bulletType, int damage) {
        super(x, y, 16, 16, speed, Color.YELLOW);
        this.bulletType = bulletType;   // 1. Inisialisasi atribut bulletType dan damage dari parameter
        this.damage = damage;
        this.vx = 0;                    // 2. Beri kecepatan awal vx sebagai nol
        this.vy = speed;                 // dan vy sesuai dengan speed
        if (speed < 0) {                // 3. Set sudut rotasi visual angle: jika speed bernilai negatif gunakan 270f (menghadap bawah), jika tidak gunakan 90f
            this.angle = 270f;
        } else {
            this.angle = 90f;
        }
        this.isPlayerBullet = true;     // 4. Tandai peluru ini sebagai dirinya adalah milik player (isPlayerBullet),
        this.grazed = false;            // lalu tandai status graze dalam keadaan tidak aktif
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        x += vx*delta;   // 1. Perbarui posisi x bertambah sebesar komponen kecepatan vx dikali delta
        y += vy*delta;   // 2. Perbarui posisi y bertambah sebesar komponen kecepatan vy dikali delta
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

    // Pool Re-initialization untuk Object Pool Pattern
    public void init(float x, float y, float vx, float vy, BulletType bulletType, int damage, boolean isPlayerBullet) {
        this.x = x;     // 1. Perbarui nilai posisi (x, y) dan komponen kecepatan vektor (vx, vy) dari parameter yang diterima
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.angle = (float) Math.toDegrees(Math.atan2(vy, vx))-90;    // 2. Hitung sudut rotasi `angle` menggunakan rumus Math.atan2 sebagaimana dijelaskan di atas, lalu ganti ke degree.
        this.speed = (float) Math.hypot(vx, vy);                    // 3. Perbarui nilai `speed` dengan menghitung panjang resultan kedua vektor kecepatan menggunakan rumus: Math.hypot sebagaimana dijelaskan di paragraf atas.
        this.bulletType = bulletType;                               // 4. Simpan atribut identitas peluru (bulletType, damage, dan isPlayerBullet) sesuai nilai parameter
        this.damage = damage;
        this.isPlayerBullet = isPlayerBullet;
        this.active = true;                         // 5. Aktifkan kembali peluru ini (active) dan mulai ulang waktu state animasinya (stateTime) dari 0
        this.stateTime = 0;
        this.grazed = false;                        // 6. Pastikan status graze (grazed) direset menjadi tidak aktif agar peluru daur ulang ini dapat di-graze kembali
    }

    @Override
    public void render(SpriteBatch batch) {
        if (batch != null && active) {
            if (sprite != null) {
                // Gambar sprite peluru menggunakan batch.draw() dengan parameter:
                batch.draw(sprite, x, y, width/2f, height/2f, width, height, 1f, 1f, (angle-90f));   // sprite, posisi (x, y), titik origin putar di tengah (width / 2f, height / 2f), ukuran (width, height), scale (1f, 1f), dan sudut rotasi (angle - 90f)
            } else {
                super.render(batch);
            }
        }
    }

    public boolean getIsPlayerBullet(){     // 1. Buat getter untuk isPlayerBullet, vx, dan vy
        return isPlayerBullet;
    }

    public  float getVx(){
        return vx;
    }

    public float getVy(){
        return vy;
    }

    public float getAngle(){                // 2. Buat getter dan setter untuk angle
        return angle;
    }

    public void setAngle(float angle){
        this.angle = angle;
    }

    public boolean hasBeenGrazed(){         // 3. Buat getter hasBeenGrazed() yang mengembalikan nilai atribut grazed
        return grazed;
    }

    public void setGrazed(boolean grazed){  // 4. Buat setter setGrazed(boolean grazed)
        this.grazed = grazed;
    }
}

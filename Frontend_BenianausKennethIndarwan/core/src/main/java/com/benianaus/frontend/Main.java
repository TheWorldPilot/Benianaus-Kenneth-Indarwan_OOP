package com.benianaus.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input;
import java.util.Iterator;
import com.benianaus.frontend.objects.enemies.Boss;
import com.benianaus.frontend.objects.enemies.Fairy;
import com.benianaus.frontend.objects.items.Item;
import com.benianaus.frontend.objects.Player;
import com.benianaus.frontend.objects.GameObject;
import com.benianaus.frontend.objects.items.ItemType;
import com.benianaus.frontend.systems.AssetManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.benianaus.frontend.systems.EntityFactory.*;

public class Main extends ApplicationAdapter {
    private ShapeRenderer shapeRenderer;

    // Declare fields for Player, Fairy, Boss, Items, and List<GameObject>
    private SpriteBatch batch;
    private Player player;
    private List<Fairy> fairy;
    private Boss boss;
    private Item powerItem;
    private Item pointItem;
    private List<GameObject> entities;

    protected TextureRegion sprite;
    protected Animation<TextureRegion> animation;
    protected float stateTime = 0f;

    @Override
    public void create() {
        batch = new SpriteBatch();

        shapeRenderer = new ShapeRenderer();
        fairy = new ArrayList<>();
        entities = new ArrayList<>();

        AssetManager.getInstance().init();

        player = createPlayer(280, 40, "Reimu Hakurei", 100, 15, 3);
        Fairy fairyRed = createFairy(150, 380, "Red Fairy", 20);
        Fairy fairyBlue = createFairy(250, 380, "Blue Fairy", 20, "fairy_idle_blue");
        fairy.add(fairyRed);
        fairy.add(fairyBlue);
        boss = createBoss(380, 400, "Rumia", 150);

        powerItem = createItem(200, 450, ItemType.POWER);
        pointItem = createItem(320, 480, ItemType.POINT);

        entities.add(player);
        entities.add(fairyRed);
        entities.add(fairyBlue);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // Update logic: items move downwards linearly
        for (GameObject obj : entities) {
            obj.update(delta);
        }

        // Jika tombol Z baru saja ditekan, tambahkan bullet baru hasil player.shootBullet() ke dalam list entities.
        // Clue: Gdx.input.isKeyJustPressed()
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            entities.add(player.shootBullet());
        }

        // Panggil updateAndClean(entities, delta, Gdx.graphics.getWidth(), Gdx.graphics.getHeight())
        // untuk meng-update sekaligus membersihkan entity yang destroyed/off-screen.
        updateAndClean(entities, delta, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // AABB Collision detection antara setiap pasangan unik entity
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                GameObject a = entities.get(i);
                GameObject b = entities.get(j);

                // Cek apakah getCoreHitbox() milik a dan b saling overlap (gunakan method .overlaps() milik Rectangle)
                if ((a.getCoreHitbox()).overlaps(b.getCoreHitbox())) {
                    // Panggil a.onCollision(b) dan b.onCollision(a)
                    a.onCollision(b);
                    b.onCollision(a);

                    if (b instanceof Item) {
                        entities.remove(b);
                    }
                }
            }
        }

        // Clear screen
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        batch.begin();
        for (GameObject entity : entities) {
            if (!entity.isDestroyed()) {
                // TODO-DONE: Buat agar setiap entity melakukan method .render() dengan mengoper parameter SpriteBatch.
                entity.render(batch);
            }
        }
        batch.end();
    }

    @Override
    public void dispose() {
        if (batch != null) {
            batch.dispose();
        }

        // TODO-DONE: Panggil dispose untuk AssetManager agar Texture yang dimuat juga dilepas.
        AssetManager.getInstance().dispose();
    }


    public <T extends GameObject> void updateAndClean(List<T> list, float delta, float screenWidth, float screenHeight) {
        // 1. Dapatkan Iterator<T> dari list yang diberikan.
        Iterator<T> it = list.iterator();

        // 2. Selama masih ada elemen berikutnya (hasNext()):
        //    a. Ambil elemen saat ini menggunakan next(), simpan ke variabel bertipe T.
        //    b. Panggil update(delta) pada elemen tersebut.
        //    c. Jika elemen tersebut isOffScreen(screenWidth, screenHeight) ATAU isDestroyed():
        //       - Tampilkan pesan: "Removed via Generic Iterator: " + [nama class entity, pakai getClass().getSimpleName()]
        //       - Hapus elemen ini dari list menggunakan method milik Iterator (BUKAN list.remove()!).
        while(it.hasNext()){
            T currElement = it.next();
            currElement.update(delta);
            if (currElement.isOffScreen(screenWidth, screenHeight) || currElement.isDestroyed()){
                System.out.println("Removed via Generic Iterator: " + currElement.getClass().getSimpleName());
                it.remove();
            }
        }
    }

}

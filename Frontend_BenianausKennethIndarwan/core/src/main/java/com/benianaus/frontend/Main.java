package com.benianaus.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.benianaus.frontend.objects.enemies.Boss;
import com.benianaus.frontend.objects.enemies.Fairy;
import com.benianaus.frontend.objects.items.Item;
import com.benianaus.frontend.objects.Player;
import com.benianaus.frontend.objects.GameObject;
import com.benianaus.frontend.objects.items.ItemType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends ApplicationAdapter {
    private ShapeRenderer shapeRenderer;

    // TODO 1: Declare fields for Player, Fairy, Boss, Items, and List<GameObject>
    private Player player;
    private Fairy fairy;
    private Boss boss;
    private Item powerItem;
    private Item pointItem;
    private List<GameObject> entities;


    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        entities = new ArrayList<>();

        // 1. Player: Red square (stationary) at bottom
        player = new Player(280, 40, "Reimu Hakurei", 100, 15, 3);

        // 2. Fairy: Pink square (stationary, small)
        fairy = new Fairy(150, 380, "Stage 1 Fairy", 20);

        // 3. Boss: Blue square (stationary, larger size)
        boss = new Boss(380, 400, "Cirno", 150);

        // 4. Items: White squares (moving downwards linearly)
        powerItem = new Item(200, 450, 16, 16, 80f, ItemType.POWER, 500L);
        pointItem = new Item(320, 480, 12, 12, 120f, ItemType.POINT, 1000L);

        entities.add(player);
        entities.add(fairy);
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

        // AABB Collision detection antara setiap pasangan unik entity
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                GameObject a = entities.get(i);
                GameObject b = entities.get(j);

                // TODO: Cek apakah getCoreHitbox() milik a dan b saling overlap (gunakan method .overlaps() milik Rectangle)
                if ((a.getCoreHitbox()).overlaps(b.getCoreHitbox())){
                    // TODO: Panggil a.onCollision(b) dan b.onCollision(a)
                    a.onCollision(b);
                    b.onCollision(a);
                }
            }
        }

        // Clear screen
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        // Render filled hitboxes with ShapeRenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (GameObject obj : entities) {
            obj.render(shapeRenderer);
        }
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
}

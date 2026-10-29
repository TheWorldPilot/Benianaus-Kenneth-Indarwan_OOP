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
import com.benianaus.frontend.systems.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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

    private BulletManager bulletManager;
    private CollisionReferee collisionReferee;
    private float enemyShootTimer = 0f;

    @Override
    public void create() {
        batch = new SpriteBatch();

        shapeRenderer = new ShapeRenderer();
        fairy = new ArrayList<>();
        entities = new ArrayList<>();

        AssetManager.getInstance().init();
        SoundManager.getInstance().init();
        SoundManager.getInstance().playBGM();

        bulletManager = new BulletManager();
        collisionReferee = new CollisionReferee();

        player = EntityFactory.createPlayer(280, 40, "Reimu Hakurei", 100, 15, 3);
        Fairy fairyRed = EntityFactory.createFairy(150, 380, "Red Fairy", 20);
        Fairy fairyBlue = EntityFactory.createFairy(250, 380, "Blue Fairy", 20, "fairy_idle_blue");
        fairy.add(fairyRed);
        fairy.add(fairyBlue);
        boss = EntityFactory.createBoss(380, 400, "Rumia", 150);

        powerItem = EntityFactory.createItem(200, 450, ItemType.POWER);
        pointItem = EntityFactory.createItem(320, 480, ItemType.POINT);

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

        // Jika tombol Z baru saja ditekan, tambahkan bullet baru hasil player.shootBullet() ke dalam list entities.
        // Clue: Gdx.input.isKeyJustPressed()
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            player.shootBullet(bulletManager);
            SoundManager.getInstance().playShoot();
        }

        updateEnemyScheduler(delta);

        // Panggil updateAndClean(entities, delta, Gdx.graphics.getWidth(), Gdx.graphics.getHeight())
        // untuk meng-update sekaligus membersihkan entity yang destroyed/off-screen.
        updateAndClean(entities, delta, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        bulletManager.update(delta, Gdx.graphics.getWidth(),  Gdx.graphics.getHeight());

        collisionReferee.resolveCollisions(player, entities, bulletManager);




        // Clear screen
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        batch.begin();
        bulletManager.render(batch);
        for (GameObject entity : entities) {
            if (!entity.isDestroyed()) {
                entity.render(batch);
            }
        }
        batch.end();
    }

    private void updateEnemyScheduler(float delta) {
        enemyShootTimer += delta;
        if (enemyShootTimer >= 1.5f) {
            boss.shootBullet(bulletManager);
            enemyShootTimer = 0f;
        }
    }

    @Override
    public void dispose() {
        if (batch != null) {
            batch.dispose();
        }

        AssetManager.getInstance().dispose();
        SoundManager.getInstance().dispose();
    }


    public <T extends GameObject> void updateAndClean(List<T> list, float delta, float screenWidth, float screenHeight) {
        Iterator<T> it = list.iterator();

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

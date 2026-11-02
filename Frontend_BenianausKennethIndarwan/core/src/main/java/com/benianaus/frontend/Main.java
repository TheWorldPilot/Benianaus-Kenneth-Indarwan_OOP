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

import com.benianaus.frontend.commands.Command;
import com.benianaus.frontend.commands.InputHandler;
import com.benianaus.frontend.objects.enemies.Boss;
import com.benianaus.frontend.objects.enemies.Fairy;
import com.benianaus.frontend.objects.items.Item;
import com.benianaus.frontend.objects.Player;
import com.benianaus.frontend.objects.GameObject;
import com.benianaus.frontend.objects.items.ItemType;
import com.benianaus.frontend.systems.*;
import com.benianaus.frontend.ui.GameHUD;

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
    private InputHandler inputHandler;
    private GameHUD gameHUD;
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
        inputHandler = new InputHandler();
        gameHUD = new GameHUD();

        player = EntityFactory.createPlayer(200, 50, "Reimu Hakurei", 100, 15, 3);
        fairy.add(EntityFactory.createFairy(150, 380, "Red Fairy", 20));
        fairy.add(EntityFactory.createFairy(250, 380, "Blue Fairy", 20, "fairy_idle_blue"));
        boss = EntityFactory.createBoss(250, 400, "Rumia", 150);
        powerItem = EntityFactory.createItem(180, 450, ItemType.POWER);
        pointItem = EntityFactory.createItem(220, 480, ItemType.POINT);

        player.registerObserver(gameHUD);

        entities.add(player);
        entities.addAll(fairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);

        inputHandler.setEntities(entities);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // Restart Game on 'R' key when Game Over
        if (Gdx.input != null && Gdx.input.isKeyJustPressed(Input.Keys.R) && player.isGameOver()) {
            restartGame();
        }

        // 1. Proses seluruh input pemain melalui Command Pattern (Invoker Command Stream)
        List<Command> commands = inputHandler.handleInput(delta);
        for (Command command : commands){
            command.execute(player, bulletManager);
        }

        // Scheduler tembakan berkala Bos
        updateEnemyScheduler(delta);

        // 3. Generic update & clean untuk entitas standar pada batas Playfield (416, 560)
        updateAndClean(entities, delta, 416, 560);

        // 4. Update dan daur ulang Object Pool pada batas Playfield (416, 560)
        bulletManager.update(delta, 416, 560);

        // 5. Evaluasi tabrakan melalui Mediator Pattern (CollisionReferee)
        collisionReferee.resolveCollisions(player, entities, bulletManager);

        // 6. Bersihkan layar
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        // 7. Render sprite entitas aktif, peluru pool, dan teks Observer GameHUD
        batch.begin();
        bulletManager.render(batch);
        for (GameObject entity : entities) {
            if (!entity.isDestroyed()) {
                entity.render(batch);
            }
        }
        gameHUD.render(batch);
        batch.end();

        // 8. Render garis bingkai UI & indikator Core Hitbox Focus Mode (Digambar DI ATAS sprite!)
        gameHUD.renderBackground();
        player.renderFocusIndicator(gameHUD.getShapeRenderer());

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

        if (gameHUD != null){
            gameHUD.dispose();
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

    private void restartGame() {
        System.out.println("[Main] Restarting game...");
        entities.clear();
        bulletManager.clearEnemyBullets();
        bulletManager.clearPlayerBullets();
        entities.add(player);
        player.resetGame();

        fairy.clear();
        fairy.add(EntityFactory.createFairy(150, 380, "Red Fairy", 20));
        fairy.add(EntityFactory.createFairy(250, 380, "Blue Fairy", 20, "fairy_idle_blue"));
        boss = EntityFactory.createBoss(250, 400, "Rumia", 150);
        powerItem = EntityFactory.createItem(180, 450, ItemType.POWER);
        pointItem = EntityFactory.createItem(220, 480, ItemType.POINT);

        entities.addAll(fairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);
    }

}

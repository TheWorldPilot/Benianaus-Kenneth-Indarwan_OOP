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
import com.benianaus.frontend.objects.patterns.entityStrategy.SCurveMovement;
import com.benianaus.frontend.objects.patterns.entityStrategy.TargetPointMovement;
import com.benianaus.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.benianaus.frontend.systems.*;
import com.benianaus.frontend.ui.GameHUD;
import com.netlab.frontend.objects.patterns.shootingStrategy.AlternatingRingShot;

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

    private float phaseTimer = 0f;
    private int currentPhase = 0; // 0: Kiri Atas (Aimed SpreadShot), 1: Kanan Atas (AlternatingRingShot)



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
        boss = EntityFactory.createBoss(200, 420, "Rumia", 150);
        powerItem = EntityFactory.createItem(180, 450, ItemType.POWER);
        pointItem = EntityFactory.createItem(220, 480, ItemType.POINT);

        player.registerObserver(gameHUD);

        entities.add(player);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);

        inputHandler.setEntities(entities);

        bulletManager.setEntities(entities);
        boss.setMovementPattern(new TargetPointMovement(200f, 420f, 150f));
        boss.setShootingPattern(new SpreadShot(220f, 5, 25f, 10, player, "bullet_danmaku_red"));
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
        // 1. Perbarui wave peri
        updateFairyWave(delta);
        fairy.removeIf(f -> f.isDestroyed() || f.isOffScreen(416, 560));

        // 2. Tembakan Berkala Bos Rumia via Method Injection (Setiap 1.5 detik)
        enemyShootTimer += delta;
        if (enemyShootTimer >= 1.5f && boss != null && !boss.isDestroyed()) {
            // TODO (4 poin): Suruh boss tembak!
            enemyShootTimer = 0f;
        }

        // 3. Dynamic Strategy Swapping Rumia Setiap 4.0 Detik
        phaseTimer += delta;
        if (phaseTimer >= 4.0f && boss != null && !boss.isDestroyed()) {
            phaseTimer = 0f;
            currentPhase = (currentPhase + 1) % 2;

            if (currentPhase == 0) {
                // Fase 1: Pindah ke Kiri Atas (120, 420) & pola tembak Aimed SpreadShot (Red Fan)
                // TODO (1 poin): Suntikkan Movement Pattern ke boss berupa TargetPointMovement ke koordinat (120f, 420f) dengan speed 150f
                boss.setMovementPattern(new TargetPointMovement(120f, 420f, 150f));
                // TODO (1 poin): Suntikkan Shooting Pattern ke boss berupa SpreadShot dengan speed 220f, 5 peluru, sudut 25f, DMG 10, target player, dan sprite "bullet_danmaku_red"
                boss.setShootingPattern(new SpreadShot(220f, 5, 25f, 10, player, "bullet_danmaku_red"));
                System.out.println("[Boss Strategy] Rumia shifted to Upper Left with Aimed SpreadShot!");
            } else {
                // Fase 2: Pindah ke Kanan Atas (280, 420) & pola tembak AlternatingRingShot (Ring Nova)
                // TODO (1 poin): Suntikkan Movement Pattern ke boss berupa TargetPointMovement ke koordinat (280f, 420f) dengan speed 150f
                boss.setMovementPattern(new TargetPointMovement(280f, 420f, 150f));
                // TODO (1 poin): Suntikkan Shooting Pattern ke boss berupa AlternatingRingShot dengan baseSpeed 180f, 16 peluru per cincin, dan DMG 10
                boss.setShootingPattern(new AlternatingRingShot(180f, 16, 10));
                System.out.println("[Boss Strategy] Rumia shifted to Upper Right with AlternatingRingShot!");
            }
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
        resetFairyWave();
        boss.setX(200);
        boss.setY(420);
        boss.setMovementPattern(new TargetPointMovement(200f, 420f, 150f));
        boss.setShootingPattern(new SpreadShot(220f, 5, 25f, 10, player, "bullet_danmaku_red"));
        currentPhase = 0; phaseTimer = 0f; enemyShootTimer = 0f;
    }

    // Stage 1: Wave 1 Fairy Stream
    private float waveTimer = 0f;
    private int wave1SpawnIndex = 0;

    private void updateFairyWave(float delta) {
        float[] spawnX = {60f, 68f, 76f, 84f, 92f};
        waveTimer += delta;
        if (wave1SpawnIndex < spawnX.length && waveTimer >= 1.0f + wave1SpawnIndex * 0.267f) {
            float x = spawnX[wave1SpawnIndex++];
            Fairy f = EntityFactory.createFairy(x, 480f, "Left S-Curve Fairy", 8, "fairy_idle_blue");

            // TODO (2 poin): Tetapkan Movement Pattern ke f sebagai SCurveMovement dengan speed 130f, initialAngle (float) Math.toRadians(270), dan mirrored false
            // TODO (1 poin): Tetapkan Shooting Pattern ke f sebagai null
            f.setMovementPattern(new SCurveMovement(130f, (float) Math.toRadians(270), false));
            f.setShootingPattern(null);

            fairy.add(f);
            entities.add(f);
        }
    }

    private void resetFairyWave() {
        waveTimer = 0f;
        wave1SpawnIndex = 0;
    }
}

package com.netlab.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.benianaus.frontend.Player;
import com.benianaus.frontend.Fairy;
import com.benianaus.frontend.Boss;
import com.benianaus.frontend.Item;
import com.benianaus.frontend.GameObject;

import java.util.ArrayList;
import java.util.List;

public class Main extends ApplicationAdapter {
    private ShapeRenderer shapeRenderer;

    // TODO 1: Declare fields for Player, Fairy, Boss, Items, and List<GameObject>
    Player player;
    Fairy fairy;
    Boss boss;
    Item items;
    List<GameObject> gameObjects;


    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        gameObjects = new ArrayList<>();

        // TODO 2: Instantiate Player (Red square) at (280, 40)
        Player player = new Player(280, 540, "Reimu Hakurei", 100, 15, 3);

        // TODO 3: Instantiate Fairy (Pink square) at (150, 380)
        Fairy fairy = new Fairy(150, 380, "Fairy", 100);


        // TODO 4: Instantiate Boss (Blue square) at (380, 400)
        Boss boss = new Boss(150, 380, "Boss", 100);

        // TODO 5: Instantiate Items (White squares) with downward speeds
        Item item= new Item(150, 380, 30, 30, -20, "Fairy", 100);

        // TODO 6: Add all entities into the gameObjects list polymorphically

    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // 1. Polymorphic Update Loop: Items move downward automatically via Item.update(delta)
        for (GameObject obj : gameObjects) {
            obj.update(delta);
        }

        // 2. Clear Screen
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        // 3. Polymorphic Render Loop: Draw hitboxes with ShapeRenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (GameObject obj : gameObjects) {
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

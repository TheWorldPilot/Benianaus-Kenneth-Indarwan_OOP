package com.benianaus.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.benianaus.frontend.objects.Collidable;
import com.benianaus.frontend.objects.Player;

public class Fairy extends Enemy{
    protected String name;
    protected int hp;
    protected int maxHp;
    protected long scoreValue;

    public Fairy(String name, int hp){
        super(150, 380, 24, 24, Color.PINK, name, hp, 500L);
    }

    public Fairy(float x, float y, String name, int hp){
        super(x, y, 24, 24, Color.PINK, name, hp, 500L);
    }

    @Override
    public void onCollision(Collidable other) {
        // TODO: Cek apakah other yang diterima method ini adalah Player
        if (other instanceof Player){
            // TODO: Cetak "Player touches fairy"
            System.out.println("Player touches fairy");
        }
    }
}

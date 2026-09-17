package com.benianaus.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.benianaus.frontend.objects.Collidable;
import com.benianaus.frontend.objects.Player;

public class Boss extends Enemy{
    private int collisionCooldown;
    private long currTime;

    public Boss(String name, int hp){
        super(380, 400, 48, 48, Color.BLUE, name, hp, 5000L);
    }

    public Boss(float x, float y, String name, int hp){
        super(x, y, 48, 48, Color.BLUE, name, hp, 5000L);
    }

    // Tipe inheritancenya adalah tipe inheritance Hierarchical karena Boss dan Fairy merupakan hasil dari satu Superclass yaitu GameObject->Enemy.

    @Override
    public void onCollision(Collidable other) {
        // TODO: Cek apakah other yang diterima method ini adalah Player
        if (other instanceof Player){
            // TODO: Cetak "Player touches boss"
            if (collisionCooldown == 0) {
                System.out.println("Player touches boss");
                collisionCooldown = 1;
                currTime = System.currentTimeMillis();
            }
            if (System.currentTimeMillis() - currTime >= 1000){
                collisionCooldown = 0;
            }
        }
    }

}

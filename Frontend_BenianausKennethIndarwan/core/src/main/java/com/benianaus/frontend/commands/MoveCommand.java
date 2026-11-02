package com.benianaus.frontend.commands;

import com.benianaus.frontend.objects.Player;
import com.benianaus.frontend.objects.bullets.Bullet;
import com.benianaus.frontend.systems.BulletManager;

public class MoveCommand implements Command {
    private float dx;
    private float dy;

    public MoveCommand(float dx, float dy) {
        // Inisialisasi atribut dx dan dy dari parameter
        this.dx = dx;
        this.dy = dy;
    }

    public void set(float dx, float dy) {
        // set dx & dy sesuai parameter
        this.dx = dx;
        this.dy = dy;
    }

    // Override `execute` dengan logika bisnis berikut:
    @Override
    public void execute(Player player, BulletManager bulletManager){
        player.move(dx,dy);                 // - Panggil metode move(dx, dy) milik player
        player.updateAnimationState(dx);    // - Update animasi player
    }
}

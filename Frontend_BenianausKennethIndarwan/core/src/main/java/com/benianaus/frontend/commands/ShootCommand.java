package com.benianaus.frontend.commands;

import com.benianaus.frontend.objects.Player;
import com.benianaus.frontend.systems.BulletManager;

public class ShootCommand implements Command {
    // Override `execute` dengan logika bisnis berikut:
    @Override
    public void execute(Player player, BulletManager bulletManager) {
        if (player != null) {                   // - Cek apabila player ada
        player.shootBullet(bulletManager);      // - Apabila ya, panggil metode shootBullet(bulletManager) milik player
        }
    }

}

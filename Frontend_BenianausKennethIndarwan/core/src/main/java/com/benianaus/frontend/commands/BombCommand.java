package com.benianaus.frontend.commands;

import com.benianaus.frontend.objects.GameObject;
import com.benianaus.frontend.objects.Player;
import com.benianaus.frontend.systems.BulletManager;

import java.util.List;

public class BombCommand implements Command {
    private List<GameObject> entities;

    public BombCommand() {}

    public BombCommand(List<GameObject> entities) {
        this.entities = entities;   // Inisialisasi atribut entities dari parameter konstruktor
    }

    public void setEntities(List<GameObject> entities) {
        this.entities = entities;   // Inisialisasi entities sesuai parameter
    }

    // Override `execute` dengan logika bisnis berikut:
    @Override
    public void execute(Player player, BulletManager bulletManager) {
        if (player != null){                            // - Cek apabila player ada
            player.useBomb(bulletManager, entities);    // - Apabila ada, panggil metode useBomb(bulletManager, entities) milik player
        }
    }
}

package com.benianaus.frontend.commands;

import com.benianaus.frontend.objects.Player;
import com.benianaus.frontend.systems.BulletManager;

public class FocusCommand implements Command {
    private boolean focused;

    public FocusCommand(boolean focused) {
        this.focused = focused;     // Inisialisasi atribut focused dari parameter konstruktor
    }

    // Override `execute` dengan logika bisnis berikut:
    @Override
    public void execute(Player player, BulletManager bulletManager){
        player.setFocused(focused); // - Panggil metode setFocused(focused) milik player
    }
}

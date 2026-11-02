package com.benianaus.frontend.commands;

import com.benianaus.frontend.objects.Player;
import com.benianaus.frontend.systems.BulletManager;

public interface Command {
    void execute(Player player, BulletManager bulletManager);
}

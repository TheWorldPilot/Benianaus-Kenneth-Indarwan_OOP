package com.benianaus.frontend.objects.patterns;

import com.benianaus.frontend.systems.BulletManager;

public interface ShootingPattern {
    void execute(float originX, float originY, BulletManager bulletManager, boolean isPlayer);
}

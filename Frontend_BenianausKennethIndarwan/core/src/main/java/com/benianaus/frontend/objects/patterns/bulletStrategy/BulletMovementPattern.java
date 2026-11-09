package com.benianaus.frontend.objects.patterns.bulletStrategy;

import com.benianaus.frontend.objects.bullets.Bullet;

public interface BulletMovementPattern {
    void move(Bullet bullet, float delta);
}

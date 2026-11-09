package com.benianaus.frontend.objects.patterns.entityStrategy;

import com.benianaus.frontend.objects.GameObject;

public interface EntityMovementPattern {
    void move(GameObject entity, float delta);
}

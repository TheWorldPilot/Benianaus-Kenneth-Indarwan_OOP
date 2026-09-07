package com.benianaus.frontend;

import java.awt.*;

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
}

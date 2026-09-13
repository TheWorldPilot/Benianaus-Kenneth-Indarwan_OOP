package com.benianaus.frontend;

import com.badlogic.gdx.graphics.Color;

public class Item extends GameObject{
    private String itemType;
    private long scoreValue;

    public Item(float x, float y, String itemType){
        super(x, y, 16, 16, 100f, Color.WHITE);
        this.itemType = itemType;
        this.scoreValue = 1000L;
    }

    public Item(float x, float y, float width, float height, float speed, String itemType){
        super(x, y, width, height, speed, Color.WHITE);
        this.itemType = itemType;
        this.scoreValue = 1000L;
    }

    public Item(float x, float y, float width, float height, float speed, String itemType, long scoreValue){
        super(x, y, width, height, speed, Color.WHITE);;
        this.itemType = itemType;
        this.scoreValue = scoreValue;
    }

    @Override
    public void update(float delta) {
        this.y -= speed * delta;
    }

    public String getItemType() {
        return itemType;
    }

    public long getScoreValue() {
        return scoreValue;
    }
}

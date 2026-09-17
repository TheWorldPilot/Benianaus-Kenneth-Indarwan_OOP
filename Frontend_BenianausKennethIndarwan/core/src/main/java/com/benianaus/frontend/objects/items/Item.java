package com.benianaus.frontend.objects.items;

import com.badlogic.gdx.graphics.Color;
import com.benianaus.frontend.objects.Collidable;
import com.benianaus.frontend.objects.GameObject;
import com.benianaus.frontend.objects.Player;

public class Item extends GameObject {
    private String itemType;
    private long scoreValue;
    private ItemType itemTypeEnum;
    public boolean collected = false;

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

    public Item(float x, float y, ItemType itemTypeEnum){
        super(x, y, 16, 16, 100f, Color.WHITE);
        this.itemType = itemTypeEnum.name();
        this.itemTypeEnum = itemTypeEnum;
        this.scoreValue = itemTypeEnum.getScoreValue();
    }

    public Item(float x, float y, float width, float height, float speed, ItemType itemTypeEnum, long scoreValue){
        super(x, y, width, height, speed, Color.WHITE);
        this.itemType = itemTypeEnum.name();
        this.itemTypeEnum = itemTypeEnum;
        this.scoreValue = scoreValue;
    }


    @Override
    public void update(float delta) {
        this.y -= speed * delta;
    }

    @Override
    public void onCollision(Collidable other) {
        // TODO: Cek apakah other yang diterima method ini adalah Player
        if (other instanceof Player){
            // Item pickup is handled on the Player side via collectItem()
        }
    }

    public String getItemType(){
        return itemType;
    }

    public ItemType getItemTypeEnum() {
        return itemTypeEnum;
    }

    public long getScoreValue() {
        return scoreValue;
    }
}

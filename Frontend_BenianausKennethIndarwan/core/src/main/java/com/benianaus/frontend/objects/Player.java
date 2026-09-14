package com.benianaus.frontend.objects;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.benianaus.frontend.objects.enemies.Enemy;
import com.benianaus.frontend.objects.items.Item;
import com.benianaus.frontend.objects.items.ItemType;

public class Player extends GameObject {

    private String name;
    private int hp;
    private int power;
    private int spellCards;
    private long score;

    public Player(String name, int hp, int power, int spellCards) {
        super(280, 40, 32, 32, 200, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }

    public Player(float x, float y, String name, int hp, int power, int spellCards) {
        super(x, y, 32, 32, 200, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }

    public void takeDamage(int damage) {
        // 1. Kurangi hp sebesar nilai damage.
        // 2. HP tidak boleh bernilai negatif.
        // 3. Jika HP masih lebih dari 0, tampilkan HP yang tersisa dalam format: [PlayerName] took [damage] damage! Remaining HP: [hp]
        // 4. Jika HP menjadi 0, tampilkan pesan bahwa Player telah dikalahkan.

        setHp(getHp() - damage);

        if (getHp() > 0) {
            System.out.println(getName() + " took " + damage + " damage! Remaining HP: " + getHp());
        } else {
            System.out.println("Player lost. Look, i'm sorry!");
        }

    }

    public void shoot(Enemy target) {
        // 1. Buat int bernama damage yang dihitung dengan menambahkan power sebanyak 10.
        // 2. Tampilkan informasi bahwa Player menembak Enemy dalam format: [name] shoots [TargetName] dealing [damage] DMG!
        // 3. Panggil method takeDamage() milik object Enemy.

        int damage = 10 + getPower();
        System.out.println(getName() + " shoots " + target.getName() + " dealing " + damage + " DMG!");

        target.takeDamage(damage);
    }

    public boolean isAlive() {
        // 1. Kembalikan true jika hp > 0 dan false jika sebaliknya
        if (hp > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void addScore(long points) {
        if (points > 0) {
            this.score += points;
            System.out.println(getName() + " gained " + points + " pts! Total Score: " + this.score);
        }
    }

    @Override
    public void update(float delta){
        if (Gdx.input != null) {
            if(Gdx.input.isKeyPressed(Input.Keys.W)){
                y += speed * delta;
            } else if (Gdx.input.isKeyPressed(Input.Keys.S)){
                y -= speed * delta;
            } else if (Gdx.input.isKeyPressed(Input.Keys.A)){
                x -= speed * delta;
            } else if (Gdx.input.isKeyPressed(Input.Keys.D)){
                x += speed * delta;
            }
        }
    }

    @Override
    public void onCollision(Collidable other) {
        // TODO: Cek apakah other yang diterima method ini adalah Item
        if (other instanceof Item){
            // TODO: Cetak "Player touches items" lalu panggil collectItem((Item) other)
            System.out.println("Player touches items");
            collectItem((Item) other);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp){
        this.hp = Math.max(0, hp);
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power){
        this.power = power;
    }

    public int getSpellCards() {
        return spellCards;
    }

    public void setSpellCards(int spellCards){
        this.spellCards = spellCards;
    }

    public long getScore() {
        return score;
    }

    public void collectItem(Item item) {
        System.out.println(getName() + " collected " + item.getItemType() + "!");
        if (item.getScoreValue() > 0) {
            addScore(item.getScoreValue());
        }

        ItemType type = item.getItemTypeEnum();
        if (type != null) {
            switch (type) {
                case POWER -> {
                    // 1. Tambahkan power sebesar type.getPowerBonus() lewat this.power
                    this.power += type.getPowerBonus();
                    // 2. Tambahkan score sebesar item.getScoreValue() lewat addScore() (addScore() sudah otomatis mencetak "gained X pts!")
                    addScore(item.getScoreValue());
                    // 3. Cetak: [name] collected POWER item! Power increased to [power]
                    System.out.println(name + " collected POWER item! Power increased to " + power);
                }
                case POINT -> {
                    // 1. Tambahkan score sebesar item.getScoreValue() lewat addScore()
                    addScore(item.getScoreValue());
                    // 2. Cetak: [name] collected POINT item!
                    System.out.println(name + " collected POINT item!");
                }
                case BOMB -> {
                    // 1. Tambahkan spellCards sebesar 1
                    spellCards++;
                    // 2. Tambahkan score sebesar item.getScoreValue() lewat addScore()
                    addScore(item.getScoreValue());
                    // 3. Cetak: [name] collected BOMB item! SpellCards: [spellCards]
                    System.out.println(name + " collected BOMB item! SpellCards: " + spellCards);
                }
                case LIFE -> {
                    // 1. Tambahkan hp sebesar 20
                    hp += 20;
                    // 2. Tambahkan score sebesar item.getScoreValue() lewat addScore()
                    addScore(item.getScoreValue());
                    // 3. Cetak: [name] collected LIFE item! HP: [hp]
                    System.out.println(name + " collected LIFE item! HP: " + hp);
                }
            }
        } else {
            addScore(item.getScoreValue());
            System.out.println(name + " collected " + item.getItemType() + "!");
        }
    }



}

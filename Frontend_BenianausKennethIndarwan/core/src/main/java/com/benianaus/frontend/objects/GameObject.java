package com.benianaus.frontend.objects;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public abstract class GameObject implements Collidable {

    protected float x;
    protected float y;
    protected float width;
    protected float height;
    protected float speed;
    protected Color color;

    public GameObject(float x, float y, float width, float height, float speed, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.color = color;
    }

    public void update(float delta){
        //Di sini, update dibiarkan kosong karena tidak semua sub-class nantinya membutuhkan update yang sama, sehingga update akan diisi di sub-class lain yang membuutuhkan saja
    }

    public void render(ShapeRenderer shapeRenderer){
        if (shapeRenderer != null && color != null) {
            shapeRenderer.setColor(this.color);
            shapeRenderer.rect(x, y, width, height);
        }
    }

    @Override
    public Rectangle getCoreHitbox() {
        // TODO: kembalikan Rectangle baru sesuai x, y, width, height object ini
        return new Rectangle(x, y, width, height);
    }

    @Override
    public Rectangle getGrazeHitbox() {
        // TODO: kembalikan Rectangle dengan padding +10px di setiap sisi
        return new Rectangle(x-10, y-10, width+20, height+20);
    }

    @Override
    public void onCollision(Collidable other) {
        // Base collision handler (boleh di-override oleh subclass yang butuh bereaksi)
    }

    public float getX() {
        return x;
    }

    public void setX(float x){
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y){
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width){
        if (width > 0) this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height){
        if (height > 0) this.height = height;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed){
        if (speed >= 0) this.speed = speed;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color){
        this.color = color;
    }
}

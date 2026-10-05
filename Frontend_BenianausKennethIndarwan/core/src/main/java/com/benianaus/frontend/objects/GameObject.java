package com.benianaus.frontend.objects;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import org.w3c.dom.Text;

public abstract class GameObject implements Collidable {

    protected float x;
    protected float y;
    protected float width;
    protected float height;
    protected float speed;
    protected Color color;
    protected boolean active = true;

    protected TextureRegion sprite;
    protected Animation<TextureRegion> animation;
    protected float stateTime = 0f;


    public GameObject(float x, float y, float width, float height, float speed, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.color = color;
    }

    public void update(float delta){
        // TODO-DONE: Tambah waktu internal objek agar animasi bergerak maju
        stateTime = stateTime+delta;
    }

    public void render(ShapeRenderer shapeRenderer){
        if (shapeRenderer != null && color != null && active) {
            shapeRenderer.setColor(this.color);
            shapeRenderer.rect(x, y, width, height);
        }
    }

    public void render(SpriteBatch batch) {
        if (batch != null && active) {
            if (animation != null) {
                TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
                batch.draw(currentFrame, x, y, width, height);
            } else if (sprite != null) {
                batch.draw(sprite, x, y, width, height);
            }
        }
    }

    @Override
    public Rectangle getCoreHitbox() {
        // kembalikan Rectangle baru sesuai x, y, width, height object ini
        return new Rectangle(x, y, width, height);
    }

    @Override
    public Rectangle getGrazeHitbox() {
        // kembalikan Rectangle dengan padding +10px di setiap sisi
        return new Rectangle(x-10, y-10, width+20, height+20);
    }

    @Override
    public void onCollision(Collidable other) {
        // Base collision handler (boleh di-override oleh subclass yang butuh bereaksi)
    }

    public boolean isDestroyed() {
        // Kembalikan true jika object TIDAK aktif (active == false)
        if (!active){
            return true;
        } else {
            return false;
        }
    }

    public void destroy() {
        // tandai object ini sebagai tidak aktif
        active = false;
    }

    public boolean isOffScreen(float screenWidth, float screenHeight) {
        // kembalikan true jika posisi x atau y sudah keluar dari batas layar
        // Gunakan margin toleransi 50px di setiap sisi, supaya objek yang baru
        // sedikit melewati tepi layar tidak langsung dianggap hilang.
        if (x < -50 || y < -50 || x > (screenWidth+50) || y > (screenHeight+50)){
            return true;
        } else {
            return false;
        }
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

    public TextureRegion getSprite(){
        return sprite;
    }

    public void setSprite(TextureRegion sprite){
        this.sprite = sprite;
    }

    public Animation<TextureRegion> getAnimation(){
        return animation;
    }

    public void setAnimation(Animation<TextureRegion> animation){
        this.animation = animation;
    }
}

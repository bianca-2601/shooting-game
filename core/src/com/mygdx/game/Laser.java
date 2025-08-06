package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Laser {
    private float x;
    private float y;
    private float speed;
    private Rectangle boundingBox;
    private TextureRegion textureRegion;
    private int damage;

    public Laser(float xCentre, float yCentre,
                 float width, float height,
                 float speed, TextureRegion texture) {
        this.boundingBox = new Rectangle(xCentre - width / 2, yCentre - height / 2, width, height);
        this.x = xCentre - width / 2;
        this.y = yCentre - height / 2;
        this.speed = speed;
        this.textureRegion = texture;
        this.damage = 1;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void update(float deltaTime) {
        boundingBox.y += speed * deltaTime;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(textureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public int getDamage() {
        return damage;
    }
}
package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.Random;

public class Meteor extends Actor {
    private static final Random random = new Random();
    public Rectangle boundingBox;
    private Texture texture;
    private float speed;
    private int hitCount;
    private int damage;

    public Meteor(Texture texture, float x, float y, float width, float height) {
        this.texture = texture;
        this.boundingBox = new Rectangle(x, y, width, height);
        this.speed = random.nextFloat() * 20 + 20;
        this.hitCount = 0;
        this.damage = 1; // Default damage for meteor
    }

    public void update(float deltaTime) {
        boundingBox.y -= speed * deltaTime;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }

    public void hitByLaser() {
        hitCount++;
        if (hitCount >= 5) {
            explode();
        }
    }

    private void explode() {

        System.out.println("Meteor exploded!");
    }

    public boolean isDestroyed() {
        return hitCount >= 5;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }
}

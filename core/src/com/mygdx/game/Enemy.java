package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

public class Enemy extends Actor {
    GameScreen game;
    float x, y;
    float width, height;
    float health = 3;
    float laserCooldown = 0.7f;
    float timeSinceLastLaser = 0;

    private float speed = 100;
    private boolean movingRight = true;

    TextureRegion textureRegion;
    Rectangle boundingBox;

    public Enemy(GameScreen game, float x, float y, float width, float height) {
        this.game = game;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.textureRegion = new TextureRegion(game.assetManager.get("enemy_ship.png", Texture.class));
        this.boundingBox = new Rectangle(x, y, width, height);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        update(delta);
    }

    public void update(float delta) {

        if (movingRight) {
            x += speed * delta;
            if (x + width > MyGdxGame.WORLD_WIDTH) {
                x = MyGdxGame.WORLD_WIDTH - width;
                movingRight = false;
            }
        } else {
            x -= speed * delta;
            if (x < 0) {
                x = 0;
                movingRight = true;
            }
        }
        timeSinceLastLaser += delta;
        boundingBox.setPosition(x, y);
    }
    public boolean shouldRemove() {
        return health <= 0;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(textureRegion, x, y, width, height);
    }

    public boolean isDestroyed() {
        return health <= 0;
    }

    public void hitByLaser(Laser laser) {
        health -= 1;
    }

    public List<Laser> fireLasers() {
        List<Laser> lasers = new ArrayList<>();
        if (timeSinceLastLaser >= laserCooldown) {
            lasers.add(new Laser(x + width / 2, y, 10, 20, -300, new TextureRegion(game.assetManager.get("laser.png", Texture.class))));
            timeSinceLastLaser = 0;
        }
        return lasers;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }
}

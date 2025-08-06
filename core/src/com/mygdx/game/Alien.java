package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Random;

public class Alien extends Actor {
    public static final float ALIEN_WIDTH = 50f;
    public static final float ALIEN_HEIGHT = 50f;
    public static final float ALIEN_SPEED = 50f;
    public static final int REQUIRED_HITS = 10;

    private GameScreen game;
    private float x, y;
    private float velocityX, velocityY;
    private float health = REQUIRED_HITS;
    private TextureRegion alienTexture;
    private Rectangle boundingBox;
    private float timeSinceLastMove;
    private float moveInterval;
    private Random random;

    public Alien(GameScreen game, float x, float y) {
        this.game = game;
        this.x = x;
        this.y = y;
        this.velocityX = 0;
        this.velocityY = -ALIEN_SPEED;
        this.alienTexture = new TextureRegion(game.assetManager.get("alien.png", Texture.class));
        this.boundingBox = new Rectangle(x, y, ALIEN_WIDTH, ALIEN_HEIGHT);
        this.random = new Random(); // Initialize random object here
        this.timeSinceLastMove = 0;
        this.moveInterval = 1f + random.nextFloat() * 2; // Use random object after initialization
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        update(delta);
    }

    public void update(float delta) {
        timeSinceLastMove += delta;

        if (timeSinceLastMove >= moveInterval) {
            velocityX = random.nextFloat() * 2 * ALIEN_SPEED - ALIEN_SPEED;
            timeSinceLastMove = 0;
            moveInterval = 1f + random.nextFloat() * 2; // Reset random interval
        }

        x += velocityX * delta;
        y += velocityY * delta;

        if (x < 0) {
            x = 0;
        } else if (x + getWidth() > MyGdxGame.WORLD_WIDTH) {
            x = MyGdxGame.WORLD_WIDTH - getWidth();
        }

        boundingBox.setPosition(x, y);
    }

    public void draw(SpriteBatch batch) {
        super.draw(batch, 1);
        batch.draw(alienTexture, x, y, ALIEN_WIDTH, ALIEN_HEIGHT);
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public boolean isDestroyed() {
        return health <= 0;
    }

    public void hitByLaser(Laser laser) {
        health -= 1;
    }
}

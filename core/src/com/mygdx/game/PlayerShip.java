package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.List;

public class PlayerShip extends Ship {
    private int lives;
    private int shields;
    private boolean  destroyed = false;
    private TextureRegion explosionTextureRegion;
    private Stage stage;

    public PlayerShip(float x, float y, float width, float height,
                      float movementSpeed, int shield,
                      float laserWidth, float laserHeight,
                      float laserMovementSpeed, float timeBetweenShots,
                      TextureRegion shipTextureRegion,
                      TextureRegion shieldTextureRegion,
                      TextureRegion laserTextureRegion,
                      TextureRegion explosionTextureRegion) {
        super(x, y, width, height, movementSpeed, shield, laserWidth, laserHeight, laserMovementSpeed, timeBetweenShots, shipTextureRegion, shieldTextureRegion, laserTextureRegion);
        this.lives = 3;
        this.shields = 3;
        this.explosionTextureRegion = explosionTextureRegion;
        this.stage = new Stage();
    }

    public void draw(SpriteBatch batch) {
        batch.draw(shipTextureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
        if (shields > 0) {
            batch.draw(shieldTextureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
        }
    }

    @Override
    public void update(float deltaTime) {
        handleInput();
        super.update(deltaTime);
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            moveLeft();
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            moveRight();
        }

        if (canFire()) {
            fireLasers();
        }
    }

    private void moveLeft() {
        float newX = boundingBox.x - movementSpeed * Gdx.graphics.getDeltaTime();
        boundingBox.setX(Math.max(newX, 0));
    }

    private void moveRight() {
        float newX = boundingBox.x + movementSpeed * Gdx.graphics.getDeltaTime();
        float maxX = MyGdxGame.WORLD_WIDTH - boundingBox.width;
        boundingBox.setX(Math.min(newX, maxX));
    }

    @Override
    public List<Laser> fireLasers() {
        List<Laser> lasers = new ArrayList<>();
        float laserSpeed1 = 100;
        lasers.add(new Laser(boundingBox.x + boundingBox.width * 0.07f, boundingBox.y + boundingBox.height * 0.45f,
                laserWidth, laserHeight, laserSpeed1, laserTextureRegion));
        lasers.add(new Laser(boundingBox.x + boundingBox.width * 0.93f, boundingBox.y + boundingBox.height * 0.45f,
                laserWidth, laserHeight, laserSpeed1, laserTextureRegion));

        timeSinceLastShot = 0;

        return lasers;
    }

    public void hitByLaser(Laser laser) {
        if (shields > 0) {
            shields -= laser.getDamage();
            if (shields <= 0) {
                loseLife();
            }
        }
    }

    public void hitByAlien(){
        destroy();
    }

    public void hitByMeteor() {
        if (shields > 0) {
            shields = 0;
        }
        loseLife();
    }

    private void loseLife() {
        lives--;
        if (lives >= 0) {
            shields = 3;
        }
        if (lives < 0) {
            destroy();
        }
    }


    private void destroy() {
        destroyed = true;
        explode();
    }

    private void explode() {
        Explosion explosion = new Explosion(explosionTextureRegion.getTexture(), boundingBox, 1.0f);
        getStage().addActor(explosion);
    }

    public int getLives() {
        return lives;
    }

    public int getShields() {
        return shields;
    }

    public Stage getStage() {
        return stage;
    }
}
package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;

public abstract class Ship {

    protected Rectangle boundingBox;
    protected float movementSpeed;
    protected float laserWidth, laserHeight;
    protected float laserMovementSpeed;
    protected float timeBetweenShots;
    protected TextureRegion shipTextureRegion;
    protected TextureRegion shieldTextureRegion;
    protected TextureRegion laserTextureRegion;
    protected float timeSinceLastShot;

    protected int shield;

    public Ship(float xCentre, float yCentre,
                float width, float height,
                float movementSpeed, int shield,
                float laserWidth, float laserHeight,
                float laserMovementSpeed, float timeBetweenShots,
                TextureRegion shipTextureRegion,
                TextureRegion shieldTextureRegion,
                TextureRegion laserTextureRegion) {
        this.boundingBox = new Rectangle(xCentre - width / 2, yCentre - height / 2, width, height);
        this.movementSpeed = movementSpeed;
        this.shield = shield;
        this.laserWidth = laserWidth;
        this.laserHeight = laserHeight;
        this.laserMovementSpeed = laserMovementSpeed;
        this.timeBetweenShots = timeBetweenShots;
        this.shipTextureRegion = shipTextureRegion;
        this.shieldTextureRegion = shieldTextureRegion;
        this.laserTextureRegion = laserTextureRegion;
        this.timeSinceLastShot = 0;
    }

    public void update(float deltaTime) {
        timeSinceLastShot += deltaTime;
    }

    public abstract List<Laser> fireLasers();

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public float getX() {
        return boundingBox.x;
    }

    public float getY() {
        return boundingBox.y;
    }

    public float getWidth() {
        return boundingBox.width;
    }

    public float getHeight() {
        return boundingBox.height;
    }

    protected boolean canFire() {
        return timeSinceLastShot >= timeBetweenShots;
    }
}

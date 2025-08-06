package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Explosion extends Actor {

    private TextureRegion textureRegion;
    private Rectangle boundingBox;
    private float stateTime;
    private float duration;

    public Explosion(Texture texture, Rectangle boundingBox, float duration) {
        this.textureRegion = new TextureRegion(texture);
        this.boundingBox = new Rectangle(boundingBox);
        this.stateTime = 0;
        this.duration = duration;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
        if (stateTime > duration) {
            this.remove();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }
}

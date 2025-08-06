package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.*;

public class GameScreen implements Screen, InputProcessor {
    MyGdxGame parentGame;
    AssetManager assetManager;

    SpriteBatch batch;
    OrthographicCamera camera;
    Viewport viewport;
    BitmapFont font;
    BitmapFontCache fontCache;
    Stage stage;

    PlayerShip playerShip;
    ArrayList<Laser> playerLasers = new ArrayList<>();
    ArrayList<Laser> enemyLasers = new ArrayList<>();
    ArrayList<Enemy> enemyList = new ArrayList<>();
    ArrayList<Meteor> meteorList = new ArrayList<>();

    private ArrayList<Alien> alienList = new ArrayList<>();

    int score = 0;
    int totalEnemies = 10;
    int totalAliens = 2;
    int activeAliens = 0;
    int activeEnemies = 5;
    int enemiesSpawned = 0;
    int highScore = 0;
    Random randomizer = new Random();
    InputMultiplexer multiInput;
    private Texture laserTexture;
    private float laserSpeed = 300f;

    public SpriteBatch getBatch() {
        return batch;
    }

    public GameScreen(Stage stage) {
        this.stage = stage;
        parentGame = (MyGdxGame) Gdx.app.getApplicationListener();
        assetManager = parentGame.getAssetManager();

        camera = new OrthographicCamera(MyGdxGame.WORLD_WIDTH, MyGdxGame.WORLD_HEIGHT);
        camera.setToOrtho(false, MyGdxGame.WORLD_WIDTH, MyGdxGame.WORLD_HEIGHT);
        viewport = new FitViewport(MyGdxGame.WORLD_WIDTH, MyGdxGame.WORLD_HEIGHT, camera);

        batch = new SpriteBatch();

        font = assetManager.get("regularfont.ttf", BitmapFont.class);
        fontCache = new BitmapFontCache(font);
        fontCache.setText("Score: 0", 5, 5);

        playerShip = createPlayerShip();
        laserTexture = assetManager.get("laser.png", Texture.class);

        spawnEnemies(activeEnemies);
        spawnMeteors(3);
        spawnAliens(2);

        Gdx.input.setInputProcessor(this);
        multiInput = new InputMultiplexer();
        multiInput.addProcessor(this);
        multiInput.addProcessor(stage);
    }

    private PlayerShip createPlayerShip() {
        Texture playerTexture = assetManager.get("player_ship.png", Texture.class);
        Texture shieldTexture = assetManager.get("shield.png", Texture.class);
        Texture laserTexture = assetManager.get("laser.png", Texture.class);
        Texture explosionTexture = assetManager.get("explosion.png", Texture.class);

        return new PlayerShip(
                MyGdxGame.WORLD_WIDTH / 2f, 50f,
                50f, 50f,
                100f, 3,
                5f, 10f,
                50f, 100f,
                new TextureRegion(playerTexture),
                new TextureRegion(shieldTexture),
                new TextureRegion(laserTexture),
                new TextureRegion(explosionTexture)
        );
    }

    private void spawnEnemies(int count) {
        for (int i = 0; i < count && enemiesSpawned < totalEnemies; i++) {
            float x = randomizer.nextFloat() * (MyGdxGame.WORLD_WIDTH - 50);
            float y = MyGdxGame.WORLD_HEIGHT - 50;
            Enemy enemy = new Enemy(this, x, y, 50, 50);
            enemyList.add(enemy);
            enemiesSpawned++;
            stage.addActor(enemy);
        }
    }

    private void spawnAliens(int count) {
        for (int i = 0; i < count && activeAliens < totalAliens; i++) {
            float x = randomizer.nextFloat() * (MyGdxGame.WORLD_WIDTH - Alien.ALIEN_WIDTH);
            float y = randomizer.nextFloat() * (MyGdxGame.WORLD_HEIGHT - Alien.ALIEN_HEIGHT);

            Alien alien = new Alien(this, x, y);
            alienList.add(alien);
            activeAliens++;
            enemiesSpawned++;
            stage.addActor(alien);

            System.out.println("Spawned alien at: " + x + ", " + y);
        }
    }


    private void spawnMeteors(int count) {
        Texture meteorTexture = assetManager.get("meteor.png", Texture.class);

        for (int i = 0; i < count; i++) {
            float x = randomizer.nextFloat() * (MyGdxGame.WORLD_WIDTH - 30);
            float y = MyGdxGame.WORLD_HEIGHT + randomizer.nextFloat() * 200;
            Meteor meteor = new Meteor(meteorTexture, x, y, 30, 30);
            meteorList.add(meteor);
            enemiesSpawned++;
            stage.addActor(meteor);
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        playerShip.update(delta);
        playerShip.draw(batch);
        List<Laser> playerFiredLasers = playerShip.fireLasers();
        playerLasers.addAll(playerFiredLasers);
        Iterator<Laser> playerLaserIter = playerLasers.iterator();
        while (playerLaserIter.hasNext()) {
            Laser laser = playerLaserIter.next();
            laser.update(delta);
            laser.draw(batch);

            if (laser.getY() > MyGdxGame.WORLD_HEIGHT) {
                playerLaserIter.remove();
            }
        }

        Iterator<Laser> enemyLaserIter = enemyLasers.iterator();
        while (enemyLaserIter.hasNext()) {
            Laser laser = enemyLaserIter.next();
            laser.update(delta);
            laser.draw(batch);

            if (laser.getY() < 0) {
                enemyLaserIter.remove();
            }
        }

        Iterator<Enemy> enemyIter = enemyList.iterator();
        while (enemyIter.hasNext()) {
            Enemy enemy = enemyIter.next();
            enemy.update(delta);
            enemy.draw(batch);

            List<Laser> enemyFiredLasers = enemy.fireLasers();
            enemyLasers.addAll(enemyFiredLasers);

            if (enemy.getY() < 0 || enemy.isDestroyed()) {
                enemyIter.remove();
            }
        }

        Iterator<Meteor> meteorIter = meteorList.iterator();
        while (meteorIter.hasNext()) {
            Meteor meteor = meteorIter.next();
            meteor.update(delta);
            meteor.draw(batch, 1);

            if (meteor.getY() < 0 || meteor.isDestroyed()) {
                meteorIter.remove();
            }
        }

        Iterator<Alien> alienIter = alienList.iterator();
        while (alienIter.hasNext()) {
            Alien alien = alienIter.next();
            alien.update(delta);
            alien.draw(batch);

            if (alien.getY() < 0 || alien.getX() < 0 ||alien.isDestroyed()) {
                alienIter.remove();
            }
        }

        handleCollisions();

        BitmapFont font = assetManager.get("regularfont.ttf", BitmapFont.class);
        font.getData().setScale(1, -1);
        BitmapFontCache fontCache = new BitmapFontCache(font);

        fontCache.setText("SHIELD: " + playerShip.getShields(), MyGdxGame.WORLD_WIDTH / 2f - 50, MyGdxGame.WORLD_HEIGHT - 50);
        fontCache.draw(batch);

        fontCache.setText("LIVES: " + playerShip.getLives(), MyGdxGame.WORLD_WIDTH - 130, MyGdxGame.WORLD_HEIGHT - 50);
        fontCache.draw(batch);

        fontCache.setText("SCORE: " + score, 5, MyGdxGame.WORLD_HEIGHT - 50);
        fontCache.draw(batch);

        if (playerShip.getLives() <= 0) {
            gameOver();
        }

        if (enemyList.isEmpty() && meteorList.isEmpty() && enemiesSpawned == totalEnemies) {
            System.out.println("No more enemies, meteors, or aliens left. Calling gameOver()");
            gameOver();
        }

        batch.end();

        stage.act(delta);
        stage.draw();
        //System.out.println("Enemies: " + enemyList.size() + ", Meteors: " + meteorList.size() + ", EnemiesSpawned: " + enemiesSpawned);
    }


    public void shootPlayerLaser() {
        float laserWidth = laserTexture.getWidth();
        float laserHeight = laserTexture.getHeight();

        float x = playerShip.getX() + playerShip.getWidth() / 2 - laserWidth / 2;
        float y = playerShip.getY() + playerShip.getHeight();

        Laser laser = new Laser(x, y, laserSpeed, laserWidth, laserHeight, new TextureRegion(laserTexture));
        playerLasers.add(laser);
    }



    public void gameOver() {
        System.out.println("Game Over");

        if (score > highScore) {
            highScore = score;
            System.out.println("New High Score: " + highScore);
        }

        this.dispose();
        parentGame.setScreen(new GameOverScreen(parentGame));
    }
    private void handleCollisions() {
        List<Laser> lasersToRemove = new ArrayList<>();
        List<Meteor> meteorsToRemove = new ArrayList<>();
        List<Enemy> enemiesToRemove = new ArrayList<>();
        List<Alien> aliensToRemove = new ArrayList<>();

        for (Laser laser : playerLasers) {
            for (Meteor meteor : meteorList) {
                if (meteor.getBoundingBox().overlaps(laser.getBoundingBox())) {
                    meteor.hitByLaser();
                    lasersToRemove.add(laser);
                    if (meteor.isDestroyed()) {
                        meteorsToRemove.add(meteor);
                        score += 100;
                    }
                }
            }

            for (Enemy enemy : enemyList) {
                if (enemy.getBoundingBox().overlaps(laser.getBoundingBox())) {
                    enemy.hitByLaser(laser);
                    lasersToRemove.add(laser);
                    if (enemy.isDestroyed()) {
                        enemiesToRemove.add(enemy);
                        score += 200;
                    }
                }
            }

            for (Alien alien : alienList) {
                if (alien.getBoundingBox().overlaps(laser.getBoundingBox())) {
                    alien.hitByLaser(laser);
                    lasersToRemove.add(laser);
                    if (alien.isDestroyed()) {
                        aliensToRemove.add(alien);
                        score += 300;
                    }
                }
            }
        }

        for (Laser laser : enemyLasers) {
            if (playerShip.getBoundingBox().overlaps(laser.getBoundingBox())) {
                playerShip.hitByLaser(laser);
                lasersToRemove.add(laser);
                break;
            }
        }

        for (Meteor meteor : meteorList) {
            if (playerShip.getBoundingBox().overlaps(meteor.getBoundingBox())) {
                playerShip.hitByMeteor();
                meteorsToRemove.add(meteor);
                score -= 50;
            }
        }

        for (Alien alien : alienList) {
            if (playerShip.getBoundingBox().overlaps(alien.getBoundingBox())) {
                playerShip.hitByAlien();
                aliensToRemove.add(alien);
                gameOver();  // langsung game over jika terkena alien
                return;
            }
        }

        playerLasers.removeAll(lasersToRemove);
        enemyLasers.removeAll(lasersToRemove);
        meteorList.removeAll(meteorsToRemove);
        enemyList.removeAll(enemiesToRemove);
        alienList.removeAll(aliensToRemove);

        //jSystem.out.println("Enemies: " + enemyList.size() + ", Meteors: " + meteorList.size() + ", Aliens: " + alienList.size() + ", EnemiesSpawned: " + enemiesSpawned);





        if (enemyList.isEmpty() && meteorList.isEmpty() && alienList.isEmpty() && enemiesSpawned >= totalEnemies) {
            System.out.println("No more enemies, meteors, or aliens left. Calling gameOver()");
            gameOver();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        font.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (playerShip.getLives() > 0) {
            shootPlayerLaser();
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}

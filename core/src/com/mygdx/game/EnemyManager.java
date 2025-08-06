package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class EnemyManager {
    private Array<Enemy> enemies;
    private GameScreen game;

    private static final int MAX_ENEMIES = 15;
    private static final int MAX_VISIBLE_ENEMIES = 3;

    public EnemyManager(GameScreen game) {
        this.game = game;
        this.enemies = new Array<>();
        spawnInitialEnemies();
    }

    private void spawnInitialEnemies() {
        for (int i = 0; i < MAX_ENEMIES; i++) {
            float x = MathUtils.random(0, MyGdxGame.WORLD_WIDTH);
            float y = MathUtils.random(0, MyGdxGame.WORLD_HEIGHT);
            float width = 64; // Adjust size as needed
            float height = 64; // Adjust size as needed
            Enemy enemy = new Enemy(game, x, y, width, height);
            enemies.add(enemy);
        }
    }

    public void update(float delta) {
        for (Enemy enemy : enemies) {
            enemy.update(delta);
        }
        removeDeadEnemies();
    }

    private void removeDeadEnemies() {
        Array<Enemy> enemiesToRemove = new Array<>();
        for (Enemy enemy : enemies) {
            if (enemy.shouldRemove()) {
                enemiesToRemove.add(enemy);
            }
        }
        enemies.removeAll(enemiesToRemove, true); // Remove dead enemies from the array

        // Spawn new enemies to maintain MAX_VISIBLE_ENEMIES
        int currentVisibleEnemies = getVisibleEnemiesCount();
        int enemiesToSpawn = MAX_VISIBLE_ENEMIES - currentVisibleEnemies;
        for (int i = 0; i < enemiesToSpawn; i++) {
            spawnNewEnemy();
        }
    }

    private void spawnNewEnemy() {
        float x = MathUtils.random(0, MyGdxGame.WORLD_WIDTH);
        float y = MathUtils.random(0, MyGdxGame.WORLD_HEIGHT);
        float width = 64; // Adjust size as needed
        float height = 64; // Adjust size as needed
        Enemy enemy = new Enemy(game, x, y, width, height);
        enemies.add(enemy);
    }

    public void draw() {
        for (Enemy enemy : enemies) {
            enemy.draw(game.getBatch());
        }
    }

    private int getVisibleEnemiesCount() {
        int count = 0;
        for (Enemy enemy : enemies) {
            if (enemy.getY() < MyGdxGame.WORLD_HEIGHT) { // Assuming enemies are removed when they go off-screen
                count++;
            }
        }
        return count;
    }
}

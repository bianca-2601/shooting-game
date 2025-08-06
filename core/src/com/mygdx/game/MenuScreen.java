package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class MenuScreen implements Screen, InputProcessor {
    MyGdxGame parentGame;
    AssetManager assetManager;

    private Viewport viewport;
    private OrthographicCamera camera, stageCamera;
    SpriteBatch batch;

    Stage stage;
    Label titleLabel;
    TextButton playButton;
    Label highScoreLabel;

    InputMultiplexer multiInput;

    Preferences prefs;

    public MenuScreen() {
        parentGame = (MyGdxGame) Gdx.app.getApplicationListener();
        assetManager = parentGame.getAssetManager();

        camera = new OrthographicCamera(MyGdxGame.WORLD_WIDTH, MyGdxGame.WORLD_HEIGHT);
        camera.setToOrtho(true, MyGdxGame.WORLD_WIDTH, MyGdxGame.WORLD_HEIGHT);
        viewport = new FitViewport(MyGdxGame.WORLD_WIDTH, MyGdxGame.WORLD_HEIGHT, camera);
        batch = new SpriteBatch();

        stageCamera = new OrthographicCamera(MyGdxGame.WORLD_WIDTH, MyGdxGame.WORLD_HEIGHT);
        stageCamera.setToOrtho(false, MyGdxGame.WORLD_WIDTH, MyGdxGame.WORLD_HEIGHT);
        stage = new Stage(new FitViewport(MyGdxGame.WORLD_WIDTH, MyGdxGame.WORLD_HEIGHT, stageCamera));

        multiInput = new InputMultiplexer();
        multiInput.addProcessor(this);
        multiInput.addProcessor(stage);

        prefs = Gdx.app.getPreferences("MyGdxGamePreferences");

        Skin mySkin = assetManager.get("uiskin.json", Skin.class);

        titleLabel = new Label("Space Shooter Game", mySkin);
        Label.LabelStyle style = new Label.LabelStyle(titleLabel.getStyle());
        style.font = assetManager.get("titlefont.ttf", BitmapFont.class);
        titleLabel.setStyle(style);
        titleLabel.setWidth(800);
        titleLabel.setX(0);
        titleLabel.setY(400);
        titleLabel.setAlignment(Align.center);
        titleLabel.setColor(Color.WHITE);
        stage.addActor(titleLabel);


        playButton = new TextButton("Play", mySkin);
        playButton.setHeight(64);
        playButton.setWidth(180);
        playButton.setPosition(400 - playButton.getWidth()/ 2, 200);
        playButton.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                parentGame.changeScreen(new GameScreen(stage));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(playButton);

        highScoreLabel = new Label("High Score: " + prefs.getInteger("highscore", 0), mySkin);
        highScoreLabel.setStyle(style);
        highScoreLabel.setWidth(800);
        highScoreLabel.setX(0);
        highScoreLabel.setY(300);
        highScoreLabel.setAlignment(Align.center);
        highScoreLabel.setColor(Color.WHITE);

        stage.addActor(highScoreLabel);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(multiInput);
        highScoreLabel.setText("High Score: " + prefs.getInteger("highscore", 0));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        Texture background = assetManager.get("menubgfix.png", Texture.class);

        for(int i=0; i<10; i++) {
            for (int j = 0; j < 18; j++)
                batch.draw(background, j * 64, i * 64);
        }

        batch.end();
        update();

        stage.act();
        stage.draw();

    }

    public void update()
    {

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        stage.clear();
    }

    @Override
    public void dispose() {
        stage.dispose();
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
        return false;
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

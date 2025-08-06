package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class MyGdxGame extends Game implements InputProcessor {

	public static final int WORLD_WIDTH = 800;
	public static final int WORLD_HEIGHT = 600;

	AssetManager manager = new AssetManager();

	Music music;

	public AssetManager getAssetManager() {
		return manager;
	}

	@Override
	public void create () {

		manager = new AssetManager();

		// register loader
		FileHandleResolver resolver = new InternalFileHandleResolver();
		manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

		FreetypeFontLoader.FreeTypeFontLoaderParameter fontParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
		fontParameter.fontFileName = "font.ttf";
		fontParameter.fontParameters.size = 32;
		fontParameter.fontParameters.color = Color.WHITE;
		fontParameter.fontParameters.borderColor = Color.BLACK;
		fontParameter.fontParameters.borderWidth = 2;
		fontParameter.fontParameters.flip = true;
		manager.load("regularfont.ttf", BitmapFont.class, fontParameter);

		//font diload dulu
		manager.finishLoading();

		manager.load("badlogic.jpg", Texture.class);
		manager.load("menubgfix.png", Texture.class);
		manager.load("bg.png", Texture.class);
		manager.load("enemy_ship.png", Texture.class);
		manager.load("explosion.png", Texture.class);
		manager.load("laser.png", Texture.class);
		manager.load("meteor.png", Texture.class);
		manager.load("shield.png", Texture.class);
		manager.load("player_ship.png", Texture.class);
		manager.load("alien.png", Texture.class);
		manager.load("gameover.png", Texture.class);

		manager.finishLoading();

		FreetypeFontLoader.FreeTypeFontLoaderParameter titlefontParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
		titlefontParameter.fontFileName = "font.ttf";
		titlefontParameter.fontParameters.size = 48;
		titlefontParameter.fontParameters.color = Color.WHITE;
		titlefontParameter.fontParameters.borderColor = Color.BLACK;
		titlefontParameter.fontParameters.borderWidth = 2;
		titlefontParameter.fontParameters.flip = false;
		manager.load("titlefont.ttf", BitmapFont.class, titlefontParameter);

		SkinLoader.SkinParameter skinParam = new SkinLoader.SkinParameter("uiskin.atlas");
		manager.load("uiskin.json", Skin.class, skinParam);
		this.setScreen(new LoadingScreen());
	}

	@Override
	public void render () {
		super.render();

	}

	public void update()
	{


	}
	public void changeScreen(Screen newScreen) {
		((Game) Gdx.app.getApplicationListener()).setScreen(newScreen);
	}


	public static TextureRegion[] CreateAnimationFrames(Texture tex, int frameWidth, int frameHeight, int frameCount, boolean flipx, boolean flipy)
	{
		//akan membuat frame animasi dari texture, texture dipotong2 sebesar frameWidth x frameHeight
		// frame akan diambil dari posisi kiri atas ke kanan bawah
		TextureRegion[][] tmp = TextureRegion.split(tex,frameWidth, frameHeight);
		TextureRegion[] frames = new TextureRegion[frameCount];
		int index = 0;
		int row = tex.getHeight() / frameHeight;
		int col = tex.getWidth() / frameWidth;
		for (int i = 0; i < row && index < frameCount; i++) {
			for (int j = 0; j < col && index < frameCount; j++) {
				frames[index] = tmp[i][j];
				frames[index].flip(flipx, flipy);
				index++;
			}
		}
		return frames;
	}
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void dispose () {
		manager.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		return true;

	}

	@Override
	public boolean keyUp(int keycode) {
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		//obj.setPosition(screenX, screenY);
		/*Vector3 tp = new Vector3(screenX, screenY, 0);
		camera.unproject(tp);
		obj.setOriginBasedPosition(tp.x, tp.y);*/
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

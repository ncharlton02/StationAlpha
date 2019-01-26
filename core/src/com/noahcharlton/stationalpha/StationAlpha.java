package com.noahcharlton.stationalpha;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.noahcharlton.stationalpha.block.Blocks;
import com.noahcharlton.stationalpha.engine.GameRenderer;
import com.noahcharlton.stationalpha.engine.assets.AssetManager;
import com.noahcharlton.stationalpha.world.World;

import java.util.Optional;

public class StationAlpha extends ApplicationAdapter {

	private static StationAlpha instance;
	private GameRenderer gameRenderer;

	private Optional<World> world = Optional.empty();
	private long lastFPSTime;

	public StationAlpha() {
		if(instance != null)
			throw new GdxRuntimeException("Cannot create instance more than once!");

		instance = this;
	}

	@Override
	public void create () {
		gameRenderer = new GameRenderer();

		Blocks.init();
		world = Optional.of(new World());

		System.out.println("Number of assets: " + AssetManager.getInstance().getNumberOfAssets());
	}

	@Override
	public void render () {
		while(!AssetManager.getInstance().isDone()){
			return;
		}

		gameRenderer.render();

		updateFPS();
	}

	private void updateFPS() {
		if(lastFPSTime + 1000 < System.currentTimeMillis()){
			lastFPSTime = System.currentTimeMillis();

			System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond());
		}
	}

	@Override
	public void resize(int width, int height) {
		gameRenderer.resize(width, height);
	}

	@Override
	public void dispose () {
		AssetManager.getInstance().dispose();
	}

	public Optional<World> getWorld() {
		return world;
	}

	public static StationAlpha getInstance() {
		return instance;
	}
}
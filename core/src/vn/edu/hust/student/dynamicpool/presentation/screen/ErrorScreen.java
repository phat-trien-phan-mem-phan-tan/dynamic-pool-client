package vn.edu.hust.student.dynamicpool.presentation.screen;

import vn.edu.hust.student.dynamicpool.presentation.WorldRenderer;
import vn.edu.hust.student.dynamicpool.presentation.assets.Assets;
import vn.edu.hust.student.dynamicpool.utils.AppConst;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ErrorScreen implements Screen {
	private WorldRenderer worldRenderer;
	private BitmapFont defaultFont;
	private BitmapFont smallFont;
	private String message;
	SpriteBatch batch;

	public ErrorScreen(WorldRenderer worldRenderer) {
		this.worldRenderer = worldRenderer;
		this.batch = worldRenderer.getBatch();
	}

	public void showError(String errorMessage) {
		this.message = errorMessage == null || errorMessage.equals("") ? "" : errorMessage;
	}

	@Override
	public void render(float delta) {
		worldRenderer.beginRender();
		batch.setColor(Color.RED);
		float x = AppConst.width / 2
				- defaultFont.getBounds(AppConst.ERROR_TITLE_TEXT).width / 2;
		defaultFont.draw(batch, AppConst.ERROR_TITLE_TEXT, x,
				AppConst.height / 2);
		smallFont.drawWrapped(batch, message, AppConst.width / 4,
				AppConst.height / 4, AppConst.width / 2, HAlignment.CENTER);
		worldRenderer.endRender();
	}

	@Override
	public void resize(int width, int height) {
		worldRenderer.resize(width, height);
	}

	@Override
	public void show() {
		this.defaultFont = Assets.instance.assetFonts.getDefaultFont();
		this.smallFont = Assets.instance.assetFonts.getSmallFont();
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}
}

package vn.edu.hust.student.dynamicpool.presentation.assets;

import vn.edu.hust.student.dynamicpool.utils.AppConst;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;

public class AssetLoadFonts {
	private BitmapFont defaultFont;
	private BitmapFont smallFont;
	private BitmapFont lagerFont;
	public void load(AssetManager assetManager) {
		FileHandleResolver resolver = new InternalFileHandleResolver();
		assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
		FreeTypeFontLoaderParameter size1Params = new FreeTypeFontLoaderParameter();
		size1Params.fontFileName = AppConst.DEFAUFT_FONT;
		size1Params.fontParameters.size =  AppConst.NORMAL_FONT_SIZE;
		assetManager.load(AppConst.DEFAULT_FONT_NAME, BitmapFont.class, size1Params);
		
		FreeTypeFontLoaderParameter smallSizePamrams = new FreeTypeFontLoaderParameter();
		smallSizePamrams.fontFileName = AppConst.DEFAUFT_FONT;
		smallSizePamrams.fontParameters.size =  AppConst.SMALL_FONT_SIZE;
		assetManager.load(AppConst.SMALL_FONT_NAME, BitmapFont.class, smallSizePamrams);
		
		FreeTypeFontLoaderParameter lagerSizePamrams = new FreeTypeFontLoaderParameter();
		lagerSizePamrams.fontFileName = AppConst.DEFAUFT_FONT;
		lagerSizePamrams.fontParameters.size =  AppConst.LARGE_FONT_SIZE;
		assetManager.load(AppConst.LARGE_FONT_NAME, BitmapFont.class, lagerSizePamrams);
	}

	public void bind(AssetManager assetManager) {
		defaultFont = assetManager.get(AppConst.DEFAULT_FONT_NAME, BitmapFont.class);
		smallFont = assetManager.get(AppConst.SMALL_FONT_NAME, BitmapFont.class);
		lagerFont = assetManager.get(AppConst.LARGE_FONT_NAME);
	}
	
	public BitmapFont getDefaultFont() {
		return defaultFont;
	}
	
	public BitmapFont getSmallFont() {
		return smallFont;
	}
	
	public BitmapFont getLagerFont() {
		return lagerFont;
	}
	
}

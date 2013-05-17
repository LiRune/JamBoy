package com.matimdev.manager;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import android.graphics.Color;

import com.matimdev.GameActivity;
import com.matimdev.database.DataBase;
import com.matimdev.scene.MainMenuScene;

public class ResourcesManager
{
	//---------------------------------------------
	// VARIABLES
	//---------------------------------------------
	
	private static final ResourcesManager INSTANCE = new ResourcesManager();
	
	public Engine engine;
	public static GameActivity activity;
	public BoundCamera camera;
	public VertexBufferObjectManager vbom;
	
	public Font font;
	
	public Music music;
	
	//---------------------------------------------
	// TEXTURES & TEXTURE REGIONS
	//---------------------------------------------
	
	public ITextureRegion splash_region;
	public ITextureRegion menu_background_region;
	public ITextureRegion play_region;
	public ITextureRegion options_region;
	
	// Game Texture
	public BuildableBitmapTextureAtlas gameTextureAtlas;
	
	// Game Texture Regions
	public ITextureRegion platform1_region;
	public ITextureRegion platform2_region;
	public ITextureRegion platform3_region;
	
	
	public ITextureRegion coin_region;
	public ITextureRegion heart_region;
	public ITextureRegion bala_region;
	public ITextureRegion llave_region;
	public ITextureRegion derecha_region;
	public ITextureRegion izquierda_region;
	public ITextureRegion saltar_region;
	public ITextureRegion pausa_region;
	public ITextureRegion reanudar_region;
	public ITextureRegion reloj_region;
	public ITiledTextureRegion explosion_region;
	
	public ITextureRegion fondo_region;
	public ITextureRegion fondo2_region;
	
	
	public ITiledTextureRegion player_region;
	public ITiledTextureRegion  enemy_region;
	
	private BitmapTextureAtlas splashTextureAtlas;
	private BuildableBitmapTextureAtlas menuTextureAtlas;
	
	// Level Complete Window
	public ITextureRegion complete_window_region;
	public ITiledTextureRegion complete_stars_region;
	
	public DataBase game;
	
	
	
	public DataBase getGame() {
		return game;
	}

	public void setGame(DataBase game) {
		this.game = game;
	}

	public static GameActivity getActivity() {
		return activity;
	}

	public void setActivity(GameActivity activity) {
		this.activity = activity;
	}
	
	
	//---------------------------------------------
	// CLASS LOGIC
	//---------------------------------------------

	

	public void loadMenuResources()
	{
		loadMenuGraphics();
		loadMenuAudio();
		loadMenuFonts();
		game = new DataBase(activity);
	}
	
	public void loadGameResources()
	{
		loadGameGraphics();
		loadGameFonts();
		loadGameAudio();
	}
	
	private void loadMenuGraphics()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
        menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
        menuTextureAtlas.addEmptyTextureAtlasSource(0, 0, 1024, 1024);
        menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu_background.png");
        play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "play.png");
        options_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "options.png");
        derecha_region =BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "der.png");
       
    	try 
    	{
			this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.menuTextureAtlas.load();
		} 
    	catch (final TextureAtlasBuilderException e)
    	{
			Debug.e(e);
		}
	}
	
	protected void loadMenuAudio()
	{
		try {
			if(music != null)
			{
				if(music.isPlaying())
				{
					music.stop();
				}
				
			}
			
			music = MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity, "mfx/menu.mp3");
			music.setLooping(true);
			music.play();
			
			if (MainMenuScene.isMusica() == false)
			{
				engine.getMusicManager().setMasterVolume(0);
			}
		} 
		catch (IOException e)
		{
		    e.printStackTrace();
		}
	}
	
	private void loadMenuFonts()
	{
		FontFactory.setAssetBasePath("font/");
		final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font.ttf", 50, true, Color.WHITE, 2, Color.BLACK);
		font.load();
	}

	private void loadGameGraphics()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		//el 1500, 1500 es el espacio del atlas
        gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1500, 1500, TextureOptions.BILINEAR);
        gameTextureAtlas.addEmptyTextureAtlasSource(0, 0, 1500, 1500);
       	platform1_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform1.png");
       	platform2_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform2.png");
       	platform3_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform3.png");
        coin_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "coin.png");
        player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player.png", 4, 1);
        heart_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "heart.png");
        bala_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "bala25.png");
        enemy_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player.png", 4, 1);
        llave_region =BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "key.png");
        reloj_region =BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "clock.png");
        explosion_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "explosion.png", 3, 1);
        
        fondo2_region =BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "fondo2.png");
        
        fondo_region =BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "fondo.png");
       
        
        izquierda_region =BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "izq.png");
        derecha_region =BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "der.png");
        saltar_region =BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "salt.png");
        pausa_region =BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "pausa.png");
        reanudar_region =BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "reiniciar.png");
        
        complete_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "levelCompleteWindow.png");
        complete_stars_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "star.png", 2, 1);

    	try 
    	{
			this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.gameTextureAtlas.load();
			
		} 
    	catch (final TextureAtlasBuilderException e)
    	{
			Debug.e(e);
		}
    	
	}
	
	private void loadGameFonts()
	{
		 
	}
	
	private void loadGameAudio()
	{
		try {
			if(music != null)
			{
				if(music.isPlaying())
				{
					music.stop();
				}
				
			}
			
			music = MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity, "mfx/game.mp3");
			music.setLooping(true);
			music.play();
			
			if (MainMenuScene.isMusica() == false)
			{
				engine.getMusicManager().setMasterVolume(0);
			}
		} 
		catch (IOException e)
		{
		    e.printStackTrace();
		}
	}
	
	public void unloadGameTextures()
	{
		// TODO (Since we did not create any textures for game scene yet)
	}
	
	public void loadSplashScreen()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash.png", 0, 0);
        splashTextureAtlas.load();	
	}
	
	public void unloadSplashScreen()
	{
		splashTextureAtlas.unload();
		splash_region = null;
	}
	
	public void loadMenuTextures()
	{
		menuTextureAtlas.load();
	}
	
	public void unloadMenuTextures()
	{
		menuTextureAtlas.unload();
	}
	
	/**
	 * @param engine
	 * @param activity
	 * @param camera
	 * @param vbom
	 * <br><br>
	 * We use this method at beginning of game loading, to prepare Resources Manager properly,
	 * setting all needed parameters, so we can latter access them from different classes (eg. scenes)
	 */
	public static void prepareManager(Engine engine, GameActivity activity, BoundCamera camera, VertexBufferObjectManager vbom)
	{
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camera = camera;
		getInstance().vbom = vbom;
	}
	
	//---------------------------------------------
	// GETTERS AND SETTERS
	//---------------------------------------------
	
	public static ResourcesManager getInstance()
	{
		return INSTANCE;
	}
}

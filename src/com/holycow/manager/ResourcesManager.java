package com.holycow.manager;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
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

import com.holycow.GameActivity;
import com.holycow.database.DataBase;
import com.holycow.scene.MainMenuScene;

/**Clase que gestiona los recursos
 * @author Samir El Aoufi
 * @author Juan José Cillero
 * @author Rubén Díaz
 *
 */

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

	/// Fuente ///
	public Font font;

	/// Música y sonido ///
	public Music music;
	public static Sound salto, disparar, enemigo_muerte, grito, coger_llave;

	/// Base de Datos ///
	public DataBase db;

	//---------------------------------------------
	// TEXTURES & TEXTURE REGIONS
	//---------------------------------------------

	public ITextureRegion splash_region;
	public ITextureRegion menu_background_region;
	public ITextureRegion play_region;
	public ITextureRegion options_region;
	public ITextureRegion musica_region;
	public ITextureRegion sonido_region;
	public ITextureRegion personaje1_region;
	public ITextureRegion personaje2_region;
	public ITextureRegion personaje3_region;
	
	public ITiledTextureRegion personajeSelec1_region;
	public ITiledTextureRegion personajeSelec2_region;
	public ITiledTextureRegion personajeSelec3_region;
	
	public ITiledTextureRegion numeroEstrellas;
	

	/// Game Texture ///
	public BuildableBitmapTextureAtlas gameTextureAtlas;
	public BitmapTextureAtlas paralaxTextureAtlas;
	private BitmapTextureAtlas splashTextureAtlas;
	private BuildableBitmapTextureAtlas menuTextureAtlas;

	
	public ITextureRegion paralax1;
	public ITextureRegion paralax2;
	public ITextureRegion paralax3;
	
	/// Game Texture Regions ///
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
	public ITextureRegion atacar_region;
	public ITextureRegion reiniciar_region;
	public ITextureRegion volver_menu_region;
	public ITextureRegion reloj_region;
	public ITextureRegion fondo_region;
	public ITextureRegion fondo2_region;
	
	public ITiledTextureRegion player_region;
	public ITiledTextureRegion  enemy_region;
	public ITiledTextureRegion explosion_region;

	// Level Complete Window
	public ITextureRegion complete_window_region;
	public ITiledTextureRegion complete_stars_region;

	//---------------------------------------------
	// GETTERS & SETTERS
	//---------------------------------------------

	public static GameActivity getActivity() {
		return activity;
	}

	public void setActivity(GameActivity activity) {
		this.activity = activity;
	}

	public DataBase getGame() {
		return db;
	}

	public void setGame(DataBase db) {
		this.db = db;
	}

	//---------------------------------------------
	// CLASS LOGIC
	//---------------------------------------------
	/**
	 * Carga las texturas, sonidos y fuentes del menu. Crea la base de datos.
	 */
	public void loadMenuResources()
	{
		loadMenuGraphics();
		loadMenuAudio();
		loadMenuFonts();
		db = new DataBase(activity);
	}
	
	/**
	 * Carga las texturas, sonidos y fuentes del juego.
	 */
	public void loadGameResources()
	{
		loadGameGraphics();
		loadGameFonts();
		loadGameAudio();
	}

	/**
	 * Se crea el atlas y en el se anade las texturas creadas para el menu
	 */
	private void loadMenuGraphics()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		menuTextureAtlas.addEmptyTextureAtlasSource(0, 0, 1024, 1024);
		menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu_background.png");
		play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "play.png");
		options_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "options.png");
		musica_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "musica1.png");
		sonido_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "musica2.png");
		derecha_region =BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "der.png");
		
		
		personaje1_region =BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "personaje1.png");
		personaje2_region =BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "personaje2.png");
		personaje3_region =BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "personaje3.png");
		
		personajeSelec1_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(menuTextureAtlas, activity, "personaje1.png", 7, 1);
		personajeSelec2_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(menuTextureAtlas, activity, "personaje2.png", 7, 1);
		personajeSelec3_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(menuTextureAtlas, activity, "personaje3.png", 7, 1);
		
		
		numeroEstrellas = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(menuTextureAtlas, activity, "stars.png", 2, 1);
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

	/**
	 * Especifica la ruta del audio y distintas opciones. Audio del menu
	 */
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

	
	/**
	 * Crea la fuente del menu y la carga
	 */
	private void loadMenuFonts()
	{
		FontFactory.setAssetBasePath("font/");
		final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font.ttf", 50, true, Color.WHITE, 2, Color.BLACK);
		font.load();
	}

	
	/**
	 * Se crea el atlas y en el se anade las texturas creadas para el juego
	 */
	private void loadGameGraphics()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		//el 1500, 1500 es el espacio del atlas
		gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1500, 1500, TextureOptions.BILINEAR);
		gameTextureAtlas.addEmptyTextureAtlasSource(0, 0, 1500, 1500);




		paralaxTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(),1024, 1024, TextureOptions.DEFAULT);
		paralaxTextureAtlas.addEmptyTextureAtlasSource(0, 0, 1024, 1024);
		//paralax1= BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.paralaxTextureAtlas, activity, "paralax1.png", 0, 0);
		paralax2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.paralaxTextureAtlas, activity, "paralax2.png", 0, 188);
		//paralax3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.paralaxTextureAtlas, activity, "paralax3.png", 0, 669);



		platform1_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform1.png");
		platform2_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform2.png");
		platform3_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform3.png");
		coin_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "coin.png");

		System.out.println("PERSONAJE SELECCIONADO:" +MainMenuScene.getIdPersonaje());
		if(MainMenuScene.getIdPersonaje()==1){
			player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "personaje1.png", 7, 1);
		}
		if(MainMenuScene.getIdPersonaje()==2){
			player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "personaje2.png", 7, 1);
		}
		if(MainMenuScene.getIdPersonaje()==3){
			player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "personaje3.png", 7, 1);
		}




		heart_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "heart.png");
		bala_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "bala25.png");
		enemy_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "enemy.png",8, 1);
		llave_region =BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "key.png");
		reloj_region =BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "clock.png");
		explosion_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "explosion.png", 3, 1);
		
		fondo2_region =BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "fondo2.png");

		fondo_region =BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "fondo.png");


		izquierda_region =BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "izq.png");
		derecha_region =BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "der.png");
		saltar_region =BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "salt.png");
		pausa_region =BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "pausa.png");
		reanudar_region =BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "reanudar.png");
		volver_menu_region =BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "volvermenu.png");
		atacar_region =BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "atacar.png");
		reiniciar_region =BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "reiniciar.png");
		
		complete_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "levelCompleteWindow.png");
		complete_stars_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "star.png", 2, 1);

		try 
		{
			this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.gameTextureAtlas.load();
			this.paralaxTextureAtlas.load();

		} 
		catch (final TextureAtlasBuilderException e)
		{
			Debug.e(e);
		}

	}

	/**
	 * 
	 * Se crea la fuente para el juego
	 */	 
	private void loadGameFonts()
	{

	}

	/**
	 * Especifica la ruta del audio y diferentes opciones. Audio del juego
	 */
	private void loadGameAudio()
	{
		try {
			// Música //
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

			// Sonido //
			SoundFactory.setAssetBasePath("sfx/");
			salto = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity, "saltar.wav");   
			disparar = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity.getApplicationContext(), "disparo.mp3");  
			enemigo_muerte = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity.getApplicationContext(), "enemigo_muerte.mp3");
			grito = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity.getApplicationContext(), "grito.ogg");
			coger_llave = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity.getApplicationContext(), "coger_llave.wav");
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Deja de cargar las texturas del Splash
	 */
	public void unloadGameTextures()
	{
		// TODO (Since we did not create any textures for game scene yet)
	}

	
	/**
	 * Se crea el atlas y en el se anade las texturas creadas para el splash.
	 */	 
	public void loadSplashScreen()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash.png", 0, 0);
		splashTextureAtlas.load();	
	}

	 /**
	  * Deja de cargar las texturas del Splash
	  */
	public void unloadSplashScreen()
	{
		splashTextureAtlas.unload();
		splash_region = null;
	}

	/**
	 * Carga texturas del menu
	 */
	public void loadMenuTextures()
	{
		menuTextureAtlas.load();
	}

	/**
	 * Dejar de cargar las texturas del menu
	 */
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
	 * Se utiliza para cargar el juego al principio, prepara el Resources Manager apropiadamente,
	 * Despues podremos acceder a las diferentes clases, por ejemplo escenas.
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

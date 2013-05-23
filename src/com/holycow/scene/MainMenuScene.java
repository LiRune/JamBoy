package com.holycow.scene;

import java.util.Vector;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.AnimatedSpriteMenuItem;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.holycow.base.BaseScene;
import com.holycow.database.DataBase;
import com.holycow.extras.LevelCompleteWindow;
import com.holycow.extras.LevelCompleteWindow.StarsCount;
import com.holycow.manager.ResourcesManager;
import com.holycow.manager.SceneManager;
import com.holycow.manager.SceneManager.SceneType;

/**Creacion de las escenas del menu
 * @author Samir El Aoufi
 * @author Juan José Cillero
 * @author Rubén Díaz
 *
 */

public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener
{
	//---------------------------------------------
	// VARIABLES
	//---------------------------------------------

	// ChildScenes
	private MenuScene menuChildScene;
	private MenuScene optionsChildScene;
	private MenuScene seleccionNivelChildScene;
	private MenuScene seleccionPersonajeChildScene;
	private MenuScene aboutChildScene;

	// Estrellas
	private TiledSprite star1;
	private TiledSprite star2;
	private TiledSprite star3;
	

	//Boton play mostrar
	private boolean jugarVisible=false;
	private boolean botonPulsado=false;

	// Nivel y personaje seleccionados
	private static int idNivel = 0;
	private static int idPersonaje = 1;
	private static int idBoton =0;
	private final int NIVELES=2;

	// Las distintas opciones de menú
	private final int MENU_PLAY = 0;
	private final int MENU_OPTIONS = 1;
	private final int MUSICA = 2;
	private final int SONIDO = 3;
	private final int NIVEL1 = 4;
	private final int NIVEL2 = 5;
	private final int JUGAR = 6;
	private final int SELECCION_PERSONAJE = 7;
	private final int PERSONAJE1 = 8;
	private final int PERSONAJE2 = 9;
	private final int PERSONAJE3 = 10;
	private final int VOLVERSEL = 11;
	private final int ABOUT = 12;

	
	private AnimatedSpriteMenuItem sonidoBoton;
	private AnimatedSpriteMenuItem musicaBoton;
	
	private AnimatedSpriteMenuItem Nivel1;
	private AnimatedSpriteMenuItem Nivel2;
	
	
	// Textos
	private Text puntuaciones;
	private Text puntosText;
	private Text nivel1Text;
	private Text nivel2Text;
	private Text samir;
	private Text juanjo;
	private Text ruben;
	private Text desarrollado;


	// Animación de los personajes
	private AnimatedSprite pers1;
	private AnimatedSprite pers2;
	private AnimatedSprite pers3;


	private IMenuItem Jugar;
	// Música y sonido
	private static boolean musica = true;
	private static boolean sonido = true;
	private float volumenMusica = engine.getMusicManager().getMasterVolume();
	private float volumenSonido = engine.getSoundManager().getMasterVolume();

	//---------------------------------------------
	// GETTERS AND SETTERS
	//---------------------------------------------

	public static int getIdPersonaje() {
		return idPersonaje;
	}

	public static void setIdPersonaje(int idPersonaje) {
		MainMenuScene.idPersonaje = idPersonaje;
	}

	public static int getIdNivel() {
		return idNivel;
	}

	public void setIdNivel(int idNivel) {
		this.idNivel = idNivel;
	}

	public static boolean isMusica() {
		return musica;
	}

	public void setMusica(boolean musica) {
		this.musica = musica;
	}

	//---------------------------------------------
	// MÉTODOS DE LA SUPERCLASS
	//---------------------------------------------

	/**
	 * Crea la escena
	 */
	@Override
	public void createScene()
	{
		createBackground();
		createMenuChildScene();
	}

	/**
	 * Accion que realiza al pulsar el boton de atras del movil
	 */
	@Override
	public void onBackKeyPressed()
	{
		if(getChildScene() == optionsChildScene || getChildScene() == seleccionNivelChildScene)
		{
			setChildScene(menuChildScene);
			idNivel=0;
		}
		else if(getChildScene() == seleccionPersonajeChildScene){
			setChildScene(seleccionNivelChildScene);			
		}
		else if(getChildScene() == aboutChildScene){
			setChildScene(optionsChildScene);			
		}
		else if(getChildScene() == menuChildScene){
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					dialogoSalir();
				}
			});
		}
	}

	/**
	 * Accion que realiza al pulsar el boton de Home del movil
	 */
	public void onHomeKeyPressed()
	{

	}

	/**
	 * Obtiene el tipo de escena
	 */
	@Override
	public SceneType getSceneType()
	{
		return SceneType.SCENE_MENU;
	}

	/**
	 * Eliminación de la escena
	 */
	@Override
	public void disposeScene()
	{

	}

	/**
	 * Segun el boton del menu clicado se hara una accion
	 */
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY)
	{
		switch(pMenuItem.getID())
		{
		case MENU_PLAY:
			// Carga selección de nivel
			createSeleccionNivelChildScene();
			return true;

		case MENU_OPTIONS:
			// Carga el menú de opciones
			createOptionsChildScene();
			return true;

		case MUSICA:
			// Activa/Desactiva música
			if (musica)
			{
				musica = false;
				engine.getMusicManager().setMasterVolume(0);
			}
			else
			{
				musica = true;
				engine.getMusicManager().setMasterVolume(volumenMusica);
			}			
			botonesTiledMusica();
			return true;

		case SONIDO:
			// Activa/Desactiva sonido
			if (sonido)
			{
				sonido = false;
				engine.getSoundManager().setMasterVolume(0);
			}
			else
			{
				sonido = true;
				engine.getSoundManager().setMasterVolume(volumenSonido);
			}
			botonesTiledSonido();
			return true;

		case NIVEL1:
			idNivel = 1;
			puntuaciones.setText(mostrarPuntuacion(idNivel));
			ponerEstrellas(mostrarEstrellas(idNivel));
			botonesTiledNivel();
			Jugar.setVisible(true);
			return true;

		case NIVEL2:				
			idNivel = 2;
			puntuaciones.setText(mostrarPuntuacion(idNivel));
			ponerEstrellas(mostrarEstrellas(idNivel));
			botonesTiledNivel();
			Jugar.setVisible(true);

			return true;

		case JUGAR:
			// Carga el nivel seleccionado
			if(idNivel!=0){
				if(DataBase.isUnlocked(idNivel)){
					SceneManager.getInstance().loadGameScene(engine);
				}
			}
			return true;

		case SELECCION_PERSONAJE:
			//Cargar Scene de PERSONAJES
			createSeleccionPersonajeChildScene();
			return true;

		case PERSONAJE1:	
			if(idPersonaje==2 || idPersonaje==3){
				pers2.setVisible(false);
				pers3.setVisible(false);
			}
			idPersonaje = 1;	
			pers1.setVisible(true);
			pers1.animate(100);
			return true;

		case PERSONAJE2:	
			if(idPersonaje==1 || idPersonaje==3){
				pers1.setVisible(false);
				pers3.setVisible(false);
			}			
			idPersonaje = 2;
			pers2.setVisible(true);
			pers2.animate(100);
			return true;

		case PERSONAJE3:	
			if(idPersonaje==1 || idPersonaje==2){
				pers1.setVisible(false);
				pers2.setVisible(false);
			}			
			idPersonaje = 3;
			pers3.setVisible(true);
			pers3.animate(100);
			return true;

		case VOLVERSEL:	
			if(getChildScene() == optionsChildScene || getChildScene() == seleccionNivelChildScene)
			{
				setChildScene(menuChildScene);
				idNivel=0;
			}
			else if(getChildScene() == seleccionPersonajeChildScene){
				setChildScene(seleccionNivelChildScene);
			}
			else if(getChildScene() == aboutChildScene){
				setChildScene(optionsChildScene);
			}
			return true;

		case ABOUT:			
			createAboutChildScene();
			return true;

		default:
			return false;
		}
	}

	//---------------------------------------------
	// LÓGICA DE CLASE
	//---------------------------------------------

	/**
	 * Crea el fondo del menú principal
	 */
	private void createBackground()
	{
		attachChild(new Sprite(400, 240, getResourcesManager().menu_background_region, getVbom())
		{
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) 
			{
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		});
	}


	/**
	 * Crea la pantalla de Menu Principal
	 */
	private void createMenuChildScene()
	{
		menuChildScene = new MenuScene(camera);
		menuChildScene.setPosition(0, 0);

		final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, getResourcesManager().play_region, getVbom()), 0.9f, 1);
		final IMenuItem optionsMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_OPTIONS, getResourcesManager().options_region, getVbom()), 0.9f, 1);

		menuChildScene.addMenuItem(playMenuItem);
		menuChildScene.addMenuItem(optionsMenuItem);

		menuChildScene.buildAnimations();
		menuChildScene.setBackgroundEnabled(false);

		playMenuItem.setPosition(playMenuItem.getX(), playMenuItem.getY() - 50);
		optionsMenuItem.setPosition(optionsMenuItem.getX(), optionsMenuItem.getY() - 70);

		menuChildScene.setOnMenuItemClickListener(this);

		setChildScene(menuChildScene);
	}

	/**
	 * Crea la pantalla de Opciones
	 */
	private void createOptionsChildScene()
	{
		optionsChildScene = new MenuScene(camera);
		optionsChildScene.setPosition(0, 0);

		sonidoBoton = new AnimatedSpriteMenuItem(SONIDO, getResourcesManager().sonido_region, getVbom());
		musicaBoton = new AnimatedSpriteMenuItem(MUSICA, getResourcesManager().musica_region, getVbom());
		
		final IMenuItem volverMenu = new ScaleMenuItemDecorator(new SpriteMenuItem(VOLVERSEL, getResourcesManager().volverMenu_region, getVbom()), 0.9f, 1);
		final IMenuItem aboutMenu = new ScaleMenuItemDecorator(new SpriteMenuItem(ABOUT, getResourcesManager().about_region, getVbom()), 0.9f, 1);
		optionsChildScene.addMenuItem(musicaBoton);
		optionsChildScene.addMenuItem(sonidoBoton);
		optionsChildScene.addMenuItem(aboutMenu);
		optionsChildScene.addMenuItem(volverMenu);

		
		//Como default ponemos las estrellas grises
		if(musica){
			musicaBoton.setCurrentTileIndex(0);
		}else{
			musicaBoton.setCurrentTileIndex(1);
		}
		
		if(sonido){
			sonidoBoton.setCurrentTileIndex(0);
		}else{
			sonidoBoton.setCurrentTileIndex(1);
		}
		
		

		optionsChildScene.buildAnimations();
		optionsChildScene.setBackgroundEnabled(true);
		final ParallaxBackground background = new ParallaxBackground(0,0,0);		
		background.attachParallaxEntity(new ParallaxEntity(0, new Sprite(400, 240,resourcesManager.menu_background2_region, vbom)));
		optionsChildScene.setBackground(background);

		musicaBoton.setPosition(musicaBoton.getX(), musicaBoton.getY() - 30);
		sonidoBoton.setPosition(sonidoBoton.getX(), sonidoBoton.getY() - 60);
		aboutMenu.setPosition(aboutMenu.getX(), aboutMenu.getY() - 90);
		volverMenu.setPosition(50, 50);

		optionsChildScene.setOnMenuItemClickListener(this);

		setChildScene(optionsChildScene);
	}

	/**
	 * Crea la pantalla de Seleccion de Nivel
	 */
	private void createSeleccionNivelChildScene()
	{
		seleccionNivelChildScene = new MenuScene(camera);
		seleccionNivelChildScene.setPosition(0, 0);

		System.out.println(getChildScene()); 

		nivel1Text = new Text(0, 0, getResourcesManager().font, "1", getVbom());
		nivel2Text = new Text(0, 0, getResourcesManager().font, "2", getVbom());


		//BOTONES NIVELES
		
		Nivel1 = new AnimatedSpriteMenuItem(NIVEL1, getResourcesManager().niveles_region, getVbom());
		Nivel2 = new AnimatedSpriteMenuItem(NIVEL2, getResourcesManager().niveles_region, getVbom());
		
		final IMenuItem personajeMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(SELECCION_PERSONAJE, getResourcesManager().seleccionPersonaje_region, getVbom()), 0.9f, 1);
		final IMenuItem volverMenu = new ScaleMenuItemDecorator(new SpriteMenuItem(VOLVERSEL, getResourcesManager().volverMenu_region, getVbom()), 0.9f, 1);


		Nivel1.setHeight(100);
		Nivel1.setWidth(100);
		Nivel2.setHeight(100);
		Nivel2.setWidth(100);

		//BOTON PLAY
		Jugar = new ScaleMenuItemDecorator(new SpriteMenuItem(JUGAR, getResourcesManager().jugar_region, getVbom()), 0.9f, 1);

		//TEXTO PUNTUACIONES
		puntuaciones = new Text(0, 0, getResourcesManager().font, "01234567892", getVbom());
		puntosText = new Text(0, 0, getResourcesManager().font, "Puntos: ", new TextOptions(HorizontalAlign.RIGHT), getVbom());
		puntosText.setText("Puntos: ");


		if(idNivel!=0){
			puntuaciones.setText(mostrarPuntuacion(idNivel));
		}else{
			puntuaciones.setText("0");
		}

		//Sprites de las estrellas		
		star1 = new TiledSprite(30, 450, ResourcesManager.getInstance().numeroEstrellas, vbom);
		star2 = new TiledSprite(110, 450, ResourcesManager.getInstance().numeroEstrellas, vbom);
		star3 = new TiledSprite(190, 450, ResourcesManager.getInstance().numeroEstrellas, vbom);

		//Como default ponemos las estrellas grises
		star1.setCurrentTileIndex(1);
		star2.setCurrentTileIndex(1);
		star3.setCurrentTileIndex(1);
		
		Nivel1.setCurrentTileIndex(0);
		Nivel2.setCurrentTileIndex(0);

		seleccionNivelChildScene.attachChild(star1);
		seleccionNivelChildScene.attachChild(star2);
		seleccionNivelChildScene.attachChild(star3);


		seleccionNivelChildScene.addMenuItem(Nivel1);
		seleccionNivelChildScene.addMenuItem(Nivel2);
		seleccionNivelChildScene.addMenuItem(Jugar);
		seleccionNivelChildScene.addMenuItem(personajeMenuItem);
		seleccionNivelChildScene.addMenuItem(volverMenu);
		seleccionNivelChildScene.attachChild(nivel1Text);
		seleccionNivelChildScene.attachChild(nivel2Text);
		seleccionNivelChildScene.attachChild(puntuaciones);
		seleccionNivelChildScene.attachChild(puntosText);


		seleccionNivelChildScene.buildAnimations();
		seleccionNivelChildScene.setBackgroundEnabled(true);
		final ParallaxBackground background = new ParallaxBackground(0,0,0);		
		background.attachParallaxEntity(new ParallaxEntity(0, new Sprite(400, 240,resourcesManager.menu_background2_region, vbom)));
		seleccionNivelChildScene.setBackground(background);

		Nivel1.setPosition(80, 350);
		Nivel2.setPosition(Nivel1.getX() + 120, Nivel1.getY());
		personajeMenuItem.setPosition(400, 50);
		volverMenu.setPosition(50, 50);

		Jugar.setPosition(750, 50);
		puntosText.setPosition(500, 450);
		puntuaciones.setPosition(puntosText.getX() + 200, puntosText.getY());			
		//estrellas.setPosition(20, 450);

		nivel1Text.setPosition(Nivel1.getX(), Nivel1.getY());
		nivel2Text.setPosition(Nivel2.getX(), Nivel2.getY());

		Jugar.setVisible(false);


		seleccionNivelChildScene.setOnMenuItemClickListener(this);

		setChildScene(seleccionNivelChildScene);
	}

	/**
	 * Crea la pantalla de seleccion de personaje
	 */
	private void createSeleccionPersonajeChildScene()
	{

		seleccionPersonajeChildScene = new MenuScene(camera);
		seleccionPersonajeChildScene.setPosition(0, 0);

		System.out.println(getChildScene()); 

		final IMenuItem personaje1MenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(PERSONAJE1, getResourcesManager().personaje1_region, getVbom()), 0.9f, 1);
		final IMenuItem personaje2MenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(PERSONAJE2, getResourcesManager().personaje2_region, getVbom()), 0.9f, 1);
		final IMenuItem personaje3MenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(PERSONAJE3, getResourcesManager().personaje3_region, getVbom()), 0.9f, 1);
		final IMenuItem volverMenu = new ScaleMenuItemDecorator(new SpriteMenuItem(VOLVERSEL, getResourcesManager().volverMenu_region, getVbom()), 0.9f, 1);

		pers1 = new AnimatedSprite(400, 300, getResourcesManager().personajeSelec1_region, vbom);
		pers2 = new AnimatedSprite(400, 300, getResourcesManager().personajeSelec2_region, vbom);
		pers3 = new AnimatedSprite(400, 300, getResourcesManager().personajeSelec3_region, vbom);


		pers1.setVisible(false);
		pers2.setVisible(false);
		pers3.setVisible(false);

		if(idPersonaje==1){
			pers1.setVisible(true);
			pers1.animate(100);
		}else if(idPersonaje==2){
			pers2.setVisible(true);
			pers2.animate(100);
		}else if(idPersonaje==3){
			pers3.setVisible(true);
			pers3.animate(100);
		}




		pers1.animate(100);

		seleccionPersonajeChildScene.addMenuItem(personaje1MenuItem);
		seleccionPersonajeChildScene.addMenuItem(personaje2MenuItem);
		seleccionPersonajeChildScene.addMenuItem(personaje3MenuItem);
		seleccionPersonajeChildScene.addMenuItem(volverMenu);
		seleccionPersonajeChildScene.attachChild(pers1);
		seleccionPersonajeChildScene.attachChild(pers2);
		seleccionPersonajeChildScene.attachChild(pers3);

		seleccionPersonajeChildScene.buildAnimations();
		seleccionPersonajeChildScene.setBackgroundEnabled(true);
		final ParallaxBackground background = new ParallaxBackground(0,0,0);		
		background.attachParallaxEntity(new ParallaxEntity(0, new Sprite(400, 240,resourcesManager.menu_background2_region, vbom)));
		seleccionPersonajeChildScene.setBackground(background);

		volverMenu.setPosition(50, 50);
		personaje1MenuItem.setPosition(250, 150);
		personaje2MenuItem.setPosition(400, 150);
		personaje3MenuItem.setPosition(550, 150);

		seleccionPersonajeChildScene.setOnMenuItemClickListener(this);

		setChildScene(seleccionPersonajeChildScene);
	}

	private void createAboutChildScene()
	{
		aboutChildScene = new MenuScene(camera);
		aboutChildScene.setPosition(0, 0);

		System.out.println(getChildScene()); 

		final IMenuItem volverMenu = new ScaleMenuItemDecorator(new SpriteMenuItem(VOLVERSEL, getResourcesManager().volverMenu_region, getVbom()), 0.9f, 1);



		//TEXTO PUNTUACIONES
		samir = new Text(0, 0, getResourcesManager().font, "Samir El Aoufi", getVbom());
		juanjo = new Text(0, 0, getResourcesManager().font, "Juanjo Cillero", getVbom());
		ruben = new Text(0, 0, getResourcesManager().font, "Ruben Diaz", getVbom());
		desarrollado = new Text(0, 0, getResourcesManager().font, "Desarrollado por:", getVbom());
		desarrollado.setColor(1, 0, 0);

		desarrollado.setPosition(400,400);
		samir.setPosition(400,300);
		juanjo.setPosition(400,200);
		ruben.setPosition(400,100);


		pers1 = new AnimatedSprite(samir.getX()+280, samir.getY()+10, getResourcesManager().personajeSelec1_region, vbom);
		pers2 = new AnimatedSprite(juanjo.getX()+280, juanjo.getY()+10, getResourcesManager().personajeSelec2_region, vbom);
		pers3 = new AnimatedSprite(ruben.getX()+280, ruben.getY()+10, getResourcesManager().personajeSelec3_region, vbom);

		pers1.animate(100);
		pers2.animate(100);
		pers3.animate(100);

		aboutChildScene.addMenuItem(volverMenu);
		aboutChildScene.attachChild(desarrollado);
		aboutChildScene.attachChild(samir);
		aboutChildScene.attachChild(juanjo);
		aboutChildScene.attachChild(ruben);
		aboutChildScene.attachChild(pers1);
		aboutChildScene.attachChild(pers2);
		aboutChildScene.attachChild(pers3);

		aboutChildScene.buildAnimations();
		aboutChildScene.setBackgroundEnabled(true);
		final ParallaxBackground background = new ParallaxBackground(0,0,0);		
		background.attachParallaxEntity(new ParallaxEntity(0, new Sprite(400, 240,resourcesManager.menu_background2_region, vbom)));
		aboutChildScene.setBackground(background);



		volverMenu.setPosition(50, 50);



		aboutChildScene.setOnMenuItemClickListener(this);

		setChildScene(aboutChildScene);
	}


	/**
	 * Hace una consulta a la bd para devolver la puntuacion del nivel clicado
	 * @param id
	 * @return puntuacion
	 */
	private String mostrarPuntuacion(int id)
	{
		String punt= null;
		DataBase myDB = new DataBase(ResourcesManager.getActivity());

		SQLiteDatabase db = myDB.getReadableDatabase();

		Cursor c = db.rawQuery(" SELECT Puntuacion FROM Niveles WHERE Numero ="+id, null);
		if (c.moveToFirst()) {
			//Recorremos el cursor hasta que no haya más registros
			do {
				punt = c.getString(0);
				System.out.println("PUNTUACION DE LA BD: "+punt);

			} while(c.moveToNext());
		}

		db.close();		
		return punt;
	}

	/**
	 * Hace una consulta a la bd para devolver el numero de estrella del nivel clicado
	 * @param id
	 * @return numero de estrellas
	 */
	private String mostrarEstrellas(int id)
	{
		String estr= null;
		DataBase myDB = new DataBase(ResourcesManager.getActivity());

		SQLiteDatabase db = myDB.getReadableDatabase();

		Cursor c = db.rawQuery(" SELECT Estrellas FROM Niveles WHERE Numero ="+id, null);
		if (c.moveToFirst()) {
			//Recorremos el cursor hasta que no haya más registros
			do {
				estr = c.getString(0);
				System.out.println("ESTRELLAS DE LA BD: "+estr);

			} while(c.moveToNext());
		}

		db.close();		
		return estr;
	}

	/**
	 * Segun el numero de estrellas que tenga el nivel pondra las estrellas de un colo o de otro
	 * @param numero
	 */
	private void ponerEstrellas(String numero){
		int numeroInt = Integer.parseInt(numero);

		System.out.println("ESTRELLAS A MOSTRAR: "+numeroInt);

		if(idNivel!=0){
			switch (numeroInt)
			{
			case 0:
				star1.setCurrentTileIndex(1);
				star2.setCurrentTileIndex(1);
				star3.setCurrentTileIndex(1);
				break;
			case 1:
				star1.setCurrentTileIndex(0);
				star2.setCurrentTileIndex(1);
				star3.setCurrentTileIndex(1);
				break;
			case 2:
				star1.setCurrentTileIndex(0);
				star2.setCurrentTileIndex(0);
				star3.setCurrentTileIndex(1);
				break;
			case 3:
				star1.setCurrentTileIndex(0);
				star2.setCurrentTileIndex(0);
				star3.setCurrentTileIndex(0);
				break;
			}
		}

	}

	/**
	 * Cambiar el tiled del boton
	 * @param numero
	 */	
	private void botonesTiledMusica(){
		

		//System.out.println("ESTRELLAS A MOSTRAR: "+numeroInt);

		if(musicaBoton.getCurrentTileIndex()==0){
			musicaBoton.setCurrentTileIndex(1);	
		}else{
			musicaBoton.setCurrentTileIndex(0);	
		}
	}
	
	private void botonesTiledSonido(){
			
		if(sonidoBoton.getCurrentTileIndex()==0){
			sonidoBoton.setCurrentTileIndex(1);
		}else{
			sonidoBoton.setCurrentTileIndex(0);	
		}
	}

	private void botonesTiledNivel(){
		if(idNivel!=0){
			switch (idNivel)
			{
			
			case 1:
				Nivel1.setCurrentTileIndex(1);
				Nivel2.setCurrentTileIndex(0);				
				break;
			case 2:
				Nivel1.setCurrentTileIndex(0);
				Nivel2.setCurrentTileIndex(1);		
				break;
			
			}
		}
	}
	/**
	 * Crea un dialogo para antes de salir
	 */
	private void dialogoSalir(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);			
		alertDialogBuilder.setTitle("Salir");		
		alertDialogBuilder
		.setMessage("¿Estás seguro de que quieres salir?")
		.setCancelable(false)
		.setPositiveButton("Sí",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {			
				System.exit(0);
			}
		})
		.setNegativeButton("No",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}

}


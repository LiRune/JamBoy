package com.matimdev.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
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

import com.matimdev.base.BaseScene;
import com.matimdev.database.DataBase;
import com.matimdev.extras.LevelCompleteWindow;
import com.matimdev.extras.LevelCompleteWindow.StarsCount;
import com.matimdev.manager.ResourcesManager;
import com.matimdev.manager.SceneManager;
import com.matimdev.manager.SceneManager.SceneType;

public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener
{
	//---------------------------------------------
	// VARIABLES
	//---------------------------------------------

	private MenuScene menuChildScene;
	private MenuScene optionsChildScene;
	private MenuScene seleccionNivelChildScene;
	private MenuScene seleccionPersonajeChildScene;

	private TiledSprite star1;
	private TiledSprite star2;
	private TiledSprite star3;


	private static int idNivel = 0;
	private static int idPersonaje = 1;

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

	private Text puntuaciones;
	private Text puntosText; 
	private Text estrellas; 

	private AnimatedSprite pers1;
	private AnimatedSprite pers2;
	private AnimatedSprite pers3;

	private static boolean musica = true;
	private static boolean sonido = true;
	private float volumenMusica = engine.getMusicManager().getMasterVolume();
	private float volumenSonido = engine.getSoundManager().getMasterVolume();

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
	// METHODS FROM SUPERCLASS
	//---------------------------------------------

	@Override
	public void createScene()
	{
		createBackground();
		createMenuChildScene();
	}

	@Override
	public void onBackKeyPressed()
	{
		if(getChildScene() == optionsChildScene || getChildScene() == seleccionNivelChildScene)
		{
			setChildScene(menuChildScene);
		}
		else if(getChildScene() == seleccionPersonajeChildScene){
			setChildScene(seleccionNivelChildScene);
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

	public void onHomeKeyPressed()
	{

	}

	@Override
	public SceneType getSceneType()
	{
		return SceneType.SCENE_MENU;
	}


	@Override
	public void disposeScene()
	{
		// TODO Auto-generated method stub
	}

	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY)
	{
		switch(pMenuItem.getID())
		{
		case MENU_PLAY:
			//Load Game Scene!
			createSeleccionNivelChildScene();
			return true;

		case MENU_OPTIONS:
			//Cargar Scene de Opciones
			createOptionsChildScene();
			return true;

		case MUSICA:
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
			return true;

		case SONIDO:
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
			return true;

		case NIVEL1:
			idNivel = 1;
			puntuaciones.setText(mostrarPuntuacion(idNivel));
			//estrellas.setText(mostrarEstrellas(idNivel));
			ponerEstrellas(mostrarEstrellas(idNivel));
			return true;

		case NIVEL2:				
			idNivel = 2;
			puntuaciones.setText(mostrarPuntuacion(idNivel));
			ponerEstrellas(mostrarEstrellas(idNivel));
			//estrellas.setText(mostrarEstrellas(idNivel));
			return true;

		case JUGAR:
			if(idNivel!=0){
				if(DataBase.nivelDesbloqueado(idNivel)){
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

		default:
			return false;
		}
	}

	//---------------------------------------------
	// CLASS LOGIC
	//---------------------------------------------

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

		playMenuItem.setPosition(playMenuItem.getX(), playMenuItem.getY() - 30);
		optionsMenuItem.setPosition(optionsMenuItem.getX(), optionsMenuItem.getY() - 60);

		menuChildScene.setOnMenuItemClickListener(this);

		setChildScene(menuChildScene);
	}

	private void createOptionsChildScene()
	{
		optionsChildScene = new MenuScene(camera);
		optionsChildScene.setPosition(0, 0);

		final IMenuItem musicaMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MUSICA, getResourcesManager().options_region, getVbom()), 0.9f, 1);
		final IMenuItem sonidoMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(SONIDO, getResourcesManager().options_region, getVbom()), 0.9f, 1);

		optionsChildScene.addMenuItem(musicaMenuItem);
		optionsChildScene.addMenuItem(sonidoMenuItem);

		optionsChildScene.buildAnimations();
		optionsChildScene.setBackgroundEnabled(true);
		optionsChildScene.setBackground(new Background(Color.BLUE));

		musicaMenuItem.setPosition(musicaMenuItem.getX(), musicaMenuItem.getY() - 30);
		sonidoMenuItem.setPosition(sonidoMenuItem.getX(), sonidoMenuItem.getY() - 60);

		optionsChildScene.setOnMenuItemClickListener(this);

		setChildScene(optionsChildScene);
	}

	private void createSeleccionNivelChildScene()
	{
		seleccionNivelChildScene = new MenuScene(camera);
		seleccionNivelChildScene.setPosition(0, 0);

		System.out.println(getChildScene()); 



		//BOTONES NIVELES
		final IMenuItem Nivel1 = new ScaleMenuItemDecorator(new SpriteMenuItem(NIVEL1, getResourcesManager().options_region, getVbom()), 0.9f, 1);
		final IMenuItem Nivel2 = new ScaleMenuItemDecorator(new SpriteMenuItem(NIVEL2, getResourcesManager().play_region, getVbom()),0.9f, 1);
		final IMenuItem personajeMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(SELECCION_PERSONAJE, getResourcesManager().derecha_region, getVbom()), 0.9f, 1);

		//BOTON PLAY
		final IMenuItem Jugar = new ScaleMenuItemDecorator(new SpriteMenuItem(JUGAR, getResourcesManager().derecha_region, getVbom()), 0.9f, 1);

		//TEXTO PUNTUACIONES
		puntuaciones = new Text(0, 0, getResourcesManager().font, "01234567892", getVbom());
		puntosText = new Text(0, 0, getResourcesManager().font, "Puntos: ", new TextOptions(HorizontalAlign.RIGHT), getVbom());
		puntosText.setText("Puntos: ");


		if(idNivel!=0){
			puntuaciones.setText(mostrarPuntuacion(idNivel));
		}else{
			puntuaciones.setText("0");
		}

		//ESTRELLAS

		/*estrellas = new Text(0, 0, getResourcesManager().font, "01234567892", getVbom());
		if(idNivel!=0){
			estrellas.setText(mostrarEstrellas(idNivel));
		}else{
			estrellas.setText("0");
		}*/


		//Sprites de las estrellas		
		star1 = new TiledSprite(30, 450, ResourcesManager.getInstance().numeroEstrellas, vbom);
		star2 = new TiledSprite(110, 450, ResourcesManager.getInstance().numeroEstrellas, vbom);
		star3 = new TiledSprite(190, 450, ResourcesManager.getInstance().numeroEstrellas, vbom);

		//Como default ponemos las estrellas grises
		star1.setCurrentTileIndex(1);
		star2.setCurrentTileIndex(1);
		star3.setCurrentTileIndex(1);

		seleccionNivelChildScene.attachChild(star1);
		seleccionNivelChildScene.attachChild(star2);
		seleccionNivelChildScene.attachChild(star3);


		seleccionNivelChildScene.addMenuItem(Nivel1);
		seleccionNivelChildScene.addMenuItem(Nivel2);
		seleccionNivelChildScene.addMenuItem(Jugar);
		seleccionNivelChildScene.addMenuItem(personajeMenuItem);
		seleccionNivelChildScene.attachChild(puntuaciones);
		seleccionNivelChildScene.attachChild(puntosText);
		//seleccionNivelChildScene.attachChild(estrellas);

		seleccionNivelChildScene.buildAnimations();
		seleccionNivelChildScene.setBackgroundEnabled(true);
		seleccionNivelChildScene.setBackground(new Background(Color.BLUE));

		Nivel1.setPosition(250, 350);
		Nivel2.setPosition(Nivel1.getX(), Nivel1.getY() - 120);
		personajeMenuItem.setPosition(750, 350);

		Jugar.setPosition(750, 50);
		puntosText.setPosition(500, 450);
		puntuaciones.setPosition(puntosText.getX() + 200, puntosText.getY());			
		//estrellas.setPosition(20, 450);

		seleccionNivelChildScene.setOnMenuItemClickListener(this);

		setChildScene(seleccionNivelChildScene);
	}

	private void createSeleccionPersonajeChildScene()
	{

		seleccionPersonajeChildScene = new MenuScene(camera);
		seleccionPersonajeChildScene.setPosition(0, 0);

		System.out.println(getChildScene()); 

		final IMenuItem personaje1MenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(PERSONAJE1, getResourcesManager().personaje1_region, getVbom()), 0.9f, 1);
		final IMenuItem personaje2MenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(PERSONAJE2, getResourcesManager().personaje2_region, getVbom()), 0.9f, 1);
		final IMenuItem personaje3MenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(PERSONAJE3, getResourcesManager().personaje3_region, getVbom()), 0.9f, 1);

		pers1 = new AnimatedSprite(400, 300, getResourcesManager().personajeSelec1_region, vbom);
		pers2 = new AnimatedSprite(400, 300, getResourcesManager().personajeSelec2_region, vbom);
		pers3 = new AnimatedSprite(400, 300, getResourcesManager().personajeSelec3_region, vbom);

		pers1.setVisible(false);
		pers2.setVisible(false);
		pers3.setVisible(false);


		seleccionPersonajeChildScene.addMenuItem(personaje1MenuItem);
		seleccionPersonajeChildScene.addMenuItem(personaje2MenuItem);
		seleccionPersonajeChildScene.addMenuItem(personaje3MenuItem);
		seleccionPersonajeChildScene.attachChild(pers1);
		seleccionPersonajeChildScene.attachChild(pers2);
		seleccionPersonajeChildScene.attachChild(pers3);

		seleccionPersonajeChildScene.buildAnimations();
		seleccionPersonajeChildScene.setBackgroundEnabled(true);
		seleccionPersonajeChildScene.setBackground(new Background(Color.RED));

		personaje1MenuItem.setPosition(450, 100);
		personaje2MenuItem.setPosition(450, 40);
		personaje2MenuItem.setPosition(450, 40);

		seleccionPersonajeChildScene.setOnMenuItemClickListener(this);

		setChildScene(seleccionPersonajeChildScene);
	}

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

	private void dialogoSalir(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);			
		alertDialogBuilder.setTitle("¿Estás seguro de que quieres salir?");		
		alertDialogBuilder
		//.setMessage("Click yes to exit!")
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


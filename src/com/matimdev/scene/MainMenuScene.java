package com.matimdev.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.adt.color.Color;

import com.matimdev.base.BaseScene;
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
	
	private static int idNivel;
	
	private final int MENU_PLAY = 0;
	private final int MENU_OPTIONS = 1;
	private final int MUSICA = 2;
	private final int NIVEL1 = 3;
	private final int NIVEL2 = 4;
	
	private static boolean musica = true;
	private float volume = engine.getMusicManager().getMasterVolume();
	
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
        else
        {
        	System.exit(0);
        }
	}
	
	public void onHomeKeyPressed()
	{
		if(getResourcesManager().music != null)
		{
			getResourcesManager().music.stop();
		}
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
					engine.getMusicManager().setMasterVolume(volume);
				}
				return true;
			case NIVEL1:
				idNivel = 1;
				SceneManager.getInstance().loadGameScene(engine);
				return true;
			case NIVEL2:
				idNivel = 2;
				SceneManager.getInstance().loadGameScene(engine);
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
		
		final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, getResourcesManager().play_region, getVbom()), 1.2f, 1);
		final IMenuItem optionsMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_OPTIONS, getResourcesManager().options_region, getVbom()), 1.2f, 1);
		
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
		
		final IMenuItem optionsMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MUSICA, getResourcesManager().options_region, getVbom()), 1.2f, 1);
		
		optionsChildScene.addMenuItem(optionsMenuItem);
		
		optionsChildScene.buildAnimations();
		optionsChildScene.setBackgroundEnabled(true);
		optionsChildScene.setBackground(new Background(Color.BLUE));
		
		optionsMenuItem.setPosition(optionsMenuItem.getX(), optionsMenuItem.getY() - 60);
		
		optionsChildScene.setOnMenuItemClickListener(this);
		
		setChildScene(optionsChildScene);
	}
	
	private void createSeleccionNivelChildScene()
	{
		seleccionNivelChildScene = new MenuScene(camera);
		seleccionNivelChildScene.setPosition(0, 0);
		
		final IMenuItem Nivel1 = new ScaleMenuItemDecorator(new SpriteMenuItem(NIVEL1, getResourcesManager().options_region, getVbom()), 1.2f, 1);
		final IMenuItem Nivel2 = new ScaleMenuItemDecorator(new SpriteMenuItem(NIVEL2, getResourcesManager().play_region, getVbom()), 1.2f, 1);
		
		seleccionNivelChildScene.addMenuItem(Nivel1);
		seleccionNivelChildScene.addMenuItem(Nivel2);
		
		seleccionNivelChildScene.buildAnimations();
		seleccionNivelChildScene.setBackgroundEnabled(false);
		
		Nivel1.setPosition(Nivel1.getX(), Nivel1.getY() - 30);
		Nivel2.setPosition(Nivel2.getX(), Nivel2.getY() - 60);
		
		seleccionNivelChildScene.setOnMenuItemClickListener(this);
		
		setChildScene(seleccionNivelChildScene);
	}
}
package com.holycow.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import com.holycow.base.BaseScene;
import com.holycow.manager.SceneManager.SceneType;

/**
 * Clase que crea las escena Splash
 * 
 * @author Samir El Aoufi
 * @author Juan José Cillero
 * @author Rubén Díaz
 *
 */


public class SplashScene extends BaseScene
{
	private Sprite splash;
	
	/**
	 * Crea la escena de Splash
	 */
	@Override
	public void createScene()
	{
		splash = new Sprite(0, 0, getResourcesManager().splash_region, getVbom())
    	{
    		@Override
            protected void preDraw(GLState pGLState, Camera pCamera) 
    		{
                super.preDraw(pGLState, pCamera);
                pGLState.enableDither();
            }
    	};
    	
    	splash.setScale(1.5f);
    	splash.setPosition(400, 240);
    	attachChild(splash);
	}

	/**
	 * Accion que realiza al pulsar el boton de atras del movil
	 */
	@Override
	public void onBackKeyPressed()
	{
		return;
	}

	@Override
	public SceneType getSceneType()
	{
		return SceneType.SCENE_SPLASH;
	}

	/**
	 * Eliminacion de la escena
	 */
	@Override
	public void disposeScene()
	{
		splash.detachSelf();
		splash.dispose();
		this.detachSelf();
		this.dispose();
	}

	/**
	 * Accion que realiza al pulsar el boton de Home del movil
	 */
	@Override
	public void onHomeKeyPressed()
	{
		return;
	}
}
package com.holycow.scene;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import com.holycow.base.BaseScene;
import com.holycow.manager.SceneManager.SceneType;

/**Clase que crea la escena Cargando...
 * @author Holy Cow
 *
 */


public class LoadingScene extends BaseScene
{
	private static Text cargandoText;
	
	
	public static Text getCargandoText() {
		return cargandoText;
	}

	public void setCargandoText(Text cargandoText) {
		this.cargandoText = cargandoText;
	}

	
	/**
	 * Crea la escena Cargando... anadiendo el texto centrado y colo de fondo blanco
	 */
	@Override
	public void createScene()
	{
		
		cargandoText = new Text(400, 240, getResourcesManager().font, "Cargando...", getVbom());		
		setBackground(new Background(Color.WHITE));
		attachChild(cargandoText);
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
		return SceneType.SCENE_LOADING;
	}

	/**
	 * Eliminacion de la escena
	 */
	@Override
	public void disposeScene()
	{

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
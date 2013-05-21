package com.holycow.scene;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import com.holycow.base.BaseScene;
import com.holycow.manager.SceneManager.SceneType;

public class LoadingScene extends BaseScene
{
	private static Text cargandoText;
	
	public static Text getCargandoText() {
		return cargandoText;
	}

	public void setCargandoText(Text cargandoText) {
		this.cargandoText = cargandoText;
	}

	@Override
	public void createScene()
	{
		
		cargandoText = new Text(400, 240, getResourcesManager().font, "Cargando...", getVbom());		
		setBackground(new Background(Color.WHITE));
		attachChild(cargandoText);
	}

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

	@Override
	public void disposeScene()
	{

	}

	@Override
	public void onHomeKeyPressed() 
	{
		return;
	}
}
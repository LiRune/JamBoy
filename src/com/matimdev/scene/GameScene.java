package com.matimdev.scene;

import java.io.IOException;

import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.input.touch.controller.MultiTouchController;
import org.andengine.util.SAXUtils;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.andengine.util.level.simple.SimpleLevelEntityLoaderData;
import org.andengine.util.level.simple.SimpleLevelLoader;
import org.xml.sax.Attributes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.matimdev.GameActivity;
import com.matimdev.base.BaseScene;
import com.matimdev.database.DataBase;
import com.matimdev.extras.LevelCompleteWindow;
import com.matimdev.extras.LevelCompleteWindow.StarsCount;
import com.matimdev.manager.ResourcesManager;
import com.matimdev.manager.SceneManager;
import com.matimdev.manager.SceneManager.SceneType;
import com.matimdev.object.Bala;
import com.matimdev.object.Enemigo;
import com.matimdev.object.Player;


public class GameScene extends BaseScene implements IOnSceneTouchListener
{
	private int score = 0;
	private int health = 3;
	private int tiempo = 20;
	private boolean isTouchedFlag = false;
	private HUD gameHUD;
	private Text scoreText;
	private Text faltankeysText;
	private Text timeText;
	private Sprite heart1;
	private Sprite heart2;
	private Sprite heart3;
	private Bala bala;
	private float balaX;
	private PhysicsWorld physicsWorld;
	private LevelCompleteWindow levelCompleteWindow;

	private boolean moverPausa=true;


	private static final String TAG_ENTITY = "entity";
	private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
	private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
	private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";

	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1 = "platform1";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2 = "platform2";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3 = "platform3";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN = "coin";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "player";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LEVEL_COMPLETE = "levelComplete";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ENEMY = "enemy";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_KEY = "key";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_HEART = "heart";


	private Player player;
	private Enemigo enemigo;
	private int vidaEnemigo;

	private Text gameOverText;


	private boolean gameOverDisplayed = false;

	private boolean firstTouch = false;

	private boolean finish = false;

	public ButtonSprite volverMenu;
	public ButtonSprite restartJuego;
	public ButtonSprite pausar;
	public ButtonSprite reanudar;
	public Sprite left;
	public Sprite right;
	public ButtonSprite jump;
	public ButtonSprite disp;

	boolean PAUSED = false;

	private int estrellas;

	private TimerHandler temporizador;

	private int key = 0;

	private int pausaBackKey = 0;

	private Sprite reloj;

	public PhysicsWorld getPhysicsWorld() {
		return physicsWorld;
	}

	public void setPhysicsWorld(PhysicsWorld physicsWorld) {
		this.physicsWorld = physicsWorld;
	}

	@Override
	public void createScene()
	{
		createBackground(MainMenuScene.getIdNivel());
		createHUD();
		createPhysics();
		loadLevel(MainMenuScene.getIdNivel());
		createGameOverText();
		//createControllers();
		crearTemporizador();

		levelCompleteWindow = new LevelCompleteWindow(getVbom());

		//setOnSceneTouchListener(this); 
	}

	@Override
	public void onBackKeyPressed()
	{
		pausaBackKey++;

		if(pausaBackKey==1){
			pausa();
		}else if (pausaBackKey==2){
			SceneManager.getInstance().loadMenuScene(engine);
		}
	}

	@Override
	public SceneType getSceneType()
	{
		return SceneType.SCENE_GAME;
	}

	@Override
	public void disposeScene()
	{
		camera.setHUD(null);
		camera.setChaseEntity(null); //TODO
		camera.setCenter(400, 240);

		// TODO code responsible for disposing scene
		// removing all game scene objects.
	}

	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
	{
		/*if (pSceneTouchEvent.isActionDown())
		{
			if (!firstTouch)
			{
				player.setRunning();
				firstTouch = true;
			}
			else
			{
				player.jump();
			}
		}*/
		return false;
	}

	public final void onManagedUpdate(final float pSecondsElapsed) {
		if(PAUSED){
			// if need pause pass 0
			super.onManagedUpdate(0f);
		}else{
			super.onManagedUpdate(pSecondsElapsed);
		}
	}

	private void loadLevel(final int levelID)
	{
		final SimpleLevelLoader levelLoader = new SimpleLevelLoader(getVbom());

		final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.01f, 0.5f);

		levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(LevelConstants.TAG_LEVEL)
				{
			public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException 
			{
				final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
				final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);

				camera.setBounds(0, 0, width, height); // here we set camera bounds
				camera.setBoundsEnabled(true);

				return GameScene.this;
			}
				});

		levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(TAG_ENTITY)
				{
			public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException
			{
				final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X);
				final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y);
				final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);

				final Sprite levelObject;


				if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1))
				{
					levelObject = new Sprite(x, y, getResourcesManager().platform1_region, getVbom());
					PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF).setUserData("platform1");
				} 

				else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2))
				{
					levelObject = new Sprite(x, y, getResourcesManager().platform2_region, getVbom());
					final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
					body.setUserData("platform2");
					physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
				}

				else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3))
				{
					levelObject = new Sprite(x, y, getResourcesManager().platform3_region, getVbom());
					final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
					body.setUserData("platform3");
					physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
				}

				else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN))
				{
					levelObject = new Sprite(x, y, getResourcesManager().coin_region, getVbom())					
					{
						@Override
						protected void onManagedUpdate(float pSecondsElapsed) 
						{
							super.onManagedUpdate(pSecondsElapsed);

							if (player.collidesWith(this))
							{
								addToScore(50);
								this.setVisible(false);
								this.setIgnoreUpdate(true);
							}
						}
					};

					levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1f)));
				}	

				else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_HEART))
				{
					levelObject = new Sprite(x, y, getResourcesManager().heart_region, getVbom())					
					{
						@Override
						protected void onManagedUpdate(float pSecondsElapsed) 
						{
							super.onManagedUpdate(pSecondsElapsed);

							if (player.collidesWith(this))
							{

								if(heart3.isVisible()==false && heart2.isVisible()==true){
									health++;
									heart3.setVisible(true);									
								}
								if(heart3.isVisible()==false && heart2.isVisible()==false){
									health++;
									heart2.setVisible(true);									
								}

								this.setVisible(false);
								this.setIgnoreUpdate(true);

							}
						}
					};

					levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1f)));
				}	

				else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_KEY))
				{
					levelObject = new Sprite(x, y, getResourcesManager().llave_region, getVbom())
					{
						@Override
						protected void onManagedUpdate(float pSecondsElapsed) 
						{
							super.onManagedUpdate(pSecondsElapsed);

							if (player.collidesWith(this))
							{
								addToScore(100);								
								key++;
								this.setVisible(false);
								this.setIgnoreUpdate(true);
								ResourcesManager.coger_llave.play();										
							}
						}
					};

					levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
				}

				else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER))
				{
					player = new Player(x, y, vbom, camera, physicsWorld)
					{
						@Override
						public void onDie()
						{
							if (!gameOverDisplayed)
							{
								pantallaGameOver();
								ResourcesManager.grito.play();
							}
						}
					};

					levelObject = player;
				}

				else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ENEMY))
				{
					enemigo = new Enemigo(x, y, vbom, camera, physicsWorld, 1)
					{
						@Override
						public void onDie()
						{
							enemigo.getBody().setActive(false);
							enemigo.detachSelf();
							enemigo.setVisible(false);
						}
					};

					physicsWorld.registerPhysicsConnector(new PhysicsConnector(enemigo, enemigo.getBody(), true, false)
					{
						@Override
						public void onUpdate(float pSecondsElapsed)
						{
							super.onUpdate(pSecondsElapsed);
							camera.onUpdate(0.1f);
							enemigo.seguirJugador(camera, player);
						}
					});

					levelObject= enemigo;
				}

				else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LEVEL_COMPLETE))
				{
					levelObject = new Sprite(x, y, getResourcesManager().complete_stars_region, getVbom())
					{
						@Override
						protected void onManagedUpdate(float pSecondsElapsed) 
						{
							super.onManagedUpdate(pSecondsElapsed);

							if (player.collidesWith(this))
							{
								if(key==3){

									if(MainMenuScene.getIdNivel()==1){
										if(score>=300 && score<400){
											levelCompleteWindow.display(StarsCount.ONE, GameScene.this, camera);
											estrellas=1;
										}
										if(score>=400 && score<500){
											levelCompleteWindow.display(StarsCount.TWO, GameScene.this, camera);
											estrellas=2;
										}
										if(score>=500){
											levelCompleteWindow.display(StarsCount.THREE, GameScene.this, camera);
											estrellas=3;
										}
									}
									else if(MainMenuScene.getIdNivel()==2){
										if(score>=300 && score<400){
											levelCompleteWindow.display(StarsCount.ONE, GameScene.this, camera);
											estrellas=1;
										}
										if(score>=400 && score<500){
											levelCompleteWindow.display(StarsCount.TWO, GameScene.this, camera);
											estrellas=2;
										}
										if(score>=500){
											levelCompleteWindow.display(StarsCount.THREE, GameScene.this, camera);
											estrellas=3;
										}
									}

									pantallaLevelComplete();

									guardarPuntuacion();

									if (getResourcesManager().music != null)
									{
										if (getResourcesManager().music.isPlaying())
										{
											getResourcesManager().music.stop();
										}
									}

									try {
										getResourcesManager().music = MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity, "mfx/victory.mp3");
										getResourcesManager().music.play();
									} catch (IllegalStateException e) {
										e.printStackTrace();
									} catch (IOException e) {
										e.printStackTrace();
									}

									this.setVisible(false);
									this.setIgnoreUpdate(true);
									player.setVisible(false);
									finish = true;
								}else{
									if(key==2){
										faltankeysText.setText("Falta 1 llave");
									}else if(key==1){
										faltankeysText.setText("Faltan 2 llaves");
									}else if(key==0){
										faltankeysText.setText("Faltan 3 llaves");
									}
									faltankeysText.setVisible(true);
								}
							}else{
								faltankeysText.setVisible(false);
							}
						}
					};
					levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
				}
				else
				{
					throw new IllegalArgumentException();
				}

				levelObject.setCullingEnabled(true);

				return levelObject;
			}
				});

		levelLoader.loadLevelFromAsset(activity.getAssets(), "level/" + levelID + ".lvl");
	}

	private void createGameOverText()
	{
		gameOverText = new Text(0, 0, getResourcesManager().font, "Game Over!", getVbom());
	}

	private void displayGameOverText()
	{
		gameOverText.detachSelf();
		camera.setChaseEntity(null);
		gameOverText.setPosition(camera.getCenterX(), camera.getCenterY());
		attachChild(gameOverText);
		gameOverDisplayed = true;
	}

	private void createHUD()
	{
		gameHUD = new HUD();

		scoreText = new Text(70, 440, getResourcesManager().font, "0123456789", new TextOptions(HorizontalAlign.LEFT), getVbom());
		//scoreText.setAnchorCenter(0, 0);	
		scoreText.setText("0");
		gameHUD.attachChild(scoreText);

		timeText = new Text(390, 440, getResourcesManager().font, "0123456789", new TextOptions(HorizontalAlign.CENTER), getVbom());
		//timeText.setAnchorCenter(0,  0);
		timeText.setText("20");

		reloj = new Sprite(330, 450, getResourcesManager().reloj_region , getVbom());

		faltankeysText = new Text(390, 300, getResourcesManager().font, "Faltan llaves", new TextOptions(HorizontalAlign.CENTER), getVbom());
		//timeText.setAnchorCenter(0,  0);
		faltankeysText.setText("Faltan llaves");
		faltankeysText.setVisible(false);
		gameHUD.attachChild(faltankeysText);
		gameHUD.attachChild(reloj);
		gameHUD.attachChild(timeText);

		setTouchAreaBindingOnActionDownEnabled(true);
		setTouchAreaBindingOnActionMoveEnabled(true);

		left = new Sprite(50, 50, getResourcesManager().izquierda_region, vbom){
			@Override
			public boolean onAreaTouched(final TouchEvent pAreaTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(moverPausa){
					switch(pAreaTouchEvent.getAction()) {
					case TouchEvent.ACTION_DOWN:  

						isTouchedFlag = true;
						player.runLeft();

						break;

					case TouchEvent.ACTION_UP:  
						isTouchedFlag = false;
						player.stop();

						break;


					}
				}
				return true;
			}


		};

		this.registerTouchArea(left);

		right = new Sprite(180, 50, getResourcesManager().derecha_region, vbom){

			@Override
			public boolean onAreaTouched(final TouchEvent pAreaTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(moverPausa){
					switch(pAreaTouchEvent.getAction()) {
					case TouchEvent.ACTION_DOWN:

						isTouchedFlag = true;
						player.runRight();

						break;

					case TouchEvent.ACTION_UP:  
						isTouchedFlag = false;
						player.stop();
						break;


					}
				}
				return true;
			}

		};

		this.registerTouchArea(right);

		volverMenu = new ButtonSprite(500, 50, getResourcesManager().reanudar_region , getVbom(), new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) { 	 
				SceneManager.getInstance().loadMenuScene(engine);
			}
		});

		volverMenu.setVisible(false);
		volverMenu.setEnabled(false);

		restartJuego = new ButtonSprite(300, 50, getResourcesManager().reanudar_region , getVbom(), new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) { 	 
				SceneManager.getInstance().loadGameScene(engine);
			}
		});

		restartJuego.setVisible(false);
		restartJuego.setEnabled(false);



		disp = new ButtonSprite(620, 50, getResourcesManager().saltar_region , getVbom(), new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(player.isFlippedHorizontal())
				{
					balaX = player.getX() - 30;
				}
				else
				{
					balaX = player.getX() + 30;
				}

				bala = new Bala(balaX, player.getY(), vbom, camera, physicsWorld);

				player.disparar(player.getX(), player.getY(), engine, bala);
			}
		});



		jump = new ButtonSprite(750, 50, getResourcesManager().saltar_region , getVbom(), new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) { 	 
				player.jump();
			}
		});

		pausar = new ButtonSprite(750, 380, getResourcesManager().pausa_region , getVbom(), new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) { 	 
				pausa();
			}
		});

		reanudar = new ButtonSprite(750, 380, getResourcesManager().reanudar_region , getVbom(), new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) { 	 
				PAUSED=false;

				pausar.setVisible(true);
				pausar.setEnabled(true);
				reanudar.setVisible(false);
				reanudar.setEnabled(false);
				left.setVisible(true);
				right.setVisible(true);
				jump.setVisible(true);		
				jump.setEnabled(true);

				disp.setVisible(true);
				disp.setEnabled(true );


				moverPausa=true;

				volverMenu.setVisible(false);
				volverMenu.setEnabled(false);

				restartJuego.setVisible(false);
				restartJuego.setEnabled(false);

				if (getResourcesManager().music != null)
				{
					getResourcesManager().music.resume();
				}
			}
		});
		reanudar.setVisible(false);
		reanudar.setEnabled(false);

		heart1 = new Sprite(660, 450, getResourcesManager().heart_region, getVbom());
		heart2 = new Sprite(710, 450, getResourcesManager().heart_region, getVbom());
		heart3 = new Sprite(760, 450, getResourcesManager().heart_region, getVbom());



		gameHUD.registerTouchArea(reanudar);
		gameHUD.registerTouchArea(pausar);
		gameHUD.registerTouchArea(left);
		gameHUD.registerTouchArea(right);
		gameHUD.registerTouchArea(jump);
		gameHUD.registerTouchArea(volverMenu);
		gameHUD.registerTouchArea(restartJuego);
		gameHUD.registerTouchArea(disp);
		gameHUD.attachChild(left);
		gameHUD.attachChild(right);
		gameHUD.attachChild(jump);
		gameHUD.attachChild(disp);
		gameHUD.attachChild(volverMenu);
		gameHUD.attachChild(restartJuego);
		gameHUD.attachChild(heart1);
		gameHUD.attachChild(heart2);
		gameHUD.attachChild(heart3);
		gameHUD.attachChild(reanudar);	
		gameHUD.attachChild(pausar);

		camera.setHUD(gameHUD);
	}

	private void createBackground(int level)
	{
		//setBackground(new Background(Color.GREEN));

		/*
		Sprite mBackground = new Sprite(0, 0, getResourcesManager().fondo_region, vbom);

		//Attach the sprite to the scene. I use getFirstChild() so that I can ensure that the background sprite is the "bottom" child
		this.attachChild(mBackground);*/


		ParallaxBackground background = new ParallaxBackground(0, 0, 0);

		if(level==1){
			background.attachParallaxEntity(new ParallaxEntity(0, new Sprite(400, 240,resourcesManager.fondo_region, vbom)));
		}else{
			background.attachParallaxEntity(new ParallaxEntity(0, new Sprite(400, 240,resourcesManager.fondo2_region, vbom)));
		}

		this.setBackground(background);






	}

	private void addToScore(int i)
	{
		score += i;
		scoreText.setText("" + score);
	}

	private void createPhysics()
	{
		physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -17), false); 
		physicsWorld.setContactListener(contactListener());
		registerUpdateHandler(physicsWorld);
	}

	private void crearTemporizador()
	{
		this.engine.registerUpdateHandler(temporizador = new TimerHandler(1.0f, true, new ITimerCallback()
		{                      
			public void onTimePassed(final TimerHandler pTimerHandler)
			{
				if(tiempo > 0 && !PAUSED) {
					tiempo--;
					timeText.setText("" + tiempo);
				}
				else if(tiempo <= 0){
					pantallaGameOver();
				}
			}
		}));
	}

	private void pantallaGameOver() {
		displayGameOverText();
		player.setVisible(false);
		player.getBody().setActive(false);
		left.setVisible(false);
		right.setVisible(false);
		jump.setVisible(false);
		volverMenu.setVisible(true);
		restartJuego.setVisible(true);
		volverMenu.setEnabled(true);
		restartJuego.setEnabled(true);
		disp.setVisible(false);
		disp.setEnabled(false);
		engine.unregisterUpdateHandler(temporizador);
		pausar.setVisible(false);
		pausar.setEnabled(false);
		reanudar.setVisible(false);
		reanudar.setEnabled(false);

	}

	private void pantallaLevelComplete() {
		engine.unregisterUpdateHandler(temporizador);
		heart1.setVisible(false);
		heart2.setVisible(false);
		heart3.setVisible(false);
		player.setVisible(false); 
		player.getBody().setActive(false);
		left.setVisible(false);
		right.setVisible(false);
		reloj.setVisible(false);
		jump.setVisible(false);		
		jump.setEnabled(false);
		disp.setVisible(false);
		disp.setEnabled(false);
		pausar.setVisible(false);
		pausar.setEnabled(false);
		volverMenu.setVisible(true);
		restartJuego.setVisible(true);
		volverMenu.setEnabled(true);
		restartJuego.setEnabled(true);
		scoreText.setVisible(false);
		timeText.setVisible(false);
		pausar.setVisible(false);
		pausar.setEnabled(false);
		reanudar.setVisible(false);
		reanudar.setEnabled(false);
	}


	public void onHomeKeyPressed()
	{

	}

	public void pausa()
	{
		PAUSED=true;
		pausar.setVisible(false);
		pausar.setEnabled(false);
		reanudar.setVisible(true);
		reanudar.setEnabled(true);
		left.setVisible(false);
		right.setVisible(false);

		jump.setVisible(false);		
		jump.setEnabled(false);

		disp.setVisible(false);
		disp.setEnabled(false);

		volverMenu.setVisible(true);
		volverMenu.setEnabled(true);

		restartJuego.setVisible(true);
		restartJuego.setEnabled(true);

		moverPausa=false;

		if (getResourcesManager().music != null)
		{
			getResourcesManager().music.pause();
		}
	}

	// ---------------------------------------------
	// INTERNAL CLASSES
	// ---------------------------------------------

	private ContactListener contactListener()
	{
		ContactListener contactListener = new ContactListener()
		{
			private AnimatedSprite explosion;

			public void beginContact(Contact contact)
			{
				final Fixture x1 = contact.getFixtureA();
				final Fixture x2 = contact.getFixtureB();

				if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
				{
					if (x2.getBody().getUserData().equals("player"))
					{
						player.increaseFootContacts();
					}

					/*HACE QUE LAS PLATAFORMAS SE CAIGAN*/
					if (x1.getBody().getUserData().equals("platform2") && x2.getBody().getUserData().equals("player"))
					{
						engine.registerUpdateHandler(new TimerHandler(0.2f, new ITimerCallback()
						{									
							public void onTimePassed(final TimerHandler pTimerHandler)
							{
								pTimerHandler.reset();
								engine.unregisterUpdateHandler(pTimerHandler);
								x1.getBody().setType(BodyType.DynamicBody);
							}
						}));
					}

					if (x1.getBody().getUserData().equals("platform3") && x2.getBody().getUserData().equals("player"))
					{
						x1.getBody().setType(BodyType.DynamicBody);
					}

					if (x1.getBody().getUserData().equals("player") && x2.getBody().getUserData().equals("enemigo")) {
						health--;

						//Si el jugador choca con el enemigo pega un salto hacia atras
						if(player.getX() > enemigo.getX()){
							player.getBody().setLinearVelocity(new Vector2(player.getBody().getLinearVelocity().x +8, 6));
						}
						if(player.getX() < enemigo.getX()){
							player.getBody().setLinearVelocity(new Vector2(player.getBody().getLinearVelocity().x -8, 6));
						}

						if(heart3.isVisible()) {
							heart3.setVisible(false);
						}
						else if(heart2.isVisible()) {
							heart2.setVisible(false);
						}
						else if(heart1.isVisible()) {
							heart1.setVisible(false);
						}

						if(health <= 0) {
							player.onDie();						
						}
					}

					if (x1.getBody().getUserData().equals("enemigo") && x2.getBody().getUserData().equals("bala"))
					{
						vidaEnemigo = enemigo.getVida() - 1;
						enemigo.setVida(vidaEnemigo);
						
						if(vidaEnemigo == 0)
						{
							ResourcesManager.enemigo_muerte.play();
							enemigo.getBody().setActive(false);
							enemigo.detachSelf();
							enemigo.setVisible(false);							
							addToScore(50);
						}
						
						explosion = new AnimatedSprite(enemigo.getX(), enemigo.getY(), getResourcesManager().explosion_region, vbom)
						{
							@Override
							protected void onManagedUpdate(float pSecondsElapsed) 
							{
								super.onManagedUpdate(pSecondsElapsed);

								if(!explosion.isAnimationRunning())
								{
									detachChild(explosion);
									explosion.setVisible(false);
									setIgnoreUpdate(true);
								}
							}
						};

						attachChild(explosion);
						explosion.animate(100, 0);
						
						bala.getBody().setActive(false);
						bala.detachSelf();
						bala.setVisible(false);
					}
				}
			}

			public void endContact(Contact contact)
			{
				final Fixture x1 = contact.getFixtureA();
				final Fixture x2 = contact.getFixtureB();

				if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
				{
					if (x2.getBody().getUserData().equals("player"))
					{
						player.decreaseFootContacts();
					}
				}
			}

			public void preSolve(Contact contact, Manifold oldManifold)
			{
				final Fixture x1 = contact.getFixtureA();
				final Fixture x2 = contact.getFixtureB();

				if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
				{					
					if (x1.getBody().getUserData().equals("player") && x2.getBody().getUserData().equals("enemigo")) {
						contact.setEnabled(false);
					}				

					if (x1.getBody().getUserData().equals("player") && x2.getBody().getUserData().equals("bala")) {
						contact.setEnabled(false);
					}	
				}
			}

			public void postSolve(Contact contact, ContactImpulse impulse)
			{
				
			}
		};
		return contactListener;
	}

	public void guardarPuntuacion(){

		SQLiteDatabase db = getResourcesManager().getGame().getWritableDatabase();


		if(!DataBase.nivelSuperado(MainMenuScene.getIdNivel()))
		{
			int nivelDesbloqueado=MainMenuScene.getIdNivel()+1;
			db.execSQL("UPDATE Niveles SET Superado = 'true' WHERE Numero = "+MainMenuScene.getIdNivel());
			db.execSQL("UPDATE Niveles SET Desbloqueado = 'true' WHERE Numero = "+nivelDesbloqueado);
		}

		if(DataBase.compararPuntuacion(MainMenuScene.getIdNivel(), score)){
			db.execSQL("UPDATE Niveles SET Puntuacion = "+score+", Estrellas= "+estrellas+" WHERE Numero = "+MainMenuScene.getIdNivel());
		}
		db.close();


		/*SQLiteDatabase db2 = getResourcesManager().getGame().getReadableDatabase();

		Cursor c = db2.rawQuery(" SELECT Puntuacion, Estrellas FROM Niveles", null);
		if (c.moveToFirst()) {
			//Recorremos el cursor hasta que no haya más registros
			do {
				String algo = c.getString(0);
				String algo2 = c.getString(1);
				System.out.println("PUNTUACION: "+algo);				
				System.out.println("ESTRELLAS: "+algo2);

			} while(c.moveToNext());
		}

		db2.close();*/
	}
}

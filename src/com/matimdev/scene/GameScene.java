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
import com.matimdev.base.BaseScene;
import com.matimdev.database.DataBase;
import com.matimdev.extras.LevelCompleteWindow;
import com.matimdev.extras.LevelCompleteWindow.StarsCount;
import com.matimdev.manager.SceneManager;
import com.matimdev.manager.SceneManager.SceneType;
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
	private Text timeText;
	private Sprite heart1;
	private Sprite heart2;
	private Sprite heart3;
	private Sprite bala;
	private Body bala_cuerpo,enemigo_cuerpo;
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
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_IZQ = "izq";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DER = "der";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SALT = "salt";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PAUSA = "pausa";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_REANUDAR = "reanudar";

	private Player player;
	private Enemigo enemigo;

	private Text gameOverText;
	private Text faltanKeyText;

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

	private TimerHandler temporizador;

	private int key=0;

	private DataBase db;

	public PhysicsWorld getPhysicsWorld() {
		return physicsWorld;
	}

	public void setPhysicsWorld(PhysicsWorld physicsWorld) {
		this.physicsWorld = physicsWorld;
	}

	@Override
	public void createScene()
	{
		createBackground();
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
		SceneManager.getInstance().loadMenuScene(engine);
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
								addToScore(10);
								this.setVisible(false);
								this.setIgnoreUpdate(true);
							}
						}
					};
					//levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
					final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
					body.setUserData("coin");
					physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
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
							}
						}
					};
					levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
				}	


				else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER))
				{
					player = new Player(x, y, getVbom(), camera, physicsWorld)
					{
						@Override
						public void onDie()
						{
							if (!gameOverDisplayed)
							{
								pantallaGameOver();
							}
						}
					};
					levelObject = player;
				}
				
				else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ENEMY))
				{
					enemigo =new Enemigo(x,y, resourcesManager.enemy_region, vbom);
					enemigo_cuerpo = PhysicsFactory.createBoxBody(physicsWorld,enemigo, BodyType.KinematicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
					enemigo_cuerpo.setLinearVelocity(-1 * 5, 0);
					enemigo_cuerpo.setUserData("enemigo");
					final float maxMovementX = 200;
					physicsWorld.registerPhysicsConnector(new PhysicsConnector(enemigo, enemigo_cuerpo, true, false)
					{
						@Override
						public void onUpdate(float pSecondsElapsed)
						{
							super.onUpdate(pSecondsElapsed);

							if (enemigo.getX() <= x - maxMovementX)
							{
								enemigo_cuerpo.setLinearVelocity(enemigo_cuerpo.getLinearVelocity().x * -1, 0);
								enemigo.setFlippedHorizontal(false);
							}
							if (enemigo.getX() >= x + maxMovementX)
							{
								enemigo_cuerpo.setLinearVelocity(enemigo_cuerpo.getLinearVelocity().x * -1, 0);
								enemigo.setFlippedHorizontal(true);
							}
							

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

							if (player.collidesWith(this) && key==3)
							{
								levelCompleteWindow.display(StarsCount.TWO, GameScene.this, camera);
								pantallaLevelComplete();
								
								/*DataBase myDB = new DataBase(activity);
								SQLiteDatabase db = myDB.getWritableDatabase();
								db.execSQL("INSERT INTO Niveles VALUES(" + levelID + ", " + "'false', " + "'true', " + score + ")");
								db.close();*/

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

		scoreText = new Text(20, 420, getResourcesManager().font, "Puntos: 0123456789", new TextOptions(HorizontalAlign.LEFT), getVbom());
		scoreText.setAnchorCenter(0, 0);	
		scoreText.setText("Puntos: 0");
		gameHUD.attachChild(scoreText);

		timeText = new Text(20, 370, getResourcesManager().font, "Tiempo: 0123456789", new TextOptions(HorizontalAlign.LEFT), getVbom());
		timeText.setAnchorCenter(0,  0);
		timeText.setText("Tiempo: 20");
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
						player.stop2();

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
				disparar(engine, player.getX(), player.getY());
			}
		});



		jump = new ButtonSprite(750, 50, getResourcesManager().saltar_region , getVbom(), new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) { 	 
				player.jump();
			}
		});

		pausar = new ButtonSprite(750, 380, getResourcesManager().pausa_region , getVbom(), new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) { 	 
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

	private void createBackground()
	{
		//setBackground(new Background(Color.GREEN));
		/*Sprite mBackground = new Sprite(0, 0, getResourcesManager().fondo_region, vbom);

		//Attach the sprite to the scene. I use getFirstChild() so that I can ensure that the background sprite is the "bottom" child
		this.attachChild(mBackground);*/



		ParallaxBackground background = new ParallaxBackground(0, 0, 0);
		background.attachParallaxEntity(new ParallaxEntity(0, new Sprite(400, 240,resourcesManager.fondo_region, vbom)));

		this.setBackground(background);

	}

	private void addToScore(int i)
	{
		score += i;
		scoreText.setText("Puntos: " + score);
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
					timeText.setText("Tiempo: " + tiempo);
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
		if(getResourcesManager().music != null)
		{
			getResourcesManager().music.stop();
		}
	}

	// ---------------------------------------------
	// INTERNAL CLASSES
	// ---------------------------------------------

	private ContactListener contactListener()
	{
		ContactListener contactListener = new ContactListener()
		{
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
							pantallaGameOver();
						}
					}

					if (x1.getBody().getUserData().equals("enemigo") && x2.getBody().getUserData().equals("bala"))
					{
						
					
						enemigo_cuerpo.setActive(false);
						//enemigo.setVisible(false);
						enemigo.setFlippedVertical(true);
						enemigo.setFlippedHorizontal(true);
					
						bala_cuerpo.setActive(false);
						bala.setVisible(false);
						
						
						
						addToScore(50);
			           
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

			}

			public void postSolve(Contact contact, ContactImpulse impulse)
			{

			}
		};
		return contactListener;
	}

	//Metodo disparo:
	public void disparar(Engine mEngine,float xCoord, float yCoord){

		float xComp;
		//Aqui se define la potencia de disparo
		final FixtureDef bulletFixtureDef=PhysicsFactory.createFixtureDef(1, 0.5f,0.5f);
		//hay que cambiar resourcesManager.platform2_region por otra imagen para la bala
		//En que coordenadas se creara el sprite y que imagen tendra 
		this.bala=new Sprite(xCoord,yCoord+10,this.resourcesManager.bala_region, engine.getVertexBufferObjectManager());
		//Se crean las fisicas de la bala
		this.bala_cuerpo=PhysicsFactory.createBoxBody(this.physicsWorld, this.bala,BodyType.DynamicBody, bulletFixtureDef);
		//Se le añade el cuerpo ^-- cuerpo
		this.bala_cuerpo.setBullet(true);

		this.physicsWorld.registerPhysicsConnector(new PhysicsConnector(this.bala, this.bala_cuerpo,true,false));

		//Si el jugador no esta girado dispara hacia delante
		if(!player.isFlippedHorizontal()){
			xComp=xCoord+5;
		}
		else{
			//Si no, la bala obtiene velocidad negativa
			xComp=-xCoord;
			//Si esta girado el spirte tambien saldra al reves
			bala.setFlippedHorizontal(true);
		}
		//Esto es la linea vectorial que sigue la bala, en este caso sale a altura 0 y a 10 pixeles delante del jugador
		this.bala_cuerpo.setLinearVelocity(new Vector2(1*xComp,0));
		bala_cuerpo.setUserData("bala");
		//Agrego la bala a la escena:
		engine.getScene().attachChild(bala);

	}
}

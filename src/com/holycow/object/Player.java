package com.holycow.object;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.holycow.manager.ResourcesManager;

/**Clase que define las opciones de player
 * @author Samir El Aoufi
 * @author Juan Jos� Cillero
 * @author Rub�n D�az
 *
 */

public abstract class Player extends AnimatedSprite
{
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------
	
	private Body body;
    private int footContacts = 0;
    private int vida = 3;
	
	// ---------------------------------------------
	// CONSTRUCTOR
	// ---------------------------------------------
	
	public Player(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld)
	{
		super(pX, pY, ResourcesManager.getInstance().player_region, vbo);
		createPhysics(camera, physicsWorld);
		camera.setChaseEntity(this);
	}
	
	// ---------------------------------------------
	// GETTERS & SETTERS
	// ---------------------------------------------
	
	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}
	
	public int getFootContacts() {
		return footContacts;
	}

	public void setFootContacts(int footContacts) {
		this.footContacts = footContacts;
	}
	
	public int getVida() {
		return vida;
	}

	public void setVida(int vida) {
		this.vida = vida;
	}
	
	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------
	
	/**
	 * Crea las fisicas de Player
	 * @param camera
	 * @param physicsWorld
	 */
	private void createPhysics(final Camera camera, PhysicsWorld physicsWorld)
	{		
		body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

		body.setUserData("player");
		body.setFixedRotation(true);
		
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false)
		{
			@Override
	        public void onUpdate(float pSecondsElapsed)
	        {
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);
				
				if (getY() <= 0 || vida == 0)
				{					
					onDie();
				}
	        }
		});
	}
	
	/**
	 * Player avanzar hacia la izquierda
	 */
	public void runLeft(){
		if (!isFlippedHorizontal()) 
		{ 
			setFlippedHorizontal(true); 
		}
		body.setLinearVelocity(new Vector2(-5, body.getLinearVelocity().y)); 
		final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100 };
		
		animate(100);
	}
	
	
	/**
	 * Player avanzar hacia la derecha
	 */
	public void runRight(){
		if (isFlippedHorizontal()) 
		{ 
			setFlippedHorizontal(false); 
		}
		body.setLinearVelocity(new Vector2(5, body.getLinearVelocity().y)); 
		final long[] PLAYER_ANIMATE = new long[] {100,100,100 };
		
		animate(100);
	}
	
	
	/**
	 * Player se para
	 */
	public void stop(){
		if (isFlippedHorizontal()) 
		{ 
			setFlippedHorizontal(true); 
		}
		else
		{
			setFlippedHorizontal(false); 
		}
		
		body.setLinearVelocity(new Vector2(0, body.getLinearVelocity().y)); 
		final long[] PLAYER_ANIMATE = new long[] {0,0,0 };
		
		animate(PLAYER_ANIMATE, 0, 2, false);
	}
	
	/**
	 * Player salta
	 */
	public void jump()
	{
		
		if (footContacts < 1) 
		{
			return; 
		}
		else
		{
			ResourcesManager.salto.play();
		}
		body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 12)); 
	}
	
	/**
	 * Player dispara Bala
	 * @param xCoord
	 * @param yCoord
	 * @param engine
	 * @param bala
	 */
	public void disparar(float xCoord, float yCoord, Engine engine, Bala bala)
	{
		float xComp = 15;

		//Si el jugador no esta girado dispara hacia delante
		if(this.isFlippedHorizontal()){
			xComp = xComp * -1;
			bala.setFlippedHorizontal(true);
		}
		
		//Esto es la linea vectorial que sigue la bala, en este caso sale a altura 0 y a 10 pixeles delante del jugador
		bala.getBody().setLinearVelocity(new Vector2(xComp,0));
		
		//Agrego la bala a la escena:
		engine.getScene().attachChild(bala);

		ResourcesManager.disparar.play();
	}
	
	/**
	 * Incrementa el contacto del Player con el suelo
	 */
	public void increaseFootContacts()
	{
		footContacts++;
	}
	
	
	/**
	 * Decrementa el contacto del Player con el suelo
	 */
	public void decreaseFootContacts()
	{
		footContacts--;
	}
	
	public abstract void onDie();
}
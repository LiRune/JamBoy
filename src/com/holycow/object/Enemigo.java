package com.holycow.object;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.holycow.manager.ResourcesManager;

public abstract class Enemigo extends AnimatedSprite
{
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	private Body body;
	private int vida = 3;

	// ---------------------------------------------
	// CONSTRUCTOR
	// ---------------------------------------------

	public Enemigo(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld, int vida)
	{
		super(pX, pY, ResourcesManager.getInstance().enemy_region, vbo);
		this.vida = vida;
		createPhysics(camera, physicsWorld);
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
	
	public int getVida() {
		return vida;
	}
	
	public void setVida(int vida) {
		this.vida = vida;
	}

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	private void createPhysics(final Camera camera, PhysicsWorld physicsWorld)
	{		
		body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

		body.setUserData("enemigo");
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

	public void seguirJugador(final Camera camera, Player player)
	{
		//Si la distancia de enemigo esta a 200 o mas positivo
		if(this.getX() - player.getX() <= 200){
			//enemigo_cuerpo.setLinearVelocity(5 * -1, 0);									
			//Se compara a cuanto esta de cerca del enemigo, si esta a  150, empieza a correr hacia la derecha
			if (player.getX() > this.getX()+150)
			{
				this.animate(100);
				body.setLinearVelocity(5, 0);
				this.setFlippedHorizontal(false);
			}
			//Sino, obtiene velocidad negativa
			else if(player.getX() < this.getX()-150)
			{
				this.animate(100);
				body.setLinearVelocity(-5, 0);
				this.setFlippedHorizontal(true);
			}


		}
		//Si la distancia del jugador es -200 o menos
		if(this.getX() - player.getX() <= -200){
			//enemigo_cuerpo.setLinearVelocity(5 * -1, 0);
			// Si el jugador esta a 150 mas se gira el enemigo
			if (player.getX() > this.getX()+150)
			{
				this.animate(100);
				body.setLinearVelocity(5, 0);
				this.setFlippedHorizontal(false);
			}

			//Sino, obtiene velocidad negativa
			else if(player.getX() < this.getX()-150)
			{
				this.animate(100);
				body.setLinearVelocity(-5, 0);
				this.setFlippedHorizontal(true);
			}
		}
	}


	public abstract void onDie();
}

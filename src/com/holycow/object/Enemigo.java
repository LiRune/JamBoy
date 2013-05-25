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

/**
 * Clase que define las características de Enemigo
 * @author Samir El Aoufi
 * @author Juan José Cillero
 * @author Rubén Díaz
 *
 */


public abstract class Enemigo extends AnimatedSprite
{
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	private Enemigo enemigo;
	private Body body;
	private int vida;
	private Player player;

	// ---------------------------------------------
	// CONSTRUCTOR
	// ---------------------------------------------

	public Enemigo(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld, int vida, Player player)
	{
		super(pX, pY, ResourcesManager.getInstance().enemy_region, vbo);
		this.vida = vida;
		this.player = player;
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
	// LÓGICA DE CLASE
	// ---------------------------------------------
	
	/**
	 * Crea las fisicas del enemigo
	 * @param camera
	 * @param physicsWorld
	 */
	private void createPhysics(final Camera camera, PhysicsWorld physicsWorld)
	{
		enemigo = this;
		body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
		//body.setUserData("enemigo");
		body.setFixedRotation(true);

		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false)
		{
			@Override
			public void onUpdate(float pSecondsElapsed)
			{
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);

				// Si el enemigo cae por debajo del límite del nivel o su vida llega a 0 muere
				if (getY() <= 0 || vida == 0)
				{					
					onDie();
				}
				
				enemigo.seguirJugador(camera, player);
			}
		});
	}

	
	/**
	 * Para seguir al Player cuando se acerque a cierta distancia
	 * @param camera
	 * @param player
	 */
	public void seguirJugador(final Camera camera, Player player)
	{
		//Si la distancia de enemigo esta a 200 o mas positivo
		if(this.getX() - player.getX() <= 200){							
			//Se compara a cuanto esta de cerca del enemigo, si esta a  150, empieza a correr hacia la derecha
			if (player.getX() > this.getX()+190)
			{
				this.animate(100);
				body.setLinearVelocity(5, 0);
				this.setFlippedHorizontal(false);
			}
			//Sino, obtiene velocidad negativa
			else if(player.getX() < this.getX()-190)
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
			if (player.getX() > this.getX()+190)
			{
				this.animate(100);
				body.setLinearVelocity(5, 0);
				this.setFlippedHorizontal(false);
			}

			//Sino, obtiene velocidad negativa
			else if(player.getX() < this.getX()-190)
			{
				this.animate(100);
				body.setLinearVelocity(-5, 0);
				this.setFlippedHorizontal(true);
			}
			
		}
	}


	public abstract void onDie();
}

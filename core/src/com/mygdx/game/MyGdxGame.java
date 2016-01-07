package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.mygdx.game.bodies.*;
import com.mygdx.game.staticbodies.GameEdge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyGdxGame extends ApplicationAdapter implements InputProcessor, ContactListener{
	SpriteBatch batch;
	Sprite sprite,sprite2;

	World world;
	List<IGameBody> gameBodies = new ArrayList();
	List<IGameBody> controlableGameBodies = new ArrayList();
	Map<String, IGameBody> allBodies = new HashMap();
	int selectedBody=0;
	Body bodyEdgeScreen;


	Matrix4 debugMatrix;
	OrthographicCamera camera;
	Box2DDebugRenderer debugRenderer;


	final float PIXELS_TO_METERS = 100f;


	final short PHYSICS_ENTITY = 0x1;    // 0001
	final short WORLD_ENTITY = 0x1 << 1; // 0010 or 0x2 in hex

	private synchronized void addLearner(float x, float y) {
		IGameBody gameBody = BodyFactory.create(Learner.class, world, (int)x,(int)y, PHYSICS_ENTITY, (short) (WORLD_ENTITY|PHYSICS_ENTITY));
		gameBodies.add(gameBody );
		allBodies.put(gameBody.getId(), gameBody);
	}

	@Override
	public void create() {
		batch = new SpriteBatch();


		world = new World(new Vector2(0, -0.0f),true);
		controlableGameBodies.add( BodyFactory.create(Platform.class, world, 10, 10, PHYSICS_ENTITY, (short) (WORLD_ENTITY|PHYSICS_ENTITY)) );
		controlableGameBodies.add( BodyFactory.create(Cube.class, world, 20,10, PHYSICS_ENTITY, (short) (WORLD_ENTITY|PHYSICS_ENTITY)) );
		addLearner(5, 5);
		addLearner(15, 7);


		// Now the physics body of the bottom edge of the screen
		float w = Gdx.graphics.getWidth()/PIXELS_TO_METERS;
		float h = Gdx.graphics.getHeight()/PIXELS_TO_METERS;

		GameEdge gameEdgeBottom = GameEdge.create(world, -w/2, -h/2, w/2, -h/2, WORLD_ENTITY, PHYSICS_ENTITY);
		GameEdge gameEdgeRight = GameEdge.create(world, -w/2, h/2, -w/2, -h/2, WORLD_ENTITY, PHYSICS_ENTITY);
		GameEdge gameEdgeLeft = GameEdge.create(world, w/2, h/2, w/2, -h/2, WORLD_ENTITY, PHYSICS_ENTITY);
		GameEdge gameEdgeTop = GameEdge.create(world, -w/2, h/2, w/2, h/2, WORLD_ENTITY, PHYSICS_ENTITY);

		Gdx.input.setInputProcessor(this);
		world.setContactListener(this);

		debugRenderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.
				getHeight());

		debugMatrix=camera.combined.cpy();

//BoxObjectManager.BOX_TO_WORLD = 100f
//Scale it by 100 as our box physics bodies are scaled down by 100
		debugMatrix.scale(90.0f, 90.0f, 1f);
	}

	@Override
	public void render() {
		camera.update();

		// Step the physics simulation forward at a rate of 60hz
		world.step(1f/60f, 6, 2);

		for (IGameBody gameBody : this.gameBodies) {
			gameBody.updateSprite();
		}
		for (IGameBody gameBody : this.controlableGameBodies) {
			gameBody.updateSprite();
		}


		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		//debugRenderer.render(world, debugMatrix);

		for (IGameBody gameBody : this.gameBodies) {
			drawSpirte(batch, gameBody.getSprite());
		}
		for (IGameBody gameBody : this.controlableGameBodies) {
			drawSpirte(batch, ((ControllableGameBody)gameBody).getSprite());
		}

		batch.end();
	}

	private void drawSpirte(SpriteBatch batch, Sprite sprite) {
		batch.draw(sprite, sprite.getX(), sprite.getY(),sprite.getOriginX(),
				sprite.getOriginY(),
				sprite.getWidth(),sprite.getHeight(),sprite.getScaleX(),sprite.
						getScaleY(),sprite.getRotation());

	}

	@Override
	public void dispose() {
		world.dispose();
	}

	//controller

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode== Input.Keys.C) {
			((ControllableGameBody)this.controlableGameBodies.get(selectedBody)).changeState();
			selectedBody = (selectedBody+1) % this.gameBodies.size();
			((ControllableGameBody)this.controlableGameBodies.get(selectedBody)).changeState();
		}
		((ControllableGameBody)this.controlableGameBodies.get(selectedBody)).applyKey(keycode);
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}


	// On touch we apply force from the direction of the users touch.
	// This could result in the object "spinning"
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		//body.applyTorque(0.4f,true);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	//contact listener

	@Override
	public void beginContact(Contact contact) {
		final Body a = contact.getFixtureA().getBody();
		final Body b = contact.getFixtureB().getBody();
		final IGameBody aBody = allBodies.get(a.getUserData());
		if (aBody==null) return;
		aBody.handleColistion(a, b, allBodies, world);
	}

	@Override
	public void endContact(Contact contact) {

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}




}


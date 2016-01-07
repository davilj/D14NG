package com.mygdx.game.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

import java.util.Map;
import java.util.Random;

/**
 * Created by danie on 12/28/2015.
 */
public abstract class AnimatedGameBody implements IGameBody {
    final Random random = new Random();
    protected static final float PIXELS_TO_METERS = 100f;
    protected TextureAtlas textureAtlas;
    private Animation animation;
    private AnimatedSprite sprite;
    private float elapsedTime = 0;
    private String id;


    protected Body body;

    boolean activated = false;

    public AnimatedGameBody(World world, int x, int y, short catBits, short maskBits) {

        //TODO this could be optimized
        textureAtlas = new TextureAtlas(Gdx.files.internal(getAtlasFileName()));
        animation = new Animation(1/15f, textureAtlas.getRegions(), Animation.PlayMode.LOOP);

        sprite = new AnimatedSprite(animation);

        sprite.setPosition(x,y);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set((sprite.getX() + sprite.getWidth()/2) /
                        PIXELS_TO_METERS,
                (sprite.getY() + sprite.getHeight()/2) / PIXELS_TO_METERS);

        body = world.createBody(bodyDef);
        this.id = random.nextLong() + " - " + this.getClass().getName() + " - " + System.currentTimeMillis();
        body.setUserData(id);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth()/2 / PIXELS_TO_METERS, sprite.getHeight()
                /2 / PIXELS_TO_METERS);


        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = getDensity();
        fixtureDef.restitution = getRestitution();
        fixtureDef.friction = getFriction();
        fixtureDef.filter.categoryBits = catBits;
        fixtureDef.filter.maskBits = maskBits;

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public abstract String getAtlasFileName();
    public abstract float getDensity();
    public abstract float getRestitution();
    public abstract float getFriction();
    public abstract void handleColistion(Body a, Body b, Map<String, IGameBody> allBodies, World world);

    public String getId() {
        return this.id;
    }

    public void updateSprite() {
        sprite.update();
        sprite.setPosition((body.getPosition().x * PIXELS_TO_METERS) - sprite.
                        getWidth()/2 ,
                (body.getPosition().y * PIXELS_TO_METERS) -sprite.getHeight()/2 );
        sprite.setRotation((float)Math.toDegrees(body.getAngle()));

    }

    public Sprite getSprite() {
        return this.sprite;
    }

    public void dispose() {
       textureAtlas.dispose();
    }



}

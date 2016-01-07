package com.mygdx.game.bodies;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.Map;
import java.util.Random;

/**
 * Created by danie on 12/28/2015.
 */
public abstract class SimpleGameBody implements IGameBody {
    final static Random random = new Random();
    protected static final float PIXELS_TO_METERS = 100f;
    protected Texture deActImage;
    protected Sprite spriteDeAcc;
    protected Sprite sprite;
    private String id;


    protected Body body;

    boolean activated = false;

    public SimpleGameBody(World world, int x, int y, short catBits, short maskBits) {
        //TODO this could be optimized
        this.deActImage = new Texture(getDeAccSpriteName());
        this.spriteDeAcc = new Sprite(this.deActImage);

        this.sprite = this.spriteDeAcc;

        sprite.setPosition(x,y);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((sprite.getX() + sprite.getWidth()/2) /
                        PIXELS_TO_METERS,
                (sprite.getY() + sprite.getHeight()/2) / PIXELS_TO_METERS);

        body = world.createBody(bodyDef);
        this.id = random.nextLong() + " - " + this.getClass().getName() + " - " + System.currentTimeMillis();
        body.setUserData(id);
        body.setUserData(this.id);


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

    public abstract String getDeAccSpriteName();
    public abstract void handleColistion(Body a, Body b, Map<String, IGameBody> allBodies, World world);

    public String getId() {
        return this.id;
    }

    @Override
    public void updateSprite() {
        sprite.setPosition((body.getPosition().x * PIXELS_TO_METERS) - sprite.
                        getWidth()/2 ,
                (body.getPosition().y * PIXELS_TO_METERS) -sprite.getHeight()/2 );
        sprite.setRotation((float)Math.toDegrees(body.getAngle()));
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public void dispose() {

    }


}

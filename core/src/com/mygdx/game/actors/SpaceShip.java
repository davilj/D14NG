package com.mygdx.game.actors;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by danie on 12/18/2015.
 */
public class SpaceShip implements Actor {
    Body circleBody;
    Sprite sprite;
    Texture img;

    public SpaceShip(World world, float startx, float starty) {
        //2d body
        BodyDef circleDef = new BodyDef();
        circleDef.type = BodyDef.BodyType.DynamicBody;
        circleDef.position.set(startx, starty);

        circleBody  = world.createBody(circleDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(3.0f);

        FixtureDef circleFixture = new FixtureDef();
        circleFixture.shape = circleShape;
        circleFixture.density = 0.009f;
        circleFixture.friction =  0.1f;
        circleFixture.restitution = 0.5f;

        circleBody.createFixture(circleFixture);



        //back light
        //new PointLight(handler, 200, Color.YELLOW, 200, startx, startx);
        //sprite
        img = new Texture("spaceship.png");
        sprite = new Sprite(img);
        sprite.setPosition(startx, starty);
    }

    @Override
    public void update(SpriteBatch batch) {
        this.sprite.setPosition(circleBody.getPosition().x, circleBody.getPosition().y);
        batch.draw(this.sprite, sprite.getX(), sprite.getY());
    }
}

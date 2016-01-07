package com.mygdx.game.bodies;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.Map;

/**
 * Created by danie on 12/28/2015.
 */
public class Platform extends ControllableGameBody {

    public Platform(World world, int x, int y, short catBits, short maskBits) {
        super(world, x, y, catBits, maskBits);
    }

    @Override
    public String getAccImageName() {
        return "platformDeAct.png";
    }

    public String getDeAccSpriteName() {
       return "platform.png";
   }

    @Override
    public void handleColistion(Body a, Body b, Map<String, IGameBody> allBodies, World world) {

    }

    @Override
    public float getDensity() {
        return 0.9F;
    }

    @Override
    public float getRestitution() {
        return 0.8F;
    }

    @Override
    public float getFriction() {
        return 0.9F;
    }

    public void applyKey(int keycode ) {
        float angle = body.getAngle();
        if(keycode == Input.Keys.LEFT)
            body.applyForce(-0.1f, 0.01f, body.getPosition().x-1, body.getPosition().y, true);
        if(keycode == Input.Keys.RIGHT)
            body.applyForce(0.1f, 0.01f, body.getPosition().x+1, body.getPosition().y, true);

        if(keycode == Input.Keys.UP) {
            Vector2 angleVector = new Vector2(
                    -(float)Math.sin(angle),
                    (float)Math.cos(angle));
            System.out.println("force: " + angleVector);
            body.applyForceToCenter(angleVector,true);
        } else if (keycode == Input.Keys.DOWN) {
            Vector2 angleVector = new Vector2(
                    (float)Math.sin(angle),
                    -(float)Math.cos(angle));
            System.out.println("force: " + angleVector);
            body.applyForceToCenter(angleVector,true);
        }
    }

}

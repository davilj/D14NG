package com.mygdx.game.bodies;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Map;

/**
 * Created by danie on 12/28/2015.
 */
public class Cube extends ControllableGameBody {

    public Cube(World world, int x, int y, short catBits, short maskBits) {
        super(world, x, y, catBits, maskBits);
    }

    @Override
    public String getAtlasFileName() {
        return "rocketOffpack.atlas";
    }

    @Override
    public String getOffAtlasFileName() {
        return "rocketOffpack.atlas";
    }

    @Override
    public String getOnAtlasFileName() {
        return "rocketpack.atlas";
    }

    public String getDeAccSpriteName() {
        return "cubeDeAct.png";
    }

    @Override
    public void handleCollision(Body a, Body b, Map<String, IGameBody> allBodies, World world) {

    }



    @Override
    public float getDensity() {
        return 0.1F;
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

        if(keycode == Input.Keys.RIGHT)
            body.applyForce(0.1f,0, body.getPosition().x, body.getPosition().y, true);
        if(keycode == Input.Keys.LEFT)
            body.applyForce(-0.1f, 0, body.getPosition().x, body.getPosition().y, true);

        if(keycode == Input.Keys.UP) {
            Vector2 currentVelocity = body.getLinearVelocity();
            if (currentVelocity.y<0.05) {
                System.err.println("V:" + currentVelocity);
                Vector2 angleVector = new Vector2(
                        0,
                        0.1f);
                body.applyForceToCenter(angleVector, true);
                //body.applyAngularImpulse(angleVector.y, true);
            }
        }
    }
}

package com.mygdx.game.bodies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Map;

/**
 * Created by danie on 1/1/2016.
 */
public interface IGameBody {
    float getDensity();

    float getRestitution();

    float getFriction();

    void updateSprite();

    Sprite getSprite();

    String getId();

    void dispose();

    void handleColistion(Body a, Body b, Map<String, IGameBody> allBodies, World world);
}

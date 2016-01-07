package com.mygdx.game.bodies;

import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by danie on 12/28/2015.
 */
public class BodyFactory {
    protected static final float PIXELS_TO_METERS = 100f;
    public static <T extends IGameBody> T create(Class<?> aClass, World world, int x, int y, short catBits, short maskBits) {
        IGameBody body;
        //TODO reflections
        if (aClass.equals(Platform.class)) {
            body = new Platform(world, x, y, catBits, maskBits);
            return (T)body;
        }

        if (aClass.equals(Cube.class)) {
            body = new Cube(world, x, y, catBits, maskBits);
            return (T)body;
        }

        body = new Learner(world, x, y, catBits, maskBits);
        return (T)body;
    }
}

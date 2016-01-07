package com.mygdx.game.actors;

import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by danie on 12/18/2015.
 */
public class ActorFactory {
    public final static Actor createSpaceShip(World world, float startx, float starty) {
        return new SpaceShip(world, startx, starty);
    }
}

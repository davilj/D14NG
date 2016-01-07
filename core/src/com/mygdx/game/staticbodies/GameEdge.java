package com.mygdx.game.staticbodies;

import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by danie on 12/23/2015.
 */
public class GameEdge {
    Body body;

    public GameEdge(GameEdge.Config config) {
        BodyDef bodyDef4 = new BodyDef();
        bodyDef4.type = BodyDef.BodyType.StaticBody;

        bodyDef4.position.set(0, 0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = config.categoryBits;
        fixtureDef.filter.maskBits = config.maskBits;

        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(config.x1, config.y1, config.x2, config.y2);
        fixtureDef.shape = edgeShape;

        body = config.world.createBody(bodyDef4);
        body.createFixture(fixtureDef);
        edgeShape.dispose();
    }

    public static GameEdge create(World world, float x1, float y1, float x2, float y2, short world_entity, short physics_entity) {
        Config config = new Config();
        config.world = world;
        config.x1 = x1;
        config.y1 = y1;
        config.x2 = x2;
        config.y2 = y2;
        config.categoryBits = world_entity;
        config.maskBits = physics_entity;
        return new GameEdge(config);
    }

    static class Config {
        World world;
        float x1;
        float y1;
        float x2;
        float y2;
        short categoryBits;
        short maskBits;
    }
}

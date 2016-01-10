package com.mygdx.game.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by danie on 12/28/2015.
 */
public class SwingerChild extends AnimatedGameBody {
    static final Random random = new Random();
    private boolean linked = false;

    public SwingerChild(World world, int x, int y, short catBits, short maskBits) {
        super(world, x, y, catBits, maskBits);
        this.body.applyForceToCenter(generateRandomSource(), true);
    }

    @Override
    public String getAtlasFileName() {
        return "swingChildpack.atlas";
    }

    @Override
    public void updateSprite() {
        super.updateSprite();
        if (!linked && (random.nextInt(100) < 1)) {
            Vector2 randomForce = generateRandomSource();
            System.out.println(randomForce);
            this.body.applyForceToCenter(randomForce, true);
        }
    }

    private Vector2 generateRandomSource() {
        float x = (random.nextInt(10) * 1.0F) - 5.0F;
        float y = (random.nextInt(10) * 1.0F) - 5.0F;
        return new Vector2(x, y);
    }

    @Override
    public float getDensity() {
        return 0.1F;
    }

    @Override
    public float getRestitution() {
        return 0.9F;
    }

    @Override
    public float getFriction() {
        return 0.9F;
    }

    @Override
    public void handleCollision(final Body a, final Body b, final Map<String, IGameBody> allBodies, final World world) {
        //do nothing
    }
}

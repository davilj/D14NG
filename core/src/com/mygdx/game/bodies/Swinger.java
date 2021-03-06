package com.mygdx.game.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by danie on 12/28/2015.
 */
public class Swinger extends AnimatedGameBody {
    static final Random random = new Random();


    private Set<SwingerChild> links = new HashSet();

    public Swinger(World world, int x, int y, short catBits, short maskBits) {
        super(world, x, y, catBits, maskBits);
        this.body.applyForceToCenter(generateRandomSource(), true);
    }

    public void incTouchPoint() {

    }

    @Override
    public String getAtlasFileName() {
        return "swingpack.atlas";
    }

    @Override
    public void updateSprite() {
        super.updateSprite();
        if (random.nextInt(100) < 1) {
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
        final Swinger thisBody = this;
        final IGameBody bBody = allBodies.get(b.getUserData());
        if (bBody == null) return;


        if (bBody instanceof SwingerChild) {
            System.err.println("Lets dance");

            final SwingerChild otherLearner = (SwingerChild) bBody;
            this.incTouchPoint();
            if (!this.links.contains(otherLearner)) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
                        revoluteJointDef.initialize(a, b, a.getWorldCenter());
                        world.createJoint(revoluteJointDef);
                        System.err.println("RevoluteJoint joint created");
                        links.add(otherLearner);

                    }
                });
            }
        }
    }
}

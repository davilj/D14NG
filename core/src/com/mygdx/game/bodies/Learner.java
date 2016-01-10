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
public class Learner extends AnimatedGameBody {
    static final Random random = new Random();
    private State state = State.CHILD;
    private int sAge = 0;
    private Set<Learner> links = new HashSet();

    public Learner(World world, int x, int y, short catBits, short maskBits) {
        super(world, x, y, catBits, maskBits);
        this.body.applyForceToCenter(generateRandomSource(), true);
    }

    public void incTouchPoint() {
        sAge++;
        manageState();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void manageState() {
        System.err.println("The Scale is: " + getSprite().getScaleX());
        if (sAge < 3) {
            this.state = State.CHILD;
        } else if (sAge < 6) {
            this.state = State.YOUNG;
        } else if (sAge < 9 && this.state != State.OLD) {
            this.state = State.MATURE;
        } else if (sAge < 100) {
            sAge = 0;
            this.state = State.YOUNG;

        } else {
            this.state = State.OLD;
        }
    }

    @Override
    public String getAtlasFileName() {
        return "learner2pack.atlas";
    }


    @Override
    public void updateSprite() {
        super.updateSprite();
        if (state == State.CHILD) {
            //getSprite().scale(1.2F);
        } else if (state == State.YOUNG) {
            //getSprite().scale(0.7F);
        } else {
            // getSprite().scale(1.0F);
        }

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
        final Learner thisBody = this;
        final IGameBody bBody = allBodies.get(b.getUserData());
        if (bBody == null) return;


        if (bBody instanceof Learner) {
            System.err.println("Go bump in the night");

            final Learner otherLearner = (Learner) bBody;
            this.incTouchPoint();
            otherLearner.incTouchPoint();
            Learner.State aState = this.getState();
            System.err.println("aState: " + aState);
            Learner.State bState = this.getState();
            System.err.println("bState: " + bState);
            if (aState == Learner.State.MATURE && aState == bState && !this.links.contains(otherLearner)) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        DistanceJointDef jointDef = new DistanceJointDef();
                        jointDef.collideConnected = true;
                        jointDef.dampingRatio = 0.0f;
                        jointDef.frequencyHz = 0;
                        jointDef.length = 0.10f;
                        jointDef.initialize(a, b, a.getPosition(), b.getPosition());
                        world.createJoint(jointDef);
                        System.err.println("Distance joint created");
                        links.add(otherLearner);
                        otherLearner.links.add(thisBody);
                    }
                });
                this.setState(Learner.State.OLD);
                otherLearner.setState(Learner.State.OLD);
            }
        }
    }


    public static enum State {
        CHILD, YOUNG, OLD, MATURE
    }


}

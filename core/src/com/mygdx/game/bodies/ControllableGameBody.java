package com.mygdx.game.bodies;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by danie on 12/29/2015.
 */
public abstract class ControllableGameBody extends SimpleGameBody {
    protected Texture actImage;
    protected Sprite spriteAcc;

    public ControllableGameBody(World world, int x, int y, short catBits, short maskBits) {
        super(world, x, y, catBits, maskBits);
        this.actImage = new Texture((getAccImageName()));
        this.spriteAcc = new Sprite(this.actImage);
    }

    public abstract String getAccImageName();

    public void changeState() {
        this.activated = !this.activated;
        if (activated) {
            this.sprite = this.spriteAcc;
        } else {
            this.sprite = this.spriteDeAcc;
        }
    }

    public void applyKey(int keycode) {
        float angle = body.getAngle();
        if(keycode == Input.Keys.RIGHT)
            body.applyForce(0.0f, 0.001f, body.getPosition().x-1, body.getPosition().y, true);
        if(keycode == Input.Keys.LEFT)
            body.applyForce(0.0f, 0.001f, body.getPosition().x+1, body.getPosition().y, true);

        if(keycode == Input.Keys.UP) {
            Vector2 angleVector = new Vector2(
                    -(float)Math.sin(angle),
                    (float)Math.cos(angle));
            System.out.println("force: " + angleVector);
            body.applyForceToCenter(angleVector,true);
        } else if (keycode==Input.Keys.DOWN) {
            Vector2 angleVector = new Vector2(
                    (float)Math.sin(angle),
                    -(float)Math.cos(angle));
            System.out.println("force: " + angleVector);
            body.applyForceToCenter(angleVector,true);
        }
    }


}

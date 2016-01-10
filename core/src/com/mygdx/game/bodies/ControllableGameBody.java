package com.mygdx.game.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

/**
 * Created by danie on 12/29/2015.
 */
public abstract class ControllableGameBody extends AnimatedGameBody {
    protected Texture actImage;
    protected AnimatedSprite offAnimatedSprite;
    protected TextureAtlas offTextureAtlas;
    private Animation offAnimation;

    protected AnimatedSprite onAnimatedSprite;
    protected TextureAtlas onTextureAtlas;
    private Animation onAnimation;

    public ControllableGameBody(World world, int x, int y, short catBits, short maskBits) {
        super(world, x, y, catBits, maskBits);
        offTextureAtlas = new TextureAtlas(Gdx.files.internal(getOffAtlasFileName()));
        offAnimation = new Animation(1/15f, offTextureAtlas.getRegions(), Animation.PlayMode.LOOP);
        offAnimatedSprite = new AnimatedSprite(offAnimation);

        onTextureAtlas = new TextureAtlas(Gdx.files.internal(getOnAtlasFileName()));
        onAnimation = new Animation(1/15f, offTextureAtlas.getRegions(), Animation.PlayMode.LOOP);
        onAnimatedSprite = new AnimatedSprite(offAnimation);

    }

    public abstract String getOffAtlasFileName();
    public abstract String getOnAtlasFileName();

    public void changeState() {
        this.activated = !this.activated;
        if (activated) {
            this.sprite = this.onAnimatedSprite;
        } else {
            this.sprite = this.offAnimatedSprite;
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

    public void dispose() {
        super.dispose();
        offTextureAtlas.dispose();
    }


}

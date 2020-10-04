package com.mygdx.game.testing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class CardTest1 extends Image {

    boolean isFlipped = false;
    TextureRegion backImage;
    TextureRegion frontImage;
    Vector2 spawnPosition;
    Vector2 targetPosition;
    Vector2 velocity;
    public CardTest1(TextureRegion back,TextureRegion front) {
        super(back);
        spawnPosition = new Vector2(100,100);
        targetPosition = spawnPosition;
        velocity = new Vector2(0,0);
        this.setPosition(spawnPosition.x,spawnPosition.y);

        backImage = back;
        this.frontImage = front;
        this.debug();
        this.setTouchable(Touchable.enabled);
        this.addListener(new DragListener() {
            //todo de resetat rotatia la touchUp/touchDown
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("touch");
                return true;
            }
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                System.out.println("Dragable");
               targetPosition.x = x;
               targetPosition.y = y;

            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }
        });
    }


    @Override
    public void act(float delta) {


        if(isFlipped)
            this.setDrawable(new SpriteDrawable(new Sprite(backImage)));
        else
            this.setDrawable(new SpriteDrawable(new Sprite(frontImage)));

        updateVelocity();
        spawnPosition.x += velocity.x * delta;
        spawnPosition.y += velocity.y * delta;

        setPosition(spawnPosition.x,spawnPosition.y);
    }

    private void updateVelocity() {
        if(targetPosition.x == spawnPosition.x)
            velocity.x = 0;

        if(targetPosition.x < spawnPosition.x)
            velocity.x = 1;

        if(targetPosition.x > spawnPosition.x)
            velocity.x = -1;

        if(targetPosition.y == spawnPosition.y)
            velocity.y = 0;

        if(targetPosition.y < spawnPosition.y)
            velocity.y = 1;

        if(targetPosition.y > spawnPosition.y)
            velocity.y = -1;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean flipped) {
        isFlipped = flipped;
    }
}

package com.mygdx.game.dataLayer.generics;

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

public class Card2 extends Image {
    private static final float DRAG = 2.0f;
    private static final float MAX_SPEED = 2000.0f;
    private static final float FOLLOW_MULTIPLIER = 5.0f;


    boolean isFlipped = false;
    TextureRegion backImage;
    TextureRegion frontImage;

    public Vector2 currentPosition ;// pozitia cartii
    public Vector2 originalPosition;
    public Vector2 spownPosition;
    public float originalRot;

    boolean flipping = false;
    boolean following = true;
    boolean goingBack = false;

    Vector2 targetPosition = new Vector2(0f,0f);// finger position
    Vector2 velocity = new Vector2(0,0);;// vector viteza al cartii

    private String cardID;

    private Image currentImage;

    public Card2(String id,TextureRegion back, TextureRegion front, Vector2 targetPos, Vector2 spawnPos) {
        super(back);
        this.setPosition(100,100);
        backImage = back;

        currentPosition = spawnPos;
        originalPosition = targetPos;
        targetPosition = targetPos;


        this.frontImage = front;
        this.debug();
        this.setTouchable(Touchable.enabled);
        this.addListener(new DragListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                targetPosition = new Vector2(getX() + x  , getY() + y);
                following = true;
                goingBack = false;
                return true;
            }
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (following ==  true){
                    targetPosition = new Vector2(getX() + x,getY() + y);
                }
                if(following == true && targetPosition.y > Gdx.graphics.getHeight()/2){
//                    if(newGameScreen.canChooseCard && !thisCard.getSymbol().equals("b")){
//                        originalPosition.y = Gdx.graphics.getHeight()/2;
//                        //ToDo ceva .emmit() pentru o singura carte
//                    }
                }
                goingBack = false;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                targetPosition =  originalPosition;
                following = false;
                goingBack = true;
            }
        });
    }


    @Override
    public void act(float delta) {
        super.act(delta);

        if(isFlipped)
            this.setDrawable(new SpriteDrawable(new Sprite(backImage)));
        else
            this.setDrawable(new SpriteDrawable(new Sprite(frontImage)));

        if (following) {
            Vector2 followVector = new Vector2(targetPosition.x - currentPosition.x, targetPosition.y - currentPosition.y);
            velocity.x = FOLLOW_MULTIPLIER * followVector.x;
            velocity.y = FOLLOW_MULTIPLIER * followVector.y;
        }
        //TODO vezi ce faci cu asta
        if (goingBack) {
            Vector2 followVector = new Vector2(targetPosition.x - currentPosition.x, targetPosition.y - currentPosition.y);
            velocity.x = FOLLOW_MULTIPLIER * followVector.x;
            velocity.y = FOLLOW_MULTIPLIER * followVector.y;
        }
        velocity.clamp(0, MAX_SPEED);
        velocity.x = velocity.x - delta *DRAG *velocity.x;
        velocity.y = velocity.y - delta *DRAG *velocity.y;

        currentPosition.x = currentPosition.x + delta * velocity.x;
        currentPosition.y = currentPosition.y + delta * velocity.y;
        //collideWithWalls(viewport.getWorldWidth(),viewport.getWorldHeight());

        this.setPosition(currentPosition.x,currentPosition.y);
    }

    public void setRot(float rot){
        currentImage.setRotation(rot);
    }

    public void setPosition(float x, float y){
        currentImage.setX(x);
        currentImage.setY(y);
    }

    public void rePosition(float x, float y){
        this.originalPosition.x = x;
        this.originalPosition.y = y;
        this.currentPosition.x = x;
        this.currentPosition.y = y;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean flipped) {
        isFlipped = flipped;
    }
}
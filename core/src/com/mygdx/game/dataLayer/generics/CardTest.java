package com.mygdx.game.dataLayer.generics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

public class CardTest extends Actor {

    private static final float DRAG = 2.0f;
    private static final float MAX_SPEED = 2000.0f;
    private static final float FOLLOW_MULTIPLIER = 5.0f;

    public Image backImage;
    public Image frontImage;
    public Image currentImage;

    public Vector2 currentPosition ;
    public Vector2 originalPosition;
    public Vector2 spownPosition;
    Vector2 targetPos;
    Vector2 spawnPos;
    Vector2 targetPosition = new Vector2(0f,0f);// finger position

    Vector2 velocity = new Vector2(0,0);


    public boolean isFlipped = false; //false pe spate, true pe fata
    boolean following = true;
    boolean goingBack = false;
    public boolean choosed = false;


    public CardTest(String id,Image backImage, Image frontImage, Vector2 targetPos, Vector2 spawnPos) {
        setName(id);
        this.isFlipped = false;
        this.currentImage = frontImage;
        this.backImage = backImage;
        this.frontImage = frontImage;
        this.targetPos = targetPos;
        this.spawnPos = spawnPos;

        currentPosition = spawnPos;
        originalPosition = targetPos;
        targetPosition = targetPos;

        currentImage.setPosition(originalPosition.x,originalPosition.y);
        currentImage.setTouchable(Touchable.enabled);
        currentImage.addListener(new DragListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("[CardTest] : touched card");
                targetPosition = new Vector2(currentImage.getX() + x, currentImage.getY() + y);
                following = true;
                goingBack = false;
                return true;
            }
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (following ==  true){
                    targetPosition = new Vector2(currentImage.getX() + x,currentImage.getY() + y);
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
        currentImage.debug();
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        currentImage.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        System.out.println("[Card] act");
        super.act(delta);
        flip();
        updatePosition(delta);
    }

    public void updatePosition(float delta){
        if (following) {
            Vector2 followVector = new Vector2(targetPosition.x - currentPosition.x, targetPosition.y - currentPosition.y);
            velocity.x = FOLLOW_MULTIPLIER * followVector.x;
            velocity.y = FOLLOW_MULTIPLIER * followVector.y;
        }
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

        currentImage.setPosition(currentPosition.x,currentPosition.y);

    }

    public void flip(){
        if(isFlipped){
            System.out.println("front");
            currentImage.setName("front");
            currentImage.setDrawable(frontImage.getDrawable());
        }else{
            System.out.println("back");
            currentImage.setName("back");
            currentImage.setDrawable(backImage.getDrawable());
        }
        isFlipped = !isFlipped;
    }


    public boolean isFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean flipped) {
        System.out.println("new flipped = "  + flipped);
        isFlipped = flipped;
    }
}

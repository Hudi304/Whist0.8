package com.mygdx.game.testing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class CardTest extends Actor {


    private Image backImage;
    private Image frontImage;
    private Image currentImage;
    private boolean isFlipped;
    Vector2 targetPos;
    Vector2 spawnPos;
    Vector2 velocity = new Vector2(0,0);


    public CardTest(String id, Image backImage, Image frontImage, final Vector2 targetPos, Vector2 spawnPos) {
        setName(id);
        setTouchable(Touchable.enabled);
        this.isFlipped = false;
        this.currentImage = backImage;
        this.backImage = backImage;
        this.frontImage = frontImage;
        this.targetPos = targetPos;
        this.spawnPos = spawnPos;

        this.currentImage.setTouchable(Touchable.enabled);
        this.currentImage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                System.out.println(x + ' ' + y);
            }
        });
       // Gdx.input.setInputProcessor((InputProcessor) currentImage);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        currentImage.draw(batch,parentAlpha);

    }


    @Override
    public void act(float delta) {
        super.act(delta);

        this.chooseImage();
        this.updateVelocity();


        this.currentImage.setPosition(this.spawnPos.x,this.spawnPos.y);
    }

    private void updateVelocity() {
        if(targetPos.x != spawnPos.x){
            if(targetPos.x < spawnPos.x){
                velocity.x = -1;
            }
            else
                velocity.x = 1;
        }
        else
            velocity.x = 0;

        if(targetPos.y != spawnPos.y){
            if(targetPos.y < spawnPos.y){
                velocity.y = -1;
            }
            else
                velocity.y = 1;
        }
        else
            velocity.y = 0;

        this.spawnPos.x += velocity.x;
        this.spawnPos.y += velocity.y;
    }
    private void chooseImage(){
        if(isFlipped) {
            currentImage = frontImage;

        }else {
            currentImage = backImage;

        }

    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean flipped) {
        System.out.println("new flipped = "  + flipped);
        isFlipped = flipped;
    }


}

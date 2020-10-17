package com.mygdx.game.testing.Atoms;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.testing.HUDs.PlayerHUD;
import com.mygdx.game.testing.HUDs.TableHUD;

public class PlayerCard extends Image {

    Viewport viewport;
    private float originPosX = 0;
    private float originPosY = 0;
    private boolean putDown = false;
    private boolean flipped = true;
    String ID;
    PlayerCard tis;
    TableHUD tableHUD;
    TextureRegion front;
    TextureRegion back;

    public PlayerCard(TextureRegion frontTR,TextureRegion backTR, final Viewport viewport, final TableHUD tableHUD) {
        super(frontTR);

        front = frontTR;
        back = backTR;
        this.tableHUD = tableHUD;
        this.viewport = viewport;
        setPosition(originPosX ,originPosY);
        setOriginX(getWidth()/2);
        setOriginY(getHeight()/2);
        setScale(0.8f);
        setRotation(0);
        tis = this;

        this.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println(getWidth());
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);
                AlphaAction action;

                    MoveToAction moveToAction = new MoveToAction();
                    moveToAction.setPosition(getX() + x ,getY() + y);
                    moveToAction.setDuration(0.3f);
                    addAction(moveToAction);
            }


            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

            if(getY() > viewport.getScreenHeight()*0.4f && ((PlayerHUD)getParent()).getCanChooseCard()){
                originPosX = viewport.getScreenWidth()/2;
                originPosY = viewport.getScreenHeight() * 0.3f;
                setRotation(0);
                ((PlayerHUD)getParent()).sendCard(getID());
                System.out.println("sent card " +  getID());
                putDown = true;
                ((PlayerHUD) getParent()).nrOfCards --;

            }else{
                MoveToAction moveToAction = new MoveToAction();
                moveToAction.setPosition(getX() + x ,getY() + y);
                moveToAction.setDuration(0.3f);
                addAction(moveToAction);
            }

                if(putDown){
                    MoveToAction moveToAction = new MoveToAction();
                    moveToAction.setPosition(originPosX ,originPosY);
                    moveToAction.setDuration(0.3f);
                    addAction(moveToAction);
                }else{
                    MoveToAction moveToAction = new MoveToAction();
                    moveToAction.setPosition(originPosX ,originPosY);
                    moveToAction.setDuration(0.3f);
                    addAction(moveToAction);
                }

            }
        });
    }

    public void flip(boolean faceUp){
        if(faceUp){
            this.setDrawable(new SpriteDrawable(new Sprite(front)));
        }
        else{
            this.setDrawable(new SpriteDrawable(new Sprite(back)));
        }
    }

    public void setOrigPos(float x , float y){
        this.originPosX = x;
        this.originPosY = y;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }



    public boolean isPutDown() {
        return putDown;
    }

    public void setPutDown(boolean putDown) {
        this.putDown = putDown;
    }
}

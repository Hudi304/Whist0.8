package com.mygdx.game.testing;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PlayerCard extends Image {
    private float originPosX = 0;
    private float originPosY = 0;


    private boolean putDown = false;
    Viewport viewport;



    String ID;

    public PlayerCard(TextureRegion image, final Viewport viewport) {
        super(image);
        this.viewport = viewport;
        setPosition(originPosX ,originPosY);
        setOriginX(getWidth()/2);
        setOriginY(getHeight()/2);
        setScale(0.8f);
        setRotation(0);

        this.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println(getWidth());
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);
                AlphaAction action;

                if(getY() > viewport.getScreenHeight()/2 && ((PlayerHUDAct)getParent()).getCanChooseCard()){
                    originPosX = viewport.getScreenWidth()/2;
                    originPosY = viewport.getScreenHeight() * 0.4f;
                    ((PlayerHUDAct) getParent()).resize();
                    ((PlayerHUDAct)getParent()).sendCard(getID());
                    putDown = true;

                }else{
                    MoveToAction moveToAction = new MoveToAction();
                    moveToAction.setPosition(getX() + x ,getY() + y);
                    moveToAction.setDuration(0.3f);
                    addAction(moveToAction);
                }

            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                if(putDown){
                    MoveToAction moveToAction = new MoveToAction();
                    moveToAction.setPosition(viewport.getScreenWidth()/2 ,viewport.getScreenHeight() * 0.4f);
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


    public boolean isPutDown() {
        return putDown;
    }

    public void setPutDown(boolean putDown) {
        this.putDown = putDown;
    }
}

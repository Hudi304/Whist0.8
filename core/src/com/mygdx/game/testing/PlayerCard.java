package com.mygdx.game.testing;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

public class PlayerCard extends Image {
    private float originPosX = 0;
    private float originPosY = 0;


    public PlayerCard(TextureRegion image) {
        super(image);
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
                MoveToAction moveToAction = new MoveToAction();
                moveToAction.setPosition(getX() + x ,getY() + y);
                moveToAction.setDuration(0.3f);
                addAction(moveToAction);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                MoveToAction moveToAction = new MoveToAction();
                moveToAction.setPosition(originPosX ,originPosY);
                moveToAction.setDuration(0.3f);
                addAction(moveToAction);
            }
        });
    }

    public void setOrigPos(float x , float y){
        this.originPosX = x;
        this.originPosY = y;
    }
}

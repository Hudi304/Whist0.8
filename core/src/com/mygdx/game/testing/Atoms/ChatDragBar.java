package com.mygdx.game.testing.Atoms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ChatDragBar extends Image {
    boolean following = false;
    float currentX;
    float followX;
    float targetX;
    float velX;

    ScrollPane scrollPane;
    TextField textField;
    TextButton sendButton;

    Viewport viewport;

    public ChatDragBar(Viewport viewport, ScrollPane scrollPane, TextButton sendBtn, TextField textField,SpriteDrawable image){
        super();
        this.viewport = viewport;
        this.scrollPane = scrollPane;
        this.sendButton = sendBtn;
        this.textField = textField;
        this.setDrawable(image);
        //this.setPosition(viewport.getWorldWidth() - this.getWidth(),viewport.getWorldHeight()/2);

        currentX = Gdx.graphics.getWidth();
        addListener(new DragListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                targetX = getX() + x;
                following = true;
                return true;
            }
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (following ==  true){
                    targetX = getX() + x;
                }


            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                targetX = getX() + x;
                following = false;

            }
        });
    }


    public ChatDragBar(Viewport viewport, SpriteDrawable image){
        super();
        this.viewport = viewport;
        this.setDrawable(image);
        //this.setPosition(viewport.getWorldWidth() - this.getWidth(),viewport.getWorldHeight()/2);

        currentX = Gdx.graphics.getHeight();
        addListener(new DragListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                targetX = getX() + x;
                following = true;
                return true;
            }
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (following ==  true){
                    targetX = getX() + x;
                }


            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                targetX = getX() + x;
                following = false;

            }
        });
    }

    @Override
    public void act(float delta) {
        float DRAG = 1f;
        float FOLLOW_MULTIPLIER = 5f;

        if (following) {
            followX = targetX - currentX;
            velX = FOLLOW_MULTIPLIER * followX;
        }

        velX = velX - delta * DRAG * velX;
        currentX = currentX + delta *velX;
        clamp(viewport.getWorldWidth());
        //table.setPosition(table.getX(),currentY + table.getRowHeight(0) * table.getRows()/2 + getHeight());
        //table.setPosition(table.getX(),currentY + getHeight());
        scrollPane.setPosition(currentX + getWidth(),scrollPane.getY());
        textField.setPosition(currentX + getWidth() + sendButton.getWidth()* 0.9f,0);
        sendButton.setPosition(currentX + getWidth(),0);

        //table.setPosition(0,0);
        this.setPosition(currentX,getY());
    }

    private void clamp( float viewportWidth) {
        if (currentX  <= viewportWidth*2/3) {
            currentX = viewportWidth*2/3;
            velX = 0;
        }
        if (currentX + getWidth() > viewportWidth ) {
            currentX = viewportWidth - getWidth();
            velX = 0;
        }
    }

}


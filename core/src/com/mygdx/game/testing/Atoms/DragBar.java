package com.mygdx.game.testing.Atoms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;



import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.viewport.Viewport;




public class DragBar extends Image {

    boolean following = false;
    float currentY;
    float followY;
    float targetY;
    float velY;

    Table table;
    Viewport viewport;

    public DragBar(Viewport viewport, Table table){
        super();
        this.viewport = viewport;
        this.table = table;

        currentY = Gdx.graphics.getHeight();
        addListener(new DragListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                targetY = getY() + y;
                following = true;
                return true;
            }
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (following ==  true){
                    targetY = getY() + y;
                }


            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                targetY = getY() + y;
                following = false;

            }
        });
    }

    @Override
    public void act(float delta) {
        float DRAG = 1f;
        float FOLLOW_MULTIPLIER = 5f;

        if (following) {
            followY = targetY - currentY;
            velY = FOLLOW_MULTIPLIER * followY;
        }

        velY = velY - delta * DRAG * velY;
        currentY = currentY + delta *velY;
        clamp(viewport.getWorldHeight());
        //table.setPosition(table.getX(),currentY + table.getRowHeight(0) * table.getRows()/2 + getHeight());
        table.setPosition(table.getX(),currentY + getHeight());
        //table.setPosition(0,0);
        this.setPosition(this.getX(),currentY);
    }

    private void clamp( float viewportHeight) {
//        if (currentY  <= viewportHeight - 10 - table.getRows() * table.getRowHeight(0)) {
//            currentY = viewportHeight - table.getRows() * table.getRowHeight(0);
//            velY = 0;
//        }

        if (currentY  <= 0) {
            currentY = 0;
            velY = 0;
        }
        if (currentY + getHeight() > viewportHeight ) {
            currentY = viewportHeight - getHeight();
            velY = 0;
        }
    }



}

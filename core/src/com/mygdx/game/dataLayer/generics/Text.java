package com.mygdx.game.dataLayer.generics;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Text extends Actor {

    public BitmapFont font;
    public String str;

    public Text(String str){
        font = new BitmapFont();
        font.setColor(0.5f,0.4f,0,1);
    }



    public void draw(SpriteBatch batch, float x, float y) {
            font.draw(batch,str,x,y);
    }


}
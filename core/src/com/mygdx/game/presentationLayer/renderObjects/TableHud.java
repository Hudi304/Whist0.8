package com.mygdx.game.presentationLayer.renderObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.dataLayer.repositories.CardsTextureRepository;
import com.mygdx.game.presentationLayer.screens.NewGameScreen;



public class TableHud extends Group{
    private CardsTextureRepository cardsTexture;
    public NewGameScreen newGameScreen;
    Image tableImage;

    public TableHud(CardsTextureRepository cardsTexture,NewGameScreen newGameScreen) {
        super();
        this.cardsTexture = cardsTexture;
        this.newGameScreen = newGameScreen;

    }


    public void initTable(){

        tableImage = new Image(new Texture("tableBG.jpg"));
        tableImage.setWidth(newGameScreen.screenWidth);
        tableImage.setHeight(newGameScreen.screenHeight);
        tableImage.setPosition(0,0);
        this.addActor(tableImage);
    }
    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public void resizeTable(){
        tableImage.setWidth(newGameScreen.screenWidth);
        tableImage.setHeight(newGameScreen.screenHeight * 0.9f);
        tableImage.setPosition(0,0);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        tableImage.draw(batch,parentAlpha);
    }
}

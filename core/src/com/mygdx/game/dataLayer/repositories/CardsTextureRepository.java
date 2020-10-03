package com.mygdx.game.dataLayer.repositories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


import java.util.HashMap;
import java.util.Map;

/**
 * Class which keeps all the textureRegions for the cards
 * HOW TO GET A CARD:
 * enter the id on the getCardTexture(id) and will return TextureRegion or null
 * id -> {'h','d','s','c'}\{1}-{2-14} || 'back'
 * ex
 * getCardTexture("h-12") -> return the image for Queen of Hearts;
 * getCardTexture("back") -> return the image for the backOfTheCard;
 * getCardTexture("12-h") || getCardTexture("h@@12") -> return null;
 */
public class CardsTextureRepository{
    private Map<String, TextureRegion> cardsRegions;


    public CardsTextureRepository(Texture texture) {
        this.cardsRegions = new HashMap<>();
        initCardsAssets(texture);
    }

    private void initCardsAssets(Texture texture) {
        TextureRegion[][] regions = TextureRegion.split(texture, 81, 117);

        String[] suits = {"h","d","s","c"};
        int max_value = 14;

        for(int i = 0; i< suits.length; i++){
            for(int j = 2; j<=max_value; j++){
                TextureRegion cardRegion = regions[i][j-2];
                String id = suits[i]+"-"+j;
                this.cardsRegions.put(id,cardRegion);
            }
        }

        this.cardsRegions.put("back",regions[4][0]);
    }


    public TextureRegion getCardTexture(String card){
        TextureRegion cardImg = this.cardsRegions.get(card);
        return cardImg;
    }

    public Map<String,TextureRegion> getAllTexture(){
        return this.cardsRegions;
    }

}
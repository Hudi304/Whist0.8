package com.mygdx.game.businessLayer.controllers;


import com.mygdx.game.Client;
import com.mygdx.game.businessLayer.networking.dto.NetworkDTO;
import com.mygdx.game.presentationLayer.screens.NewGameScreen;
import com.mygdx.game.testing.GameScreenTest;

import java.util.LinkedList;
import java.util.List;

public class GameController {

    private Client rootController;
    private GameScreenTest gameScreen;
    private List<String> playerCards;
    private List<String> opponents;
    private NetworkDTO.Token token;
    private NetworkDTO.Table table;
    private NetworkDTO.Bids bids;

    private boolean canChooseCard = false;

    public GameController(Client rootController) {
        this.rootController = rootController;
        this.gameScreen = null;
    }

    /**
     * This function calculate and store the order of the opponents on the table
     * @param opponents - a list with all the players on the table(including the player)
     * This method calls another method in gameScreen to create instances of opponents on the screen.
     */
    public void initOpponentsOrder(List<String> opponents){
        // make the player to be the first in this list;
        System.out.println("[Game Controller] : initOpponentsOrder ");
        this.opponents = new LinkedList<>();
        int i=0;
        boolean found = false;
        while(this.opponents.size() != opponents.size()-1){
            if(!found && opponents.get(i).equals(token.getNickname())){
                found = true;
                i++;
                if(i == opponents.size())
                    i = 0;

                continue;
            }

            if(found && !opponents.get(i).equals(token.getNickname()))
            {
                this.opponents.add(opponents.get(i));
            }

            i++;
            if(i==opponents.size())
                i=0;
        }
        //gameScreen.initOpponentsHUD(this.opponents);
        gameScreen.initOpponents(this.opponents);


    }

    /**
     * This method gets called when you receive the cards from server
     * This method calls 2 methods in the gameScreen: one for init the playerCards, and one for init the number of the cards for the opponents
     * @param cards
     */
    public void setCards(List<String> cards){
        System.out.println("[Game Controller] : setCards ");
        this.playerCards = cards;
        this.gameScreen.updateOpponentsCards(cards.size());
        this.gameScreen.updatePlayerCards(cards);
        //call a function in the gameScreen to init Cards in the player hand
        //call a function in the gameScreen to set the number of cards for the opponents
    }

    /**
     * This method gets called when you receive the table status from the server
     * This method calls 2 methods in the gameScreen: one for the player in order to place a card, and one for the opponent
     * It's up for the gameScreen and for respective HUD's to know if the card was already there or not
     * @param table
     */
    public void updateTableStatus(NetworkDTO.Table table){
        System.out.println("[Game Controller] : updateTableStatus ");

        if(this.table == null)
            this.table = table;
        for(NetworkDTO.Table.PlayerStatus ps: table.getPlayersStatus()){
            if(ps.getNickname().equals(token.getNickname()))
            {
                //SHOULD DO SOMETHING HERE!!
                continue;
            }
            if(ps.getCard().equals(this.table.getCardForPlayer(ps.getNickname()))){
                //THE ANIMATION WAS ALREADY DONE
                continue;
            }
            else{
                //TODO: this.gameScreen.updateCardForOpp(ps.getNickname()--String,ps.getCard()--String);
                // ****!!!!!!!***** ps.getCard() can be a specific card or !!!THE STRING "null";
                continue;
            }
        }
        this.table = table;
    }


    public void updateBidStatus(NetworkDTO.Bids bids){
        System.out.println("[Game Controller] : updateBidStatus ");
        if(this.bids == null)
            this.bids = bids;

        for(NetworkDTO.Bids.Bid bid: bids.getBids()){
            if(bid.getNickname().equals(token.getNickname())){
                //SHOULD DO SOMETHING HERE
                continue;
            }

            if(bid.equals(this.bids.getBidByPlayer(bid.getNickname()))){
                //THE ACTION WAS ALREADY DONE!
                continue;
            }
            //call a function in gameScreen to update Stats for bid
            //this.gameScreen.updateBidHudForOpp(bid.getNickname(), bid.getBidValue(),bid.getMade());
        }

        this.bids = bids;
    }


    
    public Client getRootController() {
        return rootController;
    }

    public void setRootController(Client rootController) {
        this.rootController = rootController;
    }



    public void enableBidHud(NetworkDTO.Bids.Bid bid) {
        System.out.println("Should enable bid Hud");
        gameScreen.setForbiddenValue(bid.getForbidden());
        gameScreen.seteBidHudVisibile(true);
    }

    public void disableBidHud() {
        gameScreen.seteBidHudVisibile(false);
    }

    public void enableCardHud() {
        canChooseCard = true;
    }

    public void disableCardHud() {
        canChooseCard = false;
    }

    public void setGameScreen(GameScreenTest gameScreen) {
        this.gameScreen = gameScreen;
    }

    public List<String> getPlayerCards() {
        return playerCards;
    }

    public void setPlayerCards(List<String> playerCards) {
        this.playerCards = playerCards;
    }

    public List<String> getOpponents() {
        return opponents;
    }

    public void setOpponents(List<String> opponents) {
        this.opponents = opponents;
    }

    public NetworkDTO.Token getToken() {
        return token;
    }

    public void setToken(NetworkDTO.Token token) {
        this.token = token;
    }


    public void sendBid(int bid) {
        System.out.println("Bid send: " + bid);
        //TODO: VALIDATE BID HERE
        rootController.sendBid(bid);

    }

    public void sendCard(String card) {
        System.out.println("Card send: " + card);
        //TODO: VALIDATE Card HERE
        rootController.sendCard(card);

    }


    public boolean getCanChooseCard() {
        return canChooseCard;
    }

    public void setCanChooseCard(boolean canChooseCard) {
        this.canChooseCard = canChooseCard;
    }
}

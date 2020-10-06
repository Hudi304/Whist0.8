package com.mygdx.game.businessLayer.controllers;


import com.mygdx.game.Client;
import com.mygdx.game.businessLayer.networking.dto.NetworkDTO;
import com.mygdx.game.presentationLayer.screens.NewGameScreen;

import java.util.LinkedList;
import java.util.List;

public class GameController {

    private Client rootController;
    private NewGameScreen gameScreen;
    private List<String> playerCards;
    private List<String> opponents;
    private NetworkDTO.Token token;
    private NetworkDTO.Table table;
    private NetworkDTO.Bids bids;

    public GameController(Client rootController, NewGameScreen gameScreen) {
        this.rootController = rootController;
        this.gameScreen = gameScreen;
    }

    /**
     * This function calculate and store the order of the opponents on the table
     * @param opponents - a list with all the players on the table(including the player)
     * This method calls another method in gameScreen to create instances of opponents on the screen.
     */
    public void initOpponentsOrder(List<String> opponents){
        // make the player to be the first in this list;
        this.opponents = new LinkedList<>();
        int i=0;
        boolean found = false;
        while(this.opponents.size() != opponents.size()){
            if(!found && opponents.get(i).equals(token.getNickname())){
                found = true;
                i++;
                if(i == opponents.size())
                    i = 0;

                continue;
            }

            if(found)
            {
                this.opponents.add(opponents.get(i));
            }

            i++;
            if(i==opponents.size())
                i++;
        }

        //call a function in NewGameScreen to set opponents position


    }

    /**
     * This method gets called when you receive the cards from server
     * This method calls 2 methods in the gameScreen: one for init the playerCards, and one for init the number of the cards for the opponents
     * @param cards
     */
    public void setCards(List<String> cards){
        this.playerCards = cards;
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
        this.table = table;

        String nickname = null;
        if(table.getFirstToPutCard() != null){
            nickname = table.getFirstToPutCard().getNickname();
        }
        for(NetworkDTO.Table.PlayerStatus ps: table.getPlayersStatus()){
            if(ps.getNickname().equals(token.getNickname())){
                //TODO: call a function in NewGameScreen to update the card for the player
                //ex: gameScreen.setCardForPlayer(ps.getCard());
                continue;
            }

            //TODO: call a function in NewGameScreen to update the card placed by an opponent
            //ex: gameScreen.setCardForOpponent(ps.getNickname(), ps.getCard());



            if(nickname != null && nickname.equals(ps.getNickname())){
                //TODO: call a function to make visible that a opponent is next to place a card;
                //ex. gameScreen.markOpponentAsNextToPlaceCard(ps.getNickname());
                continue;
            }



        }
    }


    public void updateBidStatus(NetworkDTO.Bids bids){
        this.bids = bids;
        String nickname = null;
        if(bids.getFirstToBid() != null)
            nickname = bids.getFirstToBid().getNickname();
        for(NetworkDTO.Bids.Bid bid: bids.getBids()){
            if(bid.getNickname().equals(token.getNickname())){
                //TODO: call a function to update the HUD for the player
                //ex. this.gameScreen.setUpdatedBidForPlayer(bid.getBid(), bid.getMade());
                continue;
            }

            //TODO: call a function to update the HUD for a specific opponent
            //ex. this.gameScreen.setUpdatedBidForOpp(bid.getNickname(), bid.getBid(), bid.getMade());

            if(nickname != null  && nickname.equals(bid.getNickname())){
                //TODO: call a function to make visible that a opponent is next to make a bid;
                //ex. this.gameScreen.markOpponentAsNextToBid(bid.getNickname());
                continue;
            }

        }
    }
    public Client getRootController() {
        return rootController;
    }

    public void setRootController(Client rootController) {
        this.rootController = rootController;
    }

    public NewGameScreen getNewGameScreen() {
        return gameScreen;
    }

    public void enableBidHud(NetworkDTO.Bids.Bid bid) {
//        gameScreen.setBidForbidden(bid.getForbidden());
//        gameScreen.enableBidHud();
    }

    public void disableBidHud() {
//        gameScreen.disableBidHud();
    }

    public void enableCardHud() {
        //gameScreen.enableCardHud();

    }

    public void disableCardHud() {
        //gameScreen.disableCardHud();
    }

    public void setNewGameScreen(NewGameScreen gameScreen) {
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



}

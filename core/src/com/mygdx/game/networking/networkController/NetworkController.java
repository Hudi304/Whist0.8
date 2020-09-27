package com.mygdx.game.networking.networkController;


import com.mygdx.game.networking.dto.NetworkDTO;

import java.net.URISyntaxException;
import java.util.List;

public interface NetworkController {

    /**
     * Get a copy of the instance of Network interface
     * ex :
     *  {
     *      NetworkService service = NetworkService.getInstance();
     *      service.initService(http,this)
     *      this.networkService = service;
     *  }
     * @param http: the address of the server (host + port)
     * @return -;
     */
    void initializeNetworkService(String http);



    void connect() throws URISyntaxException;
    /**
     * This function gets call when the connection status has changed
     * ex: when you try to connect to server, when to connection is establish... the parameter isConnected will be true;
     * @param isConnected
     */
     void setConnectedStatus(boolean isConnected);

    /**
     * This function gets called when the networkingService has received token
     * @param token
     */
    void setToken(NetworkDTO.Token token);

    /**
     * This function get's called when the player made the ROOM_REQUEST. It moves the screen to ROOM_SCREEN
     */
    void goToRoomsScreen();

    /**
     * This function get's called when the client received the response from ROOM_REQUEST. Look to put all the rooms in the ROOM_SCREEN.
     * @param roomsResponse
     */
    void updateRooms(NetworkDTO.RoomsResponse roomsResponse);

    /**
     * This functions gets called every time client receives lobbyData
     * @param lobby
     * @param isOwner -> true === u are owner, false otherwise;
     */
    void setLobbyData(NetworkDTO.Lobby lobby, boolean isOwner);

    /**
     * This function gets called when you need to change the screen to LOBBY
     */
    void goToLobbyScreen();

    /**
     * This function gets called when you need to change the screen to MAIN_MENU
     */
    void goToMainMenu();

    /**
     * This function gets called when you need to change the screen to GAME
     */
    void goToGame();

    /**
     * This functions gets called when you received the players for a game for the first time
     * @param players
     */
    void updatePlayerList(List<NetworkDTO.Player> players);

    /**
     * This function gets called when you received the cards
     *
     * @param cards
     */
     void updateCards(NetworkDTO.Cards cards);

    /**
     * This function gets called when it's your turn to make the bid
     * It's up to you to ensure that the bid which will be made is valid!!
     * @param bid
     */
    void showHudForBids(NetworkDTO.Bids.Bid bid);

    /**
     * This function gets called when it's not your turn to make bid;
     */
    void hideBidHUD();


    /**
     * This function gets called when it's your turn to place a card
     * It's up to you to ensure that the card which will be placed is valid;
     * @param ps
     */
    void showHudForCards(NetworkDTO.Table.PlayerStatus ps);


    /**
     * This function gets called when it's not your turn to place a card
     */
    void hideCardHud();

    /**
     * This function gets called when you received the bids Data
     * @param bids
     */
    void updateBids(NetworkDTO.Bids bids);

    /**
     * This function gets called when the client received Table Data
     * @param table
     */
    void updateTable(NetworkDTO.Table table);


    /**
     * Call this function when you picked a card and want to send it to server
     * !! Make sure the card is valid before calling this function!!
     * @param card
     */
    void sendCard(String card);

    /**
     * Call this function when you choosed the bid and want to send it to server
     * !!Make sure the bid is valid before calling this function!!
     * @param bid
     */
    void sendBid(int bid);

    /**
     * Call this function when you want to join a game from the room_list
     * @param nickname
     * @param roomID
     */
    void joinGame(String roomID, String nickname);

    /**
     * Call this function when you want to join a game from the
     * @param roomID
     * @param nickname
     */
    void createGame(String roomID,String nickname);

    /**
     * Call this function when you want to start the game in which you are owner;
     */
    void startGame();

    /**
     * Call this function when you want to leave the room
     */
    void leaveRoom();

    /**
     * call this function when you want to get all the rooms
     */
    public void getRooms();

    /**
     * Call this function when you want to disconnect to the server
     * EX: When the player press the exit button or shut down the main procces.
     */
    public void disconnect();


}

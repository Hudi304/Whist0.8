package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.businessLayer.controllers.GameController;
import com.mygdx.game.businessLayer.others.Constants;
import com.mygdx.game.dataLayer.generics.Card;
import com.mygdx.game.dataLayer.generics.Player;
import com.mygdx.game.businessLayer.networking.dto.NetworkDTO;
import com.mygdx.game.businessLayer.networking.networkController.NetworkController;
import com.mygdx.game.businessLayer.networking.networkService.NetworkService;
import com.mygdx.game.dataLayer.repositories.CardsTextureRepository;
import com.mygdx.game.presentationLayer.screens.MainMenu;
import com.mygdx.game.presentationLayer.screens.NewGameScreen;
import com.mygdx.game.presentationLayer.screens.ScreenState;
import com.mygdx.game.testing.GameScreenTest;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Client extends Game implements NetworkController {

	public String nickName ;
	public String roomId;

	public float screenWidth;
	public float screenHeight;

	public NetworkService networkService;
	public GameController gameController;

	com.mygdx.game.presentationLayer.screens.MainMenu mainMenuScreen;
	com.mygdx.game.presentationLayer.screens.Credentials credentialsScreen;
	com.mygdx.game.presentationLayer.screens.JoinRoom joinRoomScreen;
	com.mygdx.game.presentationLayer.screens.CreateRoom createRoomScreen;
	com.mygdx.game.presentationLayer.screens.Lobby lobbyScreen;
	com.mygdx.game.presentationLayer.screens.GameScreen gameScreen;
	com.mygdx.game.presentationLayer.screens.NewGameScreen newGameScreen;
	GameScreenTest testing;

	//FLAGS
	private com.mygdx.game.presentationLayer.screens.ScreenState screenState = com.mygdx.game.presentationLayer.screens.ScreenState.MAIN_MENU;
	private com.mygdx.game.presentationLayer.screens.ScreenState previousScreenState = com.mygdx.game.presentationLayer.screens.ScreenState.MAIN_MENU;

	CardsTextureRepository cardsTextureRepository;

	@Override
	public void dispose() {
		super.dispose();
		System.out.println("Disposed here!");
	}

	@Override
	public void create() {
		// System.out.println("Version: " + version);

		this.initializeNetworkService(Constants.serverHTTP);

		cardsTextureRepository = new CardsTextureRepository(new Texture("cardSprite.gif"));

		mainMenuScreen =  new MainMenu(this);
		credentialsScreen =  new com.mygdx.game.presentationLayer.screens.Credentials(this);
		joinRoomScreen =  new com.mygdx.game.presentationLayer.screens.JoinRoom(this);
		createRoomScreen = new com.mygdx.game.presentationLayer.screens.CreateRoom(this);
		lobbyScreen = new com.mygdx.game.presentationLayer.screens.Lobby(this);
		gameScreen =  new com.mygdx.game.presentationLayer.screens.GameScreen(this);
		newGameScreen = new com.mygdx.game.presentationLayer.screens.NewGameScreen(this,cardsTextureRepository);

		this.gameController = new GameController(this, newGameScreen);
		testing = new GameScreenTest(cardsTextureRepository);

		setSCreen(screenState);
		//setSCreen(ScreenState.TEST);


		try {
			this.connect();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}

	public void setSCreen(com.mygdx.game.presentationLayer.screens.ScreenState state){
		switch (state){
			case MAIN_MENU:
				setScreen(mainMenuScreen);
				break;
			case JOIN_ROOM:
				setScreen(joinRoomScreen);
				break;
			case LOBBY:
				setScreen(lobbyScreen);
				break;
			case CREATE_ROOM:
				setScreen(createRoomScreen);
				break;
			case GAME:
				setScreen(newGameScreen);
				break;
			case CREDENTIALS:
				setScreen(credentialsScreen);
				break;
			case NEWGAME:
				setScreen(newGameScreen);
				break;
			default:
				setScreen(testing);
				break;
		}

	}

	@Override
	public void render() {
		if(screenState != previousScreenState){
			setSCreen(screenState);
			previousScreenState = screenState;
		}
		super.render();
	}


	/**
	 * Get a copy of the instance of Network interface
	 * ex :
	 * {
	 * NetworkService service = NetworkService.getInstance();
	 * service.initService(http,this)
	 * this.networkService = service;
	 * }
	 *
	 * @param http : the address of the server (host + port)
	 * @return -;
	 */
	@Override
	public void initializeNetworkService(String http) {
		NetworkService service = NetworkService.getInstance();
	 	service.initService(http,this);
		this.networkService = service;
	}

	@Override
	public void connect() throws URISyntaxException {
		this.networkService.login();
	}

	/**
	 * This function gets call when the connection status has changed
	 * ex: when you try to connect to server, when to connection is establish... the parameter isConnected will be true;
	 *
	 * @param isConnected
	 */
	@Override
	public void setConnectedStatus(boolean isConnected) {
		System.out.println("You are connected!");
	}

	/**
	 * This function gets called when the networkingService has received token
	 *
	 * @param token
	 */
	@Override
	public void setToken(NetworkDTO.Token token) {
		System.out.println(token);
		nickName = token.getNickname();
		roomId = token.getRoomID();
		this.gameController.setToken(token);
	}

	/**
	 * This function get's called when the player made the ROOM_REQUEST. It moves the screen to ROOM_SCREEN
	 */
	@Override
	public void goToRoomsScreen() {
		this.screenState = com.mygdx.game.presentationLayer.screens.ScreenState.JOIN_ROOM;
	}

	/**
	 * This function get's called when the client received the response from ROOM_REQUEST. Look to put all the rooms in the ROOM_SCREEN.
	 *
	 * @param roomsResponse
	 */
	@Override
	public void updateRooms(NetworkDTO.RoomsResponse roomsResponse) {
		this.joinRoomScreen.setRooms(roomsResponse.getRooms());
		System.out.println("Rooms Received!");
	}

	/**
	 * This functions gets called every time client receives lobbyData
	 *
	 * @param lobby
	 * @param isOwner -> true === u are owner, false otherwise;
	 */
	@Override
	public void setLobbyData(NetworkDTO.Lobby lobby, boolean isOwner) {
		//todo booleanul pentru butonu de start
		lobbyScreen.isOwner =  isOwner;
		lobbyScreen.players = lobby.getPlayers();
		System.out.println("new data:");
		///lobbyScreen.refreshTable(lobby.getPlayers());
	}

	/**
	 * This function gets called when you need to change the screen to LOBBY
	 */
	@Override
	public void goToLobbyScreen() {
		screenState = com.mygdx.game.presentationLayer.screens.ScreenState.LOBBY;
	}

	/**
	 * This function gets called when you need to change the screen to MAIN_MENU
	 */
	@Override
	public void goToMainMenu() {
		screenState = com.mygdx.game.presentationLayer.screens.ScreenState.MAIN_MENU;
	}

	/**
	 * This function gets called when you need to change the screen to GAME
	 */
	@Override
	public void goToGame() {
		screenState = ScreenState.NEWGAME;
	}

	/**
	 * This functions gets called when you received the players for a game for the first time
	 *
	 * @param players
	 */
	@Override
	public void updatePlayerList(List<NetworkDTO.Player> players) {
		System.out.println("[Client] updatePlayerList" );
		List<String> playersStr = new LinkedList<>();
		for(NetworkDTO.Player p: players){
			playersStr.add(p.getName());
		}


		this.gameController.initOpponentsOrder(playersStr);
		System.out.println("[client]: Started to change the screen at: " + System.currentTimeMillis());
		screenState = ScreenState.NEWGAME;

	}

	/**
	 * This function gets called when you received the cards
	 *
	 * @param cards
	 */
	@Override
	public void updateCards(NetworkDTO.Cards cards) {
		System.out.println("[Client] updateCards" );


		this.gameController.setCards(cards.getCards());
	}

	/**
	 * This function gets called when it's your turn to make the bid
	 * It's up to you to ensure that the bid which will be made is valid!!
	 *
	 * @param bid
	 */
	@Override
	public void showHudForBids(NetworkDTO.Bids.Bid bid) {
		System.out.println("[Client] showHudForBids" );
		gameController.enableBidHud(bid);

	}

	/**
	 * This function gets called when it's not your turn to make bid;
	 */
	@Override
	public void hideBidHUD() {
		System.out.println("[Client] hideBidHUD" );

		gameController.disableBidHud();
	}

	/**
	 * This function gets called when it's your turn to place a card
	 * It's up to you to ensure that the card which will be placed is valid;
	 *
	 * @param ps
	 */
	@Override
	public void showHudForCards(NetworkDTO.Table.PlayerStatus ps) {
		System.out.println("[Client] showHudForCards" );
		gameController.enableCardHud();
	}

	/**
	 * This function gets called when it's not your turn to place a card
	 */
	@Override
	public void hideCardHud() {
		System.out.println("[Client] hideCardHud" );
		gameController.disableCardHud();

	}

	/**
	 * This function gets called when you received the bids Data
	 *
	 * @param bids
	 */
	@Override
	public void updateBids(NetworkDTO.Bids bids) {
		System.out.println("[Client] updateBids" );
		gameController.updateBidStatus(bids);
	}

	/**
	 * This function gets called when the client received Table Data
	 *
	 * @param table
	 */
	@Override
	public void updateTable(NetworkDTO.Table table) {
		System.out.println("[Client] updateTable" );
		gameController.updateTableStatus(table);
	}

	/**
	 * Call this function when you picked a card and want to send it to server
	 * !! Make sure the card is valid before calling this function!!
	 * @param card
	 */
	@Override
	public void sendCard(String card) {
		System.out.println("[Client] sendCard" );
		//todo valideaza asta
		this.networkService.sendCardResponse(card);
	}

	/**
	 * Call this function when you choosed the bid and want to send it to server
	 * !!Make sure the bid is valid before calling this function!!
	 * @param bid
	 */
	@Override
	public void sendBid(int bid) {
		this.networkService.sendBidResponse(bid);
	}

	/**
	 * Call this function when you want to join a game from the room_list
	 *
	 * @param roomID
	 * @param nickname
	 */
	@Override
	public void joinGame(String roomID, String nickname) {
		this.networkService.joinRoomRequest(roomID,nickname);
	}

	/**
	 * Call this function when you want to join a game from the
	 *
	 * @param roomID
	 * @param nickname
	 */
	@Override
	public void createGame(String roomID, String nickname) {
		this.networkService.createGameRequest(roomID,nickname);
	}

	/**
	 * Call this function when you want to start the game in which you are owner;
	 */
	@Override
	public void startGame() {
		networkService.startGameRequest();
	}

	/**
	 * Call this function when you want to leave the room
	 */
	@Override
	public void leaveRoom() {
		System.out.println("Client left Room");
		try{
			this.networkService.leaveRoomRequest();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * call this function when you want to get all the rooms
	 */
	@Override
	public void getRooms() {
		this.networkService.makeRoomsRequest();
	}

	/**
	 * Call this function when you want to disconnect to the server
	 * EX: When the player press the exit button or shut down the main procces.
	 */
	@Override
	public void disconnect() {

	}

	@Override
	public void sendWinner(String winner) {

	}


	public float getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(float screenWidth) {
		this.screenWidth = screenWidth;
	}

	public float getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(float screenHeight) {
		this.screenHeight = screenHeight;
	}



	public void goToScreen(com.mygdx.game.presentationLayer.screens.ScreenState state){
		this.screenState = state;
	}

	public void canRunGame(){
		System.out.println("[client]: CanRunGame method run at: " + System.currentTimeMillis());
		this.networkService.canRunGame();
	}
}


package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.mygdx.game.Screens.*;
import com.mygdx.game.networking.dto.NetworkDTO;
import com.mygdx.game.networking.networkController.NetworkController;
import com.mygdx.game.networking.networkService.NetworkService;

import java.net.URISyntaxException;
import java.security.cert.CertificateRevokedException;
import java.util.List;

public class Client extends Game implements NetworkController {

	//todo sa nu lesi asta aici
	public String nickName = "Hudy";

	public String roomId;


	public float screenWidth;
	public float screenHeight;

	public ScreenController screenController;

	public NetworkService networkService;

	MainMenu mainMenuScreen;
	Credentials credentialsScreen;
	JoinRoom joinRoomScreen;
	CreateRoom createRoomScreen;
	Lobby lobbyScreen;
	GameScreen gameScreen;
	//FLAGS
	private ScreenState screenState = ScreenState.MAIN_MENU;
	private ScreenState previousScreenState = ScreenState.MAIN_MENU;


	@Override
	public void dispose() {
		super.dispose();
		System.out.println("Disposed here!");
	}

	@Override
	public void create() {
		// System.out.println("Version: " + version);

		this.initializeNetworkService(Constants.serverHTTP);
		screenController = new ScreenController(this);


		mainMenuScreen =  new MainMenu(this);
		credentialsScreen =  new Credentials(this);
		joinRoomScreen =  new JoinRoom(this);
		createRoomScreen = new CreateRoom(this);
		lobbyScreen = new Lobby(this);
		gameScreen =  new GameScreen(this);


		setSCreen(screenState);

		try {
			this.connect();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}

	public void setSCreen(ScreenState state){
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
				setScreen(gameScreen);
				break;
			case CREDENTIALS:
				setScreen(credentialsScreen);
				break;
			default:
				System.out.println("Error on setSCreen!!!!");
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
	}

	/**
	 * This function get's called when the player made the ROOM_REQUEST. It moves the screen to ROOM_SCREEN
	 */
	@Override
	public void goToRoomsScreen() {
		this.screenState = ScreenState.JOIN_ROOM;
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
		screenState = ScreenState.LOBBY;
	}

	/**
	 * This function gets called when you need to change the screen to MAIN_MENU
	 */
	@Override
	public void goToMainMenu() {
		screenState = ScreenState.MAIN_MENU;
	}

	/**
	 * This function gets called when you need to change the screen to GAME
	 */
	@Override
	public void goToGame() {
		screenState = ScreenState.GAME;
	}

	/**
	 * This functions gets called when you received the players for a game for the first time
	 *
	 * @param players
	 */
	@Override
	public void updatePlayerList(List<NetworkDTO.Player> players) {

	}

	/**
	 * This function gets called when you received the cards
	 *
	 * @param cards
	 */
	@Override
	public void updateCards(NetworkDTO.Cards cards) {

	}

	/**
	 * This function gets called when it's your turn to make the bid
	 * It's up to you to ensure that the bid which will be made is valid!!
	 *
	 * @param bid
	 */
	@Override
	public void showHudForBids(NetworkDTO.Bids.Bid bid) {

	}

	/**
	 * This function gets called when it's not your turn to make bid;
	 */
	@Override
	public void hideBidHUD() {

	}

	/**
	 * This function gets called when it's your turn to place a card
	 * It's up to you to ensure that the card which will be placed is valid;
	 *
	 * @param ps
	 */
	@Override
	public void showHudForCards(NetworkDTO.Table.PlayerStatus ps) {

	}

	/**
	 * This function gets called when it's not your turn to place a card
	 */
	@Override
	public void hideCardHud() {

	}

	/**
	 * This function gets called when you received the bids Data
	 *
	 * @param bids
	 */
	@Override
	public void updateBids(NetworkDTO.Bids bids) {

	}

	/**
	 * This function gets called when the client received Table Data
	 *
	 * @param table
	 */
	@Override
	public void updateTable(NetworkDTO.Table table) {

	}

	/**
	 * Call this function when you picked a card and want to send it to server
	 * !! Make sure the card is valid before calling this function!!
	 *
	 * @param card
	 */
	@Override
	public void sendCard(String card) {

	}

	/**
	 * Call this function when you choosed the bid and want to send it to server
	 * !!Make sure the bid is valid before calling this function!!
	 *
	 * @param bid
	 */
	@Override
	public void sendBid(int bid) {

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
	 * Call this function when you want to start the game in which you are owner;
	 */
	@Override
	public void startGame() {

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



	public void goToScreen(ScreenState state){
		this.screenState = state;
	}
}


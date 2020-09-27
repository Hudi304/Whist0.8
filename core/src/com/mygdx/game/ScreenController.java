package com.mygdx.game;

public class ScreenController {

    Client mainClient;

    //todo orice ar fi legat de trecerea itre Screenrui ar trebui sa fie aici

    public ScreenController(Client mainClient){
        this.mainClient =mainClient;
    }

    public void goToCredentialsScreen(){
        mainClient.setScreen(mainClient.credentialsScreen);
    }

    public void goToJoinRoomScreen(){
        mainClient.setScreen(mainClient.joinRoomScreen);
    }

    public void goToMainMenu(){
        mainClient.setScreen(mainClient.mainMenuScreen);
    }

    public void goToCreateRoomScreen(){
        mainClient.setScreen(mainClient.createRoomScreen);
    }

    public void goToLobbyScreen(){
        mainClient.setScreen(mainClient.lobbyScreen);
    }

    public void goToGameScreen(){
        mainClient.setScreen(mainClient.gameScreen);
    }



}

package com.mygdx.game.businessLayer.networking.networkService;



import com.mygdx.game.Client;
import com.mygdx.game.businessLayer.controllers.GameController;
import com.mygdx.game.businessLayer.networking.networkController.NetworkController;
import com.mygdx.game.businessLayer.networking.actions.ClientActions;
import com.mygdx.game.businessLayer.networking.actions.ServerActions;
import com.mygdx.game.businessLayer.networking.dto.NetworkDTO;

import com.mygdx.game.businessLayer.others.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class NetworkService {
    Socket socket;
    private String http = "http://localhost:8080";
    private NetworkDTO.Token token = null;
    private com.mygdx.game.businessLayer.networking.networkController.NetworkController rootController = null;

    private NetworkDTO.Table table = null;
    private NetworkDTO.Bids bids = null;
    private long startPing = -1;

    private static NetworkService instance = new NetworkService();

    private NetworkService(){

    }

    public static NetworkService getInstance(){
        return instance;
    }


    public void initService(String http, NetworkController rootController){
        this.http = http;
        this.rootController = rootController;
    }



    public void login() throws URISyntaxException {
        socket = IO.socket(http);
        socket.connect();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                /**
                System.out.println("Service -> try to connect ...");
                while (!socket.connected()){
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                rootController.setConnectedStatus(true);
                System.out.println("Service -> isConnected = true");
                socket.emit(ClientActions.LOGIN);
*/
                System.out.println("Networking started!!");
                boolean connected = false;
                boolean prevStateConnected = false;
                while (true){
                    prevStateConnected = connected;
                    connected = socket.connected();

                    if(prevStateConnected != connected){
                        if(!connected){
                            System.out.println("You can't reach the server!");
                        }
                        else{
                            socket.emit(ClientActions.LOGIN);
                            socket.emit(ClientActions.PING);
                            rootController.joinGame("test1", Constants.generateNickname());

                        }

                    }

                }
            }
        };
        Thread th = new Thread(runnable);
        th.start();

        configSocketEvents();

    }

    private void configSocketEvents() {
        socket.on(ServerActions.CONNECTED, (new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject temp = (JSONObject) args[0];
                try {
                    JSONObject tokenJSON = temp.getJSONObject("token");
                    //todo
                    token = new NetworkDTO.Token(tokenJSON);
                    System.out.println("TOKEN RECEIVED");
                    rootController.setToken(token);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));

        socket.on(ServerActions.PING, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                long endTime = System.currentTimeMillis();
                if(startPing != -1){
                    System.out.println("PING: " + (endTime - startPing));
                }

                startPing = endTime;
                socket.emit(ClientActions.PING);
            }
        });


        socket.on(ServerActions.TOKEN,(new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject temp = (JSONObject) args[0];
                try {
                    JSONObject tokenJSON = temp.getJSONObject("token");
                    token = new NetworkDTO.Token(tokenJSON);
                    System.out.println("TOKEN RECEIVED");
                    rootController.setToken(token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));

        socket.on(ServerActions.ROOMS_RESPONSE,(new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject temp = (JSONObject) args[0];
                try {
                    JSONObject roomsJSON = temp.getJSONObject("roomsData");
                    NetworkDTO.RoomsResponse roomsResponse = new NetworkDTO.RoomsResponse(roomsJSON);
                    NetworkService.this.rootController.updateRooms(roomsResponse);
                    NetworkService.this.rootController.goToRoomsScreen();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));

        socket.on(ServerActions.LOBBY, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Lobby DATA RECEIVED ...");
                JSONObject temp = (JSONObject) args[0];
                try {
                    JSONObject lobbyData = temp.getJSONObject("lobby");
                    NetworkDTO.Lobby lobby = new NetworkDTO.Lobby(lobbyData);
                    boolean isOwner = false;
                    if (lobby != null && token.getNickname().equals(lobby.getOwner()))
                        isOwner = true;
                    rootController.setLobbyData(lobby, isOwner);
                    rootController.goToLobbyScreen();
                    System.out.println(lobby);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        socket.on(ServerActions.SCORE_STATUS, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("------------[NetworkService] ScotreStatus--------------");
                JSONObject temp = (JSONObject) args[0];
                //JSONArray array = (JSONArray) args[0];
                //System.out.println(array);
                //todo implement this shit
                //System.out.println(args[0]);
                try {
                    JSONArray scoreData = temp.getJSONArray("score");
                    System.out.println(scoreData);

                    List<NetworkDTO.Score> scores = new ArrayList<>();
                    for (int i = 0; i < scoreData.length(); i++) {
                        JSONObject score = null;
                        try {
                            score = scoreData.getJSONObject(i);
                            scores.add(new NetworkDTO.Score(score));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    rootController.updateScoreStatus(scores);

                    //Thread.sleep(3000);
                    socket.emit(ClientActions.GOT_SCORE, token.getToken());
                } catch (Exception e) {
                    e.printStackTrace();
                    socket.emit(ClientActions.GOT_SCORE, token.getToken());
                }

            }
        });

        socket.on(ServerActions.END_GAME, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println(args[0]);

            }
        });

        socket.on(ServerActions.ERROR,(new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println(args[0]);
            }
        }));

        socket.on(ServerActions.KICK_ALL,(new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                NetworkService.this.rootController.goToMainMenu();
            }
        }));

        socket.on(ServerActions.PLAYER_REQUEST,(new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                NetworkService.this.rootController.goToGame();
                List<NetworkDTO.Player> players = new LinkedList<>();
                JSONArray array = (JSONArray) args[0];
                for (int i = 0; i < array.length(); i++) {
                    JSONObject playerJSON = null;
                    try {
                        playerJSON = array.getJSONObject(i);
                        NetworkDTO.Player player = new NetworkDTO.Player(playerJSON);
                        players.add(player);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("[networking]: Received PlayerList at: " + System.currentTimeMillis() );
                rootController.updatePlayerList(players);
                socket.emit(ClientActions.GOT_PLAYERS,token.getToken());

            }
        }));

        socket.on(ServerActions.GET_CARDS,(new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    System.out.println(args[0]);
                    NetworkDTO.Cards cards = new NetworkDTO.Cards((JSONObject) args[0]);
                    NetworkService.this.rootController.updateCards(cards);
                    socket.emit(ClientActions.GOT_CARDS, token.getToken());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));

        socket.on(ServerActions.BID_REQUEST,(new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (bids == null) {
                    System.out.println("Bids Status is null");
                    return;
                }

                NetworkDTO.Bids.Bid bid = bids.getFirstToBid();
                if (bid != null && bid.getNickname().equals(token.getNickname())){
                    NetworkService.this.rootController.showHudForBids(bid);
                    System.out.println("recieved bid request ");
                }
                else
                    NetworkService.this.rootController.hideBidHUD();


            }
        }));

        socket.on(ServerActions.CARD_REQUEST,(new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (table == null) {
                    System.out.println("Table status is null!");
                    return;
                }
                NetworkDTO.Table.PlayerStatus ps = table.getFirstToPutCard();
                if (ps != null && ps.getNickname().equals(token.getNickname())) {
                    NetworkService.this.rootController.showHudForCards(ps);
                    System.out.println("gave card);");
                } else
                    NetworkService.this.rootController.hideCardHud();
            }
        }));


        socket.on(ServerActions.WINNER,(new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String winner = null;
                try {
                    winner = ((JSONObject)args[0]).getString("winner");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                rootController.sendWinner(winner);
                socket.emit(ClientActions.GOT_WINNER, token.getToken());
            }
        }));

        socket.on(ServerActions.BID_STATUS, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject bidResponse = (JSONObject) args[0];
                try {
                    bids = new NetworkDTO.Bids(bidResponse);
                    NetworkService.this.rootController.updateBids(bids);


                    socket.emit(ClientActions.GOT_BID_STATUS, token.getToken());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        socket.on(ServerActions.TABLE_STATUS, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject tableStatus = (JSONObject) args[0];
                try {
                    table = new NetworkDTO.Table(tableStatus);
                    for(NetworkDTO.Table.PlayerStatus ps: table.getPlayersStatus()){
                        System.out.println(ps);
                    }
                    NetworkService.this.rootController.updateTable(table);
                    socket.emit(ClientActions.GOT_TABLE_STATUS, token.getToken());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void makeRoomsRequest() {
        socket.emit(ClientActions.ROOMS_REQUEST);
    }

    public void joinRoomRequest(String roomID, String nickname) {
        JSONObject data = new JSONObject();
        try {
            data.put("roomID",roomID);
            data.put("nickname",nickname);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        socket.emit(ClientActions.JOIN_ROOM,data);

    }

    public void disconnect(){
        socket.disconnect();
    }

    public void leaveRoomRequest() throws JSONException {
        JSONObject data = new JSONObject();
        data.put("roomID", token.getRoomID());
        socket.emit(ClientActions.LEAVE_ROOM, data);
    }

    public void startGameRequest() {
        socket.emit(ClientActions.START_GAME,token.getToken());
    }

    public void sendBidResponse(int value) {
        JSONObject data = new JSONObject();
        try {
            data.put("token",token.getToken());
            data.put("bid",value);
            socket.emit(ClientActions.BID_RESPONSE,data);
            this.rootController.hideBidHUD();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void sendCardResponse(String card) {
        JSONObject data = new JSONObject();
        try {
            data.put("token",token.getToken());
            data.put("card",card);
            socket.emit(ClientActions.CARD_RESPONSE,data);
            this.rootController.hideCardHud();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void createGameRequest(String roomID,String nickname){
        JSONObject data = new JSONObject();
        try{
            data.put("roomID",roomID);
            data.put("nickname",nickname);
            socket.emit(ClientActions.CREATE_ROOM,data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void canRunGame() {
        socket.emit(ClientActions.GOT_PLAYERS, token.getToken());
    }
}

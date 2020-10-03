package com.mygdx.game.businessLayer.networking.dto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class NetworkDTO {
    public static class Bids {
        private List<Bid> bids;

        public class Bid{
            int made;
            boolean bidMade;
            int forbidden;
            String nickname;
            int bids;

            public Bid(JSONObject bidJSON) throws JSONException {
                made = bidJSON.getInt("made");
                forbidden = bidJSON.getInt("forbidden");
                nickname = bidJSON.getString("nickname");
                bids = bidJSON.getInt("bids");
                bidMade = bidJSON.getBoolean("bidMade");
            }

            public int getMade() {
                return made;
            }

            public boolean isBidMade() {
                return bidMade;
            }

            public int getForbidden() {
                return forbidden;
            }

            public String getNickname() {
                return nickname;
            }

            public int getBidValue() {
                return bids;
            }

            @Override
            public String toString() {
                return nickname + " " + made + "/" + bids + " " + "forbidden: " + forbidden + " " + bidMade;
            }
        }

        public Bids(JSONObject bidsResponseJSON) throws JSONException {
            this.bids = new LinkedList<>();
            JSONArray bidsJSON = bidsResponseJSON.getJSONArray("bidRequest");
            for(int i = 0; i< bidsJSON.length(); i++) {
                JSONObject bidJSON = bidsJSON.getJSONObject(i);
                Bid bid = new Bid(bidJSON);
                bids.add(bid);
            }
        }

        public List<Bid> getBids() {
            return bids;
        }

        public Bid getFirstToBid(){
            for(Bid bid : bids){
                if(bid.isBidMade() == false)
                    return bid;
            }
            return null;
        }


    }
    public static class Cards {
        private List<String> cards;

        public Cards(JSONObject cardsJSON) throws JSONException {
            this.cards = new LinkedList<>();

            String cardsStr = cardsJSON.getString("cards");
            this.cards = Arrays.asList(cardsStr.split(" "));
        }

        public List<String> getCards() {
            return cards;
        }


        //todo: add atu;
    }
    public class Error {} //TODO: Implement this
    public static class Lobby {
        String owner;
        int capacity;
        String roomID;
        List<String> players;

        public Lobby(JSONObject lobbyJSON) throws JSONException {
            owner = lobbyJSON.getString("owner");
            roomID = lobbyJSON.getString("roomID");
            capacity = lobbyJSON.getInt("capacity");
            players = new LinkedList<>();

            JSONArray playersJSON = lobbyJSON.getJSONArray("players");
            for(int i = 0 ; i< playersJSON.length(); i++){
                JSONObject playerJSON = playersJSON.getJSONObject(i);
                String name = playerJSON.getString("name");
                players.add(name);
            }

        }

        public String getOwner() {
            return owner;
        }

        public int getCapacity() {
            return capacity;
        }

        public String getRoomID() {
            return roomID;
        }

        public List<String> getPlayers() {
            return players;
        }

        @Override
        public String toString() {
            return "Lobby{" +
                    "owner='" + owner + '\'' +
                    ", capacity=" + capacity +
                    ", roomID='" + roomID + '\'' +
                    ", players=" + players +
                    '}';
        }
    }
    public static class Player {
        String name;

        public Player(JSONObject playerJSON) throws JSONException {
            this.name = playerJSON.getString("name");
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
    public static class Room {
        String owner;
        int players;
        String roomID;
        int capacity;


        public Room(JSONObject roomsJSON) throws JSONException {
            owner = roomsJSON.getString("owner");
            players = roomsJSON.getInt("players");
            roomID = roomsJSON.getString("roomID");
            capacity = roomsJSON.getInt("capacity");
        }


        @Override
        public String toString() {
            return roomID + "\t\t" + players+"/"+capacity + "\t\t" + owner;
        }

        public String getOwner() {
            return owner;
        }

        public int getPlayers() {
            return players;
        }

        public String getRoomID() {
            return roomID;
        }

        public int getCapacity() {
            return capacity;
        }
    }
    public static class RoomsResponse {
        private List<Room> rooms;
        private int numOfRooms;

        public RoomsResponse(JSONObject roomsData) throws JSONException {
            rooms = new LinkedList<>();
            JSONArray roomsArray = roomsData.getJSONArray("rooms");
            for(int i = 0; i< roomsArray.length(); i++){
                JSONObject temp = roomsArray.getJSONObject(i);
                Room localRoom = new Room(temp);
                rooms.add(localRoom);
            }

            numOfRooms = roomsData.getInt("num_of_rooms");
        }

        public List<Room> getRooms() {
            return rooms;
        }

        public int getNumOfRooms() {
            return numOfRooms;
        }


    }
    public static class Table {
        public class PlayerStatus{
            String nickname;
            String card;

            public PlayerStatus(JSONObject playerStatusJSON) throws JSONException {
                nickname = playerStatusJSON.getString("nickname");
                card = playerStatusJSON.getString("card");
            }

            public String getNickname() {
                return nickname;
            }

            public String getCard() {
                return card;
            }

            @Override
            public String toString() {
                return nickname + " " + card;
            }
        }

        List<PlayerStatus> table;

        public Table(JSONObject tableJSON) throws JSONException {
            table = new LinkedList<>();
            JSONArray tableArray = tableJSON.getJSONArray("cardRequest");
            for(int i = 0; i< tableArray.length(); i++){
                JSONObject playerStatusJSON = tableArray.getJSONObject(i);
                PlayerStatus playerStatus = new PlayerStatus(playerStatusJSON);
                table.add(playerStatus);
            }
        }

        public PlayerStatus getFirstToPutCard(){
            for(PlayerStatus ps : this.table){
                if(ps.getCard().equals("null"))
                    return ps;
            }
            return null;
        }

        public List<PlayerStatus> getPlayersStatus() {
            return table;
        }
    }
    public static class Token {
        String nickname,roomID,playerID;
        JSONObject tokenCopy;
        public Token(JSONObject tokenJSON) throws JSONException {
            tokenCopy = tokenJSON;
            nickname = tokenJSON.getString("nickname");
            roomID = tokenJSON.getString("roomID");
            playerID = tokenJSON.getString("playerID");
        }



        public JSONObject getToken(){
            return tokenCopy;
        }

        @Override
        public String toString() {
            return "nickname='" + nickname + '\'' +
                    ", roomID='" + roomID + '\'' +
                    ", playerID='" + playerID;
        }

        public String getNickname() {
            return nickname;
        }

        public String getRoomID() {
            return roomID;
        }

        public String getPlayerID() {
            return playerID;
        }
    }
}

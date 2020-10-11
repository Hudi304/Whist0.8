package com.mygdx.game.businessLayer.networking.actions;

public enum ClientActions {


    /**
     * CLIENT ACTIONS  = all actions made by the client
     * where to find? in startClient, in every socket.emit(...);
     * synchronized with the server...any change in client without server approval can be FATAL!
     * for every new action, create new final String in order to avoid type errors.
     */
    ;
    public static final String GOT_CARDS = "GOT_CARDS";
    public static final String BID_RESPONSE = "BID_RESPONSE" ;
    public static final String LOGIN = "LOGIN" ;
    public static final String CREATE_ROOM = "CREATE_ROOM";
    public static final String JOIN_ROOM = "JOIN_ROOM";
    public static final String ROOMS_REQUEST = "ROOMS_REQUEST";
    public static final String LEAVE_ROOM = "LEAVE_ROOM";
    public static final String START_GAME = "START_GAME";
    public static final String CARD_RESPONSE = "CARD_RESPONSE";
    public static final String GOT_PLAYERS = "GOT_PLAYERS";
    public static final String GOT_BID_STATUS = "GOT_BID_STATUS";
    public static final String GOT_TABLE_STATUS = "GOT_TABLE_STATUS";
    public static final String GOT_WINNER = "GOT_WINNER";
    public static final String GOT_SCORE = "GOT_SCORE";
    public static final String PING = "PING";
}

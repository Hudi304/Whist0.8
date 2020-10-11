var RoomRepo = require('../repositories/RoomRepo');
var Player  = require('../model/Player');
const TokenRepo = require('../repositories/TokenRepo');
const Token = require('../model/Token')

const roomState = require('../gameLogics/RoomState');
const {BID_REQUEST,CARD_REQUEST,CONNECTED,ERROR,GET_CARDS,KICK_ALL,LOBBY,PLAYERS_REQUEST,ROOMS_REQUEST,TOKEN, ROOMS_RESPONSE} = require('./ServerActions');
const { START_GAME } = require('./ClientActions');
const { HAND_ENDED } = require('../gameLogics/GameState');




module.exports = class RootService{
    constructor(roomRepo = null){
        this.roomRepo = roomRepo;
        if(roomRepo == null)
            this.roomRepo = new RoomRepo();

        this.tokenRepo = new TokenRepo();
    }

    /**
     * 
     * @param {string} playerID -> token of the connected client 
     */
    connect(playerID){ // used

        let localToken = Token(playerID,null,null);

        try{
            this.tokenRepo.addToken(localToken);
            //console.log(this.tokenRepo.getAll());
            return CONNECTED;
        }
        catch(err){
           throw err;
        }
    };

    /**
     * Create the room
     * if room exists, throw error
     * Create the player object
     * Add the player to the room
     * Update the player token
     * return ACTION.LOBBY
     * 
     * @param {*} playerID 
     * @param {*} playerName 
     * @param {*} roomID 
     */

    createRoom(playerID,playerName,roomID){ // used
        let localRoom = null;
        try{
            localRoom = this.roomRepo.addRoom(roomID);
        }catch (err){
            console.log(err);
            throw err;
        }

        if(!localRoom)
        {
            console.log("Can't create room");
            throw "Can't create room!";
        }
        
        let localPlayer =Player(playerID,playerName);        
        localRoom.addPlayer(localPlayer);


        let token = this.tokenRepo.getToken(playerID);

        token.roomID = localRoom.id;
        token.nickname = playerName;

        return LOBBY;
      
    };

    /**
    *Return the lobby data for a specific room
    * if roomID is invalid, will throw error
    * if the room does not exist, will throw error;
    * return the json object with: roomID, ownerName, players, size; 
    * @param {*} roomID 
    */
    lobbyData(roomID){ //used
        let localRoom = this.roomRepo.getRoom(roomID);

        if(!localRoom)
            throw "Room can't be found"

        return localRoom.json(true);
    }

    /**
     * Search for the room
     * if not exist, throw error
     * Create the player instace
     * Add player to the room
     * if can't add, throws error
     * Updates player token
     * return true;
     * @param {*} playerID 
     * @param {*} playerName 
     * @param {*} roomID 
     */
    joinRoom(playerID,playerName,roomID){ // used
        
        let foundRoom = this.roomRepo.getRoom(roomID);
        if(!foundRoom)
            throw "Room not found!";

        let localPlayer = Player(playerID,playerName);

        try{
            foundRoom.addPlayer(localPlayer);
            
        }catch(err)
        {
            //console.log(err);
            throw err;
        }
        

        let token = this.tokenRepo.getToken(playerID);
        token.roomID = foundRoom.id;
        token.nickname = playerName;
        
        

        return LOBBY;
    };


    /**
     * Search for the room
     * if room doesn't exist, throw error
     * Remove the player from the room
     * Verify the state of the room
     * if the state is active, return LOBBY;
     * if the state is canceled, return KICK_OUT;
     * @param {*} playerID 
     */
    leaveRoom(playerID,roomID){ // USED
        
        let foundRoom = this.roomRepo.getRoom(roomID);
        if(!foundRoom)
            throw "Room not found!";
        
        
        
        let token = this.tokenRepo.getToken(playerID);
        token.roomID = null;
        token.nickname = null;

        let playerRemoved = foundRoom.removePlayer(playerID);
        if(!playerRemoved)
            throw "Player was not in the room!";

        
        

        if(foundRoom.getState() === roomState.ACTIVE)
            return LOBBY;     
        else
            return KICK_ALL;
        
    }

    kickAll(roomID){ // used
        let foundRoom = this.roomRepo.getRoom(roomID);

        if(!foundRoom)
            throw "Room not found!";


        let players = foundRoom.players;

        for(let p of players){
            let token = this.tokenRepo.getToken(p.id);

            token.roomID = null;
            token.nickname = null;
        }

        foundRoom.clearRoom();
        let id = foundRoom.id;
        this.roomRepo.removeRoom(foundRoom.id);

        return {
            roomID: id,
            players: players.map(p => p.id)

        };

    };

    // manageStates({type,payload}){ --DEPRECATED
    //     try{
    //         switch(type){

    //             case CONNECTED:
    //                 return payload.token;

    //             case LOBBY:
    //                 return this.lobbyData(payload.roomID);

    //             case ROOMS_RESPONSE:
    //                 return {
    //                     num_of_rooms: payload.rooms.length,
    //                     rooms: payload.rooms.map(r => r.json()),
    //                 }
                    
    //             case KICK_ALL:
    //                 return this.kickAll(payload.roomID);

    //             case BID_REQUEST:
    //                 return this.sendBidStatus(payload.roomID);
                
    //             case CARD_REQUEST:
    //                 return this.sendCardStatus(payload.roomID);

    //             case PLAYERS_REQUEST:
    //                 return this.createPlayerResponse(payload.playersData);
    //             case GET_CARDS:
    //                 return payload.cards;

    //             case ERROR:
    //                 return payload.message;
                    
    //         };
    //     }catch(err){
    //         console.log(err);
    //         throw err;
    //     }
    // }
    
    // createPlayerResponse(playersData){ --DEPRECATED
    //     let data = playersData.map(pd => pd.name);
    //     return {players: data.map(p => {return {name: p}})};
    // }

    /**
    * 
    * @param {string} playerID -> token of the connected client
    */
    disconnect(playerID){ // used
    
        this.tokenRepo.removeToken({playerID});
        return true;
        
    };


    getRooms(){ // used
        let rooms = this.roomRepo.getAll();
        let roomsData =  {
            num_of_rooms: rooms.length,
            rooms: rooms.map(r => r.json()), 
        }
        return roomsData;
        
    }


    getToken(playerID){ // used
        return this.tokenRepo.getToken(playerID);
    }


    //started here

    //actions for engine

    startGame(roomID){ // used
        //console.log(token.roomID);
        let room = this.roomRepo.getRoom(roomID);
        
        

        if(room.getSize() < 2)
            throw "You need more players to start the game";
        room.startGame();

        
    }

    drawCardsForRoom(roomID){ // used
        let engine = this.roomRepo.getRoom(roomID).getEngine();
        if(!engine)
            throw "Engine not initialized! -- server error";

        engine.drawCardsForPlayers();
    }


    updateBid(token,bid){ // used
        let engine = this.roomRepo.getRoom(token.roomID).getEngine();
        if(!engine)
            throw "Engine not initialized! -- server error";


        return engine.updateBid(token.nickname,bid);

    }

    updateTable(token,card){ // used
        let engine = this.roomRepo.getRoom(token.roomID).getEngine();
        if(!engine)
            throw "Engine not initialized! -- server error";


        return engine.updateTable(token.nickname,card);
    }

    resetTableForHand(roomID){ //used
        let engine = this.roomRepo.getRoom(roomID).getEngine();
        if(!engine)
            throw "Engine not initialized! -- server error";

        engine.resetTableForHand();
    }
    
    calculateScore(roomID){ // used
        let engine = this.roomRepo.getRoom(roomID).getEngine();
        if(!engine)
            throw "Engine not initialized! -- server error";

        engine.calculateScore();
    }

    moveToNextRound(roomID){ //used
        let engine = this.roomRepo.getRoom(roomID).getEngine();
        if(!engine)
            throw "Engine not initialized! -- server error";

        engine.moveToNextRound();
    }

    //init for engine

    initBidsForRoom(roomID){ // used
        let engine = this.roomRepo.getRoom(roomID).getEngine();
        if(!engine)
            throw "Engine not initialized! -- server error";

        engine.initBidStatus();
    }

    initTableForRoom(roomID){ // used
        let engine = this.roomRepo.getRoom(roomID).getEngine();
        if(!engine)
            throw "Engine not initialized! -- server error";

        engine.initTableStatus();
    }

    //get status of 

    getCards(roomID){ // used
        let engine = this.roomRepo.getRoom(roomID).getEngine();
        if(!engine)
            throw "Engine not initialized! -- server error";

        let cards = engine.getCards();
        return cards;


    }

    getBids(roomID){ //used
        let engine = this.roomRepo.getRoom(roomID).getEngine();
        if(!engine)
            throw "Engine not initialized! -- server error";

        return engine.getBidStatus();

    }

    getTable(roomID){ //used
        let engine = this.roomRepo.getRoom(roomID).getEngine();
        if(!engine)
            throw "Engine not initialized! -- server error";

        return engine.getTableStatus();
    }
    getPlayersForGame(roomID){ //used
        let engine = this.roomRepo.getRoom(roomID).getEngine();
        if(!engine)
            throw "Engine not initialized! -- server error";
        
        let playersData = engine.getPlayersOrder();
        playersData = playersData.map(p=> {return  {name: p.name}});
        return playersData;
    }

    getWinnerForRound(roomID){ //used
        let engine = this.roomRepo.getRoom(roomID).getEngine();
        if(!engine)
            throw "Engine not initialized! -- server error";

        return engine.getWinnerStatus();
    }

    getScoreForRoom(roomID){ //used
        let engine = this.roomRepo.getRoom(roomID).getEngine();
        if(!engine)
            throw "Engine not initialized! -- server error";

        return engine.getTotalScore();
    }

    getFinalTable(roomID){ //used
        let engine = this.roomRepo.getRoom(roomID).getEngine();
        if(!engine)
            throw "Engine not initialized! -- server error";

        return engine.getFinalTable();
    }
    //received status of
    receivedGotBids(token){ // used
        let engine = this.roomRepo.getRoom(token.roomID).getEngine();
        if(!engine)
            throw "Engine not initialized! -- server error";

        return engine.gotBidsData(token.nickname);
    }

    receivedGotCards(token){ // used
        let engine = this.roomRepo.getRoom(token.roomID).getEngine();
        if(!engine)
            throw "Engine not initialized! -- server error";

        return engine.gotCardData(token.nickname);
    }

    receivedGotPlayers(token){ // used
        let engine = this.roomRepo.getRoom(token.roomID).getEngine();
        if(!engine)
            throw "Engine not initialized! -- server error";

        let result = engine.gotPlayerData(token.nickname);
        
        return result;
    }

    receivedGotTable(token){ // used
        let engine = this.roomRepo.getRoom(token.roomID).getEngine();
        if(!engine)
            throw "Engine not initialized! -- server error";

        let result = engine.gotTableData(token.nickname);
        
        return result;
    }

    receivedGotWinner(token){ // used
        let engine = this.roomRepo.getRoom(token.roomID).getEngine();
        if(!engine)
            throw "Engine not initialized! -- server error";

        let result = engine.gotWinnerData(token.nickname);
        
        return result;
    }

    receivedGotScore(token){ //used
        let engine = this.roomRepo.getRoom(token.roomID).getEngine();
        if(!engine)
            throw "Engine not initialized! -- server error";

        let result = engine.gotScoreData(token.nickname);
        
        return result;
    }

    //engine verifications
    isBiddingFinished(roomID){ // used
        let engine = this.roomRepo.getRoom(roomID).getEngine();
        if(!engine)
            throw "Engine not initialized! -- server error";

        return engine.isBiddingFinished();
    }

    isHandFinished(roomID){ // used
        let engine = this.roomRepo.getRoom(roomID).getEngine();
        if(!engine)
            throw "Engine not initialized! -- server error";

        return engine.isHandFinished();
    }

    isRoundFinished(roomID){ // used
        let engine = this.roomRepo.getRoom(roomID).getEngine();
        if(!engine)
            throw "Engine not initialized! -- server error";

        return engine.isRoundFinished();
    }

    isGameFinished(roomID){ // used
        let engine = this.roomRepo.getRoom(roomID).getEngine();
        if(!engine)
            throw "Engine not initialized! -- server error";

        return engine.isGameFinished();
    }
    //ended here

    
}


//const createAction = (type,payload) => ({type,payload}); --DEPRECATED


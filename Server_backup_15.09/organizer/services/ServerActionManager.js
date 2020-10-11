``
var client = require('./ClientActions');
var server = require('./ServerActions');
var root = require('./rootService');
var emmiter = require('../socketOperations/emmiterType');
const { LOBBY, TOKEN, KICK_ALL, CONNECTED, PLAYERS_REQUEST, CARD_REQUEST } = require('./ServerActions');



module.exports = class ServerActionManager{
    constructor(rootService){
       this.root = rootService; 
    }

    processClientRequests({type,payload}){
        switch(type){
            case client.LOGIN:
                return this.__login(payload);
            case client.CREATE_ROOM:
                return this.__createRoom(payload);
            case client.ROOMS_REQUEST:
                return this.__roomsRequest(payload);
            case client.JOIN_ROOM:
                return this.__joinRoom(payload);
            case client.LEAVE_ROOM:
                return this.__leaveRoom(payload);
            case client.DISCONNECT:
                return this.__disconnect(payload);
            case client.START_GAME:
                return this.__startGame(payload);
            case client.GOT_CARDS:
                return this.__gotCards(payload);
            case client.BID_RESPONSE:
                return this.__bidResponse(payload);
            case client.CARD_RESPONSE:
                return this.__cardResponse(payload);
            case client.GOT_BID_STATUS:
                return this.__gotBidStatus(payload);
            case client.GOT_TABLE_STATUS:
                return this.__gotTableStatus(payload);
            case client.GOT_WINNER:
                return this.__gotWinnerStatus(payload);
            case client.GOT_PLAYERS:
                return this.__gotPlayersStatus(payload);
            case client.GOT_SCORE:
                return this.__gotScoreStatus(payload);
            case client.PING:
                return this.__sendPingStatus(payload);
        }

    }

    __login(payload){
            //payload: {id: ...};
            //need to send socket id just for the player
            //or just an error for the player

            //validate payload
            if(payload.id == null)
                throw "Payload for LOGIN is not correct. id is null";

            let command = new Command();
            try{
            let action = this.root.connect(payload.id);
            if(action === server.CONNECTED)
            {
                let combinedAction = this.__createTokenAction(payload.id);
                command.addCombinedAction(combinedAction);
                return command;
            }
            }catch(err){
                let combinedAction = this.__createErrorCombinedAction(payload.id,errorType.LOGIN_ERROR,err);
                command.addCombinedAction(combinedAction);
                return command;
            }
    }

    __createRoom(payload){
        //payload: {id: ..., roomID: ..., nickname: ...}
        //first, send the updated token to the player
        //second, send the lobby data for all players in the room

        //validate payload
        if(payload.id == null || payload.roomID == null || payload.nickname == null)
            throw "Payload for CREATE_ROOM is not correct!";

        let command = new Command();
       
        let combinedAction = null;
        
        try{
            let lobbyAction = this.root.createRoom(payload.id,payload.nickname,payload.roomID);
            if(lobbyAction === server.LOBBY){
            combinedAction = this.__createLobbyAction(payload.roomID);
            command.addCombinedAction(combinedAction);
            }
        }catch(err){

            combinedAction = this.__createErrorCombinedAction(payload.id,errorType.CREATE_ROOM_ERROR,err);
            command.addCombinedAction(combinedAction);
            return command; 
        }


       
        combinedAction = this.__createTokenAction(payload.id);
        command.addCombinedAction(combinedAction);
        return command;

    }

    __roomsRequest(payload){
        // payload: {id: ...}
        // return to one all the rooms data

        if(payload.id == null)
            throw "Payload for ROOMs_REQUEST is not correct. id is null";

        
        let combinedAction = this.__createRoomResponseAction(payload.id);

        let command = new Command([combinedAction]);
        return command;
        
    }

    __joinRoom(payload){
        //payload: {id: ..., roomID: ..., nickname: ...}
        //first, send the updated token to the player
        //second, send the lobby data for all players in the room

        //validate payload
        if(payload.id == null || payload.roomID == null || payload.nickname == null)
            throw "Payload for JOIN_ROOM is not correct!";

       let command = new Command();
        
        try{
            let lobbyAction = this.root.joinRoom(payload.id,payload.nickname,payload.roomID);
            if(lobbyAction === server.LOBBY){
                let combinedAction = this.__createLobbyAction(payload.roomID);
                command.addCombinedAction(combinedAction);
            }
        }catch(err){
            let combinedAction = this.__createErrorCombinedAction(payload.id,errorType.JOIN_ROOM_ERROR,err);
            command.addCombinedAction(combinedAction);
            return command; 
        }


        
        let combinedAction = this.__createTokenAction(payload.id);
        command.addCombinedAction(combinedAction);
        return command;

    }

    __leaveRoom(payload){
        //payload = {id: ..., roomID: ...}
        //first...send the new token to player
        // if the game is just active, ... send to all room lobby data
        // if the game is running, ... send to all kickAll and their respective token
        
        //validate payload
        if(payload.id === null || payload.roomID == null)
            throw "Payload for LEAVE_ROOM is not correct!";
        
        
        let command = new Command();
        let combinedAction = null;

        try{
            let leaveRoomAction = this.root.leaveRoom(payload.id,payload.roomID);
            //send the token
            combinedAction = this.__createTokenAction(payload.id);
            command.addCombinedAction(combinedAction);


            if(leaveRoomAction === LOBBY){
                
                combinedAction = this.__createLobbyAction(payload.roomID);
                command.addCombinedAction(combinedAction);
                return command;
            }
            else if(leaveRoomAction === KICK_ALL){
                let kickAllAction = this.root.kickAll(payload.roomID);
                let playersID = kickAllAction.players;
                for(let playerID of playersID){
                    combinedAction = this.__createTokenAction(playerID);
                    command.addCombinedAction(combinedAction);
                }

                
                combinedAction = this.__createKickAllAction(kickAllAction.roomID);
                command.addCombinedAction(combinedAction);
                return command;

            }

        }catch(err){   
           
            combinedAction = this.__createErrorCombinedAction(payload.id,errorType.LEAVE_ROOM_ERROR,err);
            command.addCombinedAction(combinedAction);
            return command;   
        }

    }

    __disconnect(payload){
        // payload = {id: ...};
        // if the player was in a room,  give __leaveRoom command
        // delete the token

        let token = this.root.getToken(payload.id);
        if(!token)
            return null;
        let roomID = token.roomID;
        payload = {...payload, roomID};
        let command = null;
        if(payload.roomID != null)
            command = this.__leaveRoom(payload);

        this.root.disconnect(payload.id);
        
        return command;
        
    }

    __startGame(payload){
        //payload: {id: ..., roomID: ...}
        //send a list with player order
        

        //validate payload
        if(payload.id == null || payload.roomID == null)
            throw "Payload for START_GAME is not correct!";

        let command = new Command();

        try{
            this.root.startGame(payload.roomID);
            command.addCombinedAction(this.__createPlayerRequestAction(payload.roomID));
            return command;
        }catch(err){
            console.log(err);
            return command;
        }

    }

    __gotPlayersStatus(payload){
        if(payload.token == null)
            throw "Payload for GOT_PLAYERS is not correct!";
    
        let action = this.root.receivedGotPlayers(payload.token);
        let command = new Command();
        if(action)
        {
            this.root.drawCardsForRoom(payload.token.roomID);
            let playersCards = this.root.getCards(payload.token.roomID);
            

            for(let id of Object.keys(playersCards)){
                if(id != "atu")
                    command.addCombinedAction(this.__createGetCardsAction(id,{cards: playersCards[id], atu: playersCards["atu"] }));
            }
            
           
        }

        return command;
        
    }

    __gotCards(payload){
        //payload: {token: ...};
        let action = this.root.receivedGotCards(payload.token);
        if(action === true){
            this.root.initBidsForRoom(payload.token.roomID);
    
            let command = new Command();
            command.addCombinedAction(this.__createBidStatusAction(payload.token.roomID));
            return command;
        
        }
        return null;
    }

    __gotBidStatus(payload){
        //payload: {token: ...};
        //this is when player received bid
        //if all player received bid_status
        // if there are bids to place...send bid request
        // if not... send table status;
        if(payload.token == null)
            throw "Payload for GOT_BID_STATUS is null";
        let action = this.root.receivedGotBids(payload.token);
        let command = new Command();
        if(action === true){
            
            if(!this.root.isBiddingFinished(payload.token.roomID))
                command.addCombinedAction(this.__createBidRequestAction(payload.token.roomID));
            else{
               
                // init table status;
                //send table data;
                this.root.initTableForRoom(payload.token.roomID);
                command.addCombinedAction(this.__createTableStatusAction(payload.token.roomID));
            }
        }
        return command;
        

    }
    __bidResponse(payload){
        //payload: {token: ..., bid: ...};
        //if bid is not correct, send error and bid request
        //if bid is correct, update bid and then:
        // 1. send bid request if there are more players to bid;
        // 2. send card request if all players placed their bids;

        if(payload.token == null || payload.bid == null)
            throw "Payload in BID_RESPONSE is invalid";

        let action = this.root.updateBid(payload.token,payload.bid);
        let command = new Command();
        if(action)
        {
            //bid is good...move to next step
            command.addCombinedAction(this.__createBidStatusAction(payload.token.roomID));
        }
        else
        {
            command.addCombinedAction(this.__createErrorCombinedAction(payload.token.playerID,errorType.BID_ERROR, "Bid is not correct!Retry!"));
            command.addCombinedAction(this.__createBidStatusAction(payload.token.roomID));
        }

        return command;
    }

    __gotTableStatus(payload){
        //payload: {token: ...}
        //if not all the players got the new tableStatus -> return null;
        //if there are more players to put a card, send a card request
        //else, send the winner of the hand;

        if(payload.token == null)
            throw "Payload for GOT_TABLE_STATUS is not correct!";
        
        let action = this.root.receivedGotTable(payload.token);
        let command = new Command();

        if(action === true){
            if(!this.root.isHandFinished(payload.token.roomID)){
                command.addCombinedAction(this.__createCardRequestAction(payload.token.roomID));
            }
            else
                command.addCombinedAction(this.__createWinnerAction(payload.token.roomID));

        }

        return command;
    }
    __cardResponse(payload){
        //payload: {token: ..., card: ...};
        //if card is not correct... send error and tableStatus
        //if card is correct -> updateCard and send tableStatus

        if(payload.token == null || payload.card == null)
            throw "Payload for CARD_RESPONSE is not correct";
        
        let command = new Command();
        let action = this.root.updateTable(payload.token,payload.card);
        if(!action)
            command.addCombinedAction(this.__createErrorCombinedAction(payload.token.playerID,errorType.CARD_ERROR,"The card is not correct!"));
        
        command.addCombinedAction(this.__createTableStatusAction(payload.token.roomID));

        return command;

    }   
    __gotWinnerStatus(payload){
        //payload: {token: ...};
        //if not all players got the new winner -> return null;
        //if players have cards in their hands, send bid status, and new order of the table
        //if players dont have any cards, send the updated score.
        if(payload.token == null){
            throw "Payload for GOT_WINNER is not correct";
        }

        let action = this.root.receivedGotWinner(payload.token);
        let command = new Command();

        if(action === true){
            
            if(this.root.isRoundFinished(payload.token.roomID)){
                if(this.root.isGameFinished(payload.token.roomID))
                {
                    console.log("GAME FINISHED");
                    command.addCombinedAction(this.__createEndGameAction(payload.token.roomID));
                }

                else{
                    console.log("GET SCORE");
                    
                    this.root.calculateScore(payload.token.roomID);
                    command.addCombinedAction(this.__createScoreStatusAction(payload.token.roomID));
                }
            }

            else{
                //TODO: update Bids;
                // resetTable
                // emit BID_STATUS
                this.root.resetTableForHand(payload.token.roomID);
                command.addCombinedAction(this.__createBidStatusAction(payload.token.roomID));
            }
        }

        return command;

    }

    __gotScoreStatus(payload){ // this is only for finished round
        if(payload.token == null)
            throw "Payload for GOT_SCORE is not correct!";
        
        let action = this.root.receivedGotScore(payload.token);
        let command = new Command();
        if(action){
            this.root.moveToNextRound(payload.token.roomID);
            this.root.drawCardsForRoom(payload.token.roomID);
            let playersCards = this.root.getCards(payload.token.roomID);
            

            for(let id of Object.keys(playersCards)){
                if(id != "atu")
                    command.addCombinedAction(this.__createGetCardsAction(id,{cards: playersCards[id], atu: playersCards["atu"] }));
            }
        }
        return command;
    }

    __sendPingStatus(payload){
        let command = new Command();
        command.addCombinedAction(this.__createPingResponseAction(payload.id));
        return command;
    }

    __createPingResponseAction(to){
        let type = emmiter.ONE;
        let data = {};
        let pingAction = {type: server.PING,payload: data}
        return createCombinedAction(type,to,pingAction);
    }

    __createErrorCombinedAction(to,errorType,message){
        let type = emmiter.ONE;
        let errorData = createErrorData(errorType,message);
        let errorAction = {type:server.ERROR,payload: errorData};
        return createCombinedAction(type,to,errorAction);
    }

    __createTokenAction(to){
        let tokenData = this.root.getToken(to);
        let tokenAction = {type: "TOKEN", payload: {token: tokenData}};
        let type = emmiter.ONE;
        return createCombinedAction(type,to,tokenAction);
    }

    __createLobbyAction(to){
        let lobbyData = this.root.lobbyData(to);
        let lobbyAction = {type:server.LOBBY,payload: {lobby: lobbyData}};
        let type = emmiter.ALL;
        return createCombinedAction(type,to,lobbyAction);
    }

    __createRoomResponseAction(to){

        let roomsData = this.root.getRooms();
        let roomsAction = {type:server.ROOMS_RESPONSE,payload: {roomsData}};
        let type = emmiter.ONE;
        
        return createCombinedAction(type,to,roomsAction);
    }

    __createKickAllAction(to){
        
        let type = emmiter.ALL;
        let kickAllAction = {type: server.KICK_ALL};
        return createCombinedAction(type,to,kickAllAction);
              
    }

    __createPlayerRequestAction(to){
        let type = emmiter.ALL;
        let data = this.root.getPlayersForGame(to);
        let playerRequestAction = {type: server.PLAYERS_REQUEST, payload: data};
        return createCombinedAction(type,to,playerRequestAction);
    }

    __createGetCardsAction(to,data){
        let type = emmiter.ONE;
        let getCardsAction = {type: server.GET_CARDS, payload: data};
        return createCombinedAction(type,to,getCardsAction);
    }

    __createBidStatusAction(to){
        let data = this.root.getBids(to);
        console.log(data);
        data = {bidRequest: data};
        let type = emmiter.ALL;
        let bidStatusAction = {type: server.BID_STATUS, payload: data};
        return createCombinedAction(type,to,bidStatusAction);
    }
    __createBidRequestAction(to){
        let type = emmiter.ALL;
        let bidRequestAction = {type: server.BID_REQUEST};
        return createCombinedAction(type,to,bidRequestAction);
    }

    __createTableStatusAction(to){
        let data = this.root.getTable(to);
        data = {cardRequest: data};
        let type = emmiter.ALL;
        let tableStatusAction = {type: server.TABLE_STATUS,payload:data};
        return createCombinedAction(type,to,tableStatusAction);
    }
    __createCardRequestAction(to){

        let type = emmiter.ALL;
        let cardRequestAction = {type: server.CARD_REQUEST};
        return createCombinedAction(type,to,cardRequestAction);
    }

    __createWinnerAction(to){
        let winnerData = this.root.getWinnerForRound(to);
        winnerData = {winner: winnerData};
        let type = emmiter.ALL;
        let winnerRequestAction = {type: server.WINNER, payload:winnerData};
        return createCombinedAction(type,to,winnerRequestAction);
    }

   __createScoreStatusAction(to){
       let scoreData = this.root.getScoreForRoom(to);
       scoreData = {score: scoreData};
       let type = emmiter.ALL;
       let scoreAction = {type: server.SCORE_STATUS,payload: scoreData};
       return createCombinedAction(type,to,scoreAction);
   }

   __createEndGameAction(to){
       let tableData = this.root.getFinalTable(to);
       tableData = {table: tableData};
       let type = emmiter.ALL;
       let endGameAction = {type: server.END_GAME, payload: tableData};
       return createCombinedAction(type,to,endGameAction);
   }
}

const createCombinedAction = (type,to,action)=>{
    return {
        type,to,action
    };
}

class Command{
    constructor(list = []){
        this.__list = list;
    }

    addCombinedAction(action){
        this.__list.push(action);
    }

    popCombinedAction(){
        return this.__list.shift();
    }

    isEmpty(){
        return this.__list.length === 0;
    }

    containsErrors(){
        for(let c of this.__list){
            if(c.action.type === server.ERROR)
                return true;
        }

        return false;
    }

    containsSpecificType(type){
        for(let c of this.__list){
            if(c.action.type === type)
                return true;
        }
        return false;
    }

    length(){
        return this.__list.length;
    }
    
}


const errorType = {
    LOGIN_ERROR : "LOGIN_ERROR",
    CREATE_ROOM_ERROR: "CREATE_ROOM_ERROR",
    JOIN_ROOM_ERROR: "JOIN_ROOM_ERROR",
    LEAVE_ROOM_ERROR: "LEAVE_ROOM_ERROR",
    START_GAME_ERROR: "START_GAME_ERROR",
    BID_ERROR: "BID_ERROR",
    CARD_ERROR: "CARD_ERROR",
}

const createErrorData = (type,message) => {return {error: {type,message}}};
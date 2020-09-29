var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var RoomRepo = require('./organizer/repositories/RoomRepo');
var Room = require('./organizer/model/Room');
var Player = require('./organizer/model/Player');
var Service = require('./organizer/services/rootService');
const {BID_REQUEST,CARD_REQUEST,CONNECTED,ERROR,GET_CARDS,KICK_ALL,LOBBY,PLAYERS_REQUEST,ROOMS_RESPONSE,TOKEN, SCORE_STATUS} = require('./organizer/services/ServerActions');

const { LOGIN,LEAVE_ROOM,JOIN_ROOM,DISCONNECT,START_GAME,GOT_PLAYERS,GOT_CARDS,CREATE_ROOM,BID_RESPONSE,CARD_RESPONSE,ROOMS_REQUEST,GOT_BID_STATUS,GOT_TABLE_STATUS,GOT_WINNER, GOT_SCORE} = require('./organizer/services/ClientActions');
const emmiterTypes = require('./organizer/socketOperations/emmiterType');
const ServerActionManager = require('./organizer/services/ServerActionManager');



let roomTestList = [];
roomTestList.push(new Room("test1"));
roomTestList.push(new Room("test2"));
roomTestList.push(new Room("test3"));
roomTestList.push(new Room("test4"));
roomTestList.push(new Room("test5"));



var roomRepo = new RoomRepo(roomTestList);
var service = new Service(roomRepo);
var serverManager = new ServerActionManager(service);

var timestampStart = 0;

const validateAction =(combinedAction) => {
    let err = "";
    if(Array.isArray(combinedAction)){
        let i = 0;
        for(let action of combinedAction){
        
        if(action.to  == null)
            err += `combinedActions[${i}][to] == null\n`;

        if(action.action.type == null)
            err += `combinedActions[${i}][action][type] == null\n`;
    
        
        i+=1;
        }

        return err;
    }


    //is just an obj

    if(combinedAction.action.type === "TIMEOUT")
            return err;

    if(combinedAction.to  == null)
        err += "combinedActions[to] == null\n";

    if(combinedAction.action.type == null)
        err += "combinedActions[action][type] == null\n";
    
    

    return err;
}


const sendToOne = (combinedAction)=>{
   if(combinedAction.action.payload == null){
    io.to(combinedAction.to).emit(combinedAction.action.type);
    return;
   }
    io.to(combinedAction.to).emit(combinedAction.action.type,combinedAction.action.payload);
};

const sendToRoom = (combinedAction)=>{
    if(combinedAction.action.payload == null){
        io.to(combinedAction.to).emit(combinedAction.action.type);
        return;
       }
    io.to(combinedAction.to).emit(combinedAction.action.type,combinedAction.action.payload);
};

const sendToMembers = (combinedAction)=>{
    for(let action of combinedAction){
        io.to(action.to).emit(action.action.type,action.action.payload);
    }
};

const proccesEmitterType = (command) => {
    console.log("New command processed");
    console.log("Number of commands: " + command.length());
    console.log("______________________");
    while(!command.isEmpty()){
        let combinedAction = command.popCombinedAction();
        let errors = validateAction(combinedAction);
        console.log(combinedAction);
        if(errors !== "")
            {
                console.log(errors);
                return;
            }
        
            
        switch(combinedAction.type){
            case emmiterTypes.ONE:
                sendToOne(combinedAction);
                break;
            case emmiterTypes.ALL:
                sendToRoom(combinedAction);
                break;
            case emmiterTypes.COMBINED:
                sendToMembers(combinedAction);
                break;
            default:
                setTimeout(()=>{return;},combinedAction.action.payload.seconds);
        }
    }
    console.log("______________________\n\n\n");
    
};


const createClientRequest = (type,payload)=>{
    return {
        type,payload,
    };
}

server.listen(8080,function () {
    console.log("Server is running at port 8080");
    timestampStart = Date.now();
    console.log(`Current timestamp: ${timestampStart}`)
});


io.on('connection', socket=>{
    
    socket.on(LOGIN,(data)=>{
        console.log("REQUEST: LOGIN");
        console.log("MADE BY: " + socket.id);

        let request = createClientRequest(LOGIN,{id:socket.id});
        let command = serverManager.processClientRequests(request);
        proccesEmitterType(command);
        //tested
    });

    
    socket.on(CREATE_ROOM,data=>{
        console.log("REQUEST: CREATE_ROOM");
        console.log("MADE BY: " + socket.id);

        let request = createClientRequest(CREATE_ROOM,{id: socket.id, roomID:data.roomID, nickname: data.nickname});
        let command = serverManager.processClientRequests(request);
        if(!command.containsErrors()){
            socket.join(data.roomID);
            console.log("client added to room!");
        }
        proccesEmitterType(command);
        //tested
    });

    socket.on(ROOMS_REQUEST,()=>{
        console.log("REQUEST: ROOMS_REQUEST");
        console.log("MADE BY: " + socket.id);

        let request = createClientRequest(ROOMS_REQUEST,{id:socket.id});
        let command = serverManager.processClientRequests(request);
        proccesEmitterType(command);
        //tested
        
    })


    socket.on(JOIN_ROOM,(data)=>{
        console.log("REQUEST: JOIN_ROOM");
        console.log("MADE BY: " + socket.id);

        let request = createClientRequest(JOIN_ROOM,{id: socket.id, roomID:data.roomID, nickname: data.nickname});
        let command = serverManager.processClientRequests(request);
        if(!command.containsErrors())
            socket.join(data.roomID);
        proccesEmitterType(command);


        //tested
    });


    socket.on(LEAVE_ROOM,(data)=>{
        let roomID = data.roomID;
        console.log("REQUEST: LEAVE_ROOM");
        console.log("MADE BY: " + socket.id);

        let request = createClientRequest(LEAVE_ROOM,{id:socket.id,roomID: roomID});
        let command = serverManager.processClientRequests(request);
        if(!command.containsErrors())
            socket.leave(roomID);
        
        proccesEmitterType(command);
        if(command.containsSpecificType(KICK_ALL)){
            io.in(roomID).clients((err,socketIDs) =>{
                if(err) throw err;
                socketIDs.forEach(id => io.sockets.sockets[id].leave(roomID));
            });
        }

        
        //tested
    })


    socket.on(START_GAME,(token)=>{
        console.log("REQUEST: START_GAME");
        console.log("MADE BY: " + socket.id);

       let request = createClientRequest(START_GAME,{id:socket.id,roomID: token.roomID});
       let command = serverManager.processClientRequests(request);
       proccesEmitterType(command);
       //tested

    });


    socket.on(GOT_PLAYERS,(token)=>{
        console.log("REQUEST: GOT_PLAYERS");
        console.log("MADE BY: " + socket.id);

        let request = createClientRequest(GOT_PLAYERS,{token});
        let command = serverManager.processClientRequests(request);
        if(command)
        proccesEmitterType(command);
    });


    socket.on(GOT_CARDS,(token)=>{
        console.log("REQUEST: GOT_CARDS");
        console.log("MADE BY: " + socket.id);

        let request = createClientRequest(GOT_CARDS,{token});
        let command = serverManager.processClientRequests(request);
        if(command)
        proccesEmitterType(command)
        
        //tested

    });


    socket.on(BID_RESPONSE,(data) => {
        console.log("REQUEST: BID_RESPONSE");
        console.log("MADE BY: " + socket.id);

        let request = createClientRequest(BID_RESPONSE,{token:data.token,bid: data.bid});
        let command = serverManager.processClientRequests(request);
        proccesEmitterType(command);

       //tested
    });

    socket.on(CARD_RESPONSE,(data) => {
        console.log("REQUEST: CARD_RESPONSE");
        console.log("MADE BY: " + socket.id);
        let request = createClientRequest(CARD_RESPONSE,{token: data.token, card: data.card});
        let command = serverManager.processClientRequests(request);
        if(command)
            proccesEmitterType(command);
        
    });

    socket.on(DISCONNECT,()=>{
        console.log("REQUEST: DISCONNECT");
        console.log("MADE BY: " + socket.id);
        let request = createClientRequest(DISCONNECT,{id:socket.id});
        let command = serverManager.processClientRequests(request);
        if(command != null)
            proccesEmitterType(command);

        //tested
    });

    socket.on(GOT_BID_STATUS,(token)=>{
        console.log("REQUEST: GOT_BID_STATUS");
        console.log("MADE BY: " + socket.id);
        let request = createClientRequest(GOT_BID_STATUS,{token});
        let command = serverManager.processClientRequests(request);
        proccesEmitterType(command);
        //tested
    });

    socket.on(GOT_TABLE_STATUS,(token)=>{
        console.log("REQUEST: GOT_TABLE_STATUS");
        console.log("MADE BY: " + socket.id);
        let request = createClientRequest(GOT_TABLE_STATUS,{token});
        let command = serverManager.processClientRequests(request);
        proccesEmitterType(command);
    });

    socket.on(GOT_WINNER,(token)=>{
        console.log("REQUEST: GOT_WINNER");
        console.log("MADE BY: " + socket.id);
        let request = createClientRequest(GOT_WINNER,{token});
        let command = serverManager.processClientRequests(request);
        proccesEmitterType(command);
    });

    socket.on(GOT_SCORE,(token)=>{
        console.log("REQUEST: GOT_SCORE");
        console.log("MADE BY: " + socket.id);
        let request = createClientRequest(GOT_SCORE,{token});
        let command = serverManager.processClientRequests(request);
        proccesEmitterType(command);
    })
});




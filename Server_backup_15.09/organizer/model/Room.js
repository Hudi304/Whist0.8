const GameEngine = require("../gameLogics/GameEngine");
const state = require("../gameLogics/RoomState");



module.exports = class Room{
    constructor(id,owner = null,props = {capacity: 6,locked: true},players = []){
        this.id = id;
        this.owner = owner;
        this.MAX_CAPACITY = props.capacity;
        this.players = owner ? [owner] : [];
        this.locked = props.locked;
        this.gameEngine = null;
        this.state = state.ACTIVE;     
    }

    json(detailed = false){

        return{
            roomID: this.id,
            owner: this.owner ? this.owner.name : null,
            capacity: this.MAX_CAPACITY,
            players: !detailed ? this.players.length : this.players.map(p => {return {name :p.name}}) ,
        };
    }

    getEngine(){
        return this.gameEngine;
    }


    /**
     * Add a new player to the room
     * if the nickname exists, throw error
     * if the state of the game is not active, throw error
     * if the room is full, throw error
     * if the room doesn't have a owner, the player will become owner
     * returns true
     * @param {*} player 
     */
    addPlayer(player){

        if(this.state !== state.ACTIVE)
            throw "This game is not open";

        if(this.players.length === this.MAX_CAPACITY)
            throw "Room is full!";
        const foundNickname = this.players.find(p => p.name === player.name);

        if(foundNickname != null)
            throw "Nickname already in use";

        this.players.push(player);
        if(this.owner == null)
            this.owner = player;

        
        return true;
    };

    removePlayer(playerID){
        let playerSize = this.players.length;
        this.players = this.players.filter(p => p.id !== playerID);
        let result = true;
        if(playerSize - this.players.length === 0)
            result = false;

        if(this.owner && this.owner.id === playerID)
        {
            this.owner = null;
            if(this.players.length > 0)
                this.owner = this.players[0];
        }

        if(this.players.length === 0)
            this.state = state.CANCELED;

        if(this.state === state.RUNNING)
            this.state = state.CANCELED;

        return result;
    }
    
    getSize(){
        return this.players.length;
    }


    startGame(){
        this.state = state.RUNNING;
        this.gameEngine = new GameEngine(this.players);
        this.gameEngine.startGame();
    }

    getState(){
        return this.state;
    }


    clearRoom(){
        this.players = [];
        this.owner = null;
        this.state = state.ACTIVE;
        this.gameEngine = new GameEngine(this.id);
    }



}


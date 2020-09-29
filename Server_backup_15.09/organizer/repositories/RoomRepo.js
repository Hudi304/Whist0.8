var Room = require('../model/Room');

module.exports = class RoomRepo{
    constructor(roomList=[]){
        this.list = roomList;
    }

    addRoom(roomID){

        let foundRoom = this.list.find(r => r.id === roomID);
        if(foundRoom)
            throw "Room already exists!";

        let room = new Room(roomID,null,{capacity: 6,locked : false});
        
        this.list.push(room);
        return room;
    }

    getAll(){
        return this.list;
    }


    getRoom(id){
        return this.list.find(r => r.id === id);
    }


    removeRoom(roomID){
        this.list = this.list.filter(r => r !== roomID && r.locked !== false);
        return this.list.length;
    }



}


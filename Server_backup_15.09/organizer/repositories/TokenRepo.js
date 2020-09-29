
module.exports = class TokenRepo{
    constructor(){
        this.list = [];
    }


    addToken(token){
        let foundToken = this.list.find(t => t.playerID === token.playerID);
        if(foundToken)
            throw "Token found!";

        this.list.push(token);
    }


    removeToken(token){
        let foundToken = this.list.find(t => t.playerID === token.playerID);
        if(!foundToken)
            throw "Token not found!";


        this.list = this.list.filter(t => t.playerID !== token.playerID);
    }

    getToken(playerID){
        let foundToken = this.list.find(t => t.playerID === playerID);
        return foundToken;
    }


    getAll(){
        return this.list;
    }
}
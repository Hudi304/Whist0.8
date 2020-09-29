module.exports = class Rounds{
    constructor(){
        this.__list = this.__initRounds();
        this.index = 0;
    }

    __initRounds(){
        let cards_num = 8;
        let list = [];
        for(let i = 1; i< 3; i++){
            let round = Round(i,cards_num);
            list.push(round);
        }

        return list;
    }

    hasEnded(){
        return this.index + 1 >= this.__list.length;
    }


    getRound(){
        return this.__list[this.index];
    }

    nextRound(){
        if(this.hasEnded())
            throw "The list has ended";
        
        this.index += 1;
    }

    
}


const Round = (id,cards) => {return {id,cards}};
const score = (round,{nickname,bids,made,bidMade,forbbiden}) => {
    let total = 0;
    if(bids === made)
        total = 5 + bids;
    else
        total = total - Math.abs(bids-made);
    return {round,nickname,bids,made,total}
};

const playerData = (nickname,total) =>{return {nickname,total}};

module.exports = class TableScore{
    constructor(playersNickname){
        this.table = [];
        this.playersNickname = playersNickname;
    }

    addData(round, bidStatus){
        let foundRound = this.table.find(r => r.round === round);
        if(foundRound)
            throw "Data already exists for this round!";
        
        for(let b of bidStatus){
            let scoreData = score(round,b);
            this.table.push(scoreData);
        }

        return true;
    }


    getTotal(){
        let list = [];
        for(let nickname of this.playersNickname){
            let calculateTotal = this.table.reduce((total,el)=>{
                if(el.nickname === nickname)
                    return total + el.total;
                else return total;
            },0);

            list.push(playerData(nickname,calculateTotal));
        }

        return list;

    }

    

}

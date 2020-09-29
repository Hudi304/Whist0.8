
const Deck = require('../model/Deck');
const MyQueue = require('./MyQueue');
const Score = require('./Score');
const Rounds = require('./Rounds');
const { ALL } = require('../socketOperations/emmiterType');

const validation = (item1) => {return item1!=null}
const equals = (item1,item2) => {
    if(!item1.name || !item2.name)
        return false;
    return item1.name === item2.name;
};

const flagNames = {
    CARD: "CARD",
    PLAYERS: "PLAYERS",
    BID: "BID",
    TABLE: "TABLE",
    WINNER: "WINNER",
    SCORE: "SCORE",
    ALL:"ALL",
};

class Flag{
    constructor(){
        this.flags = {
            gotPlayers: [],
            gotCards: [],
            gotBids: [],
            gotTable:[],
            gotWinner: [],
            gotScore: [],
        };
    }

    /**
     * Clear all the data from a specific flag, or all flags
     * @param {*} flagName 
     */
    resetFlag(flagName){
        switch(flagName){
            case flagNames.CARD:
                this.flags.gotCards = [];
                break;
            case flagNames.SCORE:
                this.flags.gotScore = [];
                break;
            case flagNames.BID:
                this.flags.gotBids = [];
                break;
            case flagNames.TABLE:
                this.flags.gotTable = [];
                break;
            case flagNames.WINNER:
                this.flags.gotWinner = [];
                break;
            case flagNames.PLAYERS:
                this.flags.gotPlayers = [];
            default:
                this.flags = {
                    gotPlayers: [],
                    gotCards: [],
                    gotBids: [],
                    gotTable:[],
                    gotWinner: [],
                    gotScore: []
                };
                break;
        }
    }

    /**
     * Get the list with all the id's for a specific flag, or all flags
     * @param {*} flagName 
     */
    getFlag(flagName){
        switch(flagName){
            case flagNames.CARD:
                return this.flags.gotCards;
                
            case flagNames.SCORE:
                return this.flags.gotScore;
             
            case flagNames.BID:
                return this.flags.gotBids
            case flagNames.TABLE:
                return this.flags.gotTable
                
            case flagNames.WINNER:
                return this.flags.gotWinner
            case flagNames.PLAYERS:
                return this.flags.gotPlayers
              
            default:
                return this.flags 
        }
    }

    /**
     * Add an id to a specific flag, and returns the new length.
     * If the id already exists, throws error
     * If the flagName is not specified, throws error
     * @param {*} flagName 
     * @param {*} id 
     */
    addToFlag(flagName, id){
        switch(flagName){
            case flagNames.CARD:
                
                if(this.flags.gotCards.find(el => el === id) != null)
                    throw "ID exists!";
                this.flags.gotCards.push(id);
                return this.flags.gotCards.length;
            
            case flagNames.SCORE:
                if(this.flags.gotScore.find(el => el === id) != null)
                    throw "ID exists!";
                this.flags.gotScore.push(id);
                return this.flags.gotScore.length;
             
            case flagNames.BID:
                if(this.flags.gotBids.find(el => el === id) != null)
                    throw "ID exists!";
                this.flags.gotBids.push(id);
                return this.flags.gotBids.length;


            case flagNames.TABLE:
                if(this.flags.gotTable.find(el => el === id) != null)
                    throw "ID exists!";
                this.flags.gotTable.push(id);
                return this.flags.gotTable.length;
                
            case flagNames.WINNER:
                if(this.flags.gotWinner.find(el => el === id) != null)
                    throw "ID exists!";
                this.flags.gotWinner.push(id);
                return this.flags.gotWinner.length;
            case flagNames.PLAYERS:
                if(this.flags.gotPlayers.find(el => el === id) != null)
                    throw "ID exists!";
                this.flags.gotPlayers.push(id);
                return this.flags.gotPlayers.length;
              
            default:
                throw "flag name not recognized!";
        }
    }


}

const createBid = (nickname) => ({nickname, bids: 0, made: 0, bidMade: false, forbidden: -1});
const createTableCard = (nickname) => ({nickname,card: null});

const cardData = (card) =>{
    let cardToArray = card.split("-");
    return {type: cardToArray[0],
            value: parseInt(cardToArray[1])};
};

module.exports = class GameEngine{
    constructor(players){
        
        this.playerRoundOrder = new MyQueue(players,validation,equals);
        this.playerHandOrder = new MyQueue(players,validation,equals);
        this.scoreData = new Score(players.map(p => p.name));
        this.roundData = new Rounds();
        this.flags = new Flag();
        this.deck = new Deck(players.length);

        this.atu = null;

        this.tableStatus = [];
        this.bidStatus = [];
        this.winnerStatus = null;

        this.playerCards = {};
        this.playersIDs = {};

        for(let p of players){
            this.playersIDs[p.name] = p.id;
        }
    }

    // public functions


    //operational
    startGame(){
       
        this.deck.remakeDeck();
        this.deck.shuffleCards();
        this.flags.resetFlag(flagNames.ALL);
        //this.__initBidStatus();
        //this.__initTableStatus();
    }

    initBidStatus(){
        this.flags.resetFlag(flagNames.BID);
        this.bidStatus = [];
        for(let p of this.playerRoundOrder.getCopy()){
            this.bidStatus.push(createBid(p.name));
        }
    }

    initTableStatus(){
        this.flags.resetFlag(flagNames.TABLE);
        this.tableStatus = [];
        for(let p of this.playerHandOrder.getCopy()){
            this.tableStatus.push(createTableCard(p.name));
        }
    }

    drawCardsForPlayers(){
        let noCards = this.roundData.getRound().cards;

        for(let p of this.playerRoundOrder.getCopy()){
            let cards  = this.deck.drawSomeCards(noCards);
            this.playerCards[p.name] = cards;
        }

        if(noCards < 0)
            this.atu = this.deck.drawSomeCards(1);
    }


    updateBid(nickname,bid){
        //throw error if bid is not accepted.

        for(let b of this.bidStatus){
            if(b.nickname === nickname){
                if(b.forbidden === bid)
                    return false;
                b.bids = bid;
                b.bidMade = true;
            }
        }

        //updating the forbidden value for the last player
        this.__updateForbbidenBid();
        return true;
    }

    updateTable(nickname,card){
        let validationResult = this.__validateCard(nickname,card);
        
        if(!validationResult)
            return false;

        // update the table status
        for(let p of this.tableStatus){
            if(p.nickname === nickname)
                p.card = card;
        }


        // remove the card from player
        this.playerCards[nickname] = this.playerCards[nickname].filter(c => c!== card);

        //if all cards have been placed...decide the winner
        if(this.isHandFinished()){
            this.winnerStatus = this.__getWinnerForRound();
        }

        return true;
        
    }

    resetTableForHand(){
        this.__updateBidStatus();
        this.playerHandOrder.setFirstElement({name: this.winnerStatus});
        this.initTableStatus();
        this.winnerStatus = null;
        
    }

    calculateScore(){
        let roundID = this.roundData.getRound().id;
        this.scoreData.addData(roundID,this.bidStatus);
    }

    moveToNextRound(){
        this.roundData.nextRound();
        this.playerCards = {};
        
        this.deck.remakeDeck();
        this.deck.shuffleCards();
        this.flags.resetFlag(flagNames.ALL);

        this.playerRoundOrder.moveBy(1);
        
        
    }

    //refreshers
















    //getters

    getPlayersOrder(){
        return this.playerRoundOrder.getCopy();
    }

    getTableStatus(){
        return this.tableStatus;
    }

    getBidStatus(){
        return this.bidStatus;
    }

    getWinnerStatus(){
        return this.winnerStatus;
    }

    getCards(){
        let cardsData = {};
        for(let p of Object.keys(this.playersIDs)){
            cardsData[this.playersIDs[p]] = this.playerCards[p].reduce((total,card) => {return total + card + " "}, "");

        }
        cardsData["atu"] = this.atu;
        return cardsData;
    }

    getFinalTable(){
        let total = this.scoreData.getTotal();
        for(let t1 of total){
            let place = 1;
            for(let t2 of total)
            {
                if(t1 === t2)
                    continue;
                
                if(t1.total < t2.total)
                    place += 1;
            }
            t1["place"] = place; 
        }

        return total;



    }

    getTotalScore(){
        return this.scoreData.getTotal();
    }

    // UPDATING FLAGS
    gotPlayerData(nickname){
        let count = this.flags.addToFlag(flagNames.PLAYERS,nickname);
        let result  = count === this.playerRoundOrder.size();

        if(result)
            this.flags.resetFlag(flagNames.PLAYERS);

        return result;
    }

    gotCardData(nickname){
        let count = this.flags.addToFlag(flagNames.CARD,nickname);
        let result =  count === this.playerRoundOrder.size();

        if(result)
            this.flags.resetFlag(flagNames.CARD);

        return result;
    }

    gotBidsData(nickname){
        let count = this.flags.addToFlag(flagNames.BID,nickname);
        let result =  count === this.playerRoundOrder.size();

        if(result)
            this.flags.resetFlag(flagNames.BID);

        return result;
    }

    gotTableData(nickname){
        let count = this.flags.addToFlag(flagNames.TABLE,nickname);
        let result =  count === this.playerRoundOrder.size();

        if(result)
            this.flags.resetFlag(flagNames.TABLE);

        return result;
    }

    gotWinnerData(nickname){
        let count = this.flags.addToFlag(flagNames.WINNER,nickname);
        let result  = count === this.playerRoundOrder.size();

        if(result)
            this.flags.resetFlag(flagNames.WINNER);

        return result;
    }

    gotScoreData(nickname){
        let count = this.flags.addToFlag(flagNames.SCORE,nickname);
        let result  = count === this.playerRoundOrder.size();

        if(result)
            this.flags.resetFlag(flagNames.SCORE);

        return result;

    }
    // VERIFICATION PROCCES

    isBiddingFinished(){
        for(let b of this.bidStatus){
            if(b.bidMade === false)
                return false;
        }

        return true;
    }

    isHandFinished(){
        for(let h of this.tableStatus){
            if(h.card == null)
                return false;
        }

        return true;
    }

    isRoundFinished(){
        for(let nickname of Object.keys(this.playerCards)){
            if(this.playerCards[nickname].length != 0)
                return false;
        }
        return true;
    }

    isGameFinished(){
        return this.roundData.hasEnded()
    }


    // private functions
   __updateForbbidenBid(){
       let bidsUnmade = this.bidStatus.filter(b => b.bidMade === false);
       if(bidsUnmade.length === 1){
            let noCards = this.roundData.getRound().cards;
            let total = this.bidStatus.reduce((total,bid) => { return total + bid.bids},0);
            let lastIndex = this.bidStatus.length-1;
            if(noCards >= total)
                this.bidStatus[lastIndex].forbidden = noCards - total;

       }
   }

   __validateCard(nickname,card){
       let cardsOnTable = this.tableStatus.map(el => el.card);

       if(cardsOnTable[0] == null)
            return true; // no cards on the table

        
        if(!this.atu){
            //if this round doesn't have an atu

            let myCardData = cardData(card);
            let firstCardData = cardData(cardsOnTable[0]);

            if(myCardData.type === firstCardData.type)
                return true; // the type matches

            
            for(let c of this.playerCards[nickname]){
                let localCard = cardData(c);
                if(localCard.type === firstCardData.type)
                    return false; // the player have in hand a card that matches the type on the table;    
            }

            return true;

        }

        else{
            //TODO: IMPLEMENT ATU
            return true;
        }
   }

   __getWinnerForRound(){
       // return the nickname
       if(this.atu == null){
            let firstCard = null;
            let winner = null;
            for(let p of this.tableStatus){
                if(firstCard == null)
                    {
                        firstCard = cardData(p.card);
                        winner = p.nickname;
                        continue;
                    }
                
                let localCardData = cardData(p.card);
                let localPlayerNickname= p.nickname;

                if(firstCard.type === localCardData.type && firstCard.value < localCardData.value){
                    firstCard = localCardData;
                    winner = localPlayerNickname;
                }
            }

            return winner;
       }
       else{
           //TODO: IMPLEMENT ATU
           return null;
       }
   }

   __updateBidStatus(){
       for(let b of this.bidStatus){
           if(b.nickname === this.winnerStatus)
                {
                    b.made += 1;
                    return;
                }
       }
   }
}
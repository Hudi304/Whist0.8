

const type = ['h','s','d','c'];

module.exports = class Deck{
    constructor(no_of_players){
        this.players_size = no_of_players;
        this.remainingCards = this.createDeck();
        this.usedCards = [];
    }

    createDeck(){
        let max_value = 14;
        let cards = [];
        while(cards.length < this.players_size * 8){
            
            for(let t of type){
                let x = t +`-` +  max_value;    
                cards.push(x);
            }

            max_value -= 1;
        }

        return cards;
    }

    shuffleCards(){
        this.remainingCards = this.remainingCards.sort(() => Math.random() - 0.5);
    }

    drawSomeCards(no_of_cards){
        if(this.remainingCards < no_of_cards)
            return [];
        
        let cards = []

        while(no_of_cards > 0){
            let card = this.remainingCards.shift();
            cards.push(card);
            this.usedCards.push(card);
            no_of_cards -= 1;
        }


        if(cards.length === 1)
            return cards[0];


        return cards;
    }


    remakeDeck(){
        this.remainingCards = [...this.usedCards, ...this.remainingCards];
        this.usedCards = [];
    }

    




}
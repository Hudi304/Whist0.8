module.exports = class MyQueue{
    constructor(list = [],validator = (item) => {return true},equals = (item1,item2) => {return item1===item2}){
        this.list = list;
        this.validator = validator;
        this.equals = equals;
    }

    pushAll(items){
        this.list = [];
        this.list = [...items];
    }

    push(item){
        if(!this.validator(item))
            throw "Item not valid!";

        this.list.push(item);
        return this.list.length;
    }


    pop(){
        let item = this.list.shift();
        return item;
    }


    setFirstElement(item){
        //console.log("MyQueue");
        let foundItem = this.list.find(i => {return this.equals(item,i)});
        
        
        if(!foundItem)
            throw "Item not found!";

        while(!this.equals(this.list[0],item)){
            let localItem = this.pop();
            this.push(localItem);
        }

        return true;

    }


    moveBy(num){
        if(num > this.list.length - 1)
            throw `The max num is ${this.list.length-1}`;

        let firstItem = this.list[num];
        return this.setFirstElement(firstItem);
    }

    getCopy(){
        return [...this.list];
    }

    size(){
        return this.list.length;
    }

}
import { Jersey } from "./jersey";

export class User {
    username: string;
    shoppingCart: Jersey[];

    constructor(username: string) {
        this.username = username;
        this.shoppingCart = []
    }

    retrieveShoppingCart(): void {
        this.shoppingCart = []; // Later this will use the shopping cart service to get the user's shopping cart
    }
}
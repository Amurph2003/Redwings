/**
 * A representation of a jersey being sold in the estore.
 */
 export interface Jersey {
    id: number;
    name: string;
    number: number;
    price: number;
    size: string;
    color: string;
    quantity: number;
    imageUrl: string;
}
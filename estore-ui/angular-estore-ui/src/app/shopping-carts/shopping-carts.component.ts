import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Admin } from '../admin';
import { Jersey } from '../jersey';
import { JerseyService } from '../jersey.service';
import { ShoppingCartService } from '../shopping-cart.service';
import { User } from '../user';

@Component({
  selector: 'app-shopping-carts',
  templateUrl: './shopping-carts.component.html',
  styleUrls: ['./shopping-carts.component.css']
})
export class ShoppingCartsComponent implements OnInit {

  jerseys: Jersey[] = [];
  total: number = 0;
  congrats: string = "";
  @Input() user?: User;

  constructor(
    private shoppingCartService: ShoppingCartService,
    private jerseyService: JerseyService,
    private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.getShoppingCart()
    this.getUser()
  }

  /** retreives the jerseys in the shoppingCart and puts them in the jerseys array */
  getShoppingCart(): void {
    const username = String(this.route.snapshot.paramMap.get('username'));
    this.shoppingCartService.getShoppingCart(username)
      .subscribe(cart => this.jerseys = cart)
  }

  /**Returns jersey from shopping cart */
  return(username: string, jersey: Jersey): void {
    this.jerseys = this.jerseys.filter(j => j !== jersey);
    this.shoppingCartService.deleteJersey(username, jersey).subscribe();
    jersey.quantity = 1;
    this.jerseyService.addJersey(jersey).subscribe(); // put jersey back in inventory
    window.location.reload();
  }

  /**Removes jersey from shopping cart */
  remove(username: string, jersey: Jersey): void {
    this.jerseys = this.jerseys.filter(j => j !== jersey);
    this.shoppingCartService.deleteJersey(username, jersey).subscribe();
  }

  /** stores the username information in the user field */
  getUser(): void {
    const username = String(this.route.snapshot.paramMap.get('username'));
    this.user = new Admin(username);
  }

 
  /**Checkout out for purchase */
  checkout(username: string): number {
    this.congrats = "Congrats on your purchase!";

    this.jerseys.forEach( jersey => {
      const quantity = jersey.quantity;
      for(let i = 0; i < quantity; i++) {
        this.total += jersey.price;
        this.remove(username, jersey);
      }
    });
    // disable button based on id attribute
    const btn = document.getElementById('btn') as HTMLButtonElement | null;
    btn?.setAttribute('disabled', '');
    return this.total;
  }

  
  auto(image: string): boolean {
    if (image === "../../assets/defaults/red.webp" || image === "../../assets/defaults/white.webp"){
      return false;
    }
    return true;
  }
}

import { Component, OnInit, Input } from '@angular/core';
import { Jersey } from '../jersey';
import { JerseyService } from '../jersey.service';
import { User } from '../user';
import { ActivatedRoute } from '@angular/router';
import { Admin } from '../admin';
import { ShoppingCartService } from '../shopping-cart.service';

@Component({
  selector: 'app-browse-page',
  templateUrl: './browse-page.component.html',
  styleUrls: ['./browse-page.component.css']
})
export class BrowsePageComponent implements OnInit {

  jerseys: Jersey[] = [];
  @Input() user?: User;

  constructor(
    private jerseyService: JerseyService,
    private shoppingCartService: ShoppingCartService,
    private route: ActivatedRoute
    ) { }

  ngOnInit(): void {
    this.getJerseys()
    this.getUser()
  }


  /** retreives the jerseys in the inventory and puts them in the jerseys array */
  getJerseys(): void {
    this.jerseyService.getJerseys()
      .subscribe(inventory => this.jerseys = inventory)
  }

  /** stores the username information in the user field */
  getUser(): void {
    const username = String(this.route.snapshot.paramMap.get('username')?.toLowerCase());
    if (username === 'admin'){
      this.user = new Admin(username);
    } else {
      this.user = new User(username);
    }
  }

  isAdmin(): boolean{
    const username = this.user?.username;
    if (username === 'admin'){
      return true;
    } else {
      return false;
    }
  }

  isNotAdmin(): boolean{
    const username = this.user?.username;
    if (username === 'admin'){
      return false;
    } else {
      return true;
    }
  }

  /** add a jersey to the cart  */
  addToCart(username: string, jersey: Jersey): void {
    const oldQuantity = jersey.quantity;
    jersey.quantity = 1;
    this.shoppingCartService.addJersey(username, jersey)
      .subscribe();

    jersey.quantity = oldQuantity-1;
    this.jerseyService.updateJersey(jersey).subscribe();
  }

  auto(image: string): boolean {
    if (image === "assets/defaults/red.webp" || image === "assets/defaults/white.webp"){
      return false;
    }
    return true;
  }
}

import { Component, OnInit, Input } from '@angular/core';
import { Jersey } from '../jersey';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { JerseyService } from '../jersey.service';
import { User } from '../user';
import { Admin } from '../admin';
import { ShoppingCartService } from '../shopping-cart.service';


@Component({
  selector: 'app-jersey-detail',
  templateUrl: './jersey-detail.component.html',
  styleUrls: ['./jersey-detail.component.css']
})
export class JerseyDetailComponent implements OnInit {

  @Input() jersey?: Jersey;
  @Input() user!: User;
  altInfo = "Jersey Representational Image";

  constructor(
    private route: ActivatedRoute,
    private jerseyService: JerseyService,
    private shoppingCartService: ShoppingCartService,
    private location: Location
  ) { }

  ngOnInit(): void {
    this.getJersey();
    this.getUser();
  }

  /** gets a jersey from the server based on the id in the route and assigns it to the jersey field */
  getJersey(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.jerseyService.getJersey(id)
      .subscribe(jersey => this.jersey = jersey);
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
    const username = String(this.route.snapshot.paramMap.get('username')?.toLowerCase());
    if (username === 'admin'){
      return true;
    }
    return false;
  }

  goBack(): void {
    this.location.back();
  }

  save(): void {
  if (this.jersey?.imageUrl === null || this.jersey?.imageUrl === "null" || this.jersey!.imageUrl.length < 2) {
    this.jersey!.imageUrl = "assets/defaults/" + this.jersey!.color.toLowerCase() + ".webp";
  } else if (this.jersey!.imageUrl.length < 20 ) {
    this.jersey!.imageUrl = "assets/jerseyImages/"+this.jersey?.imageUrl;
  }

  if(!this.isAddedImage()) {
    if(this.jersey?.imageUrl != "assets/defaults/" + this.jersey!.color.toLowerCase() + ".webp") {
      this.jersey!.imageUrl = "assets/defaults/" + this.jersey!.color.toLowerCase() + ".webp";
    }
  }

  if (this.jersey) {
    this.jerseyService.updateJersey(this.jersey)
      .subscribe(() => this.goBack());
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

  isNotAdmin(): boolean{
    const username = this.user?.username;
    if (username === 'admin'){
      return false;
    } else {
      return true;
    }
  }

  isAddedImage(): boolean {
    if (this.jersey?.imageUrl === "assets/defaults/red.webp" || this.jersey?.imageUrl === "assets/defaults/white.webp") {
      return false;
    } else {
      return true;
    }
  }

}

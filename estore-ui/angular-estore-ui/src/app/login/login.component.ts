import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../user';
import { ShoppingCartService } from '../shopping-cart.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  user: User = new User("");

  constructor(
    private router: Router,
    private shoppingCartService: ShoppingCartService,
    ) { }

  ngOnInit(): void {
  }

  login() {
    if (this.user.username.toLowerCase() === 'admin'){
      this.router.navigate(['/admin/browse']);
    } else {
      this.router.navigate(['/username/browse']);
    }
  }

  createCart(username: string) {
    if (username != 'admin') {
      this.shoppingCartService.createCart(username).subscribe();
    }
  }

}

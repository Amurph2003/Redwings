import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Admin } from '../admin';
import { Jersey } from '../jersey';
import { JerseyService } from '../jersey.service';
import { User } from '../user';

@Component({
  selector: 'app-add-remove-from-store-page',
  templateUrl: './add-remove-from-store-page.component.html',
  styleUrls: ['./add-remove-from-store-page.component.css']
})
export class AddRemoveFromStorePageComponent implements OnInit {

  jerseys: Jersey[] = [];
  @Input() user?: User;

  constructor(
    private jerseyService: JerseyService,
    private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.getJerseys();
    this.getUser();
  }

  /** retreives the jerseys in the inventory and puts them in the jerseys array */
  getJerseys(): void {
    this.jerseyService.getJerseys()
      .subscribe(inventory => this.jerseys = inventory)
  }

  /** deletes a jersey from the inventory */
  delete(jersey: Jersey): void {
    this.jerseys = this.jerseys.filter(j => j !== jersey);
    this.jerseyService.deleteJersey(jersey.id).subscribe();
    window.location.reload();
  }
  
  /** add a jersey to the inventory  */
  add(name: string, num: string, pr: string, size: string, color: string, quantity: string, imageUrl: String): void {
    // makes name titlecase
    name = name.trim().toLowerCase();
    const name_arr = name.split("");
    const first_letter = name_arr[0];
    name = name.replace(first_letter, first_letter.toUpperCase())

    // capitalizations b/c they're enums
    size = size.trim().toUpperCase();
    color = color.trim().toUpperCase();
   
    if (!name) { return; }
    
    const number = Number(num)
    const price = Number(pr)
    if (imageUrl === "null" || imageUrl.length < 2) {
      imageUrl = "assets/defaults/" + color.toLowerCase() + ".webp";
    } else if ((imageUrl.length < 20)) {
      imageUrl = "assets/jerseyImages/"+imageUrl;
    }

    this.jerseyService.addJersey({ name, number, price, size, color, quantity, imageUrl } as unknown as Jersey)
      .subscribe(jersey => {
        this.jerseys.push(jersey);
      });
    window.location.reload();
  }

  /** stores the username information in the user field */
  getUser(): void {
    const username = String(this.route.snapshot.paramMap.get('username'));
    this.user = new Admin(username);
  }

  auto(image: string): boolean {
    if (image === "assets/defaults/red.webp" || image === "assets/defaults/white.webp"){
      return false;
    }
    return true;
  }

}

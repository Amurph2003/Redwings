import { TitleCasePipe } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable, Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { Admin } from '../admin';
import { Jersey } from '../jersey';
import { JerseyService } from '../jersey.service';
import { User } from '../user';

@Component({
  selector: 'app-jersey-search',
  templateUrl: './jersey-search.component.html',
  styleUrls: ['./jersey-search.component.css']
})
export class JerseySearchComponent implements OnInit {
  jerseys$!: Observable<Jersey[]>;
  private searchQueries = new Subject<string>();
  @Input() user?: User;

  constructor(
    private jerseyService: JerseyService,
    private route: ActivatedRoute
    ) {}

  search(query: string = ""): void{
    this.searchQueries.next(query);
  }

  ngOnInit(): void {
    this.jerseys$ = this.searchQueries.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap((term: string) => this.jerseyService.searchJersey(term)),
    );
    this.getUser();
  }

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

}

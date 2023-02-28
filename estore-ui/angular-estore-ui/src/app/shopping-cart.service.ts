import { Injectable } from '@angular/core';
import { Jersey } from './jersey';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { MessageService } from './message.service';
import { User } from './user';


@Injectable({
  providedIn: 'root'
})
export class ShoppingCartService {

  private shoppingCartsUrl = 'http://100.73.1.36:5600/shopping-carts';
  private jerseysUrl = 'http://100.73.1.36:5600/products';


  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient,
    private messageService: MessageService

    ) { }

  /** GET a shopping cart from the server using user's username */
  getShoppingCart(username: string): Observable<Jersey[]> {
    const shoppingCart = this.http.get<Jersey[]>(`${this.shoppingCartsUrl}/${username}`);
    return shoppingCart;
  }

  /** DELETE: delete the jersey from the server */
  removeFromInventory(id: number): Observable<Jersey> {
    const url = `${this.jerseysUrl}/${id}`;

    return this.http.delete<Jersey>(url, this.httpOptions).pipe(
      tap(_ => this.log(`deleted jersey id=${id}`)),
      catchError(this.handleError<Jersey>('deleteJersey'))
    );
  }

  /** POST: add a new jersey to the server */
  addJersey(a_username: string, a_jersey: Jersey): Observable<Jersey> {

    return this.http.put<Jersey>(this.shoppingCartsUrl + "/" + a_username, a_jersey, this.httpOptions).pipe(
      tap((newJersey: Jersey) => this.log(`added jersey w/ id=${newJersey.id}`)),
      catchError(this.handleError<Jersey>('addJersey'))
    );
  }

  /** DELETE: delete the jersey from the user cart */
  deleteJersey(a_username: string, a_jersey: Jersey): Observable<Jersey> {

    const deleteHttpOption = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
      body: a_jersey
    }

    return this.http.delete<Jersey>(this.shoppingCartsUrl + "/" + a_username, deleteHttpOption).pipe(
      tap(_ => this.log(`deleted jersey id=${a_jersey}`)),
      catchError(this.handleError<Jersey>('deleteJersey'))
    );
  }

  /** Log a JerseyService message with the MessageService */
  private log(message: string) {
    this.messageService.add(`JerseyService: ${message}`);
  }

    /**
   * Handle Http operation that failed.
   * Let the app continue.
   *
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
     private handleError<T>(operation = 'operation', result?: T) {
      return (error: any): Observable<T> => {
        console.error(error);
        return of(result as T);
      };
    }

    createCart(username: string) {
      return this.http.post<Jersey[]>(this.shoppingCartsUrl + "/" + username, this.httpOptions).pipe(
        tap((newCart: Jersey[]) => this.log(`created shopping cart for ${username}`)),
        catchError(this.handleError<Jersey[]>('make cart'))
      );
    }

}


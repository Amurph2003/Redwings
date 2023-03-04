import { Injectable } from '@angular/core';
import { Jersey } from './jersey';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { MessageService } from './message.service';


/**
 * Jersey service that provides access to CRUD methods for the 
 * estore ui.
 */
@Injectable({
  providedIn: 'root'
})
export class JerseyService {

  private jerseysUrl = 'http://100.73.1.36:5600/products';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient,
    private messageService: MessageService

    ) { }

  /** GET all jerseys from the server */
  getJerseys(): Observable<Jersey[]> {
    return this.http.get<Jersey[]>(this.jerseysUrl).pipe(
      tap(_ => console.log('fetched jerseys')),
      catchError(this.handleError<Jersey[]>('getJerseys', []))
    );
  }

  /** GET a single jersey from the server using its id */
  getJersey(id: number): Observable<Jersey> {
    const jersey = this.http.get<Jersey>(`${this.jerseysUrl}/${id}`);
    return jersey;
  }

  /** DELETE: delete the jersey from the server */
  deleteJersey(id: number): Observable<Jersey> {
    const url = `${this.jerseysUrl}/${id}`;
    if(this.deleteConfirmation() == true) {
      // case for when user confirms delete
      return this.http.delete<Jersey>(url, this.httpOptions).pipe(
        tap(_ => this.log(`deleted jersey id=${id}`)),
        catchError(this.handleError<Jersey>('deleteJersey'))
      );
    }
    else
    {
      // case for when nothing happens
      return new Observable<Jersey>;
    }
  }

  /** Log a JerseyService message with the MessageService */
  private log(message: string) {
    this.messageService.add(`JerseyService: ${message}`);
  }

  /** POST: add a new jersey to the server */
  addJersey(jersey: Jersey): Observable<Jersey> {
    return this.http.post<Jersey>(this.jerseysUrl, jersey, this.httpOptions).pipe(
      tap((newJersey: Jersey) => this.log(`added jersey w/ id=${newJersey.id}`)),
      catchError(this.handleError<Jersey>('addJersey'))
    );
  }

  /** PUT: update the jersey on the server */
  updateJersey(jersey: Jersey): Observable<any> {
    return this.http.put(this.jerseysUrl, jersey, this.httpOptions).pipe(
      tap(_ => console.log('updated the jersey')),
      catchError(this.handleError<Jersey[]>('updateJersey'))
    );
  }

  /** GET jerseys of players whose name contains the query */
  searchJersey(query: string): Observable<Jersey[]> {
    if (!query.trim()){
      return of([]);
    }
    return this.http.get<Jersey[]>(`${this.jerseysUrl}/?name=${query}`).pipe(
      tap(x => x.length ?
         console.log(`found jerseys matching "${query}"`) :
         console.log(`no jerseys matching "${query}"`)),
      catchError(this.handleError<Jersey[]>('searchJerseys', []))
    );
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

  /**
   * Delete confirmation method
   */
   private deleteConfirmation() : boolean {
    if(confirm("Are you sure you want to delete this jersey?")) {
      return true;
    }
    return false;
  }
}

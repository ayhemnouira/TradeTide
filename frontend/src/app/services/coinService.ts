import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class coinService {
  private baseUrl = 'http://localhost:8080/coins';

  constructor(private http: HttpClient) {}

  // Get coin list with pagination
  getCoinList(page: number): Observable<any> {
    const params = new HttpParams().set('page', page.toString());
    return this.http
      .get(`${this.baseUrl}`, { params })
      .pipe(catchError(this.handleError));
  }

  // Get market chart for a specific coin
  getMarketChart(coinId: string, days: number): Observable<any> {
    const params = new HttpParams().set('days', days.toString());
    return this.http
      .get(`${this.baseUrl}/${coinId}/chart`, { params })
      .pipe(catchError(this.handleError));
  }

  // Search coins by keyword
  searchCoin(keyword: string): Observable<any> {
    const params = new HttpParams().set('keyword', keyword);
    return this.http
      .get(`${this.baseUrl}/search`, { params })
      .pipe(catchError(this.handleError));
  }

  // Get top 50 coins by market cap
  getTop50CoinsByMarketCapRank(): Observable<any> {
    return this.http
      .get(`${this.baseUrl}/top50`)
      .pipe(catchError(this.handleError));
  }

  // Get trending coins
  getTrendingCoins(): Observable<any> {
    return this.http
      .get(`${this.baseUrl}/trending`)
      .pipe(catchError(this.handleError));
  }

  // Get coin details by ID
  getCoinDetails(coinId: string): Observable<any> {
    return this.http
      .get(`${this.baseUrl}/details/${coinId}`)
      .pipe(catchError(this.handleError));
  }

  // Handle HTTP errors
  private handleError(error: any) {
    console.error('API Error:', error);
    return throwError(
      () => new Error('Something went wrong; please try again later.')
    );
  }
}

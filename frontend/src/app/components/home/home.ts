import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { coinService } from '../../services/coinService';
import { Observable, Subject, of } from 'rxjs';
import {
  debounceTime,
  distinctUntilChanged,
  map,
  catchError,
} from 'rxjs/operators';

interface CoinItem {
  id: string;
  name: string;
  image: string;
  symbol: string;
  market_cap_change_percentage_24h: number;
  current_price: number;
}

interface CoinListItem {
  id: string;
  symbol: string;
  name: string;
  image: string;
  current_price: number;
  market_cap: number;
  market_cap_rank: number;
  total_volume: number;
  price_change_percentage_24h: number;
}

interface CoinResponse {
  coins: {
    item: {
      id: string;
      name: string;
      large: string;
      symbol: string;
      data: {
        price_change_percentage_24h: { usd: number };
        price: number;
      };
    };
  }[];
}

@Component({
  selector: 'app-home',
  imports: [CommonModule, FormsModule],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home implements OnInit {
  bannerData: CoinItem[] = [];
  coinList: CoinListItem[] = [];
  filteredCoinList: CoinListItem[] = [];
  top50Coins: CoinListItem[] = [];
  currentPage: number = 1;
  searchQuery: string = '';
  isSearching: boolean = false;
  searchError: string | null = null;
  showTop50: boolean = false;

  private searchTerms = new Subject<string>();

  constructor(private coinService: coinService, private router: Router) {}

  ngOnInit(): void {
    this.getBannerData();
    this.getCoinList(this.currentPage);
    this.getTop50Coins();
    this.searchTerms
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        map((term: string) => {
          this.searchQuery = term;
          this.isSearching = !!term;
          this.filterCoins();
          this.searchError = this.filteredCoinList.length
            ? null
            : 'No coins found for your search.';
          return this.filteredCoinList;
        })
      )
      .subscribe({
        next: (results: CoinListItem[]) => {
          console.log('Filtered Results:', results);
        },
        error: (err) => {
          console.error('Search Error:', err);
          this.searchError = 'Failed to process search.';
        },
      });
  }

  private filterCoins(): void {
    if (!this.searchQuery) {
      this.filteredCoinList = [...this.coinList];
      return;
    }
    const query = this.searchQuery.toLowerCase();
    this.filteredCoinList = this.coinList.filter(
      (coin) =>
        coin.name.toLowerCase().includes(query) ||
        coin.symbol.toLowerCase().includes(query)
    );
  }

  onSearch(): void {
    this.searchTerms.next(this.searchQuery);
  }

  clearSearch(): void {
    this.searchQuery = '';
    this.isSearching = false;
    this.searchError = null;
    this.filterCoins();
  }

  getBannerData() {
    this.coinService.getTrendingCoins().subscribe({
      next: (res: CoinResponse) => {
        console.log('Trending Coins Response:', res);
        this.bannerData = res.coins.map((coin) => ({
          id: coin.item.id,
          name: coin.item.name,
          image: coin.item.large,
          symbol: coin.item.symbol.toUpperCase(),
          market_cap_change_percentage_24h:
            coin.item.data.price_change_percentage_24h.usd,
          current_price: coin.item.data.price,
        }));
      },
      error: (err) => {
        console.error('Banner Error:', err);
        this.searchError = 'Failed to load trending coins.';
      },
    });
  }

  getCoinList(page: number) {
    this.currentPage = page;
    this.coinService.getCoinList(page).subscribe({
      next: (res: CoinListItem[]) => {
        this.coinList = res.map((coin) => ({
          ...coin,
          symbol: coin.symbol.toUpperCase(),
        }));
        this.filterCoins();
        this.isSearching = !!this.searchQuery;
        this.searchError = null;
      },
      error: (err) => {
        console.error('Coin List Error:', err);
        this.searchError =
          'Failed to load coin list. Please check your connection.';
      },
    });
  }

  getTop50Coins() {
    this.coinService.getTop50CoinsByMarketCapRank().subscribe({
      next: (res: CoinListItem[]) => {
        this.top50Coins = res.map((coin) => ({
          ...coin,
          symbol: coin.symbol.toUpperCase(),
        }));
      },
      error: (err) => {
        console.error('Top 50 Coins Error:', err);
        this.searchError = 'Failed to load top 50 coins.';
      },
    });
  }

  private getCoinListObservable(page: number): Observable<CoinListItem[]> {
    return this.coinService.getCoinList(page).pipe(
      map((res: CoinListItem[]) =>
        res.map((coin) => ({
          ...coin,
          symbol: coin.symbol.toUpperCase(),
        }))
      ),
      catchError((err) => {
        console.error('Coin List Observable Error:', err);
        return of([]);
      })
    );
  }

  changePage(page: number) {
    if (page >= 1) {
      this.currentPage = page;
      this.getCoinList(this.currentPage);
    }
  }

  toggleTable(showTop50: boolean) {
    this.showTop50 = showTop50;
    if (showTop50) {
      this.searchQuery = '';
      this.isSearching = false;
      this.searchError = null;
    } else {
      this.filterCoins();
    }
  }

  viewCoinChart(coinId: string, coinName: string): void {
    this.router.navigate(['/chart', coinId], {
      queryParams: { name: coinName },
    });
  }
}

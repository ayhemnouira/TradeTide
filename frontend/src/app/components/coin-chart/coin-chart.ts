import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ActivatedRouteSnapshot } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NgChartsModule } from 'ng2-charts';
import { coinService } from '../../services/coinService';
import { ChartConfiguration, ChartData, Point } from 'chart.js';

@Component({
  selector: 'app-coin-chart',
  imports: [CommonModule, NgChartsModule],
  templateUrl: './coin-chart.html',
  styleUrls: ['./coin-chart.scss'],
  standalone: true,
})
export class CoinChartComponent implements OnInit {
  coinId: string | null = null;
  coinName: string | null = null;
  chartData: ChartData<'line', (number | Point | null)[], unknown> | undefined =
    undefined;
  chartOptions: ChartConfiguration['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    animation: {
      duration: 1000,
      easing: 'easeOutQuart',
    },
    plugins: {
      legend: { display: false },
      tooltip: {
        enabled: true,
        backgroundColor: 'rgba(30, 31, 38, 0.9)',
        titleColor: '#ffffff',
        bodyColor: '#ffffff',
        borderColor: '#4b5cfa',
        borderWidth: 1,
        padding: 12,
        cornerRadius: 8,
        displayColors: false,
        callbacks: {
          label: (context) =>
            `$${context.parsed.y.toLocaleString(undefined, {
              minimumFractionDigits: 2,
              maximumFractionDigits: 2,
            })}`,
          title: (tooltipItems) => tooltipItems[0].label,
        },
      },
    },
    scales: {
      x: {
        grid: {
          display: false,
        },
        ticks: {
          color: '#a0a0a0',
          maxTicksLimit: 7,
          maxRotation: 0,
          font: { size: 12 },
        },
      },
      y: {
        grid: {
          color: 'rgba(255, 255, 255, 0.05)',
          borderDash: [5, 5] as number[],
        } as any,
        ticks: {
          color: '#a0a0a0',
          font: { size: 12 },
          callback: (tickValue: string | number): string => {
            const value =
              typeof tickValue === 'string' ? parseFloat(tickValue) : tickValue;
            return `$${value.toLocaleString(undefined, {
              minimumFractionDigits: 2,
            })}`;
          },
        },
        beginAtZero: false,
      },
    },
    elements: {
      line: {
        tension: 0.4,
        borderWidth: 2,
      },
      point: {
        radius: 0,
        hitRadius: 10,
        hoverRadius: 5,
        hoverBorderWidth: 2,
      },
    },
  };
  loading: boolean = true;
  error: string | null = null;
  selectedDays: number = 7;

  constructor(
    private route: ActivatedRouteSnapshot,
    private coinService: coinService
  ) {}

  ngOnInit(): void {
    this.coinId = this.route.paramMap.get('id');
    this.coinName = this.route.queryParamMap.get('name') || this.coinId;
    if (this.coinId) {
      this.loadChartData(this.coinId, this.selectedDays);
    }
  }

  changePeriod(days: number): void {
    this.selectedDays = days;
    if (this.coinId) {
      this.loadChartData(this.coinId, days);
    }
  }

  private loadChartData(coinId: string, days: number): void {
    this.loading = true;
    this.error = null;
    this.coinService.getMarketChart(coinId, days).subscribe({
      next: (res: { prices: [number, number][] }) => {
        const labels = res.prices.map((price) =>
          new Date(price[0]).toLocaleDateString('en-US', {
            month: 'short',
            day: 'numeric',
            hour: days === 1 ? 'numeric' : undefined,
            minute: days === 1 ? 'numeric' : undefined,
          })
        );
        const data = res.prices.map((price) => price[1]);
        this.chartData = {
          labels,
          datasets: [
            {
              label: `${this.coinName} Price`,
              data,
              borderColor: '#4b5cfa',
              backgroundColor: 'rgba(75, 92, 250, 0.2)',
              fill: true,
            },
          ],
        };
        this.loading = false;
      },
      error: (err: any) => {
        console.error('Chart Error:', err);
        this.error = 'Failed to load chart data';
        this.loading = false;
      },
    });
  }
}

import React, { useMemo } from 'react';
import Highcharts from 'highcharts/highstock';
import HighchartsReact from 'highcharts-react-official';
import stockChartCss from './StockChart.css?inline';

// Export CSS for Shadow DOM injection
export const styles = stockChartCss;

/**
 * StockChart Component for 3forge React Integration
 * 
 * A simple, responsive financial chart using Highstock.
 * Auto-resizes with container - clean and simple!
 * 
 * Props:
 * - symbol: Trading symbol (e.g., "BTC/USD", "AAPL")
 * - data: Array of [timestamp, price] pairs
 */

interface StockChartProps {
  symbol?: string;
  data?: number[][];
}

const StockChart: React.FC<StockChartProps> = ({
  symbol = 'BTC/USD',
  data
}) => {
  
  // Generate sample data if none provided
  const chartData = useMemo(() => {
    if (data && data.length > 0) {
      return data;
    }

    // Generate 100 days of HIGHLY volatile sample price data
    const sampleData: number[][] = [];
    const basePrice = 50000;
    const now = Date.now();
    const dayInMs = 24 * 60 * 60 * 1000;

    let currentPrice = basePrice;
    let trend = 1; // Start with upward trend
    let momentum = 0;
    
    for (let i = 0; i < 100; i++) {
      const time = now - (100 - i) * dayInMs;
      
      // MUCH bigger price movements for dramatic chart
      const volatility = basePrice * 0.08; // 8% daily volatility (very dramatic!)
      const randomChange = (Math.random() - 0.5) * volatility;
      const trendChange = trend * volatility * 0.5; // Stronger trends
      
      // Add momentum that builds up
      momentum = momentum * 0.9 + trendChange * 0.1;
      currentPrice += randomChange + momentum;
      
      // More frequent trend reversals for dramatic peaks/valleys
      if (Math.random() > 0.85) {
        trend *= -1;
        momentum = 0; // Reset momentum on reversal
      }
      
      // Wider price range for more dramatic moves
      currentPrice = Math.max(basePrice * 0.5, Math.min(basePrice * 1.5, currentPrice));
      
      sampleData.push([time, Math.round(currentPrice * 100) / 100]);
    }

    return sampleData;
  }, [data]);

  // Highstock configuration - matching demo style
  const options: Highcharts.Options = useMemo(() => ({
    rangeSelector: {
      selected: 1, // Default to 3m view
      buttons: [{
        type: 'month',
        count: 1,
        text: '1m'
      }, {
        type: 'month',
        count: 3,
        text: '3m'
      }, {
        type: 'month',
        count: 6,
        text: '6m'
      }, {
        type: 'ytd',
        text: 'YTD'
      }, {
        type: 'year',
        count: 1,
        text: '1y'
      }, {
        type: 'all',
        text: 'All'
      }]
    },
    title: {
      text: `${symbol} Stock Price`
    },
    navigator: {
      enabled: true // Show mini chart at bottom like demo
    },
    scrollbar: {
      enabled: true
    },
    credits: {
      enabled: false
    },
    series: [{
      type: 'area',
      name: symbol,
      data: chartData,
      tooltip: {
        valueDecimals: 2
      },
      fillColor: {
        linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
        stops: [
          [0, 'rgba(69, 114, 167, 0.4)'],
          [1, 'rgba(69, 114, 167, 0)']
        ]
      }
    }]
  }), [symbol, chartData]);

  return (
    <div className="candlestick-chart-container">
      {/* Highstock component - matches demo style */}
      <HighchartsReact
        highcharts={Highcharts}
        constructorType={'stockChart'}
        options={options}
      />
    </div>
  );
};

export default StockChart;

import React from 'react';
import traderDetailsCardCss from './TraderDetailsCard.css?inline';

// Export CSS for Shadow DOM injection
export const styles = traderDetailsCardCss;

/**
 * Trader Details Card Component for 3forge React Integration
 * 
 * Displays trader metrics from selected AG Grid row.
 * Shows default state when no trader selected.
 * 
 * Props:
 * - trader: Trader name
 * - desk: Desk name
 * - total_pnl: Total P&L value
 * - trade_count: Number of trades
 */

interface TraderDetailsCardProps {
  trader?: string;
  desk?: string;
  total_pnl?: number;
  trade_count?: number;
}

const TraderDetailsCard: React.FC<TraderDetailsCardProps> = ({
  trader,
  desk,
  total_pnl,
  trade_count
}) => {
  
  const hasData = trader && desk !== undefined;
  const isProfitable = (total_pnl ?? 0) >= 0; // For P&L color only

  return (
    <div className="trader-details-card">
      <div className="card-header">
        <h3 className="card-title">📊 Trader Details</h3>
      </div>

      {!hasData ? (
        // Default state - no trader selected
        <div className="empty-state">
          <div className="empty-icon">👤</div>
          <p className="empty-text">Please select a trader</p>
          <p className="empty-subtext">Click a row in the table below to view trader metrics</p>
        </div>
      ) : (
        // Trader data display
        <div className="card-content">
          {/* Trader Info */}
          <div className="info-section">
            <div className="info-label">Trader</div>
            <div className="info-value trader-name">{trader}</div>
          </div>

          <div className="info-section">
            <div className="info-label">Desk</div>
            <div className="info-value">{desk}</div>
          </div>

          <div className="divider"></div>

          {/* P&L Display */}
          <div className="info-section pnl-section">
            <div className="info-label">Total P&L</div>
            <div className={`info-value pnl-value ${isProfitable ? 'positive' : 'negative'}`}>
              {isProfitable ? '▲' : '▼'} ${Math.abs(total_pnl ?? 0).toLocaleString(undefined, { 
                minimumFractionDigits: 2, 
                maximumFractionDigits: 2 
              })}
            </div>
          </div>

          {/* Trade Count */}
          <div className="info-section">
            <div className="info-label">Total Trades</div>
            <div className="info-value trade-count">{trade_count}</div>
          </div>
        </div>
      )}
    </div>
  );
};

export default TraderDetailsCard;


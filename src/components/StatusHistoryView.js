import React from 'react';

const STATUS_COLORS = {
  PENDING: '#ff9800',
  IN_PROGRESS: '#2196f3',
  RESOLVED: '#4caf50',
  ESCALATED: '#f44336'
};

const STATUS_LABELS = {
  PENDING: 'Pending',
  IN_PROGRESS: 'In Progress',
  RESOLVED: 'Resolved',
  ESCALATED: 'Escalated'
};

export default function StatusHistoryView({ history = [] }) {
  if (!history.length) return null;

  return (
    <div className="status-history-section">
      <h3>Status History</h3>
      <div className="history-timeline">
        {history.map((entry, index) => (
          <div key={entry.id || index} className="history-item">
            <div className="history-indicator">
              <div
                className="history-dot"
                style={{ backgroundColor: STATUS_COLORS[entry.newStatus] }}
              />
              {index !== history.length - 1 && <div className="history-line" />}
            </div>

            <div className="history-content">
              <div className="history-status-change">
                <span className="history-old-status">
                  {STATUS_LABELS[entry.oldStatus]}
                </span>
                <span className="history-arrow">-&gt;</span>
                <span
                  className="history-new-status"
                  style={{ color: STATUS_COLORS[entry.newStatus] }}
                >
                  {STATUS_LABELS[entry.newStatus]}
                </span>
              </div>

              <div className="history-meta">
                <span className="history-actor">
                  {entry.updatedBy === 'SYSTEM' ? 'System' : entry.updatedByName}
                </span>
                <span className="history-time">
                  {new Date(entry.timestamp).toLocaleString()}
                </span>
              </div>

              {entry.remarks && (
                <div className="history-remarks">
                  {entry.remarks}
                </div>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

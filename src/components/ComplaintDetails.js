import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { apiRequest } from '../services/api';
import { inchargeService } from '../services/inchargeService';
import './Complaint.css';

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

export default function ComplaintDetails() {
  const { token } = useAuth();
  const { id } = useParams();
  const [complaint, setComplaint] = useState(null);
  const [statusHistory, setStatusHistory] = useState([]);
  const [showHistory, setShowHistory] = useState(false);

  useEffect(() => {
    const fetchDetails = async () => {
      try {
        const res = await apiRequest(`/complaints/${id}`, {}, token);
        setComplaint(res.data);
      } catch {
      }
    };
    const fetchHistory = async () => {
      try {
        const response = await inchargeService.getStatusHistory(id, token);
        if (response.success) {
          setStatusHistory(response.data);
        }
      } catch {
      }
    };
    fetchDetails();
    fetchHistory();
  }, [id, token]);

  if (!complaint) return <div className="container">Loading...</div>;

  return (
    <div className="container" style={{ maxWidth: 900 }}>
      <h2>Complaint #{complaint.id}</h2>
      <img src={`http://localhost:8080${complaint.imageUrl}`} alt="complaint" style={{ width: '100%', marginTop: 16, borderRadius: 8 }} />
      <p style={{ marginTop: 12 }}><strong>Status:</strong> {complaint.status}</p>
      <p><strong>Address:</strong> {complaint.address}</p>
      <p><strong>Coordinates:</strong> {complaint.latitude}, {complaint.longitude}</p>
      {complaint.assignedToName && <p><strong>Assigned to:</strong> {complaint.assignedToName}</p>}
      {complaint.aiGeneratedContent && (
        <div className="card" style={{ marginTop: 16 }}>
          <strong>Awareness Message</strong>
          <p style={{ marginTop: 8 }}>{complaint.aiGeneratedContent}</p>
        </div>
      )}

      {complaint.lastUpdatedBy && (
        <div className="details-section">
          <h3>Last Update</h3>
          <div className="details-item">
            <strong>Updated By:</strong>
            <p>{complaint.lastUpdatedBy}</p>
          </div>
          <div className="details-item">
            <strong>Updated At:</strong>
            <p>{new Date(complaint.lastUpdatedAt).toLocaleString()}</p>
          </div>
        </div>
      )}

      {complaint.assignedToWard && (
        <div className="details-section">
          <h3>Assigned Authority</h3>
          <div className="details-item">
            <strong>Name:</strong>
            <p>{complaint.assignedToName}</p>
          </div>
          <div className="details-item">
            <strong>Ward:</strong>
            <p>{complaint.assignedToWard}</p>
          </div>
          <div className="details-item">
            <strong>Designation:</strong>
            <p>{complaint.assignedToDesignation}</p>
          </div>
        </div>
      )}

      {complaint.hasResolutionProof && (
        <div className="details-section resolution-section">
          <h3>Resolution Proof</h3>
          <p className="resolution-message">
            This complaint has been resolved. Below is the proof of cleanup.
          </p>
          <div className="proof-image-container">
            <img
              src={`http://localhost:8080${complaint.resolutionProofUrl}`}
              alt="Resolution Proof"
              className="proof-image"
              onError={(e) => {
                e.target.src = 'https://via.placeholder.com/400x300?text=Proof+Not+Available';
              }}
            />
          </div>
        </div>
      )}

      <div className="history-toggle-section">
        <button
          onClick={() => setShowHistory(!showHistory)}
          className="btn btn-secondary"
        >
          {showHistory ? 'Hide Status History' : 'View Status History'}
        </button>
      </div>

      {showHistory && statusHistory.length > 0 && (
        <div className="status-history-section">
          <h3>Status History</h3>
          <div className="history-timeline">
            {statusHistory.map((entry, index) => (
              <div key={entry.id} className="history-item">
                <div className="history-indicator">
                  <div
                    className="history-dot"
                    style={{ backgroundColor: STATUS_COLORS[entry.newStatus] }}
                  />
                  {index !== statusHistory.length - 1 && <div className="history-line" />}
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
      )}
    </div>
  );
}

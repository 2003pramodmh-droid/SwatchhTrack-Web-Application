import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { inchargeService } from '../services/inchargeService';
import './InCharge.css';

export default function InChargeDashboard() {
  const { token } = useAuth();
  const [stats, setStats] = useState(null);
  const [complaints, setComplaints] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    const load = async () => {
      try {
        const statsRes = await inchargeService.getDashboardStats(token);
        const complaintsRes = await inchargeService.getAssignedComplaints(token);
        setStats(statsRes.data);
        setComplaints(complaintsRes.data || []);
      } catch (err) {
        setError(err.message || String(err));
      }
    };
    load();
  }, [token]);

  return (
    <div className="container incharge-dashboard">
      <h2>In-Charge Dashboard</h2>
      {error && <div className="alert alert-error">{error}</div>}

      {stats && (
        <div className="stats-grid">
          <div className="stat-card">
            <div className="stat-value">{stats.totalComplaints}</div>
            <div className="stat-label">Total</div>
          </div>
          <div className="stat-card pending">
            <div className="stat-value">{stats.pending}</div>
            <div className="stat-label">Pending</div>
          </div>
          <div className="stat-card in-progress">
            <div className="stat-value">{stats.inProgress}</div>
            <div className="stat-label">In Progress</div>
          </div>
          <div className="stat-card resolved">
            <div className="stat-value">{stats.resolved}</div>
            <div className="stat-label">Resolved</div>
          </div>
          <div className="stat-card escalated">
            <div className="stat-value">{stats.escalated}</div>
            <div className="stat-label">Escalated</div>
          </div>
        </div>
      )}

      <div className="section-header">
        <h3>Assigned Complaints</h3>
        <Link to="/incharge/complaints" className="btn btn-secondary">View All</Link>
      </div>

      <div className="complaint-grid">
        {complaints.slice(0, 6).map(c => (
          <Link key={c.id} to={`/incharge/complaint/${c.id}`} className="complaint-card">
            <img src={`http://localhost:8080${c.imageUrl}`} alt="complaint" />
            <div className="content">
              <span className={`status ${c.status}`}>{c.status}</span>
              <p style={{ marginTop: 8, fontWeight: 600 }}>{c.address}</p>
              <p style={{ marginTop: 6, color: '#666' }}>{c.assignedToWard || ''}</p>
            </div>
          </Link>
        ))}
      </div>
    </div>
  );
}

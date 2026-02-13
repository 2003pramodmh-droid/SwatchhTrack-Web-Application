import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { inchargeService } from '../services/inchargeService';
import './InCharge.css';

const STATUS_OPTIONS = ['ALL', 'PENDING', 'IN_PROGRESS', 'RESOLVED', 'ESCALATED'];

export default function InChargeComplaintList() {
  const { token } = useAuth();
  const [complaints, setComplaints] = useState([]);
  const [status, setStatus] = useState('ALL');
  const [error, setError] = useState(null);

  useEffect(() => {
    const load = async () => {
      try {
        const res = await inchargeService.getAssignedComplaints(token);
        setComplaints(res.data || []);
      } catch (err) {
        setError(err.message || String(err));
      }
    };
    load();
  }, [token]);

  const filtered = status === 'ALL'
    ? complaints
    : complaints.filter(c => c.status === status);

  return (
    <div className="container incharge-complaints">
      <h2>Assigned Complaints</h2>
      {error && <div className="alert alert-error">{error}</div>}

      <div className="filter-row">
        <label>Status Filter</label>
        <select value={status} onChange={(e) => setStatus(e.target.value)}>
          {STATUS_OPTIONS.map(s => (
            <option key={s} value={s}>{s}</option>
          ))}
        </select>
      </div>

      <div className="complaint-grid">
        {filtered.map(c => (
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

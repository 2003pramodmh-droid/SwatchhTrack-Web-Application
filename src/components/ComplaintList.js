import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { apiRequest } from '../services/api';

export default function ComplaintList() {
  const { token } = useAuth();
  const [complaints, setComplaints] = useState([]);

  useEffect(() => {
    apiRequest('/complaints/my-complaints', {}, token)
      .then(res => setComplaints(res.data || []))
      .catch(() => {});
  }, [token]);

  return (
    <div className="container">
      <h2>My Complaints</h2>
      <div className="complaint-grid" style={{ marginTop: 20 }}>
        {complaints.map(c => (
          <Link key={c.id} to={`/complaint/${c.id}`} className="complaint-card">
            <img src={`http://localhost:8080${c.imageUrl}`} alt="complaint" />
            <div className="content">
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <strong>#{c.id}</strong>
                <span className={`status ${c.status}`}>{c.status}</span>
              </div>
              <p style={{ marginTop: 8, color: '#555' }}>{c.address}</p>
            </div>
          </Link>
        ))}
      </div>
    </div>
  );
}

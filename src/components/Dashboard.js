import React, { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { apiRequest } from '../services/api';

export default function Dashboard() {
  const { token } = useAuth();
  const [stats, setStats] = useState({ total: 0, pending: 0, inProgress: 0, resolved: 0, escalated: 0 });

  useEffect(() => {
    apiRequest('/complaints/my-complaints', {}, token)
      .then(res => {
        const list = res.data || [];
        const count = { total: list.length, pending: 0, inProgress: 0, resolved: 0, escalated: 0 };
        list.forEach(c => {
          if (c.status === 'PENDING') count.pending++;
          if (c.status === 'IN_PROGRESS') count.inProgress++;
          if (c.status === 'RESOLVED') count.resolved++;
          if (c.status === 'ESCALATED') count.escalated++;
        });
        setStats(count);
      })
      .catch(() => {});
  }, [token]);

  return (
    <div className="container">
      <h2>Dashboard</h2>
      <div className="cards" style={{ marginTop: 20 }}>
        <div className="card"><h3>Total</h3><p>{stats.total}</p></div>
        <div className="card"><h3>Pending</h3><p>{stats.pending}</p></div>
        <div className="card"><h3>In Progress</h3><p>{stats.inProgress}</p></div>
        <div className="card"><h3>Resolved</h3><p>{stats.resolved}</p></div>
        <div className="card"><h3>Escalated</h3><p>{stats.escalated}</p></div>
      </div>
    </div>
  );
}

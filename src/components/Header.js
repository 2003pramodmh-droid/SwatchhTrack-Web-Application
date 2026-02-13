import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Header() {
  const { isAuthenticated, logout, userRole } = useAuth();
  return (
    <header className="header">
      <div className="header-inner container">
        <Link to="/" className="brand">SwachhTrack</Link>
        <nav className="nav">
          <Link to="/">Home</Link>
          {isAuthenticated && userRole !== 'INCHARGE' && <Link to="/dashboard">Dashboard</Link>}
          {isAuthenticated && userRole === 'INCHARGE' && <Link to="/incharge/dashboard">In-Charge Dashboard</Link>}
          {isAuthenticated && userRole !== 'INCHARGE' && <Link to="/complaints">My Complaints</Link>}
          {isAuthenticated && userRole !== 'INCHARGE' && <Link to="/complaint/new">Report</Link>}
          {!isAuthenticated && <Link to="/login">User Login</Link>}
          {!isAuthenticated && <Link to="/incharge/login">In-Charge Login</Link>}
          {isAuthenticated && <button className="btn-link" onClick={logout}>Logout</button>}
          {isAuthenticated && <span className={`role-badge ${userRole}`}>{userRole}</span>}
        </nav>
      </div>
    </header>
  );
}

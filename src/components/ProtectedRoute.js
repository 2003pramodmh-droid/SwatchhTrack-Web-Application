import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function ProtectedRoute({ children, requireRole }) {
  const { isAuthenticated, userRole, loading } = useAuth();

  if (loading) {
    return <div className="container">Loading...</div>;
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" />;
  }

  if (requireRole && userRole !== requireRole) {
    return <Navigate to={userRole === 'INCHARGE' ? '/incharge/dashboard' : '/dashboard'} />;
  }

  return children;
}

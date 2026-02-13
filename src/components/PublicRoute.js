import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function PublicRoute({ children }) {
  const { isAuthenticated, userRole, loading } = useAuth();

  if (loading) {
    return <div className="container">Loading...</div>;
  }

  if (isAuthenticated) {
    return <Navigate to={userRole === 'INCHARGE' ? '/incharge/dashboard' : '/dashboard'} />;
  }

  return children;
}

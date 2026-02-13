import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import './App.css';

import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import PublicRoute from './components/PublicRoute';

import Header from './components/Header';
import Footer from './components/Footer';
import Home from './components/Home';
import Login from './components/Login';
import Dashboard from './components/Dashboard';
import ComplaintForm from './components/ComplaintForm';
import ComplaintList from './components/ComplaintList';
import ComplaintDetails from './components/ComplaintDetails';
import InChargeLogin from './components/InChargeLogin';
import InChargeDashboard from './components/InChargeDashboard';
import InChargeComplaintList from './components/InChargeComplaintList';
import InChargeComplaintDetails from './components/InChargeComplaintDetails';

function AppContent() {
  return (
    <div className="app">
      <Header />
      <main className="main-content">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route
            path="/login"
            element={
              <PublicRoute>
                <Login />
              </PublicRoute>
            }
          />
          <Route
            path="/dashboard"
            element={
              <ProtectedRoute requireRole="USER">
                <Dashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path="/complaint/new"
            element={
              <ProtectedRoute requireRole="USER">
                <ComplaintForm />
              </ProtectedRoute>
            }
          />
          <Route
            path="/complaints"
            element={
              <ProtectedRoute requireRole="USER">
                <ComplaintList />
              </ProtectedRoute>
            }
          />
          <Route
            path="/complaint/:id"
            element={
              <ProtectedRoute requireRole="USER">
                <ComplaintDetails />
              </ProtectedRoute>
            }
          />
          <Route
            path="/incharge/login"
            element={
              <PublicRoute>
                <InChargeLogin />
              </PublicRoute>
            }
          />
          <Route
            path="/incharge/dashboard"
            element={
              <ProtectedRoute requireRole="INCHARGE">
                <InChargeDashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path="/incharge/complaints"
            element={
              <ProtectedRoute requireRole="INCHARGE">
                <InChargeComplaintList />
              </ProtectedRoute>
            }
          />
          <Route
            path="/incharge/complaint/:id"
            element={
              <ProtectedRoute requireRole="INCHARGE">
                <InChargeComplaintDetails />
              </ProtectedRoute>
            }
          />
          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      </main>
      <Footer />
    </div>
  );
}

function App() {
  return (
    <Router>
      <AuthProvider>
        <AppContent />
      </AuthProvider>
    </Router>
  );
}

export default App;

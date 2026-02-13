import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { apiRequest } from '../services/api';

export default function Login() {
  const { login } = useAuth();
  const [mobileNumber, setMobileNumber] = useState('');
  const [otpCode, setOtpCode] = useState('');
  const [stage, setStage] = useState('request');
  const [message, setMessage] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  const requestOtp = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const res = await apiRequest('/auth/request-otp', {
        method: 'POST',
        body: JSON.stringify({ mobileNumber })
      });
      setMessage(`OTP sent (simulated): ${res.data}`);
      setStage('verify');
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const verifyOtp = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const res = await apiRequest('/auth/verify-otp', {
        method: 'POST',
        body: JSON.stringify({ mobileNumber, otpCode })
      });
      login(res.data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container" style={{ maxWidth: 520 }}>
      <h2>Login with OTP</h2>
      {error && <div className="alert alert-error">{error}</div>}
      {message && <div className="alert alert-success">{message}</div>}

      {stage === 'request' && (
        <form onSubmit={requestOtp}>
          <div className="form-group">
            <label>Mobile Number</label>
            <input value={mobileNumber} onChange={(e) => setMobileNumber(e.target.value)} placeholder="9876543210" />
          </div>
          <button className="btn btn-primary" disabled={loading}>Request OTP</button>
        </form>
      )}

      {stage === 'verify' && (
        <form onSubmit={verifyOtp}>
          <div className="form-group">
            <label>Mobile Number</label>
            <input value={mobileNumber} onChange={(e) => setMobileNumber(e.target.value)} />
          </div>
          <div className="form-group">
            <label>OTP</label>
            <input value={otpCode} onChange={(e) => setOtpCode(e.target.value)} />
          </div>
          <button className="btn btn-primary" disabled={loading}>Verify & Login</button>
        </form>
      )}
    </div>
  );
}

import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { inchargeService } from '../services/inchargeService';

export default function InChargeLogin() {
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
      const res = await inchargeService.requestOTP(mobileNumber);
      setMessage(`OTP sent (simulated): ${res.data}`);
      setStage('verify');
    } catch (err) {
      setError(err.message || String(err));
    } finally {
      setLoading(false);
    }
  };

  const verifyOtp = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const res = await inchargeService.verifyOTP(mobileNumber, otpCode);
      login({
        token: res.data.token,
        mobileNumber,
        role: res.data.role || 'INCHARGE'
      });
    } catch (err) {
      setError(err.message || String(err));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container" style={{ maxWidth: 520 }}>
      <h2>In-Charge Login</h2>
      {error && <div className="alert alert-error">{error}</div>}
      {message && <div className="alert alert-success">{message}</div>}

      {stage === 'request' && (
        <form onSubmit={requestOtp}>
          <div className="form-group">
            <label>Mobile Number</label>
            <input value={mobileNumber} onChange={(e) => setMobileNumber(e.target.value)} placeholder="9876543211" />
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

import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { apiRequest } from '../services/api';
import { useNavigate } from 'react-router-dom';

export default function ComplaintForm() {
  const { token } = useAuth();
  const navigate = useNavigate();
  const [image, setImage] = useState(null);
  const [latitude, setLatitude] = useState('');
  const [longitude, setLongitude] = useState('');
  const [address, setAddress] = useState('');
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  const getLocation = () => {
    if (!navigator.geolocation) {
      setError('Geolocation not supported');
      return;
    }
    navigator.geolocation.getCurrentPosition(
      (pos) => {
        setLatitude(pos.coords.latitude.toFixed(6));
        setLongitude(pos.coords.longitude.toFixed(6));
      },
      () => setError('Unable to fetch location')
    );
  };

  const submit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const form = new FormData();
      form.append('image', image);
      form.append('latitude', latitude);
      form.append('longitude', longitude);
      form.append('address', address);

      await apiRequest('/complaints', { method: 'POST', body: form }, token);
      navigate('/complaints');
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container" style={{ maxWidth: 720 }}>
      <h2>Report Waste Issue</h2>
      {error && <div className="alert alert-error">{error}</div>}
      <form onSubmit={submit}>
        <div className="form-group">
          <label>Image</label>
          <input type="file" accept="image/*" capture="environment" onChange={(e) => setImage(e.target.files[0])} />
        </div>
        <div className="form-group">
          <label>Location</label>
          <button type="button" className="btn btn-secondary" onClick={getLocation}>Get Current Location</button>
        </div>
        <div className="form-group">
          <label>Latitude</label>
          <input value={latitude} onChange={(e) => setLatitude(e.target.value)} />
        </div>
        <div className="form-group">
          <label>Longitude</label>
          <input value={longitude} onChange={(e) => setLongitude(e.target.value)} />
        </div>
        <div className="form-group">
          <label>Address</label>
          <textarea value={address} onChange={(e) => setAddress(e.target.value)} placeholder="Enter address/landmark" />
        </div>
        <button className="btn btn-primary" disabled={loading || !image}>Submit Complaint</button>
      </form>
    </div>
  );
}

import React, { useState } from 'react';

export default function ResolutionProofModal({ isOpen, onClose, onSubmit }) {
  const [proofImage, setProofImage] = useState(null);
  const [remarks, setRemarks] = useState('');
  const [latitude, setLatitude] = useState('');
  const [longitude, setLongitude] = useState('');
  const [error, setError] = useState(null);

  if (!isOpen) return null;

  return (
    <div className="modal-overlay">
      <div className="modal">
        <h3>Upload Resolution Proof</h3>
        <form onSubmit={(e) => {
          e.preventDefault();
          if (!proofImage) return;
          if (!latitude || !longitude) {
            setError('Location is required for proof upload.');
            return;
          }
          onSubmit(proofImage, latitude, longitude, remarks);
        }}>
          {error && <div className="alert alert-error">{error}</div>}
          <div className="form-group">
            <label>Proof Image</label>
            <input type="file" accept="image/*" onChange={(e) => setProofImage(e.target.files[0])} />
          </div>
          <div className="form-group">
            <label>Proof Location</label>
            <button
              type="button"
              className="btn btn-secondary"
              onClick={() => {
                setError(null);
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
              }}
            >
              Get Current Location
            </button>
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
            <label>Remarks (optional)</label>
            <textarea value={remarks} onChange={(e) => setRemarks(e.target.value)} />
          </div>
          <div className="modal-actions">
            <button className="btn btn-secondary" type="button" onClick={onClose}>Cancel</button>
            <button className="btn btn-primary" type="submit">Upload & Resolve</button>
          </div>
        </form>
      </div>
    </div>
  );
}

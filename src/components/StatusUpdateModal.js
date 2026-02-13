import React, { useEffect, useState } from 'react';

const NEXT_STATUS = {
  PENDING: ['IN_PROGRESS'],
  IN_PROGRESS: ['RESOLVED'],
  RESOLVED: [],
  ESCALATED: []
};

export default function StatusUpdateModal({ isOpen, onClose, onSubmit, currentStatus }) {
  const [status, setStatus] = useState('');
  const [remarks, setRemarks] = useState('');

  useEffect(() => {
    const allowed = NEXT_STATUS[currentStatus] || [];
    setStatus(allowed[0] || '');
    setRemarks('');
  }, [currentStatus, isOpen]);

  if (!isOpen) return null;

  const allowed = NEXT_STATUS[currentStatus] || [];

  return (
    <div className="modal-overlay">
      <div className="modal">
        <h3>Update Status</h3>
        {allowed.length === 0 ? (
          <p>No valid status transitions available.</p>
        ) : (
          <form onSubmit={(e) => {
            e.preventDefault();
            onSubmit(status, remarks);
          }}>
            <div className="form-group">
              <label>New Status</label>
              <select value={status} onChange={(e) => setStatus(e.target.value)}>
                {allowed.map(s => (
                  <option key={s} value={s}>{s}</option>
                ))}
              </select>
            </div>
            <div className="form-group">
              <label>Remarks (optional)</label>
              <textarea value={remarks} onChange={(e) => setRemarks(e.target.value)} />
            </div>
            <div className="modal-actions">
              <button className="btn btn-secondary" type="button" onClick={onClose}>Cancel</button>
              <button className="btn btn-primary" type="submit">Update</button>
            </div>
          </form>
        )}
      </div>
    </div>
  );
}

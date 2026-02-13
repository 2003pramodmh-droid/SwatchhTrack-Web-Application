import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { inchargeService } from '../services/inchargeService';
import StatusUpdateModal from './StatusUpdateModal';
import ResolutionProofModal from './ResolutionProofModal';
import StatusHistoryView from './StatusHistoryView';
import './InCharge.css';
import './Complaint.css';

export default function InChargeComplaintDetails() {
  const { token } = useAuth();
  const { id } = useParams();
  const [complaint, setComplaint] = useState(null);
  const [history, setHistory] = useState([]);
  const [error, setError] = useState(null);
  const [showStatusModal, setShowStatusModal] = useState(false);
  const [showProofModal, setShowProofModal] = useState(false);

  const loadDetails = async () => {
    try {
      const res = await inchargeService.getComplaintDetails(id, token);
      setComplaint(res.data);
    } catch (err) {
      setError(err.message || String(err));
    }
  };

  const loadHistory = async () => {
    try {
      const res = await inchargeService.getStatusHistory(id, token);
      setHistory(res.data || []);
    } catch (err) {
      setError(err.message || String(err));
    }
  };

  useEffect(() => {
    loadDetails();
    loadHistory();
  }, [id, token]);

  const handleUpdateStatus = async (status, remarks) => {
    try {
      await inchargeService.updateStatus(id, status, remarks, token);
      setShowStatusModal(false);
      await loadDetails();
      await loadHistory();
    } catch (err) {
      setError(err.message || String(err));
    }
  };

  const handleUploadProof = async (proofImage, latitude, longitude, remarks) => {
    try {
      await inchargeService.uploadResolutionProof(id, proofImage, latitude, longitude, remarks, token);
      setShowProofModal(false);
      await loadDetails();
      await loadHistory();
    } catch (err) {
      setError(err.message || String(err));
    }
  };

  if (!complaint) return <div className="container">Loading...</div>;

  return (
    <div className="container incharge-complaint-details">
      <h2>Complaint #{complaint.id}</h2>
      {error && <div className="alert alert-error">{error}</div>}

      <img src={`http://localhost:8080${complaint.imageUrl}`} alt="complaint" className="detail-image" />
      <div className="detail-grid">
        <div><strong>Status:</strong> {complaint.status}</div>
        <div><strong>Address:</strong> {complaint.address}</div>
        <div><strong>Coordinates:</strong> {complaint.latitude}, {complaint.longitude}</div>
        <div><strong>Ward:</strong> {complaint.assignedToWard || 'N/A'}</div>
      </div>

      <div className="detail-actions">
        {complaint.status === 'PENDING' && (
          <button className="btn btn-primary" onClick={() => setShowStatusModal(true)}>
            Update Status
          </button>
        )}
        {complaint.status === 'IN_PROGRESS' && !complaint.hasResolutionProof && (
          <button className="btn btn-primary" onClick={() => setShowProofModal(true)}>
            Upload Resolution Proof
          </button>
        )}
      </div>

      {complaint.aiGeneratedContent && (
        <div className="card" style={{ marginTop: 16 }}>
          <strong>Escalation Content</strong>
          <pre style={{ marginTop: 8, whiteSpace: 'pre-wrap' }}>{complaint.aiGeneratedContent}</pre>
        </div>
      )}

      <StatusHistoryView history={history} />

      <StatusUpdateModal
        isOpen={showStatusModal}
        onClose={() => setShowStatusModal(false)}
        onSubmit={handleUpdateStatus}
        currentStatus={complaint.status}
      />

      <ResolutionProofModal
        isOpen={showProofModal}
        onClose={() => setShowProofModal(false)}
        onSubmit={handleUploadProof}
      />
    </div>
  );
}

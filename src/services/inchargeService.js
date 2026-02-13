import { apiRequest } from './api';
import { API_ENDPOINTS } from '../utils/constants';

export const inchargeService = {
  requestOTP: async (mobileNumber) => {
    return apiRequest(API_ENDPOINTS.INCHARGE_AUTH.REQUEST_OTP, {
      method: 'POST',
      body: JSON.stringify({ mobileNumber })
    });
  },

  verifyOTP: async (mobileNumber, otpCode) => {
    return apiRequest(API_ENDPOINTS.INCHARGE_AUTH.VERIFY_OTP, {
      method: 'POST',
      body: JSON.stringify({ mobileNumber, otpCode })
    });
  },

  getDashboardStats: async (token) => {
    return apiRequest(API_ENDPOINTS.INCHARGE_DASHBOARD.STATS, {}, token);
  },

  getAssignedComplaints: async (token) => {
    return apiRequest(API_ENDPOINTS.INCHARGE_DASHBOARD.COMPLAINTS, {}, token);
  },

  getAssignedComplaintsByStatus: async (status, token) => {
    return apiRequest(`${API_ENDPOINTS.INCHARGE_DASHBOARD.BY_STATUS}?status=${status}`, {}, token);
  },

  getComplaintDetails: async (id, token) => {
    return apiRequest(API_ENDPOINTS.INCHARGE_COMPLAINTS.DETAILS(id), {}, token);
  },

  updateStatus: async (id, status, remarks, token) => {
    return apiRequest(API_ENDPOINTS.INCHARGE_COMPLAINTS.UPDATE_STATUS(id), {
      method: 'PUT',
      body: JSON.stringify({ status, remarks })
    }, token);
  },

  uploadResolutionProof: async (id, proofImage, latitude, longitude, remarks, token) => {
    const formData = new FormData();
    formData.append('proofImage', proofImage);
    formData.append('latitude', latitude);
    formData.append('longitude', longitude);
    if (remarks) {
      formData.append('remarks', remarks);
    }
    return apiRequest(API_ENDPOINTS.RESOLUTION_PROOF.UPLOAD(id), {
      method: 'POST',
      body: formData
    }, token);
  },

  getStatusHistory: async (id, token) => {
    return apiRequest(API_ENDPOINTS.STATUS_HISTORY.GET(id), {}, token);
  }
};

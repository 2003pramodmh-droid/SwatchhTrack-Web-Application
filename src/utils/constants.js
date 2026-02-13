export const API_ENDPOINTS = {
  AUTH: {
    REQUEST_OTP: '/auth/request-otp',
    VERIFY_OTP: '/auth/verify-otp'
  },
  COMPLAINTS: {
    CREATE: '/complaints',
    MY: '/complaints/my-complaints',
    ALL: '/complaints/all',
    DETAILS: (id) => `/complaints/${id}`,
    UPDATE_STATUS: (id) => `/complaints/${id}/status`
  },
  INCHARGE_AUTH: {
    REQUEST_OTP: '/incharge-auth/request-otp',
    VERIFY_OTP: '/incharge-auth/verify-otp'
  },
  INCHARGE_DASHBOARD: {
    STATS: '/incharge/dashboard/stats',
    COMPLAINTS: '/incharge/dashboard/complaints',
    BY_STATUS: '/incharge/dashboard/complaints/by-status'
  },
  INCHARGE_COMPLAINTS: {
    UPDATE_STATUS: (id) => `/incharge/complaints/${id}/status`,
    DETAILS: (id) => `/incharge/complaints/${id}`
  },
  RESOLUTION_PROOF: {
    UPLOAD: (id) => `/incharge/resolution-proof/${id}`,
    GET: (id) => `/incharge/resolution-proof/${id}`
  },
  STATUS_HISTORY: {
    GET: (id) => `/complaints/${id}/history`
  }
};

export const USER_ROLES = {
  USER: 'USER',
  INCHARGE: 'INCHARGE'
};

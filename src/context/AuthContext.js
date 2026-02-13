import React, { createContext, useContext, useEffect, useState } from 'react';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [token, setToken] = useState(null);
  const [user, setUser] = useState(null);
  const [userRole, setUserRole] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const stored = localStorage.getItem('swachhtrack_auth');
    if (stored) {
      const parsed = JSON.parse(stored);
      setToken(parsed.token);
      setUser(parsed.user || null);
      setUserRole(parsed.role || null);
    }
    setLoading(false);
  }, []);

  const login = (auth) => {
    const role = auth.role || 'USER';
    const userObj = { id: auth.userId || null, mobileNumber: auth.mobileNumber };
    setToken(auth.token);
    setUser(userObj);
    setUserRole(role);
    localStorage.setItem('swachhtrack_auth', JSON.stringify({
      token: auth.token,
      user: userObj,
      role
    }));
  };

  const logout = () => {
    setToken(null);
    setUser(null);
    setUserRole(null);
    localStorage.removeItem('swachhtrack_auth');
  };

  return (
    <AuthContext.Provider value={{
      token,
      user,
      userRole,
      login,
      logout,
      isAuthenticated: !!token,
      loading
    }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}

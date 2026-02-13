import React from 'react';
import { Link } from 'react-router-dom';

export default function Home() {
  return (
    <div>
      <section className="hero">
        <h1>SwachhTrack</h1>
        <p>Report waste issues with photos and GPS. Track status, improve accountability, keep your city clean.</p>
        <div>
          <Link className="btn btn-primary" to="/login">Citizen Login</Link>
          <Link className="btn btn-secondary" style={{ marginLeft: 12 }} to="/incharge/login">In-Charge Login</Link>
        </div>
      </section>

      <section className="container" style={{ marginTop: 30 }}>
        <div className="cards">
          <div className="card">
            <h3>Report Fast</h3>
            <p>Capture an image, add location, and submit in minutes.</p>
          </div>
          <div className="card">
            <h3>Track Progress</h3>
            <p>Check complaint status updates in one place.</p>
          </div>
          <div className="card">
            <h3>Accountability</h3>
            <p>Escalations are generated if delays occur.</p>
          </div>
        </div>
      </section>
    </div>
  );
}

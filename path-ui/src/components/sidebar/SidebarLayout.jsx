import { useState, useEffect } from 'react';
import './SidebarLayout.css'; // Link to the CSS file below
import Form from '../form/Form';

export default function SidebarLayout({setResponse}) {
  // Sidebar Functionality
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const handleRoutes = (response) => {
    setResponse(response);
  }

  return (
    <div className="layout">
      {/* Toggle Button */}
      <button
        className="sidebar-toggle"
        onClick={() => setSidebarOpen(!sidebarOpen)}
      >
        {sidebarOpen ? '✕' : '☰'}
      </button>

      {/* Sidebar */}
      <div className={`sidebar ${sidebarOpen ? 'open' : ''}`}>
        <div className='sidebar-content'>
          <Form routes={handleRoutes}></Form>
        </div>
      </div>
    </div>
  );
}

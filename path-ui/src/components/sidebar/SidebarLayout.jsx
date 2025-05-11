import { useState } from 'react';
import './SidebarLayout.css'; // Link to the CSS file below

export default function SidebarLayout() {
  const [sidebarOpen, setSidebarOpen] = useState(false);

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
          
        </div>
      </div>
    </div>
  );
}

import { useState } from 'react';
import './SidebarLayout.css';
import Form from '../form/Form';
import RoutesCardGroup from '../routescardgroup/RoutesCardGroup';

export default function SidebarLayout({ setResponse }) {
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [routes, setRoutes] = useState([]);

  const handleRoutes = (response) => {
    if (setResponse) {
      setResponse(response);
    }
    setRoutes(response);
  };

  return (
    <div className="layout">
      <button
        className="sidebar-toggle"
        onClick={() => setSidebarOpen(!sidebarOpen)}
      >
        {sidebarOpen ? '✕' : '☰'}
      </button>

      <div className={`sidebar ${sidebarOpen ? 'open' : ''}`}>
        <div className='sidebar-content'>
          <Form onRoutesGenerated={handleRoutes} />
          <RoutesCardGroup routes={routes} onRouteSelect={handleRoutes} />
        </div>
      </div>
    </div>
  );
}
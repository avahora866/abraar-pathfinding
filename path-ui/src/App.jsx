import './App.css'
import 'leaflet/dist/leaflet.css';
import SidebarLayout from './components/sidebar/SidebarLayout';
import Map from './components/map/map';
import { useState, useMemo } from 'react';


function App() {
  const [routes, setRoutes] = useState([]);
  const [animKey, setAnimKey] = useState(0);

  const handleResponse = (response) => {
    setRoutes(response);
    setAnimKey(prev => prev + 1)
  }

  return (
    <>
      <SidebarLayout setResponse={handleResponse}></SidebarLayout>
      <Map routes={routes} animKey={animKey}></Map>
    </>
  )
}

export default App

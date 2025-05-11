import './App.css'
import 'leaflet/dist/leaflet.css';
import SidebarLayout from './components/sidebar/SidebarLayout';
import Map from './components/map/map';
import { useState, useMemo } from 'react';


function App() {
  const [routes, setRoutes] = useState(
    [
    ]
  );
  const [animKey, setAnimKey] = useState(0);


  const changeRoute = () => {
    let replaced = routes.map(x => {
      if (x.name === "Route 2") {
        return { ...x, selected: true };
      } else if (x.selected === true) {
        return { ...x, selected: false };
      }
      return x;
    });
    setRoutes(replaced)
    setAnimKey(prev => prev + 1)
  }

  const handleResponse = (response) => {
    setRoutes(response);
    setAnimKey(prev => prev + 1)
  }

  return (
    <>
      <SidebarLayout setResponse={handleResponse}></SidebarLayout>
      <Map routes={routes} animKey={animKey}></Map>
      <button onClick={changeRoute} style={{ "margin": "40px" }}>Change route</button>

    </>
  )
}

export default App

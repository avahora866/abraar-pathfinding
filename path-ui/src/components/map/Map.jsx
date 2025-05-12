import './Map.css'
import { MapContainer, TileLayer, Polyline } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import { useState, useMemo } from 'react';


function Map({ routes, animKey }) {
  const selectedRoute = useMemo(() => {
    return routes.filter(route => route.selected).map((r) => r.routes)
  }, [routes]);

  const otherRoutes = useMemo(() => {
    return routes.filter(route => !route.selected).map((r) => r.routes)
  }, [routes]);


  const blueOptions = { color: '#166af2', weight: 5, smoothFactor: 4, noClip: false }
  const lightBlueOptions = { color: '#78abff', weight: 4, smoothFactor: 4 }

  return (
    <>
      <MapContainer center={[52.637603, -1.112897]} zoom={13} style={{ height: "90vh", width: "100%" }} zoomControl={false}>
        <TileLayer
          attribution='&copy; OpenStreetMap contributors'
          url='https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png'
        />
        <Polyline key={`other-${animKey}`} pathOptions={lightBlueOptions} positions={otherRoutes} className="animated-polyline" />
        <Polyline key={`selected-${animKey}`} pathOptions={blueOptions} positions={selectedRoute} className="animated-polyline" />
      </MapContainer>
      {/* <button onClick={changeRoute} style={{ "margin": "40px" }}>Change route</button> */}
    </>
  )
}

export default Map

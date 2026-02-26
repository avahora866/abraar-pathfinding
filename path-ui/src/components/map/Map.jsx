import './Map.css';
import { MapContainer, TileLayer, Polyline } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import { useMemo } from 'react';

export default function Map({ routes, animKey }) {
  const selectedRoute = useMemo(() => {
    return routes.filter(route => route.selected).map((r) => r.routes);
  }, [routes]);

  const otherRoutes = useMemo(() => {
    return routes.filter(route => !route.selected).map((r) => r.routes);
  }, [routes]);

  const blueOptions = { smoothFactor: 4, noClip: false, className: "animated-polyline selected-route" };
  const lightBlueOptions = { smoothFactor: 4, className: "animated-polyline other-route" };

  return (
    <MapContainer center={[52.637603, -1.112897]} zoom={13} className="map-container" zoomControl={false}>
      <TileLayer
        attribution='&copy; OpenStreetMap contributors'
        url='https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png'
      />
      <Polyline key={`other-${animKey}`} pathOptions={lightBlueOptions} positions={otherRoutes} />
      <Polyline key={`selected-${animKey}`} pathOptions={blueOptions} positions={selectedRoute} />
    </MapContainer>
  );
}
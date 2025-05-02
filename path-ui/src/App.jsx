import './App.css'
import { MapContainer, TileLayer, Polyline } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import { useState, useMemo } from 'react';


function App() {
  const [response, setResponse] = useState(
    [
      {
        "name": "Route 1",
        "selected": true,
        "routes": [
          [52.637603, -1.112897],
          [52.636493, -1.111247],
          [52.637124, -1.108847],
          [52.637054, -1.108761],
          [52.637058, -1.108474],
          [52.636769, -1.108621],
          [52.635824, -1.107943],
          [52.635581, -1.107621],
          [52.635497, -1.107401],
          [52.635405, -1.106913],
          [52.635413, -1.106714],
          [52.635620, -1.105666],
          [52.635664, -1.105204],
          [52.635584, -1.104595],
          [52.635166, -1.103375],
          [52.632249, -1.105918],
          [52.630973, -1.102372],
          [52.630435, -1.099405],
          [52.629872, -1.099513],
          [52.628420, -1.100119]
        ]
      },
      {
        "name": "Route 2",
        "selected": false,
        "routes": [
          [52.637603, -1.112897],
          [52.636493, -1.111247],
          [52.637124, -1.108847],
          [52.637054, -1.108761],
          [52.637058, -1.108474],
          [52.636769, -1.108621],
          [52.635824, -1.107943],
          [52.635581, -1.107621],
          [52.635497, -1.107401],
          [52.635405, -1.106913],
          [52.635413, -1.106714],
          [52.635620, -1.105666],
          [52.635664, -1.105204],
          [52.635584, -1.104595],
          [52.635166, -1.103375],
          [52.632249, -1.105918],
          [52.631060, -1.106781],
          [52.629149, -1.107559],
          [52.629123, -1.107280],
          [52.628172, -1.104319],
          [52.627378, -1.100779],
          [52.628420, -1.100119]
        ]
      },
      {
        "name": "Route 3",
        "selected": false,
        "routes": [
          [52.637603, -1.112897],
          [52.636493, -1.111247],
          [52.637124, -1.108847],
          [52.637054, -1.108761],
          [52.637058, -1.108474],
          [52.636769, -1.108621],
          [52.635824, -1.107943],
          [52.635581, -1.107621],
          [52.635497, -1.107401],
          [52.635405, -1.106913],
          [52.635413, -1.106714],
          [52.635620, -1.105666],
          [52.635664, -1.105204],
          [52.635584, -1.104595],
          [52.635166, -1.103375],
          [52.632249, -1.105918]
        ]
      }
    ]
  );

  const selectedRoute = useMemo(() => {
    return response.filter(route => route.selected).map((r) => r.routes)
  }, [response]);

  const otherRoutes = useMemo(() => {
    return response.filter(route => !route.selected).map((r) => r.routes)
  }, [response]);

  const [animKey, setAnimKey] = useState(0);

  const blueOptions = { color: '#166af2', weight: 5, smoothFactor: 4, noClip: false }
  const lightBlueOptions = { color: '#78abff', weight: 4, smoothFactor: 4 }

  const changeRoute = () => {
    let replaced = response.map(x => {
      if (x.name === "Route 2") {
        return { ...x, selected: true };
      } else if (x.selected === true) {
        return { ...x, selected: false };
      }
      return x;
    });
    setResponse(replaced)
    setAnimKey(prev => prev + 1)
  }

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
      <button onClick={changeRoute} style={{ "margin": "40px" }}>Change route</button>
    </>
  )
}

export default App

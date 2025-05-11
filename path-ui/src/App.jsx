import './App.css'
import 'leaflet/dist/leaflet.css';
import SidebarLayout from './components/sidebar/SidebarLayout';
import Map from './components/map/map';


function App() {

  return (
    <>
      <SidebarLayout></SidebarLayout>
      <Map></Map>
    </>
  )
}

export default App

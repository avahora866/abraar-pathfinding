import { useState, useEffect } from 'react';
import './Form.css'; // Link to the CSS file below
import OSMAutocompleteDropdown from '../osmdropdown/OSMAutocompleteDropdown';

export default function Form({ routes }) {
  const tempRespones = [
    {
      "name": "Route 1",
      "selected": true,
      "time": "00:15",
      "distance": "2.5 km",
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
      "time": "00:15",
      "distance": "2.5 km",
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
      "time": "00:15",
      "distance": "2.5 km",
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
  // Form Functionality
  const [formData, setFormData] = useState({
    start: [],
    destination: []
  });

  const handleStartSelect = (e) => {
    setFormData({
      ...formData,
      start: [e.lat, e.lon]
    });
  };

  const handleDestinationSelect = (e) => {
    setFormData({
      ...formData,
      destination: [e.lat, e.lon]
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(formData);
  };

  const onSubmit = (data) => {
    // Replace with your logic
    console.log('Submitted data:', data);
    routes(tempRespones);
  };

  return (
    <form className="form" onSubmit={handleSubmit}>
      <label> Start:
        <OSMAutocompleteDropdown onSelect={handleStartSelect}></OSMAutocompleteDropdown>
      </label>
      <label>
        Destination:
        <OSMAutocompleteDropdown onSelect={handleDestinationSelect}></OSMAutocompleteDropdown>
      </label>
      <button type="submit">Submit</button>
    </form>
  );
}

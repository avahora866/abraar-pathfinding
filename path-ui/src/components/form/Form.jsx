import { useState, useEffect } from 'react';
import './Form.css'; // Link to the CSS file below
import OSMAutocompleteDropdown from '../osmdropdown/OSMAutocompleteDropdown';

export default function Form() {
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

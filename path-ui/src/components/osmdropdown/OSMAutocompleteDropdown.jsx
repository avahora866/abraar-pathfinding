import { useState, useEffect, useRef } from 'react';
import './OSMAutocompleteDropdown.css';

export default function OSMAutocompleteDropdown({ onSelect }) {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState([]);
  const [showDropdown, setShowDropdown] = useState(false);
  const [isSelected, setIsSelected] = useState(false);
  const inputRef = useRef(null);

  useEffect(() => {
    if (query.length < 3 || isSelected) {
      setResults([]);
      return;
    }

    const timeoutId = setTimeout(() => {
      const controller = new AbortController();
      const fetchResults = async () => {
        try {
          const res = await fetch(
            `https://nominatim.openstreetmap.org/search?q=${query}&format=json&addressdetails=1`,
            { signal: controller.signal }
          );
          const data = await res.json();
          setResults(data);
          setShowDropdown(true);
        } catch (error) {
          if (error.name !== 'AbortError') {
            console.error('Fetch error:', error);
          }
        }
      };

      fetchResults();
      return () => controller.abort();
    }, 500);

    return () => clearTimeout(timeoutId);
  }, [query, isSelected]);

  const handleSelect = (place) => {
    setIsSelected(true);
    setQuery(place.display_name);
    setShowDropdown(false);
    onSelect?.(place);
  };

  const handleInputChange = (e) => {
    setIsSelected(false);
    setQuery(e.target.value);
  };

  const handleClickOutside = (e) => {
    if (inputRef.current && !inputRef.current.contains(e.target)) {
      setShowDropdown(false);
    }
  };

  useEffect(() => {
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  return (
    <div className="autocomplete-wrapper" ref={inputRef}>
      <input
        type="text"
        value={query}
        onChange={handleInputChange}
        placeholder="Search for a place"
        className="autocomplete-input"
      />
      {showDropdown && results.length > 0 && (
        <ul className="autocomplete-dropdown">
          {results.map((place) => (
            <li
              key={place.place_id}
              onClick={() => handleSelect(place)}
              className="autocomplete-item"
            >
              {place.display_name}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
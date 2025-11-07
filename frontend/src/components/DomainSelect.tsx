import React, { useState, useEffect } from 'react';
import api from '../api/axios';

interface DomainSelectProps {
  value: string;
  onChange: (domain: string) => void;
}

const DomainSelect: React.FC<DomainSelectProps> = ({ value, onChange }) => {
  const [domains, setDomains] = useState<string[]>([]);
  const [query, setQuery] = useState('');
  const [filteredDomains, setFilteredDomains] = useState<string[]>([]);

  useEffect(() => {
    const loadDomains = async () => {
      try {
        const response = await api.get<string[]>('/domains');
        setDomains(response.data);
        setFilteredDomains(response.data);
      } catch (error) {
        console.error('Failed to load domains', error);
      }
    };
    loadDomains();
  }, []);

  useEffect(() => {
    if (query) {
      const filtered = domains.filter(d =>
        d.toLowerCase().includes(query.toLowerCase())
      );
      setFilteredDomains(filtered);
    } else {
      setFilteredDomains(domains);
    }
  }, [query, domains]);

  return (
    <div>
      <label className="block text-sm font-medium text-gray-700 mb-1">Domain</label>
      <input
        type="text"
        value={query}
        onChange={(e) => setQuery(e.target.value)}
        placeholder="Search domains..."
        className="w-full px-3 py-2 border border-gray-300 rounded-md mb-2"
      />
      <select
        value={value}
        onChange={(e) => onChange(e.target.value)}
        className="w-full px-3 py-2 border border-gray-300 rounded-md"
        required
      >
        <option value="">Select a domain</option>
        {filteredDomains.map((domain) => (
          <option key={domain} value={domain}>
            {domain}
          </option>
        ))}
      </select>
    </div>
  );
};

export default DomainSelect;

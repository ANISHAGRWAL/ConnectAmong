import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axios';
import JobCard from '../components/JobCard';

interface JobApplication {
  id: number;
  job: any;
  coverNote?: string;
  createdAt: string;
}

const MyApplications: React.FC = () => {
  const [applications, setApplications] = useState<JobApplication[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    const loadApplications = async () => {
      try {
        const response = await api.get('/jobs/my-applications');
        setApplications(response.data);
      } catch (error) {
        console.error('Failed to load applications', error);
      }
    };
    loadApplications();
  }, []);

  return (
    <div className="min-h-screen bg-gray-100">
      <nav className="bg-white shadow-md p-4 mb-4">
        <div className="max-w-4xl mx-auto flex justify-between items-center">
          <h1 className="text-2xl font-bold text-blue-600">My Applications</h1>
          <button
            onClick={() => navigate('/jobs')}
            className="text-blue-600 hover:underline"
          >
            Back to Jobs
          </button>
        </div>
      </nav>
      <div className="max-w-2xl mx-auto">
        {applications.length === 0 ? (
          <div className="bg-white p-6 rounded-lg shadow text-center text-gray-600">
            No applications yet
          </div>
        ) : (
          applications.map((app) => (
            <div key={app.id} className="mb-4">
              <JobCard job={app.job} />
              {app.coverNote && (
                <div className="bg-gray-50 p-3 rounded-lg mt-2 text-sm">
                  <p className="font-semibold mb-1">Your cover note:</p>
                  <p>{app.coverNote}</p>
                </div>
              )}
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default MyApplications;

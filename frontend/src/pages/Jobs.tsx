import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axios';
import JobCard from '../components/JobCard';

interface Job {
  id: number;
  title: string;
  company?: string;
  location?: string;
  type: string;
  description?: string;
  applyUrl?: string;
  poster: {
    fullName: string;
  };
}

const Jobs: React.FC = () => {
  const [jobs, setJobs] = useState<Job[]>([]);
  const [showApplyModal, setShowApplyModal] = useState(false);
  const [selectedJobId, setSelectedJobId] = useState<number | null>(null);
  const [coverNote, setCoverNote] = useState('');
  const navigate = useNavigate();

  const loadJobs = async () => {
    try {
      const response = await api.get('/jobs?page=0&size=20');
      setJobs(response.data.content);
    } catch (error) {
      console.error('Failed to load jobs', error);
    }
  };

  useEffect(() => {
    loadJobs();
  }, []);

  const handleApply = async (jobId: number) => {
    setSelectedJobId(jobId);
    setShowApplyModal(true);
  };

  const submitApplication = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedJobId) return;
    try {
      await api.post(`/jobs/${selectedJobId}/apply`, { coverNote });
      setCoverNote('');
      setShowApplyModal(false);
      alert('Application submitted successfully!');
    } catch (error) {
      console.error('Failed to apply', error);
      alert('Failed to submit application');
    }
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <nav className="bg-white shadow-md p-4 mb-4">
        <div className="max-w-4xl mx-auto flex justify-between items-center">
          <h1 className="text-2xl font-bold text-blue-600">Jobs</h1>
          <div className="flex gap-4">
            <button
              onClick={() => navigate('/jobs/new')}
              className="bg-green-600 text-white px-3 py-1 rounded hover:bg-green-700"
            >
              Post Job
            </button>
            <button
              onClick={() => navigate('/jobs/my-applications')}
              className="text-blue-600 hover:underline"
            >
              My Applications
            </button>
            <button
              onClick={() => navigate('/feed')}
              className="text-blue-600 hover:underline"
            >
              Back to Feed
            </button>
          </div>
        </div>
      </nav>
      <div className="max-w-2xl mx-auto">
        {jobs.map((job) => (
          <JobCard key={job.id} job={job} onApply={handleApply} />
        ))}
      </div>
      {showApplyModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
          <div className="bg-white p-6 rounded-lg w-full max-w-md">
            <h2 className="text-xl font-bold mb-4">Apply for Job</h2>
            <form onSubmit={submitApplication}>
              <textarea
                placeholder="Cover note (optional)"
                value={coverNote}
                onChange={(e) => setCoverNote(e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-md mb-4"
                rows={4}
              />
              <div className="flex gap-2">
                <button
                  type="submit"
                  className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
                >
                  Submit
                </button>
                <button
                  type="button"
                  onClick={() => setShowApplyModal(false)}
                  className="bg-gray-300 px-4 py-2 rounded hover:bg-gray-400"
                >
                  Cancel
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default Jobs;

import React from 'react';

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

interface JobCardProps {
  job: Job;
  onApply?: (id: number) => void;
}

const JobCard: React.FC<JobCardProps> = ({ job, onApply }) => {
  return (
    <div className="bg-white p-4 rounded-lg shadow mb-3">
      <h3 className="text-lg font-bold mb-1">{job.title}</h3>
      {job.company && <p className="text-gray-700 mb-1">{job.company}</p>}
      <div className="text-sm text-gray-600 mb-2">
        {job.location && <span>{job.location} · </span>}
        <span>{job.type}</span>
      </div>
      {job.description && <p className="text-gray-600 mb-3">{job.description}</p>}
      <p className="text-sm text-gray-500 mb-3">Posted by {job.poster.fullName}</p>
      {onApply && (
        <button
          onClick={() => onApply(job.id)}
          className="bg-blue-600 text-white px-4 py-1 rounded hover:bg-blue-700 text-sm"
        >
          Apply
        </button>
      )}
      {job.applyUrl && (
        <a
          href={job.applyUrl}
          target="_blank"
          rel="noopener noreferrer"
          className="ml-2 text-blue-600 hover:underline text-sm"
        >
          External Link
        </a>
      )}
    </div>
  );
};

export default JobCard;

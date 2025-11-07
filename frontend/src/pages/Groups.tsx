import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axios';
import GroupCard from '../components/GroupCard';

interface Group {
  id: number;
  name: string;
  description?: string;
  memberCount: number;
  isMember: boolean;
}

const Groups: React.FC = () => {
  const [groups, setGroups] = useState<Group[]>([]);
  const [showCreate, setShowCreate] = useState(false);
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const navigate = useNavigate();

  const loadGroups = async () => {
    try {
      const response = await api.get('/groups?page=0&size=20');
      setGroups(response.data.content);
    } catch (error) {
      console.error('Failed to load groups', error);
    }
  };

  useEffect(() => {
    loadGroups();
  }, []);

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post('/groups', { name, description });
      setName('');
      setDescription('');
      setShowCreate(false);
      loadGroups();
    } catch (error) {
      console.error('Failed to create group', error);
    }
  };

  const handleJoin = async (id: number) => {
    try {
      await api.post(`/groups/${id}/join`);
      loadGroups();
    } catch (error) {
      console.error('Failed to join group', error);
    }
  };

  const handleLeave = async (id: number) => {
    try {
      await api.delete(`/groups/${id}/leave`);
      loadGroups();
    } catch (error) {
      console.error('Failed to leave group', error);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <nav className="bg-white shadow-md p-4 mb-4">
        <div className="max-w-4xl mx-auto flex justify-between items-center">
          <h1 className="text-2xl font-bold text-blue-600">Groups</h1>
          <button
            onClick={() => navigate('/feed')}
            className="text-blue-600 hover:underline"
          >
            Back to Feed
          </button>
        </div>
      </nav>
      <div className="max-w-2xl mx-auto">
        <button
          onClick={() => setShowCreate(!showCreate)}
          className="w-full bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700 mb-4"
        >
          {showCreate ? 'Cancel' : 'Create Group'}
        </button>
        {showCreate && (
          <div className="bg-white p-4 rounded-lg shadow mb-4">
            <form onSubmit={handleCreate}>
              <input
                type="text"
                placeholder="Group name"
                value={name}
                onChange={(e) => setName(e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-md mb-2"
                required
              />
              <textarea
                placeholder="Description (optional)"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-md mb-2"
                rows={2}
              />
              <button
                type="submit"
                className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
              >
                Create
              </button>
            </form>
          </div>
        )}
        {groups.map((group) => (
          <GroupCard
            key={group.id}
            group={group}
            onJoin={handleJoin}
            onLeave={handleLeave}
            onView={(id) => navigate(`/groups/${id}`)}
          />
        ))}
      </div>
    </div>
  );
};

export default Groups;

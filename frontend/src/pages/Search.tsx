import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axios';
import PostCard from '../components/PostCard';

interface Post {
  id: number;
  author: {
    id: number;
    fullName: string;
    avatarUrl?: string;
  };
  title?: string;
  body: string;
  mediaUrl?: string;
  counts: {
    likes: number;
    comments: number;
  };
  createdAt: string;
}

interface UserProfile {
  id: number;
  fullName: string;
  domain: string;
  skills?: string;
  bio?: string;
  avatarUrl?: string;
}

const Search: React.FC = () => {
  const [tab, setTab] = useState<'posts' | 'users'>('posts');
  const [query, setQuery] = useState('');
  const [posts, setPosts] = useState<Post[]>([]);
  const [users, setUsers] = useState<UserProfile[]>([]);
  const navigate = useNavigate();

  const handleSearch = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!query.trim()) return;

    try {
      if (tab === 'posts') {
        const response = await api.get(`/search/posts?q=${encodeURIComponent(query)}&page=0&size=10`);
        setPosts(response.data.content);
      } else {
        const response = await api.get(`/search/users?q=${encodeURIComponent(query)}&page=0&size=10`);
        setUsers(response.data.content);
      }
    } catch (error) {
      console.error('Search failed', error);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <nav className="bg-white shadow-md p-4 mb-4">
        <div className="max-w-4xl mx-auto flex justify-between items-center">
          <h1 className="text-2xl font-bold text-blue-600">Search</h1>
          <button
            onClick={() => navigate('/feed')}
            className="text-blue-600 hover:underline"
          >
            Back to Feed
          </button>
        </div>
      </nav>
      <div className="max-w-2xl mx-auto">
        <div className="bg-white p-4 rounded-lg shadow mb-4">
          <div className="flex gap-4 mb-4">
            <button
              onClick={() => setTab('posts')}
              className={`px-4 py-2 rounded ${
                tab === 'posts'
                  ? 'bg-blue-600 text-white'
                  : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
              }`}
            >
              Posts
            </button>
            <button
              onClick={() => setTab('users')}
              className={`px-4 py-2 rounded ${
                tab === 'users'
                  ? 'bg-blue-600 text-white'
                  : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
              }`}
            >
              Users
            </button>
          </div>
          <form onSubmit={handleSearch}>
            <input
              type="text"
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              placeholder={`Search ${tab}...`}
              className="w-full px-3 py-2 border border-gray-300 rounded-md mb-2"
            />
            <button
              type="submit"
              className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
            >
              Search
            </button>
          </form>
        </div>

        {tab === 'posts' && posts.map((post) => (
          <PostCard key={post.id} post={post} onUpdate={() => {}} />
        ))}

        {tab === 'users' && users.map((user) => (
          <div key={user.id} className="bg-white p-4 rounded-lg shadow mb-3">
            <h3 className="text-lg font-bold">{user.fullName}</h3>
            <p className="text-sm text-gray-600">{user.domain}</p>
            {user.skills && <p className="text-sm text-gray-700 mt-1">Skills: {user.skills}</p>}
            {user.bio && <p className="text-gray-600 mt-2">{user.bio}</p>}
          </div>
        ))}
      </div>
    </div>
  );
};

export default Search;

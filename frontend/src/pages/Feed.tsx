import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';
import api from '../api/axios';
import PostComposer from '../components/PostComposer';
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

const Feed: React.FC = () => {
  const [posts, setPosts] = useState<Post[]>([]);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);
  const [hasMore, setHasMore] = useState(true);
  const { logout, user } = useAuth();
  const navigate = useNavigate();

  const loadPosts = async (pageNum: number = 0) => {
    setLoading(true);
    try {
      const response = await api.get(`/feed?page=${pageNum}&size=10`);
      if (pageNum === 0) {
        setPosts(response.data.content);
      } else {
        setPosts((prev) => [...prev, ...response.data.content]);
      }
      setHasMore(!response.data.last);
      setPage(pageNum);
    } catch (error) {
      console.error('Failed to load posts', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadPosts();
  }, []);

  const handleLoadMore = () => {
    if (!loading && hasMore) {
      loadPosts(page + 1);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <nav className="bg-white shadow-md p-4 mb-4">
        <div className="max-w-4xl mx-auto flex justify-between items-center">
          <h1 className="text-2xl font-bold text-blue-600">ConnectAmong</h1>
          <div className="flex gap-4 items-center">
            <span className="text-sm text-gray-600">
              {user?.fullName} ({user?.domain})
            </span>
            <button
              onClick={() => navigate('/groups')}
              className="text-blue-600 hover:underline"
            >
              Groups
            </button>
            <button
              onClick={() => navigate('/jobs')}
              className="text-blue-600 hover:underline"
            >
              Jobs
            </button>
            <button
              onClick={() => navigate('/search')}
              className="text-blue-600 hover:underline"
            >
              Search
            </button>
            <button
              onClick={() => navigate('/profile')}
              className="text-blue-600 hover:underline"
            >
              Profile
            </button>
            <button
              onClick={logout}
              className="bg-red-600 text-white px-3 py-1 rounded hover:bg-red-700"
            >
              Logout
            </button>
          </div>
        </div>
      </nav>
      <div className="max-w-2xl mx-auto">
        <PostComposer onPostCreated={() => loadPosts(0)} />
        {posts.map((post) => (
          <PostCard key={post.id} post={post} onUpdate={() => loadPosts(0)} />
        ))}
        {hasMore && (
          <button
            onClick={handleLoadMore}
            disabled={loading}
            className="w-full bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700 disabled:bg-gray-400"
          >
            {loading ? 'Loading...' : 'Load More'}
          </button>
        )}
      </div>
    </div>
  );
};

export default Feed;

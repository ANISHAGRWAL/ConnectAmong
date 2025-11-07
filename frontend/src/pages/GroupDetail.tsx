import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
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

const GroupDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [posts, setPosts] = useState<Post[]>([]);
  const [body, setBody] = useState('');
  const navigate = useNavigate();

  const loadPosts = async () => {
    try {
      const response = await api.get(`/groups/${id}/posts?page=0&size=10`);
      setPosts(response.data.content);
    } catch (error) {
      console.error('Failed to load group posts', error);
    }
  };

  useEffect(() => {
    loadPosts();
  }, [id]);

  const handlePost = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post('/posts', { body, groupId: id });
      setBody('');
      loadPosts();
    } catch (error) {
      console.error('Failed to create post', error);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <nav className="bg-white shadow-md p-4 mb-4">
        <div className="max-w-4xl mx-auto flex justify-between items-center">
          <h1 className="text-2xl font-bold text-blue-600">Group Posts</h1>
          <button
            onClick={() => navigate('/groups')}
            className="text-blue-600 hover:underline"
          >
            Back to Groups
          </button>
        </div>
      </nav>
      <div className="max-w-2xl mx-auto">
        <div className="bg-white p-4 rounded-lg shadow mb-4">
          <form onSubmit={handlePost}>
            <textarea
              placeholder="Post to this group..."
              value={body}
              onChange={(e) => setBody(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-md mb-2"
              rows={3}
              required
            />
            <button
              type="submit"
              className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
            >
              Post
            </button>
          </form>
        </div>
        {posts.map((post) => (
          <PostCard key={post.id} post={post} onUpdate={loadPosts} />
        ))}
      </div>
    </div>
  );
};

export default GroupDetail;

import React, { useState } from 'react';
import api from '../api/axios';

interface PostComposerProps {
  onPostCreated: () => void;
}

const PostComposer: React.FC<PostComposerProps> = ({ onPostCreated }) => {
  const [title, setTitle] = useState('');
  const [body, setBody] = useState('');
  const [mediaUrl, setMediaUrl] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    try {
      await api.post('/posts', { title, body, mediaUrl: mediaUrl || undefined });
      setTitle('');
      setBody('');
      setMediaUrl('');
      onPostCreated();
    } catch (error) {
      console.error('Failed to create post', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-white p-4 rounded-lg shadow mb-4">
      <h3 className="text-lg font-semibold mb-3">Create a Post</h3>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Title (optional)"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          className="w-full px-3 py-2 border border-gray-300 rounded-md mb-2"
        />
        <textarea
          placeholder="What's on your mind?"
          value={body}
          onChange={(e) => setBody(e.target.value)}
          className="w-full px-3 py-2 border border-gray-300 rounded-md mb-2"
          rows={3}
          required
        />
        <input
          type="url"
          placeholder="Media URL (optional)"
          value={mediaUrl}
          onChange={(e) => setMediaUrl(e.target.value)}
          className="w-full px-3 py-2 border border-gray-300 rounded-md mb-2"
        />
        <button
          type="submit"
          disabled={loading}
          className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 disabled:bg-gray-400"
        >
          {loading ? 'Posting...' : 'Post'}
        </button>
      </form>
    </div>
  );
};

export default PostComposer;

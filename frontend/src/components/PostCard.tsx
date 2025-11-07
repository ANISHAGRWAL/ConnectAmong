import React, { useState } from 'react';
import api from '../api/axios';

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

interface PostCardProps {
  post: Post;
  onUpdate: () => void;
}

const PostCard: React.FC<PostCardProps> = ({ post, onUpdate }) => {
  const [showComments, setShowComments] = useState(false);
  const [commentBody, setCommentBody] = useState('');

  const handleLike = async () => {
    try {
      await api.post(`/posts/${post.id}/react?type=LIKE`);
      onUpdate();
    } catch (error) {
      console.error('Failed to like post', error);
    }
  };

  const handleComment = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post(`/posts/${post.id}/comments`, { body: commentBody });
      setCommentBody('');
      onUpdate();
    } catch (error) {
      console.error('Failed to comment', error);
    }
  };

  return (
    <div className="bg-white p-4 rounded-lg shadow mb-4">
      <div className="flex items-center mb-3">
        <div className="w-10 h-10 bg-gray-300 rounded-full mr-3 flex items-center justify-center">
          {post.author.avatarUrl ? (
            <img src={post.author.avatarUrl} alt="" className="w-10 h-10 rounded-full" />
          ) : (
            <span className="text-gray-600 font-semibold">
              {post.author.fullName.charAt(0)}
            </span>
          )}
        </div>
        <div>
          <p className="font-semibold">{post.author.fullName}</p>
          <p className="text-sm text-gray-500">{new Date(post.createdAt).toLocaleDateString()}</p>
        </div>
      </div>
      {post.title && <h3 className="text-xl font-bold mb-2">{post.title}</h3>}
      <p className="mb-3 whitespace-pre-wrap">{post.body}</p>
      {post.mediaUrl && (
        <img src={post.mediaUrl} alt="" className="w-full rounded-md mb-3" />
      )}
      <div className="flex gap-4 text-sm text-gray-600 mb-3">
        <button onClick={handleLike} className="hover:text-blue-600">
          👍 {post.counts.likes} Likes
        </button>
        <button onClick={() => setShowComments(!showComments)} className="hover:text-blue-600">
          💬 {post.counts.comments} Comments
        </button>
      </div>
      {showComments && (
        <form onSubmit={handleComment} className="mt-3">
          <input
            type="text"
            placeholder="Write a comment..."
            value={commentBody}
            onChange={(e) => setCommentBody(e.target.value)}
            className="w-full px-3 py-2 border border-gray-300 rounded-md"
            required
          />
          <button
            type="submit"
            className="mt-2 bg-blue-600 text-white px-4 py-1 rounded-md hover:bg-blue-700 text-sm"
          >
            Comment
          </button>
        </form>
      )}
    </div>
  );
};

export default PostCard;

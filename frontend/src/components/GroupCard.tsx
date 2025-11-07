import React from 'react';

interface Group {
  id: number;
  name: string;
  description?: string;
  memberCount: number;
  isMember: boolean;
}

interface GroupCardProps {
  group: Group;
  onJoin: (id: number) => void;
  onLeave: (id: number) => void;
  onView: (id: number) => void;
}

const GroupCard: React.FC<GroupCardProps> = ({ group, onJoin, onLeave, onView }) => {
  return (
    <div className="bg-white p-4 rounded-lg shadow mb-3">
      <h3 className="text-lg font-bold mb-1">{group.name}</h3>
      {group.description && <p className="text-gray-600 mb-2">{group.description}</p>}
      <p className="text-sm text-gray-500 mb-3">{group.memberCount} members</p>
      <div className="flex gap-2">
        <button
          onClick={() => onView(group.id)}
          className="bg-blue-600 text-white px-3 py-1 rounded hover:bg-blue-700 text-sm"
        >
          View
        </button>
        {group.isMember ? (
          <button
            onClick={() => onLeave(group.id)}
            className="bg-red-600 text-white px-3 py-1 rounded hover:bg-red-700 text-sm"
          >
            Leave
          </button>
        ) : (
          <button
            onClick={() => onJoin(group.id)}
            className="bg-green-600 text-white px-3 py-1 rounded hover:bg-green-700 text-sm"
          >
            Join
          </button>
        )}
      </div>
    </div>
  );
};

export default GroupCard;

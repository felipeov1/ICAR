import React from 'react';

const Loading = () => {
  return (
    <div className="flex flex-col items-center justify-center h-screen bg-gray-50">
      <div className="w-12 h-12 border-4 border-gray-200 border-t-blue-500 rounded-full animate-spin"></div>
      <p className="mt-4 text-gray-600">Carregando...</p>
    </div>
  );
};

export default Loading;
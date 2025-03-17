import React from "react";
import { useNavigate } from "react-router-dom";

const NotFoundLanding = () => {
  const navigate = useNavigate();

  return (
    <div className="flex flex-col items-center justify-center h-screen bg-gray-50 text-center">
      <h1 className="text-4xl font-bold text-gray-800">404 - Página Não Encontrada</h1>
      <p className="mt-4 text-gray-600">
        Desculpe, a página que você está procurando não existe.
      </p>
      <button
        onClick={() => navigate("/")}
        className="mt-6 px-6 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors"
      >
        Voltar para a Home
      </button>
    </div>
  );
};

export default NotFoundLanding;
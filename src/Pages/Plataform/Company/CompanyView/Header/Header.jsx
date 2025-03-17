import React from "react";
import { ChevronLeftIcon } from "@heroicons/react/24/outline";

const HeaderMain = () => (
  <div className="relative">
    {/* Botão de Voltar */}
    <a href="/app" className="absolute top-4 left-2 text-white text-3xl">
      <div className="bg-[#0000003d] rounded-full p-1">
        <ChevronLeftIcon width={30} />
      </div>
    </a>

    {/* Imagem de Capa */}
    <div className="w-full h-56 lg:h-96 overflow-hidden">
      <img
        src="/src/images/7359-lava-rapido-kf-3.jpg"
        alt="Lava-Rápido ICAR"
        className="w-full h-full object-cover"
      />
    </div>

    {/* Informações do Estabelecimento */}
    <div className="mt-6 mb-4 pl-4 lg:pl-6 lg:mb-0">
      <h1 className="text-2xl font-bold">Lava-rápido ICAR</h1>
      <div className="flex items-center space-x-2 mt-2">
        <span className="text-yellow-500">⭐ 5.0</span>
        <span className="text-gray-500">• 4.8 Km</span>
      </div>
    </div>
  </div>
);

export default HeaderMain;

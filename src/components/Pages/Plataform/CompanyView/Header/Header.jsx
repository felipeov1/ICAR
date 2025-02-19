import React from "react";
import { ChevronLeftIcon } from "@heroicons/react/24/outline";

const HeaderMain = () => (
  <div className="relative">
    <a href="/icar" className="absolute top-4 left-2 text-white text-3xl">
      <div className="bg-[#0000003d] rounded-full p-1">
        <ChevronLeftIcon width={30} />
      </div>
    </a>

    <img
      src="/src/images/icar-blue.jpeg"
      alt="Lava-Rápido ICAR"
      className="w-full h-56 object-cover"
    />

    <div className="mt-4 p-4">
      <h1 className="text-2xl font-bold">Lava-rápido ICAR</h1>
      <div className="flex items-center space-x-2 mt-2">
        <span className="text-yellow-500">⭐ 5.0</span>
        <span className="text-gray-500">• 4.8 Km</span>
      </div>
    </div>
  </div>
);

export default HeaderMain;

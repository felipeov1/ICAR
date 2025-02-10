import React from "react";
import { HomeIcon, UserIcon, CalendarDaysIcon } from "@heroicons/react/24/outline";

const MobileNavigation = () => {
  const buttons = [
    { icon: <HomeIcon className="w-6 h-6" />, label: "Início" },
    { icon: <CalendarDaysIcon className="w-6 h-6" />, label: "Agendamentos" },
    { icon: <UserIcon className="w-6 h-6" />, label: "Minha Conta" },
  ];

  return (
    <nav className="bg-gray-800 text-white fixed bottom-0 left-0 right-0 py-2 shadow-lg">
      <div className="grid grid-cols-3 divide-x divide-gray-700">
        {buttons.map((button, index) => (
          <button
            key={index}
            className="flex flex-col items-center justify-center p-2 hover:bg-gray-700"
          >
            {button.icon}
            <span className="text-sm">{button.label}</span>
          </button>
        ))}
      </div>
    </nav>
  );
};

export default MobileNavigation;

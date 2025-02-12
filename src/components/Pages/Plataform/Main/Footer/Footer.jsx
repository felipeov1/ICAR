import React, { useState } from "react";
import { Link, useLocation } from "react-router-dom";
import { HomeIcon, UserIcon, CalendarDaysIcon } from "@heroicons/react/24/outline";

const MobileNavigation = () => {
  const location = useLocation();
  const [active, setActive] = useState(location.pathname);

  const buttons = [
    { icon: <HomeIcon className="w-6 h-6" />, label: "Início", path: "/icar" },
    { icon: <CalendarDaysIcon className="w-6 h-6" />, label: "Agendamentos", path: "/icar/agendamentos" },
    { icon: <UserIcon className="w-6 h-6" />, label: "Minha Conta", path: "/icar/minha-conta" },
  ];

  const handleClick = (path) => {
    if (active === path) {
      setActive(null); // Se já estiver ativo, desseleciona
    } else {
      setActive(path); // Caso contrário, seleciona
    }
  };

  return (
    <nav className="bg-gray-800 text-white fixed bottom-0 left-0 right-0 py-2 shadow-lg z-10">
      <div className="grid grid-cols-3 divide-x divide-gray-700">
        {buttons.map((button, index) => (
          <Link
            key={index}
            to={button.path}
            onClick={() => handleClick(button.path)}
            className={`flex flex-col items-center justify-center p-2 ${
              active === button.path ? "bg-gray-700" : "hover:bg-gray-700"
            }`}
          >
            {button.icon}
            <span className="text-sm">{button.label}</span>
          </Link>
        ))}
      </div>
    </nav>
  );
};

export default MobileNavigation;

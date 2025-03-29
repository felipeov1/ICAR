import React, { useState } from "react";
import { Link, useLocation } from "react-router-dom";
import {
  HomeIcon as HomeIconOutline,
  UserIcon as UserIconOutline,
  CalendarDaysIcon as CalendarDaysIconOutline,
} from "@heroicons/react/24/outline"; // Ícones outline (não preenchidos)
import {
  HomeIcon as HomeIconSolid,
  UserIcon as UserIconSolid,
  CalendarDaysIcon as CalendarDaysIconSolid,
} from "@heroicons/react/24/solid"; // Ícones solid (preenchidos)

const MobileNavigation = () => {
  const location = useLocation();
  const [active, setActive] = useState(location.pathname);

  const buttons = [
    {
      iconOutline: <HomeIconOutline className="w-5 h-6" />,
      iconSolid: <HomeIconSolid className="w-5 h-6" />,
      label: "Início",
      path: "/app",
    },
    {
      iconOutline: <CalendarDaysIconOutline className="w-5 h-6" />,
      iconSolid: <CalendarDaysIconSolid className="w-5 h-6" />,
      label: "Agendamentos",
      path: "/app/agendamentos",
    },
    {
      iconOutline: <UserIconOutline className="w-5 h-6" />,
      iconSolid: <UserIconSolid className="w-5 h-6" />,
      label: "Minha Conta",
      path: "/app/minha-conta",
    },
  ];

  const handleClick = (path) => {
    if (active === path) {
      setActive(null); // Se já estiver ativo, desseleciona
    } else {
      setActive(path); // Caso contrário, seleciona
    }
  };

  return (
    <nav className="bg-white text-gray-800 fixed bottom-0 left-0 right-0 py-1 shadow-[0_-2px_4px_rgba(0,0,0,0.1)] z-10 lg:hidden">
      <div className="grid grid-cols-3 divide-gray-700">
        {buttons.map((button, index) => (
          <Link
            key={index}
            to={button.path}
            onClick={() => handleClick(button.path)}
            className="flex flex-col items-center justify-center"
          >
            {/* Alterna entre ícone outline e solid com base no estado ativo */}
            {active === button.path ? button.iconSolid : button.iconOutline}
            {/* Aplica a classe text-black ao texto quando ativo */}
            <span className={`text-sm text-center ${active === button.path ? "text-black" : "text-gray-800"}`}>
              {button.label}
            </span>
          </Link>
        ))}
      </div>
    </nav>
  );
};

export default MobileNavigation;
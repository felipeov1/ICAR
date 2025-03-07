import React from "react";
import {
  HomeIcon,
  CalendarDaysIcon,
  UserIcon,
} from "@heroicons/react/24/outline";
import logoIcar from "../../../../public/assets/images/logo/icar-logo-transparent.png";

const Header = ({ className }) => {
  const buttons = [
    { icon: <HomeIcon className="w-6 h-6" />, label: "Início", path: "/icar" },
    {
      icon: <CalendarDaysIcon className="w-6 h-6" />,
      label: "Agendamentos",
      path: "/icar/agendamentos",
    },
    {
      icon: <UserIcon className="w-6 h-6" />,
      label: "Minha Conta",
      path: "/icar/minha-conta",
    },
  ];

  // Verifica a rota atual
  const currentPath = window.location.pathname;

  return (
    <header
      className={`${
        className ||
        "bg-[#161616] text-white flex p-4 rounded-bl-lg rounded-br-lg"
      }`}
      style={{ flexWrap: "wrap", alignItems: "start" }}
    >
      <div className="lg:flex-[90%] lg:flex lg:justify-between lg:items-center">
        <div className="flex-1 flex">
          <a href="/icar">
            <img
              src={logoIcar}
              alt="Logo do Icar"
              width={100}
            />
          </a>
        </div>

        {/* Navegação para dispositivos não móveis */}
        <div className="hidden lg:flex items-center space-x-4">
          {buttons.map((button, index) => (
            <a
              key={index}
              href={button.path}
              className={`flex items-center space-x-2 hover:text-orange-800 ${
                currentPath === button.path ? "text-orange-500 font-bold" : ""
              }`}
            >
              {button.icon}
              <span>{button.label}</span>
            </a>
          ))}
        </div>
      </div>
    </header>
  );
};

export default Header;

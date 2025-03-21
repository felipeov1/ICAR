import React, { useState, useEffect, useRef } from "react";
import {
  HomeIcon,
  CalendarDaysIcon,
  UserIcon,
} from "@heroicons/react/24/outline";
import logoIcar from "../../../../public/assets/images/logo/icar-logo-transparent.png";

const HeaderMain = ({ className }) => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const menuRef = useRef(null);

  const closeMenu = (e) => {
    if (menuRef.current && !menuRef.current.contains(e.target)) {
      setIsMenuOpen(false);
    }
  };

  useEffect(() => {
    document.addEventListener("click", closeMenu);
    return () => {
      document.removeEventListener("click", closeMenu);
    };
  }, []);

 

  const buttons = [
    { icon: <HomeIcon className="w-6 h-6" />, label: "Início", path: "/app" },
    {
      icon: <CalendarDaysIcon className="w-6 h-6" />,
      label: "Agendamentos",
      path: "/app/agendamentos",
    },
    {
      icon: <UserIcon className="w-6 h-6" />,
      label: "Minha Conta",
      path: "/app/minha-conta",
    },
  ];

  // Verifica a rota atual
  const currentPath = window.location.pathname;

  return (
    <header
      className={`${
        className ||
        "bg-[#170d72] text-white flex h-52 lg:h-20 md:h-20 lg:p-4 p-6 rounded-bl-lg rounded-br-lg"
      }`}
      style={{ flexWrap: "wrap", alignItems: "start" }}
    >
      <div className="flex-[5%] flex flex-col text-sm lg:hidden  ">
        <a href="/app">
          <img src={logoIcar} alt="Logo do Icar" width={100} />
        </a>
      </div>
      <div className="lg:flex-[90%] lg:flex lg:justify-between lg:items-center">
     
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

export default HeaderMain;
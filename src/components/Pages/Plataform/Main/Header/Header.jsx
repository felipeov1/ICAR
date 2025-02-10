import React, { useState, useEffect, useRef } from "react";
import { UserCircleIcon } from "@heroicons/react/24/outline";

const Header = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const menuRef = useRef(null);

  const toggleMenu = () => {
    setIsMenuOpen((prev) => !prev);
  };

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

  return (
    <header
      className="bg-[#170d72] text-white flex h-52 lg:h-24 md:h-24 p-6 rounded-bl-lg rounded-br-lg"
      style={{ flexWrap: "wrap", alignItems: "start" }}
    >

      <div className="flex-1 flex flex-col items-start">
        <span>Olá,</span>
        <span>Felipe Ferreira</span>
      </div>


      <div className="flex-1 flex justify-center">
        <img
          src="/src/images/icar-logo-transparent.png"
          alt="Logo do Icar"
          width={100}
        />
      </div>


      <div className="flex-1 flex justify-end relative" ref={menuRef}>
        <UserCircleIcon
          width={50}
          className="cursor-pointer"
          onClick={toggleMenu}
        />
        {isMenuOpen && (
          <div className="absolute right-0 mt-14 w-48 bg-white text-black rounded-lg shadow-lg">

            <div className="absolute top-[-5px] right-4 w-0 h-0 border-l-8 border-r-8 border-b-8 border-transparent border-b-white"></div>


            <ul className="py-2">
              <li
                className="px-4 py-2 hover:bg-gray-200 cursor-pointer"
                onClick={() => setIsMenuOpen(false)}
              >
                Perfil
              </li>
              <li
                className="px-4 py-2 hover:bg-gray-200 cursor-pointer"
                onClick={() => setIsMenuOpen(false)}
              >
                Configurações
              </li>
              <li
                className="px-4 py-2 hover:bg-gray-200 cursor-pointer"
                onClick={() => setIsMenuOpen(false)}
              >
                Sair
              </li>
            </ul>
          </div>
        )}
      </div>
    </header>
  );
};

export default Header;

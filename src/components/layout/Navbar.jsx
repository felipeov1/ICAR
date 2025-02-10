import React, { useState, useEffect } from "react";
import { motion } from "framer-motion";
import NavLink from "./NavLink";

const Navbar = () => {
  const [isScrolled, setIsScrolled] = useState(false);
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  useEffect(() => {
    const handleScroll = () => {
      setIsScrolled(window.scrollY > 50);
    };

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  return (
    <>

      <motion.nav
        className={`fixed w-full z-50 transition-all duration-300 ${
          isScrolled ? "bg-[#170d72] py-1" : "bg-[#170d72] py-2"
        }`}
      >

        <div className="container mx-auto px-4">
          <div className="flex justify-between items-center">

            <div className="flex items-center space-x-4">

              <button
                className="text-white md:hidden focus:outline-none"
                onClick={toggleMenu}
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  strokeWidth="2"
                  stroke="currentColor"
                  className="w-8 h-8"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M4 6h16M4 12h16M4 18h16"
                  />
                </svg>
              </button>


              <a href="/" className="text-2xl font-bold text-blue-600">
                <img
                  src="/src/images/icar-logo-transparent.png"
                  alt="Logo"
                  width={120}
                />
              </a>
            </div>


            <div className="md:hidden">
              <a
                href="/entrar"
                className="px-4 py-2 bg-orange-600 text-white font-semibold rounded hover:bg-orange-700"
              >
                Agendar
              </a>
            </div>


            <div className="hidden md:flex flex-grow justify-center space-x-8 text-white">
              <NavLink href="#inicio">
                <span className="text-white hover:text-[#e6e6e644]">Início</span>
              </NavLink>
              <NavLink href="#funcionalidades">
                <span className="text-white hover:text-[#e6e6e644]">
                  Funcionalidades
                </span>
              </NavLink>
              <NavLink href="#depoimentos">
                <span className="text-white hover:text-[#e6e6e644]">
                  Depoimentos
                </span>
              </NavLink>
              <NavLink href="#duvidas">
                <span className="text-white hover:text-[#e6e6e644]">Dúvidas</span>
              </NavLink>
            </div>


            <div className="hidden md:flex space-x-4">
              <a
                href="/entrar"
                target="_blank"
                className="px-4 py-2 bg-transparent text-white rounded hover:text-[#e6e6e644]"
              >
                Entrar
              </a>
              <a
                href="/criar-conta"
                target="_blank"
                className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
              >
                Criar Conta
              </a>
            </div>
          </div>
        </div>
      </motion.nav>


      <motion.div
        initial={{ y: "-100%" }}
        animate={{ y: isMenuOpen ? 0 : "-100%" }}
        transition={{ duration: 0.3, ease: "easeInOut" }}
        className="fixed top-0 left-0 w-full bg-[#170d72] text-white z-40 shadow-lg md:hidden"
        style={{ height: "fit-content" }}
      >
        <div className="p-4">
          <button
            className="text-white mb-4 focus:outline-none"
            onClick={toggleMenu}
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              strokeWidth="2"
              stroke="currentColor"
              className="w-8 h-8"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                d="M6 18L18 6M6 6l12 12"
              />
            </svg>
          </button>


          <nav className="flex flex-col space-y-6 pt-4">
            <NavLink href="#inicio">
              <span
                className="block text-xl text-white hover:text-[#e6e6e644]"
                onClick={() => setIsMenuOpen(false)}
              >
                Início
              </span>
            </NavLink>
            <NavLink href="#funcionalidades">
              <span
                className="block text-xl text-white hover:text-[#e6e6e644]"
                onClick={() => setIsMenuOpen(false)}
              >
                Funcionalidades
              </span>
            </NavLink>
            <NavLink href="#depoimentos">
              <span
                className="block text-xl text-white hover:text-[#e6e6e644]"
                onClick={() => setIsMenuOpen(false)}
              >
                Depoimentos
              </span>
            </NavLink>
            <NavLink href="#duvidas">
              <span
                className="block text-xl text-white hover:text-[#e6e6e644]"
                onClick={() => setIsMenuOpen(false)}
              >
                Dúvidas
              </span>
            </NavLink>
            <a
              href="/entrar"
              target="_blank"
              className="block text-xl bg-blue-600 text-center text-white rounded hover:bg-blue-700 px-4 py-2"
              onClick={() => setIsMenuOpen(false)}
            >
              Entrar
            </a>
            <a
              href="/criar-conta"
              target="_blank"
              className="block text-xl bg-orange-600 text-center text-white rounded hover:bg-orange-700 px-4 py-2"
              onClick={() => setIsMenuOpen(false)}
            >
              Criar Conta
            </a>
          </nav>
        </div>
      </motion.div>
    </>
  );
};

export default Navbar;

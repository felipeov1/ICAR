import React from "react";
import { useLocation, useNavigate } from "react-router-dom";

const NavLink = ({ href, children }) => {
  const location = useLocation();
  const navigate = useNavigate();

  const handleClick = (e) => {
    e.preventDefault();

    // Se não estiver na página inicial, redireciona para a página inicial
    if (location.pathname !== "/") {
      navigate("/", { state: { scrollTo: href } }); // Passa a seção como estado
    } else {
      // Se já estiver na página inicial, rola até a seção
      const targetElement = document.querySelector(href);
      if (targetElement) {
        const offset = -100; // Ajuste o offset conforme necessário
        const topPosition =
          targetElement.getBoundingClientRect().top + window.scrollY + offset;

        window.scrollTo({
          top: topPosition,
          behavior: "smooth",
        });
      }
    }
  };

  return (
    <a
      href={href}
      onClick={handleClick}
      className="text-gray-700 cursor-pointer hover:text-blue-600 transition-colors duration-200"
    >
      {children}
    </a>
  );
};

export default NavLink;
import React from "react";
import { useLocation, useNavigate } from "react-router-dom";

const NavLink = ({ href, children, onClick }) => {
  const location = useLocation();
  const navigate = useNavigate();

  const handleClick = (e) => {
    e.preventDefault();
    
    if (onClick) onClick(); 
  
    if (location.pathname !== "/") {

      navigate(`/${href}`);
    } else {

      window.location.hash = href; 
  
      setTimeout(() => {
        const targetElement = document.querySelector(href);
        if (targetElement) {
          const offset = -100;
          const topPosition = targetElement.getBoundingClientRect().top + window.scrollY + offset;
          window.scrollTo({ top: topPosition, behavior: "smooth" });
        }
      }, 100);
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
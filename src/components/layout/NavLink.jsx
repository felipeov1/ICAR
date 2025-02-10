import React from 'react';

const NavLink = ({ href, children }) => {
    const handleClick = (e) => {
      e.preventDefault(); 
  
      const targetElement = document.querySelector(href);
      if (targetElement) {
        const offset = -100; 
        const topPosition =
          targetElement.getBoundingClientRect().top + window.scrollY + offset;
  
        window.scrollTo({
          top: topPosition,
          behavior: "smooth",
        });
      }
    };
  
  return (
    <a
      href={href}
      onClick={handleClick}
      className="text-gray-700 cursor-pointer  hover:text-blue-600 transition-colors duration-200"
    >
      {children}
    </a>
  );
};

export default NavLink;
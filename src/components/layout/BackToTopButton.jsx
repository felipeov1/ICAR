import React, { useState, useEffect } from "react";

const BackToTopButton = () => {
  const [isVisible, setIsVisible] = useState(false);

  const checkScrollTop = () => {
    if (!isVisible && window.pageYOffset > 300) {
      setIsVisible(true);
    } else if (isVisible && window.pageYOffset <= 300) {
      setIsVisible(false);
    }
  };

  const scrollToTop = () => {
    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });
  };

  useEffect(() => {
    window.addEventListener("scroll", checkScrollTop);

    return () => {
      window.removeEventListener("scroll", checkScrollTop);
    };
  }, [isVisible]);

  return (
    <button
      onClick={scrollToTop}
      className={`fixed bottom-10 right-10 p-3 bg-[#1e3a8a] text-white rounded-full shadow-lg transition-opacity ${
        isVisible ? "opacity-100" : "opacity-0"
      }`}
      style={{ transition: "opacity 0.3s ease-in-out" }}
    >
      ↑
    </button>
  );
};

export default BackToTopButton;

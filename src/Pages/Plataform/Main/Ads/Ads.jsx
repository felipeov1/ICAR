import React, { useState, useEffect } from "react";
import { ChevronLeftIcon, ChevronRightIcon } from "@heroicons/react/24/outline";

const Ads = () => {
  const images = [
    {
      src: "/src/images/icar-logo-blue.jpg",
      link: "/link1", // Link para a primeira imagem
    },
    {
      src: "/src/images/icar-logo-blue.jpg",
      link: "/link2", // Link para a segunda imagem
    },
    {
      src: "/src/images/icar-logo-blue.jpg",
      link: "/link3", // Link para a terceira imagem
    },
    {
      src: "/src/images/icar-logo-blue.jpg",
      link: "/link4", // Link para a quarta imagem
    },
    {
      src: "/src/images/icar-logo-blue.jpg",
      link: "/link5", // Link para a quinta imagem
    },
    {
      src: "/src/images/icar-logo-blue.jpg",
      link: "/link6", // Link para a sexta imagem
    },
  ];

  const [currentIndex, setCurrentIndex] = useState(0);

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentIndex((prevIndex) => (prevIndex + 1) % images.length);
    }, 4000);

    return () => clearInterval(interval);
  }, [images.length]);

  const handleNext = () => {
    setCurrentIndex((prevIndex) => (prevIndex + 1) % images.length);
  };

  const handlePrevious = () => {
    setCurrentIndex((prevIndex) =>
      prevIndex === 0 ? images.length - 1 : prevIndex - 1
    );
  };

  return (
    <section className="max-w-7xl mx-auto px-2 sm:px-6 lg:px-8 lg:mt-10">
      <h2 className="text-lg font-bold mb-2 pl-2">Destaques</h2>

      <div className="relative">
        {/* Link na imagem atual */}
        <a
          href={images[currentIndex].link}
          
          rel="noopener noreferrer"
        >
          <img
            src={images[currentIndex].src}
            alt={`Imagem ${currentIndex + 1}`}
            className="rounded-lg w-full h-52 lg:h-96 mb-1 pl-1 pe-1"
          />
        </a>

        {/* Botão de navegação para a esquerda */}
        <button
          className="absolute left-4 top-1/2 transform -translate-y-1/2 bg-[#ffffff8a] p-2 rounded-full shadow-md hover:bg-gray-300"
          onClick={handlePrevious}
        >
          <ChevronLeftIcon className="w-6 h-6 text-gray-800" />
        </button>

        {/* Botão de navegação para a direita */}
        <button
          className="absolute right-4 top-1/2 transform -translate-y-1/2 bg-[#ffffff8a] p-2 rounded-full shadow-md hover:bg-gray-300"
          onClick={handleNext}
        >
          <ChevronRightIcon className="w-6 h-6 text-gray-800" />
        </button>

        {/* Indicadores de progresso */}
        <div className="absolute bottom-2 left-1/2 transform -translate-x-1/2 flex space-x-2 lg:w-80 w-3/4 ">
          {images.map((_, index) => (
            <div
              key={index}
              className="h-[3px] w-14 bg-gray-400 rounded-full overflow-hidden relative"
            >
              <div
                className={`absolute top-0 left-0 h-full ${
                  currentIndex >= index ? "bg-[#1e3a8a]" : "bg-gray-300"
                } transition-all`}
                style={{
                  width:
                    index === currentIndex
                      ? "100%"
                      : currentIndex > index
                      ? "100%"
                      : "0%",
                  animation:
                    index === currentIndex
                      ? "progress 4s linear infinite"
                      : "none",
                }}
              ></div>
            </div>
          ))}
        </div>
      </div>

      <style>
        {`
          @keyframes progress {
            0% {
              width: 0%;
            }
            100% {
              width: 100%;
            }
          }
        `}
      </style>
    </section>
  );
};

export default Ads;

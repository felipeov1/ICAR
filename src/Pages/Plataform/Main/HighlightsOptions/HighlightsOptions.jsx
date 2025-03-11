import React, { useRef } from "react";
import { ChevronLeftIcon, ChevronRightIcon } from "@heroicons/react/24/outline";

const HighlightsOptions = () => {
  const optionsRef1 = useRef(null);
  const optionsRef2 = useRef(null);

  const scrollLeft = (ref) => {
    ref.current.scrollBy({
      left: -200,
      behavior: "smooth",
    });
  };

  const scrollRight = (ref) => {
    ref.current.scrollBy({
      left: 200,
      behavior: "smooth",
    });
  };

  const nearbyCompanies = [
    {
      name: "FastCar",
      image: "/src/images/icar-logo-blue.jpg",
      link: "/icar/empresa",
    },
    {
      name: "Lava Rápido VIP",
      image: "/src/images/icar-logo-blue.jpg",
      link: "/icar/empresa",
    },
    {
      name: "Guará",
      image: "/src/images/icar-logo-blue.jpg",
      link: "/icar/empresa",
    },
    {
      name: "Lava Bem",
      image: "/src/images/icar-logo-blue.jpg",
      link: "/icar/empresa",
    },
  ];

  const domicilieDeliveryCompanies = [
    {
      name: "FastCar",
      image: "/src/images/icar-logo-blue.jpg",
      link: "/icar/empresa",
    },
    {
      name: "Lava Rápido VIP",
      image: "/src/images/icar-logo-blue.jpg",
      link: "/icar/empresa",
    },
    {
      name: "Guará",
      image: "/src/images/icar-logo-blue.jpg",
      link: "/icar/empresa",
    },
    {
      name: "Lava Bem",
      image: "/src/images/icar-logo-blue.jpg",
      link: "/icar/empresa",
    },
  ];

  return (
    <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
      {/* Seção de Lavagem a Domicílio ou Retirada e Entrega */}
      <h2 className="text-lg font-bold mb-4">
        Lavagem a Domicílio ou Retirada e Entrega
      </h2>
      <div className="relative mb-8">
        {/* Botões de navegação (visíveis apenas em telas pequenas) */}
        <button
          className="absolute left-2 top-1/2 transform -translate-y-1/2 bg-[#ffffff8a] p-2 rounded-full shadow-md z-10 hover:bg-gray-300 transition duration-200 sm:hidden"
          onClick={() => scrollLeft(optionsRef1)}
        >
          <ChevronLeftIcon className="w-6 h-6 text-gray-800" />
        </button>

        {/* Cards */}
        <div
          ref={optionsRef1}
          className="flex gap-4 overflow-x-hidden scrollbar-hide  sm:grid sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 sm:gap-6"
        >
          {domicilieDeliveryCompanies.map((company, index) => (
            <div
              key={index}
              className="flex-shrink-0 w-52 h-36 rounded-lg shadow-md relative hover:shadow-lg transition-shadow duration-200 sm:w-full sm:h-48 sm:flex-shrink"
            >
              <a href={company.link} rel="noopener noreferrer">
                <img
                  src={company.image}
                  alt={company.name}
                  className="w-full h-full object-cover rounded-lg"
                />
                {/* Nome da empresa (visível apenas em telas maiores) */}
                <div className="hidden sm:block absolute bottom-0 left-0 right-0 bg-black bg-opacity-50 text-white p-2 rounded-b-lg">
                  <span className="text-sm font-medium">{company.name}</span>
                </div>
              </a>
            </div>
          ))}
        </div>

        {/* Botões de navegação (visíveis apenas em telas pequenas) */}
        <button
          className="absolute right-2 top-1/2 transform -translate-y-1/2 bg-[#ffffff8a] p-2 rounded-full shadow-md z-10 hover:bg-gray-300 transition duration-200 sm:hidden"
          onClick={() => scrollRight(optionsRef1)}
        >
          <ChevronRightIcon className="w-6 h-6 text-gray-800" />
        </button>
      </div>

      {/* Seção de A 5 km de você */}
      <h2 className="text-lg font-bold mb-4">Pertos de você</h2>
      <div className="relative mb-6">
        {/* Botões de navegação (visíveis apenas em telas pequenas) */}
        <button
          className="absolute left-2 top-1/2 transform -translate-y-1/2 bg-[#ffffff8a] p-2 rounded-full shadow-md z-10 hover:bg-gray-300 transition duration-200 sm:hidden"
          onClick={() => scrollLeft(optionsRef2)}
        >
          <ChevronLeftIcon className="w-6 h-6 text-gray-800" />
        </button>

        {/* Cards */}
        <div
          ref={optionsRef2}
          className="flex gap-4 overflow-x-hidden scrollbar-hide sm:grid sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 sm:gap-6"
        >
          {nearbyCompanies.map((company, index) => (
            <div
              key={index}
              className="flex-shrink-0 w-52 h-36 rounded-lg shadow-md relative hover:shadow-lg transition-shadow duration-200 sm:w-full sm:h-48 sm:flex-shrink"
            >
              <a href={company.link} target="_blank" rel="noopener noreferrer">
                <img
                  src={company.image}
                  alt={company.name}
                  className="w-full h-full object-cover rounded-lg"
                />
                {/* Nome da empresa (visível apenas em telas maiores) */}
                <div className="hidden sm:block absolute bottom-0 left-0 right-0 bg-black bg-opacity-50 text-white p-2 rounded-b-lg">
                  <span className="text-sm font-medium">{company.name}</span>
                </div>
              </a>
            </div>
          ))}
        </div>

        {/* Botões de navegação (visíveis apenas em telas pequenas) */}
        <button
          className="absolute right-2 top-1/2 transform -translate-y-1/2 bg-[#ffffff8a] p-2 rounded-full shadow-md hover:bg-gray-300 transition duration-200 sm:hidden"
          onClick={() => scrollRight(optionsRef2)}
        >
          <ChevronRightIcon className="w-6 h-6 text-gray-800" />
        </button>
      </div>
    </section>
  );
};

export default HighlightsOptions;
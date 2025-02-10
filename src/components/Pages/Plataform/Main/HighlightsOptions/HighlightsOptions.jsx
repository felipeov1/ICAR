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
    { name: "FastCar", image: "/src/images/icar-blue.jpeg", link: "/icar/empresa" },
    { name: "Lava Rápido VIP", image: "/src/images/icar-blue.jpeg", link: "/icar/empresa" },
    { name: "Guará", image: "/src/images/icar-blue.jpeg", link: "/icar/empresa" },
    { name: "Lava Bem", image: "/src/images/icar-blue.jpeg", link: "/icar/empresa" },
  ];

  const domicilieDeliveryCompanies = [
    { name: "FastCar", image: "/src/images/icar-blue.jpeg", link: "/icar/empresa" },
    { name: "Lava Rápido VIP", image: "/src/images/icar-blue.jpeg", link: "/icar/empresa" },
    { name: "Guará", image: "/src/images/icar-blue.jpeg", link: "/icar/empresa" },
    { name: "Lava Bem", image: "/src/images/icar-blue.jpeg", link: "/icar/empresa" },
  ];

  return (
    <section className="p-4">
      <h2 className="text-lg font-bold mb-4">Lavagem a Domicílio ou Retirada e Entrega</h2>
      <div className="relative mb-12">
        <button
          className="absolute left-2 top-1/2 transform -translate-y-1/2 bg-[#ffffff8a] p-2 rounded-full shadow-md z-10"
          onClick={() => scrollLeft(optionsRef1)}
          style={{ display: "block" }}
        >
          <ChevronLeftIcon className="w-6 h-6 text-gray-800" />
        </button>

        <div
          ref={optionsRef1}
          className="flex gap-4 overflow-x-auto scrollbar-hide relative"
        >
          {domicilieDeliveryCompanies.map((company, index) => (
            <div
              key={index}
              className="flex-shrink-0 w-52 h-36 rounded-lg shadow-md relative"
            >
              <a href={company.link} rel="noopener noreferrer">
                <img
                  src={company.image}
                  alt={company.name}
                  className="w-full h-full object-cover rounded-lg"
                />
              </a>
            </div>
          ))}
        </div>

        <button
          className="absolute right-2 top-1/2 transform -translate-y-1/2 bg-[#ffffff8a] p-2 rounded-full shadow-md z-10"
          onClick={() => scrollRight(optionsRef1)}
          style={{ display: "block" }}
        >
          <ChevronRightIcon className="w-6 h-6 text-gray-800" />
        </button>
      </div>

      <h2 className="text-lg font-bold mb-4">A 5 km de você</h2>
      <div className="relative mb-6">
        <button
          className="absolute left-2 top-1/2 transform -translate-y-1/2 bg-[#ffffff8a] p-2 rounded-full shadow-md z-10"
          onClick={() => scrollLeft(optionsRef2)}
          style={{ display: "block" }}
        >
          <ChevronLeftIcon className="w-6 h-6 text-gray-800" />
        </button>

        <div
          ref={optionsRef2}
          className="flex gap-4 overflow-x-auto scrollbar-hide relative"
        >
          {nearbyCompanies.map((company, index) => (
            <div
              key={index}
              className="flex-shrink-0 w-52 h-36 rounded-lg shadow-md relative"
            >
              <a href={company.link} target="_blank" rel="noopener noreferrer">
                <img
                  src={company.image}
                  alt={company.name}
                  className="w-full h-full object-cover rounded-lg"
                />
              </a>
            </div>
          ))}
        </div>

        <button
          className="absolute right-2 top-1/2 transform -translate-y-1/2 bg-[#ffffff8a] p-2 rounded-full shadow-md z-10"
          onClick={() => scrollRight(optionsRef2)}
          style={{ display: "block" }}
        >
          <ChevronRightIcon className="w-6 h-6 text-gray-800" />
        </button>
      </div>
    </section>
  );
};

export default HighlightsOptions;

import React, { useRef } from "react";
import { useSwipeable } from "react-swipeable";
import carWashImage from "../../../../public/assets/images/logo/icar-logo-blue.jpg";

const HighlightsOptions = () => {
  const optionsRef1 = useRef(null);

  const domicilieDeliveryCompanies = [
    { name: "FastCar", image: carWashImage, link: "/app/empresa" },
    { name: "Lava Rápido VIP", image: carWashImage, link: "/app/empresa" },
    { name: "Guará", image: carWashImage, link: "/app/empresa" },
    { name: "Lava Bem", image: carWashImage, link: "/app/empresa" },
  ];

  const handlers = useSwipeable({
    onSwipedLeft: () => optionsRef1.current.scrollBy({ left: 200, behavior: "smooth" }),
    onSwipedRight: () => optionsRef1.current.scrollBy({ left: -200, behavior: "smooth" }),
    preventDefaultTouchmoveEvent: true,
    trackMouse: false,
  });

  return (
    <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 pt-6">

      <p className="mb-4">Lavagem a Domicílio</p>
      

      <div className="relative mb-8">
        <div
          ref={optionsRef1}
          {...handlers}
          className="flex gap-4 overflow-x-auto scrollbar-hide scroll-smooth sm:grid sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 sm:gap-6"
        >
          {domicilieDeliveryCompanies.map((company, index) => (
            <div
              key={index}
              className="flex-shrink-0 w-52 h-36 rounded-lg shadow-md relative hover:shadow-lg transition-shadow duration-200 sm:w-full sm:h-48"
            >
              <a href={company.link} rel="noopener noreferrer">
                <img
                  src={company.image}
                  alt={company.name}
                  className="w-full h-full object-cover rounded-lg"
                />

                <div className="hidden sm:block absolute bottom-0 left-0 right-0 bg-black bg-opacity-50 text-white p-2 rounded-b-lg">
                  <span className="text-sm font-medium">{company.name}</span>
                </div>
              </a>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};

export default HighlightsOptions;

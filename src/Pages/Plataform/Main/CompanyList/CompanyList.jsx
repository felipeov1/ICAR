import React, { useState, useEffect, useRef, useCallback } from "react";
import logoicar from "../../../../public/assets/images/logo/icar-logo-transparent.png";
import carWashImage from "../../../../public/assets/images/logo/icar-logo-blue.jpg";

const CompanyList = () => {
  const allCompanies = [
    {
      name: "Lava-Rápido ICAR",
      rating: 4.5,
      description: "Lavagem rápida e eficiente para seu veículo.",

      image: carWashImage,
      link: "/app/empresa",
      serviceType: "A Domicilio",
    },
    {
      name: "Lava-Rápido ICAR",
      rating: 4.5,
      description: "Lavagem rápida e eficiente para seu veículo.",

      image: carWashImage,
      link: "/app/empresa",
      serviceType: "Domiciliar",
    },
    {
      name: "Lava-Rápido ICAR",
      rating: 4.5,
      description: "Lavagem rápida e eficiente para seu veículo.",

      image: carWashImage,
      link: "/app/empresa",
      serviceType: "A Domicilio",
    },
    {
      name: "Lava-Rápido ICAR",
      rating: 4.5,
      description: "Lavagem rápida e eficiente para seu veículo.",

      image: carWashImage,
      link: "/app/empresa",
      serviceType: "Domiciliar",
    },
    {
      name: "Lava-Rápido ICAR",
      rating: 4.5,
      description: "Lavagem rápida e eficiente para seu veículo.",

      image: carWashImage,
      link: "/app/empresa",
      serviceType: "A Domicilio",
    },
    {
      name: "Lava-Rápido ICAR",
      rating: 4.5,
      description: "Lavagem rápida e eficiente para seu veículo.",

      image: carWashImage,
      link: "/app/empresa",
      serviceType: "Domiciliar",
    },
    {
      name: "Lava-Rápido ICAR",
      rating: 4.5,
      description: "Lavagem rápida e eficiente para seu veículo.",

      image: carWashImage,
      link: "/app/empresa",
      serviceType: "A Domicilio",
    },
    {
      name: "Lava-Rápido ICAR",
      rating: 4.5,
      description: "Lavagem rápida e eficiente para seu veículo.",

      image: carWashImage,
      link: "/app/empresa",
      serviceType: "Domiciliar",
    },
    {
      name: "Lava-Rápido ICAR",
      rating: 4.5,
      description: "Lavagem rápida e eficiente para seu veículo.",

      image: carWashImage,
      link: "/app/empresa",
      serviceType: "A Domicilio",
    },
    {
      name: "Lava-Rápido ICAR",
      rating: 4.5,
      description: "Lavagem rápida e eficiente para seu veículo.",

      image: carWashImage,
      link: "/app/empresa",
      serviceType: "Domiciliar",
    },
    {
      name: "Lava-Rápido ICAR",
      rating: 4.5,
      description: "Lavagem rápida e eficiente para seu veículo.",

      image: carWashImage,
      link: "/app/empresa",
      serviceType: "A Domicilio",
    },
    {
      name: "Lava-Rápido ICAR",
      rating: 4.5,
      description: "Lavagem rápida e eficiente para seu veículo.",

      image: carWashImage,
      link: "/app/empresa",
      serviceType: "Domiciliar",
    },
    {
      name: "Lava-Rápido ICAR",
      rating: 4.5,
      description: "Lavagem rápida e eficiente para seu veículo.",

      image: carWashImage,
      link: "/app/empresa",
      serviceType: "A Domicilio",
    },
    {
      name: "Lava-Rápido ICAR",
      rating: 4.5,
      description: "Lavagem rápida e eficiente para seu veículo.",

      image: carWashImage,
      link: "/app/empresa",
      serviceType: "Domiciliar",
    },
    {
      name: "Lava-Rápido ICAR",
      rating: 4.5,
      description: "Lavagem rápida e eficiente para seu veículo.",

      image: carWashImage,
      link: "/app/empresa",
      serviceType: "A Domicilio",
    },
    {
      name: "Lava-Rápido ICAR",
      rating: 4.5,
      description: "Lavagem rápida e eficiente para seu veículo.",

      image: carWashImage,
      link: "/app/empresa",
      serviceType: "Domiciliar",
    },
    // Adicione mais empresas aqui...
  ];

  const ITEMS_PER_PAGE = 6;
  const [visibleCompanies, setVisibleCompanies] = useState(ITEMS_PER_PAGE);
  const [loading, setLoading] = useState(false);
  const [filteredCompanies, setFilteredCompanies] = useState(allCompanies);
  const [selectedServiceType, setSelectedServiceType] =
    useState("Todas Empresas");

  const observerRef = useRef(null);

  const loadMoreCompanies = useCallback(() => {
    if (!loading && visibleCompanies < filteredCompanies.length) {
      setLoading(true);
      setTimeout(() => {
        setVisibleCompanies((prev) => prev + ITEMS_PER_PAGE);
        setLoading(false);
      }, 1000);
    }
  }, [loading, visibleCompanies, filteredCompanies.length]);

  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting) {
          loadMoreCompanies();
        }
      },
      { threshold: 1.0 }
    );

    if (observerRef.current) {
      observer.observe(observerRef.current);
    }

    return () => {
      if (observerRef.current) {
        observer.unobserve(observerRef.current);
      }
    };
  }, [loadMoreCompanies]);

  const handleFilterChange = (event) => {
    const serviceType = event.target.value;
    setSelectedServiceType(serviceType);
    if (serviceType === "Todas Empresas") {
      setFilteredCompanies(allCompanies);
    } else {
      setFilteredCompanies(
        allCompanies.filter((company) => company.serviceType === serviceType)
      );
    }
    setVisibleCompanies(ITEMS_PER_PAGE);
  };

  return (
    <section className="lg:max-w-7xl lg:mx-auto lg:px-4 lg:py-6 p-4 pb-24">

      <h2 className="text-lg font-bold mb-4">Empresas</h2>
      <div className="flex flex-row sm:flex-row gap-2 mb-6">
        <select
          value={selectedServiceType}
          onChange={handleFilterChange}
          className="px-4 py-2 rounded-xl border shadow-sm bg-gray-200 hover:bg-[#1e3a8a] hover:text-white"
        >
          <option value="Todas Empresas">Todas Empresas</option>
          <option value="A Domicilio">A Domicilio</option>
          <option value="Domiciliar">Domiciliar</option>
        </select>
      </div>


      <ul className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredCompanies.slice(0, visibleCompanies).map((company, index) => (
          <li
            key={index}
            className="flex items-start bg-white p-4 rounded-lg shadow-md hover:shadow-lg transition-shadow duration-200"
          >
            <a href={company.link} className="flex w-full">
              <div className="flex-shrink-0">
                <img
                  src={company.image}
                  alt={company.name}
                  className="w-20 h-20 rounded-full object-cover"
                />
              </div>

              <div className="flex-1 ml-4">
                <div className="flex items-center justify-between">
                  <h3 className="text-blue-500 font-bold text-lg">
                    {company.name}
                  </h3>
                  <div className="flex items-center space-x-1">
                    <span className="text-yellow-500 text-lg">★</span>
                    <span className="text-gray-700 font-bold">
                      {company.rating.toLocaleString("pt-BR", {
                        minimumFractionDigits: 1,
                        maximumFractionDigits: 1,
                      })}
                    </span>
                  </div>
                </div>
                <p className="text-gray-500 text-sm mt-1">
                  {company.description}
                </p>
              </div>
            </a>
          </li>
        ))}
      </ul>


      <div
        ref={observerRef}
        className="w-full flex justify-center items-center py-4"
      >
        {loading && (
          <div className="animate-spin h-6 w-6 border-4 border-blue-500 border-t-transparent rounded-full"></div>
        )}
      </div>
    </section>
  );
};

export default CompanyList;

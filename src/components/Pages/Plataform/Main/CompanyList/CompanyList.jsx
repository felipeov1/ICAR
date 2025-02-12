import React, { useState, useEffect, useRef, useCallback } from "react";

const CompanyList = () => {
  const allCompanies = [
    {
      name: "Lava-Rápido ICAR",
      rating: 4.5,
      description: "Lavagem rápida e eficiente para seu veículo.",
      distance: 2.3,
      image: "/src/images/icar-blue.jpeg",
      link: "/icar/empresa",  
    },
    {
      name: "Lava-Rápido ICAR",
      rating: 4.5,
      description: "Lavagem rápida e eficiente para seu veículo.",
      distance: 2.3,
      image: "/src/images/icar-blue.jpeg",
      link: "/icar/empresa",  
    },
    {
      name: "Lava-Rápido ICAR",
      rating: 4.5,
      description: "Lavagem rápida e eficiente para seu veículo.",
      distance: 2.3,
      image: "/src/images/icar-blue.jpeg",
      link: "/icar/empresa",  
    },
    {
      name: "Lava-Rápido ICAR",
      rating: 4.5,
      description: "Lavagem rápida e eficiente para seu veículo.",
      distance: 2.3,
      image: "/src/images/icar-blue.jpeg",
      link: "/icar/empresa",  
    },
    {
      name: "Lava-Rápido ICAR",
      rating: 4.5,
      description: "Lavagem rápida e eficiente para seu veículo.",
      distance: 2.3,
      image: "/src/images/icar-blue.jpeg",
      link: "/icar/empresa",  
    },
    {
      name: "Lava-Rápido ICAR",
      rating: 4.5,
      description: "Lavagem rápida e eficiente para seu veículo.",
      distance: 2.3,
      image: "/src/images/icar-blue.jpeg",
      link: "/icar/empresa",  
    },
    {
      name: "Lava-Rápido ICAR",
      rating: 4.5,
      description: "Lavagem rápida e eficiente para seu veículo.",
      distance: 2.3,
      image: "/src/images/icar-blue.jpeg",
      link: "/icar/empresa",  
    },
    {
      name: "Lava-Rápido ICAR",
      rating: 4.5,
      description: "Lavagem rápida e eficiente para seu veículo.",
      distance: 2.3,
      image: "/src/images/icar-blue.jpeg",
      link: "/icar/empresa",  
    },
  ];

  const ITEMS_PER_PAGE = 3; 
  const [visibleCompanies, setVisibleCompanies] = useState(ITEMS_PER_PAGE);
  const [loading, setLoading] = useState(false);
  const observerRef = useRef(null);

  const loadMoreCompanies = useCallback(() => {
    if (!loading && visibleCompanies < allCompanies.length) {
      setLoading(true);
      setTimeout(() => {
        setVisibleCompanies((prev) => prev + ITEMS_PER_PAGE);
        setLoading(false);
      }, 1000); 
    }
  }, [loading, visibleCompanies, allCompanies.length]);

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

  return (
    <section className="p-4 pb-24">
      <h2 className="text-lg font-bold mb-4">Todas Empresas</h2>
      <ul className="space-y-4">
        {allCompanies.slice(0, visibleCompanies).map((company, index) => (
          <li
            key={index}
            className="flex items-start bg-white p-4 rounded-lg shadow-md space-x-4 relative"
          >
            <a
              href={company.link} 
              className="flex w-full" 
            >
              <div className="flex-shrink-0">
                <img
                  src={company.image}
                  alt={company.name}
                  className="w-20 h-20 rounded-full object-cover"
                />
              </div>

              <div className="flex-1 ml-4">
                <h3 className="text-blue-500 font-bold text-lg">
                  {company.name}
                </h3>
                <p className="text-gray-500 text-sm mt-1">
                  {company.description}
                </p>
                <p className="text-gray-400 text-xs mt-2">
                  Distância:{" "}
                  {company.distance.toLocaleString("pt-BR", {
                    minimumFractionDigits: 1,
                    maximumFractionDigits: 1,
                  })}{" "}
                  km
                </p>
              </div>

              <div className="absolute top-4 right-4 flex items-center space-x-1">
                <span className="text-yellow-500 text-lg">★</span>
                <span className="text-gray-700 font-bold">
                  {company.rating.toLocaleString("pt-BR", {
                    minimumFractionDigits: 1,
                    maximumFractionDigits: 1,
                  })}
                </span>
              </div>
            </a>
          </li>
        ))}
      </ul>


      <div ref={observerRef} className="w-full flex justify-center items-center py-4">
        {loading && (
          <div className="animate-spin h-6 w-6 border-4 border-blue-500 border-t-transparent rounded-full"></div>
        )}
      </div>
    </section>
  );
};

export default CompanyList;
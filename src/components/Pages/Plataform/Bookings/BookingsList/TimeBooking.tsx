import React, { useState, useEffect, useRef, useCallback } from "react";
import Card from "./Card";

const ITEMS_PER_PAGE = 3; 

const TimeBooking = () => {
  const [activeTab, setActiveTab] = useState("agendados");
  const [notification, setNotification] = useState<{ message: string; type: string } | null>(null);
  const [visibleItems, setVisibleItems] = useState(ITEMS_PER_PAGE);
  const [loading, setLoading] = useState(false);
  const observerRef = useRef<HTMLDivElement | null>(null);

  const services = [
    { id: 1, service: "Lavagem completa", scheduledDate: "2024-11-11 11:00", value: "R$50,00", vehicle: "Ford Ka" },
    { id: 2, service: "Higienização interna", scheduledDate: "2025-07-10 15:30", value: "R$80,00", vehicle: "Chevrolet Onix" },
    { id: 3, service: "Polimento e enceramento", scheduledDate: "2025-07-15 09:00", value: "R$120,00", vehicle: "Toyota Corolla" },
    { id: 4, service: "Cristalização de vidros", scheduledDate: "2025-08-01 10:00", value: "R$90,00", vehicle: "Honda Civic" },
    { id: 5, service: "Lavagem de motor", scheduledDate: "2025-09-20 14:00", value: "R$70,00", vehicle: "Volkswagen Gol" },
    { id: 6, service: "Vitrificação de pintura", scheduledDate: "2025-10-05 13:00", value: "R$200,00", vehicle: "Jeep Compass" },
    { id: 7, service: "Revitalização de faróis", scheduledDate: "2025-10-15 09:30", value: "R$50,00", vehicle: "Renault Sandero" },
  ];

  const today = new Date();
  const agendados = services.filter((s) => new Date(s.scheduledDate) >= today);
  const anteriores = services.filter((s) => new Date(s.scheduledDate) < today);
  const filteredServices = activeTab === "agendados" ? agendados : anteriores;
  const displayedServices = filteredServices.slice(0, visibleItems);

  const showNotification = (notification: { message: string; type: string }) => {
    setNotification(notification);
    setTimeout(() => setNotification(null), 900);
  };

  const loadMore = useCallback(() => {
    if (!loading && visibleItems < filteredServices.length) {
      setLoading(true);
      setTimeout(() => {
        setVisibleItems((prev) => prev + ITEMS_PER_PAGE);
        setLoading(false);
      }, 1000); 
    }
  }, [loading, visibleItems, filteredServices.length]);

  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting) {
          loadMore();
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
  }, [loadMore]);

  return (
    <div className="pt-2 w-full p-4">
      {notification && (
        <div className={`fixed top-16 left-1/2 transform -translate-x-1/2 text-white px-4 py-2 rounded shadow-lg w-80 
          ${notification.type === "sucess" ? "bg-green-500" : "bg-red-500"}`}>
          {notification.message}
        </div>
      )}


      <div className="flex border-b">
        <button
          className={`py-2 px-4 ${activeTab === "agendados" ? "border-b-2 border-blue-500 text-blue-500 font-bold" : "text-gray-600"}`}
          onClick={() => {
            setActiveTab("agendados");
            setVisibleItems(ITEMS_PER_PAGE);
          }}
        >
          Agendados
        </button>

        <button
          className={`py-2 px-4 ${activeTab === "anteriores" ? "border-b-2 border-blue-500 text-blue-500 font-bold" : "text-gray-600"}`}
          onClick={() => {
            setActiveTab("anteriores");
            setVisibleItems(ITEMS_PER_PAGE);
          }}
        >
          Anteriores
        </button>
      </div>


      <div className="mt-4 h-[40rem] overflow-y-auto p-2">
        <h2 className="text-lg font-bold mb-2">
          {activeTab === "agendados" ? "Agendados" : "Anteriores"}
        </h2>

        {displayedServices.length > 0 ? (
          displayedServices.map((service) => (
            <Card
              key={service.id}
              service={service}
              onCancel={activeTab === "agendados" ? () => showNotification({ message: "Agendamento cancelado!", type: "sucess" }) : undefined}
              onEdit={activeTab === "agendados" ? () => showNotification({ message: "Edição feita!", type: "sucess" }) : undefined}
            />
          ))
        ) : (
          <p className="text-gray-500">Nenhum serviço encontrado.</p>
        )}


        <div ref={observerRef} className="w-full flex justify-center items-center py-4">
          {loading && <div className="animate-spin h-6 w-6 border-4 border-blue-500 border-t-transparent rounded-full"></div>}
        </div>
      </div>
    </div>
  );
};

export default TimeBooking;

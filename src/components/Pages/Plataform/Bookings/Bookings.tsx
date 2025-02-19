import React, { useState, useEffect, useRef, useCallback } from "react";
import BookingCard from "./BookingCard";


const formatDate = (dateString: string) => {
  const [day, month, year] = dateString.split("-");
  return new Date(`${year}-${month}-${day}`);
};

const Bookings = () => {
  const [activeTab, setActiveTab] = useState("agendados");
  const [notification, setNotification] = useState<{
    message: string;
    type: string;
  } | null>(null);
  const ITEMS_PER_PAGE = 3;
  const [visibleItems, setVisibleItems] = useState(ITEMS_PER_PAGE);
  const [loading, setLoading] = useState(false);
  const observerRef = useRef<HTMLDivElement | null>(null);

  const services = [
    {
      id: 1,
      service: "Lavagem completa",
      scheduledDate: "11-11-2024 11:00",
      value: "R$50,00",
      vehicle: "Hatch",
    },
    {
      id: 2,
      service: "Higienização interna",
      scheduledDate: "10-07-2025 15:30",
      value: "R$80,00",
      vehicle: "Hatch",
    },
    {
      id: 3,
      service: "Lavagem de motor",
      scheduledDate: "20-09-2025 14:00",
      value: "R$70,00",
      vehicle: "Hatch",
    },
    {
      id: 4,
      service: "Lavagem a seco",
      scheduledDate: "05-12-2024 10:00",
      value: "R$60,00",
      vehicle: "Sedan",
    },
    {
      id: 5,
      service: "Limpeza de bancos",
      scheduledDate: "15-03-2025 09:00",
      value: "R$90,00",
      vehicle: "SUV",
    },
    {
      id: 6,
      service: "Lavagem de tapetes",
      scheduledDate: "22-06-2024 12:00",
      value: "R$40,00",
      vehicle: "Hatch",
    },
    {
      id: 7,
      service: "Limpeza de vidros",
      scheduledDate: "30-08-2025 16:00",
      value: "R$30,00",
      vehicle: "Sedan",
    },
    {
      id: 8,
      service: "Higienização de ar-condicionado",
      scheduledDate: "10-10-2024 08:00",
      value: "R$100,00",
      vehicle: "SUV",
    },
  ];

  const today = new Date();
  const agendados = services.filter((s) => formatDate(s.scheduledDate.split(" ")[0]) >= today);
  const anteriores = services.filter((s) => formatDate(s.scheduledDate.split(" ")[0]) < today);
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
        <div
          className={`fixed top-16 left-1/2 transform -translate-x-1/2 text-white px-4 py-2 rounded shadow-lg w-80 
          ${notification.type === "sucess" ? "bg-green-500" : "bg-red-500"}`}
        >
          {notification.message}
        </div>
      )}

      <div className="flex border-b">
        <button
          className={`py-2 px-4 ${
            activeTab === "agendados"
              ? "border-b-2 border-blue-500 text-blue-500 font-bold"
              : "text-gray-600"
          }`}
          onClick={() => {
            setActiveTab("agendados");
            setVisibleItems(ITEMS_PER_PAGE);
          }}
        >
          Agendados
        </button>

        <button
          className={`py-2 px-4 ${
            activeTab === "anteriores"
              ? "border-b-2 border-blue-500 text-blue-500 font-bold"
              : "text-gray-600"
          }`}
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
            <BookingCard
              key={service.id}
              service={service}
              onCancel={
                activeTab === "agendados"
                  ? () =>
                      showNotification({
                        message: "Agendamento cancelado!",
                        type: "sucess",
                      })
                  : undefined
              }
              onEdit={
                activeTab === "agendados"
                  ? () =>
                      showNotification({
                        message: "Edição feita!",
                        type: "sucess",
                      })
                  : undefined
              }
            />
          ))
        ) : (
          <p className="text-gray-500">Nenhum serviço encontrado.</p>
        )}

        <div ref={observerRef} className="w-full flex justify-center items-center py-4">
          {loading && (
            <div className="animate-spin h-6 w-6 border-4 border-blue-500 border-t-transparent rounded-full"></div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Bookings;

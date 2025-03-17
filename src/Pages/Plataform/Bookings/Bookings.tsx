import React, { useState, useEffect, useRef, useCallback } from "react";
import { toast } from "react-toastify";
import BookingCard from "./BookingCard";
import PastBookingCard from "./PastBookingCard";


const Bookings = () => {
  const [activeTab, setActiveTab] = useState("agendados");
  const ITEMS_PER_PAGE = 3;
  const [visibleItems, setVisibleItems] = useState(ITEMS_PER_PAGE);
  const [loading, setLoading] = useState(false);
  const observerRef = useRef(null);

  const services = [
    {
      id: 1,
      service_name: "Lavagem completa",
      modality: "Domiciliar",
      vehicle_type: "Hatch",
      date_time: "2025-11-11 11:00:00",
      amount_paid: "50.0",
      payment_method: "Cartão de Crédito",
      status: "pending", 
    },
    {
      id: 2,
      service_name: "Higienização interna",
      modality: "Local",
      vehicle_type: "Sedan",
      date_time: "2023-07-10 15:30:00",
      amount_paid: "80.0",
      payment_method: "Pix",
      status: "completed", 
    },
    {
      id: 3,
      service_name: "Lavagem de motor",
      modality: "Local",
      vehicle_type: "SUV",
      date_time: "2023-09-20 14:00:00",
      amount_paid: "70.0",
      payment_method: "credit_card",
      status: "canceled", 
    },
  ];

  const filteredServices =
    activeTab === "agendados"
      ? services.filter((s) => s.status === "pending") 
      : services.filter(
          (s) => s.status === "completed" || s.status === "canceled"
        ); 

  const displayedServices = filteredServices.slice(0, visibleItems);

  const loadMore = useCallback(() => {
    if (!loading && visibleItems < filteredServices.length) {
      setLoading(true);
      setTimeout(() => {
        setVisibleItems((prev) => prev + ITEMS_PER_PAGE);
        setLoading(false);
      }, 1000);
    }
  }, [loading, visibleItems, filteredServices.length]);

  const handleCancel = (serviceId) => {

    toast.success("Agendamento cancelado com sucesso!", {
      autoClose: 2000,
    });
  };

  const handleRate = (serviceId, rating, comment) => {

    toast.success("Avaliação enviada com sucesso!", {
      autoClose: 1000,
    });
  };

  const handleEdit = (serviceId, newDateTime) => {

    toast.success("Edição feita com sucesso!", {
      autoClose: 2000,
    });
  };

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
    <div className="pt-2 w-full p-4 lg:p-8 lg:max-w-6xl lg:mx-auto">
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

      <div className="mt-4 h-[40rem] overflow-y-auto p-2 lg:h-[50rem] lg:p-4">
        <h2 className="text-md font-bold mb-2 lg:text-xl">
          {activeTab === "agendados" ? "Agendamentos Ativos" : "Agendamentos Finalizados ou Cancelados"}
        </h2>

        {displayedServices.length > 0 ? (
          <div className="space-y-4">
            {displayedServices.map((service) =>
              activeTab === "agendados" ? (
                <BookingCard
                  key={service.id}
                  service={service}
                  onCancel={handleCancel}
                  onEdit={handleEdit}
                />
              ) : (
                <PastBookingCard
                  key={service.id}
                  service={service}
                  onCancel={handleCancel}
                  onRate={handleRate}
                />
              )
            )}
          </div>
        ) : (
          <p className="text-gray-500">Nenhum serviço encontrado.</p>
        )}

        <div
          ref={observerRef}
          className="w-full flex justify-center items-center py-4"
        >
          {loading && (
            <div className="animate-spin h-6 w-6 border-4 border-blue-500 border-t-transparent rounded-full"></div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Bookings;
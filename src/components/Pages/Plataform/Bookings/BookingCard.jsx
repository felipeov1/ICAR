import React, { useState } from "react";
import EditBooking from "./EditBooking";

const BookingCard = ({ service, onCancel, onEdit }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);

  return (
    <>
      <section className="w-full flex flex-row justify-between items-end p-4 bg-white shadow-lg border rounded-md mt-4">
        <div>
          <p>
            <strong>Serviço:</strong> {service.service}
          </p>
          <p className="mt-2">
            <strong>Data do Serviço:</strong> {service.scheduledDate}
          </p>
          <p className="mt-2">
            <strong>Valor:</strong> {service.value}
          </p>
          <p className="mt-2">
            <strong>Tipo do Veículo:</strong> {service.vehicle}
          </p>
        </div>

        <div className="flex justify-end mt-2">
          <button
            onClick={openModal}
            className="bg-blue-500 h-fit hover:bg-blue-700 text-white font-bold text-md py-1 px-4 rounded"
          >
            Detalhes
          </button>
        </div>
      </section>

      {isModalOpen && (
        <EditBooking onClose={closeModal} onCancel={onCancel} onEdit={onEdit} />
      )}
    </>
  );
};

export default BookingCard;

import React, { useState } from "react";
import PropTypes from "prop-types";
import Modal from "react-modal";
import { FaTimes } from "react-icons/fa";
import Calendar from "../components/Calendar";

Modal.setAppElement("#root");

const isWithin12Hours = (serviceDateTime) => {
  const now = new Date();
  const serviceDate = new Date(serviceDateTime);
  const differenceInHours = (serviceDate - now) / (1000 * 60 * 60);
  return differenceInHours < 12;
};

const BookingCard = ({ service, onCancel, onEdit }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isConfirmationModalOpen, setIsConfirmationModalOpen] = useState(false);
  const [isWarningModalOpen, setIsWarningModalOpen] = useState(false);
  const [selectedDate, setSelectedDate] = useState(new Date(service.date_time));
  const [selectedTime, setSelectedTime] = useState(
    service.date_time.split(" ")[1]
  );
  const [actionType, setActionType] = useState(null);

  const dailyAvailableTimes = {
    0: ["8:30", "10:20", "13:00", "15:40", "17:00"],
    1: ["8:30", "10:20", "13:00", "15:40", "17:00"],
    2: ["8:30", "10:20", "13:00", "15:40"],
    3: ["8:30", "10:20", "13:00", "17:00"],
    4: ["8:30", "10:20", "13:00", "15:40", "17:00"],
    5: ["8:30", "10:20", "13:00", "15:40"],
    6: [],
  };

  const [currentMonth] = useState(new Date().getMonth());
  const [currentYear] = useState(new Date().getFullYear());

  const handleDateClick = (day) => {
    const newDate = new Date(currentYear, currentMonth, day);
    setSelectedDate(newDate);
    setSelectedTime("");
  };

  const openModal = () => {
    if (isWithin12Hours(service.date_time)) {
      setIsWarningModalOpen(true);
      return;
    }
    setIsModalOpen(true);
  };

  const closeModal = () => setIsModalOpen(false);
  const closeConfirmationModal = () => setIsConfirmationModalOpen(false);
  const closeWarningModal = () => setIsWarningModalOpen(false);

  const handleSave = () => {
    if (
      isWithin12Hours(
        `${selectedDate.toISOString().split("T")[0]} ${selectedTime}`
      )
    ) {
      setIsWarningModalOpen(true);
      return;
    }

    setActionType("edit");
    setIsConfirmationModalOpen(true);
  };

  const handleCancelService = () => {
    if (isWithin12Hours(service.date_time)) {
      setIsWarningModalOpen(true);
      return;
    }

    setActionType("cancel");
    setIsConfirmationModalOpen(true);
  };

  const confirmAction = () => {
    if (actionType === "edit") {
      const newDateTime = `${
        selectedDate.toISOString().split("T")[0]
      } ${selectedTime}`;
      onEdit(service.id, newDateTime);
    } else if (actionType === "cancel") {
      onCancel(service.id);
    }
    closeConfirmationModal();
    closeModal();
  };

  return (
    <>
      <section className="w-full flex flex-col md:flex-row justify-between p-6 bg-white shadow-lg border rounded-md mt-4">

        <div className="flex-1">
          <h2 className="text-xl font-bold text-blue-800">
            {service.service_name}
          </h2>
          <div className="mt-4 space-y-2">
            <div>
              <span className="text-gray-600">Empresa: </span>
              <span className="font-medium">{service.company}</span>
            </div>
            <div>
              <span className="text-gray-600">Modalidade: </span>
              <span className="font-medium">{service.modality}</span>
            </div>
            <div>
              <span className="text-gray-600 ">Tipo do Veículo: </span>
              <span className="font-medium">{service.vehicle_type}</span>
            </div>
            <div>
              <span className="text-gray-600 ">Local: </span>
              <span className="font-medium">{service.modality}</span>
            </div>
            <div>
              <span className="text-gray-600 ">Data do Serviço: </span>
              <span className="font-medium">{service.date_time}</span>
            </div>
            <div>
              <span className="text-gray-600 ">Valor: </span>
              <span className="font-medium">R${service.amount_paid}</span>
            </div>
            <div>
              <span className="text-gray-600 ">Método de Pagamento: </span>
              <span className="font-medium">{service.payment_method}</span>
            </div>
          </div>
        </div>

        <div className="mt-4 md:mt-0 flex items-end justify-end">
          <button
            onClick={openModal}
            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-6 rounded-lg transition duration-300"
          >
            Gerenciar
          </button>
        </div>
      </section>
      <Modal
        isOpen={isModalOpen}
        onRequestClose={closeModal}
        className="bg-white p-4 mx-2 rounded-lg shadow-lg overflow-y-auto md:max-w-lg md:p-8"
        style={{
          content: {
            maxHeight: "86vh",
            margin: "auto",
            position: "relative",
            top: "5%", 
            transform: "translateY(-10%)", 
            paddingBottom: "2rem", 
          },
          overlay: {
            backgroundColor: "rgba(0, 0, 0, 0.5)",
            display: "flex",
            alignItems: "flex-start", 
            justifyContent: "center",
            paddingTop: "2rem", 
          },
        }}
      >
        <button
          onClick={closeModal}
          className="absolute top-2 right-2 p-2 text-gray-600 hover:text-gray-900"
        >
          <FaTimes className="w-6 h-6" />
        </button>

        <h2 className="text-xl font-bold mb-4">Editar Agendamento</h2>

        <div className="mb-4">
          <h3 className="font-semibold mb-4">Selecione a data</h3>
          <Calendar
            onDateClick={handleDateClick}
            selectedDate={selectedDate}
            dailyAvailableTimes={dailyAvailableTimes}
          />
        </div>

        {selectedDate && (
          <div className="mt-6">
            <p className="text-gray-700 text-lg mb-4">
              Horários disponíveis para{" "}
              <span className="font-bold">
                {selectedDate.toLocaleDateString("pt-BR")}
              </span>
            </p>
            <div className="flex flex-wrap gap-2">
              {dailyAvailableTimes[new Date(selectedDate).getDay()].map(
                (time) => (
                  <button
                    key={time}
                    className={`px-4 py-2 rounded-xl border shadow-sm transition duration-200 ease-in-out ${
                      selectedTime === time
                        ? "bg-[#1e3a8a] text-white"
                        : "bg-gray-200 hover:bg-gray-300"
                    }`}
                    onClick={() => setSelectedTime(time)}
                  >
                    {time}
                  </button>
                )
              )}
            </div>
          </div>
        )}

        <div className="flex flex-row md:flex-row   md:space-y-0 md:space-x-4 mt-6">
          <button
            onClick={handleSave}
            className="px-4 py-2 bg-orange-600 text-white rounded"
          >
            Editar
          </button>

          <button
            onClick={handleCancelService}
            className="px-4 py-2 border rounded"
          >
            Cancelar Serviço
          </button>
        </div>
      </Modal>

      {/* Modal de Aviso */}
      <Modal
        isOpen={isWarningModalOpen}
        onRequestClose={closeWarningModal}
        className="bg-white p-4 mx-2 rounded-lg shadow-lg overflow-y-auto md:max-w-lg md:p-8"
        style={{
          content: {
            maxHeight: "90vh",
            margin: "auto",
            position: "relative",
          },
          overlay: {
            backgroundColor: "rgba(0, 0, 0, 0.5)",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
          },
        }}
      >
        <h2 className="text-xl font-bold mb-4">Aviso</h2>
        <p className="mb-4">
          Não é possível {actionType === "edit" ? "editar" : "cancelar"} com
          menos de 12 horas de antecedência. Entre em contato com o suporte.
        </p>
        <div className="flex justify-end">
          <button
            onClick={closeWarningModal}
            className="px-4 py-2 bg-orange-600 text-white rounded"
          >
            Fechar
          </button>
        </div>
      </Modal>

      {/* Modal de Confirmação */}
      <Modal
        isOpen={isConfirmationModalOpen}
        onRequestClose={closeConfirmationModal}
        className="bg-white p-4 mx-2 rounded-lg shadow-lg overflow-y-auto md:max-w-lg md:p-8"
        style={{
          content: {
            maxHeight: "90vh",
            margin: "auto",
            position: "relative",
          },
          overlay: {
            backgroundColor: "rgba(0, 0, 0, 0.5)",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
          },
        }}
      >
        <h2 className="text-xl font-bold mb-4">Confirmação</h2>
        <p className="mb-4">
          Tem certeza que deseja {actionType === "edit" ? "editar" : "cancelar"}{" "}
          este serviço?
        </p>
        <div className="flex justify-end space-x-2">
          <button
            onClick={closeConfirmationModal}
            className="px-4 py-2 border rounded"
          >
            Cancelar
          </button>
          <button
            onClick={confirmAction}
            className="px-4 py-2 bg-orange-600 text-white rounded"
          >
            Confirmar
          </button>
        </div>
      </Modal>
    </>
  );
};

BookingCard.propTypes = {
  service: PropTypes.shape({
    id: PropTypes.number.isRequired,
    service_name: PropTypes.string.isRequired,
    modality: PropTypes.string.isRequired,
    vehicle_type: PropTypes.string.isRequired,
    date_time: PropTypes.string.isRequired,
    amount_paid: PropTypes.string.isRequired,
    payment_method: PropTypes.string.isRequired,
    status: PropTypes.string.isRequired,
  }).isRequired,
  onCancel: PropTypes.func.isRequired,
  onEdit: PropTypes.func.isRequired,
};

export default BookingCard;

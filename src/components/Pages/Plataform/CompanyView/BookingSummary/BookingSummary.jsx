import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { ChevronLeftIcon } from "@heroicons/react/24/outline";
import Modal from "react-modal";

Modal.setAppElement("#root");

const BookingSummary = () => {
  const location = useLocation();
  const { selectedDate, selectedTime } = location.state || {};
  const [washType, setWashType] = useState("");
  const [carType, setCarType] = useState("");
  const [locationType, setLocationType] = useState("");
  const [total, setTotal] = useState(0);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const navigate = useNavigate();

  const calculateTotal = () => {
    let washCost = 0;
    let carSizeMultiplier = 1;

    if (washType === "Simples") washCost = 30;
    if (washType === "Completa") washCost = 50;
    if (washType === "Premium") washCost = 70;

    if (carType === "Carro Pequeno") carSizeMultiplier = 1;
    if (carType === "SUV") carSizeMultiplier = 1.5;
    if (carType === "Caminhonete") carSizeMultiplier = 2;

    setTotal(washCost * carSizeMultiplier);
  };

  React.useEffect(() => {
    if (washType && carType) {
      calculateTotal();
    }
  }, [washType, carType]);

  const handleNextStep = () => {
    if (washType && carType && locationType) {
      setIsModalOpen(true); // Abre o modal de resumo
    }
  };

  const handleConfirm = () => {
    navigate("/icar/empresa/agendamento/pagamento", {
      state: {
        selectedDate,
        selectedTime,
        washType,
        carType,
        locationType,
        total,
      },
    });
  };

  return (
    <div className="bg-[#f0f0f059] h-screen pb-20">
      <div className="p-4 bg-white">
        <a
          href="/icar/empresa"
          className="top-4 left-2 text-black font-semibold text-3xl flex items-center"
        >
          <div className="w-8">
            <ChevronLeftIcon width={25} />
          </div>
          <span className="ml-2 text-black text-xl">Novo Agendamento</span>
        </a>
      </div>

      <div className="pl-6 pr-6 pt-6 pb-12 max-w-md mx-auto rounded-lg font-sans md:max-w-2xl md:shadow-lg md:bg-white md:mt-6">
        <h1 className="mb-8 mt-3 font-normal text-gray-500 text-lg md:text-xl">
          Selecione os detalhes do seu agendamento
        </h1>

        <hr className="border-gray-300 my-8" />

        <p className="text-gray-700 text-left mb-4 text-lg md:text-xl">
          <strong>Data:</strong> {selectedDate} <br />
          <strong>Horário:</strong> {selectedTime}
        </p>

        <div className="mb-4">
          <h3 className="font-semibold mb-2 md:text-lg">Tipo de Lavagem</h3>
          <div className="flex flex-wrap gap-2">
            {["Simples", "Completa", "Premium"].map((type) => (
              <button
                key={type}
                className={`px-4 py-2 rounded-xl border text-sm md:text-base md:px-6 md:py-3 ${
                  washType === type
                    ? "bg-blue-600 text-white"
                    : "bg-gray-200 hover:bg-gray-300"
                }`}
                onClick={() => setWashType(type)}
              >
                {type}
              </button>
            ))}
          </div>
        </div>

        <hr className="border-gray-300 my-8" />

        <div className="mb-4">
          <h3 className="font-semibold mb-2 md:text-lg">Tipo de Veículo</h3>
          <div className="flex flex-wrap gap-2">
            {["Carro Pequeno", "SUV", "Caminhonete"].map((type) => (
              <button
                key={type}
                className={`px-4 py-2 rounded-xl border text-sm md:text-base md:px-6 md:py-3 ${
                  carType === type
                    ? "bg-blue-600 text-white"
                    : "bg-gray-200 hover:bg-gray-300"
                }`}
                onClick={() => setCarType(type)}
              >
                {type}
              </button>
            ))}
          </div>
        </div>

        <hr className="border-gray-300 my-8" />

        <div className="mb-4">
          <h3 className="font-semibold mb-2 md:text-lg">Local do Serviço</h3>
          <div className="flex flex-wrap gap-2">
            {[
              "Lava-rápido",
              "Minha Casa - Rua xxx, 23",
              "Trabalho  - Rua xxx, 244",
            ].map((type) => (
              <button
                key={type}
                className={`px-4 py-2 rounded-xl border text-sm md:text-base md:px-6 md:py-3 ${
                  locationType === type
                    ? "bg-blue-600 text-white"
                    : "bg-gray-200 hover:bg-gray-300"
                }`}
                onClick={() => setLocationType(type)}
              >
                {type}
              </button>
            ))}

            <button className="bg-blue-500 hover:bg-blue-600 text-white font-bold text-sm md:text-base py-1 px-2 rounded-xl border md:px-4 md:py-2">
              Adicionar Endereço
            </button>
          </div>
        </div>
        <hr className="border-gray-300" />

        <div className="mt-10 mb-4">
          <h3 className="font-semibold mb-2 md:text-lg">Total</h3>
          <p className="text-lg font-bold md:text-xl">R$ {total.toFixed(2)}</p>
        </div>

        <button
          className="bg-[#1e3a8a] fixed bottom-0 left-0 w-full py-3 text-white text-lg md:static md:rounded-lg md:hover:bg-[#1c3a8a] md:transition-colors"
          onClick={handleNextStep}
          disabled={!washType || !carType || !locationType}
        >
          Ir Para Pagamento
        </button>
      </div>

      {/* Modal de Resumo */}
      <Modal
        isOpen={isModalOpen}
        onRequestClose={() => setIsModalOpen(false)}
        className="bg-white p-6 max-w-md mx-auto rounded-lg shadow-lg md:max-w-lg md:p-8"
        overlayClassName="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center"
      >
        <h2 className="text-xl font-semibold mb-4 md:text-2xl">Resumo do Agendamento</h2>
        <p className="text-gray-700 mb-2">
          <strong>Data:</strong> {selectedDate}
        </p>
        <p className="text-gray-700 mb-2">
          <strong>Horário:</strong> {selectedTime}
        </p>
        <p className="text-gray-700 mb-2">
          <strong>Tipo de Lavagem:</strong> {washType}
        </p>
        <p className="text-gray-700 mb-2">
          <strong>Tipo de Veículo:</strong> {carType}
        </p>
        <p className="text-gray-700 mb-2">
          <strong>Local do Serviço:</strong> {locationType}
        </p>
        <p className="text-gray-700 mb-4">
          <strong>Total:</strong> R$ {total.toFixed(2)}
        </p>
        <p className="text-red-600 mb-4">
          ⚠️ Verifique as informações. Não será possível editá-las após prosseguir.
        </p>
        <div className="flex justify-end space-x-2">
          <button
            onClick={() => setIsModalOpen(false)}
            className="px-4 py-2 border rounded text-gray-700 hover:bg-gray-100"
          >
            Voltar
          </button>
          <button
            onClick={handleConfirm}
            className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
          >
            Confirmar
          </button>
        </div>
      </Modal>
    </div>
  );
};

export default BookingSummary;
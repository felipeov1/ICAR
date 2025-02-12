import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { ChevronLeftIcon } from "@heroicons/react/24/outline";

const BookingSummary = () => {
  const location = useLocation();
  const { selectedDate, selectedTime } = location.state || {};
  const [washType, setWashType] = useState("");
  const [carType, setCarType] = useState("");
  const [locationType, setLocationType] = useState("");
  const [total, setTotal] = useState(0);

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
    }
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

      <div className="p-6 max-w-md mx-auto rounded-lg font-sans">
        <h1 className="mb-8 mt-3 font-normal text-gray-500 text-lg">
          Selecione os detalhes do seu agendamento
        </h1>

        <hr className="border-gray-300 my-8" />

        <p className="text-gray-700 text-left mb-4 text-lg">
          <strong>Data:</strong> {selectedDate} <br />
          <strong>Horário:</strong> {selectedTime}
        </p>

        <div className="mb-4">
          <h3 className="font-semibold mb-2">Tipo de Lavagem</h3>
          <div className="flex flex-wrap gap-2">
            {["Simples", "Completa", "Premium"].map((type) => (
              <button
                key={type}
                className={`px-4 py-2 rounded-xl border ${
                  washType === type ? "bg-blue-600 text-white" : "bg-gray-200"
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
          <h3 className="font-semibold mb-2">Tipo de Veículo</h3>
          <div className="flex flex-wrap gap-2">
            {["Carro Pequeno", "SUV", "Caminhonete"].map((type) => (
              <button
                key={type}
                className={`px-4 py-2 rounded-xl border ${
                  carType === type ? "bg-blue-600 text-white" : "bg-gray-200"
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
          <h3 className="font-semibold mb-2">Local do Serviço</h3>
          <div className="flex flex-wrap gap-2">
            {[
              "Lava-rápido",
              "Minha Casa - Rua xxx, 23",
              "Trabalho  - Rua xxx, 244",
            ].map((type) => (
              <button
                key={type}
                className={`px-4 py-2 rounded-xl border ${
                  locationType === type
                    ? "bg-blue-600 text-white"
                    : "bg-gray-200"
                }`}
                onClick={() => setLocationType(type)}
              >
                {type}
              </button>
            ))}

            {/* // Open modal to edit */}

            <button className="bg-blue-500 hover:bg-blue-600 text-white font-bold text-md py-1 px-2 rounded-xl border">
              Adicionar Endereço
            </button> 
          </div>
        </div>

        <div className="mt-6 mb-4">
          <h3 className="font-semibold mb-2">Total</h3>
          <p className="text-lg font-bold">R$ {total.toFixed(2)}</p>
        </div>

        <button
          className="bg-[#1e3a8a] fixed bottom-0 left-0 w-full py-3 text-white text-lg"
          onClick={handleNextStep}
          disabled={!washType || !carType || !locationType}
        >
          Ir Para Pagamento
        </button>
      </div>
    </div>
  );
};

export default BookingSummary;

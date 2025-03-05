import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const ServicesAvaiables = () => {
  const [selectedService, setSelectedService] = useState(""); // Serviço selecionado
  const [selectedOption, setSelectedOption] = useState(""); // Opção de atendimento


  const navigate = useNavigate();

  // Simulando os serviços disponíveis com valores e opções de atendimento
  const availableServices = [
    { id: 1, name: "Lavagem Completa", price: 100 },
    { id: 2, name: "Polimento", price: 150 },
    { id: 3, name: "Higienização Interna", price: 80 },
    { id: 4, name: "Troca de Óleo", price: 120 },
  ];

  // Simulando locais de atendimento (lava-rápido ou domicilio)
  const serviceLocations = {
    "Lavagem Completa": ["Lava Rápido", "Busca e Entrega"],
    Polimento: ["Lava Rápido", "Busca e Entrega"],
    "Higienização Interna": [], // Serviço sem opções (atendimento na empresa)
    "Troca de Óleo": ["Busca e Entrega"],
  };

  const handleServiceSelect = (service) => {
    setSelectedService(service);
    setSelectedOption(""); // Resetar opção de atendimento ao selecionar novo serviço

    // Se o serviço não tiver opções de atendimento, definir como "Na Empresa"
    if (!serviceLocations[service] || serviceLocations[service].length === 0) {
      setSelectedOption("Na Empresa");
    }
  };

  const handleOptionSelect = (option) => {
    setSelectedOption(option);
  };

  const handleNextStep = () => {
    if (selectedService) {
      // Enviar o nome do serviço, a opção de atendimento e o preço do serviço selecionado
      const service = availableServices.find(
        (service) => service.name === selectedService
      );
      navigate("/icar/empresa/agendamento", {
        state: {
          selectedService,
          selectedOption: selectedOption || "Na Empresa", // Se não houver opção, assume "Na Empresa"
          price: service?.price, // Enviar o preço do serviço
        },
      });
    }
  };

  return (
    <div className="p-6 max-w-4xl mx-auto bg-white rounded-lg shadow-lg">
      <h3 className="text-2xl font-bold text-center mb-6 text-gray-800">
        Selecione o Serviço
      </h3>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        {availableServices.map((service) => (
          <div
            key={service.id}
            className={`p-4 rounded-xl border shadow-sm cursor-pointer transition-all duration-300 ${
              selectedService === service.name
                ? "bg-[rgb(30,58,138)] text-white transform scale-105"
                : "bg-gray-50 hover:bg-gray-100"
            }`}
            onClick={() => handleServiceSelect(service.name)}
          >
            <h4 className="font-semibold text-lg">{service.name}</h4>
            <p>R$ {service.price}</p>
          </div>
        ))}
      </div>

      {selectedService && (
        <div className="mt-8">
          <h4 className="font-semibold text-xl text-center text-gray-800 mb-4">
            Selecione a opção de atendimento
          </h4>
          {serviceLocations[selectedService]?.length > 0 ? (
            <>
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                {serviceLocations[selectedService]?.map((location) => (
                  <div
                    key={location}
                    className={`p-4 rounded-xl border shadow-sm cursor-pointer transition-all duration-300 ${
                      selectedOption === location
                        ? "bg-[#1e3a8a] text-white transform scale-105"
                        : "bg-gray-50 hover:bg-gray-100"
                    }`}
                    onClick={() => handleOptionSelect(location)}
                  >
                    <h5 className="font-semibold">{location}</h5>
                    <p className="text-sm">
                      {location === "Lava Rápido" ? "No local" : "Endereço Particular"}
                    </p>
                  </div>
                ))}
              </div>
            </>
          ) : (
            <p className="text-gray-600 text-center">
              Este serviço está disponível apenas no estabelecimento.
            </p>
          )}
        </div>
      )}

      <button
        className="mt-8 w-full py-3 bg-[#1e3a8a] text-white rounded-lg font-semibold hover:bg-blue-600 transition-all duration-300 disabled:bg-gray-400 disabled:cursor-not-allowed"
        onClick={handleNextStep}
        disabled={
          !selectedService ||
          (serviceLocations[selectedService]?.length > 0 && !selectedOption)
        }
      >
        Próximo Passo →
      </button>
    </div>
  );
};

export default ServicesAvaiables;

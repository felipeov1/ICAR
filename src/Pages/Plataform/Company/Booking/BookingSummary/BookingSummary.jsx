import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { ChevronLeftIcon } from "@heroicons/react/24/outline";
import Modal from "react-modal";

Modal.setAppElement("#root");

const BookingSummary = () => {
  const location = useLocation();
  const { selectedService, selectedOption, price } = location.state || {};
  const [selectedDate, setSelectedDate] = useState(null);
  const [selectedTime, setSelectedTime] = useState("");
  const [selectedAddress, setSelectedAddress] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isAddressModalOpen, setIsAddressModalOpen] = useState(false);
  const [newAddress, setNewAddress] = useState("");
  const [error, setError] = useState("");

  const navigate = useNavigate();

  // Simulando horários disponíveis por dia
  const dailyAvailableTimes = {
    0: ["8:30", "10:20", "13:00", "15:40", "17:00"], // Segunda
    1: ["8:30", "10:20", "13:00", "15:40", "17:00"], // Terça
    2: ["8:30", "10:20", "13:00", "15:40"], // Quarta
    3: ["8:30", "10:20", "13:00", "17:00"], // Quinta
    4: ["8:30", "10:20", "13:00", "15:40", "17:00"], // Sexta
    5: ["8:30", "10:20", "13:00", "15:40"], // Sábado
    6: [], // Domingo (não atende)
  };

  const [currentMonth] = useState(new Date().getMonth());
  const [currentYear] = useState(new Date().getFullYear());

  const getMonthDays = (month, year) => {
    const date = new Date(year, month, 1);
    const days = [];
    while (date.getMonth() === month) {
      days.push(date.getDate());
      date.setDate(date.getDate() + 1);
    }
    return days;
  };

  const isDisabled = (day) => {
    const today = new Date();
    const selectedDate = new Date(currentYear, currentMonth, day);
    const todayMidnight = new Date(
      today.getFullYear(),
      today.getMonth(),
      today.getDate()
    );
    const isPast = selectedDate < todayMidnight;
    const currentDayOfWeek = selectedDate.getDay();
    return isPast || dailyAvailableTimes[currentDayOfWeek].length === 0;
  };

  const handleDateClick = (day) => {
    const newDate = new Date(currentYear, currentMonth, day);
    setSelectedDate(newDate);
    setSelectedTime("");
  };

  const handleNextStep = () => {
    if (
      !selectedDate ||
      !selectedTime ||
      (selectedOption !== "Lava Rápido" && !selectedAddress)
    ) {
      setError("Por favor, preencha todos os campos obrigatórios.");
      return;
    }
    setIsModalOpen(true);
  };

  const handleConfirm = () => {
    navigate("/icar/empresa/agendamento/pagamento", {
      state: {
        selectedService,
        selectedOption,
        selectedDate: selectedDate.toLocaleDateString("pt-BR"),
        selectedTime,
        selectedAddress,
      },
    });
  };

  const handleAddAddress = () => {
    if (newAddress.trim() === "") {
      setError("Por favor, insira um endereço válido.");
      return;
    }
    setSelectedAddress(newAddress);
    setNewAddress("");
    setIsAddressModalOpen(false);
  };

  const monthDays = getMonthDays(currentMonth, currentYear);
  const weekDays = ["Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"];

  return (
    <div className="bg-[#f0f0f059] min-h-screen pb-20">
      <div className="p-4 bg-white shadow-sm">
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
        <h1 className="mt-2 font-normal text-gray-500 text-lg md:text-xl">
          Finalização do Agendamento
        </h1>

        <hr className="border-gray-300 mb-8 mt-6" />

        {/* Dados Selecionados Anteriormente */}
        <div className="mb-8">
          <p className="text-gray-700 text-xl ">
            <strong>Serviço:</strong> {selectedService}
          </p>
          <p className="text-gray-700 mt-4 text-xl">
            <strong>Opção de Atendimento:</strong> {selectedOption}
          </p>
          <p className="text-gray-700 mt-4 text-xl">
            <strong>Valor:</strong> R${price.toFixed(2)}
          </p>
        </div>

        {/* Seleção de Data e Horário */}
        <div className="mb-8">
          <h3 className="font-semibold mb-4 text-lg">Selecione a data e o horário</h3>
          <div className="grid grid-cols-7 gap-2 mb-2">
            {weekDays.map((day) => (
              <div key={day} className="text-center font-sans text-[#8f8f8f62]">
                {day}
              </div>
            ))}
          </div>
          <div className="grid grid-cols-7 gap-2">
            {monthDays.map((day) => {
              const currentDayOfWeek = new Date(
                currentYear,
                currentMonth,
                day
              ).getDay();
              const isSelected = selectedDate?.getDate() === day;
              const disabled =
                isDisabled(day) ||
                dailyAvailableTimes[currentDayOfWeek].length === 0;

              return (
                <button
                  key={day}
                  className={`p-2 rounded-2xl text-center transition duration-200 ease-in-out ${
                    isSelected
                      ? "bg-[#1e3a8a] text-white"
                      : disabled
                      ? "bg-gray-200 text-gray-400 cursor-not-allowed"
                      : "bg-gray-200 hover:bg-[#1e3a8a] hover:text-white"
                  }`}
                  onClick={() => !disabled && handleDateClick(day)}
                  disabled={disabled}
                >
                  {day}
                </button>
              );
            })}
          </div>

          {selectedDate && (
            <div className="mt-6">
              <p className="text-gray-700 text-lg mb-4">
                Horários disponíveis para{" "}
                <span className="font-bold">{selectedDate.toLocaleDateString("pt-BR")}</span>
              </p>
              <div className="flex flex-wrap gap-2">
                {dailyAvailableTimes[new Date(selectedDate).getDay()].map(
                  (time) => (
                    <button
                      key={time}
                      className={`px-4 py-2 rounded-xl  border shadow-sm transition duration-200 ease-in-out ${
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
        </div>

        {/* Seleção de Endereço (se necessário) */}
        {selectedOption !== "Lava Rápido" && (
          <div className="mb-8">
            <h3 className="font-semibold mb-4 md:text-lg">Endereço</h3>
            <div className="flex flex-wrap gap-2">
              {["Casa - Rua Exemplo, 123", "Trabalho - Avenida Teste, 456"].map(
                (address) => (
                  <button
                    key={address}
                    className={`px-4 py-2 rounded-xl border shadow-sm transition duration-200 ease-in-out ${
                      selectedAddress === address
                        ? "bg-[#1e3a8a] text-white"
                        : "bg-gray-200 hover:bg-gray-300"
                    }`}
                    onClick={() => setSelectedAddress(address)}
                  >
                    {address}
                  </button>
                )
              )}
              <button
                className="bg-blue-500 hover:bg-blue-600 text-white font-bold text-sm md:text-base py-1 px-2 rounded-xl border md:px-4 md:py-2"
                onClick={() => setIsAddressModalOpen(true)}
              >
                + Adicionar Endereço
              </button>
            </div>
          </div>
        )}

        {/* Mensagem de Erro */}
        {error && <div className="text-red-600 mb-4 text-sm">{error}</div>}

        {/* Botão de Próximo Passo */}
        <button
          className="bg-[#1e3a8a] fixed bottom-0 left-0 w-full py-3 text-white text-lg md:static md:rounded-lg md:hover:bg-[#1c3a8a] md:transition-colors"
          onClick={handleNextStep}
        >
          Ir Para Pagamento
        </button>
      </div>

      {/* Modal de Confirmação */}
      <Modal
        isOpen={isModalOpen}
        onRequestClose={() => setIsModalOpen(false)}
        className="bg-white p-6 max-w-md mx-auto rounded-lg shadow-lg md:max-w-lg md:p-8"
        overlayClassName="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center"
      >
        <h2 className="text-xl font-semibold mb-4 md:text-2xl">
          Resumo do Agendamento
        </h2>
        <p className="text-gray-700 mb-2">
          <strong>Serviço:</strong> {selectedService}
        </p>
        <p className="text-gray-700 mb-2">
          <strong>Opção de Atendimento:</strong> {selectedOption}
        </p>
        <p className="text-gray-700 mb-2">
          <strong>Data:</strong> {selectedDate?.toLocaleDateString("pt-BR")}
        </p>
        <p className="text-gray-700 mb-2">
          <strong>Horário:</strong> {selectedTime}
        </p>
        {selectedOption !== "Lava Rápido" && (
          <p className="text-gray-700 mb-2">
            <strong>Endereço:</strong> {selectedAddress}
          </p>
        )}
        <p className="text-gray-700 mb-4">
          <strong>Total:</strong> R$ {price.toFixed(2)}
        </p>
        <p className="text-red-600 mb-4">
          ⚠️ Verifique as informações. Não será possível editá-las após
          prosseguir.
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

      {/* Modal de Adicionar Endereço */}
      <Modal
        isOpen={isAddressModalOpen}
        onRequestClose={() => setIsAddressModalOpen(false)}
        className="bg-white p-6 max-w-md mx-auto rounded-lg shadow-lg md:max-w-lg md:p-8"
        overlayClassName="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center"
      >
        <h2 className="text-xl font-semibold mb-4 md:text-2xl">
          Adicionar Endereço
        </h2>
        <input
          type="text"
          value={newAddress}
          onChange={(e) => setNewAddress(e.target.value)}
          placeholder="Digite o novo endereço"
          className="w-full p-2 border rounded-lg mb-4"
        />
        {error && <p className="text-red-600 text-sm mb-4">{error}</p>}
        <div className="flex justify-end space-x-2">
          <button
            onClick={() => setIsAddressModalOpen(false)}
            className="px-4 py-2 border rounded text-gray-700 hover:bg-gray-100"
          >
            Cancelar
          </button>
          <button
            onClick={handleAddAddress}
            className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
          >
            Salvar
          </button>
        </div>
      </Modal>
    </div>
  );
};

export default BookingSummary;

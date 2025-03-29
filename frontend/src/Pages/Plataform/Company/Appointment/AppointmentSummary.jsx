import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { ChevronLeftIcon } from "@heroicons/react/24/outline";
import Modal from "react-modal";
import { FaPlus } from "react-icons/fa";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Calendar from "../../components/Calendar";
import ModalAddress from "../../components/ModalAddress";

Modal.setAppElement("#root");

const AppointmentSummary = () => {
  const location = useLocation();
  const { selectedService, selectedOption, price } = location.state || {};
  const [selectedDate, setSelectedDate] = useState(null);
  const [selectedTime, setSelectedTime] = useState("");
  const [selectedAddress, setSelectedAddress] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isModalAddressOpen, setIsModalAddressOpen] = useState(false);
  const [addresses, setAddresses] = useState([
    {
      id: 3,
      label: "Casa",
      address: "Rua das Flores, 123",
      zip: "12345-678",
      state: "Paraná",
      city: "Londrina",
    },
    {
      id: 13,
      label: "eee",
      address: "Rua das Flores, 123",
      zip: "12345-678",
      state: "Paraná",
      city: "Londrina",
    },
  ]);

  // Estados para o cupom e desconto
  const [couponCode, setCouponCode] = useState("");
  const [discount, setDiscount] = useState(0);

  const navigate = useNavigate();

  const lavaRapidoAddress = "Rua do Lava Rápido, 123 - Centro";

  const domicilioObservations =
    "Para essa modalidade, o lava-rápido escolhido utiliza água do local. Certifique-se de que há disponibilidade de água e energia elétrica no endereço informado.";

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

  // Função para aplicar o cupom
  const applyCoupon = () => {
    const validCoupons = {
      DESCONTO10: 10,
      PROMO20: 20,
    };

    if (validCoupons[couponCode]) {
      setDiscount(validCoupons[couponCode]);
      toast.success(
        `Cupom aplicado: ${couponCode} (${validCoupons[couponCode]}% de desconto)!`,
        {
          autoClose: 2000,
        }
      );
    } else {
      setDiscount(0);
      toast.error("Cupom inválido ou expirado.", { autoClose: 2000 });
    }
  };

  const handleNextStep = () => {
    if (!selectedDate || !selectedTime) {
      toast.error("Por favor, selecione uma data e um horário.", {
        autoClose: 2000,
      });
      return;
    }

    if (selectedOption === "Domiciliar" && !selectedAddress) {
      toast.error("Por favor, selecione ou adicione um endereço.", {
        autoClose: 2000,
      });
      return;
    }

    setIsModalOpen(true);
  };

  const handleConfirm = () => {
    navigate("/app/empresa/agendamento/pagamento", {
      state: {
        selectedService,
        selectedOption,
        selectedDate: selectedDate.toLocaleDateString("pt-BR"),
        selectedTime,
        selectedAddress:
          selectedOption === "Lava Rápido"
            ? lavaRapidoAddress
            : selectedAddress,
        totalPrice: price * (1 - discount / 100), // Envia o valor com desconto
      },
    });
  };

  const openModalAddress = () => {
    setIsModalAddressOpen(true);
  };

  const closeModalAddress = () => {
    setIsModalAddressOpen(false);
  };

  const handleSaveAddress = (formData) => {
    setAddresses([...addresses, { ...formData, id: Date.now() }]);
    toast.success("Endereço adicionado com sucesso!", { autoClose: 2000 });
    closeModalAddress();
  };

  return (
    <div className="bg-[#f0f0f059] min-h-screen pb-20">
      <ToastContainer />
      <div className="p-4 bg-white shadow-sm">
        <a
          href="/app/empresa"
          className="top-4 left-2 text-black font-semibold text-3xl flex items-center"
        >
          <div className="w-8">
            <ChevronLeftIcon width={25} />
          </div>
          <span className="ml-2 text-black text-xl">Novo Agendamento</span>
        </a>
      </div>

      <div className="pl-6 pr-6 pt-6 max-w-md mx-auto rounded-lg font-sans md:max-w-2xl md:shadow-lg md:bg-white md:mt-6">
        <h1 className="mt-2 font-normal text-gray-500 text-lg md:text-xl">
          Finalização do Agendamento
        </h1>

        <hr className="border-gray-300 mb-8 mt-6" />

        <div className="mb-8">
          <p className="text-gray-700 text-xl ">
            <strong>Serviço:</strong> {selectedService}
          </p>
          <p className="text-gray-700 mt-4 text-xl">
            <strong>Opção de Atendimento:</strong> {selectedOption}
          </p>
          {selectedOption === "Domiciliar" && (
            <div className="mt-6 p-4 bg-yellow-50 border-l-4 border-yellow-400">
              <p className="text-yellow-700 text-sm">{domicilioObservations}</p>
            </div>
          )}

          {/* Exibição do Valor com Desconto */}
          <p className="text-gray-700 mt-4 text-xl">
            <strong>Valor:</strong> R${" "}
            {(price * (1 - discount / 100)).toFixed(2)}{" "}
            {discount > 0 && (
              <span className="text-sm text-green-600">
                ({discount}% de desconto aplicado)
              </span>
            )}
          </p>

          {/* Campo de Cupom */}
          <div className="mt-6">
            <label className="block text-gray-700 text-xl mb-2">
              <strong>Cupom de Desconto:</strong>
            </label>
            <div className="flex">
              <input
                type="text"
                value={couponCode}
                onChange={(e) => setCouponCode(e.target.value)}
                placeholder="Digite o cupom"
                className="flex-1 p-1 border"
              />
              <button
                onClick={applyCoupon}
                className="px-4  bg-blue-800 text-white hover:bg-blue-900"
              >
                Aplicar
              </button>
            </div>
          </div>

          {selectedOption === "Lava Rápido" && (
            <p className="text-gray-700 mt-4 text-xl">
              <strong>Endereço:</strong> {lavaRapidoAddress}
            </p>
          )}
        </div>

        <div className="mb-8">
          <h3 className="font-semibold mb-4 text-lg">
            Selecione a data e o horário
          </h3>

          <Calendar
            onDateClick={handleDateClick}
            selectedDate={selectedDate}
            dailyAvailableTimes={dailyAvailableTimes}
          />

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

        {selectedOption === "Domiciliar" && (
          <div className="mb-8">
            <h3 className="font-semibold mb-4 md:text-lg">Endereço</h3>

            <div className="flex flex-wrap gap-2">
              {addresses.map((address) => (
                <button
                  key={address.id}
                  className={`w-full p-4 border rounded-lg text-left transition duration-200 ease-in-out ${
                    selectedAddress === address.label
                      ? "bg-gray-200 border-blue-800 text-black"
                      : "bg-gray-50 hover:bg-gray-100"
                  }`}
                  onClick={() => setSelectedAddress(address.label)}
                >
                  <p>
                    <span className="font-semibold">{address.label} - </span>
                    {address.address}
                  </p>
                </button>
              ))}
              <button
                onClick={openModalAddress}
                className="w-full py-2 bg-blue-700 text-white rounded-lg flex justify-center items-center"
              >
                <FaPlus className="mr-2" /> Adicionar Novo Endereço
              </button>
            </div>
          </div>
        )}

        <ModalAddress
          isOpen={isModalAddressOpen}
          onClose={closeModalAddress}
          onSave={handleSaveAddress}
        />

        <button
          className="bg-[#1e3a8a] fixed bottom-0 left-0 w-full py-3 text-white text-lg md:static md:rounded-lg md:hover:bg-[#1c3a8a] md:transition-colors"
          onClick={handleNextStep}
        >
          Ir Para Pagamento
        </button>
      </div>

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
        <p className="text-gray-700 mb-2">
          <strong>Endereço:</strong>{" "}
          {selectedOption === "Lava Rápido"
            ? lavaRapidoAddress
            : selectedAddress}
        </p>
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
    </div>
  );
};

export default AppointmentSummary;

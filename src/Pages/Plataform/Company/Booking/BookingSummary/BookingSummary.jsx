import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { ChevronLeftIcon } from "@heroicons/react/24/outline";
import Modal from "react-modal";
import { FaEdit, FaTrash, FaPlus } from "react-icons/fa";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

Modal.setAppElement("#root");

const BookingSummary = () => {
  const location = useLocation();
  const { selectedService, selectedOption, price } = location.state || {};
  const [selectedDate, setSelectedDate] = useState(null);
  const [selectedTime, setSelectedTime] = useState("");
  const [selectedAddress, setSelectedAddress] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isAddressModalOpen, setIsAddressModalOpen] = useState(false);
  const [error, setError] = useState("");
  const [isObservationsDisabled, setIsObservationsDisabled] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
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
  const [editingAddress, setEditingAddress] = useState(null);
  const [form, setForm] = useState({
    label: "",
    address: "",
    zip: "",
    state: "",
    city: "",
  });

  const navigate = useNavigate();

  // Endereço fixo do lava-rápido
  const lavaRapidoAddress = "Rua do Lava Rápido, 123 - Centro";

  // Observações para atendimento domiciliar
  const domicilioObservations =
    "Para essa modalidade, o lava-rápido escolhido utiliza água do local. Certifique-se de que há disponibilidade de água e energia elétrica no endereço informado.";

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

  const isDayDisabled = (day) => {
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
    if (!selectedDate || !selectedTime) {
      setError("Por favor, selecione uma data e um horário.");
      return;
    }

    if (selectedOption === "Domiciliar" && !selectedAddress) {
      setError("Por favor, selecione ou adicione um endereço.");
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
      },
    });
  };

  const openAddressModal = (address = null) => {
    if (address) {
      setEditingAddress(address.id);
      setForm(address);
    } else {
      setEditingAddress(null);
      setForm({
        label: "",
        address: "",
        zip: "",
        state: "",
        city: "",
      });
    }
    setIsAddressModalOpen(true);
  };

  const closeAddressModal = () => {
    setIsAddressModalOpen(false);
  };

  const handleChange = (e) => {
    let { name, value } = e.target;

    if (name === "zip") {
      value = value.replace(/\D/g, "");
      if (value.length > 8) value = value.slice(0, 8);
      if (value.length > 5) value = value.replace(/^(\d{5})(\d{0,3})/, "$1-$2");
    }

    setForm({ ...form, [name]: value });
  };

  const handleSaveAddress = () => {
    if (editingAddress !== null) {
      setAddresses(
        addresses.map((addr) =>
          addr.id === editingAddress ? { ...form, id: editingAddress } : addr
        )
      );
      toast.success("Endereço atualizado com sucesso!", { autoClose: 2000 });
    } else {
      setAddresses([...addresses, { ...form, id: Date.now() }]);
      toast.success("Endereço adicionado com sucesso!", { autoClose: 2000 });
    }
    closeAddressModal();
  };

  const handleDeleteAddress = (id) => {
    setAddresses(addresses.filter((addr) => addr.id !== id));
    toast.success("Endereço excluído com sucesso!", { autoClose: 2000 });
  };

  const handleZipBlur = async () => {
    const cleanZip = form.zip.replace("-", "");
    if (cleanZip.length === 8) {
      setIsLoading(true);
      try {
        const response = await fetch(
          `https://brasilapi.com.br/api/cep/v1/${cleanZip}`
        );
        const data = await response.json();

        if (!data.errors) {
          setForm({
            ...form,
            address: data.street || "",
            city: data.city || "",
            state: data.state || "",
          });
        } else {
          toast.error("CEP não encontrado!", { autoClose: 2000 });
        }
      } catch (error) {
        console.error("Erro ao buscar CEP:", error);
        toast.error("Erro ao buscar CEP. Tente novamente.", { autoClose: 2000 });
      } finally {
        setIsLoading(false);
      }
    }
  };

  const monthDays = getMonthDays(currentMonth, currentYear);
  const weekDays = ["Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"];

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
          {selectedOption === "Domiciliar" && (
            <div className="mt-6 p-4 bg-yellow-50 border-l-4 border-yellow-400">
              <p className="text-yellow-700 text-sm">{domicilioObservations}</p>
            </div>
          )}

          <p className="text-gray-700 mt-4 text-xl">
            <strong>Valor:</strong> R${price.toFixed(2)}
          </p>
          {selectedOption === "Lava Rápido" && (
            <p className="text-gray-700 mt-4 text-xl">
              <strong>Endereço:</strong> {lavaRapidoAddress}
            </p>
          )}
        </div>

        {/* Seleção de Data e Horário */}
        <div className="mb-8">
          <h3 className="font-semibold mb-4 text-lg">
            Selecione a data e o horário
          </h3>
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
                isDayDisabled(day) ||
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

        {/* Seleção de Endereço (apenas para domicílio) */}
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
          
          <p><span className="font-semibold">{address.label} - </span>{address.address}</p>
        </button>
      ))}
      <button
        onClick={() => setIsAddressModalOpen(true)}
        className="w-full py-2 bg-blue-700 text-white rounded-lg flex justify-center items-center"
      >
        <FaPlus className="mr-2" /> Adicionar Novo Endereço
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

      {/* Modal de Adicionar/Editar Endereço */}
      <Modal
        isOpen={isAddressModalOpen}
        onRequestClose={closeAddressModal}
        className="bg-white p-6 rounded-lg shadow-lg lg:w-1/2 mx-auto my-8 w-screen"
        overlayClassName="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center"
      >
        <h3 className="text-lg font-medium mb-4">
          {editingAddress ? "Editar Endereço" : "Adicionar Novo Endereço"}
        </h3>

        <div className="lg:flex lg:gap-3">
          <div className="lg:flex-1">
            <label className="block">Nome do Endereço</label>
            <input
              type="text"
              placeholder="Ex: Casa, Trabalho, etc."
              name="label"
              value={form.label}
              onChange={handleChange}
              className="w-full p-2 border rounded mb-2"
            />
          </div>
          <div className="lg:w-1/3">
            <label className="block">CEP</label>
            <div className="flex items-start gap-3 lg:gap-1">
              <input
                type="text"
                name="zip"
                value={form.zip}
                onChange={handleChange}
                className="p-2 border rounded mb-2 flex-1"
                maxLength={9}
              />
              <button
                type="button"
                onClick={handleZipBlur}
                disabled={isLoading || form.zip.replace("-", "").length !== 8}
                className="p-2 w-32 bg-orange-600 text-white rounded cursor-pointer disabled:bg-orange-300 disabled:cursor-not-allowed"
              >
                {isLoading ? (
                  <div className="flex items-center justify-center">
                    <div className="animate-spin h-full w-4 border-2 border-white border-t-transparent rounded-full"></div>
                  </div>
                ) : (
                  "Buscar"
                )}
              </button>
            </div>
          </div>
        </div>

        <div className="lg:flex lg:gap-3">
          <div className="lg:w-1/2">
            <label className="block">Cidade</label>
            <input
              type="text"
              name="city"
              value={form.city}
              readOnly
              className="w-full p-2 border rounded mb-2 bg-gray-200"
            />
          </div>
          <div className="lg:w-1/2">
            <label className="block">Estado</label>
            <input
              type="text"
              name="state"
              value={form.state}
              readOnly
              className="w-full p-2 border rounded mb-2 bg-gray-200"
            />
          </div>
        </div>

        <div className="flex gap-3">
          <div className="flex-1">
            <label className="block">Rua</label>
            <input
              type="text"
              name="address"
              value={form.address}
              onChange={handleChange}
              className="w-full p-2 border rounded mb-2"
              placeholder="Ex: Rua das Flores"
            />
          </div>
          <div className="w-1/3">
            <label className="block">Número</label>
            <input
              type="text"
              name="number"
              onChange={handleChange}
              className="w-full p-2 border rounded mb-2"
              placeholder="Ex: 123"
            />
          </div>
        </div>

        <div className="mt-2">
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Instruções adicionais (Observações sobre o local)
          </label>

          {/* Checkbox Personalizado */}
          <div className="flex items-center mb-3">
            <button
              onClick={() => setIsObservationsDisabled(!isObservationsDisabled)}
              className={`w-5 h-5 flex items-center justify-center border-2 rounded transition-colors duration-200 ${
                isObservationsDisabled
                  ? "bg-orange-600 border-orange-600"
                  : "bg-white border-gray-300"
              }`}
            >
              {isObservationsDisabled && (
                <svg
                  className="w-4 h-4 text-white"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M5 13l4 4L19 7"
                  />
                </svg>
              )}
            </button>
            <label
              onClick={() => setIsObservationsDisabled(!isObservationsDisabled)}
              className="ml-2 text-sm text-gray-700 cursor-pointer"
            >
              Não tem instruções adicionais
            </label>
          </div>

          {/* Textarea com Feedback Visual */}
          <div className="relative">
            <textarea
              name="complement"
              onChange={handleChange}
              className={`w-full p-3 h-44 border rounded-lg transition-all duration-200 ${
                isObservationsDisabled
                  ? "bg-gray-100 text-gray-400 cursor-not-allowed"
                  : "bg-white text-gray-700"
              }`}
              placeholder={
                "Ex.: Número do apartamento, bloco/rua/número para casas em condomínios, responsável no local, ponto de referência, qualquer informação importante para a chegada ao local, realização do serviço no endereço ou retirada e devolução do veículo."
              }
              disabled={isObservationsDisabled}
              rows={5}
            />
          </div>
        </div>

        <div className="flex justify-start space-x-2 mt-4 mb-4">
          <button
            onClick={handleSaveAddress}
            className="px-4 py-2 bg-orange-600 text-white rounded"
          >
            Salvar Endereço
          </button>
          <button
            onClick={closeAddressModal}
            className="px-4 py-2 border rounded"
          >
            Cancelar
          </button>
        </div>
      </Modal>
    </div>
  );
};

export default BookingSummary;
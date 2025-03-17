// import { X } from "lucide-react";
// import React, { useState } from "react";

// const EditBooking = ({ onClose, onCancel, onEdit }) => {
//   const [service, setService] = useState("Lavagem completa");
//   const [selectedDate, setSelectedDate] = useState("");
//   const [selectedTime, setSelectedTime] = useState("");
//   const [value, setValue] = useState("50.00");
//   const [vehicleType, setVehicleType] = useState("SUV");
//   const [paymentMethod] = useState("Cartão de Crédito");
//   const [serviceType] = useState("A domicilio"); // Adicionei o tipo de serviço

//   const [isConfirmModalOpen, setIsConfirmModalOpen] = useState(false);

//   // Simulação de dias e horários disponíveis
//   const availableDates = [
//     { date: "2024-11-11", times: ["09:00", "11:00", "14:00"] },
//     { date: "2024-11-12", times: ["10:00", "13:00", "15:00"] },
//     { date: "2024-11-13", times: ["08:00", "12:00", "16:00"] },
//     { date: "2024-11-14", times: ["09:30", "11:30", "14:30"] },
//     { date: "2024-11-15", times: ["10:30", "13:30", "15:30"] },
//   ];

//   const handleSave = () => {
//     if (!selectedDate || !selectedTime) {
//       alert("Por favor, selecione uma data e um horário.");
//       return;
//     }

//     // Simula a ação de salvar
//     onEdit();
//     onClose(); // Fecha o modal imediatamente
//   };

//   const openConfirmModal = () => {
//     setIsConfirmModalOpen(true);
//   };

//   const closeConfirmModal = () => {
//     setIsConfirmModalOpen(false);
//   };

//   const confirmCancelBooking = () => {
//     // Simula a ação de cancelar
//     onCancel();
//     onClose(); // Fecha o modal de edição
//     closeConfirmModal(); // Fecha o modal de confirmação
//   };

//   // Filtra os horários disponíveis para a data selecionada
//   const availableTimes =
//     availableDates.find((d) => d.date === selectedDate)?.times || [];

//   return (
//     <div>
//       {/* Modal de Edição */}
//       <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
//         <div className="bg-white p-6 rounded-lg shadow-lg w-full lg:w-1/3">
//           <div className="flex justify-end">
//             <button onClick={onClose}>
//               <X color="gray" size={18} />
//             </button>
//           </div>
//           <h2 className="text-xl font-bold mb-4">Agendamento</h2>

//           <div className="space-y-4">
//             {/* Data e Horário */}
//             <div>
//               <label className="block text-sm font-medium text-gray-700">
//                 Data do Serviço
//               </label>
//               <select
//                 value={selectedDate}
//                 onChange={(e) => setSelectedDate(e.target.value)}
//                 className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
//               >
//                 <option value="">Selecione uma data</option>
//                 {availableDates.map((dateObj) => (
//                   <option key={dateObj.date} value={dateObj.date}>
//                     {new Date(dateObj.date).toLocaleDateString("pt-BR")}
//                   </option>
//                 ))}
//               </select>
//             </div>

//             <div>
//               <label className="block text-sm font-medium text-gray-700">
//                 Horário do Serviço
//               </label>
//               <select
//                 value={selectedTime}
//                 onChange={(e) => setSelectedTime(e.target.value)}
//                 className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
//                 disabled={!selectedDate} // Desabilita se nenhuma data for selecionada
//               >
//                 <option value="">Selecione um horário</option>
//                 {availableTimes.map((time) => (
//                   <option key={time} value={time}>
//                     {time}
//                   </option>
//                 ))}
//               </select>
//             </div>

//             {/* Informações do Serviço */}
//             <div>
//               <label className="block text-sm font-medium text-gray-700">
//                 Serviço
//               </label>
//               <input
//                 type="text"
//                 value={service}
//                 onChange={(e) => setService(e.target.value)}
//                 className="mt-1 block w-full p-2 border border-gray-300 rounded-md cursor-not-allowed bg-gray-100"
//                 readOnly
//               />
//             </div>

//             <div>
//               <label className="block text-sm font-medium text-gray-700">
//                 Tipo de Serviço
//               </label>
//               <input
//                 type="text"
//                 value={serviceType}
//                 className="mt-1 block w-full p-2 border border-gray-300 rounded-md cursor-not-allowed bg-gray-100"
//                 readOnly
//               />
//             </div>

//             <div>
//               <label className="block text-sm font-medium text-gray-700">
//                 Valor
//               </label>
//               <input
//                 type="number"
//                 value={value}
//                 onChange={(e) => setValue(e.target.value)}
//                 className="mt-1 block w-full p-2 border border-gray-300 rounded-md cursor-not-allowed bg-gray-100"
//                 readOnly
//               />
//             </div>

//             <div>
//               <label className="block text-sm font-medium text-gray-700">
//                 Tipo do Veículo
//               </label>
//               <input
//                 type="text"
//                 value={vehicleType}
//                 onChange={(e) => setVehicleType(e.target.value)}
//                 className="mt-1 block w-full p-2 border border-gray-300 rounded-md cursor-not-allowed bg-gray-100"
//                 readOnly
//               />
//             </div>

//             <div>
//               <label className="block text-sm font-medium text-gray-700">
//                 Forma de Pagamento
//               </label>
//               <input
//                 type="text"
//                 value={paymentMethod}
//                 readOnly
//                 className="mt-1 block w-full p-2 border border-gray-300 rounded-md cursor-not-allowed bg-gray-100"
//               />
//             </div>
//           </div>

//           {/* Botões de Ação */}
//           <div className="flex justify-start mt-6 space-x-2">
//             <button
//               onClick={handleSave}
//               className="px-4 py-2 bg-orange-600 text-white rounded hover:bg-orange-700 transition-colors duration-200"
//             >
//               Salvar
//             </button>
//             <button
//               onClick={openConfirmModal}
//               className="px-4 py-2 border rounded hover:bg-gray-100 transition-colors duration-200"
//             >
//               Cancelar Agendamento
//             </button>
//           </div>
//         </div>
//       </div>

//       {/* Modal de Confirmação de Cancelamento */}
//       {isConfirmModalOpen && (
//         <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
//           <div className="bg-white p-8 rounded-lg shadow-xl w-96 transform transition-all duration-300 ease-in-out scale-95 hover:scale-100">
//             {/* Ícone de alerta */}
//             <div className="flex justify-center mb-4">
//               <svg
//                 xmlns="http://www.w3.org/2000/svg"
//                 className="h-12 w-12 text-red-500"
//                 fill="none"
//                 viewBox="0 0 24 24"
//                 stroke="currentColor"
//               >
//                 <path
//                   strokeLinecap="round"
//                   strokeLinejoin="round"
//                   strokeWidth={2}
//                   d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
//                 />
//               </svg>
//             </div>

//             {/* Título */}
//             <h3 className="text-xl font-bold text-center text-gray-800 mb-6">
//               Tem certeza que deseja cancelar?
//             </h3>

//             {/* Descrição */}
//             <p className="text-sm text-gray-600 text-center mb-6">
//               Esta ação não pode ser desfeita. O agendamento será cancelado
//               permanentemente.
//             </p>

//             {/* Botões */}
//             <div className="flex justify-center space-x-4">
//               <button
//                 onClick={closeConfirmModal}
//                 className="px-6 py-2 bg-gray-100 hover:bg-gray-200 text-gray-700 font-semibold rounded-lg transition duration-200 ease-in-out transform hover:scale-105"
//               >
//                 Não
//               </button>
//               <button
//                 onClick={confirmCancelBooking}
//                 className="px-6 py-2 bg-red-500 hover:bg-red-600 text-white font-semibold rounded-lg transition duration-200 ease-in-out transform hover:scale-105"
//               >
//                 Sim, Cancelar
//               </button>
//             </div>
//           </div>
//         </div>
//       )}
//     </div>
//   );
// };

// export default EditBooking;

import React, { useState } from "react";
import PropTypes from "prop-types";
import { X } from "lucide-react";

const ConfirmCancelModal = ({ onClose, onConfirm }) => {
  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
      <div className="bg-white p-8 rounded-lg shadow-xl w-96 transform transition-all duration-300 ease-in-out scale-95 hover:scale-100">
        <div className="flex justify-center mb-4">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="h-12 w-12 text-red-500"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
            />
          </svg>
        </div>

        <h3 className="text-xl font-bold text-center text-gray-800 mb-6">
          Tem certeza que deseja cancelar?
        </h3>

        <p className="text-sm text-gray-600 text-center mb-6">
          Esta ação não pode ser desfeita. O agendamento será cancelado
          permanentemente.
        </p>

        <div className="flex justify-center space-x-4">
          <button
            onClick={onClose}
            className="px-6 py-2 bg-gray-100 hover:bg-gray-200 text-gray-700 font-semibold rounded-lg transition duration-200 ease-in-out transform hover:scale-105"
          >
            Não
          </button>
          <button
            onClick={onConfirm}
            className="px-6 py-2 bg-red-500 hover:bg-red-600 text-white font-semibold rounded-lg transition duration-200 ease-in-out transform hover:scale-105"
          >
            Sim, Cancelar
          </button>
        </div>
      </div>
    </div>
  );
};

ConfirmCancelModal.propTypes = {
  onClose: PropTypes.func.isRequired,
  onConfirm: PropTypes.func.isRequired,
};

const EditBooking = ({ onClose, onCancel, onEdit }) => {
  const [selectedDate, setSelectedDate] = useState("");
  const [selectedTime, setSelectedTime] = useState("");
  const [isConfirmModalOpen, setIsConfirmModalOpen] = useState(false);

  const availableDates = [
    { date: "2024-11-11", times: ["09:00", "11:00", "14:00"] },
    { date: "2024-11-12", times: ["10:00", "13:00", "15:00"] },
    { date: "2024-11-13", times: ["08:00", "12:00", "16:00"] },
    { date: "2024-11-14", times: ["09:30", "11:30", "14:30"] },
    { date: "2024-11-15", times: ["10:30", "13:30", "15:30"] },
  ];

  const handleSave = () => {
    if (!selectedDate || !selectedTime) {
      alert("Por favor, selecione uma data e um horário.");
      return;
    }

    onEdit();
    onClose();
  };

  const openConfirmModal = () => setIsConfirmModalOpen(true);
  const closeConfirmModal = () => setIsConfirmModalOpen(false);

  const confirmCancelBooking = () => {
    onCancel();
    onClose();
    closeConfirmModal();
  };

  const availableTimes =
    availableDates.find((d) => d.date === selectedDate)?.times || [];

  return (
    <div>
      <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
        <div className="bg-white p-6 rounded-lg shadow-lg w-full lg:w-1/3">
          <div className="flex justify-end">
            <button onClick={onClose}>
              <X color="gray" size={18} />
            </button>
          </div>
          <h2 className="text-xl font-bold mb-4">Agendamento</h2>

          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700">
                Data do Serviço
              </label>
              <select
                value={selectedDate}
                onChange={(e) => setSelectedDate(e.target.value)}
                className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
              >
                <option value="">Selecione uma data</option>
                {availableDates.map((dateObj) => (
                  <option key={dateObj.date} value={dateObj.date}>
                    {new Date(dateObj.date).toLocaleDateString("pt-BR")}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">
                Horário do Serviço
              </label>
              <select
                value={selectedTime}
                onChange={(e) => setSelectedTime(e.target.value)}
                className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
                disabled={!selectedDate}
              >
                <option value="">Selecione um horário</option>
                {availableTimes.map((time) => (
                  <option key={time} value={time}>
                    {time}
                  </option>
                ))}
              </select>
            </div>

            {/* Outros campos do formulário */}
          </div>

          <div className="flex justify-start mt-6 space-x-2">
            <button
              onClick={handleSave}
              className="px-4 py-2 bg-orange-600 text-white rounded hover:bg-orange-700 transition-colors duration-200"
            >
              Salvar
            </button>
            <button
              onClick={openConfirmModal}
              className="px-4 py-2 border rounded hover:bg-gray-100 transition-colors duration-200"
            >
              Cancelar Agendamento
            </button>
          </div>
        </div>
      </div>

      {isConfirmModalOpen && (
        <ConfirmCancelModal
          onClose={closeConfirmModal}
          onConfirm={confirmCancelBooking}
        />
      )}
    </div>
  );
};

EditBooking.propTypes = {
  onClose: PropTypes.func.isRequired,
  onCancel: PropTypes.func.isRequired,
  onEdit: PropTypes.func.isRequired,
};

export default React.memo(EditBooking);

import { X } from "lucide-react";
import React, { useState } from "react";

const EditBooking = ({ onClose, onCancel, onEdit }) => {

  const [service, setService] = useState("Lavagem completa");
  const [scheduledDate, setScheduledDate] = useState("2024-11-11T11:00");
  const [value, setValue] = useState("50.00");
  const [vehicle, setVehicle] = useState("Ford Ka");
  const [paymentMethod] = useState("Cartão de Crédito");
  
  const [isConfirmModalOpen, setIsConfirmModalOpen] = useState(false);

  const handleSave = () => {
    onEdit();
    onClose();
  };

  const openConfirmModal = () => {
    setIsConfirmModalOpen(true);
  };

  const closeConfirmModal = () => {
    setIsConfirmModalOpen(false);
  };

  const confirmCancelBooking = () => {
    onCancel(); 
    onClose(); 
    closeConfirmModal(); 
    
  };

  return (
    <div>
      <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
        <div className="bg-white p-6 rounded-lg shadow-lg w-96">
          <div className="flex justify-end">
            <button onClick={onClose}>
              <X color="gray" size={18} />
            </button>
          </div>
          <h2 className="text-xl font-bold mb-4">Editar Agendamento</h2>

          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700">Serviço</label>
              <input
                type="text"
                value={service}
                onChange={(e) => setService(e.target.value)}
                className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Data do Serviço</label>
              <input
                type="datetime-local"
                value={scheduledDate}
                onChange={(e) => setScheduledDate(e.target.value)}
                className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Valor</label>
              <input
                type="number"
                value={value}
                onChange={(e) => setValue(e.target.value)}
                className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Veículo</label>
              <input
                type="text"
                value={vehicle}
                onChange={(e) => setVehicle(e.target.value)}
                className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Forma de Pagamento</label>
              <input
                type="text"
                value={paymentMethod}
                readOnly
                className="mt-1 block w-full p-2 border border-gray-300 rounded-md cursor-not-allowed opacity-40 bg-gray-300"
              />
            </div>
          </div>

          <div className="flex justify-start mt-6 space-x-2">
            <button
              onClick={handleSave}
              className="bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
            >
              Salvar
            </button>
            <button
              onClick={openConfirmModal}
              className="bg-red-600 hover:bg-red-700 text-white font-bold py-2 px-4 rounded"
            >
              Cancelar Agendamento
            </button>
          </div>
        </div>
      </div>


      {isConfirmModalOpen && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
          <div className="bg-white p-6 rounded-lg shadow-lg w-96">
            <h3 className="text-lg font-bold mb-4">Tem certeza que deseja cancelar?</h3>
            <div className="flex justify-end space-x-4">
              <button
                onClick={closeConfirmModal}
                className="bg-gray-500 hover:bg-gray-600 text-white font-bold py-2 px-4 rounded"
              >
                Não
              </button>
              <button
                onClick={confirmCancelBooking}
                className="bg-red-600 hover:bg-red-700 text-white font-bold py-2 px-4 rounded"
              >
                Sim, Cancelar
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default EditBooking;

import React, { useState } from "react";
import { FaEdit, FaTrash, FaPlus, FaArrowLeft } from "react-icons/fa";
import { Link } from "react-router-dom";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import ModalAddress from "../../components/ModalAddress"; // Componente unificado

const AddressEdit = () => {
  const [isDisabled, setIsDisabled] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [addresses, setAddresses] = useState([
    {
      id: 1,
      label: "Casa",
      address: "Rua das Flores, 123",
      zip: "12345-678",
      state: "Paraná",
      city: "Londrina",
    },
  ]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingAddress, setEditingAddress] = useState(null); 

  const openAddModal = () => {
    setEditingAddress(null); 
    setIsModalOpen(true);
  };

  const openEditModal = (address) => {
    setEditingAddress(address); 
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

  const handleSave = (formData) => {
    if (!formData.label || !formData.address || !formData.zip || !formData.city || !formData.state) {
      toast.error("Preencha todos os campos obrigatórios.", { autoClose: 2000 });
      return;
    }

    if (editingAddress !== null) {
      setAddresses(
        addresses.map((addr) =>
          addr.id === editingAddress.id ? { ...formData, id: editingAddress.id } : addr
        )
      );
      toast.success("Endereço atualizado com sucesso!", { autoClose: 2000 });
    } else {
      setAddresses([...addresses, { ...formData, id: Date.now() }]);
      toast.success("Endereço adicionado com sucesso!", { autoClose: 2000 });
    }
    closeModal();
  };

  const handleDelete = (id) => {
    setAddresses(addresses.filter((addr) => addr.id !== id));
    toast.success("Endereço excluído com sucesso!", { autoClose: 2000 });
  };

  return (
    <div className="flex flex-col p-6 lg:w-1/2 lg:mx-auto">
      <ToastContainer />
      <div className="flex items-center justify-between mb-6">
        <Link to="/app/minha-conta" className="text-gray-500">
          <FaArrowLeft className="text-xl" />
        </Link>
        <h2 className="flex-1 text-center text-xl font-medium text-gray-700">
          Endereços Cadastrados
        </h2>
      </div>

      {addresses.map((address) => (
        <div key={address.id} className="mb-4 mt-4 p-4 border rounded-lg">
          <div className="flex justify-between">
            <span className="font-semibold">{address.label}</span>
            <div className="flex space-x-2">
              <button
                onClick={() => openEditModal(address)}
                className="text-blue-500"
              >
                <FaEdit />
              </button>
              <button
                onClick={() => handleDelete(address.id)}
                className="text-red-500"
              >
                <FaTrash />
              </button>
            </div>
          </div>
          <p>{address.address}</p>
        </div>
      ))}

      <button
        onClick={openAddModal}
        className="w-full py-3 bg-orange-600 text-white rounded-lg flex justify-center items-center"
      >
        <FaPlus className="mr-2" /> Adicionar Novo Endereço
      </button>

      <ModalAddress
        isOpen={isModalOpen}
        onClose={closeModal}
        onSave={handleSave}
        initialData={editingAddress} 
      />
    </div>
  );
};

export default AddressEdit;
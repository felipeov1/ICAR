import React, { useState } from "react";
import { FaEdit, FaTrash, FaPlus, FaArrowLeft } from "react-icons/fa";
import { Link } from "react-router-dom";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import AddAddressModal from "../../components/ModalAddress"; 
import EditAddressModal from "../../components/EditAddressModal"; 

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
  const [addModalIsOpen, setAddModalIsOpen] = useState(false); // Estado para o modal de adicionar
  const [editModalIsOpen, setEditModalIsOpen] = useState(false); // Estado para o modal de editar
  const [editingAddress, setEditingAddress] = useState(null);
  const [form, setForm] = useState({
    label: "",
    address: "",
    zip: "",
    state: "",
    city: "",
  });

  const openAddModal = () => {
    setForm({
      label: "",
      address: "",
      zip: "",
      state: "",
      city: "",
    });
    setAddModalIsOpen(true);
  };

  const openEditModal = (address) => {
    setEditingAddress(address.id);
    setForm(address);
    setEditModalIsOpen(true);
  };

  const closeModal = () => {
    setAddModalIsOpen(false);
    setEditModalIsOpen(false);
  };

  const handleChange = (e) => {
    let { name, value } = e.target;

    if (name === "zip") {
      value = value.replace(/\D/g, ""); // Remove tudo que não for número
      if (value.length > 8) value = value.slice(0, 8); // Limita a 8 dígitos
      if (value.length > 5) value = value.replace(/^(\d{5})(\d{0,3})/, "$1-$2"); // Adiciona traço
    }

    setForm({ ...form, [name]: value });
  };

  const handleSave = () => {
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
    closeModal();
  };

  const handleDelete = (id) => {
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
            neighborhood: data.neighborhood || "",
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

      {/* Modal de Adicionar */}
      <AddAddressModal
        isOpen={addModalIsOpen}
        onClose={closeModal}
        onSave={handleSave}
        form={form}
        handleChange={handleChange}
        handleZipBlur={handleZipBlur}
        isLoading={isLoading}
        isDisabled={isDisabled}
        setIsDisabled={setIsDisabled}
      />

      {/* Modal de Editar */}
      <EditAddressModal
        isOpen={editModalIsOpen}
        onClose={closeModal}
        onSave={handleSave}
        form={form}
        handleChange={handleChange}
        handleZipBlur={handleZipBlur}
        isLoading={isLoading}
        isDisabled={isDisabled}
        setIsDisabled={setIsDisabled}
      />
    </div>
  );
};

export default AddressEdit;
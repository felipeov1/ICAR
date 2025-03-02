import React, { useState } from "react";
import Modal from "react-modal";
import { FaEdit, FaTrash, FaPlus, FaArrowLeft } from "react-icons/fa";
import { Link } from "react-router-dom";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

Modal.setAppElement("#root");

const AddressEdit = () => {
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
  const [modalIsOpen, setModalIsOpen] = useState(false);
  const [editingAddress, setEditingAddress] = useState(null);
  const [form, setForm] = useState({
    label: "",
    address: "",
    zip: "",
    state: "",
    city: "",
  });

  const openModal = (address = null) => {
    if (address) {
      setEditingAddress(address.id);
      setForm(address);
    } else {
      setEditingAddress(null);
      setForm({
        label: "",
        phone: "",
        address: "",
        zip: "",
        state: "",
        city: "",
      });
    }
    setModalIsOpen(true);
  };

  const closeModal = () => {
    setModalIsOpen(false);
  };

  const handleChange = (e) => {
    let { name, value } = e.target;

    if (name === "zip") {
      value = value.replace(/\D/g, ""); // Remove tudo que não for número
      if (value.length > 5) {
        value = value.replace(/^(\d{5})(\d{0,3})/, "$1-$2"); // Adiciona o traço após os 5 primeiros dígitos
      }
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
      toast.success("Endereço atualizado com sucesso!", {
        autoClose: 2000,
      });
    } else {
      setAddresses([...addresses, { ...form, id: Date.now() }]);
      toast.success("Endereço adicionado com sucesso!", {
        autoClose: 2000,
      });
    }
    closeModal();
  };

  const handleDelete = (id) => {
    setAddresses(addresses.filter((addr) => addr.id !== id));
    toast.success("Endereço excluído com sucesso!", {
      autoClose: 2000,
    });
  };

  const handleZipBlur = async () => {
    let cleanZip = form.zip.replace("-", "");

    if (cleanZip.length === 8) {
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
          toast.success("CEP encontrado com sucesso!", {
            autoClose: 2000,
          });
        } else {
          toast.error("CEP não encontrado!", {
            autoClose: 2000,
          });
        }
      } catch (error) {
        console.error("Erro ao buscar CEP:", error);
        toast.error("Erro ao buscar CEP. Tente novamente.", {
          autoClose: 2000,
        });
      }
    }
  };

  return (
    <div className="p-6">
      {/* ToastContainer para exibir as notificações */}
      <ToastContainer />

      <div className="flex align-center justify-stretch">
        <span className="mb-6">
          <Link
            to="/icar/minha-conta"
            className="flex items-center text-gray-500"
          >
            <FaArrowLeft className="text-xl" />
          </Link>
        </span>

        <h2 className="text-xl font-medium mb-4 text-gray-700">
          Endereços Cadastrados
        </h2>
      </div>

      {addresses.map((address) => (
        <div key={address.id} className="mb-4 p-4 border rounded-lg">
          <div className="flex justify-between">
            <span className="font-semibold">{address.label}</span>
            <div className="flex space-x-2">
              <button
                onClick={() => openModal(address)}
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
          <p>{address.phone}</p>
          <p>{address.address}</p>
        </div>
      ))}

      <button
        onClick={() => openModal()}
        className="w-full py-3 bg-orange-600 text-white rounded-lg flex justify-center items-center"
      >
        <FaPlus className="mr-2" /> Adicionar Novo Endereço
      </button>

      <Modal
        isOpen={modalIsOpen}
        onRequestClose={closeModal}
        className="bg-white p-6 mt-28 mx-auto rounded-lg shadow-lg"
        overlayClassName="fixed inset-0 bg-black bg-opacity-50"
      >
        <h3 className="text-lg font-medium mb-4">
          {editingAddress ? "Editar Endereço" : "Adicionar Novo Endereço"}
        </h3>

        <label className="block">Nome do Endereço</label>
        <input
          type="text"
          placeholder="Ex: Casa, Trabalho, etc."
          name="label"
          value={form.label}
          onChange={handleChange}
          className="w-full p-2 border rounded mb-2"
        />

        <label className="block">CEP</label>
        <div className="flex items-start gap-3">
          <input
            type="text"
            name="zip"
            value={form.zip}
            onChange={handleChange}
            className="p-2 border rounded mb-2 flex-1"
          />
          <input
            type="button"
            value="Buscar"
            onClick={handleZipBlur}
            className="p-2 w-32 bg-orange-600 text-white rounded cursor-pointer"
          />
        </div>

        <label className="block">Cidade</label>
        <input
          type="text"
          name="city"
          value={form.city}
          readOnly
          className="w-full p-2 border rounded mb-2 bg-gray-200"
        />

        <label className="block">Estado</label>
        <input
          type="text"
          name="state"
          value={form.state}
          readOnly
          className="w-full p-2 border rounded mb-2 bg-gray-200"
        />

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

        <label className="block">Complemento</label>
        <input
          type="text"
          name="complement"
          onChange={handleChange}
          className="w-full p-2 border rounded mb-2"
          placeholder="Ex: Casa, Apto, Bloco, etc."
        />

        <div className="flex justify-start space-x-2 mt-4">
          <button
            onClick={handleSave}
            className="px-4 py-2 bg-orange-600 text-white rounded"
          >
            Salvar Endereço
          </button>
          <button onClick={closeModal} className="px-4 py-2 border rounded">
            Cancelar
          </button>
        </div>
      </Modal>
    </div>
  );
};

export default AddressEdit;

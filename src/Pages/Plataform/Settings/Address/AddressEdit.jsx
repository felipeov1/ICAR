import React, { useState } from "react";
import Modal from "react-modal";
import { FaEdit, FaTrash, FaPlus, FaArrowLeft } from "react-icons/fa";
import { Link } from "react-router-dom";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

Modal.setAppElement("#root");

const AddressEdit = () => {
  const [isDisabled, setIsDisabled] = useState(false);
  const [isLoading, setIsLoading] = useState(false); // Estado para controlar o loading
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
      // Remove tudo que não for número
      value = value.replace(/\D/g, "");

      // Limita o CEP a 8 dígitos
      if (value.length > 8) {
        value = value.slice(0, 8);
      }

      // Adiciona o traço após os 5 primeiros dígitos
      if (value.length > 5) {
        value = value.replace(/^(\d{5})(\d{0,3})/, "$1-$2");
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
      setIsLoading(true); // Ativa o loading
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
          toast.error("CEP não encontrado!", {
            autoClose: 2000,
          });
        }
        // 86071-750
      } catch (error) {
        console.error("Erro ao buscar CEP:", error);
        toast.error("Erro ao buscar CEP. Tente novamente.", {
          autoClose: 2000,
        });
      } finally {
        setIsLoading(false); // Desativa o loading
      }
    }
  };

  return (
    <div className="flex flex-col p-6 lg:w-1/2 lg:mx-auto">
      {/* ToastContainer para exibir as notificações */}
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
          <div className=" lg:w-1/2 ">
            <label className="block">Cidade</label>
            <input
              type="text"
              name="city"
              value={form.city}
              readOnly
              className="w-full p-2 border rounded mb-2 bg-gray-200"
            />
          </div>
          <div className=" lg:w-1/2 lg:gap-3">
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
              onClick={() => setIsDisabled(!isDisabled)}
              className={`w-5 h-5 flex items-center justify-center border-2 rounded transition-colors duration-200 ${
                isDisabled
                  ? "bg-orange-600 border-orange-600"
                  : "bg-white border-gray-300"
              }`}
            >
              {isDisabled && (
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
              onClick={() => setIsDisabled(!isDisabled)}
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
                isDisabled
                  ? "bg-gray-100 text-gray-400 cursor-not-allowed"
                  : "bg-white text-gray-700"
              }`}
              placeholder={
                "Ex.: Número do apartamento, bloco/rua/número para casas em condomínios, responsável no local, ponto de referência, qualquer informação importante para a chegada ao local, realização do serviço no endereço ou retirada e devolução do veículo."
              }
              disabled={isDisabled}
              rows={5}
            />
          </div>
        </div>

        <div className="flex justify-start space-x-2 mt-4 mb-4">
          <button
            onClick={handleSave}
            className="px-4 py- bg-orange-600 text-white rounded"
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

import React, { useState, useEffect } from "react";
import Modal from "react-modal";
import { FaTimes } from "react-icons/fa";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

Modal.setAppElement("#root");

const ModalAddress = ({
  isOpen,
  onClose,
  onSave,
  initialData, // Dados iniciais para edição
}) => {
  const [form, setForm] = useState({
    label: "",
    address: "",
    zip: "",
    state: "",
    city: "",
    number: "",
    complement: "",
  });

  const [isLoading, setIsLoading] = useState(false);
  const [isDisabled, setIsDisabled] = useState(false);

  // Preenche o formulário com os dados iniciais quando o modal é aberto
  useEffect(() => {
    if (initialData) {
      setForm(initialData);
    } else {
      setForm({
        label: "",
        address: "",
        zip: "",
        state: "",
        city: "",
        number: "",
        complement: "",
      });
    }
  }, [initialData]);

  // Função para salvar o endereço
  const handleSaveAddress = () => {
    // Validação dos campos obrigatórios
    if (!form.zip || !form.address || !form.city || !form.state || !form.number) {
      toast.error("Preencha todos os campos obrigatórios.", { autoClose: 2000 });
      return;
    }

    onSave(form); // Passa os dados do formulário para a função onSave
    onClose(); // Fecha o modal
  };

  // Função para buscar o endereço pelo CEP
  const handleZipBlur = async () => {
    const cleanZip = form.zip.replace(/\D/g, ""); // Remove tudo que não for dígito
    if (cleanZip.length !== 8) {
      toast.error("CEP deve conter 8 dígitos.", { autoClose: 2000 });
      return;
    }

    setIsLoading(true);
    try {
      const response = await fetch(
        `https://brasilapi.com.br/api/cep/v1/${cleanZip}`
      );

      if (!response.ok) {
        throw new Error("Erro ao buscar CEP.");
      }

      const data = await response.json();

      if (data.erro) {
        toast.error("CEP não encontrado!", { autoClose: 2000 });
      } else {
        setForm({
          ...form,
          address: data.street || "",
          neighborhood: data.neighborhood || "",
          city: data.city || "",
          state: data.state || "",
        });
      }
    } catch (error) {
      console.error("Erro ao buscar CEP:", error);
      toast.error("Erro ao buscar CEP. Tente novamente.", { autoClose: 2000 });
    } finally {
      setIsLoading(false);
    }
  };

  // Função para formatar o CEP (adicionar traço)
  const handleChange = (e) => {
    const { name, value } = e.target;

    if (name === "zip") {
      // Formata o CEP (12345-678)
      const cleanValue = value.replace(/\D/g, ""); // Remove tudo que não for número
      const formattedValue = cleanValue.replace(/^(\d{5})(\d{0,3})/, "$1-$2"); // Adiciona traço
      setForm({ ...form, [name]: formattedValue });
    } else {
      setForm({ ...form, [name]: value });
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onClose}
      className="bg-white p-4 mx-2 rounded-lg shadow-lg overflow-y-auto md:max-w-lg md:p-8"
      style={{
        content: {
          maxHeight: "90vh",
          margin: "auto",
          position: "relative",
        },
        overlay: {
          backgroundColor: "rgba(0, 0, 0, 0.5)",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
        },
      }}
    >
      <button
        onClick={onClose}
        className="absolute top-2 right-2 p-2 text-gray-600 hover:text-gray-900"
      >
        <FaTimes className="w-6 h-6" />
      </button>

      <div className="flex flex-col">
        <ToastContainer />

        <h3 className="text-lg font-medium mb-4">
          {initialData ? "Editar Endereço" : "Adicionar Novo Endereço"}
        </h3>

        <div className="flex flex-col gap-3">
          <div className="w-full">
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
          <div className="w-full">
            <label className="block">CEP</label>
            <div className="flex items-start gap-3">
              <input
                type="text"
                name="zip"
                value={form.zip}
                onChange={handleChange}
                onBlur={handleZipBlur}
                className="p-2 border rounded mb-2 flex-1"
                maxLength={9}
              />
              <button
                type="button"
                onClick={handleZipBlur}
                disabled={isLoading || form.zip.replace(/\D/g, "").length !== 8}
                className="p-2 w-32 bg-orange-600 text-white rounded cursor-pointer disabled:bg-orange-300 disabled:cursor-not-allowed"
              >
                {isLoading ? (
                  <div className="flex items-center justify-center">
                    <div className="animate-spin h-5 w-5 border-2 border-white border-t-transparent rounded-full"></div>
                  </div>
                ) : (
                  "Buscar"
                )}
              </button>
            </div>
          </div>
        </div>

        <div className="flex flex-col gap-3 md:flex-row">
          <div className="w-full md:w-1/2">
            <label className="block">Cidade</label>
            <input
              type="text"
              name="city"
              value={form.city}
              onChange={handleChange}
              className="w-full p-2 border rounded mb-2"
              placeholder="Ex: São Paulo"
            />
          </div>
          <div className="w-full md:w-1/2">
            <label className="block">Estado</label>
            <input
              type="text"
              name="state"
              value={form.state}
              onChange={handleChange}
              className="w-full p-2 border rounded mb-2"
              placeholder="Ex: SP"
            />
          </div>
        </div>

        <div className="flex flex-col gap-3 md:flex-row">
          <div className="w-full md:flex-1">
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
          <div className="w-full md:w-1/3">
            <label className="block">Número</label>
            <input
              type="text"
              name="number"
              onChange={handleChange}
              className="w-full p-2 border rounded mb-2"
              placeholder="Ex: 123"
              required
            />
          </div>
        </div>

        <div className="mt-2">
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Instruções adicionais (Observações sobre o local)
          </label>

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

          <div className="relative">
            <textarea
              name="complement"
              onChange={handleChange}
              className={`w-full p-3 h-32 border rounded-lg transition-all duration-200 ${
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

        <div className="flex justify-start space-x-2 mt-4 mb-16">
          <button
            onClick={handleSaveAddress}
            className="px-4 py-2 bg-orange-600 text-white rounded"
          >
            Salvar Endereço
          </button>
          <button onClick={onClose} className="px-4 py-2 border rounded">
            Cancelar
          </button>
        </div>
      </div>
    </Modal>
  );
};

export default ModalAddress;
import React, { useState } from "react";
import { FaStar } from "react-icons/fa";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const PastBookingCard = ({ service, onCancel, onRate }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [rating, setRating] = useState(0); // Estado para armazenar a avaliação (1 a 5)
  const [comment, setComment] = useState(""); // Estado para armazenar o comentário
  const [avaliacoes, setAvaliacoes] = useState([]); // Estado para armazenar todas as avaliações

  const openModal = () => {
    // Verifica se já existe uma avaliação para este serviço
    const avaliacaoExistente = avaliacoes.find(
      (av) => av.serviceId === service.id
    );
    if (avaliacaoExistente) {
      setRating(avaliacaoExistente.rating);
      setComment(avaliacaoExistente.comment);
    } else {
      setRating(0);
      setComment("");
    }
    setIsModalOpen(true);
  };

  const closeModal = () => setIsModalOpen(false);

  const handleRate = () => {
    if (rating > 0) {
      // Cria o objeto de avaliação
      const novaAvaliacao = {
        serviceId: service.id,
        rating,
        comment,
      };

      // Adiciona a avaliação ao array de avaliações
      setAvaliacoes((prev) => {
        const outrasAvaliacoes = prev.filter(
          (av) => av.serviceId !== service.id
        );
        return [...outrasAvaliacoes, novaAvaliacao];
      });

      console.log("Avaliação salva:", novaAvaliacao); // Simula o envio para o backend

      // Fecha o modal e exibe a notificação de sucesso
      closeModal();
      toast.success("Avaliação enviada com sucesso!", {
        autoClose: 2000, // Fecha automaticamente após 2 segundos
      });
    } else {
      toast.error("Por favor, selecione uma nota antes de enviar.");
    }
  };

  // Verifica se já existe uma avaliação para este serviço
  const avaliacaoExistente = avaliacoes.find(
    (av) => av.serviceId === service.id
  );

  return (
    <>
      {/* Card do Agendamento */}
      <section className="w-full flex flex-col sm:flex-row justify-between items-start sm:items-center p-4 bg-white shadow-lg border rounded-md mt-4">
        <div className="flex-1">
          <p>
            <strong>Serviço:</strong> {service.service}
          </p>
          <p className="mt-2">
            <strong>Data do Serviço:</strong> {service.scheduledDate}
          </p>
          <p className="mt-2">
            <strong>Valor:</strong> {service.value}
          </p>
          <p className="mt-2">
            <strong>Tipo do Veículo:</strong> {service.vehicle}
          </p>
        </div>

        {/* Botão de Avaliação */}
        <div className="mt-4 sm:mt-0 sm:ml-4">
          <button
            onClick={openModal}
            className="bg-blue-500 hover:bg-blue-700 text-white font-bold text-sm sm:text-md py-2 px-2 rounded whitespace-nowrap"
          >
            {avaliacaoExistente ? "Ver Avaliação" : "Avaliar Serviço"}
          </button>
        </div>
      </section>

      {/* Modal de Avaliação */}
      {isModalOpen && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
          <div className="bg-white p-6 rounded-lg shadow-lg w-11/12 sm:w-96">
            <h2 className="text-xl font-bold mb-4 flex items-center">
              {avaliacaoExistente ? "Sua Avaliação" : "Avaliar Serviço"}
            </h2>

            {/* Avaliação em Estrelas */}
            <div className="mb-4">
              <label className="block text-gray-700 mb-2">Nota:</label>
              <div className="flex space-x-2">
                {[...Array(5)].map((_, index) => (
                  <button
                    key={index}
                    onClick={() => !avaliacaoExistente && setRating(index + 1)}
                    className={`text-2xl ${
                      index + 1 <= rating ? "text-yellow-500" : "text-gray-300"
                    } ${
                      avaliacaoExistente
                        ? "cursor-not-allowed"
                        : "cursor-pointer"
                    }`}
                  >
                    <FaStar />
                  </button>
                ))}
              </div>
            </div>

            {/* Comentário */}
            <div className="mb-4">
              <label className="block text-gray-700 mb-2">Comentário:</label>
              <textarea
                value={comment}
                onChange={(e) =>
                  !avaliacaoExistente && setComment(e.target.value)
                }
                className="w-full p-2 border border-gray-300 rounded-lg"
                rows="3"
                readOnly={avaliacaoExistente}
              />
            </div>

            {/* Botões do Modal */}
            <div className="flex justify-end">
              <button
                onClick={closeModal}
                className="bg-gray-500 text-white px-4 py-2 rounded-lg hover:bg-gray-600 transition duration-300 mr-2"
              >
                Fechar
              </button>

              {/* Se não houver uma avaliação, exibe o botão "Enviar Avaliação" */}
              {!avaliacaoExistente && (
                <button
                  onClick={handleRate}
                  className="bg-green-500 text-white px-4 py-2 rounded-lg hover:bg-green-600 transition duration-300"
                >
                  Enviar Avaliação
                </button>
              )}
            </div>
          </div>
        </div>
      )}

      {/* ToastContainer para exibir as notificações */}
      <ToastContainer />
    </>
  );
};

export default PastBookingCard;
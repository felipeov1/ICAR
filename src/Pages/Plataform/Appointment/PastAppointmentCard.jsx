import React, { useState } from "react";
import PropTypes from "prop-types";
import { FaStar, FaRedo, FaCommentDots, FaHeadset } from "react-icons/fa";
import { toast } from "react-toastify";

const PastBookingCard = ({ service, onCancel, onRate }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [rating, setRating] = useState(0);
  const [comment, setComment] = useState("");

  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);

  const handleRate = () => {
    if (rating > 0) {
      onRate(service.id, rating, comment);
      closeModal();
    } else {
      toast.error("Por favor, selecione uma nota antes de enviar.");
    }
  };

  const handleContactSupport = () => {
    // Lógica para falar com o suporte
    toast.info("Redirecionando para o suporte...");
  };

  return (
    <>
      <section className="w-full flex flex-col md:flex-row justify-between p-6 bg-white shadow-lg border rounded-md mt-4">
        <div className="flex-1">
          <h2 className="text-xl font-bold text-blue-800">
            {service.service_name}
          </h2>
          <div className="mt-4 space-y-2">
            <div>
              <span className="text-gray-600">Empresa: </span>
              <span className="font-medium">{service.company}</span>
            </div>
            <div>
              <span className="text-gray-600">Modalidade: </span>
              <span className="font-medium">{service.modality}</span>
            </div>
            <div>
              <span className="text-gray-600 ">Tipo do Veículo: </span>
              <span className="font-medium">{service.vehicle_type}</span>
            </div>
            <div>
              <span className="text-gray-600 ">Local: </span>
              <span className="font-medium">{service.vehicle_type}</span>
            </div>
            <div>
              <span className="text-gray-600 ">Data do Serviço: </span>
              <span className="font-medium">{service.date_time}</span>
            </div>
            <div>
              <span className="text-gray-600 ">Valor: </span>
              <span className="font-medium">R${service.amount_paid}</span>
            </div>
            <div>
              <span className="text-gray-600 ">Método de Pagamento: </span>
              <span className="font-medium">{service.payment_method}</span>
            </div>
            <div>
              <span className="text-gray-600 ">Situação: </span>
              <span className="font-medium">{service.status}</span>
            </div>
          </div>
        </div>

        <div className="mt-4 md:mt-0 md:ml-4 flex flex-col gap-2">
          {service.status === "Cancelado" ? (
            <button
              onClick={handleContactSupport}
              className="bg-red-500 hover:bg-red-700 text-white font-bold text-sm sm:text-md py-2 px-4 rounded flex items-center justify-center gap-2 transition duration-300"
            >
              <FaHeadset />
              Falar com Suporte
            </button>
          ) : (
            <>
              <button
                onClick={openModal}
                disabled={service.rated} // Desabilita se já foi avaliado
                className="bg-blue-500 hover:bg-blue-700 text-white font-bold text-sm sm:text-md py-2 px-4 rounded flex items-center justify-center gap-2 transition duration-300 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                <FaCommentDots />
                Avaliar Serviço
              </button>

              <button
                // onClick={handleRefazerServico} 
                className="bg-green-500 hover:bg-green-700 text-white font-bold text-sm sm:text-md py-2 px-4 rounded flex items-center justify-center gap-2 transition duration-300"
              >
                <FaRedo />
                Refazer Serviço
              </button>
            </>
          )}
        </div>
      </section>

      {isModalOpen && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
          <div className="bg-white p-6 rounded-lg shadow-lg w-11/12 sm:w-96">
            <h2 className="text-xl font-bold mb-4">Avaliar Serviço</h2>

            <div className="mb-4">
              <label className="block text-gray-700 mb-2">Nota:</label>
              <div className="flex space-x-2">
                {[...Array(5)].map((_, index) => (
                  <button
                    key={index}
                    onClick={() => setRating(index + 1)}
                    className={`text-2xl ${
                      index + 1 <= rating ? "text-yellow-500" : "text-gray-300"
                    } hover:text-yellow-500 transition duration-300`}
                  >
                    <FaStar />
                  </button>
                ))}
              </div>
            </div>

            <div className="mb-4">
              <label className="block text-gray-700 mb-2">Comentário:</label>
              <textarea
                value={comment}
                onChange={(e) => setComment(e.target.value)}
                className="w-full p-2 border border-gray-300 rounded-lg"
                rows="3"
              />
            </div>

            <div className="flex justify-end">
              <button
                onClick={closeModal}
                className="bg-gray-500 text-white px-4 py-2 rounded-lg hover:bg-gray-600 transition duration-300 mr-2"
              >
                Fechar
              </button>
              <button
                onClick={handleRate}
                className="bg-green-500 text-white px-4 py-2 rounded-lg hover:bg-green-600 transition duration-300"
              >
                Enviar Avaliação
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

PastBookingCard.propTypes = {
  service: PropTypes.shape({
    id: PropTypes.number.isRequired,
    service_name: PropTypes.string.isRequired,
    date_time: PropTypes.string.isRequired,
    amount_paid: PropTypes.string.isRequired,
    vehicle_type: PropTypes.string.isRequired,
    modality: PropTypes.string.isRequired,
    payment_method: PropTypes.string.isRequired,
    status: PropTypes.string.isRequired,
    rated: PropTypes.bool, // Adicionado para controlar se o serviço já foi avaliado
  }).isRequired,
  onCancel: PropTypes.func.isRequired,
  onRate: PropTypes.func.isRequired,
};

export default PastBookingCard;
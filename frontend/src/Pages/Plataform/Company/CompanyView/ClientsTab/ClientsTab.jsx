import React, { useState } from "react";

const ClientsTab = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalImage, setModalImage] = useState("");

  const clients = [
    {
      name: "João Silva",
      stars: 5,
      comment: "Excelente serviço! Recomendo a todos.",
    },
    {
      name: "Maria Oliveira",
      stars: 4,
      comment: "Ótima experiência, atendimento impecável.",
    },
  ];


  const handleCloseModal = () => {
    setIsModalOpen(false);
    setModalImage("");
  };

  return (
    <div>
      <h3 className="text-lg font-semibold">Feedbacks</h3>
      <p className="text-gray-700 mt-2">
        Veja os depoimentos dos nossos clientes.
      </p>
      <ul className="mt-4 space-y-4">
        {clients.map((client, index) => (
          <li key={index} className="border-b pb-4">
            <h4 className="text-xl font-semibold">{client.name}</h4>
            <div className="flex items-center space-x-2 mt-1">
              {/* Exibe as estrelas */}
              {[...Array(5)].map((_, i) => (
                <svg
                  key={i}
                  xmlns="http://www.w3.org/2000/svg"
                  className={`w-5 h-5 ${
                    i < client.stars ? "text-yellow-500" : "text-gray-300"
                  }`}
                  fill="currentColor"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    fillRule="evenodd"
                    d="M12 2.25l2.1 6.45h6.75l-5.25 3.75 2.1 6.45-5.25-3.75-5.25 3.75 2.1-6.45-5.25-3.75h6.75L12 2.25z"
                  />
                </svg>
              ))}
            </div>
            <p className="text-gray-600 mt-2">{client.comment}</p>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default ClientsTab;

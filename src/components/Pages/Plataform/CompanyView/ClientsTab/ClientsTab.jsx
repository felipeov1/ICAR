import React, { useState } from "react";

const ClientsTab = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalImage, setModalImage] = useState("");

  const clients = [
    {
      name: "João Silva",
      stars: 5,
      comment: "Excelente serviço! Recomendo a todos.",
      photos: [
        "/src/images/icar-blue.jpeg",
        "/src/images/icar-blue.jpeg",
        "/src/images/icar-blue.jpeg",
        "/src/images/icar-blue.jpeg",
      ],
    },
    {
      name: "Maria Oliveira",
      stars: 4,
      comment: "Ótima experiência, atendimento impecável.",
      photos: [
        "/src/images/icar-blue.jpeg",
        "/src/images/icar-blue.jpeg",
        "/src/images/icar-blue.jpeg",
        "/src/images/icar-blue.jpeg",
      ],
    },
  ];

  const handleImageClick = (photo) => {
    setModalImage(photo);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setModalImage("");
  };

  return (
    <div>
      <h3 className="text-lg font-semibold">Clientes</h3>
      <p className="text-gray-700 mt-2">Veja os depoimentos dos nossos clientes.</p>
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
            <div className="mt-4 flex space-x-2 overflow-x-auto">
              {client.photos.map((photo, i) => (
                <img
                  key={i}
                  src={photo}
                  alt={`Foto ${i + 1} do cliente`}
                  className="w-16 h-16 object-cover rounded-lg cursor-pointer"
                  onClick={() => handleImageClick(photo)}
                />
              ))}
            </div>
          </li>
        ))}
      </ul>

      {/* Modal para expandir imagem */}
      {isModalOpen && (
        <div className="fixed inset-0 bg-black bg-opacity-75 flex items-center justify-center z-50">
          <div className="relative">
            <button
              onClick={handleCloseModal}
              className="absolute top-2 left-2 bg-black text-white rounded-full p-2 opacity-75 hover:opacity-100 transition-opacity"
            >
              X
            </button>
            <img
              src={modalImage}
              alt="Imagem expandida"
              className="max-w-full max-h-full object-contain p-4"
            />
          </div>
        </div>
      )}
    </div>
  );
};

export default ClientsTab;

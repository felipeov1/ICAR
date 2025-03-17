import React, { useState } from "react";

const PhotosTab = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalImage, setModalImage] = useState("");

  const photos = [
    "/src/images/icar-logo-blue.jpg",
    "/src/images/icar-logo-blue.jpg",
    "/src/images/icar-logo-blue.jpg",
    "/src/images/icar-logo-blue.jpg",
    "/src/images/icar-logo-blue.jpg",
    "/src/images/icar-logo-blue.jpg",
  ];

  const handleImageClick = (photo) => {
    setModalImage(photo);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setModalImage("");
  };

  // Fechar o modal ao clicar fora da imagem
  const handleOverlayClick = (e) => {
    if (e.target === e.currentTarget) {
      handleCloseModal();
    }
  };

  return (
    <div>
      <h3 className="text-lg font-semibold">Fotos</h3>
      <p className="text-gray-700 mt-2">Confira algumas imagens do local.</p>
      <div className="grid grid-cols-3 gap-2 mt-4">
        {photos.map((photo, i) => (
          <div
            key={i}
            className="w-full h-24 bg-gray-200 rounded-lg flex items-center justify-center cursor-pointer"
            onClick={() => handleImageClick(photo)}
          >
            <img
              src={photo}
              alt={`Imagem ${i + 1}`}
              className="w-full h-full object-cover rounded-lg"
            />
          </div>
        ))}
      </div>

      {isModalOpen && (
        <div
          className="fixed inset-0 bg-black bg-opacity-75 flex items-center justify-center z-50"
          onClick={handleOverlayClick} // Fechar ao clicar fora da imagem
        >
          <div className="relative">
            {/* Botão de Fechar */}
            <button
              onClick={handleCloseModal}
              className="absolute top-4 left-4 bg-black text-white p-2 opacity-75 hover:opacity-100 transition-opacity"
            >
              X
            </button>

            {/* Imagem Expandida */}
            <img
              src={modalImage}
              alt="Imagem expandida"
              className="max-w-full max-h-[90vh] object-contain p-4"
            />
          </div>
        </div>
      )}
    </div>
  );
};

export default PhotosTab;

import React, { useState } from "react";

const PhotosTab = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalImage, setModalImage] = useState("");


  const photos = [
    "/src/images/icar-blue.jpeg",
    "/src/images/icar-blue.jpeg",
    "/src/images/icar-blue.jpeg",
    "/src/images/icar-blue.jpeg",
    "/src/images/icar-blue.jpeg",
    "/src/images/icar-blue.jpeg",
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

export default PhotosTab;

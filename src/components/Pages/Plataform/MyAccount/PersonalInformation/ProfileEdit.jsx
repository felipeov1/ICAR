import React, { useState, useRef } from "react";
import { Link } from "react-router-dom";
import { FaArrowLeft, FaPencilAlt } from "react-icons/fa";
import ProfileImage from "../../../../../images/icar-blue.jpeg";

const ProfileEdit = () => {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [image, setImage] = useState(ProfileImage);
  const fileInputRef = useRef(null);

  const handleSave = () => {
    alert("Informações salvas!");
  };

  const handleEditImage = () => {
    fileInputRef.current.click();
  };

  const handleImageChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setImage(reader.result);
      };
      reader.readAsDataURL(file);
    }
  };


  const formatPhone = (value) => {

    const cleaned = value.replace(/\D/g, "");


    const limited = cleaned.slice(0, 11);


    if (limited.length > 10) {
      return `(${limited.slice(0, 2)}) ${limited.slice(2, 7)}-${limited.slice(7)}`;
    } else if (limited.length > 6) {
      return `(${limited.slice(0, 2)}) ${limited.slice(2, 6)}-${limited.slice(6)}`;
    } else if (limited.length > 2) {
      return `(${limited.slice(0, 2)}) ${limited.slice(2)}`;
    } else {
      return limited;
    }
  };

  const handlePhoneChange = (e) => {
    const formattedPhone = formatPhone(e.target.value);
    setPhone(formattedPhone);
  };

  return (
    <div className="flex flex-col p-6">
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
          Informações Pessoais
        </h2>
      </div>

      <form className="mt-4">
        <div className="mb-4">
          <div className="flex flex-col items-center p-4 mb-6 w-full">
            <div className="relative">
              <img
                src={image}
                alt="Profile"
                className="w-24 h-24 rounded-md shadow-lg"
              />

              <button
                type="button"
                onClick={handleEditImage}
                className="absolute left-[70px] top-[70px] bg-blue-950 border-2 rounded-md p-2 shadow-2xl"
              >
                <FaPencilAlt className="text-white text-sm" />
              </button>

              <input
                type="file"
                ref={fileInputRef}
                className="absolute bottom-1 right-1 opacity-0 w-8 h-8 cursor-pointer"
                onChange={handleImageChange}
                accept="image/*"
              />
            </div>
          </div>

          <label htmlFor="name" className="block text-gray-700">
            Nome Completo
          </label>
          <input
            type="text"
            id="name"
            value={name}
            onChange={(e) => setName(e.target.value)}
            className="mt-2 mb-4 p-3 w-full border border-gray-300 rounded-lg"
          />
        </div>

        <div className="mb-4">
          <label htmlFor="email" className="block text-gray-700">
            E-mail
          </label>
          <input
            type="email"
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="mt-2 mb-4 p-3 w-full border border-gray-300 rounded-lg"
          />
        </div>

        <div className="mb-4">
          <label htmlFor="phone" className="block text-gray-700">
            Celular
          </label>
          <input
            type="text"
            id="phone"
            value={phone}
            onChange={handlePhoneChange} 
            className="mt-2 p-3 w-full border border-gray-300 rounded-lg"
            placeholder="(DD) XXXXX-XXXX"
            maxLength={15} 
          />
        </div>

        <button
          type="button"
          onClick={handleSave}
          className="w-full bg-orange-600 text-white py-3 rounded-lg mt-12 hover:bg-orange-700"
        >
          Salvar Alterações
        </button>
      </form>
    </div>
  );
};

export default ProfileEdit;
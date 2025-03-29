import React from "react";
import { Link } from "react-router-dom";
import {
  FaSignOutAlt,
  FaUser,
  FaLock,
  FaQuestionCircle,
  FaArrowRight,
  FaMap,
} from "react-icons/fa";
import ProfileImage from "../../../../public/assets/images/logo/icar-logo-blue.jpg";

const ProfileSettings = () => {
  return (
    <div className="w-full flex flex-col items-center mt-4 lg:max-w-4xl lg:mx-auto lg:mt-8">
      <div className="w-full p-4 lg:p-8 lg:bg-white lg:rounded-lg lg:shadow-lg">
        {/* Seção do Perfil */}
        <div className="flex flex-wrap gap-4 px-4 mb-6 lg:px-6 lg:mb-8">
          <div>
            <img
              src={ProfileImage}
              alt="Profile"
              className="w-16 h-16 rounded-lg mb-2 border-gray-300 shadow-lg lg:w-20 lg:h-20"
            />
          </div>

          <div className="mt-2">
            <h1 className="text-lg font-bold text-blue-950 lg:text-2xl">
              Felipe Ferreira
            </h1>
          </div>
        </div>

        {/* Lista de Configurações */}
        <ul className="lg:space-y-4">
          <SettingItem
            icon={<FaUser />}
            text="Informações Pessoais"
            path="/app/minha-conta/informacoes-pessoais"
          />
          <SettingItem
            icon={<FaLock />}
            text="Dados de Acesso"
            path="/app/minha-conta/dados-de-acesso"
          />
          <SettingItem
            icon={<FaMap />}
            text="Endereços"
            path="/app/minha-conta/enderecos"
          />
          <SettingItem
            icon={<FaQuestionCircle />}
            text="Ajuda e Suporte"
            path="/app/minha-conta/suporte"
          />

          {/* Botão de Sair */}
          <li className="flex items-center justify-between py-6 px-4 border-b last:border-0 lg:py-4 lg:px-6 lg:border-0">
            <button
              onClick={() => {
                // Lógica de logout (exemplo: limpar token e redirecionar)
                localStorage.removeItem("token"); // Remove o token
                window.location.href = "/entrar"; // Redireciona para a página de login
              }}
              className="flex items-center justify-center w-full bg-red-500 text-white px-4 py-3 rounded-lg hover:bg-red-600 transition duration-300 lg:py-4"
            >
              <FaSignOutAlt className="mr-2" />
              Sair
            </button>
          </li>
        </ul>
      </div>
    </div>
  );
};

// Componente para os itens da lista
const SettingItem = ({ icon, text, path }) => (
  <li className="flex items-center justify-between py-6 px-4 border-b last:border-0 lg:py-4 lg:px-6 lg:border-0 lg:hover:bg-gray-50 lg:rounded-lg lg:transition-colors">
    <Link to={path} className="flex items-center w-full">
      <span className="flex items-center justify-center bg-blue-500 text-white rounded-full p-3 mr-3 lg:p-4">
        {icon}
      </span>
      <span className="text-gray-700 font-medium lg:text-lg">{text}</span>
      <span className="text-gray-400 ml-auto lg:text-xl">
        <FaArrowRight />
      </span>
    </Link>
  </li>
);

export default ProfileSettings;

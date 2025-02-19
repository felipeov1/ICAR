import React from "react";
import { Link } from "react-router-dom"; // Importa o Link para navegação
import {
  FaUser,
  FaCreditCard,
  FaLock,
  FaQuestionCircle,
  FaArrowRight,
  FaMap,
  FaBell,
} from "react-icons/fa";

import ProfileImage from "../../../../images/icar-blue.jpeg";

// image as ref dowloads

const ProfileSettings = () => {
  return (
    <div className="w-full flex flex-col items-center mt-4">

      <div className="w-full p-4">

        <div className="flex items-start flex-wrap gap-4 px-4 mb-6">
          <div>
            <img
              src={ProfileImage}
              alt="Profile"
              className="w-16 h-16 rounded-lg mb-2 border-gray-300 shadow-lg"
            />
          </div>

          <div className="mt-2">
            <h1 className="text-lg font-bold text-blue-950">Felipe Ferreira</h1>
            <h6 className="font-normal text-blue-950">Avaliação: ★ 4.5</h6>
            </div>
        </div>

        <ul>
          <SettingItem
            icon={<FaUser />}
            text="Informações Pessoais"
            path="/icar/minha-conta/informacoes-pessoais"
          />
          <SettingItem
            icon={<FaLock />}
            text="Dados de Acesso"
            path="/icar/minha-conta/dados-de-acesso"
          />
          <SettingItem
            icon={<FaMap />}
            text="Endereços"
            path="/icar/minha-conta/enderecos"
          />
          <SettingItem
            icon={<FaCreditCard />}
            text="Formas de Pagamento"
            path="/icar/minha-conta/formas-de-pagamento"
          />
          <SettingItem
            icon={<FaBell />}
            text="Notificações"
            path="/icar/minha-conta/notificacoes"
          />
          <SettingItem
            icon={<FaQuestionCircle />}
            text="Ajuda e Suporte"
            path="/icar/minha-conta/ajuda-suporte"
          />
        </ul>
      </div>
    </div>
  );
};

const SettingItem = ({ icon, text, path }) => (
  <li className="flex items-center justify-between py-6 px-4 border-b last:border-0">
    <Link to={path} className="flex items-center w-full">
      <span className="flex items-center justify-center bg-blue-500 text-white rounded-full p-3 mr-3">
        {icon}
      </span>
      <span className="text-gray-700 font-medium">{text}</span>
      <span className="text-gray-400 ml-auto">
        <FaArrowRight />
      </span>
    </Link>
  </li>
);

export default ProfileSettings;

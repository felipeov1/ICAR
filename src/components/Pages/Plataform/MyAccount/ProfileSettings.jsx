import React from "react";
import {
  FaMapMarkerAlt,
  FaBell,
  FaCog,
  FaLock,
  FaQuestionCircle,
} from "react-icons/fa";

const ProfileSettings = () => {
  return (
    <div className="w-full flex flex-col items-center ">

      <div className="flex flex-col items-center mb-6 p-6 bg-gray-100 w-full">
        <img
          src="/path-to-image.jpg"
          alt="Profile"
          className="w-24 h-24 rounded-full mb-2 border-4 border-gray-300 "
        />
        <h1 className="text-lg font-semibold">Felipe Ferreira</h1>
      </div>


      <div className="w-full p-4">
        <ul>
          <SettingItem icon={<FaMapMarkerAlt />} text="Informações Pessoais" />
          <SettingItem icon={<FaMapMarkerAlt />} text="Dados de Acesso" />
          <SettingItem icon={<FaBell />} text="Endereços" />
          <SettingItem icon={<FaCog />} text="Formas de Pagamento" />
          <SettingItem icon={<FaLock />} text="Notificações" />
          <SettingItem icon={<FaQuestionCircle />} text="Ajuda e Suporte" />
          <SettingItem icon={<FaCog />} text="Personalização da Plataforma" />
        </ul>
      </div>
    </div>
  );
};

const SettingItem = ({ icon, text }) => (
  <li className="flex items-center justify-between py-6 px-4 border-b last:border-0">
    <div className="flex items-center">
      <span className="text-gray-500 text-lg mr-3">{icon}</span>
      <span className="text-gray-700 font-medium">{text}</span>
    </div>
    <span className="text-gray-400">✎</span>
  </li>
);

export default ProfileSettings;

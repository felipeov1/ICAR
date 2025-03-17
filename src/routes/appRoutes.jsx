import React from "react";
import { Route } from "react-router-dom";
import Icar from "../Pages/Plataform/Main/Index.jsx";
import CompanyView from "../Pages/Plataform/Company/index.jsx";
import BookingSummary from "../Pages/Plataform/Company/Booking/BookingSummary/BookingSummary.jsx";
import Bookings from "../Pages/Plataform/Bookings/index.jsx";
import MyAccount from "../Pages/Plataform/Settings/index.jsx";
import PersonalInformations from "../Pages/Plataform/Settings/PersonalInformation/index.jsx";
import Credentials from "../Pages/Plataform/Settings/Credentials/index.jsx";
import Address from "../Pages/Plataform/Settings/Address/index.jsx";
import HelpSupport from "../Pages/Plataform/Settings/HelpSupport/index.jsx";

const appRoutes = [
  {
    path: "/app",
    element: <Icar />,
  },
  {
    path: "/app/empresa",
    element: <CompanyView />,
  },
  {
    path: "/app/empresa/agendamento",
    element: <BookingSummary />,
  },
  {
    path: "/app/agendamentos",
    element: <Bookings />,
  },
  {
    path: "/app/minha-conta",
    element: <MyAccount />,
  },
  {
    path: "/app/minha-conta/informacoes-pessoais",
    element: <PersonalInformations />,
  },
  {
    path: "/app/minha-conta/dados-de-acesso",
    element: <Credentials />,
  },
  {
    path: "/app/minha-conta/enderecos",
    element: <Address />,
  },
  {
    path: "/app/minha-conta/suporte",
    element: <HelpSupport />,
  },
];

export default appRoutes;
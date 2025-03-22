import React, { lazy } from "react";

const Icar = lazy(() => import("../Pages/Plataform/Main/Index.jsx"));
const CompanyView = lazy(() => import("../Pages/Plataform/Company/index.jsx"));
const BookingSummary = lazy(() =>
  import("../Pages/Plataform/Company/Appointment/AppointmentSummary.jsx")
);
const Bookings = lazy(() => import("../Pages/Plataform/Appointment/index.jsx"));
const MyAccount = lazy(() => import("../Pages/Plataform/Settings/index.jsx"));
const PersonalInformations = lazy(() =>
  import("../Pages/Plataform/Settings/PersonalInformation/index.jsx")
);
const Credentials = lazy(() =>
  import("../Pages/Plataform/Settings/Credentials/index.jsx")
);
const Address = lazy(() =>
  import("../Pages/Plataform/Settings/Address/index.jsx")
);
const HelpSupport = lazy(() =>
  import("../Pages/Plataform/Settings/HelpSupport/index.jsx")
);

const appRoutes = [
  { path: "/app", element: <Icar /> },
  { path: "/app/empresa", element: <CompanyView /> },
  { path: "/app/empresa/agendamento", element: <BookingSummary /> },
  { path: "/app/agendamentos", element: <Bookings /> },
  { path: "/app/minha-conta", element: <MyAccount /> },
  {
    path: "/app/minha-conta/informacoes-pessoais",
    element: <PersonalInformations />,
  },
  { path: "/app/minha-conta/dados-de-acesso", element: <Credentials /> },
  { path: "/app/minha-conta/enderecos", element: <Address /> },
  { path: "/app/minha-conta/suporte", element: <HelpSupport /> },
];

export default appRoutes;

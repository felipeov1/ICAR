import React from "react";
import { Routes, Route } from "react-router-dom";
import landingRoutes from "./landingRoutes";
import appRoutes from "./appRoutes";
import NotFoundLanding from "../components/layout/NotFound/NotFoundLandingPage";
import NotFoundApp from "../components/layout/NotFound/NotFoundApp";

const AppRoutes = () => {
  return (
    <Routes>
      {/* Mapeia as rotas da landing page */}
      {landingRoutes.map((route, index) => (
        <Route key={index} path={route.path} element={route.element} />
      ))}

      {/* Mapeia as rotas da plataforma */}
      {appRoutes.map((route, index) => (
        <Route key={index} path={route.path} element={route.element} />
      ))}

      {/* Rota 404 para a landing page */}
      <Route path="*" element={<NotFoundLanding />} />

      {/* Rota 404 para a plataforma */}
      <Route path="/app/*" element={<NotFoundApp />} />
    </Routes>
  );
};

export default AppRoutes;
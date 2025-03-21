import React from "react";
import { Routes, Route } from "react-router-dom";
import landingRoutes from "./landingRoutes";
import appRoutes from "./appRoutes";
import NotFoundLanding from "../components/layout/NotFound/NotFoundLandingPage";
import NotFoundApp from "../components/layout/NotFound/NotFoundApp";

const AppRoutes = () => {
  return (
    <Routes>

      {landingRoutes.map((route, index) => (
        <Route key={index} path={route.path} element={route.element} />
      ))}


      {appRoutes.map((route, index) => (
        <Route key={index} path={route.path} element={route.element} />
      ))}


      <Route path="*" element={<NotFoundLanding />} />


      <Route path="/app/*" element={<NotFoundApp />} />
    </Routes>
  );
};

export default AppRoutes;
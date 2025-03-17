import React from "react";
import { Route } from "react-router-dom";
import Hero from "../Pages/LandingPage/Sections/Hero/Hero.jsx";
import Features from "../Pages/LandingPage/Sections/Feature/Features.jsx";
import Testimonials from "../Pages/LandingPage/Sections/Testimonials/index.jsx";
import FAQSection from "../Pages/LandingPage/Sections/FAQSection/index.jsx";
import PrivacyPolicy from "../Pages/LandingPage/Pages/Privacy/index.jsx";
import Login from "../Pages/Auth/Login/Login.jsx";
import CriarConta from "../Pages/Auth/Register/Register.jsx";

const landingRoutes = [
  {
    path: "/",
    element: (
      <>
        <Hero />
        <Features />
        <Testimonials />
        <FAQSection />
      </>
    ),
  },
  {
    path: "/nossas-politicas",
    element: <PrivacyPolicy />,
  },
  {
    path: "/entrar",
    element: <Login />,
  },
  {
    path: "/criar-conta",
    element: <CriarConta />,
  },
];

export default landingRoutes;
import React from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  useLocation,
} from "react-router-dom";
import Navbar from "./components/layout/Navbar";
import Hero from "./Pages/LandingPage/Sections/Hero/Hero.jsx";
import Features from "./Pages/LandingPage/Sections/Feature/Features.jsx";
import Testimonials from "./Pages/LandingPage/Sections/Testimonials/index.jsx";
import FAQSection from "./Pages/LandingPage/Sections/FAQSection/index.jsx";
import Footer from "./components/layout/Footer";
import CookieConsent from "./Pages/LandingPage/PopUpCookie/index.jsx";
import PrivacyPolicy from "./Pages/LandingPage/Pages/Privacy/index.jsx";
import BackToTopButton from "./components/layout/BackToTopButton";
import Login from "./Pages/Auth/Login/Login.jsx";
import CriarConta from "./Pages/Auth/Register/Register.jsx";
import Icar from "./Pages/Plataform/Main/Index.jsx";
import CompanyView from "./Pages/Plataform/Company/index.jsx";
import BookingSummary from "./Pages/Plataform/Company/Booking/BookingSummary/BookingSummary.jsx";
import Bookings from "./Pages/Plataform/Bookings/index.jsx";
import MyAccount from "./Pages/Plataform/Settings/index.jsx"
import PersonalInformations from "./Pages/Plataform/Settings/PersonalInformation/index.jsx";
import Credentials from "./Pages/Plataform/Settings/Credentials/index.jsx";
import Address from "./Pages/Plataform/Settings/Address/index.jsx";
import HelpSupport from "./Pages/Plataform/Settings/HelpSupport/index.jsx";

function App() {
  return (
    <Router>
      <AppContent />
    </Router>
  );
}

function AppContent() {
  const location = useLocation();

  return (
    <div className="min-h-screen">
      {location.pathname == "/" && <Navbar />}
      {location.pathname == "/nossas-politicas" && <Navbar />}

      <Routes>
        <Route
          path="/"
          element={
            <div>
              <Hero />
              <Features />
              <Testimonials />
              <FAQSection />
            </div>
          }
        />
        <Route
          path="/nossas-politicas"
          element={
            <div>
              <PrivacyPolicy />
            </div>
          }
        />
        <Route
          path="/entrar"
          element={
            <div>
              <Login />
            </div>
          }
        />

        <Route
          path="/criar-conta"
          element={
            <div>
              <CriarConta />
            </div>
          }
        />

        <Route
          path="/icar"
          element={
            <div>
              <Icar />
            </div>
          }
        />

        <Route
          path="/icar/empresa"
          element={
            <div>
              <CompanyView />
            </div>
          }
        />

        <Route
          path="/icar/empresa/agendamento"
          element={
            <div>
              <BookingSummary />
            </div>
          }
        />

        <Route
          path="/icar/agendamentos"
          element={
            <div>
              <Bookings />
            </div>
          }
        />

        <Route
          path="/icar/minha-conta"
          element={
            <div>
              <MyAccount />
            </div>
          }
        />

        <Route
          path="/icar/minha-conta/informacoes-pessoais"
          element={
            <div>
              <PersonalInformations />
            </div>
          }
        />

        <Route
          path="/icar/minha-conta/dados-de-acesso"
          element={
            <div>
              <Credentials />
            </div>
          }
        />

        <Route
          path="/icar/minha-conta/enderecos"
          element={
            <div>
              <Address />
            </div>
          }
        />

        <Route
          path="/icar/minha-conta/suporte"
          element={
            <div>
              <HelpSupport />
            </div>
          }
        />

     


      </Routes>

      {!location.pathname.includes("/icar") && <Footer />}
      {!location.pathname.includes("/icar") && <CookieConsent />}
      {!location.pathname.includes("/icar") && <BackToTopButton />}
    </div>
  );
}

export default App;

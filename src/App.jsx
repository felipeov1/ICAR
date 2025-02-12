import React from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  useLocation,
} from "react-router-dom";
import Navbar from "./components/layout/Navbar";
import Hero from "./components/Pages/Landing/Sections/Hero/Hero.jsx";
import Features from "./components/Pages/Landing/Sections/Feature/Features.jsx";
import Testimonials from "./components/Pages/Landing/Sections/Testimonials/index.jsx";
import FAQSection from "./components/Pages/Landing/Sections/FAQSection/index.jsx";
import Footer from "./components/layout/Footer";
import CookieConsent from "./components/Pages/Landing/PopUpCookie/index.jsx";
import PrivacyPolicy from "./components/Pages/Landing/Pages/Privacy/index.jsx";
import BackToTopButton from "./components/layout/BackToTopButton";
import Login from "./components/Pages/Login/index.jsx";
import CriarConta from "./components/Pages/CreateAccount/index.jsx";
import Icar from "./components/Pages/Plataform/Main/Index.jsx";
import CompanyView from "./components/Pages/Plataform/CompanyView/index.jsx";
import BookingSummary from "./components/Pages/Plataform/CompanyView/BookingSummary/BookingSummary.jsx";
import Bookings from "./components/Pages/Plataform/Bookings/index.jsx"

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


      </Routes>

      {!location.pathname.includes("/icar") && <Footer />}
      {!location.pathname.includes("/icar") && <CookieConsent />}
      {!location.pathname.includes("/icar") && <BackToTopButton />}
    </div>
  );
}

export default App;

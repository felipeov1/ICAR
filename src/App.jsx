import React, { Suspense } from "react";
import { BrowserRouter as Router, useLocation } from "react-router-dom";
import Navbar from "./components/layout/Navbar";
import Footer from "./components/layout/Footer";
import Loading from "./components/layout/loading"; 
import CookieConsent from "./Pages/LandingPage/PopUpCookie/index.jsx";
import BackToTopButton from "./components/layout/BackToTopButton";
import AppRoutes from "./routes/routes"; 

function App() {
  return (
    <Router>
      <AppContent />
    </Router>
  );
}

function AppContent() {
  const location = useLocation();
  const isAppRoute = location.pathname.startsWith("/app");

  return (
    <div className="min-h-screen">

      {!isAppRoute && <Navbar />}

      <Suspense fallback={<Loading />}>
        <AppRoutes /> 
      </Suspense>

      {!isAppRoute && (
        <>
          <Footer />
          <CookieConsent />
          <BackToTopButton />
        </>
      )}
    </div>
  );
}

export default App;
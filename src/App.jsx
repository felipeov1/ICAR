import React, { Suspense, useEffect } from "react";
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
  const isAuthPage = ["/entrar", "/criar-conta"].includes(location.pathname); 

  useEffect(() => {
    window.scrollTo(0, 0); 
  }, [location.pathname]); 

  return (
    <div className="min-h-screen">
      {!isAppRoute && !isAuthPage && <Navbar />} 

      <Suspense fallback={<Loading />}>
        <AppRoutes /> 
      </Suspense>

      {!isAppRoute && !isAuthPage && ( 
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
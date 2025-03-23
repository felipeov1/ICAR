import React, { Suspense, useEffect } from "react";
import { BrowserRouter as Router, useLocation } from "react-router-dom";
import Navbar from "./components/layout/Navbar";
import Footer from "./components/layout/Footer";
import Loading from "./components/layout/Loading.jsx";
import CookieConsent from "./Pages/LandingPage/PopUpCookie/index.jsx";
import BackToTopButton from "./components/layout/BackToTopButton";
import AppRoutes from "./routes/routes";
import { LoadingProvider, useLoading } from "./context/LoadingContext.jsx";

function App() {
  return (
    <Router>
      <LoadingProvider>
        <AppContent />
      </LoadingProvider>
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

      {!isAppRoute && <Navbar />}

      <Suspense fallback={<Loading />}>
        <AppRoutes />
      </Suspense>

      {!isAuthPage && !isAppRoute && (
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
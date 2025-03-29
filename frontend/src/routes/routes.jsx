import React, { Suspense, useEffect } from "react";
import { Routes, Route, useLocation } from "react-router-dom";
import landingRoutes from "./landingRoutes";
import appRoutes from "./appRoutes";
import NotFoundLanding from "../components/layout/NotFound/NotFoundLandingPage";
import NotFoundApp from "../components/layout/NotFound/NotFoundApp";
import Loading from "../components/layout/Loading";
import { useLoading } from "../context/LoadingContext";

const AppRoutes = () => {
  const { loading, setLoading } = useLoading();
  const location = useLocation();

  useEffect(() => {
    setLoading(true);

    const timeout = setTimeout(() => {
      setLoading(false);
    }, 100);

    return () => clearTimeout(timeout);
  }, [location.pathname, setLoading]);

  return (
    <>
      {loading && <Loading />}
      <Suspense fallback={<Loading />}>
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
      </Suspense>
    </>
  );
};

export default AppRoutes;

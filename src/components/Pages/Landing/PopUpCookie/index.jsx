import React, { useState, useEffect } from "react";

const CookieConsent = () => {
  const [isVisible, setIsVisible] = useState(false);

  useEffect(() => {
    const consentAccepted = localStorage.getItem("consentAccepted");
    if (!consentAccepted) {
      setIsVisible(true);
    }
  }, []);

  const handleAccept = () => {
    localStorage.setItem("consentAccepted", "true");
    setIsVisible(false);
  };

  if (!isVisible) return null;

  return (
    <div className="fixed bottom-4 left-4 right-4 bg-white border border-gray-300 shadow-lg p-4 rounded-lg z-50 flex items-center justify-between space-x-4">
      <div className="text-sm text-gray-700">
        <p>
          Usamos cookies e coletamos dados para melhorar sua experiência no
          site. Ao continuar, você concorda com nossa{" "}
          <a
            href="/politica-de-privacidade"
            target="_blank"
            rel="noopener noreferrer"
            className="text-blue-500 underline hover:text-blue-700"
          >
            Política de Privacidade
          </a>{" "}
          e{" "}
          <a
            href="/politica-de-cookies"
            target="_blank"
            rel="noopener noreferrer"
            className="text-blue-500 underline hover:text-blue-700"
          >
            Política de Cookies
          </a>
          .
        </p>
      </div>
      <button
        onClick={handleAccept}
        className="bg-[#1e3a8a] hover:bg-blue-600 text-white text-sm font-medium py-2 px-4 rounded-md"
      >
        Aceitar
      </button>
    </div>
  );
};

export default CookieConsent;

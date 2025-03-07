import React from "react";
import HeaderMain from "./CompanyView/Header/Header";
import Description from "./CompanyView/ClientsTab/Description/Description";
import LocationMap from "./CompanyView/LocationMap/LocationMap";
import Tabs from "./CompanyView/Tabs/Tabs";
import Footer from "../Main/Footer/Footer";

const services = [
  { id: 1, name: "Lavagem Completa", locationType: "lava-rapido" },
  { id: 2, name: "Polimento", locationType: "domicilio" },
  { id: 3, name: "Higienização", locationType: "lava-rapido" },
];

const App = () => {
  const hasLavaRapidoService = services.some(
    (service) => service.locationType === "lava-rapido"
  );

  return (
    <div className="w-full h-screen flex flex-col">
      <HeaderMain />

      <div className="flex-grow flex flex-col lg:flex-row pb-24 lg:pb-0 lg:px-0 lg:pt-2">
        <div className="flex-grow p-4 lg:w-2/3 bg-gray-100 overflow-auto lg:rounded-lg lg:shadow-lg lg:mr-4">
          <Description />
          {hasLavaRapidoService && <LocationMap />}
        </div>

        <div className="lg:w-1/3 bg-white shadow-lg overflow-auto lg:rounded-lg lg:shadow-lg">
          <Tabs />
        </div>
      </div>

      <Footer />
    </div>
  );
};

export default App;

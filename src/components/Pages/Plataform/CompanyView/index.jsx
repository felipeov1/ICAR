import React from "react";
import HeaderMain from "./Header/Header";
import Description from "./ClientsTab/Description/Description";
import LocationMap from "./LocationMap/LocationMap";
import Tabs from "./Tabs/Tabs";
import Footer from "../Main/Footer/Footer";

const App = () => (
  <div className="w-full h-screen flex flex-col">
    <HeaderMain />

    <div className="flex-grow flex flex-col lg:flex-row pb-24 lg:pb-0 lg:px-0 lg:pt-2">
      {/* Conteúdo Principal (Descrição e Mapa) */}
      <div className="flex-grow p-4 lg:w-2/3 bg-gray-100 overflow-auto lg:rounded-lg lg:shadow-lg lg:mr-4">
        <Description />
        <LocationMap />
      </div>

      {/* Tabs (Lado Direito) */}
      <div className="lg:w-1/3 bg-white shadow-lg overflow-auto lg:rounded-lg lg:shadow-lg">
        <Tabs />
      </div>
    </div>

    <Footer />
  </div>
);

export default App;
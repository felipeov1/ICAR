import React from "react";
import HeaderMain from "./Header/Header";
import Description from "./ClientsTab/Description/Description";
import LocationMap from "./LocationMap/LocationMap";
import Tabs from "./Tabs/Tabs";
import Footer from "../Main/Footer/Footer";

const App = () => (
  <div className="w-full h-screen flex flex-col">
    <HeaderMain />

    <div className="flex-grow flex flex-col lg:flex-row pb-24">
      <div className="flex-grow p-4 lg:w-2/3 bg-gray-100 overflow-auto">
        <Description />
        <LocationMap />
      </div>

      <div className="lg:w-1/3 bg-white shadow-lg overflow-auto">
        <Tabs />
      </div>
    </div>

    <Footer />
  </div>
);

export default App;

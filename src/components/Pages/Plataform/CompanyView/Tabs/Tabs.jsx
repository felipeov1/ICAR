import React, { useState } from "react";
import BookingTab from "../BookingTab/BookingTab";
import ClientsTab from "../ClientsTab/ClientsTab";
import PhotosTab from "../PhotosTab/PhotosTab";

const Tabs = () => {
  const [activeTab, setActiveTab] = useState("agendamento");

  return (
    <div className="p-4">
      <div className="flex justify-around border-b">
        {["agendamento", "clientes", "fotos"].map((tab) => (
          <button
            key={tab}
            className={`pb-2 text-lg ${
              activeTab === tab ? "border-b-2 border-blue-500" : ""
            }`}
            onClick={() => setActiveTab(tab)}
          >
            {tab.charAt(0).toUpperCase() + tab.slice(1)}
          </button>
        ))}
      </div>
      <div className="mt-4">
        {activeTab === "agendamento" && <BookingTab />}
        {activeTab === "clientes" && <ClientsTab />}
        {activeTab === "fotos" && <PhotosTab />}
      </div>
    </div>
  );
};

export default Tabs;


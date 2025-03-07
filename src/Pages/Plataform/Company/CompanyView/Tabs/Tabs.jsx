import React, { useState } from "react";
import ServicesTab from "../ServicesAvaiables/ServicesAvaliables";
import ClientsTab from "../ClientsTab/ClientsTab";
import PhotosTab from "../PhotosTab/PhotosTab";

const Tabs = () => {
  const [activeTab, setActiveTab] = useState("serviços");

  return (
    <div className="p-4">
      <div className="flex justify-around border-b">
        {["serviços", "feedbacks", "fotos"].map((tab) => (
          <button
            key={tab}
            className={`pb-2 text-lg w-1/3 ${
              activeTab === tab ? "border-b-2 border-blue-500" : ""
            }`}
            onClick={() => setActiveTab(tab)}
          >
            {tab.charAt(0).toUpperCase() + tab.slice(1)}
          </button>
        ))}
      </div>
      <div className="mt-4">
        {activeTab === "serviços" && <ServicesTab />}
        {activeTab === "feedbacks" && <ClientsTab />}
        {activeTab === "fotos" && <PhotosTab />}
      </div>
    </div>
  );
};

export default Tabs;

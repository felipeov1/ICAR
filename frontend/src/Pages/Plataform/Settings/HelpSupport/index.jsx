import React from "react";
import Header from "../../Main/Header/Header";
import Footer from "../../Main/Footer/Footer";
import HelpSupport from "./HelpSupport";

const index = () => (
  <div className="w-full h-screen flex flex-col">
    <Header className="relative bg-[#170d72] text-white flex lg:p-4 p-6  rounded-bl-lg rounded-br-lg" />
    <div className="flex-1 pb-16">
      <HelpSupport />
    </div>
    <Footer />
  </div>
);

export default index;

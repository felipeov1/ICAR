import React from "react";
import Header from "../Main/Header/Header";
import Footer from "../Main/Footer/Footer";
import Bookings from "./Bookings";

const index = () => {
  return (
    <div className="w-full h-screen flex flex-col">
      <Header className="relative bg-[#170d72] text-white flex p-6 rounded-bl-lg rounded-br-lg" />
      <Bookings />
      <Footer />
    </div>
  );
};

export default index;

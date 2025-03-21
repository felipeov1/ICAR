import React from "react";
import { ToastContainer } from "react-toastify"; 
import "react-toastify/dist/ReactToastify.css"; 
import Header from "../Main/Header/Header";
import Footer from "../Main/Footer/Footer";
import Bookings from "./Bookings";

const BookingsPage = () => {
  return (
    <div className="w-full h-screen flex flex-col">
      <Header className="relative bg-[#170d72] text-white flex lg:p-4 p-6 rounded-bl-lg rounded-br-lg" />
      <Bookings />
      <div className="flex-1 pb-16">
      <Footer />
      </div>
      <ToastContainer position="top-right" autoClose={3000} />
    </div>
  );
};

export default BookingsPage;
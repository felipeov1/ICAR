import React from "react";
import Header from "../../Main/Header/Header";
import Footer from "../../Main/Footer/Footer";
import ProfileEdit from "./ProfileEdit";

const index = () => (
  <div className="w-full h-screen flex flex-col">
    <Header className="relative bg-[#170d72] text-white flex p-6 rounded-bl-lg rounded-br-lg" />
    <ProfileEdit />
    <Footer />
  </div>
);

export default index;

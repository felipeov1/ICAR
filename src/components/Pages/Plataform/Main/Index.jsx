import React from "react";
import Header from "../Main/Header/Header";
import ServiceDetails from "../Main/ServiceDetails/ServiceDetails";
import Highlights from "./Ads/Ads";
import NearbyOptions from "./HighlightsOptions/HighlightsOptions";
import CompanyList from "../Main/CompanyList/CompanyList";
import Footer from "../Main/Footer/Footer";



const Index = () => {
  return (
    <div className="bg-gray-100 min-h-screen">
      <Header />
      <ServiceDetails />
      <Highlights />
      <NearbyOptions />
      <CompanyList />
      <Footer />
    </div>
  );
};

export default Index;

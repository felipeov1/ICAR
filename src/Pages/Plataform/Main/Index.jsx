import React from "react";
import HeaderMain from "./Header/HeaderMain";
import ServiceDetails from "../Main/NextService/NextService";
import Highlights from "./Ads/Ads";
import NearbyOptions from "./HighlightsOptions/HighlightsOptions";
import CompanyList from "../Main/CompanyList/CompanyList";
import Footer from "../Main/Footer/Footer";

const Index = () => {
  return (
    <div className="bg-gray-100 min-h-screen">
      <HeaderMain />
      <ServiceDetails />
      <Highlights />
      <NearbyOptions />
      <CompanyList />
      <Footer />
    </div>
  );
};

export default Index;

import React, { useState } from "react";
import Bookings from "./TimeBooking";

const ListCards = () => {



  return (
    <div className="relative">
     

      <ul className="flex space-y-1 list-none list-inside">
        <Bookings  />
      </ul>
    </div>
  );
};

export default ListCards;

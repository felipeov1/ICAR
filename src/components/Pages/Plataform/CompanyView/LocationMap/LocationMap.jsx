import React, { useEffect } from "react";

////// !!!!! TROCAR PARA https://www.mapbox.com/  !!!!! //////

const LocationMap = () => {
  const latitude = -23.2927;
  const longitude = -51.1732;

  const zoom = 16;

  useEffect(() => {
    const script = document.createElement("script");
    script.src = `https://maps.googleapis.com/maps/api/js?key=AIzaSyB631EO10ylEKxrhfRf4wPv_0G-FXGBTWE&callback=initMap&libraries=&v=weekly`;
    script.async = true;
    script.defer = true;
    document.body.appendChild(script);

    window.initMap = () => {
      const map = new window.google.maps.Map(document.getElementById("map"), {
        center: { lat: latitude, lng: longitude },
        zoom: zoom,
        disableDefaultUI: true,

        gestureHandling: "none",
      });

      new window.google.maps.Marker({
        position: { lat: latitude, lng: longitude },
        map: map,
        title: "Localização",
      });
    };

    return () => {
      document.body.removeChild(script);
    };
  }, [latitude, longitude, zoom]);

  return (
    <div>
      <div className="flex items-center justify-between">
        <h2 className="text-xl font-semibold">Localização</h2>
        <a
          href={`https://www.google.com/maps?q=${latitude},${longitude}`}
          target="_blank"
          rel="noopener noreferrer"
          className="bg-orange-600 text-white py-1 px-3 rounded-md text-sm hover:bg-orange-700"
        >
          Abrir no Google Maps
        </a>
      </div>

      <div
        className="w-full h-64 bg-gray-200 rounded-lg mt-2 relative"
        id="map"
      ></div>
    </div>
  );
};

export default LocationMap;

import React from "react";

const ServiceDetails = () => {
  return (
    <section className="lg:hidden md:hidden p-4 bg-white shadow-md rounded-md m-4 mt-[-80px]">
      <h2 className="text-lg font-bold mb-2">Próximo Serviço</h2>
      <p><strong>Serviço:</strong> Lavagem completa</p>
      <p><strong>Data agendada:</strong> 11/11/2024 11:00</p>
      <p><strong>Valor:</strong> R$50,00</p>
      <p><strong>Veículo:</strong> Ford Ka</p>
      <div className="text-right text-blue-500 font-bold text-lg">10%</div>
    </section>
  );
};

export default ServiceDetails;

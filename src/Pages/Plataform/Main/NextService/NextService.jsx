import React from "react";

const NextService = () => {
  const services = [
    {
      id: 1,
      service_name: "Lavagem completa",
      modality: "Domiciliar",
      vehicle_type: "Hatch",
      date_time: "2025-11-11 11:00:00",
      amount_paid: "50.0",
      payment_method: "Cartão de Crédito",
      status: "pending", 
    },
  ];

  const hasServices = services.length > 0;

  return (
    <section className="lg:hidden md:hidden p-6 bg-white shadow-lg border rounded-md m-4 mt-[-100px]">
      {/* Título */}
      <h2 className="text-xl font-bold text-blue-800 mb-4">Próximo Serviço</h2>

      {/* Verifica se há serviços */}
      {hasServices ? (
        <div className="space-y-2">
          <div>
            <span className="text-gray-600">Serviço: </span>
            <span className="font-medium">{services[0].service_name}</span>
          </div>
          <div>
            <span className="text-gray-600">Tipo do Veículo: </span>
            <span className="font-medium">{services[0].vehicle_type}</span>
          </div>
          <div>
            <span className="text-gray-600">Local: </span>
            <span className="font-medium">{services[0].modality}</span>
          </div>
          <div>
            <span className="text-gray-600">Data do Serviço: </span>
            <span className="font-medium">{services[0].date_time}</span>
          </div>
          <div>
            <span className="text-gray-600">Valor: </span>
            <span className="font-medium">R${services[0].amount_paid}</span>
          </div>
          <div>
            <span className="text-gray-600">Método de Pagamento: </span>
            <span className="font-medium">{services[0].payment_method}</span>
          </div>
        </div>
      ) : (
        <p className="text-gray-600">Nenhum serviço agendado.</p>
      )}

    </section>
  );
};

export default NextService;
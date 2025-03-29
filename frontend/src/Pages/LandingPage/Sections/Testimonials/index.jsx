import React, { useState, useEffect } from "react";
import { motion } from "framer-motion";
import { FaStar } from "react-icons/fa";

const Testimonials = () => {
  const testimonials = [
    {
      name: "Lucas M. Andrade",
      title: "Empresário",
      quote:
        "A plataforma revolucionou a forma como cuido do meu carro! Consigo agendar uma lavagem no horário que me convém e escolher se quero ir até o lava-rápido ou receber o serviço na minha casa. Prático, rápido e eficiente!",
      image: "https://via.placeholder.com/150",
    },
    {
      name: "Carla S. Mendes",
      title: "Professora Universitária",
      quote:
        "Sempre tive dificuldade em encontrar tempo para levar o carro ao lava-rápido. Com a plataforma, agendar é simples e posso solicitar o serviço onde eu estiver. É uma facilidade que economiza meu tempo e deixa meu carro impecável!",
      image: "https://via.placeholder.com/150",
    },
    {
      name: "Fernando R. Costa",
      title: "Motorista de Aplicativo",
      quote:
        "Trabalhar com carro exige que ele esteja sempre limpo. A plataforma me ajuda a encontrar os melhores lava-rápidos próximos de mim e agendar rapidamente, sem perder tempo. Posso até pedir para o profissional vir até minha localização. Excelente solução!",
      image: "https://via.placeholder.com/150",
    },
  ];

  const [activeIndex, setActiveIndex] = useState(0);

  useEffect(() => {
    const interval = setInterval(() => {
      setActiveIndex((prevIndex) => (prevIndex + 1) % testimonials.length);
    }, 5000);

    return () => clearInterval(interval);
  }, []);

  return (
    <section id="depoimentos" className="py-16 bg-white">
      <div className="container mx-auto px-4 max-w-5xl">
        <div className="text-center mb-12">
          <h2 className="text-4xl font-bold mb-4">O Que Os Clientes</h2>
          <h2 className="text-4xl font-bold mb-4">Falam da Plataforma</h2>
        </div>

        <div className="flex flex-col lg:flex-row items-center justify-between">
          <motion.div
            className="flex-shrink-0 mb-6 lg:mb-0"
            initial={{ opacity: 0, x: -50 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.6 }}
          >
            <img
              src={testimonials[activeIndex].image}
              alt={testimonials[activeIndex].name}
              className="rounded-lg w-52 h-52 object-cover"
            />
          </motion.div>

          <motion.div
            className="flex-1 pl-8"
            initial={{ opacity: 0, x: 50 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.6 }}
          >
            <div className="flex mb-4">
              {[...Array(5)].map((_, i) => (
                <FaStar key={i} className="text-yellow-400 mr-1" />
              ))}
            </div>
            <p className="text-gray-700 mb-4 leading-relaxed">
              "{testimonials[activeIndex].quote}"
            </p>
            <h3 className="font-bold text-lg">
              {testimonials[activeIndex].name}
            </h3>
            <p className="text-gray-500">{testimonials[activeIndex].title}</p>
          </motion.div>

          <div className="flex lg:flex-col md:flex-row gap-1  lg:space-y-0 ml-8 items-center justify-center">
            {testimonials.map((_, index) => (
              <button
                key={index}
                onClick={() => setActiveIndex(index)}
                className={`h-3 w-3 rounded-full ${
                  activeIndex === index ? "bg-[#1e3a8a]" : "bg-gray-300"
                }`}
              />
            ))}
          </div>
        </div>
      </div>
    </section>
  );
};

export default Testimonials;

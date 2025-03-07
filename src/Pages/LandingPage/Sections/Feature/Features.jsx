import React from "react";
import { motion } from "framer-motion";
import { useInView } from "react-intersection-observer";
import FeatureCard from "./FeatureCard";
import { 
  ClockIcon,
  StarIcon,
  CreditCardIcon,
  CalendarIcon 
} from '@heroicons/react/24/outline';// import { features } from "../../../../../data/features";

const Features = () => {

  const features = [
    {
      icon: ClockIcon,
      title: "Economia de Tempo",
      description:
        "Agende de forma rápida e eficiente para otimizar seu tempo e agilizar o processo de agendamento",
    },
    {
      icon: StarIcon,
      title: "Avaliações de Clientes",
      description: "Feedback real de clientes satisfeitos com nossos serviços",
    },
    {
      icon: CreditCardIcon,
      title: "Pagamento Integrado",
      description: "Processo de pagamento seguro e simplificado na plataforma",
    },
    {
      icon: CalendarIcon,
      title: "Agendamento Flexível",
      description: "Escolha o melhor horário de acordo com sua disponibilidade",
    },
  ];

  const [ref, inView] = useInView({
    triggerOnce: true,
    threshold: 0.1,
  });

  return (
    <section id="funcionalidades" className="py-20 bg-[#263787]">
      <div className="container mx-auto px-4">
        <motion.div
          ref={ref}
          initial={{ opacity: 0, y: 20 }}
          animate={inView ? { opacity: 1, y: 0 } : {}}
          transition={{ duration: 0.6 }}
          className="text-center mb-12"
        >
          <h2 className="text-4xl text-white font-bold mb-4">
            Nossas Funcionalidades
          </h2>
          <p className="text-white max-w-2xl mx-auto">
            Descubra como a ICAR pode transformar sua experiência com serviços
            de limpeza automotivas
          </p>
        </motion.div>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
          {features.map((feature, index) => (
            <FeatureCard key={index} {...feature} delay={index * 0.1} />
          ))}
        </div>
      </div>
    </section>
  );
};

export default Features;

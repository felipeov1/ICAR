import React, { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { IoIosArrowDown, IoIosArrowUp } from 'react-icons/io';

const FAQSection = () => {
  const [activeIndex, setActiveIndex] = useState(null);

  const toggleAnswer = (index) => {
    setActiveIndex(activeIndex === index ? null : index);
  };

  const faqs = [
    {
      question: "Como faço para me cadastrar?",
      answer: "Para se cadastrar, clique no botão de cadastro e preencha as informações solicitadas. Cadastar Agora"
    },
    {
      question: "Como funciona a lavagem a domicílio?",
      answer: "Somos um marketplace que mostra os melhores lava-rápidos de sua região. Ao acessar a plataforma procure os lava-rápidos que oferecem esse tipo de serviço e faça o agendamento."
    },
    {
      question: "Criar uma conta na plataforma é gratuito?",
      answer: "Sim! Criar uma conta é 100% gratuito. Você só paga pela avaliação, e mais nada!"
    },
    {
      question: "Como posso fazer uma alteração no meu cadastro?",
      answer: "Você pode alterar as informações do seu cadastro acessando a seção 'Editar Perfil' na sua conta."
    },
    {
      question: "Quais formas de pagamento são aceitas?",
      answer: "Aceitamos cartões de crédito e pagamento via pix."
    }
  ];

  return (
    <section id="duvidas" className="py-20 bg-gray-50">
      <div className="container mx-auto px-4">
        <div className="max-w-2xl mx-auto">
          <h2 className="text-4xl font-bold text-center mb-8">Perguntas Frequentes</h2>
          <div className="space-y-4">
            {faqs.map((faq, index) => (
              <div key={index} className="border-b">
                <button
                  className="w-full text-left py-4 px-6 bg-gray-200 rounded-lg text-xl font-semibold flex justify-between items-center"
                  onClick={() => toggleAnswer(index)}
                >
                  <span>{faq.question}</span>
                  {activeIndex === index ? (
                    <IoIosArrowUp size={24} />
                  ) : (
                    <IoIosArrowDown size={24} />
                  )}
                </button>
                <AnimatePresence>
                  {activeIndex === index && (
                    <motion.div
                      initial={{ height: 0, opacity: 0 }}
                      animate={{ height: 'auto', opacity: 1 }}
                      exit={{ height: 0, opacity: 0 }}
                      transition={{ height: { duration: 0 }, opacity: { duration: 0 } }}
                      className="overflow-hidden px-6 py-4 text-gray-600 bg-gray-100 rounded-b-lg"
                    >
                      <motion.div>
                        {faq.answer}
                      </motion.div>
                    </motion.div>
                  )}
                </AnimatePresence>
              </div>
            ))}
          </div>
          <div className="text-center mt-8">
            <p className="text-gray-700 text-lg mb-4">
              Ficou com alguma dúvida? Entre em contato com a gente!
            </p>
            <a
              href="https://wa.me/5599999999999"
              target="_blank"
              rel="noopener noreferrer"
              className="inline-block bg-green-500 text-white font-bold py-3 px-6 rounded-lg hover:bg-green-600 transition duration-300"
            >
              Fale Conosco no WhatsApp
            </a>
          </div>
        </div>
      </div>
    </section>
  );
};

export default FAQSection;

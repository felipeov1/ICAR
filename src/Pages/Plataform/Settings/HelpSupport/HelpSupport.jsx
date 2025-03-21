import React, { useState } from "react";
import { Link } from "react-router-dom";
import {
  FaArrowLeft,
  FaQuestionCircle,
  FaWhatsapp,
  FaChevronDown,
  FaChevronUp,
} from "react-icons/fa";

const HelpSupport = () => {
  const [message, setMessage] = useState("");
  const [activeIndex, setActiveIndex] = useState(null); // Estado para controlar o item ativo

  // Função para enviar a mensagem via WhatsApp
  const handleSendMessage = () => {
    if (!message.trim()) {
      alert("Por favor, digite uma mensagem antes de enviar.");
      return;
    }

    const phoneNumber = "551112345678"; // Número de telefone do suporte (com código do país)
    const encodedMessage = encodeURIComponent(message); // Codifica a mensagem para URL
    const whatsappUrl = `https://wa.me/${phoneNumber}?text=${encodedMessage}`;

    // Abre o WhatsApp em uma nova aba
    window.open(whatsappUrl, "_blank");
  };

  // Função para alternar a visibilidade de um item do accordion
  const toggleAccordion = (index) => {
    setActiveIndex(activeIndex === index ? null : index); // Fecha o item se já estiver aberto
  };

  // Dados das perguntas frequentes
  const faqItems = [
    {
      question: "Como alterar minha senha?",
      answer:
        "Para alterar sua senha, acesse a seção 'Dados de Acesso' no menu de configurações da sua conta. Lá você encontrará a opção para atualizar sua senha.",
    },
    {
      question: "Como atualizar meu endereço?",
      answer:
        "Você pode atualizar seu endereço na seção 'Endereços' do menu de configurações. Basta editar o endereço atual ou adicionar um novo.",
    },
    {
      question: "Como contatar o suporte?",
      answer:
        "Você pode entrar em contato conosco através do WhatsApp clicando no botão 'Enviar Mensagem' abaixo ou enviar um e-mail para suporte@icar.com.",
    },
  ];

  return (
    <div className=" flex flex-col items-center justify-center bg-gray-50 ">
      <div className="w-full max-w-4xl bg-white rounded-lg  p-8">
        {/* Botão de Voltar */}
        <Link
          to="/app/minha-conta"
          className="flex items-center text-blue-500 hover:text-blue-700 mb-8"
        >
          <FaArrowLeft className="mr-2" />
        </Link>

        {/* Título da Página */}
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-blue-950 flex items-center justify-center">
            <FaQuestionCircle className="mr-2" />
            Ajuda e Suporte
          </h1>
          <p className="text-gray-600 mt-2">
            Estamos aqui para ajudar! Escolha uma opção abaixo ou entre em
            contato diretamente pelo WhatsApp.
          </p>
        </div>

        {/* Seção de Perguntas Frequentes (Accordion) */}
        <div className="mb-8">
          <h2 className="text-xl font-semibold text-blue-950 mb-4">
            Perguntas Frequentes
          </h2>
          <ul className="space-y-3">
            {faqItems.map((item, index) => (
              <li key={index}>
                <div
                  className="flex items-center justify-between p-3 bg-gray-100 rounded-lg hover:bg-gray-200 transition duration-300 cursor-pointer"
                  onClick={() => toggleAccordion(index)}
                >
                  <span className="text-blue-500 hover:text-blue-700">
                    {item.question}
                  </span>
                  {activeIndex === index ? (
                    <FaChevronUp className="text-gray-500" />
                  ) : (
                    <FaChevronDown className="text-gray-500" />
                  )}
                </div>
                {activeIndex === index && (
                  <div className="p-4 bg-gray-50 rounded-b-lg">
                    <p className="text-gray-700">{item.answer}</p>
                  </div>
                )}
              </li>
            ))}
          </ul>
        </div>

        {/* Seção de Contato via WhatsApp */}
        <div className="bg-green-50 p-6 rounded-lg">
          <h2 className="text-xl font-semibold text-green-900 mb-4">
            Contato via WhatsApp
          </h2>
          <p className="text-gray-700 mb-4">
            Preencha o campo abaixo com sua dúvida ou problema, e clique em
            "Enviar Mensagem" para ser redirecionado ao WhatsApp.
          </p>
          <textarea
            placeholder="Digite sua mensagem aqui..."
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500 mb-4"
            rows="4"
          />
          <button
            onClick={handleSendMessage}
            className="w-full flex items-center justify-center bg-green-500 text-white px-4 py-3 rounded-lg hover:bg-green-600 transition duration-300"
          >
            <FaWhatsapp className="mr-2" />
            Enviar Mensagem
          </button>
        </div>
      </div>
    </div>
  );
};

export default HelpSupport;

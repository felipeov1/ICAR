import React, { useState } from "react";

const PrivacyPolicy = () => {
  const [activeLink, setActiveLink] = useState("#introducao");

  const links = [
    { href: "#introducao", text: "1. Introdução" },
    { href: "#nosso_valores", text: "2. Nossos Valores" },
    { href: "#coleta_dados", text: "3. Por Que Coletamos Seus Dados" },
    { href: "#informacoes", text: "4. Informações Coletadas" },
    { href: "#utilizacao", text: "5. Como Usamos Seus Dados" },
    { href: "#terceiros", text: "6. Serviços de Terceiros" },
    { href: "#cookies", text: "7. Cookies" },
    { href: "#seguranca", text: "8. Segurança" },
    { href: "#compartilhamento", text: "9. Compartilhamento de Dados" },
    { href: "#alteracoes", text: "10. Alterações na Política" },
    { href: "#direitos", text: "11. Seus Direitos" },
    { href: "#termos", text: "12. Termos de Uso" },
    { href: "#contato", text: "13. Contato" },
  ];

  const handleSmoothScroll = (e, href) => {
    e.preventDefault();
    setActiveLink(href);

    const target = document.querySelector(href);
    const offset = 100;

    if (target) {
      const topPosition =
        target.getBoundingClientRect().top + window.scrollY - offset;
      window.scrollTo({
        top: topPosition,
        behavior: "smooth",
      });
    }
  };

  return (
    <section className="relative min-h-screen bg-[#fcfcfc] pt-[3.6rem]">

      <header className="w-full bg-[#170d72] text-white p-20 lg:p-40">
        <div className="container mx-auto">

          <nav className="text-sm text-gray-300 mb-2">
            <a href="/" className="hover:underline">
              Home
            </a>{" "}
            {" > "}
            <span>Nossas Políticas</span>
          </nav>


          <h1 className="text-3xl lg:text-4xl font-bold text-left">
            Nossas Políticas
          </h1>
        </div>
      </header>


      <div className="flex flex-col justify-center lg:flex-row">

        <nav className="lg:w-1/4 p-5 mt-6 lg:mt-16 lg:ml-6">
          <ul className="space-y-3 border-l-2 border-gray-200">
            {links.map((link) => (
              <li key={link.href}>
                <a
                  href={link.href}
                  onClick={(e) => handleSmoothScroll(e, link.href)}
                  className={`pl-4 block text-gray-700 hover:text-black transition-all duration-200 ${
                    activeLink === link.href
                      ? "font-bold text-black border-l-2 border-black"
                      : "border-l-2 border-transparent"
                  }`}
                >
                  {link.text}
                </a>
              </li>
            ))}
          </ul>
        </nav>

        <div className="flex-1 p-6  w-full lg:w-3/4 max-w-4xl mt-[4rem]">

          <h1 className="text-4xl font-serif mb-4 text-gray-800 text-center lg:text-left">
            Guia de Privacidade, Uso de Cookies e Termos
          </h1>
          <h1 className="text-4xl font-serif mb-4 text-gray-800 text-center lg:text-left">
            de Uso
          </h1>
          <p className="text-gray-500 mb-10 text-center lg:text-left">
            Última Atualização: 18 de dezembro de 2024
          </p>


          <section id="introducao" className="mb-10">
            <h2 className="text-3xl font-semibold text-gray-800 mb-4">
              1. Introdução
            </h2>
            <p className="text-gray-600">
              A ICAR reconhece a importância da privacidade e está
              comprometida em proteger e resguardar as informações pessoais de
              seus clientes e usuários. Em conformidade com a Lei Geral de
              Proteção de Dados (LGPD - Lei nº 13.709/2018), adotamos medidas
              rigorosas para garantir a segurança, privacidade e proteção dos
              dados pessoais coletados por meio de nossas plataformas e serviços.
              Nosso compromisso é ser transparente sobre como coletamos,
              utilizamos, armazenamos e compartilhamos suas informações pessoais,
              assegurando que seus dados sejam tratados de maneira responsável e
              em conformidade com a legislação brasileira.
            </p>
            <br />
            <p className="text-gray-600">
              Esta Política de Privacidade se aplica a todos os clientes e
              usuários de nossos serviços e reflete nosso compromisso em garantir
              que nossos colaboradores, parceiros e stakeholders compreendam e
              cumpram rigorosamente as diretrizes aqui estabelecidas.
            </p>
          </section>

          <section id="nosso_valores" className="mb-10">
            <h2 className="text-3xl font-semibold text-gray-800 mb-4">
              2. Nossos Valores
            </h2>
            <p className="text-gray-600">
              Valorizamos a transparência, o respeito à privacidade e a ética no
              tratamento de dados. Acreditamos que a confiança é fundamental para
              construir relacionamentos duradouros com nossos clientes e
              usuários. Por isso, nos esforçamos para garantir que seus dados
              pessoais sejam tratados com a máxima segurança e integridade,
              respeitando sempre os seus direitos.
            </p>
            <br />
            <p className="text-gray-600">
              Estamos comprometidos com a melhoria contínua dos nossos processos
              e com a implementação de práticas de privacidade eficazes, para que
              você tenha sempre a confiança de que seus dados estão seguros
              conosco.
            </p>
          </section>

          <section id="coleta_dados" className="mb-10">
            <h2 className="text-3xl font-semibold text-gray-800 mb-4">
              3. Por Que Coletamos Seus Dados
            </h2>
            <p className="text-gray-600">
              Coletamos seus dados pessoais para proporcionar uma experiência
              personalizada e eficiente ao usar nossos serviços. Além disso, seus
              dados são fundamentais para garantir a funcionalidade, melhorar a
              qualidade e fornecer suporte contínuo para os serviços oferecidos
              por nossa plataforma.
            </p>
            <br />
            <p className="text-gray-600">
              Ao coletar suas informações, também buscamos atender a requisitos
              legais, garantir a segurança de nossos serviços e, quando
              autorizado, enviar informações relevantes, como atualizações e
              promoções.
            </p>
          </section>

          <section id="informacoes" className="mb-10">
            <h2 className="text-3xl font-semibold text-gray-800 mb-4">
              4. Informações Coletadas
            </h2>
            <p className="text-gray-600">
              Coletamos as seguintes informações quando você se registra e usa
              nossos serviços:
            </p>
            <ul className="ml-5 list-none text-gray-600">
              <li><span className="mr-2 font-medium list-none text-gray-500">1.</span> Nome completo</li>
              <li><span className="mr-2 font-medium list-none text-gray-500">2.</span> E-mail</li>
              <li><span className="mr-2 font-medium list-none text-gray-500">3.</span> Telefone</li>
              <li><span className="mr-2 font-medium list-none text-gray-500">4.</span> Dados de pagamento</li>
              <li><span className="mr-2 font-medium list-none text-gray-500">5.</span> Informações sobre o dispositivo</li>
            </ul>
          </section>

          <section id="utilizacao" className="mb-10">
            <h2 className="text-3xl font-semibold text-gray-800 mb-4">
              5. Como Usamos Seus Dados
            </h2>
            <p className="text-gray-600">
              Seus dados pessoais são usados para melhorar sua experiência em
              nossos serviços, incluindo:
            </p>
            <ul className="list-none ml-5 text-gray-600">
              <li><span className="mr-2 font-medium list-none text-gray-500">1.</span> Personalização do serviço</li>
              <li><span className="mr-2 font-medium list-none text-gray-500">2.</span> Comunicação sobre promoções e novidades</li>
              <li><span className="mr-2 font-medium list-none text-gray-500">3.</span> Melhoria contínua dos nossos sistemas</li>
              <li><span className="mr-2 font-medium list-none text-gray-500">4.</span> Atendimento ao cliente e suporte</li>
            </ul>
          </section>

          <section id="terceiros" className="mb-10">
            <h2 className="text-3xl font-semibold text-gray-800 mb-4">
              6. Serviços de Terceiros
            </h2>
            <p className="text-gray-600">
              Podemos utilizar serviços de terceiros para ajudar a operar nosso
              site ou fornecer serviços em nosso nome, como processadores de
              pagamento ou prestadores de serviços de análise. Esses terceiros
              podem ter acesso a suas informações pessoais, mas são obrigados a
              não divulgar ou usar essas informações para qualquer outra
              finalidade.
            </p>
          </section>

          <section id="cookies" className="mb-10">
            <h2 className="text-3xl font-semibold text-gray-800 mb-4">
              7. Cookies
            </h2>
            <p className="text-gray-600">
              Utilizamos cookies para melhorar a sua experiência ao navegar em
              nosso site, para analisar o tráfego e personalizar o conteúdo que
              oferecemos. Você pode configurar seu navegador para recusar cookies,
              mas isso pode afetar a funcionalidade de algumas partes do site.
            </p>
          </section>

          <section id="seguranca" className="mb-10">
            <h2 className="text-3xl font-semibold text-gray-800 mb-4">
              8. Segurança
            </h2>
            <p className="text-gray-600">
              Tomamos medidas adequadas para proteger seus dados pessoais, como
              criptografia e controles de acesso. No entanto, nenhuma transmissão
              de dados pela internet ou método de armazenamento é 100% seguro.
            </p>
          </section>

          <section id="compartilhamento" className="mb-10">
            <h2 className="text-3xl font-semibold text-gray-800 mb-4">
              9. Compartilhamento de Dados
            </h2>
            <p className="text-gray-600">
              Não compartilhamos suas informações pessoais com terceiros, exceto
              quando necessário para cumprir obrigações legais ou com nossos
              parceiros de serviços.
            </p>
          </section>

          <section id="alteracoes" className="mb-10">
            <h2 className="text-3xl font-semibold text-gray-800 mb-4">
              10. Alterações na Política
            </h2>
            <p className="text-gray-600">
              Podemos atualizar esta política de tempos em tempos. Notificaremos
              sobre quaisquer alterações significativas por e-mail ou publicando
              um aviso em nosso site.
            </p>
          </section>

          <section id="direitos" className="mb-10">
            <h2 className="text-3xl font-semibold text-gray-800 mb-4">
              11. Seus Direitos
            </h2>
            <p className="text-gray-600">
              Você tem o direito de acessar, corrigir ou excluir suas informações
              pessoais. Se desejar exercer esses direitos, entre em contato conosco.
            </p>
          </section>

          <section id="termos" className="mb-10">
            <h2 className="text-3xl font-semibold text-gray-800 mb-4">
              12. Termos de Uso
            </h2>
            <p className="text-gray-600">
              Ao utilizar nossos serviços, você concorda com os termos e
              condições descritos nesta política de privacidade.
            </p>
          </section>

          <section id="contato" className="mb-10">
            <h2 className="text-3xl font-semibold text-gray-800 mb-4">
              13. Contato
            </h2>
            <p className="text-gray-600">
              Se tiver alguma dúvida sobre nossa Política de Privacidade, entre
              em contato conosco através do e-mail:{" "}
              <a href="mailto:support@icar.com" className="text-blue-600">
                support@icar.com
              </a>
            </p>
          </section>
        </div>
      </div>
    </section>
  );
};

export default PrivacyPolicy;

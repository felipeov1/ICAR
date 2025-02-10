import { useEffect } from 'react';
import { FaFacebook, FaTwitter, FaInstagram, FaLinkedin } from "react-icons/fa";

const Footer = () => {
  useEffect(() => {
    const handleScrollToSection = () => {

      const hash = window.location.hash;
      if (hash) {
        const section = document.getElementById(hash.replace('#', ''));
        if (section) {

          section.scrollIntoView({ behavior: 'smooth' });
        }
      }
    };


    handleScrollToSection();


    window.addEventListener('hashchange', handleScrollToSection);


    return () => {
      window.removeEventListener('hashchange', handleScrollToSection);
    };
  }, []);

  return (
    <footer className="bg-gray-900 text-white py-12">
      <div className="container mx-auto px-4">
        <div className="grid grid-cols-1 md:grid-cols-5 gap-8">
          <div>
            <img
              src="/src/images/icar-logo-transparent.png"
              className="mb-2"
              alt="logo-icar"
              width={150}
            />
            <p className="text-gray-400">
              Sua plataforma para agendamentos de limpezas automotivas
            </p>
          </div>
          <div>
            <h4 className="text-lg font-semibold mb-4">Navegação</h4>
            <div className="flex flex-row space-x-4">

              <ul className="text-gray-400">
                <li className="mb-2">
                  <a href="/#inicio" className="hover:text-white">
                    Início
                  </a>
                </li>
                <li className="mb-2">
                  <a href="/#funcionalidades" className="hover:text-white">
                    Funcionalidades
                  </a>
                </li>
                <li>
                  <a href="/#duvidas" className="hover:text-white">
                    Dúvidas
                  </a>
                </li>
              </ul>

              <ul className="text-gray-400">
                <li className="mb-2">
                  <a href="/#depoimentos" className="hover:text-white">
                    Depoimentos
                  </a>
                </li>
                <li className="mb-2">
                  <a href="/entrar" className="hover:text-white">
                    Entrar
                  </a>
                </li>
                <li>
                  <a href="/criar-conta" className="hover:text-white">
                    Criar Conta
                  </a>
                </li>
              </ul>
            </div>
          </div>
          <div>
            <h4 className="text-lg font-semibold mb-4">Contato</h4>
            <ul className="text-gray-400">
              <li className="mb-2">contato@icar.com</li>
              <li className="mb-2">(11) 99999-9999</li>
              <li>Londrina, PR</li>
            </ul>
          </div>
          <div>
            <h4 className="text-lg font-semibold mb-4">Links Úteis</h4>
            <ul className="text-gray-400">
              <li className="mb-2">
                <a href="#" className="hover:text-white"></a>
              </li>
              <li className="mb-2">
                <a href="#" className="hover:text-white">
                  Serviços
                </a>
              </li>
              <li className="mb-2">
                <a href="/nossas-politicas" className="hover:text-white">
                  Termos de Uso
                </a>
              </li>
              <li>
                <a href="#nossas-politicas" className="hover:text-white">
                  Privacidade
                </a>
              </li>
            </ul>
          </div>
          <div>
            <h4 className="text-lg font-semibold mb-4">Redes Sociais</h4>
            <div className="flex space-x-4">
              <a href="#" className="text-gray-400 hover:text-white">
                <FaInstagram size={24} />
              </a>
              <a href="#" className="text-gray-400 hover:text-white">
                <FaFacebook size={24} />
              </a>
              <a href="#" className="text-gray-400 hover:text-white">
                <FaTwitter size={24} />
              </a>
              <a href="#" className="text-gray-400 hover:text-white">
                <FaLinkedin size={24} />
              </a>
            </div>
          </div>
        </div>
        <div className="border-t border-gray-800 mt-8 pt-8 text-center text-gray-400">
          <p>
            &copy; {new Date().getFullYear()} ICAR. Todos os direitos
            reservados.
          </p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;

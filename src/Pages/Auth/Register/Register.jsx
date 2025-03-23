import React, { useState, useEffect } from "react";
import banner from "../../../public/assets/images/art/auth-banner.png";
import bannerMobile from "../../../public/assets/images/art/auth-banner-mobile.png";
import logo from "../../../public/assets/images/logo/icar-logo-transparent.png";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import header from "../../../components/layout/Navbar";

function CriarConta() {
  const [windowWidth, setWindowWidth] = useState(window.innerWidth);
  const [showPassword, setShowPassword] = useState(false);
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [passwordStrength, setPasswordStrength] = useState(0);
  const [hasMinLength, setHasMinLength] = useState(false);
  const [hasUpperCase, setHasUpperCase] = useState(false);
  const [hasNumber, setHasNumber] = useState(false);
  const [hasSpecialChar, setHasSpecialChar] = useState(false);
  const [phone, setPhone] = useState("");

  useEffect(() => {
    const handleResize = () => {
      setWindowWidth(window.innerWidth);
    };

    window.addEventListener("resize", handleResize);

    return () => {
      window.removeEventListener("resize", handleResize);
    };
  }, []);

  const checkPasswordStrength = (pwd) => {
    setHasMinLength(pwd.length >= 8);
    setHasUpperCase(/[A-Z]/.test(pwd));
    setHasNumber(/[0-9]/.test(pwd));
    setHasSpecialChar(/[^A-Za-z0-9]/.test(pwd));

    let strength = 0;
    if (pwd.length >= 8) strength += 1;
    if (/[A-Z]/.test(pwd)) strength += 1;
    if (/[0-9]/.test(pwd)) strength += 1;
    if (/[^A-Za-z0-9]/.test(pwd)) strength += 1;
    setPasswordStrength(strength);
  };

  const handlePasswordChange = (e) => {
    const newPassword = e.target.value;
    setPassword(newPassword);
    checkPasswordStrength(newPassword);
  };

  const getProgressColor = () => {
    switch (passwordStrength) {
      case 0:
        return "bg-red-500";
      case 1:
        return "bg-yellow-500";
      case 2:
      case 3:
        return "bg-blue-500";
      case 4:
        return "bg-green-500";
      default:
        return "bg-gray-300";
    }
  };

  const formatPhone = (value) => {
    const cleaned = value.replace(/\D/g, "");
    const limited = cleaned.slice(0, 11);

    if (limited.length > 10) {
      return `(${limited.slice(0, 2)}) ${limited.slice(2, 7)}-${limited.slice(7)}`;
    } else if (limited.length > 6) {
      return `(${limited.slice(0, 2)}) ${limited.slice(2, 6)}-${limited.slice(6)}`;
    } else if (limited.length > 2) {
      return `(${limited.slice(0, 2)}) ${limited.slice(2)}`;
    } else {
      return limited;
    }
  };

  const handlePhoneChange = (e) => {
    const formattedPhone = formatPhone(e.target.value);
    setPhone(formattedPhone);
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (password !== confirmPassword) {
      toast.error("As senhas não coincidem!", {
        autoClose: 2000,
      });
      return;
    }

    if (passwordStrength < 4) {
      toast.error("A senha não atende aos critérios de segurança!", {
        autoClose: 2000,
      });
      return;
    }

    toast.success("Cadastro realizado com sucesso!", {
      autoClose: 2000,
    });
  };

  return (
    <div className="h-full flex flex-col md:flex-row items-center justify-center">
      <div
        className="hidden lg:block w-1/2 h-screen"
        style={{
          backgroundImage: `url(${banner})`,
          backgroundSize: "cover",
          backgroundPosition: "center",
        }}
      ></div>
      <div
        className={
          windowWidth < 768
            ? "w-full h-full bg-cover bg-center flex flex-col  items-center p-4 "
            : "w-full lg:w-1/2 min-h-screen flex flex-col items-center lg:justify-center justify-between p-6"
        }
        style={{
          backgroundImage: windowWidth < 1024 ? `url(${bannerMobile})` : "none",
          backgroundColor: "white",
          
        }}
      >

        <div className="w-11/12 sm:max-w-md p-6 bg-white/90 backdrop-blur-sm rounded-lg flex flex-col items-center mt-28 md:mt-0">
          <ToastContainer />

          <div className="hidden sm:block mb-10">
            <h1 className="text-3xl font-bold text-blue-900">
              Faça Seu Cadastro na icar
            </h1>
          </div>

          <form className="space-y-4 w-full" onSubmit={handleSubmit}>
            <div>
              <label
                htmlFor="name"
                className="block text-sm font-medium text-blue-900"
              >
                Nome Completo
              </label>
              <input
                id="name"
                type="text"
                className="mt-1 block w-full px-4 py-2 border border-blue-900 bg-white text-blue-900 rounded-lg focus:ring-blue-900 focus:border-blue-900"
                placeholder="Digite seu nome completo"
                required
              />
            </div>

            <div>
              <label
                htmlFor="email"
                className="block text-sm font-medium text-blue-900"
              >
                Email
              </label>
              <input
                id="email"
                type="email"
                className="mt-1 block w-full px-4 py-2 border border-blue-900 bg-white text-blue-900 rounded-lg focus:ring-blue-900 focus:border-blue-900"
                placeholder="Digite seu email"
                required
              />
            </div>

            <div>
              <label
                htmlFor="phone"
                className="block text-sm font-medium text-blue-900"
              >
                Celular
              </label>
              <input
                type="text"
                id="phone"
                value={phone}
                onChange={handlePhoneChange}
                className="mt-1 block w-full px-4 py-2 border border-blue-900 bg-white text-blue-900 rounded-lg focus:ring-blue-900 focus:border-blue-900"
                placeholder="(DD) XXXXX-XXXX"
                maxLength={15}
              />
            </div>

            <div>
              <label
                htmlFor="password"
                className="block text-sm font-medium text-blue-900"
              >
                Senha
              </label>
              <div className="mb-3 p-3 mt-1 bg-gray-100 rounded-lg text-gray-700 text-sm">
                🔐 Sua senha deve conter:
                <ul className="list-disc ml-5 mt-1">
                  <li
                    className={
                      hasMinLength
                        ? "text-green-600 font-semibold"
                        : "text-red-500"
                    }
                  >
                    No mínimo <strong>8 caracteres</strong>
                  </li>
                  <li
                    className={
                      hasUpperCase
                        ? "text-green-600 font-semibold"
                        : "text-red-500"
                    }
                  >
                    Pelo menos <strong>uma letra maiúscula</strong>
                  </li>
                  <li
                    className={
                      hasNumber
                        ? "text-green-600 font-semibold"
                        : "text-red-500"
                    }
                  >
                    Pelo menos <strong>um número</strong>
                  </li>
                  <li
                    className={
                      hasSpecialChar
                        ? "text-green-600 font-semibold"
                        : "text-red-500"
                    }
                  >
                    Pelo menos <strong>um caractere especial (!@#$%^&*)</strong>
                  </li>
                </ul>
              </div>
            </div>
            <div>
              <div className="relative">
                <input
                  id="password"
                  type={showPassword ? "text" : "password"}
                  value={password}
                  onChange={handlePasswordChange}
                  className="mt-1 block w-full px-4 py-2 border border-blue-900 bg-white text-blue-900 rounded-lg focus:ring-blue-900 focus:border-blue-900 pr-10"
                  placeholder="Digite sua senha"
                  required
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute inset-y-0 right-3 flex items-center text-blue-900 bg-transparent border-none cursor-pointer"
                >
                  {showPassword ? "Ocultar" : "Mostrar"}
                </button>
              </div>

              <div className="w-full h-2 bg-gray-300 rounded-lg overflow-hidden mt-2">
                <div
                  className={`h-full ${getProgressColor()} transition-all duration-300`}
                  style={{ width: `${(passwordStrength / 4) * 100}%` }}
                ></div>
              </div>

              <p className="text-sm mt-1 text-orange-600">
                {password.length > 0 && (
                  <>
                    Força da senha:{" "}
                    <span className="font-semibold">
                      {passwordStrength === 0
                        ? "Muito Fraca"
                        : passwordStrength === 1
                        ? "Fraca"
                        : passwordStrength === 2 || passwordStrength === 3
                        ? "Média"
                        : "Forte"}
                    </span>
                  </>
                )}
              </p>
            </div>

            <div>
              <label
                htmlFor="re-password"
                className="block text-sm font-medium text-blue-900"
              >
                Confirme a Senha
              </label>
              <div className="relative">
                <input
                  id="re-password"
                  type={showPassword ? "text" : "password"}
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                  className="mt-1 block w-full px-4 py-2 border border-blue-900 bg-white text-blue-900 rounded-lg focus:ring-blue-900 focus:border-blue-900"
                  placeholder="Digite sua senha novamente"
                  required
                />
              </div>
            </div>

            <button
              type="submit"
              className="w-full py-2 px-4 bg-blue-900 text-white font-bold rounded-lg hover:bg-blue-950 focus:ring-2 focus:ring-blue-900"
            >
              Fazer Cadastro
            </button>

            <div className="flex items-center">
              <input
                id="remember"
                type="checkbox"
                className="h-4 w-4 bg-blue-900 border-gray-300 rounded focus:ring-blue-900"
                required
              />
              <label
                htmlFor="remember"
                className="ml-2 block text-sm text-blue-900"
              >
                Aceitar termos de uso e privacidade
              </label>
            </div>

            <div className="mb-10">
              <p className="font-light text-center text-blue-900">
                <a
                  className="underline"
                  href="/nossas-politicas"
                  target="_blank"
                >
                  Clique aqui para ver os termos de uso e privacidade
                </a>
              </p>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default CriarConta;
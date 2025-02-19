import React, { useState } from "react";
import { Link } from "react-router-dom";
import { FaArrowLeft, FaEye, FaEyeSlash } from "react-icons/fa";

const CredentialsEdit = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [passwordStrength, setPasswordStrength] = useState(0);
  const [showPassword, setShowPassword] = useState(false);

  const [hasMinLength, setHasMinLength] = useState(false);
  const [hasUpperCase, setHasUpperCase] = useState(false);
  const [hasNumber, setHasNumber] = useState(false);
  const [hasSpecialChar, setHasSpecialChar] = useState(false);

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

  const handleSave = () => {
    if (password !== confirmPassword) {
      alert("As senhas não coincidem!");
      return;
    }

    if (passwordStrength < 4) {
      alert("A senha não atende aos critérios de segurança!");
      return;
    }

    alert("Informações salvas com sucesso!");
  };

  return (
    <div className="flex flex-col p-6 max-w-md mx-auto">
      <div className="flex align-center justify-stretch">
        <span className="mb-6">
          <Link
            to="/icar/minha-conta"
            className="flex items-center text-gray-500"
          >
            <FaArrowLeft className="text-xl" />
          </Link>
        </span>

        <h2 className="text-xl font-medium mb-4 text-gray-700">
          Dados de Acesso
        </h2>
      </div>

      <form className="mt-4">
        <div className="mb-4">
          <label htmlFor="email" className="block text-gray-700">
            E-mail
          </label>
          <input
            type="email"
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="mt-2 mb-4 p-3 w-full border border-gray-300 rounded-lg"
            placeholder="Digite seu e-mail"
          />
        </div>

        <div className="mb-3 p-3 bg-gray-100 rounded-lg text-gray-700 text-sm">
          🔐 Sua senha deve conter:
          <ul className="list-disc ml-5 mt-1">
            <li
              className={
                hasMinLength ? "text-green-600 font-semibold" : "text-red-500"
              }
            >
              No mínimo <strong>8 caracteres</strong>
            </li>
            <li
              className={
                hasUpperCase ? "text-green-600 font-semibold" : "text-red-500"
              }
            >
              Pelo menos <strong>uma letra maiúscula</strong>
            </li>
            <li
              className={
                hasNumber ? "text-green-600 font-semibold" : "text-red-500"
              }
            >
              Pelo menos <strong>um número</strong>
            </li>
            <li
              className={
                hasSpecialChar ? "text-green-600 font-semibold" : "text-red-500"
              }
            >
              Pelo menos <strong>um caractere especial (!@#$%^&*)</strong>
            </li>
          </ul>
        </div>

        <div className="mb-6 relative">
          <label htmlFor="password" className="block text-gray-700">
            Nova Senha
          </label>
          <div className="flex relative">
              <input
                type={showPassword ? "text" : "password"}
                id="password"
                value={password}
                onChange={handlePasswordChange}
                className="mt-2 mb-2 p-3 w-full border border-gray-300 rounded-lg pr-10"
                placeholder="Digite sua nova senha"
              />
              <button
                type="button"
                className="absolute right-3 top-6 text-gray-600"
                onClick={() => setShowPassword(!showPassword)}
              >
                {showPassword ? <FaEyeSlash /> : <FaEye />}
              </button>
          </div>

          <div className="w-full h-2 bg-gray-300 rounded-lg overflow-hidden mt-2">
            <div
              className={`h-full ${getProgressColor()} transition-all duration-300`}
              style={{ width: `${(passwordStrength / 4) * 100}%` }}
            ></div>
          </div>

          <p className="text-sm mt-1 text-gray-600">
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

        <div className="mb-4">
          <label htmlFor="confirmPassword" className="block text-gray-700">
            Confirmar Senha
          </label>
          <input
            type={showPassword ? "text" : "password"}
            id="confirmPassword"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            className="mt-2 p-3 w-full border border-gray-300 rounded-lg"
            placeholder="Confirme sua nova senha"
          />
        </div>

        <button
          type="button"
          onClick={handleSave}
          className="w-full py-3 rounded-lg mt-6 text-white bg-orange-600 hover:bg-orange-700"
        >
          Salvar Alterações
        </button>
      </form>
    </div>
  );
};

export default CredentialsEdit;

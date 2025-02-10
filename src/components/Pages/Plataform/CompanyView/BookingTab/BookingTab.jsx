import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const BookingTab = () => {
  const [selectedDate, setSelectedDate] = useState(null);
  const [selectedTime, setSelectedTime] = useState("");
  const [currentMonth, setCurrentMonth] = useState(new Date().getMonth());
  const [currentYear, setCurrentYear] = useState(new Date().getFullYear());

  const navigate = useNavigate();

  const availableTimes = ["8:30", "10:20", "13:00", "15:40", "17:00"];
  const weekDays = ["Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"];

  const getMonthDays = (month, year) => {
    const date = new Date(year, month, 1);
    const days = [];
    while (date.getMonth() === month) {
      days.push(date.getDate());
      date.setDate(date.getDate() + 1);
    }
    return days;
  };

  const changeMonth = (increment) => {
    let newMonth = currentMonth + increment;
    let newYear = currentYear;

    if (newMonth > 11) {
      newMonth = 0;
      newYear++;
    } else if (newMonth < 0) {
      newMonth = 11;
      newYear--;
    }

    setCurrentMonth(newMonth);
    setCurrentYear(newYear);
    setSelectedDate(null);
  };

  const isDisabled = (day) => {
    const today = new Date();
    const currentDate = new Date(currentYear, currentMonth, day);
    return currentDate < today.setHours(0, 0, 0, 0);
  };

  const handleDateClick = (day) => {
    const newDate = new Date(currentYear, currentMonth, day);
    setSelectedDate(newDate);
    setSelectedTime("");
  };

  const handleNextStep = () => {
    if (selectedDate && selectedTime) {
      navigate("/icar/empresa/agendamento", {
        state: {
          selectedDate: selectedDate.toLocaleDateString("pt-BR"),
          selectedTime,
        },
      });
    }
  };

  const monthDays = getMonthDays(currentMonth, currentYear);

  return (
    <div className="p-4">
      <h3 className="text-lg font-semibold text-center mb-4">
        Selecione a Data e Horário
      </h3>

      <div className="flex items-center justify-between mb-4">
        <button
          className="text-blue-500 hover:underline"
          onClick={() => changeMonth(-1)}
        >
          &lt;
        </button>
        <span className="text-gray-700 font-medium">
          {new Date(currentYear, currentMonth).toLocaleString("pt-BR", {
            month: "long",
            year: "numeric",
          })}
        </span>
        <button
          className="text-blue-500 hover:underline"
          onClick={() => changeMonth(1)}
        >
          &gt;
        </button>
      </div>

      <div className="grid grid-cols-7 gap-2 mb-2">
        {weekDays.map((day) => (
          <div key={day} className="text-center font-sans text-[#8f8f8f62]">
            {day}
          </div>
        ))}
      </div>

      <div className="grid grid-cols-7 gap-2">
        {monthDays.map((day) => {
          const isSelected = selectedDate?.getDate() === day;
          const disabled = isDisabled(day);
          return (
            <button
              key={day}
              className={`p-2 rounded-2xl text-center transition duration-200 ease-in-out ${
                isSelected
                  ? "bg-[#1e3a8a] text-white"
                  : disabled
                  ? "bg-gray-200 text-gray-400 cursor-not-allowed"
                  : "bg-gray-200 hover:bg-[#1e3a8a] hover:text-white"
              }`}
              onClick={() => !disabled && handleDateClick(day)}
              disabled={disabled}
            >
              {day}
            </button>
          );
        })}
      </div>

      <p className="text-gray-700 font-semibold mt-4 text-left">
        Horários disponíveis para {selectedDate?.toLocaleDateString("pt-BR")}
      </p>

      <div className="flex flex-wrap gap-2 justify-start mt-4">
        {availableTimes.map((time) => (
          <button
            key={time}
            className={`px-4 py-2 rounded-xl border shadow-sm ${
              selectedTime === time ? "bg-[#1e3a8a] text-white" : "bg-gray-200"
            }`}
            onClick={() => setSelectedTime(time)}
          >
            {time}
          </button>
        ))}
      </div>

      <button
        className="mt-6 w-full py-3 bg-[#1e3a8a] text-white rounded-lg font-semibold hover:bg-blue-600"
        onClick={handleNextStep}
        disabled={!selectedDate || !selectedTime}
      >
        Próximo Passo →
      </button>
    </div>
  );
};

export default BookingTab;

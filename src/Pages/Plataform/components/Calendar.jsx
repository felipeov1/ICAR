import React, { useState, useEffect } from "react";

const Calendar = ({ onDateClick, selectedDate, dailyAvailableTimes }) => {
  const today = new Date();
  const currentYear = today.getFullYear();
  const currentMonth = today.getMonth();

  const [monthDays, setMonthDays] = useState([]);
  const [previousMonthDays, setPreviousMonthDays] = useState([]);
  const [nextMonthDays, setNextMonthDays] = useState([]);

  useEffect(() => {
    const getMonthDays = (month, year) => {
      const firstDayDate = new Date(year, month, 1);
      const firstDayOfWeek = firstDayDate.getDay(); // Primeiro dia da semana (0 = Domingo, 1 = Segunda...)
      
      const daysInCurrentMonth = new Date(year, month + 1, 0).getDate(); // Dias do mês atual
      const daysInPreviousMonth = new Date(year, month, 0).getDate(); // Dias do mês anterior
      
      // Criar os dias do mês atual
      const days = Array.from({ length: daysInCurrentMonth }, (_, i) => i + 1);

      // Criar os dias do mês anterior (desativados)
      const prevDays = Array.from({ length: firstDayOfWeek }, (_, i) => daysInPreviousMonth - firstDayOfWeek + i + 1);

      // Criar os dias do próximo mês (desativados)
      const remainingDays = 42 - (prevDays.length + days.length); // Total de células no calendário = 42 (6 semanas)
      const nextDays = Array.from({ length: remainingDays }, (_, i) => i + 1);

      setMonthDays(days);
      setPreviousMonthDays(prevDays);
      setNextMonthDays(nextDays);
    };

    getMonthDays(currentMonth, currentYear);
  }, [currentMonth, currentYear]);

  const isDayDisabled = (day) => {
    const selectedDate = new Date(currentYear, currentMonth, day);
    const todayMidnight = new Date();
    todayMidnight.setHours(0, 0, 0, 0);

    const dayOfWeek = selectedDate.getDay();
    const hasAvailableTimes = dailyAvailableTimes[dayOfWeek]?.length > 0;

    return selectedDate < todayMidnight || !hasAvailableTimes;
  };

  return (
    <div className="calendar-container">
      <div className="grid grid-cols-7 gap-2 mb-2">
        {["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"].map((day, index) => (
          <div key={index} className="text-center font-sans text-[#8f8f8f62]">
            {day}
          </div>
        ))}
      </div>
      <div className="grid grid-cols-7 gap-2">
        {/* Dias do mês anterior (desativados) */}
        {previousMonthDays.map((day, index) => (
          <button
            key={`prev-${day}-${index}`}
            className="p-2 rounded-2xl text-center bg-gray-100 text-gray-400 cursor-not-allowed"
            disabled
          >
            {day}
          </button>
        ))}

        {/* Dias do mês atual */}
        {monthDays.map((day, index) => {
          const currentDayOfWeek = new Date(currentYear, currentMonth, day).getDay();
          const isSelected = selectedDate?.getDate() === day;
          const disabled = isDayDisabled(day) || dailyAvailableTimes[currentDayOfWeek]?.length === 0;

          return (
            <button
              key={`${day}-${currentMonth}-${currentYear}-${index}`}
              className={`p-2 rounded-2xl text-center transition duration-200 ease-in-out ${
                isSelected
                  ? "bg-[#1e3a8a] text-white"
                  : disabled
                  ? "bg-gray-200 text-gray-400 cursor-not-allowed"
                  : "bg-gray-200 hover:bg-[#1e3a8a] hover:text-white"
              }`}
              onClick={() => !disabled && onDateClick(day)}
              disabled={disabled}
            >
              {day}
            </button>
          );
        })}

        {/* Dias do próximo mês (desativados) */}
        {nextMonthDays.map((day, index) => (
          <button
            key={`next-${day}-${index}`}
            className="p-2 rounded-2xl text-center bg-gray-100 text-gray-400 cursor-not-allowed"
            disabled
          >
            {day}
          </button>
        ))}
      </div>
    </div>
  );
};

export default Calendar;

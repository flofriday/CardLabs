"use client";
import { ChangeEvent, KeyboardEvent, useState } from "react";

interface Props {
  totalRoundNumber: number;
}

export default function Slider({ totalRoundNumber }: Props): JSX.Element {
  const [sliderValue, setSliderValue] = useState(1);
  const [inputText, setInputText] = useState(sliderValue.toString());

  const handleSliderChange = (event: ChangeEvent<HTMLInputElement>) => {
    setSliderValue(parseInt(event.target.value, 10));
    setInputText(event.target.value);
  };

  const handleIncrement = () => {
    setSliderValue((prevValue) => {
      const newValue = Math.min(prevValue + 1, totalRoundNumber);
      setInputText(newValue.toString());
      return newValue;
    });
  };

  const handleDecrement = () => {
    setSliderValue((prevValue) => {
      const newValue = Math.max(prevValue - 1, 1);
      setInputText(newValue.toString());
      return newValue;
    });
  };

  const handleInputChange = (event: ChangeEvent<HTMLInputElement>) => {
    setInputText(event.target.value);
  };

  const handleInputKeyPress = (event: KeyboardEvent<HTMLInputElement>) => {
    if (event.key === "Enter") {
      const inputValue = parseInt(inputText, 10);
      if (
        !isNaN(inputValue) &&
        inputValue >= 1 &&
        inputValue <= totalRoundNumber
      ) {
        setSliderValue(inputValue);
      }
    }
  };

  return (
    <div>
      <div className="flex items-center mb-4">
        <label
          htmlFor="slider-input"
          className="mr-2 text-text text-4xl font-bold"
        >
          Round:
        </label>
        <input
          id="slider-input"
          type="text"
          value={inputText}
          onChange={handleInputChange}
          onKeyPress={handleInputKeyPress}
          className="px-4 py-3 w-14 h-14 text-4xl bg-secondary text-text rounded-md hover:bg-primary hover:text-white"
        />
      </div>

      <div className="flex items-center mb-4">
        <button
          className="px-4 py-3 w-14 h-14 text-4xl bg-secondary text-text rounded-md hover:bg-primary hover:text-white flex items-center justify-center mr-1"
          onClick={handleDecrement}
        >
          -
        </button>

        <input
          id="steps-range"
          type="range"
          min="1"
          max={totalRoundNumber.toString()}
          value={sliderValue}
          step="1"
          onChange={handleSliderChange}
          className="w-full h-2 bg-secondary rounded-lg appearance-none cursor-pointer dark:bg-gray-700"
        />

        <button
          className="px-4 py-3 w-14 h-14 text-4xl bg-secondary text-text rounded-md hover:bg-primary hover:text-white flex items-center justify-center ml-1"
          onClick={handleIncrement}
        >
          +
        </button>
      </div>
    </div>
  );
}

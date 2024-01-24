"use client";
import { ChangeEvent, KeyboardEvent, useEffect, useState } from "react";

interface Props {
  totalRoundNumber: number;
  onChange: (round: number) => void;
}

export default function Slider({
  totalRoundNumber,
  onChange,
}: Props): JSX.Element {
  const [sliderValue, setSliderValue] = useState(1);
  const [inputText, setInputText] = useState(sliderValue.toString());

  useEffect(() => {
    onChange(sliderValue);
  }, [sliderValue, onChange]);

  const handleSliderChange = (event: ChangeEvent<HTMLInputElement>): void => {
    setSliderValue(parseInt(event.target.value, 10));
    setInputText(event.target.value);
  };

  const handleIncrement = (): void => {
    setSliderValue((prevValue) => {
      const newValue = Math.min(prevValue + 1, totalRoundNumber);
      setInputText(newValue.toString());
      return newValue;
    });
  };

  const handleDecrement = (): void => {
    setSliderValue((prevValue) => {
      const newValue = Math.max(prevValue - 1, 1);
      setInputText(newValue.toString());
      return newValue;
    });
  };

  const handleInputChange = (event: ChangeEvent<HTMLInputElement>): void => {
    setInputText(event.target.value);
  };

  const handleInputKeyPress = (
    event: KeyboardEvent<HTMLInputElement>
  ): void => {
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
    <div className="flex w-full items-center space-x-5">
      <div className="flex items-center">
        <label
          htmlFor="slider-input"
          className="mr-2 text-text text-4xl font-bold"
        >
          Turn:
        </label>
        <input
          id="slider-input"
          type="text"
          value={inputText}
          onChange={handleInputChange}
          onKeyUp={handleInputKeyPress}
          className="px-4 py-3 w-28 h-14 text-center text-4xl bg-text text-primary rounded-md hover:bg-primary hover:text-text"
        />
      </div>

      <div className="flex w-full items-center">
        <button
          className="px-4 py-3 h-14 text-4xl bg-secondary text-text rounded-md hover:bg-primary hover:text-white flex items-center justify-center mr-1"
          onClick={handleDecrement}
        >
          &lt;
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
          &gt;
        </button>
      </div>
    </div>
  );
}

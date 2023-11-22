"use client";

import { useState, type ChangeEvent, useRef } from "react";
import { RiArrowDropDownLine } from "react-icons/ri";

interface Props {
  values: string[];
  className?: string;
  defaultValue?: string;
}

export default function DropDown({
  values = [],
  className = "w-fit",
  defaultValue = "",
}: Props): JSX.Element {
  const [dropDownVisability, setDropDownVisability] = useState(false);
  const dropDownMenu = useRef(null);
  const [currentItems, setCurrentItems] = useState(values);
  const [value, setValue] = useState(defaultValue);

  const test = (e: ChangeEvent<HTMLInputElement>): void => {
    const searchTerm = e.target.value.toLowerCase();

    if (dropDownMenu.current == null) {
      return;
    }
    const newItems: string[] = [];
    values.forEach((item) => {
      const text = item.toLowerCase();
      if (text.includes(searchTerm)) {
        newItems.push(text);
      }
    });
    setCurrentItems(newItems);
    console.log(currentItems);
  };

  return (
    <div className={`relative group ${className}`}>
      <button
        type="button"
        id="dropdown-button"
        onClick={() => {
          setDropDownVisability(!dropDownVisability);
        }}
        className="btn bg-primary text-text p-2 w-full rounded-lg shadow-md text-lg inline-flex justify-center items-center w-full"
      >
        <div className="flex justify-between w-full">
          <div className="ml-2">{value}</div>

          <div className="overflow-hidden w-7 h-5 relative">
            <div className="absolute -top-1.5">
              <RiArrowDropDownLine size={35} style={{ fill: "#FEF9EC" }} />
            </div>
          </div>
        </div>
      </button>
      <div
        id="dropdown-menu"
        ref={dropDownMenu}
        className={`absolute text-xl bg-primary left-0 mt-1 w-full rounded-md shadow-lg ring-1 ring-black ring-opacity-5 p-1 space-y-1 ${
          dropDownVisability ? "" : "hidden"
        }`}
      >
        <input
          id="search-input"
          className="block text-primary w-full px-4 py-2 border rounded-md "
          type="text"
          placeholder="Search items"
          autoComplete="off"
          onInput={test}
        />
        {currentItems.map((item, index) => (
          <a
            key={index}
            className="block hover:underline px-4 py-2 text-gray-700 hover:bg-gray-100 active:bg-blue-100 cursor-pointer rounded-md"
            onClick={() => {
              setValue(item);
              setDropDownVisability(false);
            }}
          >
            {item}
          </a>
        ))}
      </div>
    </div>
  );
}

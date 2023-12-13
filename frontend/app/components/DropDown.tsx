"use client";

import { useState, type ChangeEvent, useRef, useEffect } from "react";
import { RiArrowDropDownLine } from "react-icons/ri";

interface Props {
  values: string[];
  className?: string;
  defaultValue?: string;
  onChange?: (value: string) => void;
}

function useClickOutside(ref: any, onClickOutside: () => void): void {
  useEffect(() => {
    /**
     * Invoke Function onClick outside of element
     */
    function handleClickOutside(event: MouseEvent): void {
      // eslint-disable-next-line @typescript-eslint/strict-boolean-expressions
      if (ref.current && !ref.current.contains(event.target)) {
        onClickOutside();
      }
    }
    // Bind
    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      // dispose
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [ref, onClickOutside]);
}

export default function DropDown({
  values = [],
  className = "w-fit",
  defaultValue = "",
  onChange = () => {},
}: Props): JSX.Element {
  const [dropDownVisability, setDropDownVisability] = useState(false);
  const dropDownMenuButton = useRef(null);
  const dropDownMenu = useRef(null);
  const [currentItems, setCurrentItems] = useState(values);
  const [value, setValue] = useState(defaultValue);

  useEffect(() => {
    setCurrentItems(values);
  }, [values]);

  useEffect(() => {
    setValue(defaultValue);
  }, [defaultValue]);

  useClickOutside(dropDownMenu, () => {
    setDropDownVisability(false);
  });

  const toggleDropDown = (e: ChangeEvent<HTMLInputElement>): void => {
    const searchTerm = e.target.value.toLowerCase();

    if (dropDownMenuButton.current == null) {
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
    <div className={`relative group ${className}`} ref={dropDownMenu}>
      <button
        type="button"
        id="dropdown-button"
        onClick={() => {
          setDropDownVisability(!dropDownVisability);
        }}
        className="btn bg-primary text-text p-2 w-full h-full rounded-lg shadow-md text-lg inline-flex justify-center items-center"
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
        ref={dropDownMenuButton}
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
          onInput={toggleDropDown}
        />
        {currentItems.map((item, index) => (
          <a
            key={index}
            className="block hover:underline px-4 py-2 text-gray-700 hover:bg-gray-100 active:bg-blue-100 cursor-pointer rounded-md"
            onClick={() => {
              setValue(item);
              onChange(item);
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

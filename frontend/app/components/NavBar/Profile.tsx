"use client";
import Image from "next/image";
import { useState, useRef, useEffect } from "react";
import NavDropDown from "./NavDropDown";

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

export default function Profile(): JSX.Element {
  const [dropdownVisability, setDropdownVisability] = useState(false);
  const dropDownMenu = useRef(null);

  useClickOutside(dropDownMenu, () => {
    setDropdownVisability(false);
  });

  return (
    <>
      <div
        className="w-11"
        onClick={() => {
          setDropdownVisability(!dropdownVisability);
        }}
      >
        <Image
          src="/example_profile_pic.jpg"
          alt="Profile Image"
          className="w-11 shadow rounded-full max-w-full h-auto align-middle border-none"
          width={100}
          height={100}
        />
      </div>
      <div
        className={`transition-all ${
          dropdownVisability ? "opacity-100" : "opacity-0"
        }`}
        ref={dropDownMenu}
      >
        {dropdownVisability && (
          <NavDropDown
            close={() => {
              setDropdownVisability(false);
            }}
          />
        )}
      </div>
    </>
  );
}

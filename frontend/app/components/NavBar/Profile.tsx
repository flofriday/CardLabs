"use client";
import Image from "next/image";
import { useState } from "react";
import NavDropDown from "./NavDropDown";

export default function Profile(): JSX.Element {
  const [dropdownVisability, setDropdownVisability] = useState(false);
  const [closedByDropDown, setClosedByDropDown] = useState(false);

  return (
    <>
      <div
        className="w-11"
        onClick={() => {
          if (closedByDropDown) {
            setClosedByDropDown(false);
            return;
          }
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
      >
        {dropdownVisability && (
          <NavDropDown
            close={() => {
              setClosedByDropDown(true);
              setDropdownVisability(false);
            }}
          />
        )}
      </div>
    </>
  );
}

"use client";
"use client";
import Image from "next/image";
import { useEffect, useState } from "react";
import NavDropDown from "./NavDropDown";
import { getUserProfilePicture } from "@/app/services/UserService";
import { getCookie } from "cookies-next";

export default function Profile(): JSX.Element {
  const [dropdownVisibility, setDropdownVisibility] = useState(false);
  const [profilePic, setProfilePic] = useState("");
  useEffect(() => {
    const jwt = getCookie("auth_token") as string;
    if (jwt === undefined) {
      return;
    }
    const pic = getUserProfilePicture(jwt);
    setProfilePic(pic);
  }, [profilePic]);

  if (profilePic === "") {
    return <></>;
  }

  return (
    <>
      <div
        className="w-11"
        id="profile_pic_in_navbar"
        onClick={() => {
          setDropdownVisibility(!dropdownVisibility);
        }}
      >
        <Image
          src={profilePic}
          alt="Profile Image"
          className="w-11 shadow rounded-full max-w-full h-auto align-middle border-none bg-text"
          width={100}
          height={100}
          loading="lazy"
        />
      </div>
      <div
        className={`transition-all ${
          dropdownVisibility ? "opacity-100" : "opacity-0"
        }`}
      >
        {dropdownVisibility && (
          <NavDropDown
            close={() => {
              setDropdownVisibility(false);
            }}
          />
        )}
      </div>
    </>
  );
}

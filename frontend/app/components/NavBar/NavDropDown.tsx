import { deleteCookie } from "cookies-next";
import { useRouter } from "next/navigation";
import { useEffect, useRef } from "react";
import { looseUnsavedChanges } from "@/app/services/NavigationService";
import { authenticationStore } from "@/app/state/authenticationStore";
import { useSaveCodeStore } from "@/app/state/savedCodeStore";

interface Props {
  className?: string;
  close?: () => void;
}

function useClickOutside(ref: any, onClickOutside: () => void): void {
  useEffect(() => {
    function handleClickOutside(event: MouseEvent): void {
      // eslint-disable-next-line @typescript-eslint/strict-boolean-expressions
      if (ref.current && !ref.current.contains(event.target)) {
        onClickOutside();
      }
    }
    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [ref, onClickOutside]);
}

function onLogout(router: any): void {
  deleteCookie("auth_token");
  localStorage.removeItem("auth_token_expire");
  localStorage.removeItem("refresh_token");
  router.refresh();
  router.replace("/");
}

export default function NavDropDown({
  className = "",
  close = () => {},
}: Props): JSX.Element {
  const router = useRouter();

  const dropDownMenu = useRef(null);

  const codeSaved: boolean = useSaveCodeStore((state: any) => state.codeSaved);
  const setCodeSaved = useSaveCodeStore((state: any) => state.setCodeSaved);
  const setAuthenticated = authenticationStore(
    (state: any) => state.setAuthenticated
  );

  useClickOutside(dropDownMenu, () => {
    close();
  });

  return (
    <div
      className={`fixed right-8 top-14 bg-primary w-56 p-3 rounded-md border-2 ${className}`}
      ref={dropDownMenu}
    >
      <div className="flex flex-col space-y-3">
        <button
          className="btn bg-secondary text-text py-2 w-48 rounded-lg shadow-md text-lg"
          onClick={(e) => {
            if (!looseUnsavedChanges(e, codeSaved, setCodeSaved)) {
              return;
            }
            close();
            router.replace("/settings");
          }}
          id="settings_button_navbar"
        >
          Settings
        </button>
        <button
          className="btn bg-accent text-text py-2 w-48 rounded-lg shadow-md text-lg"
          onClick={(e) => {
            if (!looseUnsavedChanges(e, codeSaved, setCodeSaved)) {
              return;
            }
            close();
            setAuthenticated(false);
            onLogout(router);
          }}
          id="logout_button_navbar"
        >
          Logout
        </button>
      </div>
    </div>
  );
}

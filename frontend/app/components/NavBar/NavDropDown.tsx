import { deleteCookie } from "cookies-next";
import { useRouter } from "next/navigation";
import { useEffect, useRef } from "react";
interface Props {
  className?: string;
  close?: () => void;
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

function onLogout(router: any): void {
  deleteCookie("auth_token");
  router.refresh();
  router.replace("/");
}

export default function NavDropDown({
  className = "",
  close = () => {},
}: Props): JSX.Element {
  const router = useRouter();

  const dropDownMenu = useRef(null);

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
          className="btn bg-accent text-text py-2 w-48 rounded-lg shadow-md text-lg"
          onClick={() => {
            onLogout(router);
          }}
        >
          Logout
        </button>
        <button
          className="btn bg-secondary text-text py-2 w-48 rounded-lg shadow-md text-lg"
          onClick={() => {
            router.replace("/settings");
            close();
          }}
        >
          Settings
        </button>
      </div>
    </div>
  );
}

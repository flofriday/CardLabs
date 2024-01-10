"use client";
import { IoClose } from "react-icons/io5";
import { useRef, useState, useEffect } from "react";

interface Props {
  children: React.ReactNode;
  title: string;
  modalClassName?: string;
  onClose: () => void;
}

export default function Modal({
  children,
  onClose,
  title,
  modalClassName,
}: Props): JSX.Element {
  const parentDiv = useRef(null);
  const [showModal, setShowModal] = useState(false);
  const animation = "transition-opacity ease-in-out duration-200";
  useEffect(() => {
    setShowModal(true);
  }, []);

  function handleParentClick(e: React.MouseEvent<HTMLInputElement>): void {
    if (e.target === parentDiv.current) {
      onClose();
    }
  }

  return (
    <div>
      <div
        className={`absolute z-40 top-0 left-0 w-screen h-screen bg-text ${animation} ${
          showModal ? "opacity-40" : "opacity-0"
        }`}
      />
      <div
        className="absolute z-50 top-0 left-0 w-screen h-screen flex"
        onClick={handleParentClick}
        ref={parentDiv}
      >
        <div
          className={`bg-primary m-auto p-8 rounded-xl ${animation}
          ${showModal ? "opacity-100" : "opacity-0"}  ${modalClassName}`}
        >
          <div className="h-full w-full relative">
            <h1 className="text-3xl mb-4">{title}</h1>
            <button className="absolute -right-3 -top-3" onClick={onClose}>
              <IoClose size={40} style={{ fill: "#87153A" }} />
            </button>

            {children}
          </div>
        </div>
      </div>
    </div>
  );
}

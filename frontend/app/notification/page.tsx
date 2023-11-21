"use client";
import { toast } from "react-toastify";

export default function Login(): JSX.Element {
  return (
    <div className="flex h-screen">
      <div className="m-auto flex-col flex space-y-6">
        <button
          className="btn bg-primary text-text py-2 w-48 rounded-lg shadow-md text-lg"
          onClick={() => {
            toast("This is a default message");
          }}
        >
          Default
        </button>
        <button
          className="btn bg-success text-text py-2 w-48 rounded-lg shadow-md text-lg"
          onClick={() => {
            toast.success("This is a success message");
          }}
        >
          Success
        </button>
        <button
          className="btn bg-error text-text py-2 w-48 rounded-lg shadow-md text-lg"
          onClick={() => {
            toast.error("This is a error message");
          }}
        >
          Error
        </button>
        <button
          className="btn bg-warning text-text py-2 w-48 rounded-lg shadow-md text-lg"
          onClick={() => {
            toast.warn("This is a warn message");
          }}
        >
          Warning
        </button>
        <button
          className="btn bg-primary text-text py-2 w-48 rounded-lg shadow-md text-lg"
          onClick={() => {
            toast.info("This is a info message");
          }}
        >
          Info
        </button>
      </div>
    </div>
  );
}

"use client";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const background = {
  success: "bg-text",
  error: "bg-text",
  info: "bg-text",
  warning: "bg-text",
  default: "bg-text",
};

const progressBar = {
  success: "bg-success",
  error: "bg-error",
  info: "bg-primary",
  warning: "bg-warning",
  default: "bg-primary",
};

type background_ = keyof typeof background;
type progressBar_ = keyof typeof progressBar;

export default function Notifications(): JSX.Element {
  return (
    <ToastContainer
      toastClassName={({ type }: { type: background_ }) =>
        // eslint-disable-next-line @typescript-eslint/strict-boolean-expressions
        background[type || "default"] +
        " relative flex p-1 min-h-10 rounded-md justify-between overflow-hidden cursor-pointer"
      }
      progressClassName={({ type }: { type: progressBar_ }) =>
        // eslint-disable-next-line @typescript-eslint/strict-boolean-expressions
        progressBar[type || "default"] +
        " Toastify__progress-bar Toastify__progress-bar--animated"
      }
      bodyClassName={() => "text-sm text-primary font-med block p-3"}
      position="top-center"
      autoClose={5000}
      hideProgressBar={false}
      newestOnTop={false}
      closeOnClick
      rtl={false}
      pauseOnFocusLoss
      draggable
      pauseOnHover
      theme="dark"
    />
  );
}

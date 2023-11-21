"use client";

import { useRouter } from "next/navigation";
import { login, register } from "../services/UserService";
import { toast } from "react-toastify";

function loginSuccess(router: any): void {
  console.log("redirect?");
  router.refresh();
  router.replace("/"); // change this to /dashboard
  toast.success("Registration was successful");
}

function registerHandler(e: React.SyntheticEvent, router: any): void {
  e.preventDefault();
  const target = e.target as typeof e.target & {
    username: { value: string };
    password: { value: string };
    email: { value: string };
  };
  const username = target.username.value;
  const password = target.password.value;
  const email = target.email.value;

  register(username, email, password)
    .then(async (status: boolean) => {
      if (status) {
        if (await login(username, password)) {
          loginSuccess(router);
        }
      }
    })
    .catch(() => {});
}

export default function RegisterForm(): JSX.Element {
  const router = useRouter();

  return (
    <div className="w-full h-full">
      <form
        className="m-12 text-2xl xl:text-4xl font-regular"
        onSubmit={(e) => {
          registerHandler(e, router);
        }}
      >
        <div className="flex justify-between">
          <div className="flex flex-col space-y-7 xl:space-y-6 h-full">
            <label htmlFor="username">Username:</label>
            <label htmlFor="email">E-Mail:</label>
            <label htmlFor="password">Password:</label>
          </div>
          <div className="flex flex-col space-y-4 w-full ml-8">
            <input
              id="username"
              name="username"
              type="text"
              required
              className="max-xl:h-10 bg-text border border-secondary front-bold text-primary text-lg rounded-lg focus:ring-primary focus:border-primary block p-2.5 w-full"
            />
            <input
              id="email"
              name="email"
              type="email"
              required
              className="max-xl:h-10 bg-text border border-secondary front-bold text-primary text-lg rounded-lg focus:ring-primary focus:border-primary block p-2.5 w-full"
            />
            <input
              id="password"
              name="password"
              required
              type="password"
              className="max-xl:h-10 bg-text border border-secondary front-bold text-primary text-lg rounded-lg focus:ring-primary focus:border-primary block p-2.5 w-full"
            />
          </div>
        </div>
        <div className="flex justify-around pt-4 mt-10">
          <button className="btn bg-primary text-text py-2 w-48 rounded-lg shadow-md text-lg">
            Register
          </button>
        </div>
      </form>
    </div>
  );
}

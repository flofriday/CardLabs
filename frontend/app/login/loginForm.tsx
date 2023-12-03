"use client";

import { useRouter } from "next/navigation";
import { login } from "../services/UserService";

function loginSuccess(router: any): void {
  console.log("redirect?");
  router.refresh();
  router.replace("/dashboard");
}

function loginHandler(e: React.SyntheticEvent, router: any): void {
  e.preventDefault();
  const target = e.target as typeof e.target & {
    username: { value: string };
    password: { value: string };
  };
  const username = target.username.value;
  const password = target.password.value;

  login(username, password)
    .then((success) => {
      if (success) {
        loginSuccess(router);
      }
    })
    .catch(() => {});
}

export default function LoginForm(): JSX.Element {
  const router = useRouter();

  return (
    <div className="w-full h-full">
      <form
        className="m-12 text-2xl xl:text-4xl font-regular"
        onSubmit={(e) => {
          loginHandler(e, router);
        }}
      >
        <div className="flex justify-between">
          <div className="flex flex-col space-y-7 xl:space-y-6 h-full">
            <label htmlFor="username">Username:</label>
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
              id="password"
              name="password"
              type="password"
              required
              className="max-xl:h-10 bg-text border border-secondary front-bold text-primary text-lg rounded-lg focus:ring-primary focus:border-primary block p-2.5 w-full"
            />
          </div>
        </div>
        <div className="flex justify-around pt-4 mt-10">
          <button
            className="btn bg-primary text-text py-2 w-48 rounded-lg shadow-md text-lg"
            id="login_button"
          >
            Login
          </button>
        </div>
      </form>
    </div>
  );
}

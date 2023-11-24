"use client";

import DropDown from "../components/DropDown";
import { useRouter } from "next/navigation";
import { login, register } from "../services/UserService";
import { toast } from "react-toastify";
import { useEffect, useState } from "react";
import { getLocations } from "../services/LocationService";

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
    sendScoreUpdate: { checked: boolean };
    sendWebsiteUpdate: { checked: boolean };
    sendNewsletter: { checked: boolean };
  };

  const username = target.username.value;
  const password = target.password.value;
  const email = target.email.value;
  const sendScoreUpdate = target.sendScoreUpdate.checked;
  const sendWebsiteUpdate = target.sendWebsiteUpdate.checked;
  const sendNewsletter = target.sendNewsletter.checked;

  register(
    username,
    email,
    password,
    location,
    sendScoreUpdate,
    sendWebsiteUpdate,
    sendNewsletter
  )
    .then(async (status: boolean) => {
      if (status) {
        if (await login(username, password)) {
          loginSuccess(router);
        }
      }
    })
    .catch(() => {});
}

let location: string | null = null;
const locationNotSet = "Unkown / Not set";
function setLocation(loc: string): void {
  if (loc === locationNotSet) {
    location = null;
  } else {
    location = loc;
  }
}

export default function RegisterForm(): JSX.Element {
  const router = useRouter();

  const [locations, setLocations] = useState<string[]>([]);
  useEffect(() => {
    getLocations()
      .then((l) => {
        l.unshift(locationNotSet);
        setLocations(l);
      })
      .catch(() => {});
  }, []);

  return (
    <div className="w-full h-full">
      <form
        className="m-12 text-2xl xl:text-4xl font-regular"
        onSubmit={(e) => {
          registerHandler(e, router);
        }}
      >
        <div className="flex justify-between">
          <div className="flex flex-col space-y-7 xl:space-y-7 h-full">
            <label htmlFor="username">Username:</label>
            <label htmlFor="email">E-Mail:</label>
            <label>Location:</label>
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
            <DropDown
              values={locations}
              className="w-full"
              defaultValue="Choose location"
              onChange={setLocation}
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
        <div className="ml-10 xl:ml-24 text-xl xl:text-2xl pt-3 mt-4">
          <div className="flex space-x-3 align-middle">
            <input
              type="checkbox"
              id="sendScoreUpdate"
              name="sendScoreUpdate"
            />
            <label htmlFor="sendScoreUpdate">
              Send score update notifications
            </label>
          </div>
          <div className="flex space-x-3 align-middle">
            <input
              type="checkbox"
              id="sendWebsiteUpdate"
              name="sendWebsiteUpdate"
            />
            <label htmlFor="sendWebsiteUpdate">
              Send website update and changelog notifications
            </label>
          </div>
          <div className="flex space-x-3 align-middle">
            <input type="checkbox" id="sendNewsletter" name="sendNewsletter" />
            <label htmlFor="sendNewsletter">Send newsletter</label>
          </div>
        </div>
        <div className="flex justify-around pt-4 mt-5">
          <button
            className="btn bg-primary text-text py-2 w-48 rounded-lg shadow-md text-lg"
            id="register_button"
          >
            Register
          </button>
        </div>
      </form>
    </div>
  );
}

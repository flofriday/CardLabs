import { toast } from "react-toastify";
import { getCookie, setCookie } from "cookies-next";
import { minidenticon } from "minidenticons";
import { decodeJwt } from "jose";
// this function returns the jwt on success and null on failure
export async function login(
  username: string,
  password: string
): Promise<boolean> {
  const response = await fetch("api/authentication/login", {
    mode: "cors",
    method: "POST",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ username, password }),
  });

  if (response.status === 401) {
    toast.error("Wrong username or password");
    return false;
  } else if (response.status === 500) {
    toast.error("Login is currently not possible! We are working on it");
    return false;
  } else if (response.status !== 200) {
    toast.error("Other error: " + response.status);
    return false;
  }
  const json = await response.json();
  setCookie("auth_token", json.jwt);

  return true;
}

export async function register(
  username: string,
  email: string,
  password: string,
  location: string | null,
  sendScoreUpdates: boolean,
  sendChangeUpdates: boolean,
  sendNewsletter: boolean
): Promise<boolean> {
  const response = await fetch("api/account", {
    mode: "cors",
    method: "POST",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      username,
      email,
      password,
      location,
      sendScoreUpdates,
      sendChangeUpdates,
      sendNewsletter,
    }),
  });

  if (response.status === 201) {
    return true;
  } else if (response.status === 409) {
    toast.error("User account with this username or email exists already");
    return false;
  } else if (response.status === 500) {
    toast.error("Registration is currently not possible! We are working on it");
    return false;
  } else {
    toast.error("Invalid response on register: " + response.status);
    return false;
  }
}

export interface User {
  id: number;
  username: string;
  password: string;
  email: string;
  location: string | null;
  sendScoreUpdates: boolean;
  sendChangeUpdates: boolean;
  sendNewsletter: boolean;
}

export async function getUserInfo(): Promise<User> {
  const jwt = getCookie("auth_token");

  const response = await fetch("api/account", {
    mode: "cors",
    method: "GET",
    headers: {
      Accept: "application/json",
      Authorization: "Bearer " + jwt,
    },
  });

  const user = (await response.json()) as User;

  return user;
}

export function getUserProfilePicture(jwt: string): string {
  let username;
  if (jwt === undefined) {
    username = "Placeholder";
  } else {
    const payload = decodeJwt(jwt);
    username = payload.account_username as string;
  }

  const saturation = 100;
  const lightness = 50;
  const data =
    "data:image/svg+xml;utf8," +
    encodeURIComponent(minidenticon(username, saturation, lightness));

  return data;
}

export async function updateUser(
  location: string | null,
  sendScoreUpdates: boolean,
  sendChangeUpdates: boolean,
  sendNewsletter: boolean
): Promise<boolean> {
  const jwt = getCookie("auth_token");

  const response = await fetch("api/account", {
    mode: "cors",
    method: "PATCH",
    headers: {
      Accept: "application/json",
      Authorization: "Bearer " + jwt,
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      location,
      sendScoreUpdates,
      sendChangeUpdates,
      sendNewsletter,
    }),
  });

  if (response.status === 200) {
    toast.success("Updated user settings");
    return true;
  } else if (response.status === 403) {
    toast.error("Not authorized!");
    return false;
  } else {
    toast.error("Invalid response on register: " + response.status);
    return false;
  }
}

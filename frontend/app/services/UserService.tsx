import { toast } from "react-toastify";
import { deleteCookie, getCookie, setCookie } from "cookies-next";
import { minidenticon } from "minidenticons";
import { decodeJwt } from "jose";
import { refreshAccessToken } from "./RefreshService";

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
    toast.error("An error occurred. Please try again later.");
    return false;
  }
  const json = await response.json();
  localStorage.setItem("refresh_token", json.refreshToken.token);
  setCookie("auth_token", json.accessToken.token);
  localStorage.setItem("auth_token_expire", json.accessToken.expiration);

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
    toast.error("An error occurred. Please try again later.");
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
  await refreshAccessToken();
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

export async function deleteUser(): Promise<boolean> {
  await refreshAccessToken();
  const jwt = getCookie("auth_token");

  const response = await fetch("api/account", {
    mode: "cors",
    method: "DELETE",
    headers: {
      Accept: "application/json",
      Authorization: "Bearer " + jwt,
    },
  });

  if (response.status === 200) {
    toast.success("User account deleted");
    deleteCookie("auth_token");
    return true;
  } else if (response.status === 403) {
    toast.error("Not authorized!");
    return false;
  } else {
    toast.error("An error occurred. Please try again later.");
    return false;
  }
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
  await refreshAccessToken();
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
    toast.error("An error occurred. Please try again later.");
    return false;
  }
}

export async function isAuthenticated(): Promise<boolean> {
  console.log("isAuthenticalsdkfjha");
  const authToken = getCookie("auth_token");
  console.log(`authToken: ${authToken}`);
  if (authToken === undefined) {
    return false;
  }
  return await validToken(authToken);
}

export async function validToken(jwt: string): Promise<boolean> {
  // this needs to change if the backend is located on a different url
  // sadly this can't be "api/authentication", but I dont know why
  const response = await fetch("http://127.0.0.1:8080/authentication", {
    mode: "cors",
    method: "GET",
    headers: {
      Accept: "application/json",
      Authorization: "Bearer " + jwt,
    },
  });
  console.log(response);

  if (response.status === 200) {
    console.log(response.text());
    return true;
  } else {
    return false;
  }
}
